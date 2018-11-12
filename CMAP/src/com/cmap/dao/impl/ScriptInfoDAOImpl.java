package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.dao.ScriptInfoDAO;
import com.cmap.dao.vo.ScriptInfoDAOVO;
import com.cmap.model.ScriptInfo;

@Repository
@Transactional
public class ScriptInfoDAOImpl extends BaseDaoHibernate implements ScriptInfoDAO {

	@Override
	public long countScriptInfo(ScriptInfoDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(si.scriptInfoId) ")
		  .append(" from ScriptInfo si ")
		  .append(" where 1=1 ")
		  .append(" and si.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			sb.append(" and si.scriptType.scriptTypeId = :scriptTypeId ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			q.setParameter("scriptTypeId", daovo.getQueryScriptTypeId());
		}

		return DataAccessUtils.longResult(q.list());
	}

	@Override
	public List<ScriptInfo> findScriptInfo(ScriptInfoDAOVO daovo, Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ScriptInfo si ")
		  .append(" where 1=1 ")
		  .append(" and si.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			sb.append(" and si.scriptType.scriptTypeId = :scriptTypeId ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			q.setParameter("scriptTypeId", daovo.getQueryScriptTypeId());
		}

		return (List<ScriptInfo>)q.list();
	}

}
