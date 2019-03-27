package com.cmap.service;

import java.util.List;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.SysCheckServiceVO;

public interface SysCheckService {

	/**
	 * 傳入SQL執行 (可多組)
	 * @param sqls
	 * @throws ServiceLayerException
	 */
	public SysCheckServiceVO excuteUpdateSQLs(List<String> sqls, boolean jobTrigger) throws ServiceLayerException;
}
