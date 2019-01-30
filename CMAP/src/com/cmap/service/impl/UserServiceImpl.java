package com.cmap.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.comm.BaseAuthentication;
import com.cmap.dao.UserDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.UserRightSetting;
import com.cmap.service.UserService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
	@Log
	private static Logger log;

	@Autowired
	private UserDAO userDAO;

	@Override
	public boolean checkUserCanAccess(HttpServletRequest request, String belongGroup, String account) {
		boolean canAccess = false;
		try {
			UserRightSetting entity = userDAO.findUserRightSetting(belongGroup, account);

			if (entity == null) {

				if (StringUtils.equals(belongGroup, Constants.DATA_STAR_SYMBOL)
						&& StringUtils.equals(account, Constants.DATA_STAR_SYMBOL)) {
					canAccess = false;

				} else {
					if (!StringUtils.equals(account, Constants.DATA_STAR_SYMBOL)) {
						account = Constants.DATA_STAR_SYMBOL;

					} else if (!StringUtils.equals(belongGroup, Constants.DATA_STAR_SYMBOL)) {
						belongGroup = Constants.DATA_STAR_SYMBOL;
					}

					return checkUserCanAccess(request, belongGroup, account);
				}
			} else {
				final boolean access =
						StringUtils.equals(entity.getDenyAccess(), Constants.DATA_Y) ? false : true;

				if (!access) {
					canAccess = false;

				} else {
					canAccess = true;

					final boolean isAdmin =
							StringUtils.equals(entity.getIsAdmin(), Constants.DATA_Y) ? true : false;

					if (isAdmin) {
						BaseAuthentication.setAdminRole2Session(request);
					}
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			canAccess = false;
		}
		return canAccess;
	}

	@Override
	public boolean setUserFuncRight(HttpServletRequest request, String account) throws ServiceLayerException {
		// TODO 自動產生的方法 Stub
		return false;
	}


}
