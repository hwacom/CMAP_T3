package com.cmap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="qrtz_cron_triggers")
public class QrtzCronTriggers implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2078091996506025587L;

	@Id
	@Column(name = "SCHED_NAME", nullable = false)
	private String schedName;
	
	@Id
	@Column(name = "TRIGGER_NAME", nullable = false)
	private String triggerName;
	
	@Id
	@Column(name = "TRIGGER_GROUP", nullable = false)
	private String triggerGroup;
	
	@Column(name = "CRON_EXPRESSION", nullable = false)
	private String cronExpression;
	
	@Column(name = "TIME_ZONE_ID", nullable = true)
	private String timeZoneId;

	public QrtzCronTriggers() {
		super();
	}

	public QrtzCronTriggers(String schedName, String triggerName, String triggerGroup, String cronExpression,
			String timeZoneId) {
		super();
		this.schedName = schedName;
		this.triggerName = triggerName;
		this.triggerGroup = triggerGroup;
		this.cronExpression = cronExpression;
		this.timeZoneId = timeZoneId;
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
