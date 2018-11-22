package com.cmap.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.ProvisionLogDAO;
import com.cmap.dao.ScriptInfoDAO;
import com.cmap.dao.vo.ScriptInfoDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DeviceList;
import com.cmap.model.ProvisionAccessLog;
import com.cmap.model.ScriptInfo;
import com.cmap.security.SecurityUtil;
import com.cmap.service.DeliveryService;
import com.cmap.service.ProvisionService;
import com.cmap.service.StepService;
import com.cmap.service.vo.DeliveryParameterVO;
import com.cmap.service.vo.DeliveryServiceVO;
import com.cmap.service.vo.ProvisionServiceVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.utils.impl.CommonUtils;

@Service("deliveryService")
@Transactional
public class DeliveryServiceImpl extends CommonServiceImpl implements DeliveryService {
	@Log
	private static Logger log;

	@Autowired
	private ScriptInfoDAO scriptInfoDAO;

	@Autowired
	private ProvisionLogDAO provisionLogDAO;

	@Autowired
	private DeviceListDAO deviceListDAO;

	@Autowired
	private StepService stepService;

	@Autowired
	private ProvisionService provisionService;

	@Override
	public long countDeviceList(DeliveryServiceVO dsVO) throws ServiceLayerException {
		// TODO 自動產生的方法 Stub
		return 0;
	}

	@Override
	public List<DeliveryServiceVO> findDeviceList(DeliveryServiceVO dsVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		// TODO 自動產生的方法 Stub
		return null;
	}

	@Override
	public long countScriptList(DeliveryServiceVO dsVO) throws ServiceLayerException {
		long retVal = 0;
		try {
			ScriptInfoDAOVO siDAOVO = new ScriptInfoDAOVO();
			BeanUtils.copyProperties(dsVO, siDAOVO);

			retVal = scriptInfoDAO.countScriptInfo(siDAOVO);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢發生異常，請嘗試重新操作");
		}
		return retVal;
	}

