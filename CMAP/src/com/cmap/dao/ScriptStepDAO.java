package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ScriptStepDAOVO;

public interface ScriptStepDAO {

	public List<ScriptStepDAOVO> findScriptStepByScriptCode(String scriptCode);
}
