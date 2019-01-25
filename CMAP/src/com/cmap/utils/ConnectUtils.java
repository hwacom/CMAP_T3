package com.cmap.utils;

import java.util.List;

import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.ScriptServiceVO;
import com.cmap.service.vo.StepServiceVO;

public interface ConnectUtils {

	public boolean connect(final String ipAddress, final Integer port) throws Exception;

	public boolean login(final String account, final String password) throws Exception;

	public List<String> sendCommands(List<ScriptServiceVO> scriptList, ConfigInfoVO configInfoVO, StepServiceVO ssVO) throws Exception;

	public boolean logout() throws Exception;

	public boolean disconnect() throws Exception;
}
