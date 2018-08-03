package com.cmap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="qrtz_triggers")
public class QrtzTriggers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6680267159924382549L;

	@Id
	@Column(name = "SCHED_NAME", nullable = false)
	private String schedName;
	
	@Id
	@Column(name = "TRIGGER_NAME", nullable = false)
	private String triggerName;
	
	@Id
	@Column(name = "TRIGGER_GROUP", nullable = false)
	private String triggerGroup;
	
	@Column(name = "JOB_NAME", nullable = false)
	private String jobName;
	
	@Column(name = "JOB_GROUP", nullable = false)
	private String jobGroup;
	
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;
	
	@Column(name = "NEXT_FIRE_TIME", nullable = true)
	private Long nextFireTime;
	
	@Column(name = "PREV_FIRE_TIME", nullable = true)
	private Long prevFireTime;
	
	@Column(name = "PRIORITY", nullable = true)
	private Integer priority;
	
	@Column(name = "TRIGGER_STATE", nullable = false)
	private String triggerState;
	
	@Column(name = "TRIGGER_TYPE", nullable = false)
	private String triggerType;
	
	@Column(name = "START_TIME", nullable = false)
	private Long startTime;
	
	@Column(name = "END_TIME", nullable = true)
	private Long endTime;
	
	@Column(name = "CALENDAR_NAME", nullable = true)
	private String calendarName;
	
	@Column(name = "MISFIRE_INSTR", nullable = true)
	private Short misfireInstr;
	
	@Column(name = "JOB_DATA", nullable = true)
	private byte[] jobData;

	public QrtzTriggers() {
		super();
	}

	public QrtzTriggers(String schedName, String triggerName, String triggerGroup, String jobName, String jobGroup,
			String description, Long nextFireTime, Long prevFireTime, Integer priority, String triggerState,
			String triggerType, Long startTime, Long endTime, String calendarName, Short misfireInstr, byte[] jobData) {
		super();
		this.schedName = schedName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.description = description;
		this.nextFireTime = nextFireTime;
		this.prevFireTime = prevFireTime;
		this.priority = priority;
		this.triggerState = triggerState;
		this.triggerType = triggerType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.calendarName = calendarName;
		this.misfireInstr = misfireInstr;
		this.jobData = jobData;
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

	public Long getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Long nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public Long getPrevFireTime() {
		return prevFireTime;
	}

	public void setPrevFireTime(Long prevFireTime) {
		this.prevFireTime = prevFireTime;
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

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public Short getMisfireInstr() {
		return misfireInstr;
	}

	public void setMisfireInstr(Short misfireInstr) {
		this.misfireInstr = misfireInstr;
	}

	public byte[] getJobData() {
		return jobData;
	}

	public void setJobData(byte[] jobData) {
		this.jobData = jobData;
	}
}
