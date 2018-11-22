package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "provision_access_log",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"log_id"})
		}
		)
public class ProvisionAccessLog {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "log_id", unique = true)
	private String logId;

	@Column(name = "script_info_id", nullable = false)
	private String scriptInfoId;

	@Column(name = "script_code", nullable = true)
	private String scriptCode;

	@Column(name = "device_count", nullable = true)
	private Integer deviceCount;

	@Column(name = "device_ids", nullable = true)
	private String deviceIds;

	@Column(name = "var_key", nullable = true)
	private String varKey;

	@Column(name = "var_value", nullable = true)
	private String varValue;

	@Column(name = "ip_address", nullable = true)
	private String ipAddress;

	@Column(name = "mac_address", nullable = true)
	private String macAddress;

	@Column(name = "parameter_json", nullable = true)
	private String parameterJSON;

	@Column(name = "chk_result", nullable = true)
	private String chkResult;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public ProvisionAccessLog() {
		super();
	}

	public ProvisionAccessLog(String logId, String scriptInfoId, String scriptCode, Integer deviceCount,
			String deviceIds, String varKey, String varValue, String ipAddress, String macAddress, String parameterJSON,
			String chkResult, Timestamp createTime, String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.logId = logId;
		this.scriptInfoId = scriptInfoId;
		this.scriptCode = scriptCode;
		this.deviceCount = deviceCount;
		this.deviceIds = deviceIds;
		this.varKey = varKey;
		this.varValue = varValue;
		this.ipAddress = ipAddress;
		this.macAddress = macAddress;
		this.parameterJSON = parameterJSON;
		this.chkResult = chkResult;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
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

	public Integer getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(Integer deviceCount) {
		this.deviceCount = deviceCount;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
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

	public String getParameterJSON() {
		return parameterJSON;
	}

	public void setParameterJSON(String parameterJSON) {
		this.parameterJSON = parameterJSON;
	}

	public String getChkResult() {
		return chkResult;
	}

	public void setChkResult(String chkResult) {
		this.chkResult = chkResult;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
