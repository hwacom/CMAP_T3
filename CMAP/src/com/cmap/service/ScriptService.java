package com.cmap.service;

import java.util.List;
import java.util.Map;

import com.cmap.comm.enums.ScriptType;
import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.ScriptServiceVO;

public interface ScriptService {

	public List<ScriptServiceVO> loadDefaultScript(String deviceListId, List<ScriptServiceVO> script, ScriptType type) throws ServiceLayerException;

	public List<ScriptServiceVO> loadSpecifiedScript(String scriptInfoId, String scriptCode, Map<String, String> varMap, List<ScriptServiceVO> scripts) throws ServiceLayerException;

	/**
	 * 查找[腳本類別 + 設備系統版本]對應的預設腳本資訊
	 * @param scriptType
	 * @param systemVersion
	 * @return
	 * @throws ServiceLayerException
	 */
	public ScriptServiceVO findDefaultScriptInfoByScriptTypeAndSystemVersion(String scriptType, String systemVersion) throws ServiceLayerException;
}
