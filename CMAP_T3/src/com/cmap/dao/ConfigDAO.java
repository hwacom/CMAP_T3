package com.cmap.dao;

import java.util.List;
import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.model.ConfigContentSetting;
import com.cmap.model.ConfigVersionDiffLog;
import com.cmap.model.ConfigVersionInfo;

public interface ConfigDAO extends BaseDAO {

	public long countConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO);

	public long countConfigVersionInfoByDAOVO4New(ConfigVersionInfoDAOVO cviDAOVO);

	public List<Object[]> findConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO,
	        Integer startRow, Integer pageLength);

	/**
	 * 查詢設備最新的組態備份版號相關資訊
	 * @param cviDAOVO
	 * @param startRow
	 * @param pageLength
	 * @return Object[0]: ConfigVersionInfo<br>Object[1]: DeviceList
	 */
	public List<Object[]> findConfigVersionInfoByDAOVO4New(ConfigVersionInfoDAOVO cviDAOVO,
	        Integer startRow, Integer pageLength);

	public Integer deleteConfigVersionInfoByVersionIds(List<String> versionIDs, String actionBy);

	/**
	 * 查詢版本資料 BY PK欄位
	 * @param versionIDs
	 * @return
	 */
	public List<ConfigVersionInfo> findConfigVersionInfoByVersionIDs(List<String> versionIDs);

	/**
	 * 查詢版本資料 BY UK欄位
	 * @param groupId
	 * @param deviceId
	 * @param configVersion
	 * @return
	 */
	public ConfigVersionInfo getConfigVersionInfoByUK(String groupId, String deviceId, String configVersion);

	public void insertConfigVersionInfo(ConfigVersionInfo configVersionInfo);

	public List<ConfigContentSetting> findConfigContentSetting(String settingType, String systemVersion,
	        String deviceNameLike, String deviceListId);

	public ConfigVersionDiffLog findConfigVersionDiffLogById(String diffLogId);
}

