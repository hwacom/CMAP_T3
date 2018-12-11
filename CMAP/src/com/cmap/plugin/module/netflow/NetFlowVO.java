package com.cmap.plugin.module.netflow;

import com.cmap.service.vo.CommonServiceVO;

public class NetFlowVO extends CommonServiceVO {

	private String queryGroupId;
	private String queryIp;
	private String queryPort;
	private String queryMac;
	private String queryDateBegin;
	private String queryDateEnd;

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
}
