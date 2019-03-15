package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name = "device_login_info",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"info_id"})
		}
		)
public class DeviceLoginInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "info_id", unique = true)
	private String infoId;

	@Column(name = "device_list_id", nullable = true)
	private String deviceListId;

	@Column(name = "group_id", nullable = true)
	private String groupId;

	@Column(name = "device_id", nullable = true)
	private String deviceId;

	@Column(name = "connection_mode", nullable = true)
	private String connectionMode;

	@Column(name = "login_account", nullable = true)
	private String loginAccount;

	@Column(name = "login_password", nullable = true)
	private String loginPassword;

	@Column(name = "enable_password", nullable = true)
	private String enablePassword;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "create_time", nullable = true)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = true)
	private String createBy;

	@Column(name = "update_time", nullable = true)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = true)
	private String updateBy;

	public DeviceLoginInfo() {
		super();
	}

	public DeviceLoginInfo(String infoId, String deviceListId, String groupId, String deviceId, String connectionMode,
			String loginAccount, String loginPassword, String enablePassword, String remark, Timestamp createTime,
			String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.infoId = infoId;
		this.deviceListId = deviceListId;
		this.groupId = groupId;
		this.deviceId = deviceId;
		this.connectionMode = connectionMode;
		this.loginAccount = loginAccount;
		this.loginPassword = loginPassword;
		this.enablePassword = enablePassword;
		this.remark = remark;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getConnectionMode() {
		return connectionMode;
	}

	public void setConnectionMode(String connectionMode) {
		this.connectionMode = connectionMode;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getEnablePassword() {
		return enablePassword;
	}

	public void setEnablePassword(String enablePassword) {
		this.enablePassword = enablePassword;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
