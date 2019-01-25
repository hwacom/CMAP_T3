package com.cmap.plugin.module.vmswitch;

public class VmSwitchVO {

	private enum SwitchType {
		POWER_OFF,	// 切成備援
		POWER_ON	// 復原
	};

	private String apiVmName;
	private SwitchType switchType;

	public String getApiVmName() {
		return apiVmName;
	}
	public void setApiVmName(String apiVmName) {
		this.apiVmName = apiVmName;
	}
	public SwitchType getSwitchType() {
		return switchType;
	}
	public void setSwitchType(SwitchType switchType) {
		this.switchType = switchType;
	}
}
