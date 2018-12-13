package com.cmap.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cmap.model.DeviceList;
import com.cmap.service.vo.PrtgServiceVO;

public interface CommonService {

	public Map<String, String> getGroupAndDeviceMenu(HttpServletRequest request);

	public void updateDeviceList(List<DeviceList> deviceList);

	public Map<String, String> getMenuItem(String menuCode, boolean combineOrderDotLabel);

	public Map<String, String> getScriptTypeMenu(String defaultFlag);

	public PrtgServiceVO findPrtgLoginInfo(String sourceId);
}
