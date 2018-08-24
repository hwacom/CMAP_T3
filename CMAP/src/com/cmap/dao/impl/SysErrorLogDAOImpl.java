package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.SysErrorLogDAO;
import com.cmap.dao.vo.SysLogDAOVO;
import com.cmap.model.SysErrorLog;

@Repository("sysErrorLogDAO")
@Transactional
public class SysErrorLogDAOImpl extends BaseDaoHibernate implements SysErrorLogDAO {

	@Override
	public long countSysErrorLogByDAOVO(SysLogDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(sel.logId) ")
		.append(" from SysErrorLog sel ")
		.append(" where 1=1 ");

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			.append("       sel.logger like :searchValue ")
			.append("       or ")
			.append("       sel.logLevel like :searchValue ")
			.append("       or ")
			.append("       sel.message like :searchValue ")
			.append("       or ")
			.append("       sel.exception like :searchValue ")
			.append("     ) ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			q.setParameter("searchValue", "%".concat(daovo.getSearchValue()).concat("%"));
		}

		return DataAccessUtils.longResult(q.list());
	}

	@Override
	public List<SysErrorLog> findSysErrorLogByDAOVO(SysLogDAOVO daovo, Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysErrorLog sel ")
		.append(" where 1=1 ");

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			.append("       sel.logger like :searchValue ")
			.append("       or ")
			.append("       sel.logLevel like :searchValue ")
			.append("       or ")
			.append("       sel.message like :searchValue ")
			.append("       or ")
			.append("       sel.exception like :searchValue ")
			.append("     ) ");
		}

		if (StringUtils.isNotBlank(daovo.getOrderColumn())) {
			sb.append(" order by sel.").append(daovo.getOrderColumn()).append(" ").append(daovo.getOrderDirection());

		} else {
			sb.append(" order by sel.entryDate desc, sel.logLevel desc, sel.logger asc ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (startRow != null && pageLength != null) {
			q.setFirstResult(startRow);
			q.setMaxResults(pageLength);
		}

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			q.setParameter("searchValue", "%".concat(daovo.getSearchValue()).concat("%"));
		}

		return (List<SysErrorLog>)q.list();
	}

	@Override
	public SysErrorLog findSysErrorLogByLogId(String logId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysErrorLog sel ")
		.append(" where 1=1 ")
		.append(" and sel.logId = :logId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("logId", logId);

		return q.list() != null && !q.list().isEmpty() ? (SysErrorLog)q.list().get(0) : null;
	}

}
