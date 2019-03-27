package com.cmap.service;

import java.util.List;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.LogServiceVO;

public interface LogService {

	public enum LogType {
		ERROR_LOG,
		JOB_LOG
	}

	/**
	 * 依查詢條件查找符合的 {@link com.cmap.model.SysErrorLog} 資料筆數
	 * @param lsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public long countSysErrorLogByVO(LogServiceVO lsVO) throws ServiceLayerException;

	/**
	 * 依查詢條件查找符合的  {@link com.cmap.model.SysErrorLog} 資料
	 * @param lsVO
	 * @param startRow
	 * @param pageLength
	 * @return
	 * @throws ServiceLayerException
	 */
	public List<LogServiceVO> findSysErrorLogByVO(LogServiceVO lsVO) throws ServiceLayerException;

	/**
	 * 依查詢條件查找符合的  {@link com.cmap.model.SysJobLog} 資料筆數
	 * @param lsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public long countSysJobLogByVO(LogServiceVO lsVO) throws ServiceLayerException;

	/**
	 * 依查詢條件查找符合的  {@link com.cmap.model.SysJobLog} 資料
	 * @param lsVO
	 * @param startRow
	 * @param pageLength
	 * @return
	 * @throws ServiceLayerException
	 */
	public List<LogServiceVO> findSysJobLogByVO(LogServiceVO lsVO) throws ServiceLayerException;

	/**
	 * 查找  {@link com.cmap.model.SysErrorLog} 中 EXCEPTION 明細內容
	 * @param lsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public LogServiceVO findLogDetail(LogServiceVO lsVO) throws ServiceLayerException;
}
