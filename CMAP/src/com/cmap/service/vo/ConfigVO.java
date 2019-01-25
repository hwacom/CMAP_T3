package com.cmap.service.vo;

public class ConfigVO {

	private String settingType;
	private String systemVersion;
	private String deviceNameLike;
	private String deviceListId;
	private Integer contentLayer;
	private String contentStartRegex;
	private String contentEndRegex;
	private String action;
	private String remark;
	private String description;

	public String getSettingType() {
		return settingType;
	}
	public void setSettingType(String settingType) {
		this.settingType = settingType;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
	public String getDeviceNameLike() {
		return deviceNameLike;
	}
	public void setDeviceNameLike(String deviceNameLike) {
		this.deviceNameLike = deviceNameLike;
	}
	public String getDeviceListId() {
		return deviceListId;
	}
	public void setDeviceListId(String deviceListId) {
		this.deviceListId = deviceListId;
	}
	public Integer getContentLayer() {
		return contentLayer;
	}
	public void setContentLayer(Integer contentLayer) {
		this.contentLayer = contentLayer;
	}
	public String getContentStartRegex() {
		return contentStartRegex;
	}
	public void setContentStartRegex(String contentStartRegex) {
		this.contentStartRegex = contentStartRegex;
	}
	public String getContentEndRegex() {
		return contentEndRegex;
	}
	public void setContentEndRegex(String contentEndRegex) {
		this.contentEndRegex = contentEndRegex;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
