package com.cmap;

import java.text.SimpleDateFormat;

import com.cmap.comm.enums.ConnectionMode;

public class Constants {

	public static final String ADD_LINE = "[ADD]";
	public static final String GROUP_DEVICE_MAP = "GROUP_DEVICE_MAP";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String SYS = "SYS";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String PASSHASH = "passhash";
	public static final String IP_ADDR = "ipAddr";
	public static final String ISADMIN = "isAdmin";
	public static final String ERROR = "error";
	public static final String USERROLE = "userrole";
	public static final String USERROLE_ADMIN = "ADMIN";
	public static final String USERROLE_USER = "USER";
	public static final String FIELD_NAME_UPDATE_BY = "updateBy";
	public static final String UNKNOWN = "unknown";

	public static final String LOGIN_AUTH_MODE_OIDC = "OIDC";
	public static final String LOGIN_AUTH_MODE_PRTG = "PRTG";
	public static final String LOGIN_AUTH_MODE_PRIME = "PRIME";
	public static final String LOGIN_AUTH_MODE_DB = "DB";

	public static final SimpleDateFormat FORMAT_YYYYMMDD_HH24MISS_NOSYMBOL = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final SimpleDateFormat FORMAT_YYYY_MM_DD_NOSYMBOL = new SimpleDateFormat("yyyyMMdd");

	public static final SimpleDateFormat FORMAT_YYYYMMDD_HH24MISS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static final SimpleDateFormat FORMAT_YYYYMMDD_HH24MI = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	public static final SimpleDateFormat FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

	public static final String FTP = "FTP";
	public static final String TFTP = "TFTP";

	public static final String QUARTZ_JOB_BACKUP_CONFIG_FILES = "backupConfigFiles";

	public static final String QUARTZ_SCHED_TYPE_BACKUP_CONFIG = "backupConfig";
	public static final String QUARTZ_SCHED_TYPE_UPLOAD_BACKUP_CONFIG_FILE_2_FTP = "uploadBackupConfigFile2FTP";
	public static final String QUARTZ_SCHED_TYPE_SYS_CHECK_UPDATE = "sysCheck4Update";
	public static final String QUARTZ_SCHED_TYPE_SYS_CHECK_QUERY = "sysCheck4Query";
	public static final String QUARTZ_SCHED_TYPE_CLEAN_UP_FTP_FILE = "cleanUpFtpFile";
	public static final String QUARTZ_SCHED_TYPE_CLEAN_UP_DB_DATA = "cleanUpDbData";
	public static final String QUARTZ_SCHED_TYPE_DATA_POLLER = "dataPoller";
	public static final String QUARTZ_SCHED_TYPE_LOCAL_FILE_OPERATION = "localFileOperation";

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
	public static final String QUARTZ_PARA_SYS_CHECK_SQLS = "sysCheckSqls";
	public static final String QUARTZ_PARA_DATA_POLLER_SETTING_ID = "dataPollerSettingId";
	public static final String QUARTZ_PARA_JOB_FILE_OPERATION_SETTING_ID = "jobFileOperationSettingId";

	public static final String DATA_POLLER_FILE_BY_FTP = "FTP";
	public static final String DATA_POLLER_FILE_BY_LOCAL_DIR = "LOCAL_DIR";

	public static final String JSON_FIELD_SETTING_IDS = "settingIds";
	public static final String JSON_FIELD_MODIFY_SETTING_NAME = "modifySettingName";
	public static final String JSON_FIELD_MODIFY_SETTING_VALUE = "modifySettingValue";
	public static final String JSON_FIELD_MODIFY_SETTING_REMARK = "modifySettingRemark";

	public static final String DATA_MARK_DELETE = "Y";
	public static final String DATA_MARK_NOT_DELETE = "N";
	public static final String DATA_Y = "Y";
	public static final String DATA_N = "N";
	public static final String DATA_STAR_SYMBOL = "*";

