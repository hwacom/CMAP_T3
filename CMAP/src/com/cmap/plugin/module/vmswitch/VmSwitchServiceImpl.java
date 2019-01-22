package com.cmap.plugin.module.vmswitch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.comm.enums.ScriptType;
import com.cmap.dao.ConfigVersionInfoDAO;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.model.DeviceList;
import com.cmap.service.DeliveryService;
import com.cmap.service.ScriptService;
import com.cmap.service.VersionService;
import com.cmap.service.impl.CommonServiceImpl;
import com.cmap.service.vo.DeliveryParameterVO;
import com.cmap.service.vo.DeliveryServiceVO;
import com.cmap.service.vo.ScriptServiceVO;
import com.cmap.service.vo.VersionServiceVO;

@Service("vmSwitchService")
@Transactional
public class VmSwitchServiceImpl extends CommonServiceImpl implements VmSwitchService {
	@Log
	private static Logger log;

	@Autowired
	private VmSwitchDAO vmSwitchDAO;

	@Autowired
	private DeviceListDAO deviceListDAO;

	@Autowired
	private ConfigVersionInfoDAO configVersionInfoDAO;

	@Autowired
	private VersionService versionService;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private ScriptService scriptService;

	private boolean chkListIsEmpty(List<? extends Object> chkList) {
		return (chkList == null || (chkList != null && chkList.isEmpty()));
	}

