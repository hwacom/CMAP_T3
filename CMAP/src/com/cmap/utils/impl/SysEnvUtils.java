package com.cmap.utils.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.comm.ConnectionMode;
import com.cmap.dao.SysConfigSettingDAO;
import com.cmap.model.SysConfigSetting;
import com.cmap.utils.EnvUtils;

@Named("sysEnvUtils")
public class SysEnvUtils implements EnvUtils {
	private static Logger log = LoggerFactory.getLogger(SysEnvUtils.class);

	@Autowired
	private SysConfigSettingDAO sysConfigSettingDAO;
	
	private Map<String, List<String>> valueMap = null;
	
	@PostConstruct
	public void initEnv() throws Exception {
		List<SysConfigSetting> modelList; 
		try {
			modelList = sysConfigSettingDAO.findAllSysConfigSetting(null, null);
			
			valueMap = null;
			valueMap = new HashMap<String, List<String>>();
			
			List<String> tmpList = null;
			for (SysConfigSetting scs : modelList) {
				if (valueMap.containsKey(scs.getSettingName())) {
					tmpList = valueMap.get(scs.getSettingName());
					
					if (tmpList == null) {
						tmpList = new ArrayList<String>();
					}
				} else {
					tmpList = new ArrayList<String>();
				}
				
				tmpList.add(scs.getSettingValue());
				valueMap.put(scs.getSettingName(), tmpList);
			}
			
			mapping2Env(valueMap, true);
			
		} catch (Exception e) {
			log.error(e.toString(), e);
			e.printStackTrace();
		}
	}
	
