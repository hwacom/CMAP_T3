package com.cmap.service;

import java.util.List;

import com.cmap.comm.enums.RestoreMethod;
import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.VersionServiceVO;

public interface VersionService {

	/**
	 * 取得使用者權限下所有設備版本紀錄筆數
	 * @param groupList
	 * @param deviceList
	 * @param configType
	 * @return
	 * @throws ServiceLayerException
	 */
	public long countUserPermissionAllVersionInfo(List<String> groupList, List<String> deviceList, String configType) throws ServiceLayerException;

	/**
	 * 取得符合條件的資料筆數
	 * @param vsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public long countVersionInfo(VersionServiceVO vsVO) throws ServiceLayerException;

	/**
	 * 取得符合條件的設備清單及其最新備份版本號紀錄筆數
	 * @param vsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public long countDeviceList(VersionServiceVO vsVO) throws ServiceLayerException;

	/**
	 * 依查詢條件取得符合的資料
	 * @param vsVO
	 * @param startRow
	 * @param pageLength
	 * @return
	 * @throws ServiceLayerException
	 */
	public List<VersionServiceVO> findVersionInfo(VersionServiceVO vsVO, Integer startRow, Integer pageLength) throws ServiceLayerException;

	/**
	 * 取得符合條件的設備清單及其最新備份版本號紀錄資料
	 * @param vsVO
	 * @param startRow
	 * @param pageLength
	 * @return
	 * @throws ServiceLayerException
	 */
	public List<VersionServiceVO> findDeviceList(VersionServiceVO vsVO, Integer startRow, Integer pageLength) throws ServiceLayerException;

	/**
	 * 刪除系統內設備備份版本號紀錄(化學刪除非物理刪除)
	 * @param versionIDs
	 * @return
	 * @throws ServiceLayerException
	 */
	public boolean deleteVersionInfo(List<String> versionIDs) throws ServiceLayerException;

	/**
	 * 取得指定裝置的Config落地檔資訊 for UI查看Config檔內容 or 版本比對
	 * @param versionIDs
	 * @return
	 * @throws ServiceLayerException
	 */
	public List<VersionServiceVO> findConfigFilesInfo(List<String> versionIDs) throws ServiceLayerException;

	/**
	 * 取得FTP/TFTP上組態檔案內容
	 * @param vsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public VersionServiceVO getConfigFileContent(VersionServiceVO vsVO) throws ServiceLayerException;

	/**
	 * 取得裝置Config落地檔 for UI查看Config檔內容 or 版本比對
	 * @param voList
	 * @return
	 * @throws ServiceLayerException
	 */
	public VersionServiceVO compareConfigFiles(List<VersionServiceVO> voList) throws ServiceLayerException;

	/**
	 * 備份流程方法
	 * @param configType
	 * @param deviceListIDs
	 * @param jobTrigger
	 * @return
	 * @throws ServiceLayerException
	 */
	public VersionServiceVO backupConfig(String configType, List<String> deviceListIDs, boolean jobTrigger) throws ServiceLayerException;

	/**
	 * 組態還原流程方法
	 * @param recoverMethod
	 * @param vsVO
	 * @param sysTrigger
	 * @param triggerBy
	 * @param reason
	 * @return
	 * @throws ServiceLayerException
	 */
	public VersionServiceVO restoreConfig(RestoreMethod restoreMethod, String restoreType, VersionServiceVO vsVO, String triggerBy, String reason) throws ServiceLayerException;
}
