package com.cmap.utils.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.configuration.TrustAllSSLSocketFactory;
import com.cmap.exception.AuthenticateException;
import com.cmap.model.User;
import com.cmap.prtg.PrtgDocument;
import com.cmap.prtg.PrtgDocument.Prtg.Sensortree.Nodes.Group.Probenode.Group2;
import com.cmap.prtg.PrtgDocument.Prtg.Sensortree.Nodes.Group.Probenode.Group2.Device;
import com.cmap.security.SecurityUtil;
import com.cmap.utils.ApiUtils;


public class PrtgApiUtils implements ApiUtils {
	private static Log log = LogFactory.getLog(PrtgApiUtils.class);
	private String PRTG_ROOT = null;
	private String API_LOGIN = null;
	private String API_SENSOR_TREE = null;
	
	public PrtgApiUtils() {
		PRTG_ROOT = Env.PRTG_SERVER_IP;
		API_LOGIN = Env.PRTG_API_LOGIN;
		API_SENSOR_TREE = Env.PRTG_API_SENSOR_TREE;
	}
	
	public Map[] getGroupAndDeviceMenu(HttpServletRequest request) throws Exception {
		Map[] retObj = null;
		Map<String, Map<String, Map<String, String>>> groupDeviceMap = new HashMap<String, Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> deviceMap = null;
		Map<String, String> deviceInfoMap = null;
		Map<String, String> groupInfoMap = null;
		try {
			checkPasshash(request);
			
			API_SENSOR_TREE = StringUtils.replace(API_SENSOR_TREE, "{username}", SecurityUtil.getSecurityUser().getUser().getUserName());
			API_SENSOR_TREE = StringUtils.replace(API_SENSOR_TREE, "{passhash}", SecurityUtil.getSecurityUser().getUser().getPasshash());
			
			String apiUrl = PRTG_ROOT.concat(API_SENSOR_TREE);
			
			String retVal = callPrtg(apiUrl);
			if (StringUtils.isNotBlank(retVal)) {
				
				PrtgDocument prtgDoc = PrtgDocument.Factory.parse(retVal);
				Group2[] groups = prtgDoc.getPrtg().getSensortree().getNodes().getGroup().getProbenode().getGroupArray();
				
				groupInfoMap = new HashMap<String, String>();
				List<String> groupLabelList = new ArrayList<String>();
				List<String> groupValueList = new ArrayList<String>();
				for (Group2 group : groups) {
					if (!group.getName().equals("Network Discovery")) {
						
						String groupId = String.valueOf(group.getId());	//群組ID
						String groupName = group.getName();	//群組名稱
						
						groupInfoMap.put(groupId, groupName);
						
						groupLabelList.add(groupName);
						groupValueList.add(groupId);
						
						Device[] devices = group.getDeviceArray();
						
						for (Device device : devices) {
							deviceInfoMap = composeDeviceInfoMap(device);	//組成裝置資訊MAP
							
							if (groupDeviceMap.containsKey(groupId)) {
								groupDeviceMap.get(groupId).put(String.valueOf(device.getId()), deviceInfoMap);
								
							} else {
								deviceMap = new HashMap<String, Map<String, String>>();
								deviceMap.put(String.valueOf(device.getId()), deviceInfoMap);
								groupDeviceMap.put(groupId, deviceMap);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			throw e;
			
		} finally {
			retObj = new Map[] {
			    groupInfoMap
			   ,groupDeviceMap
			};
		}
		
		return retObj;
	}
	
	private Map<String, String> composeDeviceInfoMap(Device device) {
		Map<String, String> deviceInfoMap = null;
		try {
			deviceInfoMap = new HashMap<String, String>();
			deviceInfoMap.put(Constants.DEVICE_ID, String.valueOf(device.getId()));
			deviceInfoMap.put(Constants.DEVICE_NAME, getDeviceName(device.getName()));
			deviceInfoMap.put(Constants.DEVICE_IP, device.getHost());
			deviceInfoMap.put(Constants.DEVICE_SYSTEM, getDeviceSystem(device.getName()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return deviceInfoMap;
	}
	
	public boolean login(HttpServletRequest request, String username, String password) throws Exception {
		try {
			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				throw new Exception("[Login failed] >> username or password is blank.");
				
			} else {
				API_LOGIN = StringUtils.replace(API_LOGIN, "{username}", username);
				API_LOGIN = StringUtils.replace(API_LOGIN, "{password}", password);
				
				String apiUrl = PRTG_ROOT.concat(API_LOGIN);
				
				String retVal = callPrtg(apiUrl);
				if (StringUtils.isNotBlank(retVal)) {
					//PRTG驗證成功後將密碼hash_code存入Spring security USER物件內，供後續作業使用
//					SecurityUtil.getSecurityUser().getUser().setPasshash(retVal);
					request.getSession().setAttribute(Constants.PASSHASH, retVal);
					return true;
				}
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return false;
	}
	
	private void checkPasshash(HttpServletRequest request) throws Exception {
		User user = SecurityUtil.getSecurityUser().getUser();
		String username = user.getUserName();
		String password = user.getPassword();
		
		if (StringUtils.isBlank(user.getPasshash())) {
			login(request, username, password);
		}
	}
	
	private String getDeviceName(String oriDeviceName) throws Exception {
		String retVal = "";
		
		try {
			/*
			 * 原格式: 192.168.1.3 (R3) [Cisco Device Cisco IOS]
			 * >>>>> ip_address (裝置名稱) [裝置作業系統]
			 * >>>>> 分析取得(裝置名稱)
			 */
			if (StringUtils.isNotBlank(oriDeviceName)) {
				retVal = oriDeviceName.substring(
						oriDeviceName.indexOf("(")+1, oriDeviceName.indexOf(")"));
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return retVal;
	}
	
	private String getDeviceSystem(String oriDeviceName) throws Exception {
		String retVal = "";
		
		try {
			/*
			 * 原格式: 192.168.1.3 (R3) [Cisco Device Cisco IOS]
			 * >>>>> ip_address (裝置名稱) [裝置作業系統]
			 * >>>>> 分析取得[裝置作業系統]
			 */
			if (StringUtils.isNotBlank(oriDeviceName)) {
				retVal = oriDeviceName.substring(
						oriDeviceName.indexOf("[")+1, oriDeviceName.indexOf("]"));
			}
			
		} catch (Exception e) {
			throw e;
		}
		
		return retVal;
	}
	
	private String callPrtg(String apiUrl) throws Exception {
		GetMethod get = null;
		HttpClient client = null;
		int statusCode = 0;
		String resultStr = Integer.toString(statusCode);

		try {
			Protocol.registerProtocol("https", new Protocol("https", new TrustAllSSLSocketFactory(), 443));
            client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(Env.HTTP_CONNECTION_TIME_OUT);
            
        	get = new GetMethod(apiUrl);
			get.addRequestHeader("Content-Type", "text/html; charset=UTF-8");
			get.getParams().setSoTimeout(Env.HTTP_SOCKET_TIME_OUT);
			
			statusCode = client.executeMethod(get);
			if (statusCode != HttpStatus.SC_OK) {
				if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
					throw new AuthenticateException("PRTG authentication failed !!");
				}

			} else {
				// 取得回傳內容
				resultStr = get.getResponseBodyAsString();
			}
			
		} catch (Exception e) {
			throw e;

		} finally {
			if (get != null) {
				// release the connection back to the connection pool
				get.releaseConnection();
			} 
		}
		
		return resultStr;
	}
}
