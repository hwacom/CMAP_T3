package com.cmap.service.vo;

import java.util.ArrayList;
import java.util.List;

public class JobServiceVO {
	
	public String inputSchedType;
	public String inputSchedName;
	public String inputJobName;
	public String inputJobGroup;
	public String inputDescription;
	public String inputCronExpression;
	public Integer inputPriority;
	public String inputClassName;
	public List<String> inputDeviceListIds;
	public String inputConfigType;
	public String inputDataKeepDays;
	public Integer inputMisFirePolicy;
	
	public List<JobServiceVO> jobKeySet = new ArrayList<JobServiceVO>();
	public String jobKeyName;
	public String jobKeyGroup;
	
	private String schedName;
	private String triggerName;
	private String triggerGroup;
	private String jobName;
	private String jobGroup;
	private String description;
	private String _nextFireTime;
	private String _preFireTime;
	private Integer priority;
	private String triggerState;
	private String triggerType;
	private String _startTime;
	private String _endTime;
	private Short misFireInstr;
	private String _jobData;
	private String jobClassName;
	private String cronExpression;
	private String timeZoneId;

	private Integer startNum;
	private Integer pageLength;
	private String searchColumn;
	private String searchValue;
	private String orderColumn;
	private String orderDirection;
	
	public Integer getStartNum() {
		return startNum;
	}
	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}
	public Integer getPageLength() {
		return pageLength;
	}
	public void setPageLength(Integer pageLength) {
		this.pageLength = pageLength;
	}
	public String getSearchColumn() {
		return searchColumn;
	}
	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	public String getOrderDirection() {
		return orderDirection;
	}
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String get_nextFireTime() {
		return _nextFireTime;
	}
	public void set_nextFireTime(String _nextFireTime) {
		this._nextFireTime = _nextFireTime;
	}
	public String get_preFireTime() {
		return _preFireTime;
	}
	public void set_preFireTime(String _preFireTime) {
		this._preFireTime = _preFireTime;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public String get_startTime() {
		return _startTime;
	}
	public void set_startTime(String _startTime) {
		this._startTime = _startTime;
	}
	public String get_endTime() {
		return _endTime;
	}
	public void set_endTime(String _endTime) {
		this._endTime = _endTime;
	}
	public Short getMisFireInstr() {
		return misFireInstr;
	}
	public void setMisFireInstr(Short misFireInstr) {
		this.misFireInstr = misFireInstr;
	}
	public String get_jobData() {
		return _jobData;
	}
	public void set_jobData(String _jobData) {
		this._jobData = _jobData;
	}
	public String getJobClassName() {
		return jobClassName;
	}
	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getTimeZoneId() {
		return timeZoneId;
	}
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	public String getInputSchedName() {
		return inputSchedName;
	}
	public void setInputSchedName(String inputSchedName) {
		this.inputSchedName = inputSchedName;
	}
	public String getInputJobName() {
		return inputJobName;
	}
	public void setInputJobName(String inputJobName) {
		this.inputJobName = inputJobName;
	}
	public String getInputJobGroup() {
		return inputJobGroup;
	}
	public void setInputJobGroup(String inputJobGroup) {
		this.inputJobGroup = inputJobGroup;
	}
	public String getInputDescription() {
		return inputDescription;
	}
	public void setInputDescription(String inputDescription) {
		this.inputDescription = inputDescription;
	}
	public String getInputCronExpression() {
		return inputCronExpression;
	}
	public void setInputCronExpression(String inputCronExpression) {
		this.inputCronExpression = inputCronExpression;
	}
	public Integer getInputPriority() {
		return inputPriority;
	}
	public void setInputPriority(Integer inputPriority) {
		this.inputPriority = inputPriority;
	}
	public String getJobKeyName() {
		return jobKeyName;
	}
	public void setJobKeyName(String jobKeyName) {
		this.jobKeyName = jobKeyName;
	}
	public String getJobKeyGroup() {
		return jobKeyGroup;
	}
	public void setJobKeyGroup(String jobKeyGroup) {
		this.jobKeyGroup = jobKeyGroup;
	}
	public String getInputClassName() {
		return inputClassName;
	}
	public void setInputClassName(String inputClassName) {
		this.inputClassName = inputClassName;
	}
	public List<String> getInputDeviceListIds() {
		return inputDeviceListIds;
	}
	public void setInputDeviceListIds(List<String> inputDeviceListIds) {
		this.inputDeviceListIds = inputDeviceListIds;
	}
	public String getInputConfigType() {
		return inputConfigType;
	}
	public void setInputConfigType(String inputConfigType) {
		this.inputConfigType = inputConfigType;
	}
	public String getInputDataKeepDays() {
		return inputDataKeepDays;
	}
	public void setInputDataKeepDays(String inputDataKeepDays) {
		this.inputDataKeepDays = inputDataKeepDays;
	}
	public String getInputSchedType() {
		return inputSchedType;
	}
	public void setInputSchedType(String inputSchedType) {
		this.inputSchedType = inputSchedType;
	}
	public Integer getInputMisFirePolicy() {
		return inputMisFirePolicy;
	}
	public void setInputMisFirePolicy(Integer inputMisFirePolicy) {
		this.inputMisFirePolicy = inputMisFirePolicy;
	}
	public List<JobServiceVO> getJobKeySet() {
		return jobKeySet;
	}
	public void setJobKeySet(List<JobServiceVO> jobKeySet) {
		this.jobKeySet = jobKeySet;
	}
}
