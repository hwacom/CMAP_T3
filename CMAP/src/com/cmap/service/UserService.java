package com.cmap.service;

import javax.servlet.http.HttpServletRequest;

import com.cmap.exception.ServiceLayerException;

public interface UserService {

	/**
	 * 檢核登入者帳號是否可使用系統 & 是否為管理者並加設管理者腳色
	 * @param request
	 * @param account
	 * @return
	 * @throws ServiceLayerException
	 */
	boolean checkUserCanAccess(HttpServletRequest request, String belongGroup, String account);

	/**
	 * 設定使用者可使用功能
	 * @param request
	 * @param account
	 * @return
	 * @throws ServiceLayerException
	 */
	boolean setUserFuncRight(HttpServletRequest request, String account) throws ServiceLayerException;
}
