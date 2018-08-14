package com.cmap.controller;

import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.cmap.AppResponse;
import com.cmap.Constants;
import com.cmap.service.CommonService;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;


@Controller
@RequestMapping("/base")
public class BaseController {
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);
	
	SimpleDateFormat sdfYearMonth = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    @Autowired
	private CommonService commonService;
	
	public BaseController() {
		super();
		sdfYearMonth.setTimeZone(TimeZone.getTimeZone("GMT"));
		sdfDate.setTimeZone(TimeZone.getTimeZone("GMT"));
		sdfDateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	protected void setQueryGroupList(HttpServletRequest request, Object obj, String fieldName, String queryGroup) throws Exception {
		if (StringUtils.isBlank(queryGroup)) {
			//如果未選擇特定群組，則須依照使用者權限，給予可查詢的群組清單
			if (request.getSession() != null) {
				Map<String, Map<String, Map<String, String>>> groupDeviceMap = 
						(Map<String, Map<String, Map<String, String>>>)request.getSession().getAttribute(Constants.GROUP_DEVICE_MAP);
				
				List<String> groupList = new ArrayList<String>();
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
				
				List<String> deviceList = new ArrayList<String>();

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
			e.printStackTrace();
		}
		
		return itemMap;
	}
	
	protected Map<String, String> getGroupList(HttpServletRequest request) {
		Map<String, String> groupMap = null;
		try {
			groupMap = commonService.getGroupAndDeviceMenu(request);
			
			if (groupMap == null) {
				groupMap = new HashMap<String, String>();
			}
			
		} catch (Exception e) {
			log.error(e.toString(), e);
			e.printStackTrace();
		}
		
		return groupMap;
	}
	
	@RequestMapping(value = "getDeviceMenu", method = RequestMethod.POST)
	public @ResponseBody AppResponse getDeviceMenu(
			Model model, HttpServletRequest request, HttpServletResponse response,
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
			
			return new AppResponse(999, e.getMessage());
		}
	}
	
	private Map<String, String> composeDeviceMap4Menu(Map<String, Map<String, String>> deviceMap) {
		Map<String, String> deviceMenuMap = new HashMap<String, String>();
		try {
			for (String deviceId : deviceMap.keySet()) {
				deviceMenuMap.put(
							deviceId																  //DEVICE_ID
						   ,((Map<String, String>)deviceMap.get(deviceId)).get(Constants.DEVICE_NAME) //DEVICE_NAME
					   );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
			} else
				(new CookieLocaleResolver()).setLocale(request, response, LocaleContextHolder.getLocale());
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
					if (fieldNode.asText().indexOf("\r\n") != -1) {
						nodeValues = fieldNode.asText().split("\r\n");
					} else {
						nodeValues = new String[] {fieldNode.asText()};
					}
					
					List<String> list = new ArrayList<String>();
					
					if (nodeValues != null) {
						for (String value : nodeValues) {
							list.add(value);
						}
					}
					
					PropertyUtils.setProperty(pojo, fieldName, list);
				}
				
			} catch (Exception e) {
				log.error(e.toString(), e);
				e.printStackTrace();
			}
		}
	}
}