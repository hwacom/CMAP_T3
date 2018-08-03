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
	name = "script_type",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"script_type_id"})
	}
)
public class ScriptType {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "script_type_id", unique = true)
	private String scriptTypeId;
	
	@Column(name = "script_type_code", nullable = false)
	private String scriptTypeCode;
	
	@Column(name = "script_type_name", nullable = false)
	private String scriptTypeName;
	
	@Column(name = "default_flag", nullable = true)
	private String defaultFlag;
	
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

	public ScriptType() {
		super();
	}

	public ScriptType(String scriptTypeId, String scriptTypeCode, String scriptTypeName, String defaultFlag,
			String deleteFlag, Timestamp deleteTime, String deleteBy, Timestamp createTime, String createBy,
			Timestamp updateTime, String updateBy) {
		super();
		this.scriptTypeId = scriptTypeId;
		this.scriptTypeCode = scriptTypeCode;
		this.scriptTypeName = scriptTypeName;
		this.defaultFlag = defaultFlag;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getScriptTypeId() {
		return scriptTypeId;
	}

	public void setScriptTypeId(String scriptTypeId) {
		this.scriptTypeId = scriptTypeId;
	}

	public String getScriptTypeCode() {
		return scriptTypeCode;
	}

	public void setScriptTypeCode(String scriptTypeCode) {
		this.scriptTypeCode = scriptTypeCode;
	}

	public String getScriptTypeName() {
		return scriptTypeName;
	}

	public void setScriptTypeName(String scriptTypeName) {
		this.scriptTypeName = scriptTypeName;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
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
