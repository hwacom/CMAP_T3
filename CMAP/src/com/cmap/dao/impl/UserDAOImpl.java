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
	public List<UserRightSetting> findUserRightSetting(String belongGroup, String[] roles, String account) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from UserRightSetting urs ")
		  .append(" where 1=1 ")
		  .append(" and urs.belongGroup = :belongGroup ");

		if (roles != null) {
			sb.append(" and urs.role in (:roles) ");
		}

		sb.append(" and urs.account = :account ")
		  .append(" order by urs.isAdmin desc, urs.denyAccess asc ");

	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("belongGroup", belongGroup);
	    if (roles != null) {
	    	q.setParameterList("roles", roles);
	    }
	    q.setParameter("account", account);

	    return (List<UserRightSetting>)q.list();
	}

}
