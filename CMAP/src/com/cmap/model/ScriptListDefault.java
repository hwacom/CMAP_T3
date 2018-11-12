package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "script_list_default",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"script_list_id"})
		}
		)
public class ScriptListDefault {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "script_list_id", unique = true)
	private String scriptListId;

	@OneToOne
	@JoinColumn(name = "script_type_id")
	private ScriptType scriptType;

	@Column(name = "script_code", nullable = false)
	private String scriptCode;

	@Column(name = "script_name", nullable = false)
	private String scriptName;

	@Column(name = "script_step_order", nullable = false)
	private Integer scriptStepOrder;

	@Column(name = "script_content", nullable = false)
	private String scriptContent;

	@Column(name = "expected_terminal_symbol", nullable = true)
	private String expectedTerminalSymbol;

	@Column(name = "output", nullable = true)
	private String output;

	@Column(name = "head_cutting_lines", nullable = true)
	private Integer headCuttingLines;

	@Column(name = "tail_cutting_lines", nullable = true)
	private Integer tailCuttingLines;

	@Column(name = "remark", nullable = true)
	private String remark;

	@Column(name = "error_symbol", nullable = true)
	private String errorSymbol;

	@Column(name = "script_description", nullable = true)
	private String scriptDescription;

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

	public ScriptListDefault() {
		super();
	}

	public ScriptListDefault(String scriptListId, ScriptType scriptType, String scriptCode, String scriptName,
			Integer scriptStepOrder, String scriptContent, String expectedTerminalSymbol, String output,
			Integer headCuttingLines, Integer tailCuttingLines, String remark, String errorSymbol,
			String scriptDescription, String deleteFlag, Timestamp deleteTime, String deleteBy, Timestamp createTime,
			String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.scriptListId = scriptListId;
		this.scriptType = scriptType;
		this.scriptCode = scriptCode;
		this.scriptName = scriptName;
		this.scriptStepOrder = scriptStepOrder;
		this.scriptContent = scriptContent;
		this.expectedTerminalSymbol = expectedTerminalSymbol;
		this.output = output;
		this.headCuttingLines = headCuttingLines;
		this.tailCuttingLines = tailCuttingLines;
		this.remark = remark;
		this.errorSymbol = errorSymbol;
		this.scriptDescription = scriptDescription;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getScriptListId() {
		return scriptListId;
	}

	public void setScriptListId(String scriptListId) {
		this.scriptListId = scriptListId;
	}

	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public Integer getScriptStepOrder() {
		return scriptStepOrder;
	}

	public void setScriptStepOrder(Integer scriptStepOrder) {
		this.scriptStepOrder = scriptStepOrder;
	}

	public String getScriptContent() {
		return scriptContent;
	}

	public void setScriptContent(String scriptContent) {
		this.scriptContent = scriptContent;
	}

	public String getExpectedTerminalSymbol() {
		return expectedTerminalSymbol;
	}

	public void setExpectedTerminalSymbol(String expectedTerminalSymbol) {
		this.expectedTerminalSymbol = expectedTerminalSymbol;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getErrorSymbol() {
		return errorSymbol;
	}

	public void setErrorSymbol(String errorSymbol) {
		this.errorSymbol = errorSymbol;
	}

	public String getScriptDescription() {
		return scriptDescription;
	}

	public void setScriptDescription(String scriptDescription) {
		this.scriptDescription = scriptDescription;
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