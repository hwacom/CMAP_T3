package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ScriptDAOVO;

public interface ScriptStepDAO {

	public List<ScriptDAOVO> findScriptStepByScriptInfoIdOrScriptCode(String scriptInfoId, String scriptCode);

}
