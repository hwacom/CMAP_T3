package com.cmap.dao;

import com.cmap.model.PrtgAccountMapping;

public interface PrtgDAO extends BaseDAO {

	public PrtgAccountMapping findPrtgAccountMappingBySourceId(String sourceId);
}
