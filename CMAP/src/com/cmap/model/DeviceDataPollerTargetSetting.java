package com.cmap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name = "device_data_poller_script_setting",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"script_setting_id"})
		}
		)
public class DeviceDataPollerTargetSetting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "script_setting_id", unique = true)
	private Integer scriptSettingId;

	@Column(name = "script_setting_code", nullable = false)
	private String scriptSettingCode;

	@Column(name = "execute_order", nullable = false)
	private Integer executeOrder;

	@Column(name = "execute_script_code", nullable = false)
	private String executeScriptCode;

	@Column(name = "target_setting_code", nullable = false)
	private String targetSettingCode;

	public DeviceDataPollerTargetSetting() {
		super();
	}

	public DeviceDataPollerTargetSetting(Integer scriptSettingId, String scriptSettingCode, Integer executeOrder,
			String executeScriptCode, String targetSettingCode) {
		super();
		this.scriptSettingId = scriptSettingId;
		this.scriptSettingCode = scriptSettingCode;
		this.executeOrder = executeOrder;
		this.executeScriptCode = executeScriptCode;
		this.targetSettingCode = targetSettingCode;
	}

	public Integer getScriptSettingId() {
		return scriptSettingId;
	}

	public void setScriptSettingId(Integer scriptSettingId) {
		this.scriptSettingId = scriptSettingId;
	}

	public String getScriptSettingCode() {
		return scriptSettingCode;
	}

	public void setScriptSettingCode(String scriptSettingCode) {
		this.scriptSettingCode = scriptSettingCode;
	}

	public Integer getExecuteOrder() {
		return executeOrder;
	}

	public void setExecuteOrder(Integer executeOrder) {
		this.executeOrder = executeOrder;
	}

	public String getExecuteScriptCode() {
		return executeScriptCode;
	}

	public void setExecuteScriptCode(String executeScriptCode) {
		this.executeScriptCode = executeScriptCode;
	}

	public String getTargetSettingCode() {
		return targetSettingCode;
	}

	public void setTargetSettingCode(String targetSettingCode) {
		this.targetSettingCode = targetSettingCode;
	}
}
