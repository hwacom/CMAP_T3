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
import com.cmap.dao.ScriptListDAO;
import com.cmap.dao.vo.ScriptListDAOVO;
import com.cmap.model.ScriptDefaultMapping;
import com.cmap.model.ScriptListDefault;

@Repository("scriptListDefaultDAOImpl")
@Transactional
public class ScriptListDefaultDAOImpl extends BaseDaoHibernate implements ScriptListDAO {

	private List<ScriptListDAOVO> transModel2DAOVO(List<ScriptListDefault> modelList) {
		List<ScriptListDAOVO> voList = new ArrayList<ScriptListDAOVO>();

		ScriptListDAOVO daovo;
		for (ScriptListDefault model : modelList) {
			daovo = new ScriptListDAOVO();
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
	public List<ScriptListDAOVO> findScriptListByScriptCode(String scriptCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select sld ")
		.append(" from ScriptListDefault sld ")
		.append(" where 1=1 ")
		.append(" and sld.scriptCode = :scriptCode ")
		.append(" order by scriptStepOrder asc ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("scriptCode", scriptCode);

		return transModel2DAOVO((List<ScriptListDefault>)q.list());
	}

	@Override
	public String findDefaultScriptCodeBySystemVersion(ScriptType scriptType, String systemVersion) {
		String type = "";
		switch (scriptType) {
		case BACKUP:
			type = "BACKUP";
			break;

		case RECOVERY:
			type = "RECOVERY";
			break;
		}

		StringBuffer sb = new StringBuffer();
		sb.append(" select sdm ")
		.append(" from ScriptDefaultMapping sdm ")
		.append(" where 1=1 ")
		.append(" and sdm.scriptType = :scriptType ")
		.append(" and sdm.systemVersion = :systemVersion ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("scriptType", type);
		q.setParameter("systemVersion", StringUtils.isBlank(systemVersion) ? "*" : systemVersion);

		List<ScriptDefaultMapping> entity = (List<ScriptDefaultMapping>)q.list();
		String scriptCode = entity != null && !entity.isEmpty() ? entity.get(0).getDefaultScriptCode() : null;

		return scriptCode;
	}

	@Override
	public long countScriptList(ScriptListDAOVO slDAOVO) {
		// TODO 自動產生的方法 Stub
		return 0;
	}

}
