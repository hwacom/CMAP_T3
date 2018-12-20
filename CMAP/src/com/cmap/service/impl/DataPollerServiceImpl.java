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
		try {
			// Step 1. 查找設定檔
			DataPollerSetting setting = dataPollerDAO.findDataPollerSettingById(settingId);

			if (setting == null) {
				dpsVO.setJobExcuteResult(Result.FAILED);
				dpsVO.setJobExcuteResultRecords("0");
				dpsVO.setJobExcuteRemark("查無 DATA_POLLER_SETTING 資料 >> settingId: " + settingId);

			} else {
				// Step 2. 確認此組Data Poller是否為By天寫入資料(Record_By_Day)、以及是否有設定要By天清除資料(Record_By_Day_Clean)
				final String recordByDay = setting.getRecordByDay();
				final String interval = setting.getRecordByDayInterval();
				final String cleanNotTodayData = setting.getRecordByDayClean();

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

				// Step 4. 依照設定值，取得檔案並解析內容
				List<String> sqlList = pollingFiles(setting, mappingMap);

				// Step 5. 寫入DB
				insertData2DB(sqlList);

				dpsVO.setJobExcuteResult(Result.SUCCESS);
				dpsVO.setJobExcuteResultRecords(sqlList != null ? String.valueOf(sqlList.size()) : "0");
				dpsVO.setJobExcuteRemark("Data polling success. [setting_id = " + settingId + "]");
			}

		} catch (ServiceLayerException sle) {
			throw sle;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("Data polling 失敗");
		}
		return dpsVO;
	}

	/**
	 * 將Data_Poller_Mapping設定的內容轉換成以Index為Key的Map
	 * @param setting
	 * @return
	 */
	private Map<Integer, DataPollerServiceVO> composeMapping(DataPollerSetting setting) {
		List<DataPollerMapping> mappingList = setting.getDataPollerMappings();

		if (mappingList == null || (mappingList != null && mappingList.isEmpty())) {
			return null;

		} else {
			Map<Integer, DataPollerServiceVO> mappingMap = new TreeMap<>();

			DataPollerServiceVO vo;
			for (DataPollerMapping mapping : mappingList) {
				vo = new DataPollerServiceVO();
				BeanUtils.copyProperties(mapping, vo);
				mappingMap.put(mapping.getSourceColumnIdx(), vo);
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
	private List<String> pollingFiles(DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap) throws ServiceLayerException {
		final String method = setting.getMethod();

		List<String> sqlList = null;
		switch (method) {
			case Constants.DATA_POLLER_FILE_BY_FTP:
				break;

			case Constants.DATA_POLLER_FILE_BY_LOCAL_DIR:
				sqlList = getFileByLocalDir(setting, mappingMap);
				break;
		}

		return sqlList;
	}

	/**
	 * Method = LOCAL_DIR (本地端檔案夾)，取檔方法實作
	 * @param setting
	 * @param mappingMap
	 * @return
	 * @throws ServiceLayerException
	 */
	private List<String> getFileByLocalDir(DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap) throws ServiceLayerException {
		List<String> retSqlList = new ArrayList<>();

		final String filePath = setting.getFilePath();
		final String fileNameRegex = setting.getFileNameRegex();

		File dir = new File(filePath);
		FileFilter fileFilter = new WildcardFileFilter(fileNameRegex);

		if (!dir.exists()) {
			//資料夾不存在直接返回
			return retSqlList;
		}

		File[] files = dir.listFiles(fileFilter);

		if (files == null || (files != null && files.length == 0)) {
			//檔案不存在直接返回
			return retSqlList;
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
				retSqlList.addAll(readFileContents(targetFile, setting, mappingMap));

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
			}
		}

		return retSqlList;
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

	/**
	 * 依照設定的編碼方式(FileCharset)讀取檔案內容，並依照設定的切割符號(SplitSymbol)切割內容，再依照先前轉換好的mappingMap轉置內容
	 * @param file
	 * @param setting
	 * @param mappingMap
	 * @return
	 */
	private List<String> readFileContents(File file, DataPollerSetting setting, Map<Integer, DataPollerServiceVO> mappingMap) throws ServiceLayerException {
		List<String> retSqlList = new ArrayList<>();

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
									if (fieldValue.contains(Constants.TIME_AM_CHINESE_WORD)) {
										fieldValue = fieldValue.replace(Constants.TIME_AM_CHINESE_WORD, "");
										fieldValue += " " + Constants.TIME_AM_ENGLISH_WORD;

									} else if (fieldValue.contains(Constants.TIME_PM_CHINESE_WORD)) {
										fieldValue = fieldValue.replace(Constants.TIME_PM_CHINESE_WORD, "");
										fieldValue += " " + Constants.TIME_PM_ENGLISH_WORD;
									}

									if (fieldName.equals(recordByDayReferField)) {
										SimpleDateFormat sdf = new SimpleDateFormat(fieldJavaFormat, Locale.ENGLISH);
										try {
											String dateYyymmdd = Constants.FORMAT_YYYY_MM_DD.format(sdf.parse(fieldValue));
											recordByDayReferFieldValue = dateYyymmdd;

										} catch (ParseException e) {
											log.error(e.toString(), e);
											throw new RuntimeException("轉換來源檔案中 Record_By_Day_Refer_Field 的值成 Date 物件時異常 >> fieldValue: " + fieldValue);
										}
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
						retSqlList.addAll(transTableFieldMap2Sql(tableFieldMap, setting));

					} catch (ServiceLayerException sle) {
						throw new RuntimeException(sle);
					}
				}
			});

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("Data polling 失敗");
		}
		return retSqlList;
	}

	/**
	 * 將檔案內容暫存至Map的結果轉換成Insert SQL
	 * @param tableFieldMap
	 * @return
	 * @throws ServiceLayerException
	 */
	private List<String> transTableFieldMap2Sql(Map<String, Map<String, String>> tableFieldMap, DataPollerSetting setting) throws ServiceLayerException {
		List<String> sqlList = new ArrayList<>();

		StringBuffer insertSql = null;
		StringBuffer fieldSql = null;
		StringBuffer valueSql = null;

		for (Map.Entry<String, Map<String, String>> tableEntry : tableFieldMap.entrySet()) {
			insertSql = new StringBuffer();
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

			fieldSql = new StringBuffer();
			fieldSql.append(" ( ");

			valueSql = new StringBuffer();
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
			sqlList.add(insertSql.toString());
		}

		return sqlList;
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
			DataPollerSetting setting = dataPollerDAO.findDataPollerSettingById(settingId);

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
