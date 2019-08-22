package com.cmap.service.impl;

import java.util.List;
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
	public boolean checkUserCanAccess(HttpServletRequest request, boolean firstRound, String belongGroup, String[] roles, String account) {
		boolean canAccess = false;
		try {
		    List<UserRightSetting> entities = userDAO.findUserRightSetting(belongGroup, roles, account);

			if (entities == null || (entities != null && entities.isEmpty())) {

				if (StringUtils.equals(belongGroup, Constants.DATA_STAR_SYMBOL)
						&& StringUtils.equals(account, Constants.DATA_STAR_SYMBOL)) {
					canAccess = false;

				} else {
					if (!StringUtils.equals(account, Constants.DATA_STAR_SYMBOL)) {
						account = Constants.DATA_STAR_SYMBOL;

					} else if (!StringUtils.equals(belongGroup, Constants.DATA_STAR_SYMBOL)) {
						belongGroup = Constants.DATA_STAR_SYMBOL;
					}

					return checkUserCanAccess(request, false, belongGroup, roles, account);
				}
			} else {
				// 可能符合多個腳色權限設定，逐筆掃描
				for (UserRightSetting setting : entities) {
					String denyAccess = setting.getDenyAccess();

					if (StringUtils.equals(setting.getIsAdmin(), Constants.DATA_Y)) {
						// ADMIN
						canAccess = StringUtils.equals(denyAccess, Constants.DATA_Y) ? false : true;

						if (canAccess) {
						    BaseAuthentication.setAdminRole2Session(request);
						}

						break;
					}
					if (StringUtils.equals(denyAccess, Constants.DATA_N)) {
						// USER
						canAccess = true;
						break;
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
