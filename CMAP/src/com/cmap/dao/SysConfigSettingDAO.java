package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.SysConfigSettingDAOVO;
import com.cmap.model.SysConfigSetting;

public interface SysConfigSettingDAO {

	public SysConfigSetting findSysConfigSettingById(String settingId);
	
	public List<SysConfigSetting> findSysConfigSettingByName(List<String> settingNames);
	
	public List<SysConfigSetting> findAllSysConfigSetting(Integer startRow, Integer pageLength);
	
	public List<SysConfigSetting> findSysConfigSettingByVO(SysConfigSettingDAOVO daovo, Integer startRow, Integer pageLength);
	
	public long countSysConfigSettingByVO(SysConfigSettingDAOVO daovo);
	
	public void saveSysConfigSetting(SysConfigSetting model);
	
	public Integer deleteSysConfigSetting(List<String> versionIDs, String actionBy);
}
