package com.cmap.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.annotation.Log;
import com.cmap.dao.PrtgDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.PrtgAccountMapping;
import com.cmap.service.PrtgService;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Service("prtgService")
@Transactional
public class PrtgServiceImpl implements PrtgService {
	@Log
	private static Logger log;

	@Autowired
	private PrtgDAO prtgDAO;

	@Override
	public String getDashboardMapBySourceId(String sourceId) throws ServiceLayerException {
		try {
			if (StringUtils.isBlank(sourceId)) {
				throw new ServiceLayerException("取得PRTG dashboard MAP失敗! >> Source_ID 不得為空");
			}

			PrtgAccountMapping mapping = prtgDAO.findPrtgAccountMappingBySourceId(sourceId);

			if (mapping == null) {
				throw new ServiceLayerException("取得PRTG dashboard MAP失敗! >> 查無資料, Source_ID: " + sourceId);
			}

			return mapping.getDashboardMapUrl();

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("取得PRTG dashboard MAP失敗!  >> 非預期錯誤");
		}
	}
}