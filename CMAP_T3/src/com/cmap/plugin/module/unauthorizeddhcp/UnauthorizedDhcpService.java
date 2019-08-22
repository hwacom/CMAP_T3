package com.cmap.plugin.module.unauthorizeddhcp;

import java.util.List;

import com.cmap.exception.ServiceLayerException;

public interface UnauthorizedDhcpService {

	public long countUnauthorizedDhcpRecord(UnauthorizedDhcpServiceVO udsVO) throws ServiceLayerException;
	
	public List<UnauthorizedDhcpServiceVO> findUnauthorizedDhcpRecord(UnauthorizedDhcpServiceVO udsVO) throws ServiceLayerException;
}
