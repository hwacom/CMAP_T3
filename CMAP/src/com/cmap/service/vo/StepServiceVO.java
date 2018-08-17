package com.cmap.service.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cmap.service.StepService;

public class StepServiceVO {

	private StepService.Result resutl;
	private boolean success;
	private String message;
	private String cmdProcessLog;
	private String scriptCode;
	private String actionBy;
	private Date beginTime;
	private Date endTime;
	private String deviceName;
	private String deviceIp;
	private String actionFromIp;
	private Integer retryTimes;

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String toString =
				"Action by: [" + actionBy + "] from IP: [" + actionFromIp + "] , "
						+ "excute script code: [" + scriptCode + "] and result was: [" + resutl + "] , "
						+ "message: [" + message + "] , retry times: [" + retryTimes + "] , "
						+ "time from: [" + (beginTime != null ? sdf.format(beginTime) : "") + "] "
						+ "to: [" + (endTime != null ? sdf.format(endTime) : "") + "] , "
						+ "target device name:[" + deviceName + "] and IP: [" + deviceIp + "] , "
						+ "detail log: [" + cmdProcessLog + "]";

		return toString;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public StepService.Result getResutl() {
		return resutl;
	}
	public void setResutl(StepService.Result resutl) {
		this.resutl = resutl;
	}
	public String getCmdProcessLog() {
		return cmdProcessLog;
	}
	public void setCmdProcessLog(String cmdProcessLog) {
		this.cmdProcessLog = cmdProcessLog;
	}
	public String getScriptCode() {
		return scriptCode;
	}
	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}
	public String getActionBy() {
		return actionBy;
	}
	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	public String getActionFromIp() {
		return actionFromIp;
	}
	public void setActionFromIp(String actionFromIp) {
		this.actionFromIp = actionFromIp;
	}
	public Integer getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}
}
