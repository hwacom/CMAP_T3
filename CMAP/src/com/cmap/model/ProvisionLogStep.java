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
		name = "provision_log_step",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"log_step_id"})
		}
		)
public class ProvisionLogStep {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "log_step_id", unique = true)
	private String logStepId;

	@Column(name = "result", nullable = false)
	private String result;

	@Column(name = "message", nullable = true)
	private String message;

	@Column(name = "script_code", nullable = false)
	private String scriptCode;

	@Column(name = "begin_time", nullable = false)
	private Timestamp beginTime;

	@Column(name = "end_time", nullable = false)
	private Timestamp endTime;

	@Column(name = "spend_time_in_seconds", nullable = false)
	private Integer spendTimeInSeconds;

	@Column(name = "retry_times", nullable = false)
	private Integer retryTimes;

	@Column(name = "process_log", nullable = true)
	private String processLog;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "log_master_id", nullable = false)
	private ProvisionLogMaster provisionLogMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "provisionLogStep")
	private List<ProvisionLogDevice> provisionLogDevices;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "provisionLogStep")
	private List<ProvisionLogRetry> provisionLogRetry;

	public ProvisionLogStep() {
		super();
	}

	public ProvisionLogStep(String logStepId, String result, String message, String scriptCode, Timestamp beginTime,
			Timestamp endTime, Integer spendTimeInSeconds, Integer retryTimes, String processLog, String remark,
			Timestamp createTime, String createBy, ProvisionLogMaster provisionLogMaster,
			List<ProvisionLogDevice> provisionLogDevices) {
		super();
		this.logStepId = logStepId;
		this.result = result;
		this.message = message;
		this.scriptCode = scriptCode;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.spendTimeInSeconds = spendTimeInSeconds;
		this.retryTimes = retryTimes;
		this.processLog = processLog;
		this.remark = remark;
		this.createTime = createTime;
		this.createBy = createBy;
		this.provisionLogMaster = provisionLogMaster;
		this.provisionLogDevices = provisionLogDevices;
	}

	public String getLogStepId() {
		return logStepId;
	}

	public void setLogStepId(String logStepId) {
		this.logStepId = logStepId;
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

	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
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

	public Integer getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}

	public String getProcessLog() {
		return processLog;
	}

	public void setProcessLog(String processLog) {
		this.processLog = processLog;
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

	public List<ProvisionLogDevice> getProvisionLogDevices() {
		return provisionLogDevices;
	}

	public void setProvisionLogDevices(List<ProvisionLogDevice> provisionLogDevices) {
		this.provisionLogDevices = provisionLogDevices;
	}
}
