package com.cmap.service;

import java.util.List;

import com.cmap.service.vo.VersionServiceVO;

public interface VersionService {
	
	public long countUserPermissionAllVersionInfo(List<String> groupList, List<String> deviceList, String configType) throws Exception;
	
	public long countVersionInfo(VersionServiceVO vsVO) throws Exception;
	
	public long countDeviceList(VersionServiceVO vsVO) throws Exception;
	
	public List<VersionServiceVO> findVersionInfo(VersionServiceVO vsVO, Integer startRow, Integer pageLength) throws Exception;
	
	public List<VersionServiceVO> findDeviceList(VersionServiceVO vsVO, Integer startRow, Integer pageLength) throws Exception;
	
	public boolean deleteVersionInfo(List<String> versionIDs) throws Exception;
	
	public List<VersionServiceVO> findConfigFilesInfo(List<String> versionIDs) throws Exception;
	
	public VersionServiceVO getConfigFileContent(VersionServiceVO vsVO) throws Exception;
	
	public VersionServiceVO compareConfigFiles(List<VersionServiceVO> voList) throws Exception;
	
	/**
	 * 舊版備份流程方法
	 * @deprecated
	 * @param configType
	 * @param deviceListIDs
	 * @param jobTrigger
	 * @return
	 * @throws Exception
	 */
	public VersionServiceVO _backupConfig(String configType, List<String> deviceListIDs, boolean jobTrigger) throws Exception;
	
	/**
	 * 備份流程方法
	 * @param configType
	 * @param deviceListIDs
	 * @param jobTrigger
	 * @return
	 * @throws Exception
	 */
	public VersionServiceVO backupConfig(String configType, List<String> deviceListIDs, boolean jobTrigger) throws Exception;
	
	public VersionServiceVO recoverConfig(VersionServiceVO vsVO) throws Exception;
}
