package com.cmap.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.dao.SysErrorLogDAO;
import com.cmap.dao.SysJobLogDAO;
import com.cmap.dao.vo.SysLogDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.SysErrorLog;
import com.cmap.model.SysJobLog;
import com.cmap.service.LogService;
import com.cmap.service.vo.LogServiceVO;

@Service("logService")
@Transactional
public class LogServiceImpl implements LogService {
	@Log
	private static Logger log;

	@Autowired
	SysErrorLogDAO sysErrorLogDAO;

	@Autowired
	SysJobLogDAO sysJobLogDAO;

	@Override
	public long countSysErrorLogByVO(LogServiceVO lsVO) throws ServiceLayerException {
		long retCount = 0;

		try {
			SysLogDAOVO daovo = new SysLogDAOVO();

			if (lsVO != null) {
				BeanUtils.copyProperties(lsVO, daovo);
			}

			retCount = sysErrorLogDAO.countSysErrorLogByDAOVO(daovo);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e.toString());
		}

		return retCount;
	}

	@Override
	public List<LogServiceVO> findSysErrorLogByVO(LogServiceVO lsVO) throws ServiceLayerException {
		List<LogServiceVO> retList = new ArrayList<LogServiceVO>();

		try {
			SysLogDAOVO daovo = new SysLogDAOVO();

			if (lsVO != null) {
				BeanUtils.copyProperties(lsVO, daovo);
			}

			final Integer startRow = lsVO.getStartNum();
			final Integer pageLength = lsVO.getPageLength();

			List<SysErrorLog> entities = sysErrorLogDAO.findSysErrorLogByDAOVO(daovo, startRow, pageLength);

			LogServiceVO vo;
			for (SysErrorLog entity : entities) {
				vo = new LogServiceVO();
				BeanUtils.copyProperties(entity, vo);
				vo.setEntryDateStr(entity.getEntryDate() != null ? Constants.FORMAT_YYYYMMDD_HH24MISS.format(entity.getEntryDate()) : "");
				retList.add(vo);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e.toString());
		}

		return retList;
	}

	@Override
	public long countSysJobLogByVO(LogServiceVO lsVO) throws ServiceLayerException {
		long retCount = 0;

		try {
			SysLogDAOVO daovo = new SysLogDAOVO();

			if (lsVO != null) {
				BeanUtils.copyProperties(lsVO, daovo);
			}

			retCount = sysJobLogDAO.countSysJobLogByDAOVO(daovo);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e.toString());
		}

		return retCount;
	}

	@Override
	public List<LogServiceVO> findSysJobLogByVO(LogServiceVO lsVO) throws ServiceLayerException {
		List<LogServiceVO> retList = new ArrayList<LogServiceVO>();

		try {
			SysLogDAOVO daovo = new SysLogDAOVO();

			if (lsVO != null) {
				BeanUtils.copyProperties(lsVO, daovo);
			}

			final Integer startRow = lsVO.getStartNum();
			final Integer pageLength = lsVO.getPageLength();

			List<SysJobLog> entities = sysJobLogDAO.findSysJobLogByDAOVO(daovo, startRow, pageLength);

			LogServiceVO vo;
			for (SysJobLog entity : entities) {
				vo = new LogServiceVO();
				BeanUtils.copyProperties(entity, vo);
				vo.setStartTimeStr(entity.getStartTime() != null ? Constants.FORMAT_YYYYMMDD_HH24MISS.format(entity.getStartTime()) : "");
				vo.setEndTimeStr(entity.getEndTime() != null ? Constants.FORMAT_YYYYMMDD_HH24MISS.format(entity.getEndTime()) : "");
				vo.setPrevFireTimeStr(entity.getPrevFireTime() != null ? Constants.FORMAT_YYYYMMDD_HH24MISS.format(entity.getPrevFireTime()) : "");
				vo.setNextFireTimeStr(entity.getNextFireTime() != null ? Constants.FORMAT_YYYYMMDD_HH24MISS.format(entity.getNextFireTime()) : "");
				retList.add(vo);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e.toString());
		}

		return retList;
	}

	@Override
	public LogServiceVO findLogDetail(LogServiceVO lsVO) throws ServiceLayerException {
		LogServiceVO retVO = new LogServiceVO();

		try {
			Object entity = null;

			if (lsVO.getQueryLogType() == LogType.ERROR_LOG) {
				entity = sysErrorLogDAO.findSysErrorLogByLogId(lsVO.getQueryLogId());

			} else if (lsVO.getQueryLogType() == LogType.JOB_LOG) {
				entity = sysJobLogDAO.findSysJobLogByLogId(lsVO.getQueryLogId());
			}

			if (entity != null) {
				BeanUtils.copyProperties(entity, retVO);

				if (lsVO.getQueryLogType() == LogType.ERROR_LOG) {
					retVO.setDetails(retVO.printErrorLogDetailsWithHtml());

				} else if (lsVO.getQueryLogType() == LogType.JOB_LOG) {
					retVO.setDetails(retVO.printJobLogDetailsWithHtml());
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e.toString());
		}

		return retVO;
	}
}
