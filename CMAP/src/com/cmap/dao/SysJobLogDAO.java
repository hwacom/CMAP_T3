package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.SysLogDAOVO;
import com.cmap.model.SysJobLog;

public interface SysJobLogDAO {

	/**
	 * 查詢符合條件筆數
	 * @param daovo
	 * @return
	 */
	public long countSysJobLogByDAOVO(SysLogDAOVO daovo);

	/**
	 * 查詢符合條件資料
	 * @param daovo
	 * @return
	 */
	public List<SysJobLog> findSysJobLogByDAOVO(SysLogDAOVO daovo, Integer startRow, Integer pageLength);

	/**
	 * 查找資料 by ID
	 * @param logId
	 * @return
	 */
	public SysJobLog findSysJobLogByLogId(String logId);

	/**
	 * 新增資料
	 * @param entity
	 */
	public void insertSysJobLog(SysJobLog entity);
}
