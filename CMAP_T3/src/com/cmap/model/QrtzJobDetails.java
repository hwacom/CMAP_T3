package com.cmap.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="qrtz_job_details")
public class QrtzJobDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 779394416325317000L;

	@Id
	@Column(name = "SCHED_NAME", nullable = false)
	private String schedName;
	
	@Id
	@Column(name = "JOB_NAME", nullable = false)
	private String jobName;

	@Id
	@Column(name = "JOB_GROUP", nullable = false)
	private String jobGroup;
	
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;
	
	@Column(name = "JOB_CLASS_NAME", nullable = true)
	private String jobClassName;
	
	@Column(name = "IS_DURABLE", nullable = true)
	private String isDurable;
	
	@Column(name = "IS_NONCONCURRENT", nullable = true)
	private String isNonconcurrent;
	
	@Column(name = "IS_UPDATE_DATA", nullable = true)
	private String isUpdateData;
	
	@Column(name = "REQUESTS_RECOVERY", nullable = true)
	private String requestsRecovery;
	
	@Column(name = "JOB_DATA", nullable = true)
	private byte[] jobData;

	public QrtzJobDetails() {
		super();
	}

	public QrtzJobDetails(String schedName, String jobName, String jobGroup, String description, String jobClassName,
			String isDurable, String isNonconcurrent, String isUpdateData, String requestsRecovery, byte[] jobData) {
		super();
		this.schedName = schedName;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.description = description;
		this.jobClassName = jobClassName;
		this.isDurable = isDurable;
		this.isNonconcurrent = isNonconcurrent;
		this.isUpdateData = isUpdateData;
		this.requestsRecovery = requestsRecovery;
		this.jobData = jobData;
	}

	public String getSchedName() {
		return schedName;
	}

	public void setSchedName(String schedName) {
		this.schedName = schedName;
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

	public String getJobClassName() {
		return jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getIsDurable() {
		return isDurable;
	}

	public void setIsDurable(String isDurable) {
		this.isDurable = isDurable;
	}

	public String getIsNonconcurrent() {
		return isNonconcurrent;
	}

	public void setIsNonconcurrent(String isNonconcurrent) {
		this.isNonconcurrent = isNonconcurrent;
	}

	public String getIsUpdateData() {
		return isUpdateData;
	}

	public void setIsUpdateData(String isUpdateData) {
		this.isUpdateData = isUpdateData;
	}

	public String getRequestsRecovery() {
		return requestsRecovery;
	}

	public void setRequestsRecovery(String requestsRecovery) {
		this.requestsRecovery = requestsRecovery;
	}

	public byte[] getJobData() {
		return jobData;
	}

	public void setJobData(byte[] jobData) {
		this.jobData = jobData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
