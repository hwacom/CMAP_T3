package com.cmap.dao;

import java.util.List;

public interface BaseDAO {

	public boolean insertEntity(Object entity);

	public boolean insertEntities(List<? extends Object> entities);

	public boolean updateEntity(Object entity);

	public boolean deleteEntity(Object entity);

	public boolean insertEntitiesByNativeSQL(List<String> nativeSQLs);

	public boolean deleteEntitiesByNativeSQL(List<String> nativeSQLs);
}
