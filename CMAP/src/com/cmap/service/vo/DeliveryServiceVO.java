package com.cmap.service.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmap.model.DeviceDetailInfo;

public class DeliveryServiceVO extends CommonServiceVO {

	private String queryGroup;
	private String queryDevice;
	private String queryScriptType;
	private String queryScriptTypeId;
	private String queryScriptTypeCode;
	private String queryScriptInfoId;
	private String queryTimeBegin;
	private String queryTimeEnd;
	private String queryLogStepId;
	private boolean onlySwitchPort = false;

	private String retMsg;
	private List<String> cmdOutputList = new ArrayList<>();

	//by [設備] 角度查詢結果
	private String deviceListId;
	private String groupId;
	private String groupName;
	private String groupEngName;
	private String deviceId;
	private String deviceName;
	private String deviceEngName;
	private String deviceIp;
	private String lastDeliveryTime;

	//by [腳本] 角度查詢結果
	private String scriptInfoId;
	private String scriptCode;
	private String scriptName;
	private String scriptTypeId;
	private String scriptTypeName;
	private String systemVersion;
	private String actionScript;
	private String actionScriptVariable;
	private String actionScriptRemark;
	private String checkScript;
	private String checkScriptRemark;
	private String checkKeyWord;
	private String scriptDescription;

	private String deliveryBeginTime;
	private String deliveryReason;
	private String deliveryResult;
	private String provisionLog;

	private String groupDeviceMenuJsonStr;

	private String createTimeStr;
	private String createBy;
	private String updateTimeStr;
	private String updateBy;

	private String accessLogId;
	private String deliveryParameters;
	private Integer deliveryStep;
	private Integer deliveryDeviceCount;
	private String deliveryVarKey;
	private String deliveryVarValue;

	private String logStepId;

