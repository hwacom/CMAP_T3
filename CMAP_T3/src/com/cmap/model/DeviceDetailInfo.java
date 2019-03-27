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
		name = "device_detail_info",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"info_id"})
		}
		)
public class DeviceDetailInfo {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "info_id", unique = true)
	private String infoId;

	@Column(name = "device_list_id", nullable = false)
	private String deviceListId;

	@Column(name = "group_id", nullable = false)
	private String groupId;

	@Column(name = "device_id", nullable = false)
	private String deviceId;

	@Column(name = "info_name", nullable = false)
	private String infoName;

	@Column(name = "info_value", nullable = true)
	private String infoValue;

	@Column(name = "info_order", nullable = true)
	private Integer infoOrder;

	@Column(name = "info_remark", nullable = true)
	private String infoRemark;

	@Column(name = "delete_flag", nullable = false)
	private String deleteFlag = "N";

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

	public DeviceDetailInfo() {
		super();
	}

	public DeviceDetailInfo(String infoId, String deviceListId, String groupId, String deviceId, String infoName,
			String infoValue, Integer infoOrder, String infoRemark, String deleteFlag, Timestamp deleteTime,
			String deleteBy, Timestamp createTime, String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.infoId = infoId;
		this.deviceListId = deviceListId;
		this.groupId = groupId;
		this.deviceId = deviceId;
		this.infoName = infoName;
		this.infoValue = infoValue;
		this.infoOrder = infoOrder;
		this.infoRemark = infoRemark;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
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

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getInfoValue() {
		return infoValue;
	}

	public void setInfoValue(String infoValue) {
		this.infoValue = infoValue;
	}

	public Integer getInfoOrder() {
		return infoOrder;
	}

	public void setInfoOrder(Integer infoOrder) {
		this.infoOrder = infoOrder;
	}

	public String getInfoRemark() {
		return infoRemark;
	}

	public void setInfoRemark(String infoRemark) {
		this.infoRemark = infoRemark;
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