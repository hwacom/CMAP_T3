package com.cmap.model;

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
		name = "group_subnet_setting",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"setting_id"})
		}
)
public class GroupSubnetSetting {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "setting_id", unique = true)
	private String settingId;

	@Column(name = "group_id", nullable = false)
	private String groupId;

	@Column(name = "ipv4_subnet", nullable = true)
	private String ipv4Subnet;

	@Column(name = "ipv6_subnet", nullable = true)
	private String ipv6Subnet;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public GroupSubnetSetting() {
		super();
	}

    public GroupSubnetSetting(String settingId, String groupId, String ipv4Subnet,
            String ipv6Subnet, String remark, Timestamp createTime, String createBy,
            Timestamp updateTime, String updateBy) {
        super();
        this.settingId = settingId;
        this.groupId = groupId;
        this.ipv4Subnet = ipv4Subnet;
        this.ipv6Subnet = ipv6Subnet;
        this.remark = remark;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIpv4Subnet() {
        return ipv4Subnet;
    }

    public void setIpv4Subnet(String ipv4Subnet) {
        this.ipv4Subnet = ipv4Subnet;
    }

    public String getIpv6Subnet() {
        return ipv6Subnet;
    }

    public void setIpv6Subnet(String ipv6Subnet) {
        this.ipv6Subnet = ipv6Subnet;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
