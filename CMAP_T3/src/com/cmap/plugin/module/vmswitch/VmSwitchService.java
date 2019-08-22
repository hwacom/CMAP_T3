package com.cmap.plugin.module.vmswitch;

import com.cmap.exception.ServiceLayerException;

public interface VmSwitchService {

    public static final String SETTING_OF_STAND_BY_STATUS = "STAND_BY_STATUS";
    public static final String SETTING_OF_EPDG_HOST_IP = "EPDG_HOST_IP";
    public static final String RECONNECT_MAX_TIMES = "RECONNECT_MAX_TIMES";
    public static final String RECONNECT_INTERVAL = "RECONNECT_INTERVAL";
    public static final String RECONNECT_TIMES_TO_SHOW_MSG = "RECONNECT_TIMES_TO_SHOW_MSG";
    public static final String BACKUP_HOST_EPDG_CONFIG_PATH = "BACKUP_HOST_EPDG_CONFIG_PATH";
    public static final String BACKUP_HOST_EPDG_IMAGE_PATH = "BACKUP_HOST_EPDG_IMAGE_PATH";
    public static final String BACKUP_HOST_IP = "BACKUP_HOST_IP";

    public static final String MAIL_LIST_SETTING_CODE = "MAIL_LIST_SETTING_CODE";

    public enum Step {
        PROCESS_READY,
        CHECK_BACKUP_HOST_STATUS,
        GET_VM_MAPPING_TABLE,
        GET_SWITCH_HOST_INFO,
        GET_CONFIG_BACKUP_RECORD,
        PROCESS_CONFIG_CONTENT,
        CHECK_SSH_STATUS,
        DISABLE_SWITCH_HOST_INTERFACE,
        POWER_OFF_FROM_ESXI,
        POWER_OFF_EACH_ESXI,
        PROVISION_CONFIG_TO_BACKUP_HOST,
        MODIFY_BOOT_SETTING_AND_RELOAD,
        WAIT_FOR_RELOADING,
        PROVISION_PORT_AND_VLAN_FOR_NO_SHUTDOWN,
        MODIFY_BACKUP_HOST_STATUS,
        WRITE_PROCESS_LOG,
        STEP_RESULT,
        PROCESS_END,
        NONE
    }

    public enum Status {
        EXECUTING,
        WAITING,
        FINISH,
        ERROR,
        END,
        MSG
    };

	/**
	 * 將指定 VM Name 切換成備援機
	 * @param vmSwitchVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public String powerOff(VmSwitchVO vmSwitchVO) throws ServiceLayerException;

	/**
	 * 將指定 VM Name 從備援復原
	 * @param vmSwitchVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public String powerOn(VmSwitchVO vmSwitchVO) throws ServiceLayerException;

	/**
	 * 取得最早且尚未推播的log資料
	 * @param logKey
	 * @return
	 * @throws ServiceLayerException
	 */
	public ModuleVmProcessLog findFistOneNotPushedLogByLogKey(String logKey) throws ServiceLayerException;

	/**
	 * 將已經推播到UI的log資料註記調整為Y
	 * @param logEntity
	 * @throws ServiceLayerExceptioneeeeee
	 */
	public void updateLog(ModuleVmProcessLog logEntity) throws ServiceLayerException;

	/**
	 * 檢核VM當下狀態:
	 * (1) SSH是否可通
	 * (2) 是否有 Subscribers
	 * @param vmSwitchVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public VmSwitchVO chkVmStatus(VmSwitchVO vmSwitchVO) throws ServiceLayerException;
}
