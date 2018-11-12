package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ScriptInfoDAOVO;
import com.cmap.model.ScriptInfo;

public interface ScriptInfoDAO {

	public long countScriptInfo(ScriptInfoDAOVO daovo);

	public List<ScriptInfo> findScriptInfo(ScriptInfoDAOVO daovo, Integer startRow, Integer pageLength);
}
