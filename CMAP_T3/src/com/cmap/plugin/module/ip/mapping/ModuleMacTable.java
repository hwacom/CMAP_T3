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
@Table(name="module_mac_table")
public class ModuleMacTable implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "data_id", unique = true)
    private String dataId;

    @Column(name = "job_id", nullable = false)
    private String jobId;
    
    @Column(name = "p_date", nullable = false)
    private Date pDate;

    @Column(name = "p_time", nullable = false)
    private Date pTime;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "mac_addr", nullable = false)
    private String macAddr;

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

    public ModuleMacTable() {
        super();
    }

	public ModuleMacTable(String dataId, String jobId, Date pDate, Date pTime, String groupId, String deviceId,
			String macAddr, String portId, String remark, Timestamp createTime, String createBy, Timestamp updateTime,
			String updateBy) {
		super();
		this.dataId = dataId;
		this.jobId = jobId;
		this.pDate = pDate;
		this.pTime = pTime;
		this.groupId = groupId;
		this.deviceId = deviceId;
		this.macAddr = macAddr;
		this.portId = portId;
		this.remark = remark;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Date getpDate() {
		return pDate;
	}

	public void setpDate(Date pDate) {
		this.pDate = pDate;
	}

	public Date getpTime() {
		return pTime;
	}

	public void setpTime(Date pTime) {
		this.pTime = pTime;
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

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
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
