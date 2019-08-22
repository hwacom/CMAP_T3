package com.cmap.dao.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.comm.enums.ScriptType;
import com.cmap.dao.ScriptDefaultMappingDAO;
import com.cmap.dao.vo.ScriptDAOVO;
import com.cmap.model.ScriptDefaultMapping;
import com.cmap.model.ScriptListDefault;

@Repository("scriptListDefaultDAOImpl")
@Transactional
public class ScriptDefaultMappingDAOImpl extends BaseDaoHibernate implements ScriptDefaultMappingDAO {

	private List<ScriptDAOVO> transModel2DAOVO(List<ScriptListDefault> modelList) {
		List<ScriptDAOVO> voList = new ArrayList<>();

		ScriptDAOVO daovo;
		for (ScriptListDefault model : modelList) {
			daovo = new ScriptDAOVO();
			BeanUtils.copyProperties(model, daovo);
			daovo.setScriptTypeId(model.getScriptType().getScriptTypeId());
			daovo.setScriptStepOrder(String.valueOf(model.getScriptStepOrder()));
			daovo.setHeadCuttingLines(model.getHeadCuttingLines() != null ? String.valueOf(model.getHeadCuttingLines()) : null);
			daovo.setTailCuttingLines(model.getTailCuttingLines() != null ? String.valueOf(model.getTailCuttingLines()) : null);
			voList.add(daovo);
		}

		return voList;
	}

	@Override
	public String findDefaultScriptCodeBySystemVersion(ScriptType scriptType, String deviceModel) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select sdm ")
		.append(" from ScriptDefaultMapping sdm ")
		.append(" where 1=1 ")
		.append(" and sdm.scriptType = :scriptType ")
		.append(" and sdm.deviceModel = :deviceModel ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("scriptType", scriptType.toString());
		q.setParameter("deviceModel", StringUtils.isBlank(deviceModel) ? "*" : deviceModel);

		List<ScriptDefaultMapping> entity = (List<ScriptDefaultMapping>)q.list();
		String scriptCode = entity != null && !entity.isEmpty() ? entity.get(0).getDefaultScriptCode() : null;

		return scriptCode;
	}

	@Override
	public long countScriptList(ScriptDAOVO slDAOVO) {
		// TODO 自動產生的方法 Stub
		return 0;
	}

}
