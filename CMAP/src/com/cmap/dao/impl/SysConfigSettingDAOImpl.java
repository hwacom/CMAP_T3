package com.cmap.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.SysConfigSettingDAO;
import com.cmap.model.SysConfigSetting;

@Repository
@Transactional
public class SysConfigSettingDAOImpl extends BaseDaoHibernate implements SysConfigSettingDAO {

	@Override
	public List<SysConfigSetting> findSysConfigSettingByName(List<String> settingNames) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysConfigSetting scs where 1=1 ");
		sb.append(" and scs.settingName in (:settingNames) ");
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("settingNames", settingNames);
	    
	    return (List<SysConfigSetting>) q.list();
	}

	@Override
	public List<SysConfigSetting> findAllSysConfigSetting() {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysConfigSetting scs where 1=1 ");
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
	    return (List<SysConfigSetting>) q.list();
	}

	@Override
	public void addSysConfigSetting(List<SysConfigSetting> models) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int updateSysConfigSetting(List<SysConfigSetting> models) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteSysConfigSetting(List<SysConfigSetting> models) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
