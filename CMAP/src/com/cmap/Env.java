package com.cmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmap.comm.enums.ConnectionMode;
import com.cmap.comm.enums.Step;

public class Env {

	public static List<String> DECODE_FIELDS = new ArrayList<String>();
	public static ConnectionMode FILE_TRANSFER_MODE;
	public static String LOGIN_AUTH_MODE;
	public static String ADMIN_USERNAME;
	public static String ADMIN_PASSWORD;
	public static List<String> ADMIN_ROLE_USERNAME = new ArrayList<String>();
	public static String USER_NAME_JOB;
	public static String USER_IP_JOB;
	public static String PROVISION_REASON_OF_JOB;

	/**
	 * 組態檔異地備份上傳至FTP時，BY日期創建資料夾的名稱格式
	 */
	public static String DIR_PATH_OF_CURRENT_DATE_FORMAT;

	public static String RETRY_TIMES;

	/**
	 * 設定要排除掉的PRTG群組名稱(不呈顯於CMAP內)
	 */
	public static List<String> PRTG_EXCLUDE_GROUP_NAME = new ArrayList<String>();

	/**
	 * 設定PRTG上裝置頁簽顯示名稱中，包覆著中文「群組名稱」的符號 >> For UI選單
	 * Ex: 第一航廈 (T1)
	 * >> 中文群組名稱:第一航廈，包覆符號為「」
	 */
	public static String PRTG_WRAPPED_SYMBOL_FOR_GROUP_NAME;


	/**
	 * 設定PRTG上裝置頁簽顯示名稱中，包覆著英文「群組名稱」的符號 >> For 組態檔備份目錄名稱
	 * Ex: 第一航廈 (T1)
	 * >> 英文群組名稱:T1，包覆符號為「()」
	 */
	public static String PRTG_WRAPPED_SYMBOL_FOR_GROUP_ENG_NAME;

	/**
	 * 設定PRTG上裝置頁簽顯示名稱中，包覆著中文「裝置名稱」的符號 >> For UI選單
	 * Ex: 192.168.1.3 (1F大廳) <1F_Lobby) [Cisco Device Cisco IOS]
	 * >> 中文裝置名稱:1F大廳，包覆符號為「()」
	 */
	public static String PRTG_WRAPPED_SYMBOL_FOR_DEVICE_NAME;

	/**
	 * 設定PRTG上裝置頁簽顯示名稱中，包覆著英文「裝置名稱」的符號 >> For 組態檔備份目錄名稱
	 * Ex: 192.168.1.3 (1F大廳) <1F_Lobby) [Cisco Device Cisco IOS]
	 * >> 英文裝置名稱:1F_Lobby，包覆符號為「<>」
	 */
	public static String PRTG_WRAPPED_SYMBOL_FOR_DEVICE_ENG_NAME;

	/**
	 * 設定PRTG上裝置頁簽顯示名稱中，包覆著「裝置作業系統」的符號
	 * Ex: 192.168.1.3 (R3) [Cisco Device Cisco IOS]
	 * >> 裝置作業系統:Cisco Device Cisco IOS，包覆符號為「[]」
	 */
	public static String PRTG_WRAPPED_SYMBOL_FOR_DEVICE_SYSTEM_VERSION;
	public static String PRTG_SERVER_IP;
	public static String PRTG_API_LOGIN;
	public static String PRTG_API_SENSOR_TREE;

	public static String BACKUP_FILENAME_FORMAT;
	public static String BACKUP_FILENAME_SEQ_NO_LENGTH;
	public static SimpleDateFormat BACKUP_FILENAME_DATE_FORMAT = new SimpleDateFormat();
	public static Boolean ENABLE_TEMP_FILE_RANDOM_CODE;
	public static Boolean ENABLE_REMOTE_BACKUP_USE_TODAY_ROOT_DIR;

	public static Integer HTTP_CONNECTION_TIME_OUT;
	public static Integer HTTP_SOCKET_TIME_OUT;

	public static String DEFAULT_LOCAL_DIR_GROUP_NAME;
	public static String DEFAULT_LOCAL_DIR_DEVICE_NAME;
	public static String DEFAULT_REMOTE_DIR_GROUP_NAME;
	public static String DEFAULT_REMOTE_DIR_DEVICE_NAME;

	public static String FTP_HOST_IP;
	public static Integer FTP_HOST_PORT;
	public static String FTP_LOGIN_ACCOUNT;
	public static String FTP_LOGIN_PASSWORD;
	public static String FTP_BASE_DIR_PATH;
	public static Integer FTP_DEFAULT_TIME_OUT;
	public static Integer FTP_CONNECT_TIME_OUT;
	public static String FTP_DIR_SEPARATE_SYMBOL;

	public static String TFTP_HOST_IP;
	public static Integer TFTP_HOST_PORT;
	public static Integer TFTP_DEFAULT_TIME_OUT;
	public static Integer TFTP_SOCKET_TIME_OUT;

