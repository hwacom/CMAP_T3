package com.cmap;

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

	public static String RETRY_TIMES;

	public static String PRTG_SERVER_IP;
	public static String PRTG_API_LOGIN;
	public static String PRTG_API_SENSOR_TREE;

	public static Integer HTTP_CONNECTION_TIME_OUT;
	public static Integer HTTP_SOCKET_TIME_OUT;

	public static String DEFAULT_FTP_DIR_GROUP_NAME;
	public static String DEFAULT_FTP_DIR_DEVICE_NAME;

	public static String FTP_HOST_IP;
	public static Integer FTP_HOST_PORT;
	public static String FTP_LOGIN_ACCOUNT;
	public static String FTP_LOGIN_PASSWORD;
	public static String FTP_BASE_DIR_PATH;
	public static Integer FTP_DEFAULT_TIME_OUT;
	public static Integer FTP_CONNECT_TIME_OUT;

	public static String TFTP_HOST_IP;
	public static Integer TFTP_HOST_PORT;
	public static Integer TFTP_DEFAULT_TIME_OUT;
	public static Integer TFTP_SOCKET_TIME_OUT;

	public static Integer TELNET_DEFAULT_TIME_OUT;
	public static Integer TELNET_CONNECT_TIME_OUT;

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
