package com.cmap.utils.impl;

import java.util.List;

import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.ScriptServiceVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.utils.ConnectUtils;

public class WmiUtils implements ConnectUtils {

	@Override
	public boolean connect(final String ipAddress, final Integer port) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean login(final String account, final String password) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> sendCommands(List<ScriptServiceVO> scriptList, ConfigInfoVO configInfoVO, StepServiceVO ssVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean logout() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
