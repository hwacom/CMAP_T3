package com.cmap.dao;

import java.util.List;

import com.cmap.model.ScriptType;

public interface ScriptTypeDAO {

	public List<ScriptType> findScriptTypeByDefaultFlag(String defaultFlag);
}
