package com.cmap.service.impl;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.dao.SysConfigSettingDAO;
import com.cmap.dao.vo.SysConfigSettingDAOVO;
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
	private static Log log = LogFactory.getLog(EnvServiceImpl.class);
	
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
	public long countEnvSettingsByVO(EnvServiceVO esVO) throws Exception {
		long retVal = 0;
		
		try {
			retVal = sysConfigSettingDAO.countSysConfigSettingByVO(transServiceVO2DAOVO(esVO));
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			throw e;
		}
		return retVal;
	}

	@Override
	public List<EnvServiceVO> findEnvSettingsByVO(EnvServiceVO esVO, Integer startRow, Integer pageLength) throws Exception {
		List<EnvServiceVO> retList = new ArrayList<EnvServiceVO>();
		
		try {
			//查找DB設定內容
			List<SysConfigSetting> entityList = 
					sysConfigSettingDAO.findSysConfigSettingByVO(transServiceVO2DAOVO(esVO), startRow, pageLength);
			
			int diffCount = 0;
			EnvServiceVO retVO;
			for (SysConfigSetting entity : entityList) {
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
				Class<?> type = Env.class.getDeclaredField(retVO.getSettingName()).getType();
				if (type.isAssignableFrom(ArrayList.class)) {
					List<?> envList = (List<?>)Env.class.getDeclaredField(retVO.getSettingName()).get(null);
					
					for (Object obj : envList) {
						envVal = hasEncode ? Base64.encode(obj.toString().getBytes()) : obj.toString();
						isSame = retVO.getSettingValue().equals(envVal) ? true : false;
						
						if (isSame) {
							break;
						}
					}
				} else {
					String currentEnvVal = Env.class.getDeclaredField(retVO.getSettingName()).get(null).toString();
					envVal = hasEncode ? Base64.encode(currentEnvVal.getBytes()) : currentEnvVal;
					isSame = retVO.getSettingValue().equals(envVal) ? true : false;
				}
				
				if (!isSame) {
					envVal = "";
					diffCount++;
				}
				
				retVO.set_settingValue(envVal);
				retVO.setSame(isSame);
				
				retList.add(retVO);
			}
			
			retList.get(0).setDifferCount(diffCount);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
		return retList;
	}

	@Override
	public String deleteEnvSettings(List<String> settingIds) throws Exception {
		Integer totalCount = settingIds.size();
		Integer successCount = 0;
		
		try {
			successCount = sysConfigSettingDAO.deleteSysConfigSetting(settingIds, SecurityUtil.getSecurityUser().getUsername());
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
		}
		
		String msg = "選取刪除資料 {0} 筆；成功 {1} 筆、失敗 {2} 筆";
		Object[] args = new Object[] {
			totalCount, successCount, (totalCount-successCount)
		};
		
		return CommonUtils.converMsg(msg, args);
		
	}

	@Override
	public String addOrModifyEnvSettings(List<EnvServiceVO> esVOs) throws Exception {
		Integer totalCount = esVOs.size();
		Integer successCount = 0;
		
		try {
			List<String> settingNames = new ArrayList<String>();
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
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
		} 
		
		String msg = "異動資料 {0} 筆；成功 {1} 筆、失敗 {2} 筆";
		Object[] args = new Object[] {
			totalCount, successCount, (totalCount-successCount)
		};
		
		return CommonUtils.converMsg(msg, args);
	}

	@Override
	public String refreshAllEnv() throws Exception {
		String msg = "Refresh success";
		
		try {
			sysEnvUtils.refreshAll();
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
			msg = "Refresh failed";
		}
		
		return msg;
	}
}
