package com.cmap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name = "config_content_setting",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"setting_id"})
	}
)
public class ConfigContentSetting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "setting_id", unique = true)
	private Integer settingId;

	@Column(name = "setting_type", nullable = false)
	private String settingType;

	@Column(name = "device_model", nullable = false)
	private String deviceModel;

	@Column(name = "device_name_like", nullable = false)
	private String deviceNameLike;

	@Column(name = "device_list_id", nullable = false)
	private String deviceListId;

	@Column(name = "content_layer", nullable = false)
	private Integer contentLayer;

	@Column(name = "content_start_regex", nullable = false)
	private String contentStartRegex;

	@Column(name = "content_end_regex", nullable = false)
	private String contentEndRegex;

	@Column(name = "action", nullable = false)
	private String action;

	@Column(name = "remark", nullable = false)
	private String remark;

	@Column(name = "description", nullable = true)
	private String description;

	public ConfigContentSetting() {
		super();
	}

    public ConfigContentSetting(Integer settingId, String settingType, String deviceModel,
            String deviceNameLike, String deviceListId, Integer contentLayer,
            String contentStartRegex, String contentEndRegex, String action, String remark,
            String description) {
        super();
        this.settingId = settingId;
        this.settingType = settingType;
        this.deviceModel = deviceModel;
        this.deviceNameLike = deviceNameLike;
        this.deviceListId = deviceListId;
        this.contentLayer = contentLayer;
        this.contentStartRegex = contentStartRegex;
        this.contentEndRegex = contentEndRegex;
        this.action = action;
        this.remark = remark;
        this.description = description;
    }

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public String getSettingType() {
        return settingType;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceNameLike() {
        return deviceNameLike;
    }

    public void setDeviceNameLike(String deviceNameLike) {
        this.deviceNameLike = deviceNameLike;
    }

    public String getDeviceListId() {
        return deviceListId;
    }

    public void setDeviceListId(String deviceListId) {
        this.deviceListId = deviceListId;
    }

    public Integer getContentLayer() {
        return contentLayer;
    }

    public void setContentLayer(Integer contentLayer) {
        this.contentLayer = contentLayer;
    }

    public String getContentStartRegex() {
        return contentStartRegex;
    }

    public void setContentStartRegex(String contentStartRegex) {
        this.contentStartRegex = contentStartRegex;
    }

    public String getContentEndRegex() {
        return contentEndRegex;
    }

    public void setContentEndRegex(String contentEndRegex) {
        this.contentEndRegex = contentEndRegex;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
