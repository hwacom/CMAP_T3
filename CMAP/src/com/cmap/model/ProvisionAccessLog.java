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
	private String deviceCount;

	@Column(name = "var_key", nullable = true)
	private String varKey;

	@Column(name = "var_value", nullable = true)
	private String varValue;

	@Column(name = "ip_address", nullable = true)
	private String ipAddress;

	@Column(name = "mac_address", nullable = true)
	private String macAddress;

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

	public ProvisionAccessLog(String logId, String scriptInfoId, String scriptCode, String deviceCount, String varKey,
			String varValue, String ipAddress, String macAddress, Timestamp createTime, String createBy,
			Timestamp updateTime, String updateBy) {
		super();
		this.logId = logId;
		this.scriptInfoId = scriptInfoId;
		this.scriptCode = scriptCode;
		this.deviceCount = deviceCount;
		this.varKey = varKey;
		this.varValue = varValue;
		this.ipAddress = ipAddress;
		this.macAddress = macAddress;
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

	public String getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(String deviceCount) {
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
