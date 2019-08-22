package com.cmap.plugin.module.firewall;

import java.util.Map;
import com.cmap.service.vo.CommonServiceVO;

public class FirewallVO extends CommonServiceVO {

    private String queryType;
    private String queryDevName;
    private String queryDateBegin;
    private String queryDateEnd;
    private String queryTimeBegin;
    private String queryTimeEnd;
    private String querySrcIp;
    private String querySrcPort;
    private String queryDstIp;
    private String queryDstPort;

    private String settingName;
    private String settingValue;
    private Integer orderNo;
    private String remark;
    private Map<String, String> typeNameMap;

    private String type;
    private String devName;
    private String dateStr;
    private String timeStr;
    private String srcIp;
    private String srcPort;
    private String dstIp;
    private String dstPort;
    private String proto;
    private String app;
    private String action;
    private String sentByte;
    private String rcvdByte;
    private String utmAction;
    private String level;
    private String user;
    private String message;
    private String severity;
    private String srcCountry;
    private String attack;
    private String service;
    private String url;

    public String getQueryType() {
        return queryType;
    }
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
    public String getQueryDevName() {
        return queryDevName;
    }
    public void setQueryDevName(String queryDevName) {
        this.queryDevName = queryDevName;
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
    public String getQueryTimeBegin() {
        return queryTimeBegin;
    }
    public void setQueryTimeBegin(String queryTimeBegin) {
        this.queryTimeBegin = queryTimeBegin;
    }
    public String getQueryTimeEnd() {
        return queryTimeEnd;
    }
    public void setQueryTimeEnd(String queryTimeEnd) {
        this.queryTimeEnd = queryTimeEnd;
    }
    public String getQuerySrcIp() {
        return querySrcIp;
    }
    public void setQuerySrcIp(String querySrcIp) {
        this.querySrcIp = querySrcIp;
    }
    public String getQuerySrcPort() {
        return querySrcPort;
    }
    public void setQuerySrcPort(String querySrcPort) {
        this.querySrcPort = querySrcPort;
    }
    public String getQueryDstIp() {
        return queryDstIp;
    }
    public void setQueryDstIp(String queryDstIp) {
        this.queryDstIp = queryDstIp;
    }
    public String getQueryDstPort() {
        return queryDstPort;
    }
    public void setQueryDstPort(String queryDstPort) {
        this.queryDstPort = queryDstPort;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDevName() {
        return devName;
    }
    public void setDevName(String devName) {
        this.devName = devName;
    }
    public String getDateStr() {
        return dateStr;
    }
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
    public String getTimeStr() {
        return timeStr;
    }
    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
    public String getSrcIp() {
        return srcIp;
    }
    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }
    public String getSrcPort() {
        return srcPort;
    }
    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }
    public String getDstIp() {
        return dstIp;
    }
    public void setDstIp(String dstIp) {
        this.dstIp = dstIp;
    }
    public String getDstPort() {
        return dstPort;
    }
    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }
    public String getProto() {
        return proto;
    }
    public void setProto(String proto) {
        this.proto = proto;
    }
    public String getApp() {
        return app;
    }
    public void setApp(String app) {
        this.app = app;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getSentByte() {
        return sentByte;
    }
    public void setSentByte(String sentByte) {
        this.sentByte = sentByte;
    }
    public String getRcvdByte() {
        return rcvdByte;
    }
    public void setRcvdByte(String rcvdByte) {
        this.rcvdByte = rcvdByte;
    }
    public String getUtmAction() {
        return utmAction;
    }
    public void setUtmAction(String utmAction) {
        this.utmAction = utmAction;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSeverity() {
        return severity;
    }
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    public String getSrcCountry() {
        return srcCountry;
    }
    public void setSrcCountry(String srcCountry) {
        this.srcCountry = srcCountry;
    }
    public String getAttack() {
        return attack;
    }
    public void setAttack(String attack) {
        this.attack = attack;
    }
    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getSettingName() {
        return settingName;
    }
    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }
    public String getSettingValue() {
        return settingValue;
    }
    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Integer getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
    public Map<String, String> getTypeNameMap() {
        return typeNameMap;
    }
    public void setTypeNameMap(Map<String, String> typeNameMap) {
        this.typeNameMap = typeNameMap;
    }
}
