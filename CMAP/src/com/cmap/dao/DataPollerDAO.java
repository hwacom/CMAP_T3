package com.cmap.dao;

import java.util.List;

import com.cmap.model.DataPollerSetting;

public interface DataPollerDAO extends BaseDAO {

	public DataPollerSetting findDataPollerSettingById(String settingId);

	public List<String> findTargetTableName(String settingId);
}
