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
		name = "data_poller_target_setting",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"target_setting_id"})
		}
		)
public class DataPollerTargetSetting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "target_setting_id", unique = true)
	private Integer targetSettingId;

	@Column(name = "target_setting_code", nullable = false)
	private String targetSettingCode;

	@Column(name = "order_no", nullable = false)
	private Integer orderNo;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "group_id", nullable = true)
	private String groupId;

	@Column(name = "device_id", nullable = true)
	private String deviceId;

	@Column(name = "device_ip", nullable = true)
	private String deviceIp;

	@Column(name = "device_port", nullable = true)
	private Integer devicePort;

	@Column(name = "device_protocol", nullable = true)
	private String deviceProtocol;

	@Column(name = "script_setting_code", nullable = true)
	private String scriptSettingCode;

	@Column(name = "delete_flag", nullable = false)
	private String deleteFlag;

	@Column(name = "delete_time", nullable = true)
	private Timestamp deleteTime;

	@Column(name = "delete_by", nullable = true)
	private String deleteBy;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public DataPollerTargetSetting() {
		super();
	}

	public DataPollerTargetSetting(Integer targetSettingId, String targetSettingCode, Integer orderNo, String remark,
			String groupId, String deviceId, String deviceIp, Integer devicePort, String deviceProtocol,
			String scriptSettingCode, String deleteFlag, Timestamp deleteTime, String deleteBy, Timestamp createTime,
			String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.targetSettingId = targetSettingId;
		this.targetSettingCode = targetSettingCode;
		this.orderNo = orderNo;
		this.remark = remark;
		this.groupId = groupId;
		this.deviceId = deviceId;
		this.deviceIp = deviceIp;
		this.devicePort = devicePort;
		this.deviceProtocol = deviceProtocol;
		this.scriptSettingCode = scriptSettingCode;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
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

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}

	public String getDeviceProtocol() {
		return deviceProtocol;
	}

	public void setDeviceProtocol(String deviceProtocol) {
		this.deviceProtocol = deviceProtocol;
	}

	public String getScriptSettingCode() {
		return scriptSettingCode;
	}

	public void setScriptSettingCode(String scriptSettingCode) {
		this.scriptSettingCode = scriptSettingCode;
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
