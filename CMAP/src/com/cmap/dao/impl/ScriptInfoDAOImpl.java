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
		if (StringUtils.isNotBlank(daovo.getQueryScriptInfoId())) {
			sb.append(" and si.scriptInfoId = :scriptInfoId ");
		}
		if (StringUtils.isNotBlank(daovo.getQuerySystemDefault())) {
			sb.append(" and si.systemDefault = :systemDefault ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			q.setParameter("scriptTypeId", daovo.getQueryScriptTypeId());
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptInfoId())) {
			q.setParameter("scriptInfoId", daovo.getQueryScriptInfoId());
		}
		if (StringUtils.isNotBlank(daovo.getQuerySystemDefault())) {
			q.setParameter("systemDefault", daovo.getQuerySystemDefault());
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
		if (StringUtils.isNotBlank(daovo.getQueryScriptInfoId())) {
			sb.append(" and si.scriptInfoId = :scriptInfoId ");
		}
		if (StringUtils.isNotBlank(daovo.getQuerySystemDefault())) {
			sb.append(" and si.systemDefault = :systemDefault ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			q.setParameter("scriptTypeId", daovo.getQueryScriptTypeId());
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptInfoId())) {
			q.setParameter("scriptInfoId", daovo.getQueryScriptInfoId());
		}
		if (StringUtils.isNotBlank(daovo.getQuerySystemDefault())) {
			q.setParameter("systemDefault", daovo.getQuerySystemDefault());
		}

		return (List<ScriptInfo>)q.list();
	}

	@Override
	public ScriptInfo findScriptInfoById(String scriptInfoId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ScriptInfo si ")
		  .append(" where 1=1 ")
		  .append(" and si.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and si.scriptInfoId = :scriptInfoId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("scriptInfoId", scriptInfoId);

		List<ScriptInfo> retList = (List<ScriptInfo>)q.list();
		return (retList != null && !retList.isEmpty()) ? retList.get(0) : null;
	}
}
