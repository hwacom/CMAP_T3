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
		name = "device_data_poller_target_setting",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"target_setting_id"})
		}
		)
public class DeviceDataPollerTargetSetting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "target_setting_id", unique = true)
	private Integer targetSettingId;

	@Column(name = "target_setting_code", nullable = false)
	private String targetSettingCode;

	@Column(name = "target_table_name", nullable = false)
	private String targetTableName;

	@Column(name = "store_method", nullable = false)
	private String storeMethod;

	@Column(name = "special_var_setting", nullable = true)
	private String specialVarSetting;

	@Column(name = "insert_db_method", nullable = false)
	private String insertDbMethod;

	@Column(name = "insert_file_dir", nullable = true)
	private String insertFileDir;

	@Column(name = "store_file_dir", nullable = true)
	private String storeFileDir;

	@Column(name = "store_file_name_format", nullable = true)
	private String storeFileNameFormat;

	@Column(name = "record_by_day", nullable = false)
	private String recordByDay;

	@Column(name = "record_by_day_interval", nullable = true)
	private String recordByDayInterval;

	@Column(name = "record_by_day_clean", nullable = true)
	private String recordByDayClean;

	@Column(name = "record_by_day_refer_field", nullable = true)
	private String recordByDayReferField;

	@Column(name = "target_mapping_code", nullable = false)
	private String targetMappingCode;

	public DeviceDataPollerTargetSetting() {
		super();
	}

	public DeviceDataPollerTargetSetting(Integer targetSettingId, String targetSettingCode, String targetTableName,
			String storeMethod, String specialVarSetting, String insertDbMethod, String insertFileDir,
			String storeFileDir, String storeFileNameFormat, String recordByDay, String recordByDayInterval,
			String recordByDayClean, String recordByDayReferField, String targetMappingCode) {
		super();
		this.targetSettingId = targetSettingId;
		this.targetSettingCode = targetSettingCode;
		this.targetTableName = targetTableName;
		this.storeMethod = storeMethod;
		this.specialVarSetting = specialVarSetting;
		this.insertDbMethod = insertDbMethod;
		this.insertFileDir = insertFileDir;
		this.storeFileDir = storeFileDir;
		this.storeFileNameFormat = storeFileNameFormat;
		this.recordByDay = recordByDay;
		this.recordByDayInterval = recordByDayInterval;
		this.recordByDayClean = recordByDayClean;
		this.recordByDayReferField = recordByDayReferField;
		this.targetMappingCode = targetMappingCode;
	}

	public Integer getTargetSettingId() {
		return targetSettingId;
	}

	public void setTargetSettingId(Integer targetSettingId) {
		this.targetSettingId = targetSettingId;
	}

	public String getTargetSettingCode() {
		return targetSettingCode;
	}

	public void setTargetSettingCode(String targetSettingCode) {
		this.targetSettingCode = targetSettingCode;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public String getStoreMethod() {
		return storeMethod;
	}

	public void setStoreMethod(String storeMethod) {
		this.storeMethod = storeMethod;
	}

	public String getSpecialVarSetting() {
		return specialVarSetting;
	}

	public void setSpecialVarSetting(String specialVarSetting) {
		this.specialVarSetting = specialVarSetting;
	}

	public String getInsertDbMethod() {
		return insertDbMethod;
	}

	public void setInsertDbMethod(String insertDbMethod) {
		this.insertDbMethod = insertDbMethod;
	}

	public String getInsertFileDir() {
		return insertFileDir;
	}

	public void setInsertFileDir(String insertFileDir) {
		this.insertFileDir = insertFileDir;
	}

	public String getStoreFileDir() {
		return storeFileDir;
	}

	public void setStoreFileDir(String storeFileDir) {
		this.storeFileDir = storeFileDir;
	}

	public String getStoreFileNameFormat() {
		return storeFileNameFormat;
	}

	public void setStoreFileNameFormat(String storeFileNameFormat) {
		this.storeFileNameFormat = storeFileNameFormat;
	}

	public String getRecordByDay() {
		return recordByDay;
	}

	public void setRecordByDay(String recordByDay) {
		this.recordByDay = recordByDay;
	}

	public String getRecordByDayInterval() {
		return recordByDayInterval;
	}

	public void setRecordByDayInterval(String recordByDayInterval) {
		this.recordByDayInterval = recordByDayInterval;
	}

	public String getRecordByDayClean() {
		return recordByDayClean;
	}

	public void setRecordByDayClean(String recordByDayClean) {
		this.recordByDayClean = recordByDayClean;
	}

	public String getRecordByDayReferField() {
		return recordByDayReferField;
	}

	public void setRecordByDayReferField(String recordByDayReferField) {
		this.recordByDayReferField = recordByDayReferField;
	}

	public String getTargetMappingCode() {
		return targetMappingCode;
	}

	public void setTargetMappingCode(String targetMappingCode) {
		this.targetMappingCode = targetMappingCode;
	}
}
