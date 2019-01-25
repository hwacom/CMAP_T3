package com.cmap.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cmap.Constants;

public class VersionServiceVO extends CommonServiceVO {

	private String queryVersionId;

	private String queryGroup;
	private String queryGroup1;
	private String queryGroup2;

	private String queryDevice;
	private String queryDevice1;
	private String queryDevice2;

	private String queryDateBegin1;
	private String queryDateEnd1;
	private String queryDateBegin2;
	private String queryDateEnd2;

	private String queryConfigType;
	private boolean queryNewChkbox = false;
	private boolean jobTrigger = false;

	private List<String> queryGroupList;
	private List<String> queryGroup1List;
	private List<String> queryGroup2List;
	private List<String> queryDeviceList;
	private List<String> queryDevice1List;
	private List<String> queryDevice2List;

	private List<String> allGroupList;
	private List<String> allDeviceList;

	private String versionId;
	private String groupId;
	private String groupName;
	private String deviceId;
	private String deviceName;
	private String systemVersion;
	private String configType;
	private String configVersion;
	private String fileFullName;
	private String backupTimeStr;
	private String deviceIp;
	private String configFileDirPath;
	private String remoteFileDirPath;
	private String deviceListId;

	private Date createDate;

	private String configFileContent;
	private List<String> configContentList;

	private int lineNumber;
	private String line = Constants.ADD_LINE;
	private String line1;
	private String line2;
	private boolean lineDiff = false;
	private boolean line1Diff = false;
	private boolean line2Diff = false;
	private List<VersionServiceVO> diffRetOriList = new ArrayList<>();
	private List<VersionServiceVO> diffRetRevList = new ArrayList<>();
	private String diffPos = "";
	private String configDiffOriContent;
	private String configDiffRevContent;
	private String versionOri;
	private String versionRev;
	private String versionLineNum;

	private String retMsg;
	private String errMsg;

	//--------- Restore ----------//
	private String restoreVersionId;
	private List<String> restoreContentList = new ArrayList<>();

