package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.SysJobLogDAO;
import com.cmap.dao.vo.SysLogDAOVO;
import com.cmap.model.SysJobLog;

@Repository("sysJobLogDAO")
@Transactional
public class SysJobLogDAOImpl extends BaseDaoHibernate implements SysJobLogDAO {

	@Override
	public long countSysJobLogByDAOVO(SysLogDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(sjl.logId) ")
		.append(" from SysJobLog sjl ")
		.append(" where 1=1 ");

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			.append("       sjl.schedName like :searchValue ")
			.append("       or ")
			.append("       sjl.triggerName like :searchValue ")
			.append("       or ")
			.append("       sjl.triggerGroup like :searchValue ")
			.append("       or ")
			.append("       sjl.jobName like :searchValue ")
			.append("       or ")
			.append("       sjl.jobGroup like :searchValue ")
			.append("       or ")
			.append("       sjl.result like :searchValue ")
			.append("       or ")
			.append("       sjl.recordsNum like :searchValue ")
			.append("       or ")
			.append("       sjl.cronExpression like :searchValue ")
			.append("       or ")
			.append("       sjl.jobClassName like :searchValue ")
			.append("       or ")
			.append("       sjl.remark like :searchValue ")
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
	public List<SysJobLog> findSysJobLogByDAOVO(SysLogDAOVO daovo, Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysJobLog sjl ")
		.append(" where 1=1 ");

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			.append("       sjl.schedName like :searchValue ")
			.append("       or ")
			.append("       sjl.triggerName like :searchValue ")
			.append("       or ")
			.append("       sjl.triggerGroup like :searchValue ")
			.append("       or ")
			.append("       sjl.jobName like :searchValue ")
			.append("       or ")
			.append("       sjl.jobGroup like :searchValue ")
			.append("       or ")
			.append("       sjl.result like :searchValue ")
			.append("       or ")
			.append("       sjl.recordsNum like :searchValue ")
			.append("       or ")
			.append("       sjl.cronExpression like :searchValue ")
			.append("       or ")
			.append("       sjl.jobClassName like :searchValue ")
			.append("       or ")
			.append("       sjl.remark like :searchValue ")
			.append("     ) ");
		}

		if (StringUtils.isNotBlank(daovo.getOrderColumn())) {
			sb.append(" order by sjl.").append(daovo.getOrderColumn()).append(" ").append(daovo.getOrderDirection());

		} else {
			sb.append(" order by sjl.startTime desc, sjl.result asc, sjl.schedName desc, sjl.triggerName desc, sjl.triggerGroup desc, sjl.jobName desc, sjl.jobGroup desc, sjl.jobClassName desc ");
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

		return (List<SysJobLog>)q.list();
	}

	@Override
	public SysJobLog findSysJobLogByLogId(String logId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysJobLog sjl ")
		.append(" where 1=1 ")
		.append(" and sjl.logId = :logId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("logId", logId);

		return q.list() != null && !q.list().isEmpty() ? (SysJobLog)q.list().get(0) : null;
	}

	@Override
	public void insertSysJobLog(SysJobLog entity) {
		getHibernateTemplate().save(entity);
	}
}
