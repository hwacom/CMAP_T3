package com.cmap.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.SysConfigSettingDAO;
import com.cmap.dao.vo.SysConfigSettingDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.SysConfigSetting;
import com.cmap.security.SecurityUtil;
import com.cmap.service.EnvService;
import com.cmap.service.vo.EnvServiceVO;
import com.cmap.utils.impl.CommonUtils;
import com.cmap.utils.impl.SysEnvUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;

@Service("envService")
@Transactional
public class EnvServiceImpl implements EnvService {
	@Log
	private static Logger log;

	@Autowired
	SysConfigSettingDAO sysConfigSettingDAO;

	@Autowired
	SysEnvUtils sysEnvUtils;

	private SysConfigSettingDAOVO transServiceVO2DAOVO(EnvServiceVO esVO) {
		SysConfigSettingDAOVO daovo = new SysConfigSettingDAOVO();
		if (esVO != null) {
			daovo.setSettingName(esVO.getInputSettingName());
			daovo.setSettingValue(esVO.getInputSettingValue());
			daovo.setSettingRemark(esVO.getInputSettingRemark());
			daovo.setSearchValue(esVO.getSearchValue());
			daovo.setOrderColumn(esVO.getOrderColumn());
			daovo.setOrderDirection(esVO.getOrderDirection());
		}
		return daovo;
	}

