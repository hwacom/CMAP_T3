package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * 系統環境變數Entity
 * @author Ken Lin
 *
 */
@Entity
@Table(
		name = "sys_error_log",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"log_id"})
		}
	)
public class SysErrorLog {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "log_id", unique = true)
	private String logId;

	@Column(name = "entry_date", nullable = false)
	private Timestamp entryDate;

	@Column(name = "logger", nullable = true)
	private String logger;

	@Column(name = "log_level", nullable = true)
	private String logLevel;

	@Column(name = "message", nullable = true)
	private String message;

	@Column(name = "exception", nullable = true)
	private String exception;

	public SysErrorLog() {
		super();
	}

	public SysErrorLog(String logId, Timestamp entryDate, String logger, String logLevel, String message,
			String exception) {
		super();
		this.logId = logId;
		this.entryDate = entryDate;
		this.logger = logger;
		this.logLevel = logLevel;
		this.message = message;
		this.exception = exception;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public Timestamp getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = entryDate;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
}
