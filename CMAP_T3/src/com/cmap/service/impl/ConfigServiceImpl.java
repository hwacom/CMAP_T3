package com.cmap.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.dao.ConfigDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.ConfigContentSetting;
import com.cmap.service.ConfigService;
import com.cmap.service.vo.ConfigVO;

@Service("configService")
@Transactional
public class ConfigServiceImpl extends CommonServiceImpl implements ConfigService {
	@Log
	private static Logger log;

	@Autowired
	private ConfigDAO configDAO;

	@Override
	public ConfigVO findConfigContentSetting(ConfigVO configVO, String settingType, String systemVersion, String deviceNameLike, String deviceListId) throws ServiceLayerException {
		configVO = (configVO == null) ? new ConfigVO() : configVO;
		List<ConfigVO> retList = configVO.getConfigVOList() == null ? new ArrayList<>() : configVO.getConfigVOList();

		try {
			if (StringUtils.isBlank(settingType)) {
				throw new ServiceLayerException("[查詢Config_Content_Setting] >> settingType 不可為空!");
			}
			systemVersion = StringUtils.isBlank(systemVersion) ? Constants.DATA_STAR_SYMBOL : systemVersion;
			deviceNameLike = StringUtils.isBlank(deviceNameLike) ? Constants.DATA_STAR_SYMBOL : deviceNameLike;
			deviceListId = StringUtils.isBlank(deviceListId) ? Constants.DATA_STAR_SYMBOL : deviceListId;

			List<ConfigContentSetting> entities = configDAO.findConfigContentSetting(settingType, systemVersion, deviceNameLike, deviceListId);

			if (entities != null && !entities.isEmpty()) {
				retList.addAll(transConfigContentSetting2ConfigVO(entities));
				configVO.setConfigVOList(retList);
			}

			/*
			 * 設定可以by不同範圍設定，依序由範圍小到大循環查詢，取得所有符合的設定
			 * 範圍小→大: deviceListId > deviceNameLike > systemVersion > settingType
			 */
			if (StringUtils.equals(deviceListId, Constants.DATA_STAR_SYMBOL)
					&& StringUtils.equals(deviceNameLike, Constants.DATA_STAR_SYMBOL)
						&& StringUtils.equals(systemVersion, Constants.DATA_STAR_SYMBOL)) {

				return configVO;

			} else {
				if (!StringUtils.equals(deviceListId, Constants.DATA_STAR_SYMBOL)) {
					deviceListId = Constants.DATA_STAR_SYMBOL;

				} else if (!StringUtils.equals(deviceNameLike, Constants.DATA_STAR_SYMBOL)) {
					deviceNameLike = Constants.DATA_STAR_SYMBOL;

				} else if (!StringUtils.equals(systemVersion, Constants.DATA_STAR_SYMBOL)) {
					systemVersion = Constants.DATA_STAR_SYMBOL;
				}

				return findConfigContentSetting(configVO, settingType, systemVersion, deviceNameLike, deviceListId);
			}

		} catch (ServiceLayerException sle) {
			log.error(sle.toString(), sle);
			throw sle;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("[查詢Config_Content_Setting] >> 發生異常");
		}
	}

	private List<ConfigVO> transConfigContentSetting2ConfigVO(List<ConfigContentSetting> entities) {
		List<ConfigVO> retList = new ArrayList<>();

		ConfigVO cVO = null;
		for (ConfigContentSetting entity : entities) {
			cVO = new ConfigVO();
			BeanUtils.copyProperties(entity, cVO);
			retList.add(cVO);
		}
		return retList;
	}
}
