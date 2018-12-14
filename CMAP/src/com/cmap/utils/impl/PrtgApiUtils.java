package com.cmap.utils.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.User;
import com.cmap.prtg.PrtgDocument;
import com.cmap.prtg.PrtgDocument.Prtg.Sensortree.Nodes.Group.Probenode.Group2;
import com.cmap.prtg.PrtgDocument.Prtg.Sensortree.Nodes.Group.Probenode.Group2.Device;
import com.cmap.security.SecurityUtil;
import com.cmap.utils.ApiUtils;

public class PrtgApiUtils implements ApiUtils {
	private static Logger log = LoggerFactory.getLogger(PrtgApiUtils.class);

	private String PRTG_ROOT = null;
	private String API_LOGIN = null;
	private String API_SENSOR_TREE = null;

	public PrtgApiUtils() {
		PRTG_ROOT = Env.PRTG_SERVER_IP;
		API_LOGIN = Env.PRTG_API_LOGIN;
		API_SENSOR_TREE = Env.PRTG_API_SENSOR_TREE;
	}

	@Override
	public Map[] getGroupAndDeviceMenu(HttpServletRequest request) throws Exception {
		Map[] retObj = null;
		/*
		 * groupDeviceMap >> MAP<Group_ID, Map<Device_ID, Map<Device詳細內容key, 詳細內容value>>>
		 * deviceMap >> Map<Device_ID, Map<Device詳細內容key, 詳細內容value>>
		 * deviceInfoMap >> Map<Device詳細內容key, 詳細內容value>
		 * groupInfoMap >> Map<Group_ID, Group_Name>
		 */
		Map<String, Map<String, Map<String, String>>> groupDeviceMap = new HashMap<>();
		Map<String, Map<String, String>> deviceMap = null;
		Map<String, String> deviceInfoMap = null;
		Map<String, String> groupInfoMap = null;
		try {
			checkPasshash(request);

			API_SENSOR_TREE = StringUtils.replace(API_SENSOR_TREE, "{username}", SecurityUtil.getSecurityUser().getUser().getPrtgLoginAccount());
			API_SENSOR_TREE = StringUtils.replace(API_SENSOR_TREE, "{passhash}", SecurityUtil.getSecurityUser().getUser().getPasshash());

			String apiUrl = PRTG_ROOT.concat(API_SENSOR_TREE);

			String retVal = callPrtg(apiUrl);
			if (StringUtils.isNotBlank(retVal)) {

				PrtgDocument prtgDoc = PrtgDocument.Factory.parse(retVal);
				Group2[] groups = null;

				try {
					groups = prtgDoc.getPrtg().getSensortree().getNodes().getGroup().getProbenode().getGroupArray();

				} catch (Exception e) {
					throw new ServiceLayerException("取得PRTG API >> groupArray異常 :: "+e.toString());
				}

				groupInfoMap = new HashMap<>();
				List<String> groupLabelList = new ArrayList<>();
				List<String> groupValueList = new ArrayList<>();
				for (Group2 group : groups) {
					if (!Env.PRTG_EXCLUDE_GROUP_NAME.contains(group.getName())) {

						String groupId = String.valueOf(group.getId());	//群組ID
						String groupName = group.getName();	//群組名稱

						groupInfoMap.put(groupId, groupName);

						groupLabelList.add(groupName);
						groupValueList.add(groupId);

						Device[] devices = group.getDeviceArray();

						for (Device device : devices) {
							deviceInfoMap = composeDeviceInfoMap(group, device);	//組成裝置資訊MAP

							if (groupDeviceMap.containsKey(groupId)) {
								groupDeviceMap.get(groupId).put(String.valueOf(device.getId()), deviceInfoMap);

							} else {
								deviceMap = new HashMap<>();
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

	private Map<String, String> composeDeviceInfoMap(Group2 group, Device device) {
		Map<String, String> deviceInfoMap = null;
		try {
			deviceInfoMap = new HashMap<>();
			deviceInfoMap.put(Constants.GROUP_ID, String.valueOf(group.getId()));
			deviceInfoMap.put(Constants.GROUP_NAME, getName(group.getName(), Env.PRTG_WRAPPED_SYMBOL_FOR_GROUP_NAME));
			deviceInfoMap.put(Constants.GROUP_ENG_NAME, getName(group.getName(), Env.PRTG_WRAPPED_SYMBOL_FOR_GROUP_ENG_NAME));
			deviceInfoMap.put(Constants.DEVICE_ID, String.valueOf(device.getId()));
			deviceInfoMap.put(Constants.DEVICE_NAME, getName(device.getName(), Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_NAME));
			deviceInfoMap.put(Constants.DEVICE_ENG_NAME, getName(device.getName(), Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_ENG_NAME));
			deviceInfoMap.put(Constants.DEVICE_IP, device.getHost());
			deviceInfoMap.put(Constants.DEVICE_SYSTEM, device.getTags());

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return deviceInfoMap;
	}

	@Override
	public boolean login(HttpServletRequest request, String username, String password) throws Exception {
		try {
			request.getSession().setAttribute(Constants.ERROR, null);

			if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				throw new Exception("[Login failed] >> username or password is blank.");

			} else {
				API_LOGIN = StringUtils.replace(API_LOGIN, "{username}", username);
				API_LOGIN = StringUtils.replace(API_LOGIN, "{password}", password);

				String apiUrl = PRTG_ROOT.concat(API_LOGIN);

				String retVal = null;
				try {
					retVal = callPrtg(apiUrl);

				} catch (ConnectTimeoutException cte) {
					request.getSession().setAttribute(Constants.ERROR, new ConnectTimeoutException("ERROR.connectionTimeOut"));
				}

				if (StringUtils.isNotBlank(retVal)) {
					//PRTG驗證成功後將密碼hash_code存入Spring security USER物件內，供後續作業使用
					//					SecurityUtil.getSecurityUser().getUser().setPasshash(retVal);
					request.getSession().setAttribute(Constants.PRTG_LOGIN_ACCOUNT, username);
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

	private String getName(String oriName, final String WRAPPED_SYMBOL) throws Exception {
		String retVal = "";

		try {
			/*
			 * 原格式: 192.168.1.3 (1樓大廳) {1F_Lobby} [Cisco Device Cisco IOS]
			 * >>>>> ip_address (裝置中文名稱) {裝置英文名稱} [裝置作業系統]
			 * >>>>> 分析取得(裝置中文名稱)
			 */
			if (StringUtils.isNotBlank(oriName)) {
				if (StringUtils.isNotBlank(WRAPPED_SYMBOL)) {
					if (WRAPPED_SYMBOL.length() == 2) {
						final String head = WRAPPED_SYMBOL.substring(0, 1);
						final String tail = WRAPPED_SYMBOL.substring(1, 2);

						if (oriName.indexOf(head) != -1) {
							oriName = oriName.substring(oriName.indexOf(head)+1, oriName.length());
						}
						if (oriName.indexOf(tail) != -1) {
							oriName = oriName.substring(0, oriName.indexOf(tail));
						}

					} else if (WRAPPED_SYMBOL.length() == 1) {
						if (oriName.indexOf(WRAPPED_SYMBOL) != -1) {
							oriName = oriName.substring(0, oriName.indexOf(WRAPPED_SYMBOL)).trim();
						}
					}
				}
			}

			retVal = oriName;

		} catch (Exception e) {
			throw e;
		}

		return retVal;
	}

	/*
	private String getGroupEngName(String oriGroupName, boolean includeSymbol) throws Exception {
		String retVal = "";

		try {
			/*
	 * 原格式: 192.168.1.3 (1樓大廳) {1F_Lobby} [Cisco Device Cisco IOS]
	 * >>>>> ip_address (裝置中文名稱) {裝置英文名稱} [裝置作業系統]
	 * >>>>> 分析取得{裝置英文名稱}
			if (StringUtils.isNotBlank(oriGroupName)) {
				if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_GROUP_ENG_NAME)) {
					final String head = Env.PRTG_WRAPPED_SYMBOL_FOR_GROUP_ENG_NAME.substring(0, 1);
					final String tail = Env.PRTG_WRAPPED_SYMBOL_FOR_GROUP_ENG_NAME.substring(1, 2);

					retVal = oriGroupName.substring(
							oriGroupName.indexOf(head)+1, oriGroupName.indexOf(tail));

					if (includeSymbol) {
						retVal = head.concat(retVal).concat(tail);
					}

				} else {
					if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_GROUP_NAME)) {
						oriGroupName = StringUtils.replace(oriGroupName, getGroupName(oriGroupName, true), "");
					}

					retVal = oriGroupName;
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return retVal;
	}

	private String getDeviceName(String oriDeviceName, boolean includeSymbol) throws Exception {
		String retVal = "";

		try {
			/*
	 * 原格式: 192.168.1.3 (1樓大廳) {1F_Lobby} [Cisco Device Cisco IOS]
	 * >>>>> ip_address (裝置中文名稱) {裝置英文名稱} [裝置作業系統]
	 * >>>>> 分析取得(裝置中文名稱)
			if (StringUtils.isNotBlank(oriDeviceName)) {
				if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_NAME)) {
					final String head = Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_NAME.substring(0, 1);
					final String tail = Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_NAME.substring(1, 2);

					retVal = oriDeviceName.substring(
							oriDeviceName.indexOf(head)+1, oriDeviceName.indexOf(tail));

					if (includeSymbol) {
						retVal = head.concat(retVal).concat(tail);
					}

				} else {
					if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_ENG_NAME)) {
						oriDeviceName = StringUtils.replace(oriDeviceName, getDeviceEngName(oriDeviceName, true), "");
					}
					if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_SYSTEM_VERSION)) {
						oriDeviceName = StringUtils.replace(oriDeviceName, getDeviceSystem(oriDeviceName, true), "");
					}

					retVal = oriDeviceName;
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return retVal;
	}

	private String getDeviceEngName(String oriDeviceName, boolean includeSymbol) throws Exception {
		String retVal = "";

		try {
			/*
	 * 原格式: 192.168.1.3 (1樓大廳) {1F_Lobby} [Cisco Device Cisco IOS]
	 * >>>>> ip_address (裝置中文名稱) {裝置英文名稱} [裝置作業系統]
	 * >>>>> 分析取得{裝置英文名稱}
			if (StringUtils.isNotBlank(oriDeviceName)) {
				if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_ENG_NAME)) {
					final String head = Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_ENG_NAME.substring(0, 1);
					final String tail = Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_ENG_NAME.substring(1, 2);

					retVal = oriDeviceName.substring(
							oriDeviceName.indexOf(head)+1, oriDeviceName.indexOf(tail));

					if (includeSymbol) {
						retVal = head.concat(retVal).concat(tail);
					}

				} else {
					if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_NAME)) {
						oriDeviceName = StringUtils.replace(oriDeviceName, getDeviceName(oriDeviceName, true), "");
					}
					if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_SYSTEM_VERSION)) {
						oriDeviceName = StringUtils.replace(oriDeviceName, getDeviceSystem(oriDeviceName, true), "");
					}

					retVal = oriDeviceName;
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return retVal;
	}

	private String getDeviceSystem(String oriDeviceName, boolean includeSymbol) throws Exception {
		String retVal = "";

		try {
			/*
	 * 原格式: 192.168.1.3 (1樓大廳) {1F_Lobby} [Cisco Device Cisco IOS]
	 * >>>>> ip_address (裝置中文名稱) {裝置英文名稱} [裝置作業系統]
	 * >>>>> 分析取得[裝置作業系統]
			if (StringUtils.isNotBlank(oriDeviceName)) {
				if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_SYSTEM_VERSION)) {
					final String head = Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_SYSTEM_VERSION.substring(0, 1);
					final String tail = Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_SYSTEM_VERSION.substring(1, 2);

					retVal = oriDeviceName.substring(
							oriDeviceName.indexOf(head)+1, oriDeviceName.indexOf(tail));

					if (includeSymbol) {
						retVal = head.concat(retVal).concat(tail);
					}

				} else {
					if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_NAME)) {
						oriDeviceName = StringUtils.replace(oriDeviceName, getDeviceName(oriDeviceName, true), "");
					}
					if (StringUtils.isNotBlank(Env.PRTG_WRAPPED_SYMBOL_FOR_DEVICE_ENG_NAME)) {
						oriDeviceName = StringUtils.replace(oriDeviceName, getDeviceEngName(oriDeviceName, true), "");
					}

					retVal = oriDeviceName;
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return retVal;
	}
	 */

	private String callPrtg(String apiUrl) throws Exception {
		String resultStr = "";

		try {
			CloseableHttpClient httpclient = CloseableHttpClientUtils.prepare();

			HttpGet httpGet = new HttpGet(apiUrl);

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(Env.HTTP_CONNECTION_TIME_OUT)				//設置連接逾時時間，單位毫秒。
					.setConnectionRequestTimeout(Env.HTTP_CONNECTION_TIME_OUT)	//設置從connect Manager獲取Connection 超時時間，單位毫秒。這個屬性是新加的屬性，因為目前版本是可以共用連接池的。
					.setSocketTimeout(Env.HTTP_SOCKET_TIME_OUT)					//請求獲取資料的超時時間，單位毫秒。 如果訪問一個介面，多少時間內無法返回資料，就直接放棄此次調用。
					.build();
			httpGet.setConfig(requestConfig);

			log.info("Executing request " + httpGet.getRequestLine());

			// Create a custom response handler
			ResponseHandler<String> responseHandler = response -> {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode >= 200 && statusCode < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + statusCode);
				}
			};
			resultStr = httpclient.execute(httpGet, responseHandler);

		} catch (ConnectTimeoutException cte) {
			log.error(cte.toString(), cte);
			throw cte;

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return resultStr;
	}

	/*
	 * Under "commons-httpclient" version: 3.1
	private String callPrtg(String apiUrl) throws Exception {
		GetMethod get = null;
		HttpClient client = null;
		int statusCode = 0;
		String resultStr = Integer.toString(statusCode);

		try {
			Protocol.registerProtocol("https", new Protocol("https", new TrustAllSSLSocketFactory(), 443));
            client = new HttpClient();
//            client.getHttpConnectionManager().getParams().setConnectionTimeout(Env.HTTP_CONNECTION_TIME_OUT);
            client.getHttpConnectionManager().getParams().setConnectionTimeout(1000);

            log.info("apiUrl: "+apiUrl);
        	get = new GetMethod(apiUrl);
			get.addRequestHeader("Content-Type", "text/html; charset=UTF-8");
//			get.getParams().setSoTimeout(Env.HTTP_SOCKET_TIME_OUT);
			get.getParams().setSoTimeout(1000);

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
	 */
}
