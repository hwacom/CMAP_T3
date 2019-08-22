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
	name = "prtg_account_mapping",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"id"})
	}
)
public class PrtgAccountMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true)
	private String id;

	@Column(name = "source_id", nullable = false)
	private String sourceId;

	@Column(name = "prtg_account", nullable = false)
	private String prtgAccount;

	@Column(name = "prtg_password", nullable = false)
	private String prtgPassword;

	@Column(name = "index_url", nullable = true)
	private String indexUrl;

	@Column(name = "dashboard_map_url", nullable = true)
	private String dashboardMapUrl;

	@Column(name = "topography_map_url", nullable = true)
    private String topographyMapUrl;

	@Column(name = "alarm_summary_map_url", nullable = true)
    private String alarmSummaryMapUrl;

	@Column(name = "net_flow_map_url", nullable = true)
	private String netFlowMapUrl;

	@Column(name = "net_flow_output_map_url", nullable = true)
    private String netFlowOutputMapUrl;

	@Column(name = "device_failure_map_url", nullable = true)
	private String deviceFailureMapUrl;

	@Column(name = "abnormal_traffic_map_url", nullable = true)
	private String abnormalTrafficMapUrl;

	@Column(name = "email_update_map_url", nullable = true)
    private String emailUpdateMapUrl;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	public PrtgAccountMapping() {
		super();
	}

    public PrtgAccountMapping(String id, String sourceId, String prtgAccount, String prtgPassword,
            String indexUrl, String dashboardMapUrl, String topographyMapUrl,
            String alarmSummaryMapUrl, String netFlowMapUrl, String netFlowOutputMapUrl,
            String deviceFailureMapUrl, String abnormalTrafficMapUrl, String emailUpdateMapUrl,
            String remark, Timestamp createTime, String createBy) {
        super();
        this.id = id;
        this.sourceId = sourceId;
        this.prtgAccount = prtgAccount;
        this.prtgPassword = prtgPassword;
        this.indexUrl = indexUrl;
        this.dashboardMapUrl = dashboardMapUrl;
        this.topographyMapUrl = topographyMapUrl;
        this.alarmSummaryMapUrl = alarmSummaryMapUrl;
        this.netFlowMapUrl = netFlowMapUrl;
        this.netFlowOutputMapUrl = netFlowOutputMapUrl;
        this.deviceFailureMapUrl = deviceFailureMapUrl;
        this.abnormalTrafficMapUrl = abnormalTrafficMapUrl;
        this.emailUpdateMapUrl = emailUpdateMapUrl;
        this.remark = remark;
        this.createTime = createTime;
        this.createBy = createBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPrtgAccount() {
        return prtgAccount;
    }

    public void setPrtgAccount(String prtgAccount) {
        this.prtgAccount = prtgAccount;
    }

    public String getPrtgPassword() {
        return prtgPassword;
    }

    public void setPrtgPassword(String prtgPassword) {
        this.prtgPassword = prtgPassword;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getDashboardMapUrl() {
        return dashboardMapUrl;
    }

    public void setDashboardMapUrl(String dashboardMapUrl) {
        this.dashboardMapUrl = dashboardMapUrl;
    }

    public String getTopographyMapUrl() {
        return topographyMapUrl;
    }

    public void setTopographyMapUrl(String topographyMapUrl) {
        this.topographyMapUrl = topographyMapUrl;
    }

    public String getAlarmSummaryMapUrl() {
        return alarmSummaryMapUrl;
    }

    public void setAlarmSummaryMapUrl(String alarmSummaryMapUrl) {
        this.alarmSummaryMapUrl = alarmSummaryMapUrl;
    }

    public String getNetFlowMapUrl() {
        return netFlowMapUrl;
    }

    public void setNetFlowMapUrl(String netFlowMapUrl) {
        this.netFlowMapUrl = netFlowMapUrl;
    }

    public String getNetFlowOutputMapUrl() {
        return netFlowOutputMapUrl;
    }

    public void setNetFlowOutputMapUrl(String netFlowOutputMapUrl) {
        this.netFlowOutputMapUrl = netFlowOutputMapUrl;
    }

    public String getDeviceFailureMapUrl() {
        return deviceFailureMapUrl;
    }

    public void setDeviceFailureMapUrl(String deviceFailureMapUrl) {
        this.deviceFailureMapUrl = deviceFailureMapUrl;
    }

    public String getAbnormalTrafficMapUrl() {
        return abnormalTrafficMapUrl;
    }

    public void setAbnormalTrafficMapUrl(String abnormalTrafficMapUrl) {
        this.abnormalTrafficMapUrl = abnormalTrafficMapUrl;
    }

    public String getEmailUpdateMapUrl() {
        return emailUpdateMapUrl;
    }

    public void setEmailUpdateMapUrl(String emailUpdateMapUrl) {
        this.emailUpdateMapUrl = emailUpdateMapUrl;
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
}
