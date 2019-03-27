package com.cmap.service.vo;

public class ProvisionAccessLogVO {

	private String logId
	;
	private boolean accessLogChkResult;
	private String accessLogChkMsg;

	private String scriptInfoId;
	private String scriptCode;
	private String deviceIds;
	private Integer deviceCount;
	private String varKey;
	private String varValue;
	private String ipAddress;
	private String macAddress;
	private String parameterJson;
	private String chkResult;

	public ProvisionAccessLogVO() {
		super();
	}

	public ProvisionAccessLogVO(String logId, boolean accessLogChkResult, String accessLogChkMsg, String scriptInfoId,
			String scriptCode, String deviceIds, Integer deviceCount, String varKey, String varValue, String ipAddress,
			String macAddress, String parameterJson, String chkResult) {
		super();
		this.logId = logId;
		this.accessLogChkResult = accessLogChkResult;
		this.accessLogChkMsg = accessLogChkMsg;
		this.scriptInfoId = scriptInfoId;
		this.scriptCode = scriptCode;
		this.deviceIds = deviceIds;
		this.deviceCount = deviceCount;
		this.varKey = varKey;
		this.varValue = varValue;
		this.ipAddress = ipAddress;
		this.macAddress = macAddress;
		this.parameterJson = parameterJson;
		this.chkResult = chkResult;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public boolean isAccessLogChkResult() {
		return accessLogChkResult;
	}

	public void setAccessLogChkResult(boolean accessLogChkResult) {
		this.accessLogChkResult = accessLogChkResult;
	}

	public String getAccessLogChkMsg() {
		return accessLogChkMsg;
	}

	public void setAccessLogChkMsg(String accessLogChkMsg) {
		this.accessLogChkMsg = accessLogChkMsg;
	}

	public String getScriptInfoId() {
		return scriptInfoId;
	}

	public void setScriptInfoId(String scriptInfoId) {
		this.scriptInfoId = scriptInfoId;
	}

	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public Integer getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(Integer deviceCount) {
		this.deviceCount = deviceCount;
	}

	public String getVarKey() {
		return varKey;
	}

	public void setVarKey(String varKey) {
		this.varKey = varKey;
	}

	public String getVarValue() {
		return varValue;
	}

	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getParameterJson() {
		return parameterJson;
	}

	public void setParameterJson(String parameterJson) {
		this.parameterJson = parameterJson;
	}

	public String getChkResult() {
		return chkResult;
	}

	public void setChkResult(String chkResult) {
		this.chkResult = chkResult;
	}
}
