package com.cmap.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.annotation.Log;
import com.cmap.dao.SysCheckDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.service.SysCheckService;
import com.cmap.service.vo.SysCheckServiceVO;

@Service("sysCheckService")
@Transactional
public class SysCheckServiceImpl implements SysCheckService {
	@Log
	private static Logger log;

	@Autowired
	private SysCheckDAO sysCheckDAO;

	@Override
	public SysCheckServiceVO excuteUpdateSQLs(List<String> sqls, boolean jobTrigger) throws ServiceLayerException {
		StringBuilder records = new StringBuilder();
		StringBuilder remarks = new StringBuilder();

		if (sqls != null) {
			final int totalCount = sqls.size();
			int errorCount = 0;

			int idx = 0;
			for (String sql : sqls) {
				int rc = 0;
				try {
					rc = sysCheckDAO.excuteUpdateSQL(sql);

				} catch (Exception e) {
					log.error(e.toString(), e);
					errorCount++;
					continue;

				} finally {
					records.append("["+idx+"]="+rc+";");
					idx++;
				}
			}

			remarks.append("執行 "+totalCount+" 道SQL。成功: "+(totalCount-errorCount)+"；失敗: "+errorCount);
		}

		SysCheckServiceVO scsVO = new SysCheckServiceVO();
		scsVO.setJobExcuteResultRecords(records.toString());
		scsVO.setJobExcuteRemark(remarks.toString());

		return scsVO;
	}
}
