package com.cmap.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DataPollerDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DataPollerMapping;
import com.cmap.model.DataPollerScriptSetting;
import com.cmap.model.DataPollerSetting;
import com.cmap.model.DataPollerTargetSetting;
import com.cmap.plugin.module.netflow.NetFlowDAO;
import com.cmap.plugin.module.netflow.statistics.NetFlowStatisticsService;
import com.cmap.service.DataPollerService;
import com.cmap.service.DeliveryService;
import com.cmap.service.impl.jobs.BaseJobImpl.Result;
import com.cmap.service.vo.DataPollerServiceVO;
import com.cmap.service.vo.DeliveryParameterVO;

@Service("dataPollerService")
@Transactional
public class DataPollerServiceImpl extends CommonServiceImpl implements DataPollerService {
	@Log
	private static Logger log;

	@Autowired
	private DataPollerDAO dataPollerDAO;

	@Autowired
	private NetFlowDAO netFlowDAO;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private NetFlowStatisticsService netFlowStatisticsService;

	private String getTodayTableName(String interval, String tableBaseName) {
		String tableName = tableBaseName;
		/*
		 * Y181207, Ken Lin
		 * 因資料量過於龐大，拆分不同星期寫入不同TABLE，一張TABLE儲存一天資料
		 */
		Calendar cal = Calendar.getInstance();
		tableName = getTableName(tableBaseName, interval, cal);
		return tableName;
	}

	private synchronized String getTableName(String tableName, String interval, Calendar cal) {
	    Integer tableSeq = null;
	    switch (interval) {
	        case Constants.INTERVAL_DAY_OF_MONTH:           //By日of月 (1~31)
	            tableSeq = cal.get(Calendar.DAY_OF_MONTH);
	            break;

	        case Constants.INTERVAL_DAY_OF_YEAR:            //By日of年 (1~365)
                tableSeq = cal.get(Calendar.DAY_OF_YEAR);
                break;

            case Constants.INTERVAL_WEEK:                   //By星期紀錄
                tableSeq = cal.get(Calendar.DAY_OF_WEEK);   //取得當前系統時間是星期幾 (Sunday=1、Monday=2、...)
                break;

            case Constants.INTERVAL_MONTH:                  //By月紀錄
                tableSeq = cal.get(Calendar.MONTH) + 1;     //一月=0、...
                break;
        }

	    if (tableSeq != null) {
	        tableName += "_" + StringUtils.leftPad(String.valueOf(tableSeq), 3, "0"); //TABLE流水編碼部分補0成3碼(ex:1→001)
	    }

	    return tableName;
	}

	private synchronized String getSpecifyDayTableName(String interval, String tableBaseName, String date) throws ServiceLayerException {
		String tableName = tableBaseName;
		try {
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date queryDate = sdf.parse(date);
			/*
			 * Y181207, Ken Lin
			 * 因資料量過於龐大，拆分不同星期寫入不同TABLE，一張TABLE儲存一天資料
			 */
			Calendar cal = Calendar.getInstance();
			cal.setTime(queryDate);
			tableName = getTableName(tableBaseName, interval, cal);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("轉換查詢日期成Date物件時異常，queryDate >> " + date);
		}

		return tableName;
	}

