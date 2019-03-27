package com.cmap.controller;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.cmap.AppResponse;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.model.User;
import com.cmap.security.SecurityUser;
import com.cmap.service.CommonService;
import com.cmap.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


@Controller
@RequestMapping("/base")
public class BaseController {
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	SimpleDateFormat sdfYearMonth = new SimpleDateFormat("yyyyMM");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Autowired
	protected CommonService commonService;

	@Autowired
	private UserService userService;

	public BaseController() {
		super();
		sdfYearMonth.setTimeZone(TimeZone.getTimeZone("GMT"));
		sdfDate.setTimeZone(TimeZone.getTimeZone("GMT"));
		sdfDateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	protected Object transJSON2Object(String jsonStr, Class<?> mClass) {
		ObjectMapper oMapper = new ObjectMapper();
		try {
			return oMapper.readValue(jsonStr, mClass);

		} catch (Exception e) {
			return null;
		}
	}

	protected String manualAuthenticatd4EduOIDC(Model model, Principal principal, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();

			String userRole = Objects.toString(session.getAttribute(Constants.USERROLE), "");
			String[] userRoles = StringUtils.split(userRole, Env.COMM_SEPARATE_SYMBOL);
			final String[] USER_ROLES = userRoles;

			final String USER_ACCOUNT = Objects.toString(session.getAttribute(Constants.OIDC_SUB), "");
			final String USER_NAME = Objects.toString(session.getAttribute(Constants.OIDC_USER_NAME), "");
			final String USER_UNIT = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), "");
			final String USER_EMAIL = Objects.toString(session.getAttribute(Constants.OIDC_EMAIL), "");
			final String USER_IP = Objects.toString(session.getAttribute(Constants.IP_ADDR), "unknow");
			final String PRTG_ACCOUNT = Objects.toString(session.getAttribute(Constants.PRTG_LOGIN_ACCOUNT), "");
			final String PRTG_PASSWORD = Objects.toString(session.getAttribute(Constants.PRTG_LOGIN_PASSWORD), "");
			final String PRTG_PASSHASH = Objects.toString(session.getAttribute(Constants.PASSHASH), "");
			final String OIDC_SUB = Objects.toString(session.getAttribute(Constants.OIDC_SUB), "");
			final String OIDC_PASSWORD = Objects.toString(session.getAttribute(Constants.OIDC_SUB), "");
			final String OIDC_SCHOOL_ID = Objects.toString(session.getAttribute(Constants.OIDC_SCHOOL_ID), "");

			List<SimpleGrantedAuthority> authorities = new ArrayList<>();

