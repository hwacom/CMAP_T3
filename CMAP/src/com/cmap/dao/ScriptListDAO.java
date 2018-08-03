package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ScriptListDAOVO;

public interface ScriptListDAO {

	public List<ScriptListDAOVO> findScriptListByScriptCode(String scriptCode);
}