	@Override
	public DataPollerServiceVO executePolling(String settingId) throws ServiceLayerException {
		DataPollerServiceVO dpsVO = new DataPollerServiceVO();
		Map<String, String> specialSetFieldMap = new HashMap<>();

		Map<String, List<String>> recordListMap = null;
		try {
		    // 執行時間
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(new Date());
		    cal.set(Calendar.HOUR_OF_DAY, 0);
		    cal.set(Calendar.MINUTE, 0);
		    cal.set(Calendar.SECOND, 0);
		    cal.set(Calendar.MILLISECOND, 0);
		    final Date EXECUTE_DATE = cal.getTime();

			// Step 1. 查找設定檔
			DataPollerSetting setting = dataPollerDAO.findDataPollerSettingBySettingId(settingId);

			if (setting == null) {
				dpsVO.setJobExcuteResult(Result.FAILED);
				dpsVO.setJobExcuteResultRecords("0");
				dpsVO.setJobExcuteRemark("查無 DATA_POLLER_SETTING 資料 >> settingId: " + settingId);

			} else {
				// Step 2. 確認此組Data Poller是否為By天寫入資料(Record_By_Day)、以及是否有設定要By天清除資料(Record_By_Day_Clean)
				final String storeMethod = setting.getStoreMethod();
				final String recordByDay = setting.getRecordByDay();
				final String interval = setting.getRecordByDayInterval();
				final String cleanNotTodayData = setting.getRecordByDayClean();
				final String insertDBMethod = setting.getInsertDbMethod();

				final String targetSettingCode = setting.getTargetSettingCode();
				final String targetExceptionPolicy = setting.getTargetExceptionPolicy();

				if (StringUtils.equals(storeMethod, Constants.DATA_POLLER_STORE_METHOD_DB)) {
					if (StringUtils.equals(recordByDay, Constants.DATA_Y)) {
						if (StringUtils.isNotBlank(cleanNotTodayData) && StringUtils.equals(cleanNotTodayData, Constants.DATA_Y)) {
							List<String> targetTableList = dataPollerDAO.findTargetTableName(settingId);

							List<String> deleteSqls = new ArrayList<>();
							if (targetTableList != null && !targetTableList.isEmpty()) {
								for (String tableBaseName : targetTableList) {
									String sql = " DELETE FROM " + getTodayTableName(interval, tableBaseName) + " WHERE CREATE_TIME < DATE(NOW()); ";
									deleteSqls.add(sql);
								}
							}

							if (!deleteSqls.isEmpty()) {
								dataPollerDAO.deleteEntitiesByNativeSQL(deleteSqls);
							}
						}
					}
				}

				// Step 3. 組合欄位對照map
				Map<Integer, DataPollerServiceVO> mappingMap = composeMapping(setting);

				recordListMap = new HashMap<>();	// 初始化

				// Step 4. 依照設定值，取得檔案並解析內容
				DataPollerServiceVO retVO = pollingFiles(recordListMap, setting, mappingMap, specialSetFieldMap);

				boolean success = true;
				// Step 5. 依照設定判斷寫入DB or 產生落地檔
				if (StringUtils.equals(storeMethod, Constants.DATA_POLLER_STORE_METHOD_DB)) {
					switch (insertDBMethod) {
						case Constants.DATA_POLLER_INSERT_DB_BY_SQL:	// Mode 1. 直接 Insert SQL 入 DB (效能差)
							insertData2DB(recordListMap);
							break;

						case Constants.DATA_POLLER_INSERT_DB_BY_CSV_FILE:	// Mode 2. 產生 CSV 落地檔，採 Load local file 入 DB (效能佳)
							if (recordListMap == null || (recordListMap != null && recordListMap.isEmpty())) {
								dpsVO.setJobExcuteResult(Result.SUCCESS);
								dpsVO.setJobExcuteResultRecords("0");
								dpsVO.setJobExcuteRemark("無資料須解析");

								success = false;

							} else {
								for (Map.Entry<String, List<String>> entry : recordListMap.entrySet()) {
									final String targetTableName = entry.getKey();
									final List<String> recordList = entry.getValue();

									writeCsvDataFile(targetTableName, setting, recordList);
								}
							}

							break;
					}

				} else if (StringUtils.equals(storeMethod, Constants.DATA_POLLER_STORE_METHOD_FILE)) {
					// Mode 3. 採 CSV 格式落地檔模式儲存資料，不入 DB (效能佳，彈性高)
					writeOutput2File(setting, recordListMap, specialSetFieldMap);

				} else {
					if (recordListMap == null || (recordListMap != null && recordListMap.isEmpty())) {
						dpsVO.setJobExcuteResult(Result.SUCCESS);
						dpsVO.setJobExcuteResultRecords("0");
						dpsVO.setJobExcuteRemark("無資料須解析");

						success = false;
					}
				}

				// Step 6. 若有設定要執行腳本
				if (recordListMap != null && !recordListMap.isEmpty() && StringUtils.isNotBlank(targetSettingCode)) {
					// Step 6-1. 取得執行目標設備
					List<DataPollerTargetSetting> targetList = dataPollerDAO.findDataPollerTargetSettingByTargetSettingCode(targetSettingCode);

					if (targetList != null && !targetList.isEmpty()) {
						for (DataPollerTargetSetting target : targetList) {
							try {
								final String groupId = target.getGroupId();
								final String deviceId = target.getDeviceId();
								final String deviceIp = target.getDeviceIp();
								final Integer devicePort = target.getDevicePort();
								final String deviceProtocol = target.getDeviceProtocol();
								final String scriptSettingCode = target.getScriptSettingCode();

								if (StringUtils.isNotBlank(scriptSettingCode)) {
									// Step 6-2. 取得要執行的腳本
									List<DataPollerScriptSetting> scriptList = dataPollerDAO.findDataPollerScriptSettingByScriptSettingCode(scriptSettingCode);

									if (scriptList != null && !scriptList.isEmpty()) {

										DeliveryParameterVO dpVO = null;
										for (DataPollerScriptSetting script : scriptList) {
											try {
												String executeKeySetting = script.getExecuteKeySetting();
												String executeKeyType = script.getExecuteKeyType();
												List<Map<String, String>> sourceEntryMapList = retVO.getSourceEntryMapList();
												String scriptCode = script.getExecuteScriptCode();
												String reason = script.getExecuteReason();

												List<String> varKey = transSourceEntryMap2KeyList(executeKeySetting);
												List<List<String>> varValue = transSourceEntryMap2ValueList(executeKeyType, executeKeySetting, sourceEntryMapList);

												List<String> groupIds = new ArrayList<>();
												groupIds.add(groupId);

												List<String> deviceIds = new ArrayList<>();
												deviceIds.add(deviceId);

												dpVO = new DeliveryParameterVO();
												dpVO.setScriptCode(scriptCode);
												dpVO.setGroupId(groupIds);
												dpVO.setDeviceId(deviceIds);
												dpVO.setVarKey(varKey);
												dpVO.setVarValue(varValue);
												dpVO.setReason(reason);

												deliveryService.doDelivery(Env.CONNECTION_MODE_OF_DELIVERY, dpVO, true, Constants.SYS, reason, false);

											} catch (Exception e) {
												log.error(e.toString(), e);

												// 設備執行腳本失敗則跳過此設備
												break;
											}
										}
									}
								}

							} catch (Exception e) {
								log.error(e.toString(), e);

								// 發生錯誤時依照設定的POLICY決定處理方式
								if (StringUtils.isNotBlank(targetExceptionPolicy)) {
									if (StringUtils.equals(targetExceptionPolicy, Constants.EXCEPTION_POLICY_BREAK)) {
										break;

									} else if (StringUtils.equals(targetExceptionPolicy, Constants.EXCEPTION_POLICY_CONTINUE)) {
										continue;

									} else {
										break;
									}

								} else {
									break;
								}
							}
						}
					}
				}

				String dataType = setting.getDataType();

				// 若 DATA_TYPE = NET_FLOW
				if (StringUtils.equals(dataType, Constants.DATA_TYPE_OF_NET_FLOW)) {
				    // Step 7. 若有設定要統計IP流量
	                if (StringUtils.equals(Env.ENABLE_NET_FLOW_IP_STATISTICS, Constants.DATA_Y)) {
	                    if (retVO != null && retVO.getSourceEntryMapList() != null && !retVO.getSourceEntryMapList().isEmpty()) {
	                        List<Map<String, String>> sourceEntryMapList = retVO.getSourceEntryMapList();

	                        final String SOURCE_IP = Env.NET_FLOW_SOURCE_COLUMN_NAME_OF_SOURCE_IP;
	                        final String DESTINATION_IP = Env.NET_FLOW_SOURCE_COLUMN_NAME_OF_DESTINATION_IP;
	                        final String SIZE = Env.NET_FLOW_SOURCE_COLUMN_NAME_OF_SIZE;

	                        Map<String, String> specialSettingMap = composeSpecialFieldMap(setting.getSpecialVarSetting());
	                        String groupId = specialSettingMap.get(Constants.GROUP_ID);
	                        String groupSubnet = getGroupSubnetSetting(groupId, Constants.IPV4);

	                        long beginTime = System.currentTimeMillis();
	                        Map<String, Map<String, Long>> ipTrafficMap = new HashMap<>();
	                        for (Map<String, String> sourceEntryMap : sourceEntryMapList) {
	                            String sourceIP = sourceEntryMap.get(SOURCE_IP);
	                            String destinationIP = sourceEntryMap.get(DESTINATION_IP);
	                            Long size = StringUtils.isNotBlank(sourceEntryMap.get(SIZE)) ? Long.valueOf(sourceEntryMap.get(SIZE)) : 0;

	                            if (size < 0) {
	                                log.error("************ [Net_Flow_Statistic.ERROR] sourceIP: " + sourceIP + " >> destinationIP: " + destinationIP + " >> size: " + size);
	                            }
	                            // Source_IP 角度 >>> 上傳流量
	                            if ((StringUtils.equals(Env.NET_FLOW_IP_STATISTICS_ONLY_IN_GROUP, Constants.DATA_Y)
	                                    && chkIpInGroupSubnet(groupSubnet, sourceIP, Constants.IPV4)
	                                ) || !StringUtils.equals(Env.NET_FLOW_IP_STATISTICS_ONLY_IN_GROUP, Constants.DATA_Y)) {
	                                ipTrafficMap = calculateIPTraffic(ipTrafficMap, sourceIP, size, Constants.UPLOAD);
	                            }

	                            // Destination_IP 角度 >>> 下載流量
	                            if ((StringUtils.equals(Env.NET_FLOW_IP_STATISTICS_ONLY_IN_GROUP, Constants.DATA_Y)
	                                    && chkIpInGroupSubnet(groupSubnet, destinationIP, Constants.IPV4)
	                                ) || !StringUtils.equals(Env.NET_FLOW_IP_STATISTICS_ONLY_IN_GROUP, Constants.DATA_Y)) {
	                                ipTrafficMap = calculateIPTraffic(ipTrafficMap, destinationIP, size, Constants.DOWNLOAD);
	                            }
	                        }
	                        long endTime = System.currentTimeMillis();
	                        log.info("******************* NET_FLOW_IP_STATISTICS > for-loop takes " + (endTime-beginTime) + " ms");

	                        beginTime = System.currentTimeMillis();
	                        if (ipTrafficMap != null && !ipTrafficMap.isEmpty()) {
	                            // 寫入TABLE
	                            netFlowStatisticsService.calculateIpTrafficStatistics(groupId, EXECUTE_DATE, ipTrafficMap);
	                        }
	                        endTime = System.currentTimeMillis();
	                        log.info("******************* NET_FLOW_IP_STATISTICS > write-table takes " + (endTime-beginTime) + " ms");
	                    }
	                }
				}

				if (success) {
					dpsVO.setJobExcuteResult(Result.SUCCESS);

					int totalCount = 0;
					for (Map.Entry<String, List<String>> entry : recordListMap.entrySet()) {
						totalCount += entry.getValue().size();
					}
					dpsVO.setJobExcuteResultRecords(String.valueOf(totalCount));
					dpsVO.setJobExcuteRemark("Data polling success. [setting_id = " + settingId + "]");
				}
			}

		} catch (ServiceLayerException sle) {
			throw sle;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("Data polling 失敗");

		} finally {
			int totalCount = 0;
			if (recordListMap != null) {
				for (Map.Entry<String, List<String>> entry : recordListMap.entrySet()) {
					totalCount += entry.getValue().size();
				}
			}

			recordListMap = null;
			specialSetFieldMap = null;

			System.gc();
		}
		return dpsVO;
	}

	private Map<String, Map<String, Long>> calculateIPTraffic(Map<String, Map<String, Long>> ipTrafficMap, String ip, Long size, String direction) {
	    Map<String, Long> tmpMap = null;
	    if (StringUtils.isNotBlank(ip)) {
            if (ipTrafficMap.containsKey(ip)) {
                tmpMap = ipTrafficMap.get(ip);
            } else {
                tmpMap = new HashMap<>();
            }

            Long preTraffic = tmpMap.containsKey(direction) ? tmpMap.get(direction) : 0;
            Long newTraffic = preTraffic + size;

            if (preTraffic < 0 || newTraffic < 0) {
                log.error("******* [Net_Flow_Statistic.ERROR] ip: " + ip + " >> preTraffic: " + preTraffic + " >> size: " + size + " >> newTraffic: " + newTraffic);
            } else {
                tmpMap.put(direction, newTraffic);
            }

            Long preTtlTraffic = tmpMap.containsKey(Constants.TOTAL) ? tmpMap.get(Constants.TOTAL) : 0;
            Long newTtlTraffic = preTtlTraffic + size;

            if (preTtlTraffic < 0 || newTtlTraffic < 0) {
                log.error("******* [Net_Flow_Statistic.ERROR] ip: " + ip + " >> preTtlTraffic: " + preTtlTraffic + " >> size: " + size + " >> newTtlTraffic: " + newTtlTraffic);
            } else {
                tmpMap.put(Constants.TOTAL, newTtlTraffic);
            }

            ipTrafficMap.put(ip, tmpMap);
        }

	    return ipTrafficMap;
	}

