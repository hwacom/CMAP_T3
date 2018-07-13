package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.model.ConfigVersionInfo;

public interface ConfigVersionInfoDAO {
	
	public static final String[] NATIVE_FIELD_NAME = new String[] {
			"version_id", "group_id", "group_name", "device_id", "device_name", "system_version", "config_version", "create_time"
	};
	
	public static final String[] HQL_FIELD_NAME = new String[] {
			"versionId", "groupId", "groupName", "deviceId", "deviceName", "systemVersion", "configVersion", "createTime"
	};
	
	public long countConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO);
	
	public List<ConfigVersionInfo> findConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO, Integer startRow, Integer pageLength);
	
	public Integer deleteConfigVersionInfoByVersionIds(List<String> versionIDs, String actionBy);
}
