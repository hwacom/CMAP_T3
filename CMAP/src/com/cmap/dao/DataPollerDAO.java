package com.cmap.dao;

import java.util.List;

import com.cmap.model.DataPollerMapping;
import com.cmap.model.DataPollerScriptSetting;
import com.cmap.model.DataPollerSetting;
import com.cmap.model.DataPollerTargetSetting;

public interface DataPollerDAO extends BaseDAO {

	public DataPollerSetting findDataPollerSettingBySettingId(String settingId);

	public List<DataPollerMapping> findDataPollerMappingByMappingCode(String mappingCode);

	public List<String> findTargetTableName(String settingId);

	public List<DataPollerSetting> findDataPollerSettingByDataType(String dataType);

	public DataPollerSetting findDataPollerSettingByDataTypeAndQueryId(String dataType, String queryId);

	public List<DataPollerTargetSetting> findDataPollerTargetSettingByTargetSettingCode(String targetSettingCode);

	public List<DataPollerScriptSetting> findDataPollerScriptSettingByScriptSettingCode(String scriptSettingCode);

}
