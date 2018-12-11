package com.cmap.dao;

import java.sql.Timestamp;
import java.util.List;

import com.cmap.dao.vo.DeviceListDAOVO;
import com.cmap.model.DeviceDetailInfo;
import com.cmap.model.DeviceDetailMapping;
import com.cmap.model.DeviceList;

public interface DeviceListDAO extends BaseDAO {

	public DeviceList findDeviceListByDeviceListId(String deviceListId);

	public DeviceList findDeviceListByGroupAndDeviceId(String groupId, String deviceId);

	public long countDeviceListAndLastestVersionByDAOVO(DeviceListDAOVO dlDAOVO);

	public List<Object[]> findDeviceListAndLastestVersionByDAOVO(DeviceListDAOVO dlDAOVO, Integer startRow, Integer pageLength);

	public void saveOrUpdateDeviceListByModel(List<DeviceList> entityList);

	public List<DeviceList> findDistinctDeviceListByGroupIdsOrDeviceIds(List<String> groupIds, List<String> deviceIds);

	public List<Object[]> getGroupIdAndNameByGroupIds(List<String> groupIds);

	public List<Object[]> getDeviceIdAndNameByDeviceIds(List<String> deviceIds);

	public List<DeviceDetailInfo> findDeviceDetailInfo(String deviceListId, String groupId, String deviceId, String infoName);

	public List<DeviceDetailMapping> findDeviceDetailMapping(String targetInfoName);

	public boolean deleteDeviceDetailInfoByInfoName(
			String deviceListId, String groupId, String deviceId, String infoName, Timestamp deleteTime, String deleteBy) throws Exception;
}
