package com.cmap.plugin.module.ip.mapping;

import java.util.Date;
import com.cmap.service.vo.CommonServiceVO;

public class IpMappingServiceVO extends CommonServiceVO {

	private String queryGroup;
	private String queryDevice;
	private String queryIp;
	private String queryMac;
	private String queryPort;

    private Date executeDate;
    private String dateTime;
    private String groupId;
    private String groupName;
    private String deviceId;
    private String deviceName;
    private String deviceModel;
    private String interfaceId;
    private String macAddress;
    private String ipAddress;
    private String portId;
    private String portName;

    private String showMsg;
    private String country;

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
	public String getQueryIp() {
		return queryIp;
	}
	public void setQueryIp(String queryIp) {
		this.queryIp = queryIp;
	}
	public String getQueryMac() {
		return queryMac;
	}
	public void setQueryMac(String queryMac) {
		this.queryMac = queryMac;
	}
	public String getQueryPort() {
		return queryPort;
	}
	public void setQueryPort(String queryPort) {
		this.queryPort = queryPort;
	}
	public Date getExecuteDate() {
		return executeDate;
	}
	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
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
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getInterfaceId() {
		return interfaceId;
	}
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
	public String getShowMsg() {
		return showMsg;
	}
	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
}
