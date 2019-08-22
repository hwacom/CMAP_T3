package com.cmap.plugin.module.unauthorizeddhcp;

import com.cmap.service.vo.CommonServiceVO;

public class UnauthorizedDhcpServiceVO extends CommonServiceVO {

	private String queryGroup;
	private String queryDevice;
	
    private String dateTime;
    private String groupId;
    private String groupName;
    private String deviceId;
    private String deviceName;
    private String portId;
    private String portName;
    
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
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
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
	public String getPortId() {
		return portId;
	}
	public void setPortId(String portId) {
		this.portId = portId;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
}
