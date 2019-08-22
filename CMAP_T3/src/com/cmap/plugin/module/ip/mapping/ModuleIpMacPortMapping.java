package com.cmap.plugin.module.ip.mapping;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="module_ip_mac_port_mapping")
public class ModuleIpMacPortMapping implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "mapping_id", unique = true)
    private String mappingId;

    @Column(name = "job_id", nullable = false)
    private String jobId;
    
    @Column(name = "record_date", nullable = false)
    private Date recordDate;

    @Column(name = "record_time", nullable = false)
    private Date recordTime;
    
    @Column(name = "group_id", nullable = false)
    private String groupId;
    
    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "mac_address", nullable = false)
    private String macAddress;

    @Column(name = "port_id", nullable = false)
    private String portId;

    @Column(name = "remark", nullable = true)
    private String remark;

    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @Column(name = "create_by", nullable = false)
    private String createBy;

    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

    @Column(name = "update_by", nullable = false)
    private String updateBy;

    public ModuleIpMacPortMapping() {
        super();
    }

	public ModuleIpMacPortMapping(String mappingId, String jobId, Date recordDate, Date recordTime, String groupId,
			String deviceId, String ipAddress, String macAddress, String portId, String remark, Timestamp createTime,
			String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.mappingId = mappingId;
		this.jobId = jobId;
		this.recordDate = recordDate;
		this.recordTime = recordTime;
		this.groupId = groupId;
		this.deviceId = deviceId;
		this.ipAddress = ipAddress;
		this.macAddress = macAddress;
		this.portId = portId;
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

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
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