	private Map<String, Map<String, List<DeviceDetailInfo>>> deviceVarMap = new HashMap<>();

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
	public String getQueryScriptType() {
		return queryScriptType;
	}
	public void setQueryScriptType(String queryScriptType) {
		this.queryScriptType = queryScriptType;
	}
	public String getQueryScriptTypeId() {
		return queryScriptTypeId;
	}
	public void setQueryScriptTypeId(String queryScriptTypeId) {
		this.queryScriptTypeId = queryScriptTypeId;
	}
	public String getQueryScriptTypeCode() {
		return queryScriptTypeCode;
	}
	public void setQueryScriptTypeCode(String queryScriptTypeCode) {
		this.queryScriptTypeCode = queryScriptTypeCode;
	}
	public String getDeviceListId() {
		return deviceListId;
	}
	public void setDeviceListId(String deviceListId) {
		this.deviceListId = deviceListId;
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
	public String getGroupEngName() {
		return groupEngName;
	}
	public void setGroupEngName(String groupEngName) {
		this.groupEngName = groupEngName;
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
	public String getDeviceEngName() {
		return deviceEngName;
	}
	public void setDeviceEngName(String deviceEngName) {
		this.deviceEngName = deviceEngName;
	}
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	public String getLastDeliveryTime() {
		return lastDeliveryTime;
	}
	public void setLastDeliveryTime(String lastDeliveryTime) {
		this.lastDeliveryTime = lastDeliveryTime;
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
	public String getScriptName() {
		return scriptName;
	}
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	public String getScriptTypeId() {
		return scriptTypeId;
	}
	public void setScriptTypeId(String scriptTypeId) {
		this.scriptTypeId = scriptTypeId;
	}
	public String getScriptTypeName() {
		return scriptTypeName;
	}
	public void setScriptTypeName(String scriptTypeName) {
		this.scriptTypeName = scriptTypeName;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
	public String getActionScript() {
		return actionScript;
	}
	public void setActionScript(String actionScript) {
		this.actionScript = actionScript;
	}
	public String getActionScriptRemark() {
		return actionScriptRemark;
	}
	public void setActionScriptRemark(String actionScriptRemark) {
		this.actionScriptRemark = actionScriptRemark;
	}
	public String getCheckScript() {
		return checkScript;
	}
	public void setCheckScript(String checkScript) {
		this.checkScript = checkScript;
	}
	public String getCheckScriptRemark() {
		return checkScriptRemark;
	}
	public void setCheckScriptRemark(String checkScriptRemark) {
		this.checkScriptRemark = checkScriptRemark;
	}
	public String getCheckKeyWord() {
		return checkKeyWord;
	}
	public void setCheckKeyWord(String checkKeyWord) {
		this.checkKeyWord = checkKeyWord;
	}
	public String getScriptDescription() {
		return scriptDescription;
	}
	public void setScriptDescription(String scriptDescription) {
		this.scriptDescription = scriptDescription;
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
	public String getQueryScriptInfoId() {
		return queryScriptInfoId;
	}
	public void setQueryScriptInfoId(String queryScriptInfoId) {
		this.queryScriptInfoId = queryScriptInfoId;
	}
	public String getGroupDeviceMenuJsonStr() {
		return groupDeviceMenuJsonStr;
	}
	public void setGroupDeviceMenuJsonStr(String groupDeviceMenuJsonStr) {
		this.groupDeviceMenuJsonStr = groupDeviceMenuJsonStr;
	}
	public String getActionScriptVariable() {
		return actionScriptVariable;
	}
	public void setActionScriptVariable(String actionScriptVariable) {
		this.actionScriptVariable = actionScriptVariable;
	}
	public String getDeliveryParameters() {
		return deliveryParameters;
	}
	public void setDeliveryParameters(String deliveryParameters) {
		this.deliveryParameters = deliveryParameters;
	}
	public String getAccessLogId() {
		return accessLogId;
	}
	public void setAccessLogId(String accessLogId) {
		this.accessLogId = accessLogId;
	}
	public Integer getDeliveryDeviceCount() {
		return deliveryDeviceCount;
	}
	public void setDeliveryDeviceCount(Integer deliveryDeviceCount) {
		this.deliveryDeviceCount = deliveryDeviceCount;
	}
	public String getDeliveryVarKey() {
		return deliveryVarKey;
	}
	public void setDeliveryVarKey(String deliveryVarKey) {
		this.deliveryVarKey = deliveryVarKey;
	}
	public String getDeliveryVarValue() {
		return deliveryVarValue;
	}
	public void setDeliveryVarValue(String deliveryVarValue) {
		this.deliveryVarValue = deliveryVarValue;
	}
	public Integer getDeliveryStep() {
		return deliveryStep;
	}
	public void setDeliveryStep(Integer deliveryStep) {
		this.deliveryStep = deliveryStep;
	}
	public Map<String, Map<String, List<DeviceDetailInfo>>> getDeviceVarMap() {
		return deviceVarMap;
	}
	public void setDeviceVarMap(Map<String, Map<String, List<DeviceDetailInfo>>> deviceVarMap) {
		this.deviceVarMap = deviceVarMap;
	}
	public String getQueryTimeBegin() {
		return queryTimeBegin;
	}
	public void setQueryTimeBegin(String queryTimeBegin) {
		this.queryTimeBegin = queryTimeBegin;
	}
	public String getQueryTimeEnd() {
		return queryTimeEnd;
	}
	public void setQueryTimeEnd(String queryTimeEnd) {
		this.queryTimeEnd = queryTimeEnd;
	}
	public String getDeliveryBeginTime() {
		return deliveryBeginTime;
	}
	public void setDeliveryBeginTime(String deliveryBeginTime) {
		this.deliveryBeginTime = deliveryBeginTime;
	}
	public String getDeliveryReason() {
		return deliveryReason;
	}
	public void setDeliveryReason(String deliveryReason) {
		this.deliveryReason = deliveryReason;
	}
	public String getDeliveryResult() {
		return deliveryResult;
	}
	public void setDeliveryResult(String deliveryResult) {
		this.deliveryResult = deliveryResult;
	}
	public String getProvisionLog() {
		return provisionLog;
	}
	public void setProvisionLog(String provisionLog) {
		this.provisionLog = provisionLog;
	}
	public String getLogStepId() {
		return logStepId;
	}
	public void setLogStepId(String logStepId) {
		this.logStepId = logStepId;
	}
	public String getQueryLogStepId() {
		return queryLogStepId;
	}
	public void setQueryLogStepId(String queryLogStepId) {
		this.queryLogStepId = queryLogStepId;
	}
	public boolean isOnlySwitchPort() {
		return onlySwitchPort;
	}
	public void setOnlySwitchPort(boolean onlySwitchPort) {
		this.onlySwitchPort = onlySwitchPort;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public List<String> getCmdOutputList() {
		return cmdOutputList;
	}
	public void setCmdOutputList(List<String> cmdOutputList) {
		this.cmdOutputList = cmdOutputList;
	}
}