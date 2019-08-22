package com.cmap.plugin.module.vmswitch;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="module_vm_setting")
public class ModuleVmSetting implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false)
	private String id;

	@Column(name = "setting_name", nullable = false)
	private String settingName;

	@Column(name = "setting_value", nullable = false)
	private String settingValue;

	@Column(name = "remark", nullable = true)
    private String remark;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public ModuleVmSetting() {
		super();
		// TODO Auto-generated constructor stub
	}

    public ModuleVmSetting(String id, String settingName, String settingValue, String remark,
            Timestamp updateTime, String updateBy) {
        super();
        this.id = id;
        this.settingName = settingName;
        this.settingValue = settingValue;
        this.remark = remark;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
