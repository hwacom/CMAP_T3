package com.cmap.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.MenuItemDAO;
import com.cmap.exception.AuthenticateException;
import com.cmap.model.DeviceList;
import com.cmap.model.MenuItem;
import com.cmap.service.CommonService;
import com.cmap.utils.ApiUtils;
import com.cmap.utils.impl.PrtgApiUtils;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
	@Log
	private static Logger log;

	@Autowired
	DeviceListDAO deviceListDAO;
	
	@Autowired
	MenuItemDAO menuItemDAO;
	
	/**
	 * 呼叫PRTG API取得當前使用者權限下所有群組&設備清單
	 */
	@Override
	public Map<String, String> getGroupAndDeviceMenu(HttpServletRequest request) {
		Map<String, String> retMap = null;
		ApiUtils prtgApi = null;
		try {
			prtgApi = new PrtgApiUtils();
			Map[] prtgMap = prtgApi.getGroupAndDeviceMenu(request);
			
			if (prtgMap != null) {
				
				final Map<String, String> groupInfoMap = (Map<String, String>)prtgMap[0];
				retMap = groupInfoMap;
				
				if (prtgMap[1] != null && !((Map<String, Map<String, Map<String, String>>>)prtgMap[1]).isEmpty()) {
					if (request.getSession() != null) {
						request.getSession().setAttribute(Constants.GROUP_DEVICE_MAP, prtgMap[1]);
						
						List<DeviceList> deviceList = new ArrayList<DeviceList>();
						
						DeviceList dl = null;
						Map<String, Map<String, Map<String, String>>> groupDeviceMap = (HashMap<String, Map<String, Map<String, String>>>)prtgMap[1];
						for (String groupId : groupDeviceMap.keySet()) {
							Map<String, Map<String, String>> deviceMap = groupDeviceMap.get(groupId);
							
							for (String deviceId : deviceMap.keySet()) {
								
								Map<String, String> deviceInfoMap = deviceMap.get(deviceId);
								
								// 先撈取查看此群組+設備ID資料是否已存在
								dl = deviceListDAO.findDeviceListByGroupAndDeviceId(groupId, deviceId);
								
								boolean noNeedToAddOrModify = true;
								if (dl == null) {
									// 不存在表示後面要新增
									noNeedToAddOrModify = false;
									
									dl = new DeviceList();
									dl.setGroupId(groupId);
									dl.setDeviceId(deviceId);
									
									String dirPath = "/"
													 .concat(StringUtils.replace(Env.DEFAULT_FTP_DIR_GROUP_NAME, "[gid]", groupId))
													 .concat("/")
													 .concat(StringUtils.replace(Env.DEFAULT_FTP_DIR_DEVICE_NAME, "[did]", deviceId));
									
									dl.setConfigFileDirPath(dirPath);
									dl.setDeleteFlag(Constants.DATA_MARK_NOT_DELETE);
									dl.setCreateBy(Constants.SYS);
									dl.setCreateTime(new Timestamp((new Date()).getTime()));

								} else {
									// 若已存在，確認以下欄位是否有異動，若其中一項有異動的話則後面要進行更新
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify = 
												(StringUtils.isBlank(dl.getGroupName()) ? "" : dl.getGroupName()).equals(groupInfoMap.get(groupId));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify = 
												(StringUtils.isBlank(dl.getDeviceName()) ? "" : dl.getDeviceName()).equals(deviceInfoMap.get(Constants.DEVICE_NAME));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify = 
												(StringUtils.isBlank(dl.getDeviceIp()) ? "" : dl.getDeviceIp()).equals(deviceInfoMap.get(Constants.DEVICE_IP));
									}
									if (noNeedToAddOrModify) {
										noNeedToAddOrModify = 
												(StringUtils.isBlank(dl.getSystemVersion()) ? "" : dl.getSystemVersion()).equals(deviceInfoMap.get(Constants.DEVICE_SYSTEM));
									}
								}
								
								if (!noNeedToAddOrModify) {
									dl.setGroupName(groupInfoMap.get(groupId));
									dl.setDeviceName(deviceInfoMap.get(Constants.DEVICE_NAME));
									dl.setDeviceIp(deviceInfoMap.get(Constants.DEVICE_IP));
									dl.setSystemVersion(deviceInfoMap.get(Constants.DEVICE_SYSTEM));
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
			System.out.println(ae.toString());
			
		} catch (Exception e) {
			log.error(e.toString(), e);
			e.printStackTrace();
		}
		return retMap;
	}

	@Override
	public void updateDeviceList(List<DeviceList> deviceList) {
		// 更新 or 寫入 DEVICE_LIST 資料
		deviceListDAO.saveOrUpdateDeviceListByModel(deviceList);
	}

	@Override
	public Map<String, String> getMenuItem(String menuCode, boolean combineOrderDotLabel) {
		Map<String, String> retMap = new LinkedHashMap<String, String>();
		
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
}
