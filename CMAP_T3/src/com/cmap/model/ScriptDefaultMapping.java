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
	name = "script_default_mapping",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"mapping_id"})
	}
)
public class ScriptDefaultMapping {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "mapping_id", unique = true)
	private String mappingId;

	@Column(name = "script_type", nullable = false)
	private String scriptType;

	@Column(name = "device_model", nullable = false)
	private String deviceModel;

	@Column(name = "default_script_code", nullable = false)
	private String defaultScriptCode;

	@Column(name = "remark", nullable = true)
	private String remark;

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

	public ScriptDefaultMapping() {
		super();
	}

    public ScriptDefaultMapping(String mappingId, String scriptType, String deviceModel,
            String defaultScriptCode, String remark, String deleteFlag, Timestamp deleteTime,
            String deleteBy, Timestamp createTime, String createBy, Timestamp updateTime,
            String updateBy) {
        super();
        this.mappingId = mappingId;
        this.scriptType = scriptType;
        this.deviceModel = deviceModel;
        this.defaultScriptCode = defaultScriptCode;
        this.remark = remark;
        this.deleteFlag = deleteFlag;
        this.deleteTime = deleteTime;
        this.deleteBy = deleteBy;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDefaultScriptCode() {
        return defaultScriptCode;
    }

    public void setDefaultScriptCode(String defaultScriptCode) {
        this.defaultScriptCode = defaultScriptCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
