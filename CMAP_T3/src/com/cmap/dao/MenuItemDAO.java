package com.cmap.dao;

import java.util.List;

import com.cmap.model.MenuItem;

public interface MenuItemDAO {

	public List<MenuItem> findMenuItemByMenuCode(String menuCode) throws Exception;
}
