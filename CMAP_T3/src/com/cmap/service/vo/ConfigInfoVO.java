package com.cmap.service.vo;

import java.util.ArrayList;
import java.util.List;

public class ConfigInfoVO implements Cloneable {

	private String deviceListId;
	private String groupId;
	private String groupName;
	private String groupEngName;
	private String deviceId;
	private String deviceName;
	private String deviceEngName;
	private String configFileDirPath;	//設定本地備份落地檔路徑
	private String remoteFileDirPath;	//設定異地備份路徑
	private String configContent;
	private List<String> configContentList = new ArrayList<>();
	private String configFileName;
	private String fileFullName;
	private String systemVersion;
	private String configType;
	private String deviceFlashConfigPath;

	private String deviceIp;
	private String account;
	private String password;
	private String enablePassword;

	private String tFtpIP;
	private Integer tFtpPort;
	private String tFtpAccount;
	private String tFtpPassword;
	private String tFtpFilePath;

	private String ftpIP;
	private String ftpUrl;
	private Integer ftpPort;
	private String ftpAccount;
	private String ftpPassword;
	private String ftpFilePath;

	private String times;
	private String tempFilePath;
	private String tempFileRandomCode;

	private String uploadFileName;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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

	public String getConfigFileDirPath() {
		return configFileDirPath;
	}

	public void setConfigFileDirPath(String configFileDirPath) {
		this.configFileDirPath = configFileDirPath;
	}

	public String getConfigContent() {
		return configContent;
	}

	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}

	public String getConfigFileName() {
		return configFileName;
	}

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
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

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String gettFtpIP() {
		return tFtpIP;
	}

	public void settFtpIP(String tFtpIP) {
		this.tFtpIP = tFtpIP;
	}

	public String gettFtpFilePath() {
		return tFtpFilePath;
	}

	public void settFtpFilePath(String tFtpFilePath) {
		this.tFtpFilePath = tFtpFilePath;
	}

	public String getFtpIP() {
		return ftpIP;
	}

	public void setFtpIP(String ftpIP) {
		this.ftpIP = ftpIP;
	}

	public String getFtpAccount() {
		return ftpAccount;
	}

	public void setFtpAccount(String ftpAccount) {
		this.ftpAccount = ftpAccount;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getEnablePassword() {
		return enablePassword;
	}

	public void setEnablePassword(String enablePassword) {
		this.enablePassword = enablePassword;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public Integer gettFtpPort() {
		return tFtpPort;
	}

	public void settFtpPort(Integer tFtpPort) {
		this.tFtpPort = tFtpPort;
	}

	public String gettFtpAccount() {
		return tFtpAccount;
	}

	public void settFtpAccount(String tFtpAccount) {
		this.tFtpAccount = tFtpAccount;
	}

	public String gettFtpPassword() {
		return tFtpPassword;
	}

	public void settFtpPassword(String tFtpPassword) {
		this.tFtpPassword = tFtpPassword;
	}

	public String getFileFullName() {
		return fileFullName;
	}

	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}

	public List<String> getConfigContentList() {
		return configContentList;
	}

	public void setConfigContentList(List<String> configContentList) {
		this.configContentList = configContentList;
	}

	public String getTempFileRandomCode() {
		return tempFileRandomCode;
	}

	public void setTempFileRandomCode(String tempFileRandomCode) {
		this.tempFileRandomCode = tempFileRandomCode;
	}

	public String getTempFilePath() {
		return tempFilePath;
	}

	public void setTempFilePath(String tempFilePath) {
		this.tempFilePath = tempFilePath;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getGroupEngName() {
		return groupEngName;
	}

	public void setGroupEngName(String groupEngName) {
		this.groupEngName = groupEngName;
	}

	public String getDeviceEngName() {
		return deviceEngName;
	}

	public void setDeviceEngName(String deviceEngName) {
		this.deviceEngName = deviceEngName;
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

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getFtpUrl() {
		return ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.ftpUrl = ftpUrl;
	}

	public String getFtpFilePath() {
		return ftpFilePath;
	}

	public void setFtpFilePath(String ftpFilePath) {
		this.ftpFilePath = ftpFilePath;
	}

	public String getDeviceFlashConfigPath() {
		return deviceFlashConfigPath;
	}

	public void setDeviceFlashConfigPath(String deviceFlashConfigPath) {
		this.deviceFlashConfigPath = deviceFlashConfigPath;
	}
}
