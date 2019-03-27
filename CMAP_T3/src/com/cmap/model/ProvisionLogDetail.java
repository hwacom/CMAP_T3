package com.cmap.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "provision_log_detail",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"log_detail_id"})
		}
		)
public class ProvisionLogDetail {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "log_detail_id", unique = true)
	private String logDetailId;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "user_ip", nullable = false)
	private String userIp;

	@Column(name = "result", nullable = false)
	private String result;

	@Column(name = "message", nullable = true)
	private String message;

	@Column(name = "begin_time", nullable = false)
	private Timestamp beginTime;

	@Column(name = "end_time", nullable = false)
	private Timestamp endTime;

	@Column(name = "spend_time_in_seconds", nullable = false)
	private Integer spendTimeInSeconds;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "log_master_id", nullable = false)
	private ProvisionLogMaster provisionLogMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "provisionLogDetail")
	private List<ProvisionLogStep> provisionLogSteps;

	public ProvisionLogDetail() {
		super();
	}

	public ProvisionLogDetail(String logDetailId, String userName, String userIp, String result, String message,
			Timestamp beginTime, Timestamp endTime, Integer spendTimeInSeconds, String remark, Timestamp createTime,
			String createBy, ProvisionLogMaster provisionLogMaster, List<ProvisionLogStep> provisionLogSteps) {
		super();
		this.logDetailId = logDetailId;
		this.userName = userName;
		this.userIp = userIp;
		this.result = result;
		this.message = message;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.spendTimeInSeconds = spendTimeInSeconds;
		this.remark = remark;
		this.createTime = createTime;
		this.createBy = createBy;
		this.provisionLogMaster = provisionLogMaster;
		this.provisionLogSteps = provisionLogSteps;
	}

	public String getLogDetailId() {
		return logDetailId;
	}

	public void setLogDetailId(String logDetailId) {
		this.logDetailId = logDetailId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Integer getSpendTimeInSeconds() {
		return spendTimeInSeconds;
	}

	public void setSpendTimeInSeconds(Integer spendTimeInSeconds) {
		this.spendTimeInSeconds = spendTimeInSeconds;
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

	public ProvisionLogMaster getProvisionLogMaster() {
		return provisionLogMaster;
	}

	public void setProvisionLogMaster(ProvisionLogMaster provisionLogMaster) {
		this.provisionLogMaster = provisionLogMaster;
	}

	public List<ProvisionLogStep> getProvisionLogSteps() {
		return provisionLogSteps;
	}

	public void setProvisionLogSteps(List<ProvisionLogStep> provisionLogSteps) {
		this.provisionLogSteps = provisionLogSteps;
	}
}
