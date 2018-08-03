package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
	name = "sys_config_setting",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"setting_id"})
	}
)
public class SysConfigSetting {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "setting_id", unique = true)
	private String settingId;
	
	@Column(name = "setting_name", nullable = false)
	private String settingName;
	
	@Column(name = "setting_value", nullable = false)
	private String settingValue;
	
	@Column(name = "setting_remark", nullable = true)
	private String settingRemark;
	
	@Column(name = "create_time", nullable = true)
	private Timestamp createTime;
	
	@Column(name = "create_by", nullable = true)
	private String createBy;
	
	@Column(name = "update_time", nullable = true)
	private Timestamp updateTime;
	
	@Column(name = "update_by", nullable = true)
	private String updateBy;

	public SysConfigSetting() {
		super();
	}

	public SysConfigSetting(String settingId, String settingName, String settingValue, String settingRemark,
			Timestamp createTime, String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.settingId = settingId;
		this.settingName = settingName;
		this.settingValue = settingValue;
		this.settingRemark = settingRemark;
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

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public String getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}

	public String getSettingRemark() {
		return settingRemark;
	}

	public void setSettingRemark(String settingRemark) {
		this.settingRemark = settingRemark;
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