	public static final String GROUP_ID = "GROUP_ID";
	public static final String GROUP_NAME = "GROUP_NAME";
	public static final String GROUP_ENG_NAME = "GROUP_ENG_NAME";
	public static final String DEVICE_ID = "DEVICE_ID";
	public static final String DEVICE_NAME = "DEVICE_NAME";
	public static final String DEVICE_ENG_NAME = "DEVICE_ENG_NAME";
	public static final String DEVICE_IP = "DEVICE_IP";
	public static final String DEVICE_SYSTEM = "DEVICE_SYSTEM";

	public static final String DIR_PATH_GROUP_ID = "[gID]";
	public static final String DIR_PATH_GROUP_NAME = "[gName]";
	public static final String DIR_PATH_DEVICE_ID = "[dID]";
	public static final String DIR_PATH_DEVICE_NAME = "[dName]";
	public static final String DIR_PATH_DEVICE_IP = "[dIP]";
	public static final String DIR_PATH_DEVICE_SYSTEM = "[dSystem]";
	public static final String DIR_PATH_CONFIG_TYPE = "[configType]";
	public static final String DIR_PATH_DATE = "[date]";
	public static final String DIR_PATH_SEQ_NO = "[seqNo]";
	public static final String DIR_PATH_EXT_NAME = "[extName]";

	public static final String SCRIPT_MODE_ACTION = "A";
	public static final String SCRIPT_MODE_CHECK = "C";
	public static final String DEFAULT_FLAG_Y = "Y";
	public static final String DEFAULT_FLAG_N = "N";

	public static final String DEVICE_CONFIG_BACKUP_MODE_TELNET_SSH_FTP = "STEP.TELNET+DEVICE.SSH+FILE_SERVER.FTP";
	public static final String DEVICE_CONFIG_BACKUP_MODE_FTP_SSH_FTP = "STEP.FTP+DEVICE.SSH+FILE_SERVER.FTP";
	public static final String DEVICE_CONFIG_BACKUP_MODE_FTP_TELNET_FTP = "STEP.FTP+DEVICE.TELNET+FILE_SERVER.FTP";
	public static final String DEVICE_CONFIG_BACKUP_MODE_TFTP_SSH_TFTP = "STEP.TFTP+DEVICE.SSH+FILE_SERVER.TFTP";
	public static final String DEVICE_CONFIG_BACKUP_MODE_TFTP_TELNET_TFTP = "STEP.TFTP+DEVICE.TELNET+FILE_SERVER.TFTP";

	public static final String BACKUP_FILE_BACKUP_MODE_NULL_FTP_FTP = "STEP.NULL+DOWNLOAD.FTP+UPLOAD.FTP";
	public static final String BACKUP_FILE_BACKUP_MODE_STEP_TFTP_FTP = "STEP.STEP+DOWNLOAD.TFTP+UPLOAD.FTP";

	public static final String FTP_FILE_SEPARATOR = "/";
	public static final String HTML_BREAK_LINE_SYMBOL = "<br>";
	public static final String HTML_SEPARATION_LINE_SYMBOL = "<br>";

	public static final String TIME_AM_CHINESE_WORD = "上午";
	public static final String TIME_PM_CHINESE_WORD = "下午";
	public static final String TIME_AM_ENGLISH_WORD = "AM";
	public static final String TIME_PM_ENGLISH_WORD = "PM";

	public static final String RECORD_BY_DAY_REFER_FIELD = "REFER_FIELD";

	public static final String FIELD_TYPE_OF_VARCHAR = "VARCHAR";
	public static final String FIELD_TYPE_OF_TIMESTAMP = "TIMESTAMP";

	public static final String DATA_TYPE_OF_NET_FLOW = "NET_FLOW";

	public static final ConnectionMode DEFAULT_DEVICE_CONNECTION_MODE = ConnectionMode.TELNET;

	public static final String MODEL_ATTR_LOGIN_ERROR = "LOGIN_EXCEPTION";

