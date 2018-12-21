package com.cmap.dao;

import java.util.List;

import com.cmap.model.DataPollerMapping;
import com.cmap.model.DataPollerSetting;

public interface DataPollerDAO extends BaseDAO {

	public DataPollerSetting findDataPollerSettingBySettingId(String settingId);

	public DataPollerMapping findDataPollerMappingBySettingIdAndSourceColumnName(String settingId, String sourceColumnName);

	public List<String> findTargetTableName(String settingId);
}
