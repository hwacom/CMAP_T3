package com.cmap.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import com.cmap.model.DataPollerSetting;
import com.cmap.plugin.module.netflow.NetFlowDAO;
import com.cmap.service.DataPollerService;
import com.cmap.service.impl.jobs.BaseJobImpl.Result;
import com.cmap.service.vo.DataPollerServiceVO;

@Service("dataPollerService")
@Transactional
public class DataPollerServiceImpl implements DataPollerService {
	@Log
	private static Logger log;

	@Autowired
	private DataPollerDAO dataPollerDAO;
	
	@Autowired
	private NetFlowDAO netFlowDAO;

	private String getTodayTableName(String interval, String tableBaseName) {
		String tableName = tableBaseName;
		/*
		 * Y181207, Ken Lin
		 * 因資料量過於龐大，拆分不同星期寫入不同TABLE，一張TABLE儲存一天資料
		 */
		Calendar cal = Calendar.getInstance();

		switch (interval) {
			case "WEEK":	//By星期紀錄
				int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);	//取得當前系統時間是星期幾 (Sunday=1、Monday=2、...)
				tableName += "_" + dayOfWeek;
				break;
		}

		return tableName;
	}

	private String getSpecifyDayTableName(String interval, String tableBaseName, String date) throws ServiceLayerException {
		String tableName = tableBaseName;
		try {
			Date queryDate = Constants.FORMAT_YYYY_MM_DD.parse(date);
			/*
			 * Y181207, Ken Lin
			 * 因資料量過於龐大，拆分不同星期寫入不同TABLE，一張TABLE儲存一天資料
			 */
			Calendar cal = Calendar.getInstance();
			cal.setTime(queryDate);

			switch (interval) {
				case "WEEK":	//By星期紀錄
					int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);	//取得當前系統時間是星期幾 (Sunday=1、Monday=2、...)
					tableName += "_" + dayOfWeek;
					break;
			}

		} catch (ParseException e) {
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

									insertData2DBByFile(targetTableName, setting, recordList, mappingMap, specialSetFieldMap);
								}
							}

							break;
					}

				} else {
					// Mode 3. 採 CSV 格式落地檔模式儲存資料，不入 DB (效能佳，彈性高)
					writeOutput2File(setting, recordListMap, specialSetFieldMap);
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
			for (Map.Entry<String, List<String>> entry : recordListMap.entrySet()) {
				totalCount += entry.getValue().size();
			}
			System.out.println("******* Finish records : " + totalCount);

			recordListMap = null;
			specialSetFieldMap = null;

			System.gc();
		}
		return dpsVO;
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

				targetFile = FileUtils.getFile(backupFileFullName);

				boolean moveFile = file.renameTo(targetFile);

				if (!moveFile) {
					throw new ServiceLayerException("移動檔案至備份資料夾失敗");
				}

			} else {
				targetFile = file;
			}

			try {
				if (StringUtils.equals(storeMethod, Constants.DATA_POLLER_STORE_METHOD_DB)) {
					switch (insertDbMethod) {
						case Constants.DATA_POLLER_INSERT_DB_BY_SQL:
							readFileContents2SQLFormat4DB(recordListMap, targetFile, setting, mappingMap);
							break;

						case Constants.DATA_POLLER_INSERT_DB_BY_CSV_FILE:
							retVO = readFileContents2CSVFormat4DB(recordListMap, targetFile, setting, mappingMap, specialSetFieldMap);
							break;
					}

				} else if (StringUtils.equals(storeMethod, Constants.DATA_POLLER_STORE_METHOD_FILE)) {
					retVO = readFileContents2CSVFormat4File(recordListMap, targetFile, setting, mappingMap, specialSetFieldMap);
				}

			} catch (Exception e) {
				log.error(e.toString(), e);

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

	private DataPollerServiceVO readFileContents2CSVFormat4DB(Map<String, List<String>> retRecordListMap, File file, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		DataPollerServiceVO retVO = new DataPollerServiceVO();

		Map<String, String> tmpSpecialFieldMap = null;
		final String dataType = setting.getDataType();

		switch (dataType) {
			case Constants.DATA_TYPE_OF_NET_FLOW:
				tmpSpecialFieldMap = composeSpecialFieldMap(setting.getSpecialVarSetting());
				break;
		}

		final Map<String, String> specialFieldMap = tmpSpecialFieldMap;

		final String splitBy = setting.getSplitSymbol();
		final String charset = StringUtils.isNotBlank(setting.getFileCharset()) ? setting.getFileCharset() : Env.DEFAULT_DATA_POLLER_FILE_CHARSET;

		try {
			List<String> fileContent = Files.readAllLines(Paths.get(file.getPath()), Charset.forName(charset));

			// 逐行讀取檔案內容
			for (String line : fileContent) {
				String[] lineData = line.split(splitBy);
				String[] targetStr = new String[mappingMap.size()];

				String recordDateTableName = null;
				String targetTableName = "";
				for (Map.Entry<Integer, DataPollerServiceVO> map : mappingMap.entrySet()) {
					final Integer targetFieldIdx = map.getKey();
					final DataPollerServiceVO data = map.getValue();
					final String isSourceColumn = data.getIsSourceColumn();
					final Integer sourceColumnIdx = data.getSourceColumnIdx();
					final String sourceColumnName = data.getSourceColumnName();
					final String sourceColumnType = data.getSourceColumnType();
					final String sourceColumnJavaFormat = data.getSourceColumnJavaFormat();
					final String sourceColumnSqlFormat = data.getSourceColumnSqlFormat();

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
								String dateYyymmdd = Constants.FORMAT_YYYY_MM_DD.format(valueDate);

								String retTableName = getSpecifyDayTableName(recordByDayInterval, targetTableName, dateYyymmdd);
								recordDateTableName = retTableName;

							} catch (ServiceLayerException e) {
								throw e;
							}
						}
						
					} else if (StringUtils.equals(setting.getRecordByMapping(), Constants.DATA_Y)) {
						if (StringUtils.equals(setting.getDataType(), Constants.DATA_TYPE_OF_NET_FLOW)) {
							/*
							 * 若是 NET_FLOW plugin，以GROUP_ID查找Mapping檔取得要寫入的TABLE名稱
							 */
							String groupId = specialFieldMap.get(Constants.GROUP_ID);
							String retTableName = netFlowDAO.findTargetTableNameByGroupId(groupId);
							
							if (StringUtils.isBlank(retTableName)) {
								throw new ServiceLayerException("群組ID(GROUP_ID)查無對照表目標寫入TABLE_NAME設定");
							}
							recordDateTableName = retTableName;
						}
					}

					if (StringUtils.equals(isSourceColumn, Constants.DATA_Y)) {
						/*
						 * 當前欄位為來源CSV內欄位 >> 塞值來源 = CSV讀取內容
						 */
						String targetValue = lineData[sourceColumnIdx];

						if (StringUtils.startsWith(sourceColumnType, Constants.FIELD_TYPE_OF_TIMESTAMP)) {
							targetValue = transSourceDateColumnFormatFromChinese2English(targetValue);
						}

						targetStr[targetFieldIdx] = targetValue;

					} else {
						if (specialFieldMap.containsKey(targetFieldName)) {
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

						specialSetFieldMap.put(targetFieldName, funcStr);

					} else if (StringUtils.equals(sourceColumnType, Constants.DATA_POLLER_SETTING_TYPE_OF_USER)) {
						String userName = "'" + Env.USER_NAME_JOB + "'";
						specialSetFieldMap.put(targetFieldName, userName);
					}
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

				sb.setLength(0);	// for GC
				targetStr = null;	// for GC
				lineData = null;	// for GC
			}

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
	private void readFileContents2SQLFormat4DB(Map<String, List<String>> retRecordListMap, File file, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap) throws ServiceLayerException {
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

		try (Stream<String> stream = Files.lines(Paths.get(file.getPath()), Charset.forName(charset))) {
			stream.forEach(line -> {
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
										String dateYyymmdd = Constants.FORMAT_YYYY_MM_DD.format(valueDate);
										recordByDayReferFieldValue = dateYyymmdd;
									}

									fieldValue = "STR_TO_DATE('" + fieldValue + "', '" + fieldSqlFormat + "')";
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

							if (fieldName.equals(recordByDayReferField)) {
								fieldValueMap.put(Constants.RECORD_BY_DAY_REFER_FIELD, recordByDayReferFieldValue);
							}

							tableFieldMap.put(tableName, fieldValueMap);
						}
					}

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
									recordDay = Constants.FORMAT_YYYY_MM_DD.format(sourceDate);
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

	private void writeOutput2File(DataPollerSetting setting, Map<String, List<String>> recordListMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
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
			throw new ServiceLayerException("輸出檔案失敗");
		}
	}

	private void insertData2DBByFile(String targetTableName, DataPollerSetting setting, List<String> csvRecords, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		try {
			/*
			 * Step 1. 將重組好的CSV語句輸出成檔案
			 */
			final String insertFileDir =
					StringUtils.isNotBlank(setting.getInsertFileDir()) ? setting.getInsertFileDir() : Env.DEFAULT_INSERT_DB_FILE_DIR;
			final String fileName = "INSERT_" + setting.getDataType() + "_" + targetTableName + "_" + Constants.FORMAT_YYYYMMDD_HH24MISS_NOSYMBOL.format(new Date()) + ".csv";

			Path targetFilePath = Paths.get(insertFileDir + File.separator + fileName);

			if (!Paths.get(insertFileDir).toFile().exists()) {
					Files.createDirectories(Paths.get(insertFileDir));
			}

			final String fileCharset = setting.getFileCharset();
			final String linesTerminatedBy = setting.getLinesTerminatedBy();

			FileUtils.writeLines(targetFilePath.toFile(), fileCharset, csvRecords, null, true);

			String extraSetStr = null;
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

			/*
			 * Step 3. 呼叫執行 LOAD DATA INFILE 指令
			 */
			final String fieldsTerminatedBy = setting.getFieldsTerminatedBy();

			String sqlFilePath = targetFilePath.toString().replace("\\", "\\\\");
			dataPollerDAO.loadDataInFile(targetTableName, sqlFilePath, fileCharset, fieldsTerminatedBy, linesTerminatedBy, extraSetStr);

		} catch (IOException e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("輸出CSV檔案失敗");
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

	@Override
	public List<String> getFieldName(String settingId, String fieldType) throws ServiceLayerException {
		List<String> retList = new ArrayList<>();
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
}
