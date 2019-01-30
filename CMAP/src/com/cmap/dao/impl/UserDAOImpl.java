package com.cmap.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.UserDAO;
import com.cmap.model.UserRightSetting;

@Repository("userDAO")
@Transactional
public class UserDAOImpl extends BaseDaoHibernate implements UserDAO {

	@Override
	public UserRightSetting findUserRightSetting(String belongGroup, String account) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from UserRightSetting urs ")
		  .append(" where 1=1 ")
		  .append(" and urs.belongGroup = :belongGroup ")
		  .append(" and urs.account = :account ");

	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("belongGroup", belongGroup);
	    q.setParameter("account", account);

	    List<UserRightSetting> entities = (List<UserRightSetting>)q.list();
	    if (entities == null || (entities != null && entities.isEmpty())) {
	    	return null;
	    } else {
	    	return entities.get(0);
	    }
	}

}
