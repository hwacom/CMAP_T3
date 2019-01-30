package com.cmap.dao;

import com.cmap.model.UserRightSetting;

public interface UserDAO extends BaseDAO {

	UserRightSetting findUserRightSetting(String belongGroup, String account);
}
