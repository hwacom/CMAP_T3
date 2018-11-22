package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.dao.ScriptStepDAO;
import com.cmap.dao.vo.ScriptDAOVO;

@Repository("scriptStepActionDAOImpl")
@Transactional
public class ScriptStepActionDAOImpl extends ScriptStepDAOImpl implements ScriptStepDAO {

	@Override
	public List<ScriptDAOVO> findScriptStepByScriptInfoIdOrScriptCode(String scriptInfoId, String scriptCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ScriptStepAction ssa ")
		  .append(" where 1=1 ")
		  .append(" and ssa.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(scriptInfoId)) {
			sb.append(" and ssa.scriptInfo.scriptInfoId = :scriptInfoId ");
		}
		if (StringUtils.isNotBlank(scriptCode)) {
			sb.append(" and ssa.scriptInfo.scriptCode = :scriptCode ");
		}

		sb.append(" order by ssa.stepOrder ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(scriptInfoId)) {
			q.setParameter("scriptInfoId", scriptInfoId);
		}
		if (StringUtils.isNotBlank(scriptCode)) {
			q.setParameter("scriptCode", scriptCode);
		}

		return transModel2DAOVO(q.list());
	}

}
