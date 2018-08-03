package com.cmap.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.ScriptListDAO;
import com.cmap.dao.vo.ScriptListDAOVO;

@Repository("scriptListDefaultDAOImpl")
@Transactional
public class ScriptListDefaultDAOImpl extends BaseDaoHibernate implements ScriptListDAO {

	private <T> List<ScriptListDAOVO> transModel2DAOVO(List<T> modelList) {
		List<ScriptListDAOVO> voList = new ArrayList<ScriptListDAOVO>();
		
		ScriptListDAOVO daovo;
		for (T model : modelList) {
			daovo = new ScriptListDAOVO();
			BeanUtils.copyProperties(model, daovo);
			voList.add(daovo);
		}
		
		return voList;
	}
	
	@Override
	public List<ScriptListDAOVO> findScriptListByScriptCode(String scriptCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select sld ")
		  .append(" from ScriptListDefault sld ")
		  .append(" where 1=1 ")
		  .append(" and sld.scriptCode = :scriptCode ")
		  .append(" order by scriptStepOrder asc ");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("scriptCode", scriptCode);
	    
		return transModel2DAOVO(q.list());
	}

}
