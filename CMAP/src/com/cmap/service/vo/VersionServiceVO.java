package com.cmap.service.vo;

import java.util.List;

public class VersionServiceVO {

	private String queryGroup1;
	private String queryGroup2;
	private String queryDevice1;
	private String queryDevice2;
	private String queryDateBegin1;
	private String queryDateEnd1;
	private String queryDateBegin2;
	private String queryDateEnd2;
	
	private List<String> queryGroup1List;
	private List<String> queryGroup2List;
	private List<String> queryDevice1List;
	private List<String> queryDevice2List;
	
	private List<String> allGroupList;
	private List<String> allDeviceList;
	
	private Integer startNum;
	private Integer pageLength;
	private String searchColumn;
	private String searchValue;
	private String orderColumn;
	private String orderDirection;
	
	private String versionId;
	private String groupId;
	private String groupName;
	private String deviceId;
	private String deviceName;
	private String systemVersion;
	private String configVersion;
	private String backupTimeStr;
	
	public String getQueryGroup1() {
		return queryGroup1;
	}
	public void setQueryGroup1(String queryGroup1) {
		this.queryGroup1 = queryGroup1;
	}
	public String getQueryGroup2() {
		return queryGroup2;
	}
	public void setQueryGroup2(String queryGroup2) {
		this.queryGroup2 = queryGroup2;
	}
	public String getQueryDevice1() {
		return queryDevice1;
	}
	public void setQueryDevice1(String queryDevice1) {
		this.queryDevice1 = queryDevice1;
	}
	public String getQueryDevice2() {
		return queryDevice2;
	}
	public void setQueryDevice2(String queryDevice2) {
		this.queryDevice2 = queryDevice2;
	}
	public String getQueryDateBegin1() {
		return queryDateBegin1;
	}
	public void setQueryDateBegin1(String queryDateBegin1) {
		this.queryDateBegin1 = queryDateBegin1;
	}
	public String getQueryDateEnd1() {
		return queryDateEnd1;
	}
	public void setQueryDateEnd1(String queryDateEnd1) {
		this.queryDateEnd1 = queryDateEnd1;
	}
	public String getQueryDateBegin2() {
		return queryDateBegin2;
	}
	public void setQueryDateBegin2(String queryDateBegin2) {
		this.queryDateBegin2 = queryDateBegin2;
	}
	public String getQueryDateEnd2() {
		return queryDateEnd2;
	}
	public void setQueryDateEnd2(String queryDateEnd2) {
		this.queryDateEnd2 = queryDateEnd2;
	}
	public List<String> getQueryGroup1List() {
		return queryGroup1List;
	}
	public void setQueryGroup1List(List<String> queryGroup1List) {
		this.queryGroup1List = queryGroup1List;
	}
	public List<String> getQueryGroup2List() {
		return queryGroup2List;
	}
	public void setQueryGroup2List(List<String> queryGroup2List) {
		this.queryGroup2List = queryGroup2List;
	}
	public List<String> getQueryDevice1List() {
		return queryDevice1List;
	}
	public void setQueryDevice1List(List<String> queryDevice1List) {
		this.queryDevice1List = queryDevice1List;
	}
	public List<String> getQueryDevice2List() {
		return queryDevice2List;
	}
	public void setQueryDevice2List(List<String> queryDevice2List) {
		this.queryDevice2List = queryDevice2List;
	}
	public List<String> getAllGroupList() {
		return allGroupList;
	}
	public void setAllGroupList(List<String> allGroupList) {
		this.allGroupList = allGroupList;
	}
	public List<String> getAllDeviceList() {
		return allDeviceList;
	}
	public void setAllDeviceList(List<String> allDeviceList) {
		this.allDeviceList = allDeviceList;
	}
	public Integer getStartNum() {
		return startNum;
	}
	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}
	public Integer getPageLength() {
		return pageLength;
	}
	public void setPageLength(Integer pageLength) {
		this.pageLength = pageLength;
	}
	public String getSearchColumn() {
		return searchColumn;
	}
	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
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
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
	public String getConfigVersion() {
		return configVersion;
	}
	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}
	public String getBackupTimeStr() {
		return backupTimeStr;
	}
	public void setBackupTimeStr(String backupTimeStr) {
		this.backupTimeStr = backupTimeStr;
	}
}
