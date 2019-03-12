package com.cmap.dao.vo;

import java.util.List;

public class DeviceDAOVO extends CommonDAOVO {

	private String queryGroup;
	private String queryDevice;
	
	private List<String> queryGroupList;
	private List<String> queryDeviceList;
	
	public String getQueryGroup() {
		return queryGroup;
	}
	public void setQueryGroup(String queryGroup) {
		this.queryGroup = queryGroup;
	}
	public String getQueryDevice() {
		return queryDevice;
	}
	public void setQueryDevice(String queryDevice) {
		this.queryDevice = queryDevice;
	}
	public List<String> getQueryGroupList() {
		return queryGroupList;
	}
	public void setQueryGroupList(List<String> queryGroupList) {
		this.queryGroupList = queryGroupList;
	}
	public List<String> getQueryDeviceList() {
		return queryDeviceList;
	}
	public void setQueryDeviceList(List<String> queryDeviceList) {
		this.queryDeviceList = queryDeviceList;
	}
}
