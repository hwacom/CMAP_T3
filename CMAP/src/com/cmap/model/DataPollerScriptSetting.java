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
		name = "data_poller_script_setting",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"script_setting_id"})
		}
		)
public class DataPollerScriptSetting {

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

	@Column(name = "execute_key_setting", nullable = true)
	private String executeKeySetting;

	@Column(name = "execute_key_type", nullable = true)
	private String executeKeyType;

	@Column(name = "execute_reason", nullable = true)
	private String executeReason;

	public DataPollerScriptSetting() {
		super();
	}

	public DataPollerScriptSetting(Integer scriptSettingId, String scriptSettingCode, Integer executeOrder,
			String executeScriptCode, String executeKeySetting, String executeKeyType, String executeReason) {
		super();
		this.scriptSettingId = scriptSettingId;
		this.scriptSettingCode = scriptSettingCode;
		this.executeOrder = executeOrder;
		this.executeScriptCode = executeScriptCode;
		this.executeKeySetting = executeKeySetting;
		this.executeKeyType = executeKeyType;
		this.executeReason = executeReason;
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

	public String getExecuteKeySetting() {
		return executeKeySetting;
	}

	public void setExecuteKeySetting(String executeKeySetting) {
		this.executeKeySetting = executeKeySetting;
	}

	public String getExecuteKeyType() {
		return executeKeyType;
	}

	public void setExecuteKeyType(String executeKeyType) {
		this.executeKeyType = executeKeyType;
	}

	public String getExecuteReason() {
		return executeReason;
	}

	public void setExecuteReason(String executeReason) {
		this.executeReason = executeReason;
	}
}
