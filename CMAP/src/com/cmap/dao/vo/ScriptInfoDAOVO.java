package com.cmap.dao.vo;

public class ScriptInfoDAOVO extends CommonDAOVO {

	private String queryScriptTypeId;
	private String queryScriptInfoId;
	private String querySystemDefault;
	private boolean onlySwitchPort = false;

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
}
