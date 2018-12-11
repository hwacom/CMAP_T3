package com.cmap.plugin.module.netflow;

import java.util.List;

import com.cmap.dao.BaseDAO;

public interface NetFlowDAO extends BaseDAO {

	public long countNetFlowData(NetFlowVO nfVO, List<String> searchLikeField, String tableName);

	public List<Object[]> findNetFlowData(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField, String tableName);
}
