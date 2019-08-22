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
		name = "device_port_info",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"info_id"})
		}
		)
public class DevicePortInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "info_id", unique = true)
	private String infoId;

	@Column(name = "group_id", nullable = false)
    private String groupId;

	@Column(name = "device_id", nullable = false)
    private String deviceId;

	@Column(name = "device_model", nullable = false)
	private String deviceModel;

	@Column(name = "port_id", nullable = false)
	private String portId;

	@Column(name = "port_name", nullable = false)
	private String portName;

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

	public DevicePortInfo() {
		super();
	}

    public DevicePortInfo(String infoId, String groupId, String deviceId, String deviceModel,
            String portId, String portName, String remark, Timestamp createTime, String createBy,
            Timestamp updateTime, String updateBy) {
        super();
        this.infoId = infoId;
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.deviceModel = deviceModel;
        this.portId = portId;
        this.portName = portName;
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

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
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
