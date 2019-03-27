package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * 系統環境變數Entity
 * @author Ken Lin
 *
 */
@Entity
@Table(
		name = "sys_job_log",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"log_id"})
		}
		)
public class SysJobLog {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "log_id", unique = true)
	private String logId;

	@Column(name = "sched_name", nullable = false)
	private String schedName;

	@Column(name = "trigger_name", nullable = false)
	private String triggerName;

	@Column(name = "trigger_group", nullable = false)
	private String triggerGroup;

	@Column(name = "job_name", nullable = false)
	private String jobName;

	@Column(name = "job_group", nullable = false)
	private String jobGroup;

	@Column(name = "result", nullable = false)
	private String result;

	@Column(name = "records_num", nullable = true)
	private String recordsNum;

	@Column(name = "start_time", nullable = false)
	private Timestamp startTime;

	@Column(name = "end_time", nullable = false)
	private Timestamp endTime;

	@Column(name = "spend_time_in_seconds", nullable = true)
	private Integer spendTimeInSeconds;

	@Column(name = "cron_expression", nullable = false)
	private String cronExpression;

	@Column(name = "job_class_name", nullable = false)
	private String jobClassName;

	@Column(name = "job_data_map", nullable = true)
	private String jobDataMap;

	@Column(name = "priority", nullable = true)
	private Integer priority;

	@Column(name = "misfire_instr", nullable = true)
	private Short misfireInstr;

	@Column(name = "prev_fire_time", nullable = true)
	private Timestamp prevFireTime;

	@Column(name = "next_fire_time", nullable = true)
	private Timestamp nextFireTime;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	public SysJobLog() {
		super();
	}

	public SysJobLog(String logId, String schedName, String triggerName, String triggerGroup, String jobName,
			String jobGroup, String result, String recordsNum, Timestamp startTime, Timestamp endTime,
			Integer spendTimeInSeconds, String cronExpression, String jobClassName, String jobDataMap, Integer priority,
			Short misfireInstr, Timestamp prevFireTime, Timestamp nextFireTime, String remark, Timestamp createTime,
			String createBy) {
		super();
		this.logId = logId;
		this.schedName = schedName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.result = result;
		this.recordsNum = recordsNum;
		this.startTime = startTime;
		this.endTime = endTime;
		this.spendTimeInSeconds = spendTimeInSeconds;
		this.cronExpression = cronExpression;
		this.jobClassName = jobClassName;
		this.jobDataMap = jobDataMap;
		this.priority = priority;
		this.misfireInstr = misfireInstr;
		this.prevFireTime = prevFireTime;
		this.nextFireTime = nextFireTime;
		this.remark = remark;
		this.createTime = createTime;
		this.createBy = createBy;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getSchedName() {
		return schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRecordsNum() {
		return recordsNum;
	}

	public void setRecordsNum(String recordsNum) {
		this.recordsNum = recordsNum;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
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

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getJobDataMap() {
		return jobDataMap;
	}

	public void setJobDataMap(String jobDataMap) {
		this.jobDataMap = jobDataMap;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Short getMisfireInstr() {
		return misfireInstr;
	}

	public void setMisfireInstr(Short misfireInstr) {
		this.misfireInstr = misfireInstr;
	}

	public Timestamp getPrevFireTime() {
		return prevFireTime;
	}

	public void setPrevFireTime(Timestamp prevFireTime) {
		this.prevFireTime = prevFireTime;
	}

	public Timestamp getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Timestamp nextFireTime) {
		this.nextFireTime = nextFireTime;
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
}
