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
		name = "device_data_poller_target_mapping",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"target_mapping_id"})
		}
		)
public class DeviceDataPollerTargetMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "target_mapping_id", unique = true)
	private Integer targetMappingId;

	@Column(name = "target_mapping_code", nullable = false)
	private String targetMappingCode;

	@Column(name = "source_string", nullable = false)
	private String sourceString;

	@Column(name = "split_by", nullable = false)
	private String splitBy;

	@Column(name = "get_value_index", nullable = false)
	private Integer getValueIndex;

	@Column(name = "target_info_name", nullable = false)
	private String targetInfoName;

	@Column(name = "target_info_remark", nullable = true)
	private String targetInfoRemark;

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

	public DeviceDataPollerTargetMapping() {
		super();
	}

	public DeviceDataPollerTargetMapping(Integer targetMappingId, String targetMappingCode, String sourceString,
			String splitBy, Integer getValueIndex, String targetInfoName, String targetInfoRemark, String deleteFlag,
			Timestamp deleteTime, String deleteBy, Timestamp createTime, String createBy, Timestamp updateTime,
			String updateBy) {
		super();
		this.targetMappingId = targetMappingId;
		this.targetMappingCode = targetMappingCode;
		this.sourceString = sourceString;
		this.splitBy = splitBy;
		this.getValueIndex = getValueIndex;
		this.targetInfoName = targetInfoName;
		this.targetInfoRemark = targetInfoRemark;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public Integer getTargetMappingId() {
		return targetMappingId;
	}

	public void setTargetMappingId(Integer targetMappingId) {
		this.targetMappingId = targetMappingId;
	}

	public String getTargetMappingCode() {
		return targetMappingCode;
	}

	public void setTargetMappingCode(String targetMappingCode) {
		this.targetMappingCode = targetMappingCode;
	}

	public String getSourceString() {
		return sourceString;
	}

	public void setSourceString(String sourceString) {
		this.sourceString = sourceString;
	}

	public String getSplitBy() {
		return splitBy;
	}

	public void setSplitBy(String splitBy) {
		this.splitBy = splitBy;
	}

	public Integer getGetValueIndex() {
		return getValueIndex;
	}

	public void setGetValueIndex(Integer getValueIndex) {
		this.getValueIndex = getValueIndex;
	}

	public String getTargetInfoName() {
		return targetInfoName;
	}

	public void setTargetInfoName(String targetInfoName) {
		this.targetInfoName = targetInfoName;
	}

	public String getTargetInfoRemark() {
		return targetInfoRemark;
	}

	public void setTargetInfoRemark(String targetInfoRemark) {
		this.targetInfoRemark = targetInfoRemark;
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
