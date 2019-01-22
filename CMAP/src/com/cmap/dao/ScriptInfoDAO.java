package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ScriptInfoDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.ScriptInfo;

public interface ScriptInfoDAO {

	public long countScriptInfo(ScriptInfoDAOVO daovo);

	public List<ScriptInfo> findScriptInfo(ScriptInfoDAOVO daovo, Integer startRow, Integer pageLength);

	public ScriptInfo findScriptInfoById(String scriptInfoId);

	/**
	 * 查找[腳本類別 + 設備系統版本]對應的預設腳本資訊
	 * @param scriptType
	 * @param systemVersion
	 * @return
	 * @throws ServiceLayerException
	 */
	public ScriptInfo findDefaultScriptInfoByScriptTypeAndSystemVersion(String scriptType, String systemVersion) throws ServiceLayerException;
}