	@Override
	public String powerOff(VmSwitchVO vmSwitchVO) throws ServiceLayerException {
		final String apiVmName = vmSwitchVO.getApiVmName();
		String retVal = "已成功將【" + apiVmName + "】切換至備援模式";

		try {
			/*
			 * Step 1. 查詢 VM name 對照表，取得 API 傳入的名稱對應到 CMAP & ESXi 內實際 VMware 設定的名稱
			 */
			ModuleVmNameMapping mapping = vmSwitchDAO.findVmNameMappingInfoByApiVmName(apiVmName);

			if (mapping == null) {
				throw new ServiceLayerException("API傳入的VM name查詢不到 ModuleVmNameMapping 資料 >> apiVmName: " + apiVmName);
			}

			final String nameOfCmap = mapping.getNameOfCmap();
			final String deviceListId = mapping.getDeviceListId();

			Map<Integer, String> esxiIdNameMapping = new HashMap<>();
			for (ModuleVmNameMappingDetail detail : mapping.getModuleVmNameMappingDetail()) {
				Integer esxiSettingId = detail.getModuleVmEsxiSetting().getSettingId();
				String nameOfVmware = detail.getNameOfVmware();

				esxiIdNameMapping.put(esxiSettingId, nameOfVmware);
			}

			if (StringUtils.isBlank(deviceListId)) {
				throw new ServiceLayerException("API傳入的VM name對照不到CMAP名稱 >> apiVmName: " + apiVmName + ", deviceListId: " + deviceListId);
			}

			/*
			 * Step 2. 查詢指定的 VM name 系統組態備份紀錄，確認有無備份紀錄，有的話才有辦法執行後續動作
			 */
			DeviceList deviceList = deviceListDAO.findDeviceListByDeviceListId(deviceListId);

			if (deviceList == null) {
				throw new ServiceLayerException("API傳入的VM name查詢不到CMAP DeviceList 資料 >> apiVmName: " + apiVmName);
			}

			final String groupId = deviceList.getGroupId();
			final String deviceId = deviceList.getDeviceId();

			ConfigVersionInfoDAOVO cviDAOVO = new ConfigVersionInfoDAOVO();
			cviDAOVO.setQueryGroup1(groupId);
			cviDAOVO.setQueryDevice1(deviceId);
			List<Object[]> versionList = configVersionInfoDAO.findConfigVersionInfoByDAOVO4New(cviDAOVO, null, null);

			if (chkListIsEmpty(versionList)) {
				throw new ServiceLayerException("API傳入的VM name查詢不到組態備份紀錄(ConfigVersionInfo) >> apiVmName: " + apiVmName + ", groupId: " + groupId + ", deviceId: " + deviceId);
			}

			final ConfigVersionInfo configVersionInfo = (ConfigVersionInfo)versionList.get(0)[0];

			/*
			 * Step 2-1. 取得最新備份版本內容 for 後續切換使用
			 */
			List<String> configContent = null;

			List<String> versionIDs = new ArrayList<>();
			versionIDs.add(configVersionInfo.getVersionId());

			List<VersionServiceVO> vsVOList = versionService.findConfigFilesInfo(versionIDs);

			if (chkListIsEmpty(vsVOList)) {
				throw new ServiceLayerException("API傳入的VM name查詢不到組態備份紀錄(ConfigVersionInfo) >> apiVmName: " + apiVmName + ", groupId: " + groupId + ", deviceId: " + deviceId);

			} else {
				VersionServiceVO retVO = vsVOList.get(0);

				try {
					retVO = versionService.getConfigFileContent(retVO);

				} catch (ServiceLayerException sle) {
					throw new ServiceLayerException(
							"API傳入的VM name無法取得前次備份的組態檔內容 >> apiVmName: " + apiVmName + ", groupId: " + groupId + ", deviceId: " + deviceId + ", filePath: " + retVO.getConfigFileDirPath() + ", fileName: " + retVO.getFileFullName());
				}

				if (retVO.getConfigContentList() == null
						|| (retVO.getConfigContentList() != null && retVO.getConfigContentList().isEmpty())) {

					throw new ServiceLayerException(
							"API傳入的VM name取得的組態檔內容為空 >> apiVmName: " + apiVmName + ", groupId: " + groupId + ", deviceId: " + deviceId + ", filePath: " + retVO.getConfigFileDirPath() + ", fileName: " + retVO.getFileFullName());

				} else {
					configContent = retVO.getConfigContentList();
				}
			}

			/*
			 * Step 3. 取得 VMWare ESXi 主機連線資訊
			 */
			List<ModuleVmEsxiSetting> esxiSettings = vmSwitchDAO.findAllVmEsxiSetting();

			if (chkListIsEmpty(esxiSettings)) {
				throw new ServiceLayerException("查詢不到 VM ESXi 主機表設定 >> ModuleVmEsxiSetting");
			}

			/*
			 * Step 4. 迴圈執行多台 ESXi 主機備援切換動作
			 */
			ScriptServiceVO vmInfoScriptVO = scriptService.findDefaultScriptInfoByScriptTypeAndSystemVersion(ScriptType.VM_INFO.toString(), Constants.DATA_STAR_SYMBOL);

			if (vmInfoScriptVO == null) {
				throw new ServiceLayerException("查詢不到預設腳本 for 查詢VM ID >> scriptType: " + ScriptType.VM_INFO);
			}

			ScriptServiceVO powerOffScriptVO = scriptService.findDefaultScriptInfoByScriptTypeAndSystemVersion(ScriptType.VM_POWER_OFF.toString(), Constants.DATA_STAR_SYMBOL);

			if (powerOffScriptVO == null) {
				throw new ServiceLayerException("查詢不到預設腳本 for 關機VM >> scriptType: " + ScriptType.VM_POWER_OFF);
			}

			Map<String, String> deviceInfo;
			DeliveryServiceVO deliveryVO;
			for (ModuleVmEsxiSetting esxi : esxiSettings) {
				final Integer esxiID = esxi.getSettingId();

				int round = 1;
				int retryTimes = StringUtils.isNotBlank(Env.RETRY_TIMES) ? Integer.parseInt(Env.RETRY_TIMES) : 1;

				while (round <= retryTimes) {
					try {
						/*
						 * Step 4-1. 執行 CLI 取得 VM name 對應的 VM ID
						 */
						DeliveryParameterVO dpVO = new DeliveryParameterVO();
						dpVO.setScriptInfoId(vmInfoScriptVO.getScriptInfoId());
						dpVO.setScriptCode(vmInfoScriptVO.getScriptCode());

						deviceInfo = new HashMap<>();
						deviceInfo.put(Constants.DEVICE_IP, esxi.getHostIp());
						deviceInfo.put(Constants.DEVICE_NAME, esxi.getEsxiName());
						deviceInfo.put(Constants.DEVICE_LOGIN_ACCOUNT, base64Decoder(esxi.getLoginAccount()));		//Base64解碼
						deviceInfo.put(Constants.DEVICE_LOGIN_PASSWORD, base64Decoder(esxi.getLoginPassword()));	//Base64解碼
						dpVO.setDeviceInfo(deviceInfo);

						deliveryVO = deliveryService.doDelivery(Env.CONNECTION_MODE_OF_VM_SWITCH, dpVO, true, "PRTG", "【VM備援切換】1/3.取得ESXi主機上VM清單", false);

						final String nameOfVMware = esxiIdNameMapping.get(esxiID);
						final String vmInfo = StringUtils.split(deliveryVO.getCmdOutputList().get(0), Env.COMM_SEPARATE_SYMBOL)[1];
						System.out.println(nameOfVMware + " >> [ VM INFO ] ##############################################################################");
						System.out.println(vmInfo);

						final String vmId = analyzeVmId(vmInfo, nameOfVMware);
						System.out.println("*********** VM_ID = [" + vmId + "]");

						/*
						 * Step 4-2. 執行 CLI 將指定的 VM ID 關機(power off)
						 */
						dpVO.setScriptInfoId(powerOffScriptVO.getScriptInfoId());
						dpVO.setScriptCode(powerOffScriptVO.getScriptCode());

						final String scriptKey = powerOffScriptVO.getActionScriptVariable();

						List<String> varKey = (List<String>)transJSON2Object(scriptKey, List.class);
						dpVO.setVarKey(varKey);

						List<List<String>> varValue = new ArrayList<>();
						List<String> vmIdList = new ArrayList<>();
						vmIdList.add(vmId);
						varValue.add(vmIdList);
						dpVO.setVarValue(varValue);

						deliveryVO = deliveryService.doDelivery(Env.CONNECTION_MODE_OF_VM_SWITCH, dpVO, true, "PRTG", "【VM備援切換】2/3.將異常的VM進行關機", false);

						break;		//執行過程正常下停止retry迴圈

					} catch (Exception e) {
						log.error(e.toString(), e);
						round++;	//執行異常進行retry
					}
				}
			}

			/*
			 * Step 5. 登入備援機，進入 Conf 模式將指定的 VM 要切換的組態內容逐行貼上，最後執行 Exit 離開 Conf 模式並立即生效
			 */
			VersionServiceVO vsVO = new VersionServiceVO();
			versionService.recoverConfig(Constants.RECOVER_METHOD_BY_CLI, vsVO, "PRTG", "【VM備援切換】3/3.寫入備份組態設定至還原機");

			/*
			 * Step 6. 將切換紀錄寫入 DB
			 */

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException();
		}
		return retVal;
	}

	/**
	 * 分析ESXi上VM清單，取得異常VM名稱對應的ID值
	 * @param vmInfo
	 * @param targetVmName
	 * @return
	 */
	private String analyzeVmId(String vmInfo, String targetVmName) {
		String vmId = null;
		try {
			String[] lines = vmInfo.split("\r\n");

			for (String line : lines) {
				if (StringUtils.contains(line, targetVmName)) {
					String[] tmp = line.split(targetVmName);

					vmId = StringUtils.isNotBlank(tmp[0]) ? tmp[0].replaceAll("\\s+","") : null;
					break;
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return vmId;
	}

	private List<String> transVarKeyList(String scriptVariables) {

		return null;
	}

	private List<List<String>> transOneDeviceVarValueList(String values) {

		return null;
	}

	@Override
	public String powerOn(VmSwitchVO vmSwitchVO) throws ServiceLayerException {
		final String apiVmName = vmSwitchVO.getApiVmName();
		String retVal = "已成功將【" + apiVmName + "】從備援模式恢復成一般模式";

		try {




		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException();
		}
		return retVal;
	}
}
