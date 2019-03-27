package com.cmap.plugin.module.netflow;

import java.util.ArrayList;
import java.util.List;

import com.cmap.service.vo.CommonServiceVO;

public class NetFlowVO extends CommonServiceVO {

	private String queryGroupId;
	private String querySchoolId;
	private String queryIp;
	private String queryPort;
	private String querySourceIp;
	private String querySourcePort;
	private String queryDestinationIp;
	private String queryDestinationPort;
	private String querySenderIp;
	private String queryMac;
	private String queryDate;
	private String queryDateBegin;
	private String queryDateEnd;
	private String queryTimeBegin;
	private String queryTimeEnd;
	private String queryValue;
	private String queryCondition;
	private String queryDateStr;
	private String queryTimeBeginStr;
	private String queryTimeEndStr;

	private List<NetFlowVO> matchedList = new ArrayList<>();
	private int totalCount = 0;

	private String groupId;
	private String groupName;
	private String now;
	private String fromDateTime;
	private String toDateTime;
	private String ethernetType;
	private String protocol;
	private String sourceIP;
	private String sourcePort;
	private String sourceMAC;
	private String destinationIP;
	private String destinationPort;
	private String destinationMAC;
	private String size;
	private String channelID;
	private String toS;
	private String senderIP;
	private String inboundInterface;
	private String outboundInterface;
	private String sourceASI;
	private String destinationASI;
	private String sourceMask;
	private String destinationMask;
	private String nextHop;
	private String sourceVLAN;
	private String destinationVLAN;
	private String flowID;

	private String totalFlow;

	public String getQueryGroupId() {
		return queryGroupId;
	}

	public void setQueryGroupId(String queryGroupId) {
		this.queryGroupId = queryGroupId;
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

	public String getQueryIp() {
		return queryIp;
	}

	public void setQueryIp(String queryIp) {
		this.queryIp = queryIp;
	}

	public String getQueryPort() {
		return queryPort;
	}

	public void setQueryPort(String queryPort) {
		this.queryPort = queryPort;
	}

	public String getQueryMac() {
		return queryMac;
	}

	public void setQueryMac(String queryMac) {
		this.queryMac = queryMac;
	}

	public String getNow() {
		return now;
	}

	public void setNow(String now) {
		this.now = now;
	}

	public String getFromDateTime() {
		return fromDateTime;
	}

	public void setFromDateTime(String fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public String getToDateTime() {
		return toDateTime;
	}

	public void setToDateTime(String toDateTime) {
		this.toDateTime = toDateTime;
	}

	public String getEthernetType() {
		return ethernetType;
	}

	public void setEthernetType(String ethernetType) {
		this.ethernetType = ethernetType;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	public String getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
	}

	public String getSourceMAC() {
		return sourceMAC;
	}

	public void setSourceMAC(String sourceMAC) {
		this.sourceMAC = sourceMAC;
	}

	public String getDestinationIP() {
		return destinationIP;
	}

	public void setDestinationIP(String destinationIP) {
		this.destinationIP = destinationIP;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getDestinationMAC() {
		return destinationMAC;
	}

	public void setDestinationMAC(String destinationMAC) {
		this.destinationMAC = destinationMAC;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getToS() {
		return toS;
	}

	public void setToS(String toS) {
		this.toS = toS;
	}

	public String getSenderIP() {
		return senderIP;
	}

	public void setSenderIP(String senderIP) {
		this.senderIP = senderIP;
	}

	public String getInboundInterface() {
		return inboundInterface;
	}

	public void setInboundInterface(String inboundInterface) {
		this.inboundInterface = inboundInterface;
	}

	public String getOutboundInterface() {
		return outboundInterface;
	}

	public void setOutboundInterface(String outboundInterface) {
		this.outboundInterface = outboundInterface;
	}

	public String getSourceASI() {
		return sourceASI;
	}

	public void setSourceASI(String sourceASI) {
		this.sourceASI = sourceASI;
	}

	public String getDestinationASI() {
		return destinationASI;
	}

	public void setDestinationASI(String destinationASI) {
		this.destinationASI = destinationASI;
	}

	public String getSourceMask() {
		return sourceMask;
	}

	public void setSourceMask(String sourceMask) {
		this.sourceMask = sourceMask;
	}

	public String getDestinationMask() {
		return destinationMask;
	}

	public void setDestinationMask(String destinationMask) {
		this.destinationMask = destinationMask;
	}

	public String getNextHop() {
		return nextHop;
	}

	public void setNextHop(String nextHop) {
		this.nextHop = nextHop;
	}

	public String getSourceVLAN() {
		return sourceVLAN;
	}

	public void setSourceVLAN(String sourceVLAN) {
		this.sourceVLAN = sourceVLAN;
	}

	public String getDestinationVLAN() {
		return destinationVLAN;
	}

	public void setDestinationVLAN(String destinationVLAN) {
		this.destinationVLAN = destinationVLAN;
	}

	public String getFlowID() {
		return flowID;
	}

	public void setFlowID(String flowID) {
		this.flowID = flowID;
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

	public String getQuerySourceIp() {
		return querySourceIp;
	}

	public void setQuerySourceIp(String querySourceIp) {
		this.querySourceIp = querySourceIp;
	}

	public String getQuerySourcePort() {
		return querySourcePort;
	}

	public void setQuerySourcePort(String querySourcePort) {
		this.querySourcePort = querySourcePort;
	}

	public String getQueryDestinationIp() {
		return queryDestinationIp;
	}

	public void setQueryDestinationIp(String queryDestinationIp) {
		this.queryDestinationIp = queryDestinationIp;
	}

	public String getQueryDestinationPort() {
		return queryDestinationPort;
	}

	public void setQueryDestinationPort(String queryDestinationPort) {
		this.queryDestinationPort = queryDestinationPort;
	}

	public String getQuerySenderIp() {
		return querySenderIp;
	}

	public void setQuerySenderIp(String querySenderIp) {
		this.querySenderIp = querySenderIp;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	public String getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

	public String getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(String queryCondition) {
		this.queryCondition = queryCondition;
	}

	public List<NetFlowVO> getMatchedList() {
		return matchedList;
	}

	public void setMatchedList(List<NetFlowVO> matchedList) {
		this.matchedList = matchedList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getQuerySchoolId() {
		return querySchoolId;
	}

	public void setQuerySchoolId(String querySchoolId) {
		this.querySchoolId = querySchoolId;
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

	public String getQueryDateStr() {
		return queryDateStr;
	}

	public void setQueryDateStr(String queryDateStr) {
		this.queryDateStr = queryDateStr;
	}

	public String getQueryTimeBeginStr() {
		return queryTimeBeginStr;
	}

	public void setQueryTimeBeginStr(String queryTimeBeginStr) {
		this.queryTimeBeginStr = queryTimeBeginStr;
	}

	public String getQueryTimeEndStr() {
		return queryTimeEndStr;
	}

	public void setQueryTimeEndStr(String queryTimeEndStr) {
		this.queryTimeEndStr = queryTimeEndStr;
	}

	public String getTotalFlow() {
		return totalFlow;
	}

	public void setTotalFlow(String totalFlow) {
		this.totalFlow = totalFlow;
	}
}
