package com.cmap.dao.impl;

import java.util.ArrayList;
import java.util.List;

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

			try {
				scriptInfo = (ScriptInfo)PropertyUtils.getProperty(modelObj, "scriptInfo");
				scriptStepOrder = PropertyUtils.getProperty(modelObj, "stepOrder");
				headCuttingLines = PropertyUtils.getProperty(modelObj, "headCuttingLines");
				tailCuttingLines = PropertyUtils.getProperty(modelObj, "tailCuttingLines");
				outputFlag = PropertyUtils.getProperty(modelObj, "outputFlag");
				scriptContent = PropertyUtils.getProperty(modelObj, "command");
				commandRemark = PropertyUtils.getProperty(modelObj, "commandRemark");

			} catch (Exception e) {
				e.printStackTrace();
			}

			daovo.setScriptTypeId(scriptInfo != null ? scriptInfo.getScriptType().getScriptTypeId() : null);
			daovo.setScriptStepOrder(scriptStepOrder != null ? String.valueOf(scriptStepOrder) : null);
			daovo.setHeadCuttingLines(headCuttingLines != null ? String.valueOf(headCuttingLines) : null);
			daovo.setTailCuttingLines(tailCuttingLines != null ? String.valueOf(tailCuttingLines) : null);
			daovo.setOutput(outputFlag != null ? String.valueOf(outputFlag) : null);
			daovo.setScriptContent(scriptContent != null ? String.valueOf(scriptContent) : null);
			daovo.setScriptName(scriptInfo != null ? scriptInfo.getScriptName() : null);
			daovo.setRemark(commandRemark != null ? String.valueOf(commandRemark) : null);
			daovo.setScriptDescription(scriptInfo != null ? scriptInfo.getScriptDescription() : null);

			voList.add(daovo);
		}

		return voList;
	}
}
