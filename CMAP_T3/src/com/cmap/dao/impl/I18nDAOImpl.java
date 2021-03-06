package com.cmap.dao.impl;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.dao.I18nDAO;
import com.cmap.model.I18n;

@Repository
@Transactional
public class I18nDAOImpl extends BaseDaoHibernate implements I18nDAO {

	@Override
	public I18n findI18n(String key, String locale){
		StringBuffer sb = new StringBuffer();
		sb.append(" from I18n i ")
		  .append(" where 1=1 ")
		  .append(" and i.type = :key ")
		  .append(" and i.sort = :locale ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("key", key);
        q.setParameter("locale", locale);
		return (I18n)q.uniqueResult();
	}

	@Override
	public List<I18n> listI18n(){
		StringBuffer sb = new StringBuffer();
		sb.append(" from I18n i where 1=1 ");
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    return (List<I18n>) q.list();
	}
}
