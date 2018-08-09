package com.cmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmap.comm.ConnectionMode;
import com.cmap.comm.Step;

public class Env {
	
	public static List<String> DECODE_FIELDS = new ArrayList<String>();
	public static ConnectionMode FILE_TRANSFER_MODE;
	public static String LOGIN_AUTH_MODE;
	public static String ADMIN_USERNAME;
	public static String ADMIN_PASSWORD;
	public static List<String> ADMIN_ROLE_USERNAME = new ArrayList<String>();
	
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
			Step.CONNECT_FILE_SERVER,
			Step.LOGIN_FILE_SERVER,
			Step.UPLOAD_FTP,
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
			Step.COMPOSE_OUTPUT_VO,
			Step.RECORD_DB
	};
	
	public static Map<String, String> SCHED_TYPE_CLASS_MAPPING = new HashMap<String, String>();
	
	static {
		//系統預設值，當SYS_CONFIG_SETTING未設定時採用
		SCHED_TYPE_CLASS_MAPPING.put("backupConfig", "com.cmap.service.impl.jobs.JobBackupConfig");
	}
}
