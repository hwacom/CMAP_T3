package com.cmap.plugin.module.ip.mapping;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="module_mac_table_exclude_port")
public class ModuleMacTableExcludePort implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "setting_id", unique = true)
    private String settingId;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "port_id", nullable = false)
    private String portId;

    @Column(name = "remark", nullable = true)
    private String remark;

    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    @Column(name = "create_by", nullable = false)
    private String createBy;

    public ModuleMacTableExcludePort() {
        super();
    }

    public ModuleMacTableExcludePort(String settingId, String groupId, String deviceId,
            String portId, String remark, Timestamp createTime, String createBy) {
        super();
        this.settingId = settingId;
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.portId = portId;
        this.remark = remark;
        this.createTime = createTime;
        this.createBy = createBy;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
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
}
