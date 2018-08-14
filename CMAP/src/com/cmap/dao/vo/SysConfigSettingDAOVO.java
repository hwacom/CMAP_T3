package com.cmap.dao.vo;

public class SysConfigSettingDAOVO extends CommonDAOVO {

	private String settingName;
	private String settingValue;
	private String settingRemark;
	
	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public String getSettingValue() {
		return settingValue;
	}
	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}
	public String getSettingRemark() {
		return settingRemark;
	}
	public void setSettingRemark(String settingRemark) {
		this.settingRemark = settingRemark;
	}
}
