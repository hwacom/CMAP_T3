package com.cmap.service;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

public interface CommonService {

	public static final String DEVICE_MENU_ATTR = "DEVICE_MENU";
	
	public static final SimpleDateFormat FORMAT_YYYYMMDD_HH24MI = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	
	public Object[] getGroupAndDeviceMenu(HttpServletRequest request);
}
