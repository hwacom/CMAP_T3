package com.cmap.service;

import java.util.List;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.VersionServiceVO;

public interface VersionService {

	public long countUserPermissionAllVersionInfo(List<String> groupList, List<String> deviceList, String configType) throws ServiceLayerException;

	public long countVersionInfo(VersionServiceVO vsVO) throws ServiceLayerException;

	public long countDeviceList(VersionServiceVO vsVO) throws ServiceLayerException;

	public List<VersionServiceVO> findVersionInfo(VersionServiceVO vsVO, Integer startRow, Integer pageLength) throws ServiceLayerException;

	public List<VersionServiceVO> findDeviceList(VersionServiceVO vsVO, Integer startRow, Integer pageLength) throws ServiceLayerException;

	public boolean deleteVersionInfo(List<String> versionIDs) throws ServiceLayerException;

	public List<VersionServiceVO> findConfigFilesInfo(List<String> versionIDs) throws ServiceLayerException;

	public VersionServiceVO getConfigFileContent(VersionServiceVO vsVO) throws ServiceLayerException;

	public VersionServiceVO compareConfigFiles(List<VersionServiceVO> voList) throws ServiceLayerException;

	/**
	 * 舊版備份流程方法
	 * @deprecated
	 * @param configType
	 * @param deviceListIDs
	 * @param jobTrigger
	 * @return
	 * @throws ServiceLayerException
	 */
	@Deprecated
	public VersionServiceVO _backupConfig(String configType, List<String> deviceListIDs, boolean jobTrigger) throws ServiceLayerException;

	/**
	 * 備份流程方法
	 * @param configType
	 * @param deviceListIDs
	 * @param jobTrigger
	 * @return
	 * @throws ServiceLayerException
	 */
	public VersionServiceVO backupConfig(String configType, List<String> deviceListIDs, boolean jobTrigger) throws ServiceLayerException;

	public VersionServiceVO recoverConfig(VersionServiceVO vsVO) throws ServiceLayerException;
}
