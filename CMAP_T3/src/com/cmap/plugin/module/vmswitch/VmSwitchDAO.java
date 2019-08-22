package com.cmap.plugin.module.vmswitch;

import java.util.List;
import com.cmap.dao.BaseDAO;

public interface VmSwitchDAO extends BaseDAO {

	/**
	 * 使用 API 呼叫傳入的 VM name 查找相關的 CMAP & VMware 內各自對應的名稱<br>
	 * @param apiVmName
	 * @return ModuleVmNameMapping
	 */
	public ModuleVmNameMapping findVmNameMappingInfoByApiVmName(String apiVmName);

	/**
	 * 查找所有 VM ESXi 設定
	 * @return List<ModuleVmEsxiSetting>
	 */
	public List<ModuleVmEsxiSetting> findAllVmEsxiSetting();

	public void saveOrUpdateProcessLog(ModuleVmProcessLog moduleVmProcessLog);

	public int updateProcessLog(ModuleVmProcessLog moduleVmProcessLog);

	public List<ModuleVmProcessLog> findModuleVmProcessLogByLogKey(String logKey);

	public List<ModuleVmProcessLog> findNotPushedModuleVmProcessLogByLogKey(String logKey);

	public ModuleVmSetting getVmSetting(String settingName);

	public void updateVmSetting(ModuleVmSetting entity);
}
