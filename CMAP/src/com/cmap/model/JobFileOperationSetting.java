package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name = "job_file_operation_setting",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"setting_id"})
		}
		)
public class JobFileOperationSetting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "setting_id", unique = true)
	private String settingId;

	@Column(name = "get_source_method", nullable = false)
	private String getSourceMethod;

	@Column(name = "remote_host_ip", nullable = true)
	private String remoteHostIp;

	@Column(name = "remote_host_port", nullable = true)
	private Integer remoteHostPort;

	@Column(name = "remote_login_account", nullable = true)
	private String remoteLoginAccount;

	@Column(name = "remote_login_password", nullable = true)
	private String remoteLoginPassword;

	@Column(name = "source_dir", nullable = false)
	private String sourceDir;

	@Column(name = "source_file_name_regex", nullable = false)
	private String sourceFileNameRegex;

	@Column(name = "source_file_size_regex", nullable = true)
	private String sourceFileSizeRegex;

	@Column(name = "do_action", nullable = false)
	private String doAction;

	@Column(name = "target_dir", nullable = true)
	private String targetDir;

	@Column(name = "target_file_name_format", nullable = true)
	private String targetFileNameFormat;

	@Column(name = "target_dir_by_day", nullable = true)
	private String targetDirByDay;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public JobFileOperationSetting() {
		super();
	}

	public JobFileOperationSetting(String settingId, String getSourceMethod, String remoteHostIp, Integer remoteHostPort,
			String remoteLoginAccount, String remoteLoginPassword, String sourceDir, String sourceFileNameRegex,
			String sourceFileSizeRegex, String doAction, String targetDir, String targetFileNameFormat,
			String targetDirByDay, Timestamp createTime, String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.settingId = settingId;
		this.getSourceMethod = getSourceMethod;
		this.remoteHostIp = remoteHostIp;
		this.remoteHostPort = remoteHostPort;
		this.remoteLoginAccount = remoteLoginAccount;
		this.remoteLoginPassword = remoteLoginPassword;
		this.sourceDir = sourceDir;
		this.sourceFileNameRegex = sourceFileNameRegex;
		this.sourceFileSizeRegex = sourceFileSizeRegex;
		this.doAction = doAction;
		this.targetDir = targetDir;
		this.targetFileNameFormat = targetFileNameFormat;
		this.targetDirByDay = targetDirByDay;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getSettingId() {
		return settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public String getGetSourceMethod() {
		return getSourceMethod;
	}

	public void setGetSourceMethod(String getSourceMethod) {
		this.getSourceMethod = getSourceMethod;
	}

	public String getRemoteHostIp() {
		return remoteHostIp;
	}

	public void setRemoteHostIp(String remoteHostIp) {
		this.remoteHostIp = remoteHostIp;
	}

	public Integer getRemoteHostPort() {
		return remoteHostPort;
	}

	public void setRemoteHostPort(Integer remoteHostPort) {
		this.remoteHostPort = remoteHostPort;
	}

	public String getRemoteLoginAccount() {
		return remoteLoginAccount;
	}

	public void setRemoteLoginAccount(String remoteLoginAccount) {
		this.remoteLoginAccount = remoteLoginAccount;
	}

	public String getRemoteLoginPassword() {
		return remoteLoginPassword;
	}

	public void setRemoteLoginPassword(String remoteLoginPassword) {
		this.remoteLoginPassword = remoteLoginPassword;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getSourceFileNameRegex() {
		return sourceFileNameRegex;
	}

	public void setSourceFileNameRegex(String sourceFileNameRegex) {
		this.sourceFileNameRegex = sourceFileNameRegex;
	}

	public String getSourceFileSizeRegex() {
		return sourceFileSizeRegex;
	}

	public void setSourceFileSizeRegex(String sourceFileSizeRegex) {
		this.sourceFileSizeRegex = sourceFileSizeRegex;
	}

	public String getDoAction() {
		return doAction;
	}

	public void setDoAction(String doAction) {
		this.doAction = doAction;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public String getTargetFileNameFormat() {
		return targetFileNameFormat;
	}

	public void setTargetFileNameFormat(String targetFileNameFormat) {
		this.targetFileNameFormat = targetFileNameFormat;
	}

	public String getTargetDirByDay() {
		return targetDirByDay;
	}

	public void setTargetDirByDay(String targetDirByDay) {
		this.targetDirByDay = targetDirByDay;
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

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
