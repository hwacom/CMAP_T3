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
	name = "config_version_info",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"version_id"})
	}
)
public class ConfigVersionInfo {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "version_id", unique = true)
	private String versionId;
	
	@Column(name = "group_id", nullable = false)
	private String groupId;
	
	@Column(name = "group_name", nullable = true)
	private String groupName;
	
	@Column(name = "device_id", nullable = false)
	private String deviceId;
	
	@Column(name = "device_name", nullable = true)
	private String deviceName;
	
	@Column(name = "system_version", nullable = false)
	private String systemVersion;
	
	@Column(name = "config_type", nullable = false)
	private String configType;
	
	@Column(name = "config_version", nullable = false)
	private String configVersion;
	
	@Column(name = "file_full_name", nullable = false)
	private String fileFullName;
	
	@Column(name = "delete_flag", nullable = false)
	private String deleteFlag;
	
	@Column(name = "delete_time", nullable = true)
	private Timestamp deleteTime;
	
	@Column(name = "delete_by", nullable = true)
	private String deleteBy;
	
	@Column(name = "create_time", nullable = true)
	private Timestamp createTime;
	
	@Column(name = "create_by", nullable = true)
	private String createBy;
	
	@Column(name = "update_time", nullable = true)
	private Timestamp updateTime;
	
	@Column(name = "update_by", nullable = true)
	private String updateBy;

	public ConfigVersionInfo() {
		super();
	}

	public ConfigVersionInfo(String versionId, String groupId, String groupName, String deviceId, String deviceName,
			String systemVersion, String configType, String configVersion, String fileFullName, String deleteFlag,
			Timestamp deleteTime, String deleteBy, Timestamp createTime, String createBy, Timestamp updateTime,
			String updateBy) {
		super();
		this.versionId = versionId;
		this.groupId = groupId;
		this.groupName = groupName;
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.systemVersion = systemVersion;
		this.configType = configType;
		this.configVersion = configVersion;
		this.fileFullName = fileFullName;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
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

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getConfigVersion() {
		return configVersion;
	}

	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}

	public String getFileFullName() {
		return fileFullName;
	}

	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Timestamp getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getDeleteBy() {
		return deleteBy;
	}

	public void setDeleteBy(String deleteBy) {
		this.deleteBy = deleteBy;
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
