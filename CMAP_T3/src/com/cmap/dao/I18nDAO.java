package com.cmap.dao;

import java.util.List;

import com.cmap.model.I18n;

public interface I18nDAO {

	public I18n findI18n(String key, String locale);
	
	public List<I18n> listI18n();
	
}
