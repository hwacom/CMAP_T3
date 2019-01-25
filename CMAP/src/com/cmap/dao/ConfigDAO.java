package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.model.ConfigContentSetting;
import com.cmap.model.ConfigVersionInfo;

public interface ConfigDAO {

	public long countConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO);

	public long countConfigVersionInfoByDAOVO4New(ConfigVersionInfoDAOVO cviDAOVO);

	public List<Object[]> findConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO, Integer startRow, Integer pageLength);

	public List<Object[]> findConfigVersionInfoByDAOVO4New(ConfigVersionInfoDAOVO cviDAOVO, Integer startRow, Integer pageLength);

	public Integer deleteConfigVersionInfoByVersionIds(List<String> versionIDs, String actionBy);

	public List<ConfigVersionInfo> findConfigVersionInfoByVersionIDs(List<String> versionIDs);

	public void insertConfigVersionInfo(ConfigVersionInfo configVersionInfo);

	public List<ConfigContentSetting> findConfigContentSetting(String settingType, String systemVersion, String deviceNameLike, String deviceListId);
}

