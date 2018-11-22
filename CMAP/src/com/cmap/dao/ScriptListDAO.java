package com.cmap.dao;

import java.util.List;

import com.cmap.comm.enums.ScriptType;
import com.cmap.dao.vo.ScriptDAOVO;

public interface ScriptListDAO {

	public long countScriptList(ScriptDAOVO slDAOVO);

	public List<ScriptDAOVO> findScriptListByScriptCode(String scriptCode);

	public String findDefaultScriptCodeBySystemVersion(ScriptType scriptType, String systemVersion);
}
