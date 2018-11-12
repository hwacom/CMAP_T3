package com.cmap.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
		name = "script_info",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"script_info_id"})
		}
		)
public class ScriptInfo {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "script_info_id", unique = true)
	private String scriptInfoId;

	@Column(name = "script_code", nullable = false)
	private String scriptCode;

	@Column(name = "script_name", nullable = false)
	private String scriptName;

	@OneToOne
	@JoinColumn(name = "script_type_id")
	private ScriptType scriptType;

	@Column(name = "system_version", nullable = false)
	private String systemVersion;

	@Column(name = "action_script", nullable = false)
	private String actionScript;

	@Column(name = "action_script_variable", nullable = true)
	private String actionScriptVariable;

	@Column(name = "action_script_remark", nullable = true)
	private String actionScriptRemark;

	@Column(name = "check_script", nullable = true)
	private String checkScript;

	@Column(name = "check_script_remark", nullable = true)
	private String checkScriptRemark;

	@Column(name = "check_keyword", nullable = true)
	private String checkKeyword;

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scriptInfo")
	private List<ScriptStepAction> scriptStepActions;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scriptInfo")
	private List<ScriptStepCheck> scriptStepChecks;

	public ScriptInfo() {
		super();
	}

	public ScriptInfo(String scriptInfoId, String scriptCode, String scriptName, ScriptType scriptType,
			String systemVersion, String actionScript, String actionScriptVariable, String actionScriptRemark,
			String checkScript, String checkScriptRemark, String checkKeyword, String scriptDescription,
			String deleteFlag, Timestamp deleteTime, String deleteBy, Timestamp createTime, String createBy,
			Timestamp updateTime, String updateBy, List<ScriptStepAction> scriptStepActions,
			List<ScriptStepCheck> scriptStepChecks) {
		super();
		this.scriptInfoId = scriptInfoId;
		this.scriptCode = scriptCode;
		this.scriptName = scriptName;
		this.scriptType = scriptType;
		this.systemVersion = systemVersion;
		this.actionScript = actionScript;
		this.actionScriptVariable = actionScriptVariable;
		this.actionScriptRemark = actionScriptRemark;
		this.checkScript = checkScript;
		this.checkScriptRemark = checkScriptRemark;
		this.checkKeyword = checkKeyword;
		this.scriptDescription = scriptDescription;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
		this.scriptStepActions = scriptStepActions;
		this.scriptStepChecks = scriptStepChecks;
	}

	public String getScriptInfoId() {
		return scriptInfoId;
	}

	public void setScriptInfoId(String scriptInfoId) {
		this.scriptInfoId = scriptInfoId;
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

	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getActionScript() {
		return actionScript;
	}

	public void setActionScript(String actionScript) {
		this.actionScript = actionScript;
	}

	public String getActionScriptVariable() {
		return actionScriptVariable;
	}

	public void setActionScriptVariable(String actionScriptVariable) {
		this.actionScriptVariable = actionScriptVariable;
	}

	public String getActionScriptRemark() {
		return actionScriptRemark;
	}

	public void setActionScriptRemark(String actionScriptRemark) {
		this.actionScriptRemark = actionScriptRemark;
	}

	public String getCheckScript() {
		return checkScript;
	}

	public void setCheckScript(String checkScript) {
		this.checkScript = checkScript;
	}

	public String getCheckScriptRemark() {
		return checkScriptRemark;
	}

	public void setCheckScriptRemark(String checkScriptRemark) {
		this.checkScriptRemark = checkScriptRemark;
	}

	public String getCheckKeyword() {
		return checkKeyword;
	}

	public void setCheckKeyword(String checkKeyword) {
		this.checkKeyword = checkKeyword;
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

	public List<ScriptStepAction> getScriptStepActions() {
		return scriptStepActions;
	}

	public void setScriptStepActions(List<ScriptStepAction> scriptStepActions) {
		this.scriptStepActions = scriptStepActions;
	}

	public List<ScriptStepCheck> getScriptStepChecks() {
		return scriptStepChecks;
	}

	public void setScriptStepChecks(List<ScriptStepCheck> scriptStepChecks) {
		this.scriptStepChecks = scriptStepChecks;
	}
}
