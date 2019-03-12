package com.cmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmap.comm.enums.ConnectionMode;
import com.cmap.comm.enums.Step;

public class Env {

	/**
	 * 設定正常登入後要導向哪個頁面
	 */
	public static String HOME_PAGE;

	/**
	 * 設定MENU TREE功能是否顯示
	 */
	public static String SHOW_MENU_TREE_PRTG;					// PRTG index & net_flow & dashboard
	public static String SHOW_MENU_ITEM_CM_SCRIPT;				// 組態管理 > 腳本管理
	public static String SHOW_MENU_TREE_ABNORMAL_ALARM;			// 異常告警
	public static String SHOW_MENU_ITEM_IP_CONFLICT;			// 異常告警 > IP衝突查詢
	public static String SHOW_MENU_ITEM_UNAUTHORIZED_DHCP;		// 異常告警 > 未授權DHCP設備
	public static String SHOW_MENU_ITEM_LOOP_LOOP;				// 異常告警 > LOOP迴圈
	public static String SHOW_MENU_ITEM_DEVICE_FAILURE;			// 異常告警 > 設備故障
	public static String SHOW_MENU_ITEM_ABNORMAL_TRAFFIC;		// 異常告警 > 流量異常
	public static String SHOW_MENU_ITEM_OTHER_EXCEPTION;		// 異常告警 > 其他異常
	public static String SHOW_MENU_TREE_PLUGIN;					// 資安通報
	public static String SHOW_MENU_ITEM_PLUGIN_WIFI_POLLER;		// 資安通報 > Wifi查詢
	public static String SHOW_MENU_ITEM_PLUGIN_NET_FLOW;		// 資安通報 > Net flow查詢
	public static String SHOW_MENU_ITEM_PLUGIN_SWITCH_PORT;		// 資安通報 > 開關PORT
	public static String SHOW_MENU_ITEM_IP_OPEN_BLOCK;			// 資安通報 > IP開通/封鎖
	public static String SHOW_MENU_ITEM_MAC_OPEN_BLOCK;			// 資安通報 > 網卡MAC開通/封鎖

	public static Boolean ENABLE_CM_SCRIPT_MODIFY;				// 設定是否啟用腳本管理編輯功能(Y:啟用;N:不啟用，僅可做查詢)

	public static String TABLE_NAME_OF_FIREWALL_BLACK_LIST_RECORD;	// 設定防火牆黑名單紀錄TABLE名稱

	/**
	 * GROUP_NAME下拉選單排序相關設定
	 */
	public static Boolean SORT_GROUP_MENU_BY_GROUP_NAME_INCLUDED_SEQ_NO;	// 以GROUP_NAME內含的序碼做排序 (e.g. 030.XX國小)，目前for苗栗縣教網使用
	public static String GROUP_NAME_SPLIT_SEQ_NO_SYMBOL;		// 設定GROUP_NAME用來切割序碼的符號 (搭配SORT_GROUP_MENU_BY_GROUP_NAME_INCLUDED_SEQ_NO使用)
	public static Integer GROUP_NAME_SPLITTED_SEQ_NO_INDEX;		// 設定GROUP_NAME切割後序碼所在陣列的INDEX (搭配GROUP_NAME_SPLIT_SEQ_NO_SYMBOL使用)

	public static List<String> DECODE_FIELDS = new ArrayList<>();
	public static ConnectionMode FILE_TRANSFER_MODE;
	public static String LOGIN_AUTH_MODE;
	public static String ADMIN_USERNAME;
	public static String ADMIN_PASSWORD;
	public static List<String> ADMIN_ROLE_USERNAME = new ArrayList<>();
	public static String USER_NAME_JOB;
	public static String USER_IP_JOB;
	public static String PROVISION_REASON_OF_JOB;

	/**
	 * 設定組態檔內容的一個層級對應幾個空白字元 FOR 組態還原使用
	 */
	public static Integer CONFIG_CONTENT_ONE_LAYER_EQUAL_TO_WHITE_SPACE_COUNT;
	public static Integer CONFIG_CONTENT_TOP_LAYER_NUM;
	public static Integer CONFIG_CONTENT_NO_LIMIT_LAYER_NUM;

	/**
	 * 組態檔異地備份上傳至FTP時，BY日期創建資料夾的名稱格式
	 */
	public static String DIR_PATH_OF_CURRENT_DATE_FORMAT;

	public static String RETRY_TIMES;

	public static Boolean PRTG_HA;
	public static List<String> PRTG_EXCLUDE_PROBENODE_ID = new ArrayList();

