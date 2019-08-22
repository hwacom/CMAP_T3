package com.cmap.plugin.module.unauthorizeddhcp;

import java.util.List;

public interface UnauthorizedDhcpDAO {

	public long countUnauthorizedDhcpRecord(UnauthorizedDhcpServiceVO udsVO);
	
	public List<Object[]> findUnauthorizedDhcpRecord(UnauthorizedDhcpServiceVO udsVO,
	        Integer startRow, Integer pageLength);
}
