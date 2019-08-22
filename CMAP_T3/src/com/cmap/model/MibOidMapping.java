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
		name = "mib_oid_mapping",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"mapping_id"})
		}
		)
public class MibOidMapping {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "mapping_id", unique = true)
	private String mappingId;

	@Column(name = "oid_name", nullable = false)
	private String oidName;

	@Column(name = "oid_value", nullable = false)
	private String oidValue;

	@Column(name = "poller_method", nullable = false)
	private String pollerMethod;

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

	public MibOidMapping() {
		super();
	}

    public MibOidMapping(String mappingId, String oidName, String oidValue, String pollerMethod,
            String remark, Timestamp createTime, String createBy, Timestamp updateTime,
            String updateBy) {
        super();
        this.mappingId = mappingId;
        this.oidName = oidName;
        this.oidValue = oidValue;
        this.pollerMethod = pollerMethod;
        this.remark = remark;
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

    public String getOidName() {
        return oidName;
    }

    public void setOidName(String oidName) {
        this.oidName = oidName;
    }

    public String getOidValue() {
        return oidValue;
    }

    public void setOidValue(String oidValue) {
        this.oidValue = oidValue;
    }

    public String getPollerMethod() {
        return pollerMethod;
    }

    public void setPollerMethod(String pollerMethod) {
        this.pollerMethod = pollerMethod;
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
