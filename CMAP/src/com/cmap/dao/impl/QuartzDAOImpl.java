package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.QuartzDAO;
import com.cmap.dao.vo.QuartzDAOVO;

@Repository("quartzDAOImpl")
@Transactional
public class QuartzDAOImpl extends BaseDaoHibernate implements QuartzDAO {

	@Override
	public long countQuartzDataByDAOVO(QuartzDAOVO daoVO) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(*) ")
		  .append(" from QrtzTriggers qt, QrtzCronTriggers qct ")
		  .append(" where 1=1 ")
		  .append(" and qt.schedName = qct.schedName ")
		  .append(" and qt.triggerName = qct.triggerName ")
		  .append(" and qt.triggerGroup = qct.triggerGroup ");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
		return DataAccessUtils.longResult(q.list());
	}
	
	@Override
	public List<Object[]> findQuartzDataByDAOVO(QuartzDAOVO daoVO) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select qt, qct, qjd")
		  .append(" from QrtzTriggers qt, QrtzCronTriggers qct, QrtzJobDetails qjd ")
		  .append(" where 1=1 ")
		  .append(" and qt.schedName = qct.schedName ")
		  .append(" and qt.triggerName = qct.triggerName ")
		  .append(" and qt.triggerGroup = qct.triggerGroup ")
		  .append(" and qt.schedName = qjd.schedName ")
		  .append(" and qt.jobName = qjd.jobName ")
		  .append(" and qt.jobGroup = qjd.jobGroup ");
		
		if (daoVO != null && StringUtils.isNotBlank(daoVO.getJobKeyName())) {
			sb.append(" and qt.jobName = :jobName ");
		}
		if (daoVO != null && StringUtils.isNotBlank(daoVO.getJobKeyGroup())) {
			sb.append(" and qt.jobGroup = :jobGroup ");
		}
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
	    if (daoVO != null && StringUtils.isNotBlank(daoVO.getJobKeyName())) {
			q.setParameter("jobName", daoVO.getJobKeyName());
		}
		if (daoVO != null && StringUtils.isNotBlank(daoVO.getJobKeyGroup())) {
			q.setParameter("jobGroup", daoVO.getJobKeyGroup());
		}
	    
		return (List<Object[]>)q.list();
	}
}
