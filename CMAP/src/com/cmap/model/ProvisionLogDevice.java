package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "provision_log_device",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"log_device_id"})
		}
	)
public class ProvisionLogDevice {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "log_device_id", unique = true)
	private String logDeviceId;

	@Column(name = "order_num", nullable = false)
	private Integer orderNum;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "log_step_id", nullable = false)
	private ProvisionLogStep provisionLogStep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_list_id", nullable = false)
	private DeviceList deviceList;

	public ProvisionLogDevice() {
		super();
	}

	public ProvisionLogDevice(String logDeviceId, Integer orderNum, String remark, Timestamp createTime,
			String createBy, ProvisionLogStep provisionLogStep, DeviceList deviceList) {
		super();
		this.logDeviceId = logDeviceId;
		this.orderNum = orderNum;
		this.remark = remark;
		this.createTime = createTime;
		this.createBy = createBy;
		this.provisionLogStep = provisionLogStep;
		this.deviceList = deviceList;
	}

	public String getLogDeviceId() {
		return logDeviceId;
	}

	public void setLogDeviceId(String logDeviceId) {
		this.logDeviceId = logDeviceId;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
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

	public ProvisionLogStep getProvisionLogStep() {
		return provisionLogStep;
	}

	public void setProvisionLogStep(ProvisionLogStep provisionLogStep) {
		this.provisionLogStep = provisionLogStep;
	}

	public DeviceList getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(DeviceList deviceList) {
		this.deviceList = deviceList;
	}
}
