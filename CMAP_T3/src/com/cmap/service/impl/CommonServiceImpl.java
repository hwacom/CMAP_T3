package com.cmap.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DeviceDAO;
import com.cmap.dao.MenuItemDAO;
import com.cmap.dao.ProtocolDAO;
import com.cmap.dao.PrtgDAO;
import com.cmap.dao.ScriptTypeDAO;
import com.cmap.exception.AuthenticateException;
import com.cmap.model.DeviceList;
import com.cmap.model.MenuItem;
import com.cmap.model.ProtocolSpec;
import com.cmap.model.PrtgAccountMapping;
import com.cmap.model.ScriptType;
import com.cmap.security.SecurityUtil;
import com.cmap.service.CommonService;
import com.cmap.service.vo.CommonServiceVO;
import com.cmap.service.vo.PrtgServiceVO;
import com.cmap.utils.ApiUtils;
import com.cmap.utils.impl.PrtgApiUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
	@Log
	private static Logger log;

	@Autowired
	DeviceDAO deviceDAO;

	@Autowired
	MenuItemDAO menuItemDAO;

	@Autowired
	ScriptTypeDAO scriptTypeDAO;

	@Autowired
	PrtgDAO prtgDAO;

	@Autowired
	ProtocolDAO protocolDAO;

	/**
	 * 組合 Local / Remote 落地檔路徑資料夾
	 * @param deviceInfoMap
	 * @param local (true=Local;false=Remote)
	 * @return
	 */
	private String composeFilePath(Map<String, String> deviceInfoMap, boolean local) {
		String dirPath = Env.FTP_DIR_SEPARATE_SYMBOL;

		String groupDirName = local ? Env.DEFAULT_LOCAL_DIR_GROUP_NAME : Env.DEFAULT_REMOTE_DIR_GROUP_NAME;
		if (groupDirName.indexOf(Constants.DIR_PATH_GROUP_ID) != -1) {
			groupDirName = StringUtils.replace(groupDirName, Constants.DIR_PATH_GROUP_ID, deviceInfoMap.get(Constants.GROUP_ID));
		}
		if (groupDirName.indexOf(Constants.DIR_PATH_GROUP_NAME) != -1) {
			groupDirName = StringUtils.replace(groupDirName, Constants.DIR_PATH_GROUP_NAME, deviceInfoMap.get(Constants.GROUP_ENG_NAME));
		}

		String deviceDirName = local ? Env.DEFAULT_LOCAL_DIR_DEVICE_NAME : Env.DEFAULT_REMOTE_DIR_DEVICE_NAME;
		if (deviceDirName.indexOf(Constants.DIR_PATH_DEVICE_ID) != -1) {
			deviceDirName = StringUtils.replace(deviceDirName, Constants.DIR_PATH_DEVICE_ID, deviceInfoMap.get(Constants.DEVICE_ID));
		}
		if (deviceDirName.indexOf(Constants.DIR_PATH_DEVICE_NAME) != -1) {
			deviceDirName = StringUtils.replace(deviceDirName, Constants.DIR_PATH_DEVICE_NAME, deviceInfoMap.get(Constants.DEVICE_ENG_NAME));
		}
		if (deviceDirName.indexOf(Constants.DIR_PATH_DEVICE_IP) != -1) {
			deviceDirName = StringUtils.replace(deviceDirName, Constants.DIR_PATH_DEVICE_IP, deviceInfoMap.get(Constants.DEVICE_IP));
		}
		if (deviceDirName.indexOf(Constants.DIR_PATH_DEVICE_SYSTEM) != -1) {
			deviceDirName = StringUtils.replace(deviceDirName, Constants.DIR_PATH_DEVICE_SYSTEM, deviceInfoMap.get(Constants.DEVICE_SYSTEM));
		}

		dirPath = dirPath.concat(StringUtils.isNotBlank(groupDirName) ? groupDirName : "")
				.concat(StringUtils.isNotBlank(groupDirName) ? Env.FTP_DIR_SEPARATE_SYMBOL : "")
				.concat(StringUtils.isNotBlank(deviceDirName) ? deviceDirName : "");

		return dirPath;
	}

	/**
	 * 呼叫PRTG API取得當前使用者權限下所有群組&設備清單
	 */
	@Override
	public Map<String, String> getGroupAndDeviceMenu(HttpServletRequest request) {
		Map<String, String> retMap = null;
		try {
			ApiUtils prtgApi = new PrtgApiUtils();
			Map[] prtgMap = prtgApi.getGroupAndDeviceMenu(request);

			if (prtgMap != null) {

				final Map<String, String> groupInfoMap = prtgMap[0];
				retMap = groupInfoMap;

				if (prtgMap[1] != null && !((Map<String, Map<String, Map<String, String>>>)prtgMap[1]).isEmpty()) {
					if (request.getSession() != null) {
						request.getSession().setAttribute(Constants.GROUP_DEVICE_MAP, prtgMap[1]);

						List<DeviceList> deviceList = new ArrayList<>();

						DeviceList dl = null;
						Map<String, Map<String, Map<String, String>>> groupDeviceMap = prtgMap[1];
						for (String groupId : groupDeviceMap.keySet()) {
							Map<String, Map<String, String>> deviceMap = groupDeviceMap.get(groupId);

							for (String deviceId : deviceMap.keySet()) {

								Map<String, String> deviceInfoMap = deviceMap.get(deviceId);

								// 先撈取查看此群組+設備ID資料是否已存在
								// Y181203, 改成只看設備ID, 否則若從PRTG內將設備移動到別的群組下會更新不到
								dl = deviceDAO.findDeviceListByGroupAndDeviceId(null, deviceId);

								final String localFileDirPath = composeFilePath(deviceInfoMap, true);
								final String remoteFileDirPath = composeFilePath(deviceInfoMap, false);
								boolean noNeedToAddOrModify = true;
								if (dl == null) {
									// 不存在表示後面要新增
									noNeedToAddOrModify = false;

									dl = new DeviceList();
									dl.setGroupId(groupId);
									dl.setDeviceId(deviceId);

									dl.setConfigFileDirPath(localFileDirPath);
									dl.setRemoteFileDirPath(remoteFileDirPath);

									dl.setDeleteFlag(Constants.DATA_MARK_NOT_DELETE);
									dl.setCreateBy(Constants.SYS);
									dl.setCreateTime(new Timestamp((new Date()).getTime()));

								} else {
									// 若已存在，確認以下欄位是否有異動，若其中一項有異動的話則後面要進行更新
									// Y181203, 增加判斷群組ID是否有異動
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getGroupId()) ? "" : dl.getGroupId()).equals(deviceInfoMap.get(Constants.GROUP_ID));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getGroupName()) ? "" : dl.getGroupName()).equals(deviceInfoMap.get(Constants.GROUP_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getGroupEngName()) ? "" : dl.getGroupEngName()).equals(deviceInfoMap.get(Constants.GROUP_ENG_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getDeviceName()) ? "" : dl.getDeviceName()).equals(deviceInfoMap.get(Constants.DEVICE_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getDeviceEngName()) ? "" : dl.getDeviceEngName()).equals(deviceInfoMap.get(Constants.DEVICE_ENG_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getDeviceIp()) ? "" : dl.getDeviceIp()).equals(deviceInfoMap.get(Constants.DEVICE_IP));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getSystemVersion()) ? "" : dl.getSystemVersion()).equals(deviceInfoMap.get(Constants.DEVICE_SYSTEM));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getConfigFileDirPath()) ? "" : dl.getConfigFileDirPath()).equals(localFileDirPath);
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify =
												(StringUtils.isBlank(dl.getRemoteFileDirPath()) ? "" : dl.getRemoteFileDirPath()).equals(remoteFileDirPath);
									}
								}

								if (!noNeedToAddOrModify) {
									dl.setGroupName(deviceInfoMap.get(Constants.GROUP_NAME));
									dl.setGroupEngName(deviceInfoMap.get(Constants.GROUP_ENG_NAME));
									dl.setDeviceName(deviceInfoMap.get(Constants.DEVICE_NAME));
									dl.setDeviceEngName(deviceInfoMap.get(Constants.DEVICE_ENG_NAME));
									dl.setDeviceIp(deviceInfoMap.get(Constants.DEVICE_IP));
									dl.setSystemVersion(deviceInfoMap.get(Constants.DEVICE_SYSTEM));
									dl.setConfigFileDirPath(localFileDirPath);
									dl.setRemoteFileDirPath(remoteFileDirPath);
									dl.setUpdateBy(Constants.SYS);
									dl.setUpdateTime(new Timestamp((new Date()).getTime()));

									deviceList.add(dl);
								}
							}
						}

						// 更新 or 寫入 DEVICE_LIST 資料
						if (deviceList != null && !deviceList.isEmpty()) {
							updateDeviceList(deviceList);
						}
					}
				}
			}

		} catch (AuthenticateException ae) {
			log.error(ae.toString());

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retMap;
	}

	@Override
	public void updateDeviceList(List<DeviceList> deviceList) {
		// 更新 or 寫入 DEVICE_LIST 資料
		deviceDAO.saveOrUpdateDeviceListByModel(deviceList);
	}

	@Override
	public Map<String, String> getMenuItem(String menuCode, boolean combineOrderDotLabel) {
		Map<String, String> retMap = new LinkedHashMap<>();

		try {
			List<MenuItem> itemList = menuItemDAO.findMenuItemByMenuCode(menuCode);

			for (MenuItem item : itemList) {
				retMap.put(
						item.getOptionValue(),
						combineOrderDotLabel ? String.valueOf(item.getOptionOrder()).concat(Env.MENU_ITEM_COMBINE_SYMBOL).concat(item.getOptionLabel())
								: item.getOptionLabel());
			}

		} catch (Exception e) {

		}

		return retMap;
	}

	@Override
	public Map<String, String> getScriptTypeMenu(String defaultFlag) {
		Map<String, String> retMap = new LinkedHashMap<>();

		try {
			List<ScriptType> scriptTypeList = scriptTypeDAO.findScriptTypeByDefaultFlag(defaultFlag);

			for (ScriptType type : scriptTypeList) {
				retMap.put(type.getScriptTypeCode(), type.getScriptTypeName());
			}

		} catch (Exception e) {

		}
		return retMap;
	}

	protected String currentUserName() {
		return SecurityUtil.getSecurityUser().getUsername();
	}

	protected Timestamp currentTimestamp() {
		return new Timestamp((new Date()).getTime());
	}

	protected Object transJSON2Object(String jsonStr, Class<?> mClass) {
		ObjectMapper oMapper = new ObjectMapper();
		try {
			return oMapper.readValue(jsonStr, mClass);

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public PrtgServiceVO findPrtgLoginInfo(String sourceId) {
		PrtgServiceVO retVO = null;
		try {
			PrtgAccountMapping mapping = prtgDAO.findPrtgAccountMappingBySourceId(sourceId);

			if (mapping != null) {
				retVO = new PrtgServiceVO();
				retVO.setAccount(mapping.getPrtgAccount());
				retVO.setPassword(mapping.getPrtgPassword());
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retVO;
	}

	public String base64Decoder(String base64String) {
		final Base64.Decoder decoder = Base64.getDecoder();
		try {
			return new String(decoder.decode(base64String), Constants.CHARSET_UTF8);

		} catch (UnsupportedEncodingException e) {
			log.error(e.toString(), e);
			return base64String;
		}
	}

	@Override
	public Map<Integer, CommonServiceVO> getProtoclSpecMap() {
		Map<Integer, CommonServiceVO> retMap = new HashMap<>();
		try {
			List<ProtocolSpec> reList = protocolDAO.findAllProtocolSpec();

			CommonServiceVO csVO;
			for (ProtocolSpec ps : reList) {
				csVO = new CommonServiceVO();
				BeanUtils.copyProperties(csVO, ps);

				retMap.put(ps.getProtocolNo(), csVO);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retMap;
	}
}
