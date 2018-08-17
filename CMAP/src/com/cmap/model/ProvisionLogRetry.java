package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "provision_log_retry",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"log_retry_id"})
		}
		)
public class ProvisionLogRetry {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "log_retry_id", unique = true)
	private String logRetryId;

	@Column(name = "retry_order", nullable = false)
	private Integer retryOrder;

	@Column(name = "result", nullable = false)
	private String result;

	@Column(name = "message", nullable = true)
	private String message;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "log_step_id", nullable = false)
	private ProvisionLogStep provisionLogStep;

	public ProvisionLogRetry() {
		super();
	}

	public ProvisionLogRetry(String logRetryId, Integer retryOrder, String result, String message, Timestamp createTime,
			String createBy, ProvisionLogStep provisionLogStep) {
		super();
		this.logRetryId = logRetryId;
		this.retryOrder = retryOrder;
		this.result = result;
		this.message = message;
		this.createTime = createTime;
		this.createBy = createBy;
		this.provisionLogStep = provisionLogStep;
	}

	public String getLogRetryId() {
		return logRetryId;
	}

	public void setLogRetryId(String logRetryId) {
		this.logRetryId = logRetryId;
	}

	public Integer getRetryOrder() {
		return retryOrder;
	}

	public void setRetryOrder(Integer retryOrder) {
		this.retryOrder = retryOrder;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public ProvisionLogStep getProvisionLogStep() {
		return provisionLogStep;
	}

	public void setProvisionLogStep(ProvisionLogStep provisionLogStep) {
		this.provisionLogStep = provisionLogStep;
	}
}
