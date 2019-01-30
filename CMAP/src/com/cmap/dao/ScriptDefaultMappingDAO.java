package com.cmap.dao;

import com.cmap.comm.enums.ScriptType;
import com.cmap.dao.vo.ScriptDAOVO;

public interface ScriptDefaultMappingDAO {

	public long countScriptList(ScriptDAOVO slDAOVO);

	public String findDefaultScriptCodeBySystemVersion(ScriptType scriptType, String systemVersion);
}
