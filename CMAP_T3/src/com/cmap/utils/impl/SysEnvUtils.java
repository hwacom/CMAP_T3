package com.cmap.utils.impl;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.comm.enums.ConnectionMode;
import com.cmap.dao.SysConfigSettingDAO;
import com.cmap.model.SysConfigSetting;
import com.cmap.utils.EnvUtils;

@Named("sysEnvUtils")
public class SysEnvUtils implements EnvUtils {
	@Log
	private static Logger log;

	@Autowired
	private SysConfigSettingDAO sysConfigSettingDAO;

	private Map<String, List<String>> valueMap = null;

	/**
	 * 初始化環境變數(Env)
	 */
	@Override
	@PostConstruct
	public void initEnv() throws Exception {
		List<SysConfigSetting> modelList;
		try {
			modelList = sysConfigSettingDAO.findAllSysConfigSetting(null, null);

			valueMap = null;
			valueMap = new HashMap<>();

			List<String> tmpList = null;
			for (SysConfigSetting scs : modelList) {
				if (valueMap.containsKey(scs.getSettingName())) {
					tmpList = valueMap.get(scs.getSettingName());

					if (tmpList == null) {
						tmpList = new ArrayList<>();
					}
				} else {
					tmpList = new ArrayList<>();
				}

				tmpList.add(scs.getSettingValue());
				valueMap.put(scs.getSettingName(), tmpList);
			}

			mapping2Env(valueMap, true);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
	}

	/**
	 * 將DB內設定值，動態依照Env參數型態將值塞入
	 * @param sourceMap
	 * @param initInstance
	 */
	private void mapping2Env(Map<String, List<String>> sourceMap, boolean initInstance) {
		Map<String, Boolean> settingInitMap = new HashMap<>();

		Env env = new Env();
		for (Entry<String, List<String>> entry : sourceMap.entrySet()) {
			try {
				Class<?> dynamicClass = Env.class.getDeclaredField(entry.getKey()).getType();

				if (dynamicClass.isAssignableFrom(String.class)) {
					Env.class.getDeclaredField(entry.getKey()).set(env, entry.getValue().get(0));

				} else if (dynamicClass.isAssignableFrom(Integer.class)) {
					Env.class.getDeclaredField(entry.getKey()).set(env, entry.getValue().get(0) == null ? null : Integer.valueOf(entry.getValue().get(0)));

				} else if (dynamicClass.isAssignableFrom(Boolean.class)) {
					Boolean trueOrFalse = StringUtils.equalsIgnoreCase(entry.getValue().get(0), "TRUE") ? Boolean.TRUE : Boolean.FALSE;
					Env.class.getDeclaredField(entry.getKey()).set(env, trueOrFalse);

				} else if (dynamicClass.isAssignableFrom(ConnectionMode.class)) {
					ConnectionMode cMode = null;

					switch (entry.getValue().get(0)) {
						case Constants.FTP:
							cMode = ConnectionMode.FTP;
							break;

						case Constants.TFTP:
							cMode = ConnectionMode.TFTP;
							break;

						case Constants.TELNET:
							cMode = ConnectionMode.TELNET;
							break;

						case Constants.SSH:
							cMode = ConnectionMode.SSH;
							break;

						case Constants.WMI:
							cMode = ConnectionMode.WMI;
							break;

						case Constants.SNMP:
							cMode = ConnectionMode.SNMP;
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
						if (value != null) {
							list.add(value);
						}
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

				} else if (dynamicClass.isAssignableFrom(SimpleDateFormat.class)) {
					final String format = entry.getValue().get(0);

					SimpleDateFormat sdf = (SimpleDateFormat)Env.class.getDeclaredField(entry.getKey()).get(null);
					sdf.applyPattern(format);
				}

			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				//Mapping不到的不處理
				log.error(e.toString(), e);
			}
		}

		//針對Base64編碼欄位進行解碼
		if (Env.DECODE_FIELDS != null && !Env.DECODE_FIELDS.isEmpty()) {
			final List<String> fields = Env.DECODE_FIELDS;

			List<String> refreshNames = new ArrayList<>();
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
						log.error(e.toString(), e);
					}
				}
			}
		}
	}

	/**
	 * 刷新Env所有參數值，與DB同步
	 */
	@Override
	public void refreshAll() throws Exception {
		initEnv();
	}

	/**
	 * 刷新指定的Env參數值，與DB同步
	 */
	@Override
	public void refreshByNames(List<String> settingNames) throws Exception {
		List<SysConfigSetting> modelList;
		try {
			if (valueMap == null) {
				initEnv();

			} else {
				Map<String, List<String>> tmpRefreshMap = new HashMap<>();

				modelList = sysConfigSettingDAO.findSysConfigSettingByName(settingNames);

				List<String> tmpList = null;
				for (SysConfigSetting scs : modelList) {
					if (tmpRefreshMap.containsKey(scs.getSettingName())) {
						tmpList = tmpRefreshMap.get(scs.getSettingName());

						if (tmpList == null) {
							tmpList = new ArrayList<>();
						}

					} else {
						tmpList = new ArrayList<>();
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
		}
	}

	/**
	 * 查找指定的Env參數值
	 */
	@Override
	public List<Env> findEnvs(String settingName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查找指定的Env參數值
	 */
	@Override
	public Env findEnv(String settingName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
