package com.cmap.dao.vo;

public class SysMailDAOVO {

    private String mailListSettingId;
    private String mailContentSettingId;
    private String settingCode;

    private String subject;
    private String[] mailTo;
    private String[] mailCc;
    private String[] mailBcc;
    private String remark;

    private String mailContent;

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String[] getMailTo() {
        return mailTo;
    }
    public void setMailTo(String[] mailTo) {
        this.mailTo = mailTo;
    }
    public String[] getMailCc() {
        return mailCc;
    }
    public void setMailCc(String[] mailCc) {
        this.mailCc = mailCc;
    }
    public String[] getMailBcc() {
        return mailBcc;
    }
    public void setMailBcc(String[] mailBcc) {
        this.mailBcc = mailBcc;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getMailContent() {
        return mailContent;
    }
    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }
    public String getMailListSettingId() {
        return mailListSettingId;
    }
    public void setMailListSettingId(String mailListSettingId) {
        this.mailListSettingId = mailListSettingId;
    }
    public String getMailContentSettingId() {
        return mailContentSettingId;
    }
    public void setMailContentSettingId(String mailContentSettingId) {
        this.mailContentSettingId = mailContentSettingId;
    }
    public String getSettingCode() {
        return settingCode;
    }
    public void setSettingCode(String settingCode) {
        this.settingCode = settingCode;
    }
}
