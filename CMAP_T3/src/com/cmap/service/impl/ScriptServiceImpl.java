package com.cmap.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.comm.enums.ScriptType;
import com.cmap.dao.ScriptDefaultMappingDAO;
import com.cmap.dao.ScriptInfoDAO;
import com.cmap.dao.ScriptStepDAO;
import com.cmap.dao.vo.ScriptDAOVO;
import com.cmap.dao.vo.ScriptInfoDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DeviceList;
import com.cmap.model.ScriptInfo;
import com.cmap.service.ScriptService;
import com.cmap.service.vo.ScriptServiceVO;

@Service("scriptService")
@Transactional
public class ScriptServiceImpl extends CommonServiceImpl implements ScriptService {
	@Log
	private static Logger log;

	@Autowired
	private ScriptInfoDAO scriptInfoDAO;

	@Autowired
	private ScriptDefaultMappingDAO scriptListDefaultDAO;

	@Autowired
	@Qualifier("scriptStepActionDAOImpl")
	private ScriptStepDAO scriptStepActionDAO;

	@Autowired
	@Qualifier("scriptStepCheckDAOImpl")
	private ScriptStepDAO scriptStepCheckDAO;

	@Override
	public List<ScriptServiceVO> loadDefaultScript(String deviceListId, List<ScriptServiceVO> script, ScriptType type) throws ServiceLayerException {
		if (script != null && !script.isEmpty()) {
			return script;

		} else if (script == null) {
			script = new ArrayList<>();
		}

		DeviceList device = null;
		if (!StringUtils.equals(deviceListId, "*")) {
			device = deviceDAO.findDeviceListByDeviceListId(deviceListId);
		}

		String systemVersion = device != null ? device.getSystemVersion() : Env.MEANS_ALL_SYMBOL;
		final String scriptCode = scriptListDefaultDAO.findDefaultScriptCodeBySystemVersion(type, systemVersion);

		List<ScriptDAOVO> daovoList = scriptStepActionDAO.findScriptStepByScriptInfoIdOrScriptCode(null, scriptCode);

		if (daovoList == null || (daovoList != null && daovoList.isEmpty())) {
			if (!StringUtils.equals(systemVersion, Env.MEANS_ALL_SYMBOL)) {
				script = loadDefaultScript("*", script, type);	//帶入機器系統版本號查不到腳本時，將版本調整為*號後再查找一次預設腳本

			} else {
				throw new ServiceLayerException("未設定[" + type + "]預設腳本");
			}

		} else {
			if (script == null) {
				script = new ArrayList<>();
			}

			ScriptServiceVO ssVO;
			for (ScriptDAOVO daovo : daovoList) {
				ssVO = new ScriptServiceVO();
				BeanUtils.copyProperties(daovo, ssVO);

				script.add(ssVO);
			}
		}

		return script;
	}

	@Override
	public List<ScriptServiceVO> loadSpecifiedScript(String scriptInfoId, String scriptCode, List<Map<String, String>> varMapList, List<ScriptServiceVO> scripts) throws ServiceLayerException {
		List<ScriptServiceVO> retScriptList = new ArrayList<>();

		if (scripts != null && !scripts.isEmpty()) {
			return scripts;

		} else if (scripts == null) {
			scripts = new ArrayList<>();
		}

		List<ScriptDAOVO> daovoList = scriptStepActionDAO.findScriptStepByScriptInfoIdOrScriptCode(scriptInfoId, scriptCode);

		ScriptServiceVO ssVO;
		for (ScriptDAOVO daovo : daovoList) {
			ssVO = new ScriptServiceVO();
			BeanUtils.copyProperties(daovo, ssVO);

			scripts.add(ssVO);
		}

		if (scripts == null || (scripts != null && scripts.isEmpty())) {
			log.error("查無腳本資料 >> scriptInfoId: " + scriptInfoId + " , scriptCode: " + scriptCode);
			throw new ServiceLayerException("查無腳本資料，請重新操作");
		}

		// 有傳入參數MAP才需跑替換參數值流程
		if (varMapList != null && !varMapList.isEmpty()) {

			for (Map<String, String> varMap : varMapList) {
				for (ScriptServiceVO script : scripts) {
					ScriptServiceVO newVO = new ScriptServiceVO();
					BeanUtils.copyProperties(script, newVO);

					String cmd = newVO.getScriptContent();

					if (cmd.indexOf("%") != -1) {
						String[] strSlice = cmd.split("%");

						for (int i=0; i<strSlice.length; i++) {
							if (i % 2 == 0) {
								continue;

							} else {
								String varKey = Env.SCRIPT_VAR_KEY_SYMBOL + strSlice[i] + Env.SCRIPT_VAR_KEY_SYMBOL;

								if (!varMap.containsKey(varKey)) {
									throw new ServiceLayerException("錯誤的腳本變數");

								} else {
									cmd = cmd.replace(varKey, varMap.get(varKey));
									newVO.setScriptContent(cmd);
								}
							}
						}
					}

					retScriptList.add(newVO);
				}
			}
		}

		return retScriptList;
	}

	@Override
	public ScriptServiceVO findDefaultScriptInfoByScriptTypeAndSystemVersion(String scriptType, String systemVersion) throws ServiceLayerException {
		ScriptServiceVO retVO = null;
		try {
			ScriptInfo scriptInfo = scriptInfoDAO.findDefaultScriptInfoByScriptTypeAndSystemVersion(scriptType, systemVersion);

			if (scriptInfo == null) {
				if (!StringUtils.equals(systemVersion, Constants.DATA_STAR_SYMBOL)) {
					return findDefaultScriptInfoByScriptTypeAndSystemVersion(scriptType, Constants.DATA_STAR_SYMBOL);
				}

			} else {
				retVO = new ScriptServiceVO();
				BeanUtils.copyProperties(scriptInfo, retVO);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retVO;
	}

	private ScriptInfoDAOVO transServiceVO2ScriptInfoDAOVO(ScriptServiceVO ssVO) {
		ScriptInfoDAOVO siDAOVO = new ScriptInfoDAOVO();
		BeanUtils.copyProperties(ssVO, siDAOVO);
		return siDAOVO;
	}

	@Override
	public long countScriptInfo(ScriptServiceVO ssVO) throws ServiceLayerException {
		long retCount = 0;
		ScriptInfoDAOVO siDAOVO;
		try {
			siDAOVO = transServiceVO2ScriptInfoDAOVO(ssVO);

			retCount = scriptInfoDAO.countScriptInfo(siDAOVO);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢異常，請重新操作");
		}

		return retCount;
	}

	@Override
	public List<ScriptServiceVO> findScriptInfo(ScriptServiceVO ssVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		List<ScriptServiceVO> retList = new ArrayList<>();
		ScriptInfoDAOVO siDAOVO;
		try {
			siDAOVO = transServiceVO2ScriptInfoDAOVO(ssVO);

			List<ScriptInfo> entities = scriptInfoDAO.findScriptInfo(siDAOVO, startRow, pageLength);

			if (entities != null && !entities.isEmpty()) {
				ScriptServiceVO vo;
				for (ScriptInfo entity : entities) {
					vo = new ScriptServiceVO();
					BeanUtils.copyProperties(entity, vo);
					vo.setActionScript(StringUtils.replace(entity.getActionScript(), "\r\n", "<br>"));
					vo.setScriptTypeName(entity.getScriptType().getScriptTypeName());
					vo.setCreateTimeStr(Constants.FORMAT_YYYYMMDD_HH24MISS.format(entity.getCreateTime()));
					vo.setUpdateTimeStr(Constants.FORMAT_YYYYMMDD_HH24MISS.format(entity.getUpdateTime()));
					vo.setEnableModify(Env.ENABLE_CM_SCRIPT_MODIFY);

					retList.add(vo);
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢異常，請重新操作");
		}

		return retList;
	}

	@Override
	public ScriptServiceVO getScriptInfoByScriptInfoId(String scriptInfoId) throws ServiceLayerException {
		ScriptServiceVO retVO = new ScriptServiceVO();
		try {
			ScriptInfo entity = scriptInfoDAO.findScriptInfoByIdOrCode(scriptInfoId, null);

			if (entity != null) {
				retVO.setScriptName(entity.getScriptName());
				retVO.setActionScript(entity.getActionScript());
				retVO.setCheckScript(entity.getCheckScript());

			} else {
				throw new ServiceLayerException("查無此腳本內容，請重新操作");
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢腳本內容異常，請重新操作");
		}
		return retVO;
	}
}