	private void moveFile2ErrorFolder(File targetFile) {
	    //處理過程若有失敗，將檔案移至ERROR資料夾下
        if (targetFile != null) {
            final String fileDir = targetFile.getParentFile().getPath();
            final String errorFolder = fileDir + File.separator + "error";
            Path errorPath = Paths.get(errorFolder);

            if (!Files.exists(errorPath)) {
                try {
                    Files.createDirectories(errorPath);

                } catch (IOException ioe) {
                    log.error(ioe.toString(), ioe);
                }
            }

            try {
                final String errorFileFullName = errorFolder + File.separator + targetFile.getName();

                Files.move(Paths.get(targetFile.getPath()), Paths.get(errorFileFullName), StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e1) {
                log.error(e1.toString(), e1);
            }
        }
	}

	private Map<String, String> transKeySetting2Map(String executeKeySetting) {
		Map<String, String> retMap = new HashMap<>();
		String[] keyMappings = executeKeySetting.split(Env.COMM_SEPARATE_SYMBOL);

		for (String keyM : keyMappings) {
			String[] key = keyM.split("=");
			String scriptKey = key[0];
			String voKey = key[1];

			retMap.put(scriptKey, voKey);
		}

		return retMap;
	}

	private List<String> transSourceEntryMap2KeyList(String executeKeySetting) {
		List<String> retList = new ArrayList<>();

		Map<String, String> keyMapping = transKeySetting2Map(executeKeySetting);
		for (String scriptKey : keyMapping.keySet()) {
			retList.add(scriptKey);
		}

		return retList;
	}

	private List<List<String>> transSourceEntryMap2ValueList(String executeKeyType, String executeKeySetting, List<Map<String, String>> sourceEntryMapList) throws ServiceLayerException {
		List<List<String>> retList = new ArrayList<>();

		Map<String, String> keyMapping = transKeySetting2Map(executeKeySetting);

		List<String> entryList = null;

		if (StringUtils.equals(executeKeyType, Constants.DATA_POLLER_SCRIPT_EXECUTE_KEY_TYPE_OF_ENTRY)) {
			/*
			 * Case 1. 指令1個key對應1個sourceEntry的value
			 */
			for (Map<String, String> sourceEntry : sourceEntryMapList) {

				entryList = new ArrayList<>();
				for (String voKey : keyMapping.values()) {

					if (sourceEntry.containsKey(voKey)) {
						entryList.add(sourceEntry.get(voKey));
					}
				}

				retList.add(entryList);
			}

		} else if (StringUtils.equals(executeKeyType, Constants.DATA_POLLER_SCRIPT_EXECUTE_KEY_TYPE_OF_LIST)) {
			/*
			 * Case 2. 若指令key是要將所有sourceEntry其中的某個value組成一長串
			 * e.g.: 防火牆黑名單多組IP加入至群組內
			 * config firewall addrgrp
				    edit "Black-List"
				        set member "61.216.102.145/32" "220.135.193.215/32" <<--- 多組IP組成一長串
				end
			 */
			entryList = new ArrayList<>();
			StringBuffer sb = null;
			for (String voKey : keyMapping.values()) {
				sb = new StringBuffer();
				String[] tmp = voKey.split(",");
				String voField = tmp[0];

				// 變數值VO參數名稱使用[]包夾
				String voFieldName = voField.substring(voField.indexOf("[") + 1, voField.indexOf("]"));

				// 判斷變數值前後是否有額外固定字詞組成最終指令結果
				String frontStr = "";
				String endStr = "";
				if (StringUtils.isNotBlank(voField.split("\\[")[0])) {
					frontStr = voField.substring(0, voField.indexOf("["));

				}
				if (StringUtils.isNotBlank(voField.split("\\]")[1])) {
					endStr = voField.substring(voField.indexOf("]") + 1);
				}

				String _entrySymbol = tmp[1];	// 一組Entry包夾符號 (e.g.: "1.1.1.1/32" 使用「"」包夾)
				String _entrySeperator = tmp[2];	// 多組Entry組成一長串時中間的分隔符號 (e.g.: "1.1.1.1/32" "2.2.2.2/32" 使用「<空格>」區隔)

				for (Map<String, String> sourceEntry : sourceEntryMapList) {
					if (sourceEntry.containsKey(voFieldName)) {
						String sourceVal =
								_entrySymbol
								+ (frontStr + sourceEntry.get(voFieldName) + endStr)
								+ _entrySymbol
								+ _entrySeperator;
						sb.append(sourceVal);
					}
				}

				entryList.add(sb.toString());
				retList.add(entryList);
			}
		}

		return retList;
	}

	/**
	 * 將Data_Poller_Mapping設定的內容轉換成以Index為Key的Map
	 * @param setting
	 * @return
	 */
	private Map<Integer, DataPollerServiceVO> composeMapping(DataPollerSetting setting) {
		final String insertDbMethod = setting.getInsertDbMethod();
		List<DataPollerMapping> mappingList = dataPollerDAO.findDataPollerMappingByMappingCode(setting.getMappingCode());

		if (mappingList == null || (mappingList != null && mappingList.isEmpty())) {
			return null;

		} else {
			Map<Integer, DataPollerServiceVO> mappingMap = new TreeMap<>();

			DataPollerServiceVO vo;
			for (DataPollerMapping mapping : mappingList) {
				if (StringUtils.equals(insertDbMethod, Constants.DATA_POLLER_INSERT_DB_BY_SQL)
						&& !StringUtils.equals(mapping.getIsSourceColumn(), Constants.DATA_Y)) {
					continue;
				}
				vo = new DataPollerServiceVO();
				BeanUtils.copyProperties(mapping, vo);

				if (StringUtils.equals(insertDbMethod, Constants.DATA_POLLER_INSERT_DB_BY_SQL)) {
					mappingMap.put(mapping.getSourceColumnIdx(), vo);

				} else if (StringUtils.equals(insertDbMethod, Constants.DATA_POLLER_INSERT_DB_BY_CSV_FILE)) {
					mappingMap.put(mapping.getTargetFieldIdx(), vo);

				} else {
					mappingMap.put(mapping.getSourceColumnIdx(), vo);
				}
			}

			return mappingMap;
		}
	}

	/**
	 * 依照設定的取檔方式(Method)，抓取檔案並做後續的內容解析、組成insert SQL回傳
	 * @param setting
	 * @param mappingMap
	 * @return
	 * @throws ServiceLayerException
	 */
	private DataPollerServiceVO pollingFiles(Map<String, List<String>> recordListMap, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		DataPollerServiceVO retVO = null;
		final String method = setting.getGetSourceMethod();

		switch (method) {
			case Constants.DATA_POLLER_FILE_BY_FTP:
				break;

			case Constants.DATA_POLLER_FILE_BY_LOCAL_DIR:
				retVO = getFileByLocalDir(recordListMap, setting, mappingMap, specialSetFieldMap);
				break;
		}

		return retVO;
	}

	/**
	 * Method = LOCAL_DIR (本地端檔案夾)，取檔方法實作
	 * @param setting
	 * @param mappingMap
	 * @return
	 * @throws ServiceLayerException
	 */
	private DataPollerServiceVO getFileByLocalDir(Map<String, List<String>> recordListMap, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		DataPollerServiceVO retVO = null;

		final String storeMethod = setting.getStoreMethod();
		final String insertDbMethod = setting.getInsertDbMethod();
		final String filePath = setting.getFilePath();
		final String fileNameRegex = setting.getFileNameRegex();

		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter(fileNameRegex);

		if (!dir.exists()) {
			//資料夾不存在直接返回
			recordListMap = null;
		}

		File[] files = dir.listFiles(fileFilter);

		if (files == null || (files != null && files.length == 0)) {
			//檔案不存在直接返回
			recordListMap = null;
			return null;
		}

		Path targetFilePath = null;
		File targetFile = null;
		for (File file : files) {
			if (file == null || !file.exists()) {
				//檔案有異常則直接跳下一筆
				continue;
			}

			/*
			 * 檔案備份流程 (Backup_Source_File = "Y")
			 * 先移動至備份夾後再讀檔
			 */
			final String backupFilePath = setting.getBackupFilePath();
			final String backupFileAppendExt = Constants.FORMAT_YYYYMMDD_HH24MISS_NOSYMBOL.format(new Date());
			final String today = Constants.FORMAT_YYYY_MM_DD_NOSYMBOL.format(new Date());
			final String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
			final String fileExtName = file.getName().substring(file.getName().lastIndexOf(".") + 1);

			if (setting.getBackupSourceFile().equals(Constants.DATA_Y)) {
				//備份夾以日期再分開存放
				final String targetFolder = backupFilePath + File.separator + today;
				Path path = Paths.get(targetFolder);

				if (!Files.exists(path)) {
					try {
						Files.createDirectories(path);

					} catch (IOException e) {
						log.error(e.toString(), e);
						throw new ServiceLayerException("建立備份資料夾失敗");
					}
				}

				final String backupFileFullName = targetFolder + File.separator + fileName + "_" + backupFileAppendExt + "." + fileExtName;

				targetFilePath = Paths.get(backupFileFullName);

				boolean moveFile = false;
				boolean deleteFile = false;
				Exception moveException = null;
				int runTime = 0;
				while (runTime < 3) {
//					moveFile = file.renameTo(targetFile);
					try {
						Files.move(file.toPath(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);
						moveFile = true;
						break;

					} catch (Exception e) {
						moveException = e;
						// 移動檔案失敗retry 3次 (間格等待1秒)
						runTime++;

						try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					}
				}

				if (!moveFile) {
					throw new ServiceLayerException(
							"移動檔案至備份資料夾失敗 >>> oriFile: " + file.getPath() + ", targetFile: " + targetFilePath + " >>> Exception: " + moveException.toString()
							, moveException);

				} else {
					if (file.exists()) {
						// 判斷原始目錄下檔案是否還存在，若存在的話則嘗試做刪除
						runTime = 0;
						while (runTime < 3) {
							try {

								deleteFile = file.delete();
								if (deleteFile) {
									break;

								} else {
									// 刪除檔案失敗retry 3次 (間格等待1秒)
									runTime++;

									try {
										Thread.sleep(1000);
									} catch (InterruptedException ie) {
										ie.printStackTrace();
									}
								}

							} catch (Exception e) {
								// 刪除檔案失敗retry 3次 (間格等待1秒)
								runTime++;

								try {
									Thread.sleep(1000);
								} catch (InterruptedException ie) {
									ie.printStackTrace();
								}
							}
						}

					} else {
						deleteFile = true;
					}
				}

				targetFile = targetFilePath.toFile();

				if (!deleteFile) {
					// 原始檔案刪除不掉時，則將先前複製到backup的檔案刪除並拋錯不處理，待下一次排程啟動時在處理，避免一個檔案重複處理多次
					if (targetFile.exists()) {
						targetFile.delete();
					}
					throw new ServiceLayerException("原始檔案刪除失敗，此次排程不做處理 >>> oriFile: " + file.getPath());
				}

			} else {
				targetFile = file;
			}

			try {
				if (StringUtils.equals(storeMethod, Constants.DATA_POLLER_STORE_METHOD_DB)) {
					switch (insertDbMethod) {
						case Constants.DATA_POLLER_INSERT_DB_BY_SQL:
							retVO = readFileContents2SQLFormat4DB(recordListMap, targetFile, setting, mappingMap);
							break;

						case Constants.DATA_POLLER_INSERT_DB_BY_CSV_FILE:
							retVO = readFileContents2CSVFormat4DB(recordListMap, targetFile, setting, mappingMap, specialSetFieldMap);
							break;
					}

				} else if (StringUtils.equals(storeMethod, Constants.DATA_POLLER_STORE_METHOD_FILE)) {
					retVO = readFileContents2CSVFormat4File(recordListMap, targetFile, setting, mappingMap, specialSetFieldMap);
				}

				//塞入targetFile物件 for 後續流程發生異常時可將檔案移至error資料夾
				retVO.setTargetFile(targetFile);

			} catch (Exception e) {
				log.error(e.toString(), e);

				//處理過程若有失敗，將檔案移至ERROR資料夾下
				moveFile2ErrorFolder(targetFile);

			} finally {
				targetFile = null;	// for GC
			}
		}

		return retVO;
	}

	private Map<String, String> composeSpecialFieldMap(String specialVarSetting) {
		if (StringUtils.isBlank(specialVarSetting)) {
			return null;
		}

		Map<String, String> specialFieldMap = new HashMap<>();
		String[] fields = specialVarSetting.split(Env.COMM_SEPARATE_SYMBOL);

		if (fields != null && fields.length != 0) {
			for (String field : fields) {
				final String fName = field.split("=")[0];
				final String fValue = field.split("=")[1];

				if (StringUtils.isNotBlank(fName) && StringUtils.isNotBlank(fValue)) {
					specialFieldMap.put(fName, fValue);
				}
			}
		}
		return specialFieldMap;
	}

	private Map<String, String> processRowData(
	        String fileName, String recordDateTableName, String line,
	        DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap,
	        Map<String, String> specialFieldMap, Map<String, String> specialSetFieldMap,
	        Map<String, List<String>> retRecordListMap) {

	    Map<String, String> sourceEntryMap = null;
	    final String splitBy = setting.getSplitSymbol();

	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        sourceEntryMap = new HashMap<>();

            String[] lineData = line.split(splitBy);
            String[] targetStr = new String[mappingMap.size()];

            String targetTableName = "";
            for (Map.Entry<Integer, DataPollerServiceVO> map : mappingMap.entrySet()) {
                final Integer targetFieldIdx = map.getKey();
                final DataPollerServiceVO data = map.getValue();
                final String isSourceColumn = data.getIsSourceColumn();
                final Integer sourceColumnIdx = data.getSourceColumnIdx();
                final String sourceColumnName = data.getSourceColumnName();
                final String sourceColumnType = data.getSourceColumnType();
                final String sourceColumnJavaFormat = data.getSourceColumnJavaFormat();

                targetTableName = data.getTargetTableName();
                final String targetFieldName = data.getTargetFieldName();

                if (StringUtils.equals(setting.getRecordByDay(), Constants.DATA_Y)) {
                    if (StringUtils.equals(sourceColumnName, setting.getRecordByDayReferField())) {
                        /*
                         * 若有設定要分日期儲存，則當前處理的欄位名稱為設定的日期參考欄位
                         * >> 取得當前要寫入的資料的發生日期及目標要寫入的TABLE名稱
                         */
                        final String recordByDayInterval = setting.getRecordByDayInterval();

                        /*
                        DataPollerMapping referDateColumnMapping =
                                dataPollerDAO.findDataPollerMappingBySettingIdAndSourceColumnName(setting.getSettingId(), setting.getRecordByDayReferField());

                        if (referDateColumnMapping == null) {
                            throw new RuntimeException("Setting 中的 Record_By_Day_Refer_Field 查找不到 Mapping 資料");
                        }
                        */

                        String dataDate = lineData[sourceColumnIdx];

                        try {
                            Date valueDate = transSourceDateColumnFormat2DateObj(dataDate, sourceColumnJavaFormat);
                            String dateYyymmdd = sdf.format(valueDate);

                            String retTableName = getSpecifyDayTableName(recordByDayInterval, targetTableName, dateYyymmdd);
                            recordDateTableName = retTableName;

                        } catch (ServiceLayerException e) {
                            throw e;
                        }
                    }
                }

                if (StringUtils.equals(isSourceColumn, Constants.DATA_Y)) {
                    /*
                     * 當前欄位為來源CSV內欄位 >> 塞值來源 = CSV讀取內容
                     */
                    String targetValue = "";
                    if (StringUtils.startsWith(sourceColumnType, Constants.FIELD_TYPE_OF_TIMESTAMP)) {
                        if (sourceColumnIdx >= lineData.length) {
                            // 若日期欄位為空則預設寫入當下系統時間
                            targetValue = sdf.format(new Date());

                        } else {
                            targetValue = lineData[sourceColumnIdx];
                            targetValue = transSourceDateColumnFormatFromChinese2English(targetValue);
                        }

                    } else {
                        if (sourceColumnIdx < lineData.length) {
                            targetValue = lineData[sourceColumnIdx];
                        }
                    }

                    targetStr[targetFieldIdx] = targetValue;

                    sourceEntryMap.put(sourceColumnName, targetValue);

                } else {
                    if (specialFieldMap != null && specialFieldMap.containsKey(targetFieldName)) {
                        /*
                         * 當前欄位非來源CSV內欄位(for 目標TABLE另增加的欄位)
                         * >> 塞值來源 = 判斷是否為特殊欄位(specialFieldMap)並取值
                         */
                        targetStr[targetFieldIdx] = specialFieldMap.get(targetFieldName);

                    } else {
                        /*
                         * 非特殊欄位，當前預設不塞值 (採DB內TABLE設定的預設值)
                         */
                        targetStr[targetFieldIdx] = "";
                    }
                }

                transSpecialSetField(specialSetFieldMap, map, fileName);
            }

            StringBuffer sb = new StringBuffer();
            for (int i=0; i<targetStr.length; i++) {
                sb.append(targetStr[i]);

                if (i < targetStr.length - 1) {
                    sb.append(",");
                }
            }

            if (retRecordListMap.containsKey(recordDateTableName)) {
                retRecordListMap.get(recordDateTableName).add(sb.toString());

            } else {
                List<String> recordList = new ArrayList<>();
                recordList.add(sb.toString());

                retRecordListMap.put(recordDateTableName, recordList);
            }

            sb.setLength(0);    // for GC
            targetStr = null;   // for GC
            lineData = null;    // for GC

            return sourceEntryMap;

	    } catch (Exception e) {
	        log.error(e.toString(), e);
	        return null;

	    } finally {
	        sourceEntryMap = null;
	    }
	}

	private DataPollerServiceVO readFileContents2CSVFormat4DB(Map<String, List<String>> retRecordListMap, File file, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		DataPollerServiceVO retVO = new DataPollerServiceVO();
		List<Map<String, String>> sourceEntryMapList = new ArrayList<>();	//紀錄來源資料key-value for後續若有要執行腳本時使用

		Map<String, String> tmpSpecialFieldMap = null;
		final String dataType = setting.getDataType();

		switch (dataType) {
			case Constants.DATA_TYPE_OF_NET_FLOW:
			case Constants.DATA_TYPE_OF_FIREWALL_LOG:
				tmpSpecialFieldMap = composeSpecialFieldMap(setting.getSpecialVarSetting());
				break;
		}

		final Map<String, String> specialFieldMap = tmpSpecialFieldMap;


		final String charset = StringUtils.isNotBlank(setting.getFileCharset()) ? setting.getFileCharset() : Env.DEFAULT_DATA_POLLER_FILE_CHARSET;

		try {
//			List<String> fileContent = Files.readAllLines(Paths.get(file.getPath()), Charset.forName(charset));

			// 先取得後續要 INSERT 的目標 TABLE 名稱
			String recordDateTableName = null;
			if (StringUtils.equals(setting.getRecordByMapping(), Constants.DATA_Y)) {
                if (StringUtils.equals(setting.getDataType(), Constants.DATA_TYPE_OF_NET_FLOW)) {
                    /*
                     * 若是 NET_FLOW plugin，以GROUP_ID查找Mapping檔取得要寫入的TABLE名稱
                     * [應用Case]: 苗栗教網
                     */
                    String groupId = specialFieldMap.get(Constants.GROUP_ID);
                    String retTableName = netFlowDAO.findTargetTableNameByGroupId(groupId);

                    if (StringUtils.isBlank(retTableName)) {
                        throw new ServiceLayerException("群組ID(GROUP_ID)查無對照表目標寫入TABLE_NAME設定");
                    }
                    recordDateTableName = retTableName;

                } else if (StringUtils.equals(setting.getDataType(), Constants.DATA_TYPE_OF_FIREWALL_BLACK_LIST)) {
                    /*
                     * 防火牆黑名單記錄預設寫入table名稱
                     * [應用Case]: 桃機T1/T2
                     */
                    recordDateTableName = Env.TABLE_NAME_OF_FIREWALL_BLACK_LIST_RECORD;

                } else if (StringUtils.equals(setting.getDataType(), Constants.DATA_TYPE_OF_FIREWALL_LOG)) {
                    /*
                     * 防火牆LOG，依 Special_Var_Setting 中設定的 TARGET_TABLE 決定寫入的table名稱
                     * [應用Case]: 桃機T1/T2
                     */
                    String targetTableName = specialFieldMap.get(Constants.TARGET_TABLE);
                    recordDateTableName = targetTableName;
                }
			}

			final String targetTableName = recordDateTableName;
			final String fileName = file.getName();

			// 定義 Thread Pool 數量 (看 Setting 中 Special_Var_Setting 有無設定 THREAD_COUNT，若無則取預設值)
			String settingThreadCount = specialFieldMap != null ? specialFieldMap.get(Constants.THREAD_COUNT) : null;
			final int threadCount = StringUtils.isBlank(settingThreadCount)
			                            ? Env.THREAD_COUNT_OF_DATA_POLLER : Integer.parseInt(settingThreadCount);
			ExecutorService executor = Executors.newFixedThreadPool(threadCount);

			String settingSkipHeadLinesCount = specialFieldMap != null ? specialFieldMap.get(Constants.SKIP_HEAD_LINES_COUNT) : null;
			int skipHeadLinesCount = StringUtils.isNotBlank(settingSkipHeadLinesCount)
			                            ? Integer.parseInt(settingSkipHeadLinesCount) : 0;

			// 逐行讀取檔案內容
			long ttlSize = Files.lines(Paths.get(file.getPath()), Charset.forName(charset)).count() - skipHeadLinesCount;
			List<List<String>> sliceList = new LinkedList<>();
			List<String> tmpList = new LinkedList<>();
			int sliceSize =
			        ((new BigDecimal(ttlSize)).divide(new BigDecimal(threadCount), RoundingMode.CEILING)).intValue();
			int row = 0;

			FileInputStream fis = null;
			BufferedReader br = null;
			InputStreamReader isr = null;
			try {
			    fis = new FileInputStream(file);
			    isr = new InputStreamReader(fis, Charset.forName(charset));
			    br = new BufferedReader(isr);

			    String line;
			    while ((line = br.readLine()) != null) {
			        if (row < skipHeadLinesCount) {
			            // 若有設定要跳過前幾行的話則跳過
			            row++;
			            continue;
			        }

			        tmpList.add(line);
                    line = null;

			        if (row > 0 && (row % sliceSize == 0) || (row == ttlSize - 1)) {
	                    sliceList.add(tmpList);
	                    tmpList = null;
	                    tmpList = new LinkedList<>();
	                }

	                row++;
			    }

			} catch (Exception e) {

			} finally {
			    // 將 BufferedReader / InputStreamReader / FileInputStream 關閉，釋放資源
			    if (br != null) {
                    br.close();
                }
			    if (isr != null) {
			        isr.close();
			    }
			    if (fis != null) {
			        fis.close();
			    }

			    br = null;
			    isr = null;
			    fis = null;
			}

			for (List<String> slice : sliceList) {
			    try {
	                executor.execute(new Runnable() {
	                    @Override
	                    public void run() throws RuntimeException {
	                        try {
	                            for (String line : slice) {
	                                //紀錄來源資料key(欄位名稱)-value(來源資料數值)
	                                Map<String, String>  sourceEntryMap =
	                                        processRowData(fileName, targetTableName, line,
	                                                       setting, mappingMap, specialFieldMap,
	                                                       specialSetFieldMap, retRecordListMap);

	                                if (sourceEntryMap != null) {
	                                    sourceEntryMapList.add(sourceEntryMap);
	                                }
	                            }

	                        } catch (Exception e) {
	                            throw new RuntimeException(e.getMessage());
	                        }
	                    }
	                });

	            } catch (Exception e) {
	                log.error(e.toString(), e);
	            }
			}

			executor.shutdown();

//			long beginT = System.currentTimeMillis();
//            int preNum = 0;

			while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
			    /*
			    int listSize = sourceEntryMapList.size();

                if (listSize < ttlSize - 1) {
                    if (listSize % 1000 == 0) {
                        long endT = System.currentTimeMillis();
                        preNum = listSize + 1;
                        beginT = System.currentTimeMillis();
                    }

                } else {
                    long endT = System.currentTimeMillis();
                }
                */
			}

			retVO.setSourceEntryMapList(sourceEntryMapList);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("Data polling 失敗");

		} finally {
			tmpSpecialFieldMap = null;	// for GC
		}

		return retVO;
	}

