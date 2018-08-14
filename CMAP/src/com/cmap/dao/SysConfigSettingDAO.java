package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.SysConfigSettingDAOVO;
import com.cmap.model.SysConfigSetting;

public interface SysConfigSettingDAO {

	/**
	 * 以單筆 Setting_ID 取得唯一且標記不為刪除的entity物件
	 * @param settingId
	 * @return
	 */
	public SysConfigSetting findSysConfigSettingById(String settingId);
	
	/**
	 * 以多筆 Setting_ID 取得對應且標記不為刪除的entity物件List
	 * @param settingIds
	 * @return
	 */
	public List<SysConfigSetting> findSysConfigSettingByIds(List<String> settingIds);
	
	/**
	 * 以 Setting_Name(允許多筆) 查找資料 (Notice: 此方法不判斷資料是否被標記刪除，將一律查出)
	 * @param settingNames
	 * @return
	 */
	public List<SysConfigSetting> findSysConfigSettingByName(List<String> settingNames);
	
	/**
	 * 不帶任何條件，查找出所有標記不為刪除的資料 (允許傳入分頁變數)
	 * @param startRow
	 * @param pageLength
	 * @return
	 */
	public List<SysConfigSetting> findAllSysConfigSetting(Integer startRow, Integer pageLength);
	
	/**
	 * 以 DAOVO ({@link com.cmap.dao.vo.SysConfigSettingDAOVO} 傳入篩選條件，查找出符合條件且標記不為刪除的資料
	 * @param daovo
	 * @param startRow
	 * @param pageLength
	 * @return
	 */
	public List<SysConfigSetting> findSysConfigSettingByVO(SysConfigSettingDAOVO daovo, Integer startRow, Integer pageLength);
	
	/**
	 * 以 DAOVO ({@link com.cmap.dao.vo.SysConfigSettingDAOVO} 傳入篩選條件，查找出符合條件且標記不為刪除的資料筆數
	 * @param daovo
	 * @return
	 */
	public long countSysConfigSettingByVO(SysConfigSettingDAOVO daovo);
	
	/**
	 * 儲存資料 by 單一entity物件
	 * @param model
	 */
	public void saveSysConfigSetting(SysConfigSetting model);
	
	/**
	 * 刪除資料 (允許一次傳入多筆 Setting_ID；採化學刪除法，將資料標記為已刪除但不實質刪除資料)
	 * @param versionIDs
	 * @param actionBy
	 * @return
	 */
	public Integer deleteSysConfigSetting(List<String> settingIds, String actionBy);
}
