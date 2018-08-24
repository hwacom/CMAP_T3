package com.cmap.service.vo;

import java.sql.Timestamp;

import com.cmap.service.LogService.LogType;

public class LogServiceVO extends CommonServiceVO {

	private LogType queryLogType;
	private String queryLogId;

	// COMMON
	private String logId;
	private String details;

	// SYS_ERROR_LOG
	private Timestamp entryDate;
	private String entryDateStr;
	private String logger;
	private String logLevel;
	private String message;
	private String exception;

	// SYS_JOB_LOG
	private String schedName;
	private String triggerName;
	private String triggerGroup;
	private String jobName;
	private String jobGroup;
	private String result;
	private String recordsNum;
	private Timestamp startTime;
	private String startTimeStr;
	private Timestamp endTime;
	private String endTimeStr;
	private Integer spendTimeInSeconds;
	private String cronExpression;
	private String jobClassName;
	private String jobDataMap;
	private Integer priority;
	private Short misfireInstr;
	private Timestamp prevFireTime;
	private String prevFireTimeStr;
	private Timestamp nextFireTime;
	private String nextFireTimeStr;
	private String remark;

	public String printErrorLogDetailsWithHtml() {
		//		return StringUtils.replace(exception, "\r\n", "<br>");
		return exception;
	}

	public String printJobLogDetailsWithHtml() {
		StringBuffer sb = new StringBuffer();
		sb.append("[schedName]: ").append("\t").append(schedName).append("\n")
		.append("[triggerName]: ").append("\t").append(triggerName).append("\n")
		.append("[triggerGroup]: ").append("\t").append(triggerGroup).append("\n")
		.append("[jobName]: ").append("\t\t").append(jobName).append("\n")
		.append("[jobGroup]: ").append("\t\t").append(jobGroup).append("\n")
		.append("[jobClassName]: ").append("\t").append(jobClassName).append("\n")
		.append("[jobDataMap]: ").append("\t").append(jobDataMap).append("\n")
		.append("[priority]: ").append("\t\t").append(priority).append("\n")
		.append("[misfireInstr]: ").append("\t").append(misfireInstr).append("\n");
		return sb.toString();
	}

	public String getQueryLogId() {
		return queryLogId;
	}
	public void setQueryLogId(String queryLogId) {
		this.queryLogId = queryLogId;
	}
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public Timestamp getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = entryDate;
	}
	public String getEntryDateStr() {
		return entryDateStr;
	}
	public void setEntryDateStr(String entryDateStr) {
		this.entryDateStr = entryDateStr;
	}
	public String getLogger() {
		return logger;
	}
	public void setLogger(String logger) {
		this.logger = logger;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
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
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
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
	public String getPrevFireTimeStr() {
		return prevFireTimeStr;
	}
	public void setPrevFireTimeStr(String prevFireTimeStr) {
		this.prevFireTimeStr = prevFireTimeStr;
	}
	public Timestamp getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Timestamp nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public String getNextFireTimeStr() {
		return nextFireTimeStr;
	}
	public void setNextFireTimeStr(String nextFireTimeStr) {
		this.nextFireTimeStr = nextFireTimeStr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public LogType getQueryLogType() {
		return queryLogType;
	}
	public void setQueryLogType(LogType queryLogType) {
		this.queryLogType = queryLogType;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
}
