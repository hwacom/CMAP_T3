package com.cmap.service.vo;

import java.util.ArrayList;
import java.util.List;

public class JobServiceVO extends CommonServiceVO {
	
	private String inputSchedType;
	private String inputSchedName;
	private String inputJobName;
	private String inputJobGroup;
	private String inputDescription;
	private String inputCronExpression;
	private Integer inputPriority;
	private String inputClassName;
	private List<String> inputDeviceListIds = new ArrayList<String>();
	private String inputConfigType;
	private String inputDataKeepDays;
	private Integer inputMisFirePolicy;
	private List<String> inputGroupIds = new ArrayList<String>();
	private List<String> inputDeviceIds = new ArrayList<String>();
	
	private List<JobServiceVO> jobKeySet = new ArrayList<JobServiceVO>();
	private String jobKeyName;
	private String jobKeyGroup;
	private List<String> groupIds = new ArrayList<String>();
	private List<String> deviceIds = new ArrayList<String>();
	private String groupIdsStr;
	private String deviceIdsStr;
	private String configType;
	
	private String schedType;
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
	private Short misfireInstr;
	private String _jobData;
	private String jobClassName;
	private String cronExpression;
	private String timeZoneId;
	
	public String getInputSchedType() {
		return inputSchedType;
	}
	public void setInputSchedType(String inputSchedType) {
		this.inputSchedType = inputSchedType;
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
	public Integer getInputMisFirePolicy() {
		return inputMisFirePolicy;
	}
	public void setInputMisFirePolicy(Integer inputMisFirePolicy) {
		this.inputMisFirePolicy = inputMisFirePolicy;
	}
	public List<String> getInputGroupIds() {
		return inputGroupIds;
	}
	public void setInputGroupIds(List<String> inputGroupIds) {
		this.inputGroupIds = inputGroupIds;
	}
	public List<String> getInputDeviceIds() {
		return inputDeviceIds;
	}
	public void setInputDeviceIds(List<String> inputDeviceIds) {
		this.inputDeviceIds = inputDeviceIds;
	}
	public List<JobServiceVO> getJobKeySet() {
		return jobKeySet;
	}
	public void setJobKeySet(List<JobServiceVO> jobKeySet) {
		this.jobKeySet = jobKeySet;
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
	public List<String> getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}
	public List<String> getDeviceIds() {
		return deviceIds;
	}
	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}
	public String getGroupIdsStr() {
		return groupIdsStr;
	}
	public void setGroupIdsStr(String groupIdsStr) {
		this.groupIdsStr = groupIdsStr;
	}
	public String getDeviceIdsStr() {
		return deviceIdsStr;
	}
	public void setDeviceIdsStr(String deviceIdsStr) {
		this.deviceIdsStr = deviceIdsStr;
	}
	public String getConfigType() {
		return configType;
	}
	public void setConfigType(String configType) {
		this.configType = configType;
	}
	public String getSchedType() {
		return schedType;
	}
	public void setSchedType(String schedType) {
		this.schedType = schedType;
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
	public Short getMisfireInstr() {
		return misfireInstr;
	}
	public void setMisfireInstr(Short misfireInstr) {
		this.misfireInstr = misfireInstr;
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
}
