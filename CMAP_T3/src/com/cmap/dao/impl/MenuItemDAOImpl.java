package com.cmap.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.dao.MenuItemDAO;
import com.cmap.model.MenuItem;

@Repository("menuItemDAOImpl")
@Transactional
public class MenuItemDAOImpl extends BaseDaoHibernate implements MenuItemDAO {

	@Override
	public List<MenuItem> findMenuItemByMenuCode(String menuCode) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" from MenuItem mi where 1=1 ")
		  .append(" and mi.menuCode = :menuCode ")
		  .append(" and mi.deleteFlag = :deleteFlag ")
		  .append(" order by mi.optionOrder asc ");
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("menuCode", menuCode);
	    q.setParameter("deleteFlag", Constants.DATA_MARK_NOT_DELETE);
	    
		return (List<MenuItem>)q.list();
	}

}
