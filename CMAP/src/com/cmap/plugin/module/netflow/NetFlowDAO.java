package com.cmap.plugin.module.netflow;

import java.util.List;
import java.util.Map;

import com.cmap.dao.BaseDAO;
import com.cmap.model.DataPollerSetting;
import com.cmap.service.vo.DataPollerServiceVO;

public interface NetFlowDAO extends BaseDAO {

	public long countNetFlowDataFromDB(NetFlowVO nfVO, List<String> searchLikeField, String tableName);

	public List<Object[]> findNetFlowDataFromDB(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField, String tableName);

	public NetFlowVO findNetFlowDataFromFile(
			DataPollerSetting setting,
			Map<Integer, DataPollerServiceVO> fieldIdxMap,
			Map<String, DataPollerServiceVO> fieldVOMap,
			Map<String, NetFlowVO> queryMap,
			Integer startRow,
			Integer pageLength);

}
