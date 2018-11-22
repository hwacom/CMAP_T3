package com.cmap.utils;

import java.util.List;

import com.cmap.dao.vo.ScriptDAOVO;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.StepServiceVO;

public interface ConnectUtils {

	public boolean connect(final String ipAddress, final Integer port) throws Exception;

	public boolean login(final String account, final String password) throws Exception;

	public List<String> sendCommands(List<ScriptDAOVO> scriptList, ConfigInfoVO configInfoVO, StepServiceVO ssVO) throws Exception;

	public boolean logout() throws Exception;

	public boolean disconnect() throws Exception;
}
