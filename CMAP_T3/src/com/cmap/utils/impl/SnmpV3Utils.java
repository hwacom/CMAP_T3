package com.cmap.utils.impl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.VariableBinding;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.ScriptServiceVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.utils.ConnectUtils;

public class SnmpV3Utils implements ConnectUtils {
	private static Logger log = LoggerFactory.getLogger(SnmpV3Utils.class);

	@Override
	public boolean connect(final String udpAddress, final String community) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean login(String account, String password) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, List<VariableBinding>> pollData(List<String> oids, SNMP pollMethod) throws Exception {
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

    @Override
    public boolean connect(String ipAddress, Integer port) throws Exception {
        // TODO 自動產生的方法 Stub
        return false;
    }

    @Override
    public List<String> sendCommands(List<ScriptServiceVO> scriptList, ConfigInfoVO configInfoVO,
            StepServiceVO ssVO) throws Exception {
        // TODO 自動產生的方法 Stub
        return null;
    }

	@Override
	public Map<String, Map<String, String>> pollTableView(String oid, Map<String, String> entryMap) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
