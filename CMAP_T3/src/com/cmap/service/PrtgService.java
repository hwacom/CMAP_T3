package com.cmap.service;

import com.cmap.exception.ServiceLayerException;
import com.cmap.model.PrtgAccountMapping;

public interface PrtgService {

	public PrtgAccountMapping getMappingBySourceId(String sourceId) throws ServiceLayerException;

	public String getMapUrlBySourceIdAndType(String sourceId, String type) throws ServiceLayerException;

}