	/**
	 * 設定要排除掉的PRTG群組名稱(不呈顯於CMAP內)
	 */
	public static List<String> PRTG_EXCLUDE_GROUP_NAME = new ArrayList<>();

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
	public static Boolean ENABLE_LOCAL_BACKUP_USE_TODAY_ROOT_DIR;
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

	public static String DEFAULT_RESTORE_SCRIPT_CODE;
	public static String DEFAULT_DEVICE_CONFIG_BACKUP_MODE;	//預設設備組態檔備份模式 (DEVICE -> TFTP SERVER1)
	public static String DEFAULT_BACKUP_FILE_BACKUP_MODE;	//預設組態備份檔案備份模式 (TFTP SERVER1 -> FTP SERVER2)
	public static String DEFAULT_DEVICE_CONFIG_RESTORE_MODE;

	public static String CONFIG_FILE_EXTENSION_NAME;

	public static String COMM_SEPARATE_SYMBOL;

	public static Integer QUARTZ_DEFAULT_PRIORITY;

	public static String CLI_VAR_ENABLE_PWD;
	public static String CLI_VAR_PWD;
	public static String CLI_VAR_ACT;
	public static String CLI_VAR_TFTP_IP;
	public static String CLI_VAR_TFTP_OUTPUT_FILE_PATH;
	public static String CLI_VAR_CMD_LIST;

	public static String MENU_CODE_OF_CONFIG_TYPE;
	public static String MENU_CODE_OF_SCHED_TYPE;
	public static String MENU_CODE_OF_MISS_FIRE_POLICY;

	public static String MENU_ITEM_COMBINE_SYMBOL;

	public static Integer FILES_UPLOAD_PER_BATCH_COUNT;
	public static String UPLOAD_NEWEST_BACKUP_FILE_ONLY;

	public static Boolean TFTP_SERVER_AT_LOCAL;
	public static String TFTP_TEMP_DIR_PATH;
	public static String TFTP_LOCAL_ROOT_DIR_PATH;
	public static String TFTP_DIR_PATH_SEPARATE_SYMBOL;

	public static String MEANS_ALL_SYMBOL;
	public static String SCRIPT_VAR_KEY_SYMBOL;

	public static String DEFAULT_DATA_POLLER_FILE_CHARSET;
	public static Integer DEFAULT_BATCH_INSERT_FLUSH_COUNT;

	public static String SETTING_ID_OF_NET_FLOW;

	public static String DATA_POLLER_NET_FLOW_TABLE_BASE_NAME;
	public static String NET_FLOW_OUTPUT_FILE_EXT_NAE;

	public static String PRTG_LOGIN_URI;
	public static String PRTG_INDEX_URI;
	public static String PRTG_DEFAULT_INDEX_URI;
	public static String PRTG_DEFAULT_DASHBOARD_URI;
	public static String PRTG_DEFAULT_NET_FLOW_SUMMARY_URI;
	public static String PRTG_DEFAULT_DEVICE_FAILURE_URI;
	public static String PRTG_DEFAULT_ABNORMAL_TRAFFIC_URI;
	public static String PRTG_LOGOUT_URI;

	public static String DEFAULT_INSERT_DB_FILE_DIR;
	public static String DEFAULT_FILE_LINE_ENDING_SYMBOL;

	public static String TIMEOUT_4_NET_FLOW_QUERY;

	public static ConnectionMode CONNECTION_MODE_OF_DELIVERY;
	public static ConnectionMode CONNECTION_MODE_OF_VM_SWITCH;

	// 設定資安通報下「開關PORT」的腳本SCRIPT_CODE
	public static List<String> DELIVERY_SWITCH_PORT_SCRIPT_CODE = new ArrayList<>();
	// 設定資安通報下「IP封鎖/開通」的腳本SCRIPT_CODE
	public static List<String> DELIVERY_IP_OPEN_BLOCK_SCRIPT_CODE = new ArrayList<>();
	// 設定資安通報下「MAC封鎖/開通」的腳本SCRIPT_CODE
	public static List<String> DELIVERY_MAC_OPEN_BLOCK_SCRIPT_CODE = new ArrayList<>();
	/*
	 * OpenID
	 */
	public static String OIDC_CONFIGURATION_ENDPOINT;
	public static String OIDC_CLIENT_ID;
	public static String OIDC_CIENT_SECRET;
	public static String OIDC_AUTH_ENDPOINT;
	public static String OIDC_TOKEN_ENDPOINT;
	public static String OIDC_USER_INFO_ENDPOINT;
	public static String OIDC_EDU_INFO_ENDPOINT;
	public static String OIDC_JWKS_URI;
	public static String OIDC_REDIRECT_URI;
	public static String OIDC_RESPONSE_TYPE;
	public static String OIDC_SCOPE;

