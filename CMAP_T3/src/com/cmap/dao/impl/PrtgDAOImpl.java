package com.cmap.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.PrtgDAO;
import com.cmap.model.PrtgAccountMapping;

@Repository("prtgDAO")
@Transactional
public class PrtgDAOImpl extends BaseDaoHibernate implements PrtgDAO {

	@Override
	public PrtgAccountMapping findPrtgAccountMappingBySourceId(String sourceId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from PrtgAccountMapping pam ")
		  .append(" where 1=1 ")
		  .append(" and pam.sourceId = :sourceId ");

	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("sourceId", sourceId);

	    List<PrtgAccountMapping> entities = (List<PrtgAccountMapping>)q.list();
	    if (entities == null || (entities != null && entities.isEmpty())) {
	    	return null;
	    } else {
	    	return entities.get(0);
	    }
	}
}
