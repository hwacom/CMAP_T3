package com.cmap.plugin.module.vmswitch;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="module_vm_process_log")
public class ModuleVmProcessLog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id", nullable = false)
	private Integer logId;

	@Column(name = "log_key", nullable = false)
	private String logKey;

	@Column(name = "order_no", nullable = false)
	private Integer orderNo;

	@Column(name = "step", nullable = false)
	private String step;

	@Column(name = "result", nullable = false)
    private String result;

	@Column(name = "message", nullable = false)
	private String message;

	@Column(name = "pushed", nullable = false)
    private String pushed = "N";

	@Column(name = "create_time", nullable = true)
	private Timestamp createTime = new Timestamp(new Date().getTime());

	@Column(name = "create_by", nullable = true)
	private String createBy;

	@Column(name = "update_time", nullable = true)
	private Timestamp updateTime = new Timestamp(new Date().getTime());

	@Column(name = "update_by", nullable = true)
	private String updateBy;

	public ModuleVmProcessLog() {
		super();
		// TODO Auto-generated constructor stub
	}

    public ModuleVmProcessLog(Integer logId, String logKey, Integer orderNo, String step,
            String result, String message, String pushed, Timestamp createTime, String createBy,
            Timestamp updateTime, String updateBy) {
        super();
        this.logId = logId;
        this.logKey = logKey;
        this.orderNo = orderNo;
        this.step = step;
        this.result = result;
        this.message = message;
        this.pushed = pushed;
        this.createTime = createTime;
        this.createBy = createBy;
        this.updateTime = updateTime;
        this.updateBy = updateBy;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getLogKey() {
        return logKey;
    }

    public void setLogKey(String logKey) {
        this.logKey = logKey;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPushed() {
        return pushed;
    }

    public void setPushed(String pushed) {
        this.pushed = pushed;
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