	public static String OIDC_AUTH_ENDPOINT_REQUEST_PARA_CODE;
	public static String OIDC_AUTH_ENDPOINT_REQUEST_STATE_CODE;
	public static String OIDC_USERINFO_ENDPOINT_JSON_NAME_NODE;
	public static String OIDC_USERINFO_ENDPOINT_JSON_EMAIL_NODE;
	public static String OIDC_USERINFO_ENDPOINT_JSON_OPEN2ID_NODE;
	public static String OIDC_EDUINFO_ENDPOINT_JSON_SCHOOLID_NODE;

	public static String FILE_EXTENSION_NAME_OF_NET_FLOW;

	public static final Step[] SEND_SCRIPT = new Step[] {
			Step.LOAD_SPECIFIED_SCRIPT,
			Step.FIND_DEVICE_CONNECT_INFO,
			Step.FIND_DEVICE_LOGIN_INFO,
			Step.CONNECT_DEVICE,
			Step.LOGIN_DEVICE,
			Step.SEND_COMMANDS,
			Step.CHECK_PROVISION_RESULT,
			Step.CLOSE_DEVICE_CONNECTION
	};
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
			Step.RECORD_DB_OF_CONFIG_VERSION_INFO
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
			Step.ANALYZE_CONFIG_INFO,
			Step.COMPOSE_OUTPUT_VO,
			Step.RECORD_DB_OF_CONFIG_VERSION_INFO
	};
	public static final Step[] BACKUP_BY_FTP = new Step[] {
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
			Step.ANALYZE_CONFIG_INFO,
			Step.COMPOSE_OUTPUT_VO,
			Step.RECORD_DB_OF_CONFIG_VERSION_INFO
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
	public static final Step[] RESTORE_BY_CLI = new Step[] {
			Step.FIND_DEVICE_CONNECT_INFO,
			Step.FIND_DEVICE_LOGIN_INFO,
			Step.GET_VERSION_INFO,
			Step.CONNECT_FILE_SERVER_4_DOWNLOAD,
			Step.LOGIN_FILE_SERVER_4_DOWNLOAD,
			Step.DOWNLOAD_FILE,
			Step.CLOSE_FILE_SERVER_CONNECTION,
			Step.PROCESS_CONFIG_CONTENT_SETTING,
			Step.LOAD_DEFAULT_SCRIPT,
			Step.CONNECT_DEVICE,
			Step.LOGIN_DEVICE,
			Step.SEND_COMMANDS,
			Step.CLOSE_DEVICE_CONNECTION
	};
	public static final Step[] RESTORE_BY_FTP = new Step[] {
			Step.FIND_DEVICE_CONNECT_INFO,
			Step.FIND_DEVICE_LOGIN_INFO,
			Step.GET_VERSION_INFO,
			Step.CONNECT_FILE_SERVER_4_DOWNLOAD,
			Step.LOGIN_FILE_SERVER_4_DOWNLOAD,
			Step.DOWNLOAD_FILE,
			Step.CLOSE_FILE_SERVER_CONNECTION,
			Step.PROCESS_CONFIG_CONTENT_SETTING,
			Step.LOAD_DEFAULT_SCRIPT,
			Step.CONNECT_DEVICE,
			Step.LOGIN_DEVICE,
			Step.SEND_COMMANDS,
			Step.CLOSE_DEVICE_CONNECTION
	};
	public static final Step[] RESTORE_BY_TFTP = new Step[] {
			Step.FIND_DEVICE_CONNECT_INFO,
			Step.FIND_DEVICE_LOGIN_INFO,
			Step.GET_VERSION_INFO,
			Step.CONNECT_FILE_SERVER_4_DOWNLOAD,
			Step.LOGIN_FILE_SERVER_4_DOWNLOAD,
			Step.DOWNLOAD_FILE,
			Step.CLOSE_FILE_SERVER_CONNECTION,
			Step.PROCESS_CONFIG_CONTENT_SETTING,
			Step.LOAD_DEFAULT_SCRIPT,
			Step.CONNECT_DEVICE,
			Step.LOGIN_DEVICE,
			Step.SEND_COMMANDS,
			Step.CLOSE_DEVICE_CONNECTION
	};

	public static Map<String, String> SCHED_TYPE_CLASS_MAPPING = new HashMap<>();

	static {
		//系統預設值，當SYS_CONFIG_SETTING未設定時採用
	}
}