			for (String ROLE : USER_ROLES) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + ROLE));
			}

			User user = new User(
								USER_ACCOUNT,
								USER_NAME,
								USER_UNIT,
								USER_EMAIL,
								PRTG_ACCOUNT,
								PRTG_PASSWORD,
								OIDC_SUB,
								OIDC_PASSWORD,
								PRTG_PASSHASH,
								USER_IP,
								OIDC_SCHOOL_ID,
								USER_ROLES);
			SecurityUser securityUser = new SecurityUser(user, USER_NAME, OIDC_PASSWORD, true, true, true, true, authorities);

	        Authentication authentication =  new UsernamePasswordAuthenticationToken(securityUser, USER_NAME, authorities);
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        return "redirect:" + Env.HOME_PAGE;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return "redirect:/login";
		}
	}

	protected boolean checkUserCanOrNotAccess(HttpServletRequest request, String belongGroup, String[] roles, String account) {
		return userService.checkUserCanAccess(request, belongGroup, roles, account);
	}

	protected void setQueryGroupList(HttpServletRequest request, Object obj, String fieldName, String queryGroup) throws Exception {
		if (StringUtils.isBlank(queryGroup)) {
			//如果未選擇特定群組，則須依照使用者權限，給予可查詢的群組清單
			if (request.getSession() != null) {
				Map<String, Map<String, Map<String, String>>> groupDeviceMap =
						(Map<String, Map<String, Map<String, String>>>)request.getSession().getAttribute(Constants.GROUP_DEVICE_MAP);

				List<String> groupList = new ArrayList<>();
				for (Iterator<String> it = groupDeviceMap.keySet().iterator(); it.hasNext();) {
					groupList.add(it.next());
				}

				new PropertyDescriptor(fieldName, obj.getClass()).getWriteMethod().invoke(obj, groupList);
			}

		} else {
			new PropertyDescriptor(fieldName, obj.getClass()).getWriteMethod().invoke(obj, queryGroup);
		}
	}

	protected void setQueryDeviceList(HttpServletRequest request, Object obj, String fieldName, String queryGroup, String queryDevice) throws Exception {
		if (StringUtils.isBlank(queryDevice)) {
			//如果未選擇特定群組，則須依照使用者權限，給予可查詢的群組清單
			if (request.getSession() != null) {
				Map<String, Map<String, Map<String, String>>> groupDeviceMap =
						(Map<String, Map<String, Map<String, String>>>)request.getSession().getAttribute(Constants.GROUP_DEVICE_MAP);

				List<String> deviceList = new ArrayList<>();

				if (StringUtils.isBlank(queryGroup)) {
					//如果群組選擇ALL，則取出使用者有權限內的群組底下所有設備(Device)清單
					for (Entry<String, Map<String, Map<String, String>>> groupEntry : groupDeviceMap.entrySet()) {
						for (Entry<String, Map<String, String>> deviceEntry : groupEntry.getValue().entrySet()) {
							deviceList.add(deviceEntry.getKey());
						}
					}

				} else {
					//如果群組有選擇項目，則取出該群組底下使用者有權限的設備(Device)清單
					for (Entry<String, Map<String, String>> deviceEntry : groupDeviceMap.get(queryGroup).entrySet()) {
						deviceList.add(deviceEntry.getKey());
					}
				}

				new PropertyDescriptor(fieldName, obj.getClass()).getWriteMethod().invoke(obj, deviceList);
			}

		} else {
			new PropertyDescriptor(fieldName, obj.getClass()).getWriteMethod().invoke(obj, queryDevice);
		}
	}

	protected Map<String, String> getMenuItem(String menuCode, boolean combineOrderDotLabel) {
		Map<String, String> itemMap = null;
		try {
			itemMap = commonService.getMenuItem(menuCode, combineOrderDotLabel);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return itemMap;
	}

	protected Map<String, String> getScriptTypeList(String defaultFlag) {
		Map<String, String> typeMap = null;
		try {
			typeMap = commonService.getScriptTypeMenu(defaultFlag);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return typeMap;
	}

	protected Map<String, String> getGroupList(HttpServletRequest request) {
		Map<String, String> retMap = new LinkedHashMap<>();
		Map<String, String> groupMap = null;
		try {
			groupMap = commonService.getGroupAndDeviceMenu(request);

			if (groupMap == null) {
				groupMap = new HashMap<>();
			}

			/*
			 * 排序設定處理
			 */
			if (Env.SORT_GROUP_MENU_BY_GROUP_NAME_INCLUDED_SEQ_NO) {
				Map<Integer, String> sortedMap = new TreeMap<>();
				Map<String, String> sortedNonNumberMap = new TreeMap<>();
				for (Map.Entry<String, String> entry : groupMap.entrySet()) {
					final String sourceMapKey = entry.getKey();
					final String sourceMapValue = entry.getValue();

					String splitSymbolWithoutRegex = Env.GROUP_NAME_SPLIT_SEQ_NO_SYMBOL.replace("\\", "");
					if (sourceMapValue.indexOf(splitSymbolWithoutRegex) != -1) {
						Integer groupSeq =
								Integer.parseInt(sourceMapValue.split(Env.GROUP_NAME_SPLIT_SEQ_NO_SYMBOL)[Env.GROUP_NAME_SPLITTED_SEQ_NO_INDEX]);
						sortedMap.put(groupSeq, sourceMapKey);

					} else {
						sortedNonNumberMap.put(sourceMapKey, sourceMapKey);
					}
				}

				for (String sourceKey : sortedMap.values()) {
					retMap.put(sourceKey, groupMap.get(sourceKey));
				}
				for (String sourceKey : sortedNonNumberMap.values()) {
					retMap.put(sourceKey, groupMap.get(sourceKey));
				}

			} else {
				for (Map.Entry<String, String> entry : groupMap.entrySet()) {
					retMap.put(entry.getKey(), entry.getValue());
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return retMap;
	}

	public Map<String, String> getGroupDeviceMenu(HttpServletRequest request, String searchTxt, String systemVersion) {
		//Map<String, String> reverseMenuMap = new LinkedHashMap<String, String>();
		Map<String, String> menuMap = new LinkedHashMap<>();

		Map<String, Map<String, Map<String, String>>> groupDeviceMap
			= (Map<String, Map<String, Map<String, String>>>)request.getSession().getAttribute(Constants.GROUP_DEVICE_MAP);

		if (groupDeviceMap != null && !groupDeviceMap.isEmpty()) {
			for (Iterator<String> it = groupDeviceMap.keySet().iterator(); it.hasNext();) {
				final String groupKey = it.next();
				Map<String, Map<String, String>> groupMap = groupDeviceMap.get(groupKey);

				String groupName;
				int idx = 0;
				for (Map<String, String> deviceMap : groupMap.values()) {
					groupName = deviceMap.get(Constants.GROUP_NAME);

					final String deviceId = deviceMap.get(Constants.DEVICE_ID);
					final String deviceName = deviceMap.get(Constants.DEVICE_ENG_NAME);
					final String deviceSystemVersion = deviceMap.get(Constants.DEVICE_SYSTEM);

					if (StringUtils.isBlank(searchTxt) && StringUtils.isBlank(systemVersion)) {
						if (idx == 0) {
							menuMap.put("GROUP_"+groupKey, groupName);
						}
						menuMap.put("DEVICE_"+deviceId, deviceName);
						idx++;

					} else {
						if (!StringUtils.equalsIgnoreCase(systemVersion, Constants.DATA_STAR_SYMBOL)) {
							if (!StringUtils.containsIgnoreCase(deviceSystemVersion, systemVersion)) {
								continue;
							}
						}
						if (StringUtils.isBlank(searchTxt)
							|| (StringUtils.isNotBlank(searchTxt) && StringUtils.containsIgnoreCase(groupName, searchTxt))
							|| (StringUtils.isNotBlank(searchTxt) && StringUtils.containsIgnoreCase(deviceName, searchTxt))) {
								if (idx == 0) {
									menuMap.put("GROUP_"+groupKey, groupName);
								}

								menuMap.put("DEVICE_"+deviceId, deviceName);
								idx++;
						}
					}
				}
			}
		}

		/*
		if (menuMap != null && !menuMap.isEmpty()) {

			ListIterator<Entry<String, String>> it = new ArrayList<>(menuMap.entrySet()).listIterator(menuMap.entrySet().size());

		    while (it.hasPrevious())
		    {
		    	Entry<String, String> el = it.previous();
		    	reverseMenuMap.put(el.getKey(), el.getValue());
		    }
		}
		*/

		return menuMap;
	}

	@RequestMapping(value = "getGroupDeviceMenu.json", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse getGroupDeviceMenu(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="searchTxt", required=true) String searchTxt,
			@RequestParam(name="systemVersion", required=true) String systemVersion) {

		try {
			Map<String, String> menuMap = getGroupDeviceMenu(request, searchTxt, systemVersion);

			AppResponse appResponse = new AppResponse(HttpServletResponse.SC_OK, "取得設備清單成功");
			appResponse.putData("groupDeviceMenu",  new Gson().toJson(menuMap));

			return appResponse;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(value = "getDeviceMenu", method = RequestMethod.POST, produces="application/json;odata=verbose")
	public @ResponseBody AppResponse getDeviceMenu(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="groupId", required=true) String groupId) {

		Map<String, String> deviceMap;
		try {
			AppResponse appResponse;
			if (StringUtils.isBlank(groupId)) {
				appResponse = new AppResponse(HttpServletResponse.SC_NO_CONTENT, "群組未選擇，設備保持為空");
				return appResponse;
			}

			Map<String, Map<String, Map<String, String>>> groupDeviceMap
				= (Map<String, Map<String, Map<String, String>>>)request.getSession().getAttribute(Constants.GROUP_DEVICE_MAP);
			/*
			if (StringUtils.isBlank(groupId)) {
				//如果群組選擇ALL，則列出所有設備清單
				retMap = new HashMap<String, String>();

				for (Map<String, String> dMap : deviceMap.values()) {
					for (Entry<String, String> entry : dMap.entrySet()) {
						if (!retMap.containsKey(entry.getKey())) {
							//不同群組可能涵蓋相同設備，排除重複
							retMap.put(entry.getKey(), entry.getValue());
						}
					}
				}

			} else {
				retMap = (Map<String, String>)deviceMap.get(groupId);
			}
			 */

			if (groupDeviceMap != null && !groupDeviceMap.isEmpty()) {
				deviceMap = composeDeviceMap4Menu(groupDeviceMap.get(groupId));

				if (deviceMap != null && !deviceMap.isEmpty()) {
					appResponse = new AppResponse(HttpServletResponse.SC_OK, "取得設備清單成功");
					appResponse.putData("device",  new Gson().toJson(deviceMap));

				} else {
					appResponse = new AppResponse(HttpServletResponse.SC_NOT_FOUND, "無法取得設備清單");
				}

			} else {
				appResponse = new AppResponse(HttpServletResponse.SC_NOT_FOUND, "無法取得設備清單");
			}

			return appResponse;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return new AppResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
	}

	private Map<String, String> composeDeviceMap4Menu(Map<String, Map<String, String>> deviceMap) {
		Map<String, String> deviceMenuMap = new HashMap<>();
		for (String deviceId : deviceMap.keySet()) {
			deviceMenuMap.put(
					deviceId											//DEVICE_ID
					,deviceMap.get(deviceId).get(Constants.DEVICE_NAME) //DEVICE_NAME
					);
		}

		return deviceMenuMap;
	}

	protected Date validateDate(String date) {
		Date d = null;
		if (null == date) {
			return d;
		}
		try {
			d = sdfDate.parse(date);
		} catch (ParseException e) {
			return d;
		}
		return d;
	}

	protected Date validateDateTime(String date) {
		Date d = null;
		if (null == date) {
			return d;
		}
		try {
			d = sdfDateTime.parse(date);
		} catch (ParseException e) {
			return d;
		}
		return d;
	}

	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	//基于Cookie的国际化处理
	protected void changeLang(HttpServletRequest request, HttpServletResponse response, Model model, String langType) {
		if (!model.containsAttribute("contentModel")) {
			if (langType.contains("zh")) {
				Locale locale = new Locale("zh", "CN");
				(new CookieLocaleResolver()).setLocale(request, response, locale);
			} else if (langType.equals("en_US")) {
				Locale locale = new Locale("en", "US");
				(new CookieLocaleResolver()).setLocale(request, response, locale);
			} else {
				(new CookieLocaleResolver()).setLocale(request, response, LocaleContextHolder.getLocale());
			}
		}
	}

	protected void convertJson2POJO(Object pojo, final JsonNode jsonData) {

		Iterator<String> it = jsonData.fieldNames();

		while (it.hasNext()) {
			try {
				final String fieldName = it.next();
				Class<?> fieldNameType = pojo.getClass().getDeclaredField(fieldName).getType();

				final JsonNode fieldNode = jsonData.findValue(fieldName);

				if (fieldNameType.isAssignableFrom(String.class)) {
					PropertyUtils.setProperty(pojo, fieldName, fieldNode.asText());

				} else if (fieldNameType.isAssignableFrom(Integer.class)) {
					PropertyUtils.setProperty(pojo, fieldName, Integer.parseInt(fieldNode.asText()));

				} else if (fieldNameType.isAssignableFrom(List.class)) {
					String[] nodeValues = null;

					if (fieldName.equals("inputSysCheckSql")) {
						if (fieldNode.asText().indexOf("\r\n") != -1) {
							String[] tmp = fieldNode.asText().split("\r\n");

							List<String> sqlList = new ArrayList<>();
							StringBuffer sb = new StringBuffer();
							for (String str : tmp) {
								sb.append(str).append("\r\n");

								if (str.equals(";") || (!str.equals(";") && str.contains(";"))) {
									sqlList.add(sb.toString());
									sb.setLength(0);
								}
							}

							nodeValues = new String[sqlList.size()];
							for (int i=0; i<sqlList.size(); i++) {
								nodeValues[i] = sqlList.get(i);
							}

						} else {
							nodeValues = new String[] {fieldNode.asText()};
						}

					} else {
						if (fieldNode.asText().indexOf("\r\n") != -1) {
							nodeValues = fieldNode.asText().split("\r\n");
						} else {
							nodeValues = new String[] {fieldNode.asText()};
						}
					}

					List<String> list = new ArrayList<>();

					if (nodeValues != null) {
						for (String value : nodeValues) {
							list.add(value);
						}
					}

					PropertyUtils.setProperty(pojo, fieldName, list);
				}

			} catch (Exception e) {
				log.error(e.toString(), e);
			}
		}
	}

	protected String getIp(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip != null) {
			if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
				int index = ip.indexOf(",");
				if (index != -1) {
					return ip.substring(0, index);
				} else {
					return ip;
				}
			}
		}

		ip = request.getHeader("X-Real-IP");
		if (ip != null) {
			if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}

		ip = request.getHeader("Proxy-Client-IP");
		if (ip != null) {
			if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}

		ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip != null) {
			if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}

		ip = request.getRemoteAddr();
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	protected String getMac(String ipAddr) {
		String mac = "";
        try {
        	Process p = Runtime.getRuntime().exec("arp -n");
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
			p.waitFor();

            boolean flag = true;
			String ipStr = "(" + ipAddr + ")";
            while(flag) {
                String str = input.readLine();
                if (str != null) {
					if (str.indexOf(ipStr) > 1) {
                        int temp = str.indexOf("at");
                        mac = str.substring(
                        temp + 3, temp + 20);
                        break;
                    }
                } else {
					flag = false;
				}
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(System.out);
        }
        return mac;
    }
}