package com.cmap.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cmap.model.DeviceList;

public interface CommonService {

	public Map<String, String> getGroupAndDeviceMenu(HttpServletRequest request);
	
	public void updateDeviceList(List<DeviceList> deviceList);
	
	public Map<String, String> getConfigTypeMenu();
}