	private void mapping2Env(Map<String, List<String>> sourceMap, boolean initInstance) {
		Map<String, Boolean> settingInitMap = new HashMap<String, Boolean>();
		
		Env env = new Env();
		for (Entry<String, List<String>> entry : sourceMap.entrySet()) {
			try {
				Class<?> dynamicClass = Env.class.getDeclaredField(entry.getKey()).getType();

				if (dynamicClass.isAssignableFrom(String.class)) {
					Env.class.getDeclaredField(entry.getKey()).set(env, entry.getValue().get(0));
					
				} else if (dynamicClass.isAssignableFrom(Integer.class)) {
					Env.class.getDeclaredField(entry.getKey()).set(env, entry.getValue().get(0) == null ? null : Integer.valueOf(entry.getValue().get(0)));
					
				} else if (dynamicClass.isAssignableFrom(ConnectionMode.class)) {
					ConnectionMode cMode = null;
					
					switch (entry.getValue().get(0)) {
						case Constants.FTP:
							cMode = ConnectionMode.FTP;
							break;
						
						case Constants.TFTP:
							cMode = ConnectionMode.TFTP;
							break;
					}
					
					Env.class.getDeclaredField(entry.getKey()).set(env, cMode);
					
				} else if (dynamicClass.isAssignableFrom(List.class)) {
					List list = (List)Env.class.getDeclaredField(entry.getKey()).get(null);
					
					if (initInstance) {
						if (!settingInitMap.containsKey(entry.getKey())) {
							list.removeAll(list);
							settingInitMap.put(entry.getKey(), true);
						}
					}
					
					for (String value : entry.getValue()) {
						list.add(value);
					}
					
				} else if (dynamicClass.isAssignableFrom(Map.class)) {
					Map map = (Map)Env.class.getDeclaredField(entry.getKey()).get(null);
					
					if (initInstance) {
						if (!settingInitMap.containsKey(entry.getKey())) {
							map.clear();
							settingInitMap.put(entry.getKey(), true);
						}
					}
					
					for (String value : entry.getValue()) {
						final String mapKey = value.split(Env.COMM_SEPARATE_SYMBOL)[0];
						final String mapValue = value.split(Env.COMM_SEPARATE_SYMBOL)[1];
						map.put(mapKey, mapValue);
					}
				}
				
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				//Mapping不到的不處理
				e.printStackTrace();
			}
		}
		
		//針對Base64編碼欄位進行解碼
		if (Env.DECODE_FIELDS != null && !Env.DECODE_FIELDS.isEmpty()) {
			final List<String> fields = Env.DECODE_FIELDS;
			
			List<String> refreshNames = new ArrayList<String>();
			refreshNames.addAll(sourceMap.keySet());
			
			boolean needRefreshDecodeFields = false;
			for (String rName : refreshNames) {
				if (fields.contains(rName)) {
					needRefreshDecodeFields = true;
					break;
				}
			}
			
			if (needRefreshDecodeFields) {
				final Base64.Decoder decoder = Base64.getDecoder();
				
				for (String fName : fields) {
					try {
						String fValue = Objects.toString(Env.class.getDeclaredField(fName).get(env));
						
						if (StringUtils.isNotBlank(fValue)) {
							Env.class.getDeclaredField(fName).set(env, new String(decoder.decode(fValue), Constants.CHARSET_UTF8));
						}
									
					} catch (UnsupportedEncodingException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		/*
		Env.DEFAULT_FTP_DIR_GROUP_NAME = valueMap.get("DEFAULT_FTP_DIR_GROUP_NAME");
		Env.DEFAULT_FTP_DIR_DEVICE_NAME = valueMap.get("DEFAULT_FTP_DIR_DEVICE_NAME");
		
		Env.FTP_HOST_IP = valueMap.get("FTP_HOST_IP");
		Env.FTP_HOST_PORT = toInt(Env.FTP_HOST_PORT, valueMap.get("FTP_HOST_PORT"));
		Env.FTP_LOGIN_ACCOUNT = valueMap.get("FTP_LOGIN_ACCOUNT");
		Env.FTP_LOGIN_PASSWORD = valueMap.get("FTP_LOGIN_PASSWORD");
		Env.FTP_BASE_DIR_PATH = valueMap.get("FTP_BASE_DIR_PATH");
		Env.FTP_DEFAULT_TIME_OUT = toInt(Env.FTP_DEFAULT_TIME_OUT, valueMap.get("FTP_DEFAULT_TIME_OUT"));
		Env.FTP_CONNECT_TIME_OUT = toInt(Env.FTP_CONNECT_TIME_OUT, valueMap.get("FTP_CONNECT_TIME_OUT"));
		*/
		
	}

	@Override
	public void refreshAll() throws Exception {
		initEnv();
	}

	@Override
	public void refreshByNames(List<String> settingNames) throws Exception {
		List<SysConfigSetting> modelList; 
		try {
			if (valueMap == null) {
				initEnv();
				
			} else {
				Map<String, List<String>> tmpRefreshMap = new HashMap<String, List<String>>();
				
				modelList = sysConfigSettingDAO.findSysConfigSettingByName(settingNames);
				
				List<String> tmpList = null;
				for (SysConfigSetting scs : modelList) {
					if (tmpRefreshMap.containsKey(scs.getSettingName())) {
						tmpList = tmpRefreshMap.get(scs.getSettingName());
						
						if (tmpList == null) {
							tmpList = new ArrayList<String>();
						}
						
					} else {
						tmpList = new ArrayList<String>();
					}
					
					//若是刪除設定(Delete_Flag = Y)，則將環境變數值刷新為「null」
					final String refreshVal = scs.getDeleteFlag().equals(Constants.DATA_MARK_DELETE) ? null : scs.getSettingValue();
					tmpList.add(refreshVal);
					tmpRefreshMap.put(scs.getSettingName(), tmpList);
				}
				
				mapping2Env(tmpRefreshMap, true);
			}
			
		} catch (Exception e) {
			log.error(e.toString(), e);
			e.printStackTrace();
		}
	}

	@Override
	public List<Env> findEnvs(String settingName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Env findEnv(String settingName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
