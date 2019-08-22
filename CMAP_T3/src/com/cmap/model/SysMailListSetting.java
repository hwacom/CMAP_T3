package com.cmap.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * SYS_MAIL_LIST_SETTING Entity
 * @author Ken Lin
 *
 */
@Entity
@Table(
		name = "sys_mail_list_setting",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"setting_id"})
		}
	)
public class SysMailListSetting {

	@Id
	@Column(name = "setting_id", unique = true)
	private String settingId;

	@Column(name = "setting_code", nullable = false)
    private String settingCode;

	@Column(name = "mail_subject", nullable = false)
    private String mailSubject;

	@Column(name = "mail_to", nullable = false)
    private String mailTo;

	@Column(name = "mail_cc", nullable = true)
    private String mailCc;

	@Column(name = "mail_bcc", nullable = true)
    private String mailBcc;

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

	public SysMailListSetting() {
		super();
	}

    public SysMailListSetting(String settingId, String settingCode, String mailSubject,
            String mailTo, String mailCc, String mailBcc, String remark, Timestamp createTime,
            String createBy, Timestamp updateTime, String updateBy) {
        super();
        this.settingId = settingId;
        this.settingCode = settingCode;
        this.mailSubject = mailSubject;
        this.mailTo = mailTo;
        this.mailCc = mailCc;
        this.mailBcc = mailBcc;
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

    public String getSettingCode() {
        return settingCode;
    }

    public void setSettingCode(String settingCode) {
        this.settingCode = settingCode;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailCc() {
        return mailCc;
    }

    public void setMailCc(String mailCc) {
        this.mailCc = mailCc;
    }

    public String getMailBcc() {
        return mailBcc;
    }

    public void setMailBcc(String mailBcc) {
        this.mailBcc = mailBcc;
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
