package com.cmap.dao;

import java.util.List;

import com.cmap.model.UserRightSetting;

public interface UserDAO extends BaseDAO {

	List<UserRightSetting> findUserRightSetting(String belongGroup, String[] roles, String account);
}
