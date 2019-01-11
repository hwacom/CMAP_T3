package com.cmap.service;

import com.cmap.exception.ServiceLayerException;

public interface PrtgService {

	public String getMapUrlBySourceIdAndType(String sourceId, String type) throws ServiceLayerException;

}
