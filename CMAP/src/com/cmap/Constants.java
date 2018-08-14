package com.cmap;

import java.text.SimpleDateFormat;

public class Constants {

	public static final String ADD_LINE = "[ADD]";
	public static final String GROUP_DEVICE_MAP = "GROUP_DEVICE_MAP";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String SYS = "SYS";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String PASSHASH = "passhash";
	public static final String ISADMIN = "isAdmin";
	public static final String USERROLE = "userrole";
	public static final String USERROLE_ADMIN = "ADMIN";
	public static final String USERROLE_USER = "USER";
	
	public static final String LOGIN_AUTH_MODE_PRTG = "PRTG";
	public static final String LOGIN_AUTH_MODE_PRIME = "PRIME";
	public static final String LOGIN_AUTH_MODE_DB = "DB";
	
	public static final SimpleDateFormat FORMAT_YYYYMMDD_HH24MI = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	public static final SimpleDateFormat FORMAT_CONFIG_FILE_NAME = new SimpleDateFormat("yyyyMMddHHmm");
	public static final SimpleDateFormat FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final String FTP = "FTP";
	public static final String TFTP = "TFTP";
	
	public static final String QUARTZ_JOB_BACKUP_CONFIG_FILES = "backupConfigFiles";
	
	public static final String QUARTZ_SCHED_TYPE_BACKUP_CONFIG = "backupConfig";
	public static final String QUARTZ_SCHED_TYPE_UPLOAD_BACKUP_CONFIG_FILE_2_FTP = "uploadBackupConfigFile2FTP";
	public static final String QUARTZ_SCHED_TYPE_CLEAN_UP_FTP_FILE = "cleanUpFtpFile";
	public static final String QUARTZ_SCHED_TYPE_CLEAN_UP_DB_DATA = "cleanUpDbData";
	
	public static final String QUARTZ_PARA_JSON_STR = "paraJsonStr";
	public static final String QUARTZ_PARA_DEVICE_LIST_ID = "deviceListIds";
	public static final String QUARTZ_PARA_CONFIG_TYPE = "configType";
	public static final String QUARTZ_PARA_GROUP_ID = "groupId";
	public static final String QUARTZ_PARA_DEVICE_ID = "deviceId";
	public static final String QUARTZ_PARA_SCHED_TYPE = "schedType";
	public static final String QUARTZ_PARA_FTP_NAME = "ftpName";
	public static final String QUARTZ_PARA_FTP_HOST = "ftpHost";
	public static final String QUARTZ_PARA_FTP_PORT = "ftpPort";
	public static final String QUARTZ_PARA_FTP_ACCOUNT = "ftpAccount";
	public static final String QUARTZ_PARA_FTP_PASSWORD = "ftpPassword";
	
	public static final String JSON_FIELD_SETTING_IDS = "settingIds";
	public static final String JSON_FIELD_MODIFY_SETTING_NAME = "modifySettingName";
	public static final String JSON_FIELD_MODIFY_SETTING_VALUE = "modifySettingValue";
	public static final String JSON_FIELD_MODIFY_SETTING_REMARK = "modifySettingRemark";
	
	public static final String DATA_MARK_DELETE = "Y";
	
	public static final String DATA_MARK_NOT_DELETE = "N";
	
	public static final String DATA_Y = "Y";
	
	public static final String DATA_N = "N";
	
	public static final String DEVICE_ID = "DEVICE_ID";
	
	public static final String DEVICE_NAME = "DEVICE_NAME";
	
	public static final String DEVICE_IP = "DEVICE_IP";
	
	public static final String DEVICE_SYSTEM = "DEVICE_SYSTEM";
	
	public static final String FTP_FILE_SEPARATOR = "/";
	
	public static final String[] NATIVE_FIELD_NAME_FOR_VERSION = new String[] {
			"version_id", "group_id", "group_name", "device_id", "device_name", "system_version", "config_type", "config_version", "file_full_name", "create_time"
	};
	
	public static final String[] HQL_FIELD_NAME_FOR_VERSION = new String[] {
			"versionId", "groupId", "groupName", "deviceId", "deviceName", "systemVersion", "configType", "configVersion", "fileFullName", "createTime"
	};
	
	public static final String[] NATIVE_FIELD_NAME_FOR_VERSION_2 = new String[] {
			"version_id", "config_type", "config_version", "create_time"
	};
	
	public static final String[] HQL_FIELD_NAME_FOR_VERSION_2 = new String[] {
			"versionId", "configType", "configVersion", "createTime"
	};
	
	public static final String[] NATIVE_FIELD_NAME_FOR_DEVICE = new String[] {
			"device_list_id", "device_ip", "config_file_dir_path"
	};
	
	public static final String[] HQL_FIELD_NAME_FOR_DEVICE = new String[] {
			"deviceListId", "deviceIp", "configFileDirPath"
	};
	
	public static final String[] NATIVE_FIELD_NAME_FOR_DEVICE_2 = new String[] {
			"device_list_id", "group_id", "group_name", "device_id", "device_name", "system_version", "device_ip", "config_file_dir_path"
	};
	
	public static final String[] HQL_FIELD_NAME_FOR_DEVICE_2 = new String[] {
			"deviceListId", "groupId", "groupName", "deviceId", "deviceName", "systemVersion","deviceIp", "configFileDirPath"
	};
}
