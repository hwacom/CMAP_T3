package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.dao.ScriptTypeDAO;
import com.cmap.model.ScriptType;

@Repository
@Transactional
public class ScriptTypeDAOImpl extends BaseDaoHibernate implements ScriptTypeDAO {

	@Override
	public List<ScriptType> findScriptTypeByDefaultFlag(String defaultFlag) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select st ")
		  .append(" from ScriptType st ")
		  .append(" where 1=1 ")
		  .append(" and st.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(defaultFlag)) {
			sb.append(" and st.defaultFlag = :defaultFlag ");
		}

		sb.append(" order by st.scriptTypeCode ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());

	    if (StringUtils.isNotBlank(defaultFlag)) {
	    	 q.setParameter("defaultFlag", defaultFlag);
	    }

		return (List<ScriptType>)q.list();
	}

}
