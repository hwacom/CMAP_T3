package com.cmap.plugin.module.netflow.statistics;

import com.cmap.service.vo.CommonServiceVO;

public class NetFlowStatisticsVO extends CommonServiceVO {

    private String queryGroupId;
    private String queryDatePeriod;
    private String queryDateBegin;
    private String queryDateEnd;

    private String ipAddress;
    private String groupId;
    private String groupName;
    private String percent;
    private String totalTraffic;
    private String uploadTraffic;
    private String downloadTraffic;

    public String getQueryGroupId() {
        return queryGroupId;
    }
    public void setQueryGroupId(String queryGroupId) {
        this.queryGroupId = queryGroupId;
    }
    public String getQueryDatePeriod() {
        return queryDatePeriod;
    }
    public void setQueryDatePeriod(String queryDatePeriod) {
        this.queryDatePeriod = queryDatePeriod;
    }
    public String getQueryDateBegin() {
        return queryDateBegin;
    }
    public void setQueryDateBegin(String queryDateBegin) {
        this.queryDateBegin = queryDateBegin;
    }
    public String getQueryDateEnd() {
        return queryDateEnd;
    }
    public void setQueryDateEnd(String queryDateEnd) {
        this.queryDateEnd = queryDateEnd;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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
    public String getPercent() {
        return percent;
    }
    public void setPercent(String percent) {
        this.percent = percent;
    }
    public String getTotalTraffic() {
        return totalTraffic;
    }
    public void setTotalTraffic(String totalTraffic) {
        this.totalTraffic = totalTraffic;
    }
    public String getUploadTraffic() {
        return uploadTraffic;
    }
    public void setUploadTraffic(String uploadTraffic) {
        this.uploadTraffic = uploadTraffic;
    }
    public String getDownloadTraffic() {
        return downloadTraffic;
    }
    public void setDownloadTraffic(String downloadTraffic) {
        this.downloadTraffic = downloadTraffic;
    }
}
