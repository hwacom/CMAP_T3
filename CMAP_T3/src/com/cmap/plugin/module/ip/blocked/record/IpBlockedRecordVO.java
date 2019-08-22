package com.cmap.plugin.module.ip.blocked.record;

import com.cmap.service.vo.CommonServiceVO;

public class IpBlockedRecordVO extends CommonServiceVO {

    private String queryGroupId;
    private String queryDeviceId;
    private String queryIpAddress;
    private String queryStatusFlag;
    private String queryBeginDate;
    private String queryEndDate;

    private String listId;
    private String groupId;
    private String deviceId;
    private String ipAddress;
    private String statusFlag;
    private String blockTimeStr;
    private String blockBy;
    private String blockReason;
    private String openTimeStr;
    private String openBy;
    private String openReason;
    private String remark;
    private String updateTimeStr;
    private String updateBy;

    private String groupName;
    private String deviceName;

    public String getQueryGroupId() {
        return queryGroupId;
    }
    public void setQueryGroupId(String queryGroupId) {
        this.queryGroupId = queryGroupId;
    }
    public String getQueryDeviceId() {
        return queryDeviceId;
    }
    public void setQueryDeviceId(String queryDeviceId) {
        this.queryDeviceId = queryDeviceId;
    }
    public String getQueryIpAddress() {
        return queryIpAddress;
    }
    public void setQueryIpAddress(String queryIpAddress) {
        this.queryIpAddress = queryIpAddress;
    }
    public String getQueryStatusFlag() {
        return queryStatusFlag;
    }
    public void setQueryStatusFlag(String queryStatusFlag) {
        this.queryStatusFlag = queryStatusFlag;
    }
    public String getGroupId() {
        return groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getStatusFlag() {
        return statusFlag;
    }
    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }
    public String getBlockTimeStr() {
        return blockTimeStr;
    }
    public void setBlockTimeStr(String blockTimeStr) {
        this.blockTimeStr = blockTimeStr;
    }
    public String getBlockBy() {
        return blockBy;
    }
    public void setBlockBy(String blockBy) {
        this.blockBy = blockBy;
    }
    public String getBlockReason() {
        return blockReason;
    }
    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }
    public String getOpenTimeStr() {
        return openTimeStr;
    }
    public void setOpenTimeStr(String openTimeStr) {
        this.openTimeStr = openTimeStr;
    }
    public String getOpenBy() {
        return openBy;
    }
    public void setOpenBy(String openBy) {
        this.openBy = openBy;
    }
    public String getOpenReason() {
        return openReason;
    }
    public void setOpenReason(String openReason) {
        this.openReason = openReason;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getUpdateTimeStr() {
        return updateTimeStr;
    }
    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getQueryBeginDate() {
        return queryBeginDate;
    }
    public void setQueryBeginDate(String queryBeginDate) {
        this.queryBeginDate = queryBeginDate;
    }
    public String getQueryEndDate() {
        return queryEndDate;
    }
    public void setQueryEndDate(String queryEndDate) {
        this.queryEndDate = queryEndDate;
    }
    public String getListId() {
        return listId;
    }
    public void setListId(String listId) {
        this.listId = listId;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
