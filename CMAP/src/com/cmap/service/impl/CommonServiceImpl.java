package com.cmap.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.service.CommonService;
import com.cmap.utils.ApiUtils;
import com.cmap.utils.impl.PrtgApiUtils;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
	private static Log log = LogFactory.getLog(CommonServiceImpl.class);

	@Override
	public Object[] getGroupAndDeviceMenu(HttpServletRequest request) {
		Object[] retObj = null;
		ApiUtils prtgApi = null;
		try {
			prtgApi = new PrtgApiUtils();
			Object[] prtgObj = prtgApi.getGroupAndDeviceMenu();
			
			if (prtgObj != null) {
				retObj = new Object[] {
					prtgObj[0]
				   ,prtgObj[1]
				};
				
				if (prtgObj[2] != null && !((Map<String, Map<String, String>>)prtgObj[2]).isEmpty()) {
					if (request.getSession() != null) {
						request.getSession().setAttribute(DEVICE_MENU_ATTR, prtgObj[2]);
					}
				}
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
		return retObj;
	}
}
