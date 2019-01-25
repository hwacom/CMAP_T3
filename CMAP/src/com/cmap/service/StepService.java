package com.cmap.service;

import java.util.List;
import java.util.Map;

import com.cmap.comm.enums.ConnectionMode;
import com.cmap.comm.enums.RestoreMethod;
import com.cmap.model.ScriptInfo;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.service.vo.VersionServiceVO;

public interface StepService {

	public enum Result {
		SUCCESS,
		NO_DIFFERENT,
		ERROR
	}

	/**
	 * 設備組態備份流程
	 * (Device >> Server Local File)
	 * @param deviceListId
	 * @param jobTrigger
	 * @return
	 */
	public StepServiceVO doBackupStep(String deviceListId, boolean jobTrigger);

	/**
	 * 組態備份檔異地備份流程
	 * (Server Local File >> Remote FTP)
	 * @param vsVOs
	 * @param ciVO
	 * @param jobTrigger
	 * @return
	 */
	public StepServiceVO doBackupFileUpload2FTPStep(List<VersionServiceVO> vsVOs, ConfigInfoVO ciVO, boolean jobTrigger);

	/**
	 * 設備組態還原流程
	 * @param recoverMethod (by FILE / CLI)
	 * @param stepServiceVO
	 * @param triggerBy
	 * @param reason
	 * @return
	 */
	public StepServiceVO doRestoreStep(RestoreMethod restoreMethod, String restoreType, StepServiceVO stepServiceVO, String triggerBy, String reason);

	/**
	 * 供裝派送流程
	 * @param connectionMode
	 * @param deviceListId
	 * @param deviceInfo
	 * @param scriptInfo
	 * @param varMap
	 * @param sysTrigger
	 * @param triggerBy
	 * @param triggerRemark
	 * @return
	 */
	public StepServiceVO doScript(
			ConnectionMode connectionMode,
			String deviceListId,
			Map<String, String> deviceInfo,
			ScriptInfo scriptInfo,
			Map<String, String> varMap,
			boolean sysTrigger,
			String triggerBy,
			String triggerRemark);
}
