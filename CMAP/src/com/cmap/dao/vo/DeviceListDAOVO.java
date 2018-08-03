package com.cmap.dao.vo;

import java.util.List;

public class DeviceListDAOVO {

	private String queryGroup;
	private String queryDevice;
	
	private List<String> queryGroupList;
	private List<String> queryDeviceList;
	
	private String searchValue;
	private String orderColumn;
	private String orderDirection;
	
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
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	public String getOrderDirection() {
		return orderDirection;
	}
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
}