	/**
	 * 依照設定的編碼方式(FileCharset)讀取檔案內容，並依照設定的切割符號(SplitSymbol)切割內容，再依照先前轉換好的mappingMap轉置內容
	 * @param file
	 * @param setting
	 * @param mappingMap
	 * @return
	 */
	private DataPollerServiceVO readFileContents2SQLFormat4DB(Map<String, List<String>> retRecordListMap, File file, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap) throws ServiceLayerException {
		DataPollerServiceVO retVO = new DataPollerServiceVO();
		List<Map<String, String>> sourceEntryMapList = new ArrayList<>();	//紀錄來源資料key-value for後續若有要執行腳本時使用

		Map<String, String> tmpSpecialFieldMap = null;
		final String dataType = setting.getDataType();

		switch (dataType) {
			case Constants.DATA_TYPE_OF_NET_FLOW:
				tmpSpecialFieldMap = composeSpecialFieldMap(setting.getSpecialVarSetting());
				break;
		}

		final Map<String, String> specialFieldMap = tmpSpecialFieldMap;

		final String firstColumnNameOfTitle = mappingMap.get(0).getSourceColumnName();
		final String splitBy = setting.getSplitSymbol();
		final String charset = StringUtils.isNotBlank(setting.getFileCharset()) ? setting.getFileCharset() : Env.DEFAULT_DATA_POLLER_FILE_CHARSET;

		final String recordByDayReferField = setting.getRecordByDayReferField();
		final String fileName = file.getName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		try (Stream<String> stream = Files.lines(Paths.get(file.getPath()), Charset.forName(charset))) {
			stream.forEach(line -> {
				Map<String, String> sourceEntryMap = new HashMap<>();	//紀錄來源資料key(欄位名稱)-value(來源資料數值)

				String[] lineData = line.split(splitBy);
				Map<String, Map<String, String>> tableFieldMap = new HashMap<>();

				if (lineData != null && lineData.length > 0) {
					if (lineData[0].equals(firstColumnNameOfTitle)) {
						return;
					}

					String recordByDayReferFieldValue = "";
					for (int i=0; i<lineData.length; i++) {
						if (mappingMap.containsKey(i)) {
							final String tableName = mappingMap.get(i).getTargetTableName();
							final String fieldName = mappingMap.get(i).getTargetFieldName();
							final String fieldType = mappingMap.get(i).getSourceColumnType();
							final String fieldJavaFormat = mappingMap.get(i).getSourceColumnJavaFormat();
							final String fieldSqlFormat = mappingMap.get(i).getSourceColumnSqlFormat();

							String fieldValue = lineData[i];

							switch (fieldType) {
								case Constants.FIELD_TYPE_OF_VARCHAR:
									fieldValue = "'" + fieldValue + "'";
									break;

								case Constants.FIELD_TYPE_OF_TIMESTAMP:
									if (fieldName.equals(recordByDayReferField)) {
										Date valueDate = transSourceDateColumnFormat2DateObj(fieldValue, fieldJavaFormat);
										String dateYyymmdd = sdf.format(valueDate);
										recordByDayReferFieldValue = dateYyymmdd;
									}

									fieldValue = "STR_TO_DATE('" + fieldValue + "', '" + fieldSqlFormat + "')";
									break;

								case Constants.DATA_POLLER_SETTING_TYPE_OF_USER:
									fieldValue = "'" + Env.USER_NAME_JOB + "'";
									break;

								case Constants.DATA_POLLER_SETTING_TYPE_OF_FILE_NAME:
									fieldValue = "'" + fileName + "'";
									break;

								default:
									break;
							}

							Map<String, String> fieldValueMap = null;
							if (tableFieldMap.containsKey(tableName)) {
								fieldValueMap = tableFieldMap.get(tableName);

							} else {
								fieldValueMap = new HashMap<>();

								if (specialFieldMap != null && !specialFieldMap.isEmpty()) {
									fieldValueMap.putAll(specialFieldMap);
								}
							}

							fieldValueMap.put(fieldName, fieldValue);
							sourceEntryMap.put(fieldName, fieldValue);

							if (fieldName.equals(recordByDayReferField)) {
								fieldValueMap.put(Constants.RECORD_BY_DAY_REFER_FIELD, recordByDayReferFieldValue);
							}

							tableFieldMap.put(tableName, fieldValueMap);
						}
					}

					sourceEntryMapList.add(sourceEntryMap);

					try {
						transTableFieldMap2Sql(retRecordListMap, tableFieldMap, setting);

					} catch (ServiceLayerException sle) {
						throw new RuntimeException(sle);
					}
				}
			});

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("Data polling 失敗");

		}

		retVO.setSourceEntryMapList(sourceEntryMapList);
		return retVO;
	}

