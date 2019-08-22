package com.cmap.service.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.cmap.service.StepService;

public class StepServiceVO extends CommonServiceVO {

	private StepService.Result result;
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
	private List<String> cmdOutputList;

	private String deviceListId;
	private String restoreVersionId;           // 要還原的版本號
	private List<String> restoreContentList;   // 要還原的組態內容
	private String restoreVersionConfigPath;   // 要還原的組態版本在設備的哪個路徑 (for VM切換，ePDG config已先放在設備內)
	private String restoreVersionImagePath;    // 要還原的image版本在設備的哪個路徑 (for VM切換，ePDG需指定image)

	private List<List<VersionServiceVO>> versionList = new ArrayList<>();
	private List<String> preVerConfigList = new ArrayList<>();     // 前一版本Config內容
	private List<String> newVerConfigList = new ArrayList<>();     // 最新(當下備份)版本Config內容

	private ProvisionServiceVO psVO;

	ProvisionServiceVO psMasterVO = new ProvisionServiceVO();
	ProvisionServiceVO psDetailVO = new ProvisionServiceVO();
	ProvisionServiceVO psStepVO = new ProvisionServiceVO();
	ProvisionServiceVO psRetryVO;
	ProvisionServiceVO psDeviceVO;

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String toString =
				"Action by: [" + actionBy + "] from IP: [" + actionFromIp + "] , "
						+ "excute script code: [" + scriptCode + "] and result was: [" + result + "] , "
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
	@Override
	public String getActionBy() {
		return actionBy;
	}
	@Override
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

	public StepService.Result getResult() {
		return result;
	}

	public void setResult(StepService.Result result) {
		this.result = result;
	}

	public ProvisionServiceVO getPsVO() {
		return psVO;
	}

	public void setPsVO(ProvisionServiceVO psVO) {
		this.psVO = psVO;
	}

	public ProvisionServiceVO getPsMasterVO() {
		return psMasterVO;
	}

	public void setPsMasterVO(ProvisionServiceVO psMasterVO) {
		this.psMasterVO = psMasterVO;
	}

	public ProvisionServiceVO getPsDetailVO() {
		return psDetailVO;
	}

	public void setPsDetailVO(ProvisionServiceVO psDetailVO) {
		this.psDetailVO = psDetailVO;
	}

	public ProvisionServiceVO getPsStepVO() {
		return psStepVO;
	}

	public void setPsStepVO(ProvisionServiceVO psStepVO) {
		this.psStepVO = psStepVO;
	}

	public ProvisionServiceVO getPsRetryVO() {
		return psRetryVO;
	}

	public void setPsRetryVO(ProvisionServiceVO psRetryVO) {
		this.psRetryVO = psRetryVO;
	}

	public ProvisionServiceVO getPsDeviceVO() {
		return psDeviceVO;
	}

	public void setPsDeviceVO(ProvisionServiceVO psDeviceVO) {
		this.psDeviceVO = psDeviceVO;
	}

	public List<String> getCmdOutputList() {
		return cmdOutputList;
	}

	public void setCmdOutputList(List<String> cmdOutputList) {
		this.cmdOutputList = cmdOutputList;
	}

	public String getRestoreVersionId() {
		return restoreVersionId;
	}

	public void setRestoreVersionId(String restoreVersionId) {
		this.restoreVersionId = restoreVersionId;
	}

	public List<String> getRestoreContentList() {
		return restoreContentList;
	}

	public void setRestoreContentList(List<String> restoreContentList) {
		this.restoreContentList = restoreContentList;
	}

	public String getDeviceListId() {
		return deviceListId;
	}

	public void setDeviceListId(String deviceListId) {
		this.deviceListId = deviceListId;
	}

    public String getRestoreVersionConfigPath() {
        return restoreVersionConfigPath;
    }

    public void setRestoreVersionConfigPath(String restoreVersionConfigPath) {
        this.restoreVersionConfigPath = restoreVersionConfigPath;
    }

    public String getRestoreVersionImagePath() {
        return restoreVersionImagePath;
    }

    public void setRestoreVersionImagePath(String restoreVersionImagePath) {
        this.restoreVersionImagePath = restoreVersionImagePath;
    }

    public List<String> getPreVerConfigList() {
        return preVerConfigList;
    }

    public void setPreVerConfigList(List<String> preVerConfigList) {
        this.preVerConfigList = preVerConfigList;
    }

    public List<String> getNewVerConfigList() {
        return newVerConfigList;
    }

    public void setNewVerConfigList(List<String> newVerConfigList) {
        this.newVerConfigList = newVerConfigList;
    }

    public List<List<VersionServiceVO>> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<List<VersionServiceVO>> versionList) {
        this.versionList = versionList;
    }
}
