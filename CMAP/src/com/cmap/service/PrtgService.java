package com.cmap.service;

import com.cmap.exception.ServiceLayerException;

public interface PrtgService {

	public String getDashboardMapBySourceId(String sourceId) throws ServiceLayerException;

}
