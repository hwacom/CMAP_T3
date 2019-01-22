package com.cmap.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProvisionServiceVO extends CommonServiceVO {

	private String logMasterId;
	private String logDetailId;
	private String logStepId;
	private String logRetryId;
	private String logDeviceId;

	// Common
	private String result;
	private String message;
	private Date beginTime;
	private Date endTime;
	private Integer spendTimeInSeconds;
	private String remark;

	// Provision_Log_Master
	private String userName;
	private String userIp;
	private String reason;

	// Provision_Log_Step
	private String scriptCode;
	private Integer retryTimes;
	private String processLog;

	// Provision_Log_Device
	private Integer orderNum;
	private String deviceListId;
	private String deviceInfoStr;

	private Integer retryOrder;

	private List<ProvisionServiceVO> detailVO = new ArrayList<>();
	private List<ProvisionServiceVO> stepVO = new ArrayList<>();
	private List<ProvisionServiceVO> retryVO = new ArrayList<>();
	private List<ProvisionServiceVO> deviceVO = new ArrayList<>();

	public String getLogMasterId() {
		return logMasterId;
	}
	public void setLogMasterId(String logMasterId) {
		this.logMasterId = logMasterId;
	}
	public String getLogDetailId() {
		return logDetailId;
	}
	public void setLogDetailId(String logDetailId) {
		this.logDetailId = logDetailId;
	}
	public String getLogStepId() {
		return logStepId;
	}
	public void setLogStepId(String logStepId) {
		this.logStepId = logStepId;
	}
	public String getLogRetryId() {
		return logRetryId;
	}
	public void setLogRetryId(String logRetryId) {
		this.logRetryId = logRetryId;
	}
	public String getLogDeviceId() {
		return logDeviceId;
	}
	public void setLogDeviceId(String logDeviceId) {
		this.logDeviceId = logDeviceId;
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
	public Integer getSpendTimeInSeconds() {
		return spendTimeInSeconds;
	}
	public void setSpendTimeInSeconds(Integer spendTimeInSeconds) {
		this.spendTimeInSeconds = spendTimeInSeconds;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getScriptCode() {
		return scriptCode;
	}
	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}
	public Integer getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}
	public String getProcessLog() {
		return processLog;
	}
	public void setProcessLog(String processLog) {
		this.processLog = processLog;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getDeviceListId() {
		return deviceListId;
	}
	public void setDeviceListId(String deviceListId) {
		this.deviceListId = deviceListId;
	}
	public Integer getRetryOrder() {
		return retryOrder;
	}
	public void setRetryOrder(Integer retryOrder) {
		this.retryOrder = retryOrder;
	}
	public List<ProvisionServiceVO> getDetailVO() {
		return detailVO;
	}
	public void setDetailVO(List<ProvisionServiceVO> detailVO) {
		this.detailVO = detailVO;
	}
	public List<ProvisionServiceVO> getStepVO() {
		return stepVO;
	}
	public void setStepVO(List<ProvisionServiceVO> stepVO) {
		this.stepVO = stepVO;
	}
	public List<ProvisionServiceVO> getRetryVO() {
		return retryVO;
	}
	public void setRetryVO(List<ProvisionServiceVO> retryVO) {
		this.retryVO = retryVO;
	}
	public List<ProvisionServiceVO> getDeviceVO() {
		return deviceVO;
	}
	public void setDeviceVO(List<ProvisionServiceVO> deviceVO) {
		this.deviceVO = deviceVO;
	}
	public String getDeviceInfoStr() {
		return deviceInfoStr;
	}
	public void setDeviceInfoStr(String deviceInfoStr) {
		this.deviceInfoStr = deviceInfoStr;
	}
}