	@Override
	public long countEnvSettingsByVO(EnvServiceVO esVO) throws ServiceLayerException {
		long retVal = 0;

		try {
			retVal = sysConfigSettingDAO.countSysConfigSettingByVO(transServiceVO2DAOVO(esVO));

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e);
		}
		return retVal;
	}

	@Override
	public List<EnvServiceVO> findEnvSettingsByVO(EnvServiceVO esVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		List<EnvServiceVO> retList = new ArrayList<>();

		//查找DB設定內容
		List<SysConfigSetting> entityList =
				sysConfigSettingDAO.findSysConfigSettingByVO(transServiceVO2DAOVO(esVO), startRow, pageLength);

		int diffCount = 0;
		EnvServiceVO retVO;
		for (SysConfigSetting entity : entityList) {
			try {
				//轉換頁面呈顯用VO
				retVO = new EnvServiceVO();
				BeanUtils.copyProperties(entity, retVO);
				retVO.setCreateTimeStr(Constants.FORMAT_YYYYMMDD_HH24MI.format(new Date(entity.getCreateTime().getTime())));
				retVO.setUpdateTimeStr(Constants.FORMAT_YYYYMMDD_HH24MI.format(new Date(entity.getUpdateTime().getTime())));

				//判斷當前參數值是否為需編碼欄位
				boolean hasEncode = false;
				List<String> encodeFields = Env.DECODE_FIELDS;
				if (encodeFields != null && !encodeFields.isEmpty()) {
					if (encodeFields.contains(retVO.getSettingName())) {
						hasEncode = true;
					}
				}

				//判斷DB設定內容是否與當前系統環境變數(Env)所套用中的值相同
				boolean isSame = false;
				String envVal = "";

				try {
					Class<?> type = Env.class.getDeclaredField(retVO.getSettingName()).getType();
					if (type.isAssignableFrom(List.class)) {
						List<?> envList =
								Env.class.getDeclaredField(retVO.getSettingName()).get(null) != null
								? (List<?>)Env.class.getDeclaredField(retVO.getSettingName()).get(null)
										: null;

								if (envList != null) {
									for (Object obj : envList) {
										envVal = hasEncode ? Base64.encode(obj.toString().getBytes()) : obj.toString();
										isSame = retVO.getSettingValue().equals(envVal) ? true : false;

										if (isSame) {
											break;
										}
									}
								}

					} else if (type.isAssignableFrom(HashMap.class)) {
						HashMap<?,?> envMap =
								Env.class.getDeclaredField(retVO.getSettingName()).get(null) != null
								? (HashMap<?,?>)Env.class.getDeclaredField(retVO.getSettingName()).get(null)
										: null;

								final String dbMapKey = retVO.getSettingValue().split(Env.COMM_SEPARATE_SYMBOL)[0];
								final String dbMapValue = retVO.getSettingValue().split(Env.COMM_SEPARATE_SYMBOL)[1];

								if (envMap != null && envMap.containsKey(dbMapKey)) {
									envVal = hasEncode ? Base64.encode(((String)envMap.get(dbMapKey)).getBytes()) : (String)envMap.get(dbMapKey);
									isSame = dbMapValue.equals(envVal) ? true : false;
								}

								if (isSame) {
									envVal = dbMapKey.concat(Env.COMM_SEPARATE_SYMBOL)
											.concat(hasEncode ? Base64.encode(((String)envMap.get(dbMapKey)).getBytes())
													: (String)envMap.get(dbMapKey));
								}

					} else {
						String currentEnvVal =
								Env.class.getDeclaredField(retVO.getSettingName()).get(null) != null
								? Env.class.getDeclaredField(retVO.getSettingName()).get(null).toString()
										: "";
								envVal = hasEncode ? Base64.encode(currentEnvVal.getBytes()) : currentEnvVal;
								isSame = retVO.getSettingValue().equals(envVal) ? true : false;
					}

					if (!isSame) {
						envVal = "";
						diffCount++;
					}

				} catch (NoSuchFieldException nsfe) {
					//若DB的參數名稱不存在於Env宣告的變數名稱內
					envVal = "***** 不存在 *****";
					diffCount++;
				}

				retVO.set_settingValue(envVal);
				retVO.setSame(isSame);

				retList.add(retVO);

			} catch (Exception e) {
				log.error(e.toString(), e);
			}
		}

		retList.get(0).setDifferCount(diffCount);

		return retList;
	}

	@Override
	public String deleteEnvSettings(List<String> settingIds) throws ServiceLayerException {
		Integer totalCount = settingIds.size();
		Integer successCount = 0;

		try {
			//先查詢出setting_name for後續刷新環境變數(Env)使用
			List<SysConfigSetting> settings = sysConfigSettingDAO.findSysConfigSettingByIds(settingIds);

			List<String> settingNames = new ArrayList<>();
			for (SysConfigSetting entity : settings) {
				settingNames.add(entity.getSettingName());
			}

			//刪除
			successCount = sysConfigSettingDAO.deleteSysConfigSetting(settingIds, SecurityUtil.getSecurityUser().getUsername());

			//更新完DB資料後同步至系統環境變數(Env)<僅更新此次有異動參數>
			if (!settingNames.isEmpty()) {
				sysEnvUtils.refreshByNames(settingNames);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);

		}

		String msg = "選取刪除資料 {0} 筆；成功 {1} 筆、失敗 {2} 筆";
		Object[] args = new Object[] {
				totalCount, successCount, (totalCount-successCount)
		};

		return CommonUtils.converMsg(msg, args);

	}

	@Override
	public String addOrModifyEnvSettings(List<EnvServiceVO> esVOs) throws ServiceLayerException {
		Integer totalCount = esVOs.size();
		Integer successCount = 0;

		try {
			List<String> settingNames = new ArrayList<>();
			SysConfigSetting entity;
			for (EnvServiceVO esVO : esVOs) {
				entity = sysConfigSettingDAO.findSysConfigSettingById(esVO.getSettingId());

				final String username = SecurityUtil.getSecurityUser().getUsername();
				final Timestamp nowTimestamp = new Timestamp((new Date()).getTime());

				if (entity == null) {
					entity = new SysConfigSetting();
					entity.setCreateBy(username);
					entity.setCreateTime(nowTimestamp);
				}

				entity.setSettingName(esVO.getModifySettingName());
				entity.setSettingValue(esVO.getModifySettingValue());
				entity.setSettingRemark(esVO.getModifySettingRemark());
				entity.setUpdateBy(username);
				entity.setUpdateTime(nowTimestamp);

				sysConfigSettingDAO.saveSysConfigSetting(entity);
				successCount++;

				settingNames.add(esVO.getModifySettingName());
			}

			//更新完DB資料後同步至系統環境變數(Env)<僅更新此次有異動參數>
			if (!settingNames.isEmpty()) {
				sysEnvUtils.refreshByNames(settingNames);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);

		}

		String msg = "異動資料 {0} 筆；成功 {1} 筆、失敗 {2} 筆";
		Object[] args = new Object[] {
				totalCount, successCount, (totalCount-successCount)
		};

		return CommonUtils.converMsg(msg, args);
	}

	@Override
	public String refreshAllEnv() throws ServiceLayerException {
		String msg = "Refresh success";

		try {
			sysEnvUtils.refreshAll();

		} catch (Exception e) {
			log.error(e.toString(), e);

			msg = "Refresh failed";
		}

		return msg;
	}
}
