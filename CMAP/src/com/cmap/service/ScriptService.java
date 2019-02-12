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

	/**
	 * 依查詢條件查詢符合的資料筆數
	 * @param ssVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public long countScriptInfo(ScriptServiceVO ssVO) throws ServiceLayerException;

	/**
	 * 依查詢條件查詢符合的資料
	 * @param ssVO
	 * @param startRow
	 * @param pageLength
	 * @return
	 * @throws ServiceLayerException
	 */
	public List<ScriptServiceVO> findScriptInfo(ScriptServiceVO ssVO, Integer startRow, Integer pageLength) throws ServiceLayerException;

	/**
	 * 以 Script_Info_Id 查找資料
	 * @param scriptInfoId
	 * @return
	 * @throws ServiceLayerException
	 */
	public ScriptServiceVO getScriptInfoByScriptInfoId(String scriptInfoId) throws ServiceLayerException;
}
