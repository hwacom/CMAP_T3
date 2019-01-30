package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "script_step_action",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"step_id"})
		}
		)
public class ScriptStepAction {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "step_id", unique = true)
	private String stepId;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "script_info_id", nullable = false)
	private ScriptInfo scriptInfo;

	@Column(name = "step_order", nullable = false)
	private Integer stepOrder;

	@Column(name = "command", nullable = false)
	private String command;

	@Column(name = "expected_terminal_symbol", nullable = true)
	private String expectedTerminalSymbol;

	@Column(name = "output_flag", nullable = true)
	private String outputFlag;

	@Column(name = "head_cutting_lines", nullable = true)
	private Integer headCuttingLines;

	@Column(name = "tail_cutting_lines", nullable = true)
	private Integer tailCuttingLines;

	@Column(name = "error_symbol", nullable = true)
	private String errorSymbol;

	@Column(name = "repeat_flag", nullable = false)
	private String repeatFlag;

	@Column(name = "command_remark", nullable = true)
	private String commandRemark;

	@Column(name = "command_description", nullable = true)
	private String commandDescription;

	@Column(name = "delete_flag", nullable = false)
	private String deleteFlag;

	@Column(name = "delete_time", nullable = true)
	private Timestamp deleteTime;

	@Column(name = "delete_by", nullable = true)
	private String deleteBy;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "update_time", nullable = false)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = false)
	private String updateBy;

	public ScriptStepAction() {
		super();
	}

	public ScriptStepAction(String stepId, ScriptInfo scriptInfo, Integer stepOrder, String command,
			String expectedTerminalSymbol, String outputFlag, Integer headCuttingLines, Integer tailCuttingLines,
			String errorSymbol, String repeatFlag, String commandRemark, String commandDescription, String deleteFlag,
			Timestamp deleteTime, String deleteBy, Timestamp createTime, String createBy, Timestamp updateTime,
			String updateBy) {
		super();
		this.stepId = stepId;
		this.scriptInfo = scriptInfo;
		this.stepOrder = stepOrder;
		this.command = command;
		this.expectedTerminalSymbol = expectedTerminalSymbol;
		this.outputFlag = outputFlag;
		this.headCuttingLines = headCuttingLines;
		this.tailCuttingLines = tailCuttingLines;
		this.errorSymbol = errorSymbol;
		this.repeatFlag = repeatFlag;
		this.commandRemark = commandRemark;
		this.commandDescription = commandDescription;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public ScriptInfo getScriptInfo() {
		return scriptInfo;
	}

	public void setScriptInfo(ScriptInfo scriptInfo) {
		this.scriptInfo = scriptInfo;
	}

	public Integer getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getExpectedTerminalSymbol() {
		return expectedTerminalSymbol;
	}

	public void setExpectedTerminalSymbol(String expectedTerminalSymbol) {
		this.expectedTerminalSymbol = expectedTerminalSymbol;
	}

	public String getOutputFlag() {
		return outputFlag;
	}

	public void setOutputFlag(String outputFlag) {
		this.outputFlag = outputFlag;
	}

	public Integer getHeadCuttingLines() {
		return headCuttingLines;
	}

	public void setHeadCuttingLines(Integer headCuttingLines) {
		this.headCuttingLines = headCuttingLines;
	}

	public Integer getTailCuttingLines() {
		return tailCuttingLines;
	}

	public void setTailCuttingLines(Integer tailCuttingLines) {
		this.tailCuttingLines = tailCuttingLines;
	}

	public String getErrorSymbol() {
		return errorSymbol;
	}

	public void setErrorSymbol(String errorSymbol) {
		this.errorSymbol = errorSymbol;
	}

	public String getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

	public String getCommandRemark() {
		return commandRemark;
	}

	public void setCommandRemark(String commandRemark) {
		this.commandRemark = commandRemark;
	}

	public String getCommandDescription() {
		return commandDescription;
	}

	public void setCommandDescription(String commandDescription) {
		this.commandDescription = commandDescription;
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
