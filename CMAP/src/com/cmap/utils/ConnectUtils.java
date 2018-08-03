package com.cmap.utils;

import java.util.List;

import com.cmap.dao.vo.ScriptListDAOVO;
import com.cmap.service.vo.ConfigInfoVO;

public interface ConnectUtils {

	public boolean connect(final String ipAddress, final Integer port) throws Exception;
	
	public boolean login(final String account, final String password) throws Exception;
	
	public List<String> sendCommands(List<ScriptListDAOVO> scriptList, ConfigInfoVO configInfoVO) throws Exception;
	
	public boolean logout() throws Exception;
	
	public boolean disconnect() throws Exception;
}
