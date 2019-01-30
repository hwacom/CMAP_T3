package com.cmap.service;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.ConfigVO;

public interface ConfigService {

	public ConfigVO findConfigContentSetting(
			ConfigVO configVO, String settingType, String systemVersion, String deviceNameLike, String deviceListId) throws ServiceLayerException;
}