	public String getQueryGroup() {
		return queryGroup;
	}
	public void setQueryGroup(String queryGroup) {
		this.queryGroup = queryGroup;
	}
	public String getQueryGroup1() {
		return queryGroup1;
	}
	public void setQueryGroup1(String queryGroup1) {
		this.queryGroup1 = queryGroup1;
	}
	public String getQueryGroup2() {
		return queryGroup2;
	}
	public void setQueryGroup2(String queryGroup2) {
		this.queryGroup2 = queryGroup2;
	}
	public String getQueryDevice() {
		return queryDevice;
	}
	public void setQueryDevice(String queryDevice) {
		this.queryDevice = queryDevice;
	}
	public String getQueryDevice1() {
		return queryDevice1;
	}
	public void setQueryDevice1(String queryDevice1) {
		this.queryDevice1 = queryDevice1;
	}
	public String getQueryDevice2() {
		return queryDevice2;
	}
	public void setQueryDevice2(String queryDevice2) {
		this.queryDevice2 = queryDevice2;
	}
	public String getQueryDateBegin1() {
		return queryDateBegin1;
	}
	public void setQueryDateBegin1(String queryDateBegin1) {
		this.queryDateBegin1 = queryDateBegin1;
	}
	public String getQueryDateEnd1() {
		return queryDateEnd1;
	}
	public void setQueryDateEnd1(String queryDateEnd1) {
		this.queryDateEnd1 = queryDateEnd1;
	}
	public String getQueryDateBegin2() {
		return queryDateBegin2;
	}
	public void setQueryDateBegin2(String queryDateBegin2) {
		this.queryDateBegin2 = queryDateBegin2;
	}
	public String getQueryDateEnd2() {
		return queryDateEnd2;
	}
	public void setQueryDateEnd2(String queryDateEnd2) {
		this.queryDateEnd2 = queryDateEnd2;
	}
	public String getQueryConfigType() {
		return queryConfigType;
	}
	public void setQueryConfigType(String queryConfigType) {
		this.queryConfigType = queryConfigType;
	}
	public boolean isQueryNewChkbox() {
		return queryNewChkbox;
	}
	public void setQueryNewChkbox(boolean queryNewChkbox) {
		this.queryNewChkbox = queryNewChkbox;
	}
	public boolean isJobTrigger() {
		return jobTrigger;
	}
	public void setJobTrigger(boolean jobTrigger) {
		this.jobTrigger = jobTrigger;
	}
	public List<String> getQueryGroupList() {
		return queryGroupList;
	}
	public void setQueryGroupList(List<String> queryGroupList) {
		this.queryGroupList = queryGroupList;
	}
	public List<String> getQueryGroup1List() {
		return queryGroup1List;
	}
	public void setQueryGroup1List(List<String> queryGroup1List) {
		this.queryGroup1List = queryGroup1List;
	}
	public List<String> getQueryGroup2List() {
		return queryGroup2List;
	}
	public void setQueryGroup2List(List<String> queryGroup2List) {
		this.queryGroup2List = queryGroup2List;
	}
	public List<String> getQueryDeviceList() {
		return queryDeviceList;
	}
	public void setQueryDeviceList(List<String> queryDeviceList) {
		this.queryDeviceList = queryDeviceList;
	}
	public List<String> getQueryDevice1List() {
		return queryDevice1List;
	}
	public void setQueryDevice1List(List<String> queryDevice1List) {
		this.queryDevice1List = queryDevice1List;
	}
	public List<String> getQueryDevice2List() {
		return queryDevice2List;
	}
	public void setQueryDevice2List(List<String> queryDevice2List) {
		this.queryDevice2List = queryDevice2List;
	}
	public List<String> getAllGroupList() {
		return allGroupList;
	}
	public void setAllGroupList(List<String> allGroupList) {
		this.allGroupList = allGroupList;
	}
	public List<String> getAllDeviceList() {
		return allDeviceList;
	}
	public void setAllDeviceList(List<String> allDeviceList) {
		this.allDeviceList = allDeviceList;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
	public String getConfigType() {
		return configType;
	}
	public void setConfigType(String configType) {
		this.configType = configType;
	}
	public String getConfigVersion() {
		return configVersion;
	}
	public void setConfigVersion(String configVersion) {
		this.configVersion = configVersion;
	}
	public String getFileFullName() {
		return fileFullName;
	}
	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}
	public String getBackupTimeStr() {
		return backupTimeStr;
	}
	public void setBackupTimeStr(String backupTimeStr) {
		this.backupTimeStr = backupTimeStr;
	}
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	public String getConfigFileDirPath() {
		return configFileDirPath;
	}
	public void setConfigFileDirPath(String configFileDirPath) {
		this.configFileDirPath = configFileDirPath;
	}
	public String getRemoteFileDirPath() {
		return remoteFileDirPath;
	}
	public void setRemoteFileDirPath(String remoteFileDirPath) {
		this.remoteFileDirPath = remoteFileDirPath;
	}
	public String getDeviceListId() {
		return deviceListId;
	}
	public void setDeviceListId(String deviceListId) {
		this.deviceListId = deviceListId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getConfigFileContent() {
		return configFileContent;
	}
	public void setConfigFileContent(String configFileContent) {
		this.configFileContent = configFileContent;
	}
	public List<String> getConfigContentList() {
		return configContentList;
	}
	public void setConfigContentList(List<String> configContentList) {
		this.configContentList = configContentList;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public boolean isLineDiff() {
		return lineDiff;
	}
	public void setLineDiff(boolean lineDiff) {
		this.lineDiff = lineDiff;
	}
	public boolean isLine1Diff() {
		return line1Diff;
	}
	public void setLine1Diff(boolean line1Diff) {
		this.line1Diff = line1Diff;
	}
	public boolean isLine2Diff() {
		return line2Diff;
	}
	public void setLine2Diff(boolean line2Diff) {
		this.line2Diff = line2Diff;
	}
	public List<VersionServiceVO> getDiffRetOriList() {
		return diffRetOriList;
	}
	public void setDiffRetOriList(List<VersionServiceVO> diffRetOriList) {
		this.diffRetOriList = diffRetOriList;
	}
	public List<VersionServiceVO> getDiffRetRevList() {
		return diffRetRevList;
	}
	public void setDiffRetRevList(List<VersionServiceVO> diffRetRevList) {
		this.diffRetRevList = diffRetRevList;
	}
	public String getDiffPos() {
		return diffPos;
	}
	public void setDiffPos(String diffPos) {
		this.diffPos = diffPos;
	}
	public String getConfigDiffOriContent() {
		return configDiffOriContent;
	}
	public void setConfigDiffOriContent(String configDiffOriContent) {
		this.configDiffOriContent = configDiffOriContent;
	}
	public String getConfigDiffRevContent() {
		return configDiffRevContent;
	}
	public void setConfigDiffRevContent(String configDiffRevContent) {
		this.configDiffRevContent = configDiffRevContent;
	}
	public String getVersionOri() {
		return versionOri;
	}
	public void setVersionOri(String versionOri) {
		this.versionOri = versionOri;
	}
	public String getVersionRev() {
		return versionRev;
	}
	public void setVersionRev(String versionRev) {
		this.versionRev = versionRev;
	}
	public String getVersionLineNum() {
		return versionLineNum;
	}
	public void setVersionLineNum(String versionLineNum) {
		this.versionLineNum = versionLineNum;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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
	public String getQueryVersionId() {
		return queryVersionId;
	}
	public void setQueryVersionId(String queryVersionId) {
		this.queryVersionId = queryVersionId;
	}
}
