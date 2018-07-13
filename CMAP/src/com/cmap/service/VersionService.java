package com.cmap.service;

import java.util.List;

import com.cmap.service.vo.VersionServiceVO;

public interface VersionService {

	public long countUserPermissionAllVersionInfo(List<String> groupList, List<String> deviceList);
	
	public long countVersionInfo(VersionServiceVO vsVO);
	
	public List<VersionServiceVO> findVersionInfo(VersionServiceVO vsVO, Integer startRow, Integer pageLength);
	
	public boolean deleteVersionInfo(List<String> versionIDs);
}
