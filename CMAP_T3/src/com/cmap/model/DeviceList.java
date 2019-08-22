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
		name = "device_list",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"device_list_id"})
		}
		)
public class DeviceList {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "device_list_id", unique = true)
	private String deviceListId;

	@Column(name = "group_id", nullable = false)
	private String groupId;

	@Column(name = "group_name", nullable = true)
	private String groupName;

	@Column(name = "group_eng_name", nullable = true)
	private String groupEngName;

	@Column(name = "device_id", nullable = false)
	private String deviceId;

	@Column(name = "device_name", nullable = true)
	private String deviceName;

	@Column(name = "device_eng_name", nullable = true)
	private String deviceEngName;

	@Column(name = "device_ip", nullable = true)
	private String deviceIp;

	@Column(name = "device_model", nullable = true)
    private String deviceModel;

	@Column(name = "device_layer", nullable = true)
    private String deviceLayer;

	@Column(name = "config_file_dir_path", nullable = false)
	private String configFileDirPath;

	@Column(name = "remote_file_dir_path", nullable = false)
	private String remoteFileDirPath;

	@Column(name = "delete_flag", nullable = false)
	private String deleteFlag;

	@Column(name = "delete_time", nullable = true)
	private Timestamp deleteTime;

	@Column(name = "delete_by", nullable = true)
	private String deleteBy;

	@Column(name = "create_time", nullable = true)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = true)
	private String createBy;

	@Column(name = "update_time", nullable = true)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = true)
	private String updateBy;

	public DeviceList() {
		super();
	}

    public DeviceList(String deviceListId, String groupId, String groupName, String groupEngName,
            String deviceId, String deviceName, String deviceEngName, String deviceIp,
            String deviceModel, String deviceLayer, String configFileDirPath,
            String remoteFileDirPath, String deleteFlag, Timestamp deleteTime, String deleteBy,
            Timestamp createTime, String createBy, Timestamp updateTime, String updateBy) {
        super();
        this.deviceListId = deviceListId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupEngName = groupEngName;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceEngName = deviceEngName;
        this.deviceIp = deviceIp;
        this.deviceModel = deviceModel;
        this.deviceLayer = deviceLayer;
        this.configFileDirPath = configFileDirPath;
        this.remoteFileDirPath = remoteFileDirPath;
        this.deleteFlag = deleteFlag;
        this.deleteTime = deleteTime;
        this.deleteBy = deleteBy;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public String getDeviceListId() {
        return deviceListId;
    }

    public void setDeviceListId(String deviceListId) {
        this.deviceListId = deviceListId;
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

    public String getGroupEngName() {
        return groupEngName;
    }

    public void setGroupEngName(String groupEngName) {
        this.groupEngName = groupEngName;
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

    public String getDeviceEngName() {
        return deviceEngName;
    }

    public void setDeviceEngName(String deviceEngName) {
        this.deviceEngName = deviceEngName;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceLayer() {
        return deviceLayer;
    }

    public void setDeviceLayer(String deviceLayer) {
        this.deviceLayer = deviceLayer;
    }

    public String getConfigFileDirPath() {
        return configFileDirPath;
    }

    public void setConfigFileDirPath(String configFileDirPath) {
        this.configFileDirPath = configFileDirPath;
    }

    public String getRemoteFileDirPath() {
        return remoteFileDirPath;
    }

    public void setRemoteFileDirPath(String remoteFileDirPath) {
        this.remoteFileDirPath = remoteFileDirPath;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
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
