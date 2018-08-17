package com.cmap.dao.vo;

public class ScriptListDAOVO {

	private String scriptListId;

	private String scriptTypeId;
	private String scriptCode;
	private String scriptName;
	private String scriptStepOrder;
	private String scriptContent;
	private String expectedTerminalSymbol;
	private String output;
	private String headCuttingLines;
	private String tailCuttingLines;
	private String scriptDescription;
	private String remark;
	private String errorSymbol;

	public String getScriptListId() {
		return scriptListId;
	}
	public void setScriptListId(String scriptListId) {
		this.scriptListId = scriptListId;
	}
	public String getScriptTypeId() {
		return scriptTypeId;
	}
	public void setScriptTypeId(String scriptTypeId) {
		this.scriptTypeId = scriptTypeId;
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
	public String getScriptStepOrder() {
		return scriptStepOrder;
	}
	public void setScriptStepOrder(String scriptStepOrder) {
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
	public String getHeadCuttingLines() {
		return headCuttingLines;
	}
	public void setHeadCuttingLines(String headCuttingLines) {
		this.headCuttingLines = headCuttingLines;
	}
	public String getTailCuttingLines() {
		return tailCuttingLines;
	}
	public void setTailCuttingLines(String tailCuttingLines) {
		this.tailCuttingLines = tailCuttingLines;
	}
	public String getScriptDescription() {
		return scriptDescription;
	}
	public void setScriptDescription(String scriptDescription) {
		this.scriptDescription = scriptDescription;
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
}
