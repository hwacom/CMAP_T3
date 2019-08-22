package com.cmap.dao.vo;

import java.util.List;

public class DeviceDAOVO extends CommonDAOVO {

	private String queryGroup;
	private String queryDevice;

	private String groupId;
	private String deviceId;
	private String deviceIp;
	private String deviceModel;
	private String deviceLayer;

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
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceIp() {
        return deviceIp;
    }
    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }
    public String getDeviceModel() {
        return deviceModel;
    }
    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
    public String getDeviceLayer() {
        return deviceLayer;
    }
    public void setDeviceLayer(String deviceLayer) {
        this.deviceLayer = deviceLayer;
    }
}
