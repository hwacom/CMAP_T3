package com.cmap.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
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
	public String getMapUrlBySourceIdAndType(String sourceId, String type) throws ServiceLayerException {
		try {
			if (StringUtils.isBlank(sourceId)) {
				throw new ServiceLayerException("取得PRTG mapping表失敗! >> 傳入ID為空");
			}

			PrtgAccountMapping mapping = prtgDAO.findPrtgAccountMappingBySourceId(sourceId);

			if (mapping == null) {
				throw new ServiceLayerException("取得PRTG mapping表失敗! >> 傳入ID查無資料, Source_ID: " + sourceId);
			}

			switch (type) {
				case Constants.MAP_URL_OF_INDEX:
					return mapping.getIndexUrl();

				case Constants.MAP_URL_OF_DASHBOARD:
					return mapping.getDashboardMapUrl();

				case Constants.MAP_URL_OF_TOPOGRAPHY:
                    return mapping.getTopographyMapUrl();

				case Constants.MAP_URL_OF_ALARM_SUMMARY:
                    return mapping.getAlarmSummaryMapUrl();

				case Constants.MAP_URL_OF_NET_FLOW_SUMMARY:
					return mapping.getNetFlowMapUrl();

				case Constants.MAP_URL_OF_NET_FLOW_OUTPUT:
                    return mapping.getNetFlowOutputMapUrl();

				case Constants.MAP_URL_OF_DEVICE_FAILURE:
					return mapping.getDeviceFailureMapUrl();

				case Constants.MAP_URL_OF_ABNORMAL_TRAFFIC:
					return mapping.getAbnormalTrafficMapUrl();

				case Constants.MAP_URL_OF_EMAIL_UPDATE:
                    return mapping.getEmailUpdateMapUrl();

				default:
					return null;
			}

		} catch (ServiceLayerException sle) {
            throw sle;

        } catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("取得PRTG mapping表失敗!  >> 非預期錯誤(" + e.getMessage() + ")");
		}
	}

	@Override
	public PrtgAccountMapping getMappingBySourceId(String sourceId) throws ServiceLayerException {
		try {
			if (StringUtils.isBlank(sourceId)) {
				throw new ServiceLayerException("取得PRTG mapping表失敗! >> 傳入ID為空");
			}

			PrtgAccountMapping mapping = prtgDAO.findPrtgAccountMappingBySourceId(sourceId);

			if (mapping == null) {
				throw new ServiceLayerException("取得PRTG mapping表失敗! >> 傳入ID查無資料, Source_ID: " + sourceId);
			}

			return mapping;

		} catch (ServiceLayerException sle) {
		    throw sle;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("取得PRTG mapping表失敗!  >> 非預期錯誤(" + e.getMessage() + ")");
		}
	}
}
