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

	public DataPollerScriptSetting() {
		super();
	}

	public DataPollerScriptSetting(Integer scriptSettingId, String scriptSettingCode, Integer executeOrder,
			String executeScriptCode) {
		super();
		this.scriptSettingId = scriptSettingId;
		this.scriptSettingCode = scriptSettingCode;
		this.executeOrder = executeOrder;
		this.executeScriptCode = executeScriptCode;
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
}
