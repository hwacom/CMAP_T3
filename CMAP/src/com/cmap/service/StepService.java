package com.cmap.service;

import java.util.List;
import java.util.Map;

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

	public StepServiceVO doBackupStep(String deviceListId, boolean jobTrigger);

	public StepServiceVO doBackupFileUpload2FTPStep(List<VersionServiceVO> vsVOs, ConfigInfoVO ciVO, boolean jobTrigger);

	public StepServiceVO doScript(String deviceListId, ScriptInfo scriptInfo, Map<String, String> varMap, boolean jobTrigger);
}
