package com.cmap.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cmap.model.DeviceList;
import com.cmap.model.DeviceLoginInfo;
import com.cmap.service.vo.CommonServiceVO;
import com.cmap.service.vo.PrtgServiceVO;

public interface CommonService {

    public String convertByteSizeUnit(BigDecimal sizeByte, Integer targetUnit);

    /**
     * 取得 GROUP 及 DEVICE 選單
     * @param request
     * @return
     */
	public Map<String, String> getGroupAndDeviceMenu(HttpServletRequest request);

	/**
	 * 更新 Device_List 資料
	 * @param deviceList
	 */
	public void updateDeviceList(List<DeviceList> deviceList);

	/**
	 * 取得選單資料 (Menu_Item)
	 * @param menuCode
	 * @param combineOrderDotLabel 設定選單序號跟選單呈顯值中間是否用點串聯
	 * @return
	 */
	public Map<String, String> getMenuItem(String menuCode, boolean combineOrderDotLabel);

	/**
	 * 取得腳本分類選單
	 * @param defaultFlag
	 * @return
	 */
	public Map<String, String> getScriptTypeMenu(String defaultFlag);

	/**
	 * 取得 PRTG 登入帳密資訊 (Prtg_Account_Mapping)
	 * @param sourceId
	 * @return
	 */
	public PrtgServiceVO findPrtgLoginInfo(String sourceId);

	/**
	 * 取得 IP protocol 規格表
	 * @return
	 */
	public Map<Integer, CommonServiceVO> getProtoclSpecMap();

	/**
	 * 寄送MAIL
	 * @param toAddress
	 * @param ccAddress
	 * @param bccAddress
	 * @param subject
	 * @param mailContent
	 * @param filePathList
	 * @throws Exception
	 */
	public void sendMail(String[] toAddress, String[] ccAddress, String[] bccAddress,
	        String subject, String mailContent, ArrayList<String> filePathList) throws Exception;

	/**
	 * 取得當前登入者名稱
	 * @return
	 */
	public String getUserName();

	/**
	 * 取得學校群組(GROUP)的IP網段設定
	 * @param groupId
	 * @param ipVersion
	 * @return
	 */
	public String getGroupSubnetSetting(String groupId, String ipVersion);

	/**
	 * 判斷IP是否在網段內
	 * @param gSubnet
	 * @param ipAddress
	 * @return
	 */
	public boolean chkIpInGroupSubnet(String cidr, String ip, String ipVersion);
	
	/**
	 * 遞迴查找DEVICE_LOGIN_INFO資料
	 * @param deviceListId
	 * @param groupId
	 * @param deviceId
	 * @return
	 */
	public DeviceLoginInfo findDeviceLoginInfo(String deviceListId, String groupId, String deviceId);
}
