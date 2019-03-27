package com.cmap.service.vo;

public class EnvServiceVO extends CommonServiceVO {

	/*
	 * 查詢使用欄位
	 */
	private String inputSettingName;
	private String inputSettingValue;
	private String inputSettingRemark;
	
	/*
	 * 查詢結果呈顯使用欄位
	 */
	private String settingId;
	private String settingName;
	private String settingValue;
	private String settingRemark;
	private String createTimeStr;
	private String createBy;
	private String updateTimeStr;
	private String updateBy;
	private String _settingValue;
	private boolean isSame;
	private Integer differCount;
	
	/*
	 * 修改使用欄位
	 */
	private String modifySettingName;
	private String modifySettingValue;
	private String modifySettingRemark;
	
	public String getSettingId() {
		return settingId;
	}
	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}
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
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateTimeStr() {
		return updateTimeStr;
	}
	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getInputSettingName() {
		return inputSettingName;
	}
	public void setInputSettingName(String inputSettingName) {
		this.inputSettingName = inputSettingName;
	}
	public String getInputSettingValue() {
		return inputSettingValue;
	}
	public void setInputSettingValue(String inputSettingValue) {
		this.inputSettingValue = inputSettingValue;
	}
	public String getInputSettingRemark() {
		return inputSettingRemark;
	}
	public void setInputSettingRemark(String inputSettingRemark) {
		this.inputSettingRemark = inputSettingRemark;
	}
	public String getModifySettingValue() {
		return modifySettingValue;
	}
	public void setModifySettingValue(String modifySettingValue) {
		this.modifySettingValue = modifySettingValue;
	}
	public String getModifySettingRemark() {
		return modifySettingRemark;
	}
	public void setModifySettingRemark(String modifySettingRemark) {
		this.modifySettingRemark = modifySettingRemark;
	}
	public String getModifySettingName() {
		return modifySettingName;
	}
	public void setModifySettingName(String modifySettingName) {
		this.modifySettingName = modifySettingName;
	}
	public String get_settingValue() {
		return _settingValue;
	}
	public void set_settingValue(String _settingValue) {
		this._settingValue = _settingValue;
	}
	public boolean isSame() {
		return isSame;
	}
	public void setSame(boolean isSame) {
		this.isSame = isSame;
	}
	public Integer getDifferCount() {
		return differCount;
	}
	public void setDifferCount(Integer differCount) {
		this.differCount = differCount;
	}
}