	public static Integer TELNET_DEFAULT_TIME_OUT;
	public static Integer TELNET_CONNECT_TIME_OUT;
	public static Integer TELNET_DEFAULT_PORT;

	public static Integer SSH_CONNECT_TIME_OUT;
	public static Integer SSH_SOCKET_TIME_OUT;
	public static Integer SSH_DEFAULT_PORT;

	public static String DEFAULT_DEVICE_LOGIN_ACCOUNT;
	public static String DEFAULT_DEVICE_LOGIN_PASSWORD;
	public static String DEFAULT_DEVICE_ENABLE_PASSWORD;

	public static String DEFAULT_BACKUP_SCRIPT_CODE;
	public static String DEFAULT_RESTORE_SCRIPT_CODE;

	public static String CONFIG_FILE_EXTENSION_NAME;

	public static String COMM_SEPARATE_SYMBOL;

	public static Integer QUARTZ_DEFAULT_PRIORITY;

	public static String SIGN_ENABLE_PWD;
	public static String SIGN_PWD;
	public static String SIGN_ACT;
	public static String SIGN_TFTP_IP;
	public static String SIGN_TFTP_OUTPUT_FILE_PATH;

	public static String MENU_CODE_OF_CONFIG_TYPE;
	public static String MENU_CODE_OF_SCHED_TYPE;
	public static String MENU_CODE_OF_MIS_FIRE_POLICY;

	public static String MENU_ITEM_COMBINE_SYMBOL;

	public static Integer FILES_UPLOAD_PER_BATCH_COUNT;
	public static String UPLOAD_NEWEST_BACKUP_FILE_ONLY;

	public static Boolean TFTP_SERVER_AT_LOCAL;
	public static String TFTP_TEMP_DIR_PATH;
	public static String TFTP_LOCAL_ROOT_DIR_PATH;
	public static String TFTP_DIR_PATH_SEPARATE_SYMBOL;

	public static String MEANS_ALL_SYMBOL;

	public static final Step[] BACKUP_BY_TELNET = new Step[] {
			Step.LOAD_DEFAULT_SCRIPT,
			Step.FIND_DEVICE_CONNECT_INFO,
			Step.FIND_DEVICE_LOGIN_INFO,
			Step.CONNECT_DEVICE,
			Step.LOGIN_DEVICE,
			Step.SEND_COMMANDS,
			Step.CLOSE_DEVICE_CONNECTION,
			Step.DEFINE_OUTPUT_FILE_NAME,
			Step.COMPOSE_OUTPUT_VO,
			Step.CONNECT_FILE_SERVER_4_UPLOAD,
			Step.LOGIN_FILE_SERVER_4_UPLOAD,
			Step.UPLOAD_FILE_SERVER,
			Step.CLOSE_FILE_SERVER_CONNECTION,
			Step.RECORD_DB
	};
	public static final Step[] BACKUP_BY_TFTP = new Step[] {
			Step.LOAD_DEFAULT_SCRIPT,
			Step.FIND_DEVICE_CONNECT_INFO,
			Step.FIND_DEVICE_LOGIN_INFO,
			Step.CONNECT_DEVICE,
			Step.LOGIN_DEVICE,
			Step.DEFINE_OUTPUT_FILE_NAME,
			Step.SEND_COMMANDS,
			Step.CLOSE_DEVICE_CONNECTION,
			Step.CONNECT_FILE_SERVER_4_UPLOAD,
			Step.COMPARE_CONTENTS,
			Step.COMPOSE_OUTPUT_VO,
			Step.RECORD_DB
	};
	public static final Step[] DOWNLOAD_FILE_FROM_TFTP = new Step[] {
			Step.CONNECT_FILE_SERVER_4_DOWNLOAD,
			Step.DOWNLOAD_FILE,
			Step.CLOSE_FILE_SERVER_CONNECTION
	};
	public static final Step[] DOWNLOAD_FILE_FROM_FTP = new Step[] {
			Step.CONNECT_FILE_SERVER_4_DOWNLOAD,
			Step.LOGIN_FILE_SERVER_4_DOWNLOAD,
			Step.DOWNLOAD_FILE,
			Step.CLOSE_FILE_SERVER_CONNECTION
	};
	public static final Step[] BACKUP_FILE_DOWNLOAD_FROM_TFTP_AND_UPLOAD_2_FTP = new Step[] {
			Step.CONNECT_FILE_SERVER_4_DOWNLOAD,
			Step.DOWNLOAD_FILE,
			Step.CLOSE_FILE_SERVER_CONNECTION,
			Step.CONNECT_FILE_SERVER_4_UPLOAD,
			Step.LOGIN_FILE_SERVER_4_UPLOAD,
			Step.UPLOAD_FILE_SERVER,
			Step.CLOSE_FILE_SERVER_CONNECTION
	};

	public static Map<String, String> SCHED_TYPE_CLASS_MAPPING = new HashMap<String, String>();

	static {
		//系統預設值，當SYS_CONFIG_SETTING未設定時採用
	}
}
