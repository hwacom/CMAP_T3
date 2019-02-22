package com.cmap.dao.vo;

public class ScriptInfoDAOVO extends CommonDAOVO {

	private String queryScriptTypeId;
	private String queryScriptTypeCode;
	private String queryScriptInfoId;
	private String querySystemDefault;
	private boolean onlySwitchPort = false;
	private boolean onlyIpOpenBlock = false;
	private boolean onlyMacOpenBlock = false;

	public String getQueryScriptTypeId() {
		return queryScriptTypeId;
	}

	public void setQueryScriptTypeId(String queryScriptTypeId) {
		this.queryScriptTypeId = queryScriptTypeId;
	}

	public String getQueryScriptInfoId() {
		return queryScriptInfoId;
	}

	public void setQueryScriptInfoId(String queryScriptInfoId) {
		this.queryScriptInfoId = queryScriptInfoId;
	}

	public String getQuerySystemDefault() {
		return querySystemDefault;
	}

	public void setQuerySystemDefault(String querySystemDefault) {
		this.querySystemDefault = querySystemDefault;
	}

	public boolean isOnlySwitchPort() {
		return onlySwitchPort;
	}

	public void setOnlySwitchPort(boolean onlySwitchPort) {
		this.onlySwitchPort = onlySwitchPort;
	}

	public String getQueryScriptTypeCode() {
		return queryScriptTypeCode;
	}

	public void setQueryScriptTypeCode(String queryScriptTypeCode) {
		this.queryScriptTypeCode = queryScriptTypeCode;
	}

	public boolean isOnlyIpOpenBlock() {
		return onlyIpOpenBlock;
	}

	public void setOnlyIpOpenBlock(boolean onlyIpOpenBlock) {
		this.onlyIpOpenBlock = onlyIpOpenBlock;
	}

	public boolean isOnlyMacOpenBlock() {
		return onlyMacOpenBlock;
	}

	public void setOnlyMacOpenBlock(boolean onlyMacOpenBlock) {
		this.onlyMacOpenBlock = onlyMacOpenBlock;
	}
}
