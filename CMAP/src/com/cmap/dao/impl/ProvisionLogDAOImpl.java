package com.cmap.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.ProvisionLogDAO;
import com.cmap.dao.vo.ProvisionLogDAOVO;
import com.cmap.model.ProvisionAccessLog;
import com.cmap.model.ProvisionLogDetail;
import com.cmap.model.ProvisionLogDevice;
import com.cmap.model.ProvisionLogMaster;
import com.cmap.model.ProvisionLogRetry;
import com.cmap.model.ProvisionLogStep;

@Repository("provisionLogDAOImpl")
@Transactional
public class ProvisionLogDAOImpl extends BaseDaoHibernate implements ProvisionLogDAO {

	@Override
	public long countProvisionLogByDAOVO(ProvisionLogDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(*) ")
		.append(" from ProvisionLogMaster ")
		.append(" where 1=1 ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		return DataAccessUtils.longResult(q.list());
	}

	@Override
	public List<ProvisionLogDetail> findProvisionLogByDAOVO(ProvisionLogDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select plm ")
		.append(" from ProvisionLogMaster plm ")
		.append(" where 1=1 ");

		sb.append(" order by plm.createDate desc, plm.logMasterId desc ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		return (List<ProvisionLogDetail>)q.list();
	}

	@Override
	public void insertProvisionLog(ProvisionLogMaster master, List<ProvisionLogDetail> details, List<ProvisionLogStep> steps,
			List<ProvisionLogDevice> devices, List<ProvisionLogRetry> retrys) {

		if (master != null) {
			getHibernateTemplate().save(master);
		}

		if (details != null) {
			for (ProvisionLogDetail detail : details) {
				getHibernateTemplate().save(detail);
			}
		}

		if (steps != null) {
			for (ProvisionLogStep step : steps) {
				getHibernateTemplate().save(step);
			}
		}

		if (devices != null) {
			for (ProvisionLogDevice device : devices) {
				getHibernateTemplate().save(device);
			}
		}

		if (retrys != null) {
			for (ProvisionLogRetry retry : retrys) {
				getHibernateTemplate().save(retry);
			}
		}
	}

	@Override
	public List<ProvisionAccessLog> findProvisionAccessLogById(String logId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ProvisionAccessLog ")
		  .append(" where 1=1 ")
		  .append(" and logId = :logId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("logId", logId);

		return (List<ProvisionAccessLog>)q.list();
	}
}
