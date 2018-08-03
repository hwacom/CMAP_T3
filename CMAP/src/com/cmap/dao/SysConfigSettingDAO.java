package com.cmap.dao;

import java.util.List;

import com.cmap.model.SysConfigSetting;

public interface SysConfigSettingDAO {

	public List<SysConfigSetting> findSysConfigSettingByName(List<String> settingNames);
	
	public List<SysConfigSetting> findAllSysConfigSetting();
	
	public void addSysConfigSetting(List<SysConfigSetting> models);
	
	public int updateSysConfigSetting(List<SysConfigSetting> models);
	
	public int deleteSysConfigSetting(List<SysConfigSetting> models);
}
