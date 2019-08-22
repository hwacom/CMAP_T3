package com.cmap.plugin.module.vmswitch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VmSwitchVO {

	private enum SwitchType {
		POWER_OFF,	// 切成備援
		POWER_ON	// 復原
	};

	private String logKey;
	private String apiVmName;
	private SwitchType switchType;
	private boolean isEPDG;

	private String restoreVersionId;
	private List<String> oriConfigList;
	private List<String> newConfigList;

	private Map<Integer, String> esxiIdNameMapping = new HashMap<>();
	private String deviceListId;

	private boolean vmNowFailure;
	private String vmStatus;
	private String vmStatusMsg;

    public String getLogKey() {
        return logKey;
    }
    public void setLogKey(String logKey) {
        this.logKey = logKey;
    }
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
    public boolean isEPDG() {
        return isEPDG;
    }
    public void setEPDG(boolean isEPDG) {
        this.isEPDG = isEPDG;
    }
    public String getRestoreVersionId() {
        return restoreVersionId;
    }
    public void setRestoreVersionId(String restoreVersionId) {
        this.restoreVersionId = restoreVersionId;
    }
    public List<String> getOriConfigList() {
        return oriConfigList;
    }
    public void setOriConfigList(List<String> oriConfigList) {
        this.oriConfigList = oriConfigList;
    }
    public List<String> getNewConfigList() {
        return newConfigList;
    }
    public void setNewConfigList(List<String> newConfigList) {
        this.newConfigList = newConfigList;
    }
    public Map<Integer, String> getEsxiIdNameMapping() {
        return esxiIdNameMapping;
    }
    public void setEsxiIdNameMapping(Map<Integer, String> esxiIdNameMapping) {
        this.esxiIdNameMapping = esxiIdNameMapping;
    }
    public String getDeviceListId() {
        return deviceListId;
    }
    public void setDeviceListId(String deviceListId) {
        this.deviceListId = deviceListId;
    }
    public boolean isVmNowFailure() {
        return vmNowFailure;
    }
    public void setVmNowFailure(boolean vmNowFailure) {
        this.vmNowFailure = vmNowFailure;
    }
    public String getVmStatus() {
        return vmStatus;
    }
    public void setVmStatus(String vmStatus) {
        this.vmStatus = vmStatus;
    }
    public String getVmStatusMsg() {
        return vmStatusMsg;
    }
    public void setVmStatusMsg(String vmStatusMsg) {
        this.vmStatusMsg = vmStatusMsg;
    }
}
