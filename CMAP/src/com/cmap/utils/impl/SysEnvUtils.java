package com.cmap.utils.impl;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.comm.ConnectionMode;
import com.cmap.dao.SysConfigSettingDAO;
import com.cmap.model.SysConfigSetting;
import com.cmap.utils.EnvUtils;

@Named("sysEnvUtils")
public class SysEnvUtils implements EnvUtils {
	private static Log log = LogFactory.getLog(SysEnvUtils.class);

	@Autowired
	private SysConfigSettingDAO sysConfigSettingDAO;
	
	private Map<String, String> valueMap = null;
	
	@PostConstruct
	public void initEnv() throws Exception {
		List<SysConfigSetting> modelList; 
		try {
			modelList = sysConfigSettingDAO.findAllSysConfigSetting();
			
			valueMap = null;
			valueMap = new HashMap<String, String>();
			for (SysConfigSetting scs : modelList) {
				valueMap.put(scs.getSettingName(), scs.getSettingValue());
			}
			
			mapping2Env();
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
	}
	
	private Integer toInt(Integer intVar, String value) {
		return value != null ? new Integer(value) : intVar;
	}
	
	private void mapping2Env() {
		
		Env env = new Env();
		for (Entry<String, String> entry : valueMap.entrySet()) {
			try {
				Class type = Env.class.getDeclaredField(entry.getKey()).getType();

				if (type.isAssignableFrom(String.class)) {
					Env.class.getDeclaredField(entry.getKey()).set(env, entry.getValue());
					
				} else if (type.isAssignableFrom(Integer.class)) {
					Env.class.getDeclaredField(entry.getKey()).setInt(env, Integer.valueOf(entry.getValue()));
					
				} else if (type.isAssignableFrom(ConnectionMode.class)) {
					ConnectionMode cMode = null;
					
					switch (entry.getValue()) {
						case Constants.FTP:
							cMode = ConnectionMode.FTP;
							break;
						
						case Constants.TFTP:
							cMode = ConnectionMode.TFTP;
							break;
					}
					
					Env.class.getDeclaredField(entry.getKey()).set(env, cMode);
				}
				
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				//Mapping不到的不處理
			}
		}
		
		//針對Base64編碼欄位進行解碼
		if (StringUtils.isNotBlank(Env.DECODE_FIELDS)) {
			String[] fields = Env.DECODE_FIELDS.split(Env.COMM_SEPARATE_SYMBOL);
			
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
				modelList = sysConfigSettingDAO.findSysConfigSettingByName(settingNames);
				
				for (SysConfigSetting scs : modelList) {
					if (valueMap.containsKey(scs.getSettingName())) {
						valueMap.put(scs.getSettingName(), scs.getSettingValue());
					}
				}
				
				mapping2Env();
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
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
