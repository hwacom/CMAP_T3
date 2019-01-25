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

	@Column(name = "net_flow_map_url", nullable = true)
	private String netFlowMapUrl;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	public PrtgAccountMapping() {
		super();
	}

	public PrtgAccountMapping(String id, String sourceId, String prtgAccount, String prtgPassword, String indexUrl,
			String dashboardMapUrl, String netFlowMapUrl, String remark, Timestamp createTime, String createBy) {
		super();
		this.id = id;
		this.sourceId = sourceId;
		this.prtgAccount = prtgAccount;
		this.prtgPassword = prtgPassword;
		this.indexUrl = indexUrl;
		this.dashboardMapUrl = dashboardMapUrl;
		this.netFlowMapUrl = netFlowMapUrl;
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

	public String getNetFlowMapUrl() {
		return netFlowMapUrl;
	}

	public void setNetFlowMapUrl(String netFlowMapUrl) {
		this.netFlowMapUrl = netFlowMapUrl;
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
