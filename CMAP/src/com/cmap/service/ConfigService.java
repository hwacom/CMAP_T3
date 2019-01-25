package com.cmap.service;

import java.util.List;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.ConfigVO;

public interface ConfigService {

	public List<ConfigVO> findConfigContentSetting(
			List<ConfigVO> configVOList, String settingType, String systemVersion, String deviceNameLike, String deviceListId) throws ServiceLayerException;
}
