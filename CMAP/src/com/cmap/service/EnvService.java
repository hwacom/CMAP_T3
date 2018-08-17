package com.cmap.service;

import java.util.List;

import com.cmap.exception.ServiceLayerException;
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
	 * @throws ServiceLayerException
	 */
	public long countEnvSettingsByVO(EnvServiceVO esVO) throws ServiceLayerException;

	/**
	 * 依查詢條件查找資料
	 * @param esVO
	 * @param startRow
	 * @param pageLength
	 * @return
	 * @throws ServiceLayerException
	 */
	public List<EnvServiceVO> findEnvSettingsByVO(EnvServiceVO esVO, Integer startRow, Integer pageLength) throws ServiceLayerException;

	/**
	 * 新增/修改 Sys_Config_Setting 資料
	 * @param esVOs
	 * @throws ServiceLayerException
	 */
	public String addOrModifyEnvSettings(List<EnvServiceVO> esVOs) throws ServiceLayerException;

	/**
	 * 刪除 Sys_Config_Setting 資料
	 * @param settingIds
	 * @return
	 * @throws ServiceLayerException
	 */
	public String deleteEnvSettings(List<String> settingIds) throws ServiceLayerException;

	/**
	 * 刷新所有環境變數內容
	 * @return
	 * @throws ServiceLayerException
	 */
	public String refreshAllEnv() throws ServiceLayerException;
}
