package com.cmap.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.ProtocolDAO;
import com.cmap.model.ProtocolSpec;

@Repository("protocolDAOImpl")
@Transactional
public class ProtocolDAOImpl extends BaseDaoHibernate implements ProtocolDAO {

	@Override
	public List<ProtocolSpec> findAllProtocolSpec() {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ProtocolSpec ps ")
		  .append(" where 1=1 ")
		  .append(" order by ps.protocolNo ");

	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());

		return (List<ProtocolSpec>)q.list();
	}
}
