package com.cmap.service.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"scriptInfoId",
"scriptCode",
"groupId",
"deviceId",
"varKey",
"varValue",
"reason"
})
public class DeliveryParameterVO {

	@JsonProperty("scriptInfoId")
	private String scriptInfoId;
	@JsonProperty("scriptCode")
	private String scriptCode;
	@JsonProperty("groupId")
	private List<String> groupId = null;
	@JsonProperty("deviceId")
	private List<String> deviceId = null;
	@JsonProperty("varKey")
	private List<String> varKey = null;
	@JsonProperty("varValue")
	private List<List<String>> varValue = null;
	@JsonProperty("reason")
	private String reason;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();
	@JsonIgnore
	private Map<String, String> deviceInfo = new HashMap<>();

	@JsonProperty("scriptInfoId")
	public String getScriptInfoId() {
		return scriptInfoId;
	}

	@JsonProperty("scriptInfoId")
	public void setScriptInfoId(String scriptInfoId) {
		this.scriptInfoId = scriptInfoId;
	}

	@JsonProperty("scriptCode")
	public String getScriptCode() {
		return scriptCode;
	}

	@JsonProperty("scriptCode")
	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	@JsonProperty("groupId")
	public List<String> getGroupId() {
		return groupId;
	}

	@JsonProperty("groupId")
	public void setGroupId(List<String> groupId) {
		this.groupId = groupId;
	}

	@JsonProperty("deviceId")
	public List<String> getDeviceId() {
		return deviceId;
	}

	@JsonProperty("deviceId")
	public void setDeviceId(List<String> deviceId) {
		this.deviceId = deviceId;
	}

	@JsonProperty("varKey")
	public List<String> getVarKey() {
		return varKey;
	}

	@JsonProperty("varKey")
	public void setVarKey(List<String> varKey) {
		this.varKey = varKey;
	}

	@JsonProperty("varValue")
	public List<List<String>> getVarValue() {
		return varValue;
	}

	@JsonProperty("varValue")
	public void setVarValue(List<List<String>> varValue) {
		this.varValue = varValue;
	}

	@JsonProperty("reason")
	public String getReason() {
		return reason;
	}

	@JsonProperty("reason")
	public void setReason(String reason) {
		this.reason = reason;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public Map<String, String> getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(Map<String, String> deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("scriptInfoId", scriptInfoId).append("scriptCode", scriptCode).append("groupId", groupId).append("deviceId", deviceId).append("varKey", varKey).append("varValue", varValue).append("reason", reason).append("additionalProperties", additionalProperties).append("deviceInfo", deviceInfo).toString();
	}
}