package com.cmap.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

import com.cmap.dao.vo.ScriptDAOVO;
import com.cmap.model.ScriptInfo;

public class ScriptStepDAOImpl extends BaseDaoHibernate {

	protected List<ScriptDAOVO> transModel2DAOVO(List<? extends Object> modelList) {
		List<ScriptDAOVO> voList = new ArrayList<ScriptDAOVO>();

		ScriptDAOVO daovo;
		for (Object modelObj : modelList) {
			daovo = new ScriptDAOVO();
			BeanUtils.copyProperties(modelObj, daovo);

			ScriptInfo scriptInfo = null;
			Object scriptStepOrder = null;
			Object headCuttingLines = null;
			Object tailCuttingLines = null;
			Object outputFlag = null;
			Object scriptContent = null;
			Object commandRemark = null;
			Object commandDescription = null;

			try {
				scriptInfo = (ScriptInfo)PropertyUtils.getProperty(modelObj, "scriptInfo");
				scriptStepOrder = PropertyUtils.getProperty(modelObj, "stepOrder");
				headCuttingLines = PropertyUtils.getProperty(modelObj, "headCuttingLines");
				tailCuttingLines = PropertyUtils.getProperty(modelObj, "tailCuttingLines");
				outputFlag = PropertyUtils.getProperty(modelObj, "outputFlag");
				scriptContent = PropertyUtils.getProperty(modelObj, "command");
				commandRemark = PropertyUtils.getProperty(modelObj, "commandRemark");
				commandDescription = PropertyUtils.getProperty(modelObj, "commandDescription");

			} catch (Exception e) {
				e.printStackTrace();
			}

			daovo.setScriptTypeId(scriptInfo != null ? scriptInfo.getScriptType().getScriptTypeId() : null);
			daovo.setScriptStepOrder(Objects.toString(scriptStepOrder, null));
			daovo.setHeadCuttingLines(Objects.toString(headCuttingLines, null));
			daovo.setTailCuttingLines(Objects.toString(tailCuttingLines, null));
			daovo.setOutput(Objects.toString(outputFlag, null));
			daovo.setScriptContent(Objects.toString(scriptContent, null));
			daovo.setScriptName(scriptInfo != null ? scriptInfo.getScriptName() : null);
			daovo.setRemark(Objects.toString(commandRemark, null));
			daovo.setScriptDescription(Objects.toString(commandDescription, null));
			daovo.setScriptCode(scriptInfo != null ? scriptInfo.getScriptCode() : null);

			voList.add(daovo);
		}

		return voList;
	}
}
