package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.DeviceListDAOVO;
import com.cmap.model.DeviceList;

public interface DeviceListDAO {

	public DeviceList findDeviceListByDeviceListId(String deviceListId);
	
	public DeviceList findDeviceListByGroupAndDeviceId(String groupId, String deviceId);
	
	public long countDeviceListAndLastestVersionByDAOVO(DeviceListDAOVO dlDAOVO);
			
	public List<Object[]> findDeviceListAndLastestVersionByDAOVO(DeviceListDAOVO dlDAOVO, Integer startRow, Integer pageLength);
	
	public void saveOrUpdateDeviceListByModel(List<DeviceList> entityList);
}
