package com.cmap.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * USER_OPERATION_LOG Entity
 * @author Ken Lin
 *
 */
@Entity
@Table(name="user_operation_log")
public class UserOperationLog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LOG_ID", nullable = false)
	private String logId;

	@Column(name = "USER_NAME", nullable = false)
	private String userName;

	@Column(name = "TABLE_NAME", nullable = false)
	private String tableName;

	@Column(name = "TARGET_ID", nullable = false)
	private String targetId;

	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@Column(name = "OPERATE_TIME", nullable = false)
	private Timestamp operateTime;

	public UserOperationLog() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserOperationLog(String logId, String userName, String tableName, String targetId, String description,
			Timestamp operateTime) {
		super();
		this.logId = logId;
		this.userName = userName;
		this.tableName = tableName;
		this.targetId = targetId;
		this.description = description;
		this.operateTime = operateTime;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
