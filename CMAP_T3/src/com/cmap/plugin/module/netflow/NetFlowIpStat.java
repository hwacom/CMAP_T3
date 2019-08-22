package com.cmap.plugin.module.netflow;

import java.math.BigInteger;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "net_flow_ip_stat",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"stat_id"})
		}
		)
public class NetFlowIpStat {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "stat_id", unique = true)
	private String statId;

	@Column(name = "stat_date", nullable = false)
    private String statDate;

	@Column(name = "group_id", nullable = false)
	private String groupId;

	@Column(name = "ip_addr", nullable = false)
	private String ipAddr;

	@Column(name = "direction", nullable = false)
	private String direction;

	@Column(name = "ttl_size", nullable = false)
	private BigInteger ttlSize;

	@Column(name = "limit_size", nullable = false)
    private BigInteger limitSize;

	@Column(name = "block_flag", nullable = false)
	private String blockFlag = "N";

	@Column(name = "send_prtg_flag", nullable = false)
    private String sendPrtgFlag = "N";

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public NetFlowIpStat() {
		super();
	}

    public NetFlowIpStat(String statId, String statDate, String groupId, String ipAddr,
            String direction, BigInteger ttlSize, BigInteger limitSize, String blockFlag,
            String sendPrtgFlag, Timestamp createTime, String createBy, Timestamp updateTime,
            String updateBy) {
        super();
        this.statId = statId;
        this.statDate = statDate;
        this.groupId = groupId;
        this.ipAddr = ipAddr;
        this.direction = direction;
        this.ttlSize = ttlSize;
        this.limitSize = limitSize;
        this.blockFlag = blockFlag;
        this.sendPrtgFlag = sendPrtgFlag;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public String getStatId() {
        return statId;
    }

    public void setStatId(String statId) {
        this.statId = statId;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BigInteger getTtlSize() {
        return ttlSize;
    }

    public void setTtlSize(BigInteger ttlSize) {
        this.ttlSize = ttlSize;
    }

    public BigInteger getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(BigInteger limitSize) {
        this.limitSize = limitSize;
    }

    public String getBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(String blockFlag) {
        this.blockFlag = blockFlag;
    }

    public String getSendPrtgFlag() {
        return sendPrtgFlag;
    }

    public void setSendPrtgFlag(String sendPrtgFlag) {
        this.sendPrtgFlag = sendPrtgFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
