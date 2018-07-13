package com.cmap.utils.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmap.model.User;
import com.cmap.prtg.PrtgDocument;
import com.cmap.prtg.PrtgDocument.Prtg.Sensortree.Nodes.Group.Probenode.Group2;
import com.cmap.prtg.PrtgDocument.Prtg.Sensortree.Nodes.Group.Probenode.Group2.Device;
import com.cmap.security.SecurityUtil;
import com.cmap.utils.ApiUtils;


public class PrtgApiUtils implements ApiUtils {
	private static Log log = LogFactory.getLog(PrtgApiUtils.class);
	private static final String PRTG_ROOT = "https://192.168.26.150/";
	private String API_LOGIN = "api/getpasshash.htm?username={username}&password={password}";
	private String API_SENSOR_TREE = "api/table.xml?content=sensortree&output=xml&username={username}&passhash={passhash}";
	
	public Object[] getGroupAndDeviceMenu() throws Exception {
		Object[] retObj = null;
		Object[] groupLabelArray = null;
		Object[] groupValueArray = null;
		Map<String, Map<String, String>> groupDeviceMap = new HashMap<String, Map<String, String>>();
		try {
			checkPasshash();
			
			API_SENSOR_TREE = StringUtils.replace(API_SENSOR_TREE, "{username}", SecurityUtil.getSecurityUser().getUser().getUserName());
			API_SENSOR_TREE = StringUtils.replace(API_SENSOR_TREE, "{passhash}", SecurityUtil.getSecurityUser().getUser().getPasshash());
			System.out.println("API_SENSOR_TREE: "+API_SENSOR_TREE);
			
			String apiUrl = PRTG_ROOT.concat(API_SENSOR_TREE);
			System.out.println("API_URL: "+apiUrl);
			
			String retVal = callPrtg(apiUrl);
			if (StringUtils.isNotBlank(retVal)) {
				System.out.println("Get sensor tree success !!");
				
				PrtgDocument prtgDoc = PrtgDocument.Factory.parse(retVal);
				Group2[] groups = prtgDoc.getPrtg().getSensortree().getNodes().getGroup().getProbenode().getGroupArray();
				
				List<String> groupLabelList = new ArrayList<String>();
				List<String> groupValueList = new ArrayList<String>();
				for (Group2 group : groups) {
					if (!group.getName().equals("Network Discovery")) {
						System.out.println("===== [ "+group.getId()+", "+group.getName()+" ] =====");
						groupLabelList.add(group.getName());
						groupValueList.add(String.valueOf(group.getId()));
						
						Device[] devices = group.getDeviceArray();
						String groupId = String.valueOf(group.getId());
						
						for (Device device : devices) {
							System.out.println(">>>>> "+device.getId()+", "+getDeviceName(device.getName()));
							
							if (groupDeviceMap.containsKey(groupId)) {
								groupDeviceMap.get(groupId).put(String.valueOf(device.getId()), getDeviceName(device.getName()));
								
							} else {
								Map<String, String> deviceMap = new HashMap<String, String>();
								deviceMap.put(String.valueOf(device.getId()), getDeviceName(device.getName()));
								groupDeviceMap.put(groupId, deviceMap);
							}
						}
					}
				}
				
				groupLabelArray = groupLabelList.toArray();
				groupValueArray = groupValueList.toArray();
			}

			for (Object o:groupLabelArray) {
				System.out.println("groupLabelArray: "+o);
			}
			for (Object o:groupValueArray) {
				System.out.println("groupValueArray: "+o);
			}
			System.out.println("groupDeviceMap: "+groupDeviceMap);
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			retObj = new Object[] {
				groupLabelArray
			   ,groupValueArray
			   ,groupDeviceMap
			};
		}
		
		return retObj;
	}
	
	private void login(String username, String password) throws Exception {
		try {
			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				throw new Exception("[Login failed] >> username or password is blank.");
				
			} else {
				API_LOGIN = StringUtils.replace(API_LOGIN, "{username}", username);
				API_LOGIN = StringUtils.replace(API_LOGIN, "{password}", password);
				System.out.println("API_LOGIN: "+API_LOGIN);
				
				String apiUrl = PRTG_ROOT.concat(API_LOGIN);
				System.out.println("API_URL: "+apiUrl);
				
				String retVal = callPrtg(apiUrl);
				if (StringUtils.isNotBlank(retVal)) {
					System.out.println("Login authentication success !!");
					
					//PRTG驗證成功後將密碼hash_code存入Spring security USER物件內，供後續作業使用
					SecurityUtil.getSecurityUser().getUser().setPasshash(retVal);
					
				}
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void checkPasshash() throws Exception {
		User user = SecurityUtil.getSecurityUser().getUser();
		String username = user.getUserName();
		String password = user.getPassword();
		
		if (StringUtils.isBlank(user.getPasshash())) {
			login(username, password);
		}
	}
	
	private String getDeviceName(String oriDeviceName) throws Exception {
		String retVal = "";
		
		try {
			/*
			 * 原格式: 192.168.1.3 (R3) [Cisco Device Cisco IOS]
			 * >>>>> [ip_address] (裝置名稱) [裝置作業系統]
			 * >>>>> 分析取得中間的(裝置名稱)
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
	
	private String callPrtg(String apiUrl) throws Exception {
		GetMethod get = null;
		HttpClient client = null;
		int statusCode = 0;
		String resultStr = Integer.toString(statusCode);

		try {
            client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
            
        	get = new GetMethod(apiUrl);
			get.addRequestHeader("Content-Type", "text/html; charset=UTF-8");
			get.getParams().setSoTimeout(SOCKET_TIMEOUT);
			
			statusCode = client.executeMethod(get);
			if (statusCode != HttpStatus.SC_OK) {
				if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
					throw new Exception("PRTG authentication failed !!");
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
