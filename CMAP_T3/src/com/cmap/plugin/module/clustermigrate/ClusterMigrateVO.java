package com.cmap.plugin.module.clustermigrate;

import java.util.List;
import com.cmap.comm.enums.ConnectionMode;
import com.cmap.service.vo.CommonServiceVO;

public class ClusterMigrateVO extends CommonServiceVO {

    private String processResultFlag;
    private String processResultMsg;
    private String processRemark = "";

    private List<String> clusterList = null;
    private String serverIp;
    private String serverPort;
    private ConnectionMode connectionMode = null;
    private String loginAccount;
    private String loginPassword;

    private String logId;
    private String dateStr;
    private String migrateFromCluster;
    private String processFlag;
    private String processFlagDesc;
    private String migrateServiceName;
    private String migrateToCluster;
    private String migrateStartTimeStr;
    private String migrateEndTimeStr;
    private String migrateResult;
    private String remark;

    private String jobKeyGroup;
    private String jobKeyName;

    public String getProcessResultFlag() {
        return processResultFlag;
    }
    public void setProcessResultFlag(String processResultFlag) {
        this.processResultFlag = processResultFlag;
    }
    public String getProcessResultMsg() {
        return processResultMsg;
    }
    public void setProcessResultMsg(String processResultMsg) {
        this.processResultMsg = processResultMsg;
    }
    public String getProcessRemark() {
        return processRemark;
    }
    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }
    public List<String> getClusterList() {
        return clusterList;
    }
    public void setClusterList(List<String> clusterList) {
        this.clusterList = clusterList;
    }
    public String getServerIp() {
        return serverIp;
    }
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
    public String getServerPort() {
        return serverPort;
    }
    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
    public ConnectionMode getConnectionMode() {
        return connectionMode;
    }
    public void setConnectionMode(ConnectionMode connectionMode) {
        this.connectionMode = connectionMode;
    }
    public String getLoginAccount() {
        return loginAccount;
    }
    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }
    public String getLoginPassword() {
        return loginPassword;
    }
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    public String getLogId() {
        return logId;
    }
    public void setLogId(String logId) {
        this.logId = logId;
    }
    public String getDateStr() {
        return dateStr;
    }
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
    public String getMigrateFromCluster() {
        return migrateFromCluster;
    }
    public void setMigrateFromCluster(String migrateFromCluster) {
        this.migrateFromCluster = migrateFromCluster;
    }
    public String getProcessFlag() {
        return processFlag;
    }
    public void setProcessFlag(String processFlag) {
        this.processFlag = processFlag;
    }
    public String getProcessFlagDesc() {
        return processFlagDesc;
    }
    public void setProcessFlagDesc(String processFlagDesc) {
        this.processFlagDesc = processFlagDesc;
    }
    public String getMigrateServiceName() {
        return migrateServiceName;
    }
    public void setMigrateServiceName(String migrateServiceName) {
        this.migrateServiceName = migrateServiceName;
    }
    public String getMigrateToCluster() {
        return migrateToCluster;
    }
    public void setMigrateToCluster(String migrateToCluster) {
        this.migrateToCluster = migrateToCluster;
    }
    public String getMigrateStartTimeStr() {
        return migrateStartTimeStr;
    }
    public void setMigrateStartTimeStr(String migrateStartTimeStr) {
        this.migrateStartTimeStr = migrateStartTimeStr;
    }
    public String getMigrateEndTimeStr() {
        return migrateEndTimeStr;
    }
    public void setMigrateEndTimeStr(String migrateEndTimeStr) {
        this.migrateEndTimeStr = migrateEndTimeStr;
    }
    public String getMigrateResult() {
        return migrateResult;
    }
    public void setMigrateResult(String migrateResult) {
        this.migrateResult = migrateResult;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getJobKeyGroup() {
        return jobKeyGroup;
    }
    public void setJobKeyGroup(String jobKeyGroup) {
        this.jobKeyGroup = jobKeyGroup;
    }
    public String getJobKeyName() {
        return jobKeyName;
    }
    public void setJobKeyName(String jobKeyName) {
        this.jobKeyName = jobKeyName;
    }
}
