package com.cmap.plugin.module.netflow;

import java.util.List;

import com.cmap.exception.ServiceLayerException;

public interface NetFlowService {

	public long countNetFlowRecord(NetFlowVO nfVO, List<String> searchLikeField) throws ServiceLayerException;

	public List<NetFlowVO> findNetFlowRecord(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField) throws ServiceLayerException;
}