	private String getTargetFieldTitle(Map<Integer, DataPollerServiceVO> mappingMap) {
		StringBuffer retSb = new StringBuffer();

		for (Map.Entry<Integer, DataPollerServiceVO> entry : mappingMap.entrySet()) {
			final String targetFieldName = entry.getValue().getTargetFieldName();

			retSb.append(targetFieldName).append(",");
		}

		return retSb.toString().substring(0, retSb.length()-1);
	}

	private DataPollerServiceVO readFileContents2CSVFormat4File(Map<String, List<String>> retRecordListMap, File file, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		DataPollerServiceVO retVO = new DataPollerServiceVO();
		specialSetFieldMap = composeSpecialFieldMap(setting.getSpecialVarSetting());

		long start = System.currentTimeMillis();

		final String seperator = setting.getFieldsTerminatedBy();	// 檔案內容欄位分隔符號
		final String retOriFileName = file.getName();
		retVO.setRetOriFileName(retOriFileName);

		SimpleDateFormat dateFormat;
		SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		BufferedReader br = null;
		try {
			final String recordByDayReferField = setting.getRecordByDayReferField(); // By日期拆檔案所使用的參考日期欄位
			String charset = setting.getFileCharset();	// 檔案編碼

	        br = new BufferedReader(
	                new InputStreamReader(new FileInputStream(file.getPath()), charset));

	        String[] fields = null;
	        StringBuffer csvLine = new StringBuffer();
	        while(br.ready())
	        {
	        	String line = br.readLine();
	        	fields = line.split(seperator);

	        	if (fields != null) {
	        		// Clean StringBuffer
	        		csvLine.setLength(0);

	        		String recordDay = null;
	        		for (Map.Entry<Integer, DataPollerServiceVO> entry : mappingMap.entrySet()) {
	        			final DataPollerServiceVO dpsVO = entry.getValue();

	        			boolean isSourceColumn = StringUtils.equals(dpsVO.getIsSourceColumn(), Constants.DATA_Y) ? true : false;
	        			String sourceColumnType = dpsVO.getSourceColumnType();
	        			String sourceColumnName = dpsVO.getSourceColumnName();

	        			String targetValue = "";
	        			if (isSourceColumn) {
	        				/*
	        				 * 如果是來源檔案內欄位，針對日期欄位做格式轉換，其餘則照來源值塞入
	        				 */
	        				int sourceColumnIdx = dpsVO.getSourceColumnIdx();
	        				targetValue = (sourceColumnIdx < fields.length) ? fields[sourceColumnIdx] : "";

	        				if (StringUtils.startsWith(sourceColumnType, Constants.FIELD_TYPE_OF_TIMESTAMP)) {
								targetValue = transSourceDateColumnFormatFromChinese2English(targetValue);

								dateFormat = new SimpleDateFormat(dpsVO.getSourceColumnJavaFormat(), Locale.ENGLISH);
								Date sourceDate = dateFormat.parse(targetValue);

								String adjDateSymbol = sourceColumnType.indexOf("+") != -1
															? "+"
															: sourceColumnType.indexOf("-") != -1 ? "-" : null;
								if (adjDateSymbol != null) {
									// 判斷此日期欄位是否有設定要加減時間
									String adjDateStr = sourceColumnType.split("\\" + adjDateSymbol)[1];
									String adjHour = null;
									String adjMinute = null;
									String adjSecond = null;

									if (StringUtils.contains(adjDateStr, ":")) {
										adjHour = adjDateStr.split(":")[0];
										adjMinute = adjDateStr.split(":")[1];
										adjSecond = adjDateStr.split(":")[2];

									} else {
										adjHour = adjDateStr;
									}

									sourceDate = DateUtils.addHours(sourceDate, adjHour == null ? 0 : Integer.parseInt(adjHour));
									sourceDate = DateUtils.addMinutes(sourceDate, adjMinute == null ? 0 : Integer.parseInt(adjMinute));
									sourceDate = DateUtils.addSeconds(sourceDate, adjSecond == null ? 0 : Integer.parseInt(adjSecond));
								}

								targetValue = Constants.FORMAT_YYYYMMDD_HH24MISS.format(sourceDate);

								if (StringUtils.equals(sourceColumnName, recordByDayReferField)) {
									recordDay = sdf_yyyyMMdd.format(sourceDate);
			        			}
							}

	        			} else {
	        				/*
	        				 * 如果是額外新增的欄位，判斷target_field_type:空值表示不入target file(跳過)
	        				 */
	        				String targetFieldType = dpsVO.getTargetFieldType();

	        				if (StringUtils.isBlank(targetFieldType)) {
	        					continue;
	        				}

	        				String targetFieldName = dpsVO.getTargetFieldName();
	        				String targetValueFormat = dpsVO.getTargetValueFormat();

	        				switch (targetValueFormat) {
	        					case Constants.DATA_POLLER_TARGET_VALUE_FORMAT_OF_NOW:
	        						targetValue = Constants.FORMAT_YYYYMMDD_HH24MISS.format(new Date());
	        						break;

	        					case Constants.DATA_POLLER_TARGET_VALUE_FORMAT_OF_USER:
	        						targetValue = Env.USER_NAME_JOB;
	        						break;

	        					case Constants.DATA_POLLER_TARGET_VALUE_FORMAT_OF_SPECIAL_VAR:
		        					targetValue = specialSetFieldMap.containsKey(targetFieldName) ? specialSetFieldMap.get(targetFieldName) : targetValue;
	        						break;

	        					case Constants.DATA_POLLER_TARGET_VALUE_FORMAT_OF_FILE_NAME:
	        						targetValue = retOriFileName;
	        						break;
	        				}
	        			}

	        			csvLine.append(targetValue).append(",");
	        		}

	        		if (retRecordListMap.containsKey(recordDay)) {
	        			retRecordListMap.get(recordDay).add(csvLine.toString().substring(0, csvLine.length()-1));

	        		} else {
	        			List<String> recordList = new ArrayList<>();
	        			recordList.add(csvLine.toString().substring(0, csvLine.length()-1));

	        			retRecordListMap.put(recordDay, recordList);
	        		}
	        	}
	        }

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("解析檔案過程發生異常 >> " + e.toString());

		} finally {
	        try {
				br.close();
			} catch (IOException e) {
				log.error(e.toString(), e);
			}

	        long end = System.currentTimeMillis();
	        log.info("DataPollerService.readFileContents2CSVFormat4File,使用記憶體="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())+",使用時間毫秒="+(end-start));
		}

