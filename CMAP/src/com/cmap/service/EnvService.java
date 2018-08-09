package com.cmap.service;

import java.util.List;

import com.cmap.service.vo.EnvServiceVO;

/**
 * 系統環境變數({@link com.cmap.model.SysConfigSetting})相關維護、刷新處理元件    
 * @author Ken Lin
 * 
 */
public interface EnvService {

	/**
	 * 取得查詢條件下資料筆數
	 * @param esVO
	 * @return
	 * @throws Exception
	 */
	public long countEnvSettingsByVO(EnvServiceVO esVO) throws Exception;
	
	/**
	 * 依查詢條件查找資料
	 * @param esVO
	 * @param startRow
	 * @param pageLength
	 * @return
	 * @throws Exception
	 */
	public List<EnvServiceVO> findEnvSettingsByVO(EnvServiceVO esVO, Integer startRow, Integer pageLength) throws Exception;
	
	/**
	 * 新增/修改 Sys_Config_Setting 資料
	 * @param esVOs
	 * @throws Exception
	 */
	public String addOrModifyEnvSettings(List<EnvServiceVO> esVOs) throws Exception;
	
	/**
	 * 刪除 Sys_Config_Setting 資料
	 * @param settingIds
	 * @return
	 * @throws Exception
	 */
	public String deleteEnvSettings(List<String> settingIds) throws Exception;
	
	/**
	 * 刷新所有環境變數內容
	 * @return
	 * @throws Exception
	 */
	public String refreshAllEnv() throws Exception;
}
