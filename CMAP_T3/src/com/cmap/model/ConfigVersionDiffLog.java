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
	name = "config_version_diff_log",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"diff_log_id"})
	}
)
public class ConfigVersionDiffLog {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "diff_log_id", unique = true)
	private String diffLogId;

	@Column(name = "group_id", nullable = false)
	private String groupId;

	@Column(name = "device_id", nullable = false)
	private String deviceId;

	@Column(name = "pre_version_id", nullable = false)
	private String preVersionId;

	@Column(name = "pre_version", nullable = false)
	private String preVersion;

	@Column(name = "new_version_id", nullable = false)
    private String newVersionId;

    @Column(name = "new_version", nullable = false)
    private String newVersion;

	@Column(name = "create_time", nullable = true)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = true)
	private String createBy;

	@Column(name = "update_time", nullable = true)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = true)
	private String updateBy;

	public ConfigVersionDiffLog() {
		super();
	}

    public ConfigVersionDiffLog(String diffLogId, String groupId, String deviceId,
            String preVersionId, String preVersion, String newVersionId, String newVersion,
            Timestamp createTime, String createBy, Timestamp updateTime, String updateBy) {
        super();
        this.diffLogId = diffLogId;
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.preVersionId = preVersionId;
        this.preVersion = preVersion;
        this.newVersionId = newVersionId;
        this.newVersion = newVersion;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public String getDiffLogId() {
        return diffLogId;
    }

    public void setDiffLogId(String diffLogId) {
        this.diffLogId = diffLogId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPreVersionId() {
        return preVersionId;
    }

    public void setPreVersionId(String preVersionId) {
        this.preVersionId = preVersionId;
    }

    public String getPreVersion() {
        return preVersion;
    }

    public void setPreVersion(String preVersion) {
        this.preVersion = preVersion;
    }

    public String getNewVersionId() {
        return newVersionId;
    }

    public void setNewVersionId(String newVersionId) {
        this.newVersionId = newVersionId;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
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