	@Override
	public List<DeliveryServiceVO> findScriptList(DeliveryServiceVO dsVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		List<DeliveryServiceVO> retList = new ArrayList<DeliveryServiceVO>();
		try {
			ScriptInfoDAOVO siDAOVO = new ScriptInfoDAOVO();
			BeanUtils.copyProperties(dsVO, siDAOVO);

			List<ScriptInfo> entities = scriptInfoDAO.findScriptInfo(siDAOVO, startRow, pageLength);

			DeliveryServiceVO vo;
			if (entities != null && !(entities.isEmpty())) {
				for (ScriptInfo entity : entities) {
					vo = new DeliveryServiceVO();
					BeanUtils.copyProperties(entity, vo);
					vo.setScriptTypeName(entity.getScriptType().getScriptTypeName());

					retList.add(vo);
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢發生異常，請嘗試重新操作");
		}
		return retList;
	}

	@Override
	public DeliveryServiceVO getScriptInfoById(String scriptInfoId) throws ServiceLayerException {
		DeliveryServiceVO retVO = new DeliveryServiceVO();
		try {
			ScriptInfoDAOVO siDAOVO = new ScriptInfoDAOVO();
			siDAOVO.setQueryScriptInfoId(scriptInfoId);

			List<ScriptInfo> entities = scriptInfoDAO.findScriptInfo(siDAOVO, null, null);

			if (entities != null && !entities.isEmpty()) {
				ScriptInfo entity = entities.get(0);
				BeanUtils.copyProperties(entity, retVO);
				retVO.setScriptTypeName(entity.getScriptType().getScriptTypeName());

			} else {
				throw new ServiceLayerException("查詢此腳本資料，請重新查詢");
			}

		} catch (ServiceLayerException sle) {
			log.error(sle.toString(), sle);
			throw sle;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢發生異常，請嘗試重新操作");
		}
		return retVO;
	}

	private boolean checkDeliveryParameter(DeliveryParameterVO pVO) {
		try {
			//Step 1.檢核腳本是否存在
			ScriptInfoDAOVO siDAOVO = new ScriptInfoDAOVO();
			siDAOVO.setQueryScriptInfoId(pVO.getScriptInfoId());

			List<ScriptInfo> scriptList = scriptInfoDAO.findScriptInfo(siDAOVO, null, null);

			if (scriptList == null || (scriptList != null && scriptList.isEmpty())) {
				return false;
			}

			ScriptInfo dbEntity = scriptList.get(0);

			//Step 2.檢核JSON內Script_Info_Id與Script_Code是否匹配
			final String dbScriptCode = dbEntity.getScriptCode();
			final String jsonScriptCode = pVO.getScriptCode();

			if (!dbScriptCode.equals(jsonScriptCode)) {
				return false;
			}

			//Step 3.檢核JSON內VarKey與系統內設定的腳本變數欄位是否相符
			final String dbVarKeyJSON = dbEntity.getActionScriptVariable();
			final List<String> dbVarKeyList = (List<String>)transJSON2Object(dbVarKeyJSON, ArrayList.class);
			final List<String> jsonVarKeyList = pVO.getVarKey();

			if (dbVarKeyList.size() != jsonVarKeyList.size()) {
				return false;

			} else {
				if (!dbVarKeyList.containsAll(jsonVarKeyList)) {
					return false;
				}
			}

			//Step 4.檢核變數值(VarValue)是否有缺
			//VarValue資料結構: List(設備)<List(VarValue)>
			final List<List<String>> jsonVarValueList = pVO.getVarValue();
			final List<String> jsonDeviceIdList = pVO.getDeviceId();

			if (jsonDeviceIdList.size() != jsonVarValueList.size()) {
				return false;

			} else {
				boolean success = true;
				for (List<String> varValueList : jsonVarValueList) {
					if (dbVarKeyList.size() != varValueList.size()) {
						success = false;
						break;
					}
				}

				if (!success) {
					return false;
				}
			}

			//Step 5.檢核設備是否皆存在 且 是否與JSON內設備ID相符
			final List<String> jsonGroupIdList = pVO.getGroupId();


			if (jsonGroupIdList.size() != jsonDeviceIdList.size()) {
				return false;
			}

			int i=0;
			for (String deviceId : jsonDeviceIdList) {
				String groupId = jsonGroupIdList.get(i);

				final String jsonDeviceId = jsonDeviceIdList.get(i);
				DeviceList deviceEntity = deviceListDAO.findDeviceListByGroupAndDeviceId(groupId, deviceId);

				if (deviceEntity == null || (deviceEntity != null && !deviceEntity.getDeviceId().equals(jsonDeviceId))) {
					return false;
				}

				i++;
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;
		}

		return true;
	}

	@Override
	public String doDelivery(DeliveryServiceVO dsVO, boolean jobTrigger) throws ServiceLayerException {
		String result = "";
		try {
			String psJSON = dsVO.getDeliveryParameters();

			DeliveryParameterVO pVO = (DeliveryParameterVO)transJSON2Object(psJSON, DeliveryParameterVO.class);
			System.out.println(pVO);

			/*
			 * Step 1.再次驗證傳入的參數值合法性
			 */
			boolean chkSuccess = checkDeliveryParameter(pVO);

			if (!chkSuccess) {
				throw new ServiceLayerException("供裝前系統檢核不通過，請重新操作；若仍再次出現此訊息，請與系統維護商聯繫");

			} else {
				result = goDelivery(pVO, jobTrigger);
			}

		} catch (ServiceLayerException sle) {
			log.error(sle.toString(), sle);
			throw sle;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e);
		}
		return result;
	}

	private String goDelivery(DeliveryParameterVO psVO, boolean jobTrigger) throws ServiceLayerException {
		String retMsg = "";

		//Step 1.取得腳本資料
		final String scriptInfoId = psVO.getScriptInfoId();
		ScriptInfo scriptInfo = scriptInfoDAO.findScriptInfoById(scriptInfoId);

		if (scriptInfo == null) {
			throw new ServiceLayerException("無法取得腳本資料，請重新操作");
		}

		//Step 2.替換腳本參數值
		final List<String> varKeyList = psVO.getVarKey();
		final List<List<String>> deviceVarValueList = psVO.getVarValue();
		final String actionScript = scriptInfo.getActionScript();
		final List<String> groupIdList = psVO.getGroupId();
		final List<String> deviceIdList = psVO.getDeviceId();

		ProvisionServiceVO masterVO = new ProvisionServiceVO();
		masterVO.setLogMasterId(UUID.randomUUID().toString());
		masterVO.setBeginTime(new Date());
		masterVO.setUserName(jobTrigger ? Env.USER_NAME_JOB : SecurityUtil.getSecurityUser().getUsername());

		StringBuffer errorSb = new StringBuffer();
		int deviceCount = deviceIdList.size();
		int successCount = 0;
		int failedCount = 0;
		for (int idx=0; idx<deviceIdList.size(); idx++) {

			final String groupId = groupIdList.get(idx);
			final String deviceId = deviceIdList.get(idx);

			try {
				DeviceList deviceList = deviceListDAO.findDeviceListByGroupAndDeviceId(groupId, deviceId);

				if (deviceList == null) {
					throw new ServiceLayerException("查無設備資料 >> groupId: " + groupId + " , deviceId: " + deviceId);
				}

				final String groupName = deviceList.getGroupName();
				final String deviceName = deviceList.getDeviceName();
				final String deviceListId = deviceList.getDeviceListId();
				String script = actionScript;

				Map<String, String> varMap = null;
				if (!varKeyList.isEmpty()) {
					final List<String> vValueList = deviceVarValueList.get(idx);

					varMap = new HashMap<String, String>();

					for (int i=0; i<varKeyList.size(); i++) {
						final String vKey = Env.SCRIPT_VAR_KEY_SYMBOL + varKeyList.get(i) + Env.SCRIPT_VAR_KEY_SYMBOL;
						final String vValue = vValueList.get(i);

						if (script.indexOf(vKey) != -1) {
							script = StringUtils.replace(script, vKey, vValue);
						}

						varMap.put(vKey, vValue);
					}
				}

				System.out.println("Device ID: " + deviceIdList.get(idx) + " ::: script: " + script);

				//Step 3.呼叫共用執行腳本
				StepServiceVO processVO = null;
				try {
					processVO = stepService.doScript(deviceListId, scriptInfo, varMap, jobTrigger);

					masterVO.getDetailVO().addAll(processVO.getPsVO().getDetailVO());

				} catch (Exception e) {
					log.error(e.toString(), e);
				}

				if (processVO == null || (processVO != null && !processVO.isSuccess())) {
					errorSb.append("[" + (idx+1) + "] >> 群組名稱:【" + groupName + "】/設備名稱:【" + deviceName + "】供裝失敗" + Constants.HTML_BREAK_LINE_SYMBOL);
					failedCount++;
					continue;
				}

			} catch (ServiceLayerException sle) {
				log.error(sle.toString(), sle);
				errorSb.append("[" + (idx+1) + "] >> 群組ID:【" + groupId + "】/設備ID:【" + deviceId + "】供裝失敗" + Constants.HTML_BREAK_LINE_SYMBOL);
				failedCount++;
				continue;

			} catch (Exception e) {
				log.error(e.toString(), e);
				errorSb.append("[" + (idx+1) + "] >> 群組ID:【" + groupId + "】/設備ID:【" + deviceId + "】供裝失敗" + Constants.HTML_BREAK_LINE_SYMBOL);
				failedCount++;
				continue;
			}

			successCount++;
		}

		String msg = "";
		String[] args = null;
		if (deviceCount == 1) {
			if (failedCount == 0) {
				msg = "供裝成功";

			} else if (failedCount == 1) {
				msg = "供裝失敗";
			}

		} else {
			msg = "選定供裝 {0} 筆設備: 成功 {1} 筆；失敗 {2} 筆";
			args = new String[] {
					String.valueOf(deviceCount),
					String.valueOf(successCount),
					String.valueOf(failedCount)
			};
		}

		masterVO.setEndTime(new Date());
		masterVO.setResult(CommonUtils.converMsg(msg, args));

		provisionService.insertProvisionLog(masterVO);

		retMsg += CommonUtils.converMsg(msg, args) + Constants.HTML_BREAK_LINE_SYMBOL + Constants.HTML_SEPARATION_LINE_SYMBOL + errorSb.toString();

		return retMsg;
	}

	@Override
	public String logAccessRecord(DeliveryServiceVO dsVO) throws ServiceLayerException {
		String uuid = null;
		try {
			Integer step = dsVO.getDeliveryStep();

			if (step == 0) {
				uuid = UUID.randomUUID().toString();
				ProvisionAccessLog access = new ProvisionAccessLog();
				access.setLogId(uuid);
				access.setIpAddress(dsVO.getIpAddr());
				access.setMacAddress(dsVO.getMacAddr());
				access.setCreateTime(new Timestamp(dsVO.getActionTime().getTime()));
				access.setCreateBy(dsVO.getActionBy());
				access.setUpdateTime(access.getCreateTime());
				access.setUpdateBy(dsVO.getCreateBy());
				provisionLogDAO.insertEntity(access);

			} else {

			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return uuid;
	}
}
