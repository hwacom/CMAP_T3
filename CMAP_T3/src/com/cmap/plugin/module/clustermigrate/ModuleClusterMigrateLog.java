package com.cmap.plugin.module.clustermigrate;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="module_cluster_migrate_log")
public class ModuleClusterMigrateLog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id", nullable = false)
	private Integer logId;

	@Column(name = "date_str", nullable = false)
	private String dateStr;

	@Column(name = "migrate_from_cluster", nullable = false)
	private String migrateFromCluster;

	@Column(name = "process_flag", nullable = false)
	private String processFlag = "O";

	@Column(name = "migrate_service_name", nullable = true)
    private String migrateServiceName;

	@Column(name = "migrate_to_cluster", nullable = true)
	private String migrateToCluster;

	@Column(name = "migrate_start_time", nullable = true)
    private Timestamp migrateStartTime;

	@Column(name = "migrate_end_time", nullable = true)
    private Timestamp migrateEndTime;

	@Column(name = "migrate_result", nullable = false)
    private String migrateResult = "O";

	@Column(name = "remark", nullable = true)
    private String remark;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime = new Timestamp(new Date().getTime());

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime = new Timestamp(new Date().getTime());

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public ModuleClusterMigrateLog() {
		super();
	}

    public ModuleClusterMigrateLog(Integer logId, String dateStr, String migrateFromCluster,
            String processFlag, String migrateServiceName, String migrateToCluster,
            Timestamp migrateStartTime, Timestamp migrateEndTime, String migrateResult,
            String remark, Timestamp createTime, String createBy, Timestamp updateTime,
            String updateBy) {
        super();
        this.logId = logId;
        this.dateStr = dateStr;
        this.migrateFromCluster = migrateFromCluster;
        this.processFlag = processFlag;
        this.migrateServiceName = migrateServiceName;
        this.migrateToCluster = migrateToCluster;
        this.migrateStartTime = migrateStartTime;
        this.migrateEndTime = migrateEndTime;
        this.migrateResult = migrateResult;
        this.remark = remark;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getMigrateFromCluster() {
        return migrateFromCluster;
    }

    public void setMigrateFromCluster(String migrateFromCluster) {
        this.migrateFromCluster = migrateFromCluster;
    }

    public String getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(String processFlag) {
        this.processFlag = processFlag;
    }

    public String getMigrateServiceName() {
        return migrateServiceName;
    }

    public void setMigrateServiceName(String migrateServiceName) {
        this.migrateServiceName = migrateServiceName;
    }

    public String getMigrateToCluster() {
        return migrateToCluster;
    }

    public void setMigrateToCluster(String migrateToCluster) {
        this.migrateToCluster = migrateToCluster;
    }

    public Timestamp getMigrateStartTime() {
        return migrateStartTime;
    }

    public void setMigrateStartTime(Timestamp migrateStartTime) {
        this.migrateStartTime = migrateStartTime;
    }

    public Timestamp getMigrateEndTime() {
        return migrateEndTime;
    }

    public void setMigrateEndTime(Timestamp migrateEndTime) {
        this.migrateEndTime = migrateEndTime;
    }

    public String getMigrateResult() {
        return migrateResult;
    }

    public void setMigrateResult(String migrateResult) {
        this.migrateResult = migrateResult;
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
