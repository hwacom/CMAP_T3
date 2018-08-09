package com.cmap.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ApiUtils extends Api {
	
	public boolean login(HttpServletRequest request, String username, String password) throws Exception;
	
	public Map[] getGroupAndDeviceMenu(HttpServletRequest request) throws Exception;
}
