package com.cmap.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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

		List<String> recordList = null;
		try {
			// Step 1. 查找設定檔
			DataPollerSetting setting = dataPollerDAO.findDataPollerSettingBySettingId(settingId);

			if (setting == null) {
				dpsVO.setJobExcuteResult(Result.FAILED);
				dpsVO.setJobExcuteResultRecords("0");
				dpsVO.setJobExcuteRemark("查無 DATA_POLLER_SETTING 資料 >> settingId: " + settingId);

			} else {
				// Step 2. 確認此組Data Poller是否為By天寫入資料(Record_By_Day)、以及是否有設定要By天清除資料(Record_By_Day_Clean)
				final String recordByDay = setting.getRecordByDay();
				final String interval = setting.getRecordByDayInterval();
				final String cleanNotTodayData = setting.getRecordByDayClean();
				final String insertDBMethod = setting.getInsertDbMethod();

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

				// Step 3. 組合欄位對照map
				Map<Integer, DataPollerServiceVO> mappingMap = composeMapping(setting);

				recordList = new ArrayList<>();	// 初始化

				// Step 4. 依照設定值，取得檔案並解析內容
				String retVal = pollingFiles(recordList, setting, mappingMap, specialSetFieldMap);

				// Step 5. 寫入DB
				switch (insertDBMethod) {
					case Constants.DATA_POLLER_INSERT_DB_BY_SQL:
						insertData2DB(recordList);
						break;

					case Constants.DATA_POLLER_INSERT_DB_BY_CSV_FILE:
						final String targetTableName = retVal;
						insertData2DBByFile(targetTableName, setting, recordList, specialSetFieldMap);
						break;
				}

				dpsVO.setJobExcuteResult(Result.SUCCESS);
				dpsVO.setJobExcuteResultRecords(recordList != null ? String.valueOf(recordList.size()) : "0");
				dpsVO.setJobExcuteRemark("Data polling success. [setting_id = " + settingId + "]");
			}

		} catch (ServiceLayerException sle) {
			throw sle;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("Data polling 失敗");

		} finally {
			System.out.println("******* Finish records : " + recordList.size());

			recordList = null;
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
		final String insertMethod = setting.getInsertDbMethod();
		List<DataPollerMapping> mappingList = setting.getDataPollerMappings();

		if (mappingList == null || (mappingList != null && mappingList.isEmpty())) {
			return null;

		} else {
			Map<Integer, DataPollerServiceVO> mappingMap = new TreeMap<>();

			DataPollerServiceVO vo;
			for (DataPollerMapping mapping : mappingList) {
				if (StringUtils.equals(insertMethod, Constants.DATA_POLLER_INSERT_DB_BY_SQL)
						&& !StringUtils.equals(mapping.getIsSourceColumn(), Constants.DATA_Y)) {
					continue;
				}
				vo = new DataPollerServiceVO();
				BeanUtils.copyProperties(mapping, vo);

				if (StringUtils.equals(insertMethod, Constants.DATA_POLLER_INSERT_DB_BY_SQL)) {
					mappingMap.put(mapping.getSourceColumnIdx(), vo);

				} else if (StringUtils.equals(insertMethod, Constants.DATA_POLLER_INSERT_DB_BY_CSV_FILE)) {
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
	private String pollingFiles(List<String> recordList, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		String retVal = null;
		final String method = setting.getMethod();

		switch (method) {
			case Constants.DATA_POLLER_FILE_BY_FTP:
				break;

			case Constants.DATA_POLLER_FILE_BY_LOCAL_DIR:
				retVal = getFileByLocalDir(recordList, setting, mappingMap, specialSetFieldMap);
				break;
		}

		return retVal;
	}

	/**
	 * Method = LOCAL_DIR (本地端檔案夾)，取檔方法實作
	 * @param setting
	 * @param mappingMap
	 * @return
	 * @throws ServiceLayerException
	 */
	private String getFileByLocalDir(List<String> recordList, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		String retVal = null;

		final String insertMethod = setting.getInsertDbMethod();
		final String filePath = setting.getFilePath();
		final String fileNameRegex = setting.getFileNameRegex();

		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter(fileNameRegex);

		if (!dir.exists()) {
			//資料夾不存在直接返回
			recordList = null;
		}

		File[] files = dir.listFiles(fileFilter);

		if (files == null || (files != null && files.length == 0)) {
			//檔案不存在直接返回
			recordList = null;
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
				switch (insertMethod) {
					case Constants.DATA_POLLER_INSERT_DB_BY_SQL:
						readFileContents(recordList, targetFile, setting, mappingMap);
						break;

					case Constants.DATA_POLLER_INSERT_DB_BY_CSV_FILE:
						retVal = readFileContentsCompose2CSVFormat(recordList, targetFile, setting, mappingMap, specialSetFieldMap);
						break;
				}

			} catch (Exception e) {
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

		return retVal;
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

	private String readFileContentsCompose2CSVFormat(List<String> retRecordList, File file, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		String retTableName = "";

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
			boolean firstRun = true;
			for (String line : fileContent) {
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
					final String sourceColumnSqlFormat = data.getSourceColumnSqlFormat();

					targetTableName = data.getTargetTableName();
					final String targetFieldName = data.getTargetFieldName();

					if (firstRun) {
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

									retTableName = getSpecifyDayTableName(recordByDayInterval, targetTableName, dateYyymmdd);

								} catch (ServiceLayerException e) {
									throw e;
								}

								firstRun = false;
							}
						}
					}

					if (StringUtils.equals(isSourceColumn, Constants.DATA_Y)) {
						/*
						 * 當前欄位為來源CSV內欄位 >> 塞值來源 = CSV讀取內容
						 */
						targetStr[targetFieldIdx] = lineData[sourceColumnIdx];

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
					if (StringUtils.equals(sourceColumnType, Env.DATA_POLLER_SETTING_TYPE_OF_TIMESTAMP)) {
						//STR_TO_DATE(@var1,'%m/%d/%Y %k:%i');
						String funcStr = "STR_TO_DATE(@" + sourceColumnName + ", '" + sourceColumnSqlFormat + "') ";
						specialSetFieldMap.put(targetFieldName, funcStr);
					}
				}

				StringBuffer sb = new StringBuffer();
				for (int i=0; i<targetStr.length; i++) {
					sb.append(targetStr[i]);

					if (i < targetStr.length - 1) {
						sb.append(",");
					}
				}
				retRecordList.add(sb.toString());

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

		return retTableName;
	}

	/**
	 * 依照設定的編碼方式(FileCharset)讀取檔案內容，並依照設定的切割符號(SplitSymbol)切割內容，再依照先前轉換好的mappingMap轉置內容
	 * @param file
	 * @param setting
	 * @param mappingMap
	 * @return
	 */
	private void readFileContents(List<String> retRecordList, File file, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap) throws ServiceLayerException {
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
						transTableFieldMap2Sql(retRecordList, tableFieldMap, setting);

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

	private Date transSourceDateColumnFormat2DateObj(String sourceDateStr, String fieldJavaFormat) {
		Date retDate = null;

		if (sourceDateStr.contains(Constants.TIME_AM_CHINESE_WORD)) {
			sourceDateStr = sourceDateStr.replace(Constants.TIME_AM_CHINESE_WORD, "");
			sourceDateStr += " " + Constants.TIME_AM_ENGLISH_WORD;

		} else if (sourceDateStr.contains(Constants.TIME_PM_CHINESE_WORD)) {
			sourceDateStr = sourceDateStr.replace(Constants.TIME_PM_CHINESE_WORD, "");
			sourceDateStr += " " + Constants.TIME_PM_ENGLISH_WORD;
		}

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
	private void transTableFieldMap2Sql(List<String> retRecordList, Map<String, Map<String, String>> tableFieldMap, DataPollerSetting setting) throws ServiceLayerException {
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
			retRecordList.add(insertSql.toString());

			insertSql.setLength(0);	//for GC
			fieldSql.setLength(0);	//for GC
			valueSql.setLength(0);	//for GC
		}
	}

	private void insertData2DBByFile(String targetTableName, DataPollerSetting setting, List<String> csvRecords, Map<String, String> specialSetFieldMap) throws ServiceLayerException {
		try {
			/*
			 * Step 1. 將重組好的CSV語句輸出成檔案
			 */
			final String insertFileDir =
					StringUtils.isNotBlank(setting.getInsertFileDir()) ? setting.getInsertFileDir() : Env.DEFAULT_INSERT_DB_FILE_DIR;
			final String fileName = "INSERT_" + setting.getDataType() + "_" + Constants.FORMAT_YYYYMMDD_HH24MISS_NOSYMBOL.format(new Date()) + ".csv";

			Path targetFilePath = Paths.get(insertFileDir + File.separator + fileName);

			if (!Paths.get(insertFileDir).toFile().exists()) {
					Files.createDirectories(Paths.get(insertFileDir));
			}

			final String fileCharset = setting.getFileCharset();
			final String linesTerminatedBy = setting.getLinesTerminatedBy();

			FileUtils.writeLines(targetFilePath.toFile(), fileCharset, csvRecords, null, true);

			/*
			 * Step 2. 判斷是否有特殊欄位(e.g. Timestamp)需做SET語句轉換
			 */


			/*
			 * Step 3. 呼叫執行 LOAD DATA INFILE 指令
			 */
			final String fieldsTerminatedBy = setting.getFieldsTerminatedBy();

			String sqlFilePath = targetFilePath.toString().replace("\\", "\\\\");
			dataPollerDAO.loadDataInFile(targetTableName, sqlFilePath, fileCharset, fieldsTerminatedBy, linesTerminatedBy, null);

		} catch (IOException e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("輸出CSV檔案失敗");
		}
	}

	/**
	 * 執行Insert SQL實作
	 * @param sqls
	 */
	private void insertData2DB(List<String> sqls) {
		dataPollerDAO.insertEntitiesByNativeSQL(sqls);
	}

	@Override
	public List<String> getFieldName(String settingId, String fieldType) throws ServiceLayerException {
		List<String> retList = new ArrayList<>();
		try {
			DataPollerSetting setting = dataPollerDAO.findDataPollerSettingBySettingId(settingId);

			if (setting != null) {
				List<DataPollerMapping> mappingList = setting.getDataPollerMappings();

				if (mappingList != null && !mappingList.isEmpty()) {
					mappingList.forEach(mapping -> {
						retList.add(
							fieldType.equals(FIELD_TYPE_SOURCE)
								? mapping.getSourceColumnName()
								: mapping.getTargetFieldName());
					});
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢異常，請重新操作");
		}
		return retList;
	}
}
