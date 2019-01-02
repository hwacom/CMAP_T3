package com.cmap.plugin.module.netflow;

import java.util.List;

import com.cmap.exception.ServiceLayerException;

public interface NetFlowService {

	public long countNetFlowRecordFromDB(NetFlowVO nfVO, List<String> searchLikeField) throws ServiceLayerException;

	public List<NetFlowVO> findNetFlowRecordFromDB(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField) throws ServiceLayerException;

	public NetFlowVO findNetFlowRecordFromFile(NetFlowVO nfVO, Integer startRow, Integer pageLength) throws ServiceLayerException;
}
