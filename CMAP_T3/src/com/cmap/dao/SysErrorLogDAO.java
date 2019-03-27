package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.SysLogDAOVO;
import com.cmap.model.SysErrorLog;

public interface SysErrorLogDAO {

	/**
	 * 查詢符合條件筆數
	 * @param daovo
	 * @return
	 */
	public long countSysErrorLogByDAOVO(SysLogDAOVO daovo);

	/**
	 * 查詢符合條件資料
	 * @param daovo
	 * @return
	 */
	public List<SysErrorLog> findSysErrorLogByDAOVO(SysLogDAOVO daovo, Integer startRow, Integer pageLength);

	/**
	 * 查找資料 by ID
	 * @param logId
	 * @return
	 */
	public SysErrorLog findSysErrorLogByLogId(String logId);
}