	public static final String JOB_FILE_OPERATE_ACTION_CUT = "CUT";
	public static final String JOB_FILE_OPERATE_ACTION_COPY = "COPY";
	public static final String JOB_FILE_OPERATE_ACTION_DELETE = "DELETE";
	public static final String JOB_FILE_OPERATE_ACTION_RENAME = "RENAME";

	public static final String FILE_FORMAT_YYYYMMDDHH24MISS = "[YYYYMMDDHH24MISS]";
	public static final String FILE_FORMAT_FILE_EXT = "[FILE_EXT]";

	public static final String DATA_POLLER_INSERT_DB_BY_SQL = "SQL";
	public static final String DATA_POLLER_INSERT_DB_BY_CSV_FILE = "CSV_FILE";

	/*
	 * OIDC
	 */
	public static final String OIDC_CLIENT_ID = "OIDC_CLIENT_ID";
	public static final String OIDC_CLIENT_SECRET = "OIDC_CLIENT_SECRET";
	public static final String OIDC_CONFIGURATION_ENDPOINT = "OIDC_CONFIGURATION_ENDPOINT";
	public static final String OIDC_TOKEN_ENDPOINT = "OIDC_TOKEN_ENDPOINT";
	public static final String OIDC_USER_INFO_ENDPOINT = "OIDC_USER_INFO_ENDPOINT";
	public static final String OIDC_EDU_INFO_ENDPOINT = "OIDC_EDU_INFO_ENDPOINT";
	public static final String OIDC_JWKS_URI = "OIDC_JWKS_URI";
	public static final String OIDC_REDIRECT_URI = "OIDC_REDIRECT_URI";
	public static final String OIDC_STATE = "OIDC_STATE";
	public static final String OIDC_CODE = "OIDC_CODE";
	public static final String OIDC_ACCESS_TOKEN = "OIDC_ACCESS_TOKEN";
	public static final String OIDC_ID_TOKEN = "OIDC_ID_TOKEN";
	public static final String OIDC_REFRESH_TOKEN = "OIDC_REFRESH_TOKEN";
	public static final String OIDC_USER_INFO_JSON = "OIDC_USER_INFO_JSON";
	public static final String OIDC_EDU_INFO_JSON = "OIDC_EDU_INFO_JSON";
	public static final String OIDC_OPEN_2_ID = "OIDC_OPEN_2_ID";
	public static final String OIDC_SUB = "OIDC_SUB";
	public static final String OIDC_SCHOOL_ID = "OIDC_SCHOOL_ID";
	public static final String OIDC_EMAIL = "OIDC_EMAIL";
	public static final String OIDC_USER_NAME = "OIDC_USER_NAME";

	public static final String OIDC_MLC_CONFIGURATION_ENDPOINT = "https://mlc.sso.edu.tw/.well-known/openid-configuration";

	public static final String PRTG_LOGIN_ACCOUNT = "PRTG_LOGIN_ACCOUNT";
	public static final String PRTG_LOGIN_PASSWORD = "PRTG_LOGIN_PASSWORD";

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
			"device_list_id", "device_ip", "config_file_dir_path", "remote_file_dir_path"
	};

	public static final String[] HQL_FIELD_NAME_FOR_DEVICE = new String[] {
			"deviceListId", "deviceIp", "configFileDirPath", "remoteFileDirPath"
	};

	public static final String[] NATIVE_FIELD_NAME_FOR_DEVICE_2 = new String[] {
			"device_list_id", "group_id", "group_name", "device_id", "device_name", "system_version", "device_ip", "config_file_dir_path", "remote_file_dir_path"
	};

	public static final String[] HQL_FIELD_NAME_FOR_DEVICE_2 = new String[] {
			"deviceListId", "groupId", "groupName", "deviceId", "deviceName", "systemVersion","deviceIp", "configFileDirPath", "remoteFileDirPath"
	};
}