		return retVO;
	}

	private String transSourceDateColumnFormatFromChinese2English(String sourceDateStrInChinese) {
		if (sourceDateStrInChinese.contains(Constants.TIME_AM_CHINESE_WORD)) {
			sourceDateStrInChinese = sourceDateStrInChinese.replace(Constants.TIME_AM_CHINESE_WORD, "");
			sourceDateStrInChinese += " " + Constants.TIME_AM_ENGLISH_WORD;

		} else if (sourceDateStrInChinese.contains(Constants.TIME_PM_CHINESE_WORD)) {
			sourceDateStrInChinese = sourceDateStrInChinese.replace(Constants.TIME_PM_CHINESE_WORD, "");
			sourceDateStrInChinese += " " + Constants.TIME_PM_ENGLISH_WORD;
		}

		return sourceDateStrInChinese;
	}

	private Date transSourceDateColumnFormat2DateObj(String sourceDateStr, String fieldJavaFormat) {
		Date retDate = null;

		sourceDateStr = transSourceDateColumnFormatFromChinese2English(sourceDateStr);

		SimpleDateFormat sdf = new SimpleDateFormat(fieldJavaFormat, Locale.ENGLISH);
		try {
			retDate = sdf.parse(sourceDateStr);

		} catch (ParseException e) {
			log.error(e.toString(), e);
			throw new RuntimeException("轉換來源檔案中 Record_By_Day_Refer_Field 的值成 Date 物件時異常 >> sourceDateStr: " + sourceDateStr + ", javaFormat: " + fieldJavaFormat);
		}

		return retDate;
	}

	/**
	 * 將檔案內容暫存至Map的結果轉換成Insert SQL
	 * @param tableFieldMap
	 * @return
	 * @throws ServiceLayerException
	 */
	private void transTableFieldMap2Sql(Map<String, List<String>> retRecordListMap, Map<String, Map<String, String>> tableFieldMap, DataPollerSetting setting) throws ServiceLayerException {
		StringBuffer insertSql = new StringBuffer();
		StringBuffer fieldSql = new StringBuffer();
		StringBuffer valueSql = new StringBuffer();

		for (Map.Entry<String, Map<String, String>> tableEntry : tableFieldMap.entrySet()) {
			String tableName = tableEntry.getKey();
			final Map<String, String> fieldValueMap = tableEntry.getValue();

			if (StringUtils.equals(setting.getRecordByDay(), Constants.DATA_Y)) {
				final String recordByDayInterval = setting.getRecordByDayInterval();
				final String recordByDayReferField = Constants.RECORD_BY_DAY_REFER_FIELD;

				String dataDate = null;
				if (!fieldValueMap.containsKey(recordByDayReferField)) {
					throw new ServiceLayerException("Setting 中的 Record_By_Day_Refer_Field 對應不到 Map 中欄位");

				} else {
					dataDate = fieldValueMap.get(recordByDayReferField);
				}

				tableName = getSpecifyDayTableName(recordByDayInterval, tableName, dataDate);
			}

			insertSql.append("INSERT INTO ").append(tableName);
			fieldSql.append(" ( ");
			valueSql.append(" VALUES ( ");

			int idx = 0;
			for (Map.Entry<String, String> fieldEntry : fieldValueMap.entrySet()) {
				final String fieldName = fieldEntry.getKey();
				final String fieldValue = fieldEntry.getValue();

				if (fieldName.equals(Constants.RECORD_BY_DAY_REFER_FIELD)) {
					idx++;
					continue;
				}

				fieldSql.append(fieldName);
				valueSql.append(fieldValue);

				if (idx < fieldValueMap.size() - 1) {
					fieldSql.append(", ");
					valueSql.append(", ");

				} else {
					fieldSql.append(" ) ");
					valueSql.append(" ) ");
				}

				idx++;
			}

			insertSql.append(fieldSql).append(valueSql).append(";");

			if (retRecordListMap.containsKey(tableName)) {
				retRecordListMap.get(tableName).add(insertSql.toString());

			} else {
				List<String> insertSqlList = new ArrayList<>();
				insertSqlList.add(insertSql.toString());

				retRecordListMap.put(tableName, insertSqlList);
			}

			insertSql.setLength(0);	//for GC
			fieldSql.setLength(0);	//for GC
			valueSql.setLength(0);	//for GC
		}
	}

	private void writeOutput2File(DataPollerSetting setting, Map<String, List<String>> recordListMap, Map<String, String> specialSetFieldMap)
	        throws ServiceLayerException {
		try {
			specialSetFieldMap = composeSpecialFieldMap(setting.getSpecialVarSetting());

			final String schoolId = specialSetFieldMap.get("SCHOOL_ID");
			final String dataType = setting.getDataType();

			for (Map.Entry<String, List<String>> entry : recordListMap.entrySet()) {
				/*
				 * 組成落地檔案名稱
				 */
				String fileName = setting.getStoreFileNameFormat();
				String yyyyMMdd = entry.getKey();

				if (StringUtils.contains(fileName, Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_SCHOOL_ID)) {
					fileName = fileName.replace(Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_SCHOOL_ID, schoolId);
				}
				if (StringUtils.contains(fileName, Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_YYYYMMDD)) {
					fileName = fileName.replace(Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_YYYYMMDD, yyyyMMdd);
				}
				if (StringUtils.contains(fileName, Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_FILE_EXT)) {
					String extName = Env.FILE_EXTENSION_NAME_OF_NET_FLOW;
					switch (dataType) {
						case Constants.DATA_TYPE_OF_NET_FLOW:
							extName = Env.NET_FLOW_OUTPUT_FILE_EXT_NAE;
					}
					fileName = fileName.replace(Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_FILE_EXT, extName);
				}

				String storeFileDir = setting.getStoreFileDir();
				Path storePath = Paths.get(storeFileDir + File.separator + schoolId + File.separator + fileName);

				List<String> recordList = entry.getValue();
				dataPollerDAO.insertEntities2File(storePath, recordList, true);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("輸出檔案失敗 (" + e.getMessage() + ")");
		}
	}

	private void writeCsvDataFile(String targetTableName, DataPollerSetting setting, List<String> csvRecords) throws ServiceLayerException {
		try {
			/*
			 * Step 1. 將重組好的CSV語句輸出成檔案
			 */
			final String insertFileDir =
					StringUtils.isNotBlank(setting.getInsertFileDir()) ? setting.getInsertFileDir() : Env.DEFAULT_INSERT_DB_FILE_DIR;
			final String fileName = "INSERT_" + setting.getDataType() + "_[" + targetTableName + "]_" + Constants.FORMAT_YYYYMMDD_HH24MISS_NOSYMBOL.format(new Date()) + ".csv";

			Path targetFilePath = Paths.get(insertFileDir + File.separator + fileName);

			if (!Paths.get(insertFileDir).toFile().exists()) {
				Files.createDirectories(Paths.get(insertFileDir));
			}

			final String fileCharset = setting.getFileCharset();

			FileUtils.writeLines(targetFilePath.toFile(), fileCharset, csvRecords, null, true);

		} catch (IOException e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("輸出CSV檔案失敗 (" + e.getMessage() + ")");
		}
	}

	/**
	 * 若有設定特殊欄位處理，呼叫此方法取得特殊欄位設定SQL語句
	 * @param specialSetFieldMap
	 * @param mappingMap
	 * @return
	 */
	private String composeExtraSetStr(Map<String, String> specialSetFieldMap, Map<Integer, DataPollerServiceVO> mappingMap)
	        throws ServiceLayerException {
	    String extraSetStr = null;
	    try {
            /*
             * Step 2. 判斷是否有特殊欄位(e.g. Timestamp)需做SET語句轉換
             */
            if (specialSetFieldMap != null && !specialSetFieldMap.isEmpty()) {
                StringBuffer fields = new StringBuffer();
                StringBuffer sets = new StringBuffer();

                fields.append(" ( ");
                sets.append(" SET ");

                /*
                 * LOAD DATA INFILE '~/infile.txt'
                    INTO TABLE People
                    FIELDS TERMINATED BY ','
                    LINES TERMINATED BY '\n'
                    (@DATE, NAME)
                    SET DATE = NOW();
                 */
                int idxMappingMap = 0;
                int idxSpecialMap = 0;
                for (Map.Entry<Integer, DataPollerServiceVO> entry : mappingMap.entrySet()) {
                    String fieldName = entry.getValue().getTargetFieldName();

                    if (specialSetFieldMap.containsKey(fieldName)) {
                        String fieldFunc = specialSetFieldMap.get(fieldName);

                        fields.append(" @`").append(fieldName).append("` ");
                        sets.append(fieldName).append(" = ").append(fieldFunc);

                        if (idxSpecialMap < specialSetFieldMap.size() - 1) {
                            sets.append(", ");
                            idxSpecialMap++;
                        }

                    } else {
                        fields.append(" `").append(fieldName).append("` ");
                    }

                    if (idxMappingMap < mappingMap.size() - 1) {
                        fields.append(", ");
                    }
                    if (idxMappingMap == mappingMap.size() - 1) {
                        fields.append(" ) ");
                    }

                    idxMappingMap++;
                }

                extraSetStr = fields.toString() + sets.toString();
            }

	    } catch (Exception e) {
	        log.error(e.toString(), e);
	        throw new ServiceLayerException("處理特殊欄位異常 (" + e.getMessage() + ")");
	    }

	    return extraSetStr;
	}

	/**
	 * 執行CSV檔寫入DB
	 * @param setting
	 * @param targetFilePath
	 * @param targetTableName
	 * @param extraSetStr
	 */
	private void insertData2DBByFile(DataPollerSetting setting, String targetFilePath, String targetTableName, String extraSetStr)
	        throws ServiceLayerException {
	    try {
	        final String fileCharset = setting.getFileCharset();
	        final String linesTerminatedBy = setting.getLinesTerminatedBy();
	        final String targetDB = setting.getTargetDb();

            /*
             * Step 3. 呼叫執行 LOAD DATA INFILE 指令
             */
            final String fieldsTerminatedBy = setting.getFieldsTerminatedBy();

            String sqlFilePath = targetFilePath.toString().replace("\\", "\\\\");
            dataPollerDAO.loadDataInFile(targetDB, targetTableName, sqlFilePath, fileCharset, fieldsTerminatedBy, linesTerminatedBy, extraSetStr);

	    } catch (Exception e) {
	        log.error(e.toString(), e);
	        throw new ServiceLayerException("insert DB 異常 (" + targetFilePath + ")(" + e.getMessage() + ")");
	    }
	}

	/**
	 * 執行Insert SQL實作
	 * @param sqls
	 */
	private void insertData2DB(Map<String, List<String>> sqlMap) {
		for (Map.Entry<String, List<String>> entry : sqlMap.entrySet()) {
			dataPollerDAO.insertEntitiesByNativeSQL(entry.getValue());
		}
	}

	private void deleteInsertCsvFile(String targetFilePath) {
	    try {
	        Files.deleteIfExists(Paths.get(targetFilePath));

	    } catch (Exception e) {
	        log.error(e.toString(), e);
	    }
	}

	@Override
	public List<String> getFieldName(String settingId, String fieldType) throws ServiceLayerException {
		List<String> retList = new LinkedList<>();
		try {
			DataPollerSetting setting = dataPollerDAO.findDataPollerSettingBySettingId(settingId);

			if (setting != null) {
				List<DataPollerMapping> mappingList = dataPollerDAO.findDataPollerMappingByMappingCode(setting.getMappingCode());

				if (mappingList != null && !mappingList.isEmpty()) {
					mappingList.forEach(mapping -> {
						if (StringUtils.equals(mapping.getShowFlag(), Constants.DATA_Y)) {
							retList.add(
									fieldType.equals(FIELD_TYPE_SOURCE)
										? mapping.getSourceColumnName()
										: mapping.getTargetFieldName());
						}
					});
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢異常，請重新操作");
		}
		return retList;
	}

	@Override
	public String getStoreMethodByDataType(String dataType) throws ServiceLayerException {
		String retVal = "";
		try {
			List<DataPollerSetting> settings = dataPollerDAO.findDataPollerSettingByDataType(dataType);

			if (settings == null || (settings != null && settings.isEmpty())) {
				if (!StringUtils.equals(dataType, Constants.DATA_STAR_SYMBOL)) {
					return getStoreMethodByDataType(Constants.DATA_STAR_SYMBOL);
				}

			} else {
				retVal = settings.get(0).getStoreMethod();
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("取得 STORE_METHOD 異常");
		}
		return retVal;
	}

	@Override
	public String getRecordBySetting(String dataType) throws ServiceLayerException {
		String retVal = "";
		try {
			List<DataPollerSetting> settings = dataPollerDAO.findDataPollerSettingByDataType(dataType);

			if (settings == null || (settings != null && settings.isEmpty())) {
				if (!StringUtils.equals(dataType, Constants.DATA_STAR_SYMBOL)) {
					return getRecordBySetting(Constants.DATA_STAR_SYMBOL);
				}

			} else {
				DataPollerSetting setting = settings.get(0);

				if (StringUtils.equals(setting.getRecordByDay(), Constants.DATA_Y)) {
					retVal = Constants.RECORD_BY_DAY;

				} else  if (StringUtils.equals(setting.getRecordByMapping(), Constants.DATA_Y)) {
					retVal = Constants.RECORD_BY_MAPPING;
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("取得 RECORD_BY 異常");
		}
		return retVal;
	}

	private Map<String, String> composeSpecialSetFieldMap(Map<Integer, DataPollerServiceVO> mappingMap, String fileName) {
	    Map<String, String> specialSetFieldMap = new HashMap<>();

	    for (Map.Entry<Integer, DataPollerServiceVO> map : mappingMap.entrySet()) {
            transSpecialSetField(specialSetFieldMap, map, fileName);
        }

	    return specialSetFieldMap;
	}

	private void transSpecialSetField(Map<String, String> specialSetFieldMap, Map.Entry<Integer, DataPollerServiceVO> map, String fileName) {
        final DataPollerServiceVO data = map.getValue();
        final String isSourceColumn = data.getIsSourceColumn();
        final String sourceColumnType = data.getSourceColumnType();
        final String sourceColumnSqlFormat = data.getSourceColumnSqlFormat();
        final String targetFieldName = data.getTargetFieldName();
        final String targetFieldType = data.getTargetFieldType();

	    /*
         * 判斷當前欄位型態是否為日期
         */
        if (StringUtils.startsWith(sourceColumnType, Constants.DATA_POLLER_SETTING_TYPE_OF_TIMESTAMP)) {
            String funcStr = "STR_TO_DATE(@`" + targetFieldName + "`, '" + sourceColumnSqlFormat + "') ";

            if (!StringUtils.equals(isSourceColumn, Constants.DATA_Y)) {
                //非原始CSV檔內欄位(e.g. Create_Time、Update_Time)，一律以 NOW()函式(設定在Source_Column_SQL_Format欄位)取得當下日期時間即可
                funcStr = sourceColumnSqlFormat;

            } else {
                /*
                 * 判斷是否有設定要 ADDTIME
                 * (e.g. TIMESTAMP+0:8:0 >> 表示要加8小時)
                 */
                if (StringUtils.contains(sourceColumnType, "+")) {
                    String[] str = sourceColumnType.split("\\+");
                    String addTime = str[1];

                    funcStr = "ADDTIME(" + funcStr + ", \"" + addTime + "\")";
                }
            }

            if (StringUtils.equals(targetFieldType, Constants.FIELD_TYPE_OF_VARCHAR)) {
                // 時間欄位轉成字串寫入DB
                String targetValueFormat = data.getTargetValueFormat();
                funcStr = "DATE_FORMAT(" + funcStr + ", '" + targetValueFormat + "')";
            }

            specialSetFieldMap.put(targetFieldName, funcStr);

        } else if (StringUtils.equals(sourceColumnType, Constants.DATA_POLLER_SETTING_TYPE_OF_USER)) {
            String userName = "'" + Env.USER_NAME_JOB + "'";
            specialSetFieldMap.put(targetFieldName, userName);

        } else if (StringUtils.equals(sourceColumnType, Constants.DATA_POLLER_SETTING_TYPE_OF_FILE_NAME)) {
            String sqlFileName = "'" + fileName + "'";
            specialSetFieldMap.put(targetFieldName, sqlFileName);
        }
	}

    @Override
    public DataPollerServiceVO saveData2DB(String settingId) throws ServiceLayerException {
        DataPollerServiceVO dpsVO = new DataPollerServiceVO();
        try {
            // Step 1. 查找設定檔
            DataPollerSetting setting = dataPollerDAO.findDataPollerSettingBySettingId(settingId);

            if (setting == null) {
                dpsVO.setJobExcuteResult(Result.FAILED);
                dpsVO.setJobExcuteResultRecords("0");
                dpsVO.setJobExcuteRemark("Setting_ID (" + settingId + ") 取不到設定");

                return dpsVO;
            }

            final String insertFileDir = setting.getInsertFileDir();

            // Step 2. 依設定值，取得待寫入的檔案清單
            File insertDir = Paths.get(insertFileDir).toFile();
            File[] files = insertDir.listFiles();

            if (files == null || (files != null && files.length == 0)) {
                dpsVO.setJobExcuteResult(Result.SUCCESS);
                dpsVO.setJobExcuteResultRecords("0");
                dpsVO.setJobExcuteRemark("無檔案需處理");

                return dpsVO;
            }

            // Step 3. 取得特殊欄位處理設定SQL語句
            final Map<Integer, DataPollerServiceVO> mappingMap = composeMapping(setting);

            if (mappingMap == null) {
                dpsVO.setJobExcuteResult(Result.FAILED);
                dpsVO.setJobExcuteResultRecords("0");
                dpsVO.setJobExcuteRemark("Mapping_Code (" + setting.getMappingCode() + ") 取不到 Data_Poller_Mapping 資料");

                return dpsVO;
            }

            final Map<String, String> specialSetFieldMap = composeSpecialSetFieldMap(mappingMap, null);
            final String extraSetStr = composeExtraSetStr(specialSetFieldMap, mappingMap);
            final Map<String, String> specialFieldMap = composeSpecialFieldMap(setting.getSpecialVarSetting());

            // Step 4. 依照設定，決定使用 single / multi thread 執行寫入動作
            final Integer threadCount = Integer.parseInt(Objects.toString(specialFieldMap.get(Constants.THREAD_COUNT), "1"));

            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            // 排序檔案，最早進來的檔案先處理
            Arrays.sort(files, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));

            int ttlCount = files.length;
            int errorCount = 0;
            for (File f : files) {
                final String targetFilePath = f.getPath();
                final String fileName = f.getName();

                if (fileName.indexOf("[") == -1 || fileName.indexOf("]") == -1) {
                    // 檔案名稱格式不正確，無法解析出 targetTableName
                    continue;
                }

                final String targetTableName = fileName.split("\\[")[1].split("\\]")[0];

                try {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() throws RuntimeException {
                            try {
                                // Step 1. 執行CSV檔資料寫入DB
                                insertData2DBByFile(setting, targetFilePath, targetTableName, extraSetStr);

                                // Step 2. 刪除CSV檔案
                                deleteInsertCsvFile(targetFilePath);

                            } catch (Exception e) {
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    errorCount++;
                }
            }

            executor.shutdown();

            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            }

            String remark = "共處理 " + ttlCount + " 筆檔案。成功 " + (ttlCount - errorCount) + " 筆；失敗 " + errorCount + " 筆";
            dpsVO.setJobExcuteResultRecords(String.valueOf(ttlCount - errorCount));
            dpsVO.setJobExcuteRemark(remark);

            if (errorCount == 0) {
                dpsVO.setJobExcuteResult(Result.SUCCESS);

            } else if (ttlCount == errorCount) {
                dpsVO.setJobExcuteResult(Result.FAILED);

            } else {
                dpsVO.setJobExcuteResult(Result.PARTIAL_SUCCESS);
            }

        } catch (ServiceLayerException sle) {
            dpsVO.setJobExcuteResult(Result.FAILED);
            dpsVO.setJobExcuteResultRecords("0");
            dpsVO.setJobExcuteRemark(sle.getMessage());

        } catch (Exception e) {
            log.error(e.toString(), e);

            dpsVO.setJobExcuteResult(Result.FAILED);
            dpsVO.setJobExcuteResultRecords("0");
            dpsVO.setJobExcuteRemark(e.toString());
        }
        return dpsVO;
    }
}
