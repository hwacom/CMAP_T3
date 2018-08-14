package com.cmap.service;

import java.util.List;

import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.VersionServiceVO;

public interface StepService {

	public boolean doBackupStep(String deviceListId, boolean jobTrigger);
	
	public void doBackupFileUpload2FTPStep(List<VersionServiceVO> vsVOs, ConfigInfoVO ciVO, boolean jobTrigger);
}
