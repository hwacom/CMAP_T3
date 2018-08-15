package com.cmap.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.dao.SysConfigSettingDAO;
import com.cmap.dao.vo.SysConfigSettingDAOVO;
import com.cmap.model.SysConfigSetting;

@Repository
@Transactional
public class SysConfigSettingDAOImpl extends BaseDaoHibernate implements SysConfigSettingDAO {
	@Log
	private static Logger log;
	
	@Override
	public SysConfigSetting findSysConfigSettingById(String settingId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysConfigSetting scs ")
		  .append(" where 1=1 ")
		  .append(" and deleteFlag = '"+Constants.DATA_MARK_NOT_DELETE+"' ")
		  .append(" and scs.settingId = :settingId ");
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("settingId", settingId);
	    
	    return (q.list() != null && !q.list().isEmpty()) ? (SysConfigSetting) q.list().get(0) : null;
	}
	
	@Override
	public List<SysConfigSetting> findSysConfigSettingByIds(List<String> settingIds) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysConfigSetting scs ")
		  .append(" where 1=1 ")
		  .append(" and deleteFlag = '"+Constants.DATA_MARK_NOT_DELETE+"' ")
		  .append(" and scs.settingId in (:settingIds) ");
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("settingIds", settingIds);
	    
	    return (List<SysConfigSetting>)q.list();
	}
	
	@Override
	public List<SysConfigSetting> findSysConfigSettingByName(List<String> settingNames) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysConfigSetting scs ")
		  .append(" where 1=1 ")
//		  .append(" and deleteFlag = '"+Constants.DATA_MARK_NOT_DELETE+"' ")
		  .append(" and scs.settingName in (:settingNames) ");
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("settingNames", settingNames);
	    
	    return (List<SysConfigSetting>) q.list();
	}

	@Override
	public List<SysConfigSetting> findAllSysConfigSetting(Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysConfigSetting scs ")
		  .append(" where 1=1 ")
		  .append(" and deleteFlag = '"+Constants.DATA_MARK_NOT_DELETE+"' ");
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
	    if (startRow != null && pageLength != null) {
	    	q.setFirstResult(startRow);
		    q.setMaxResults(pageLength);
	    }
	    
	    return (List<SysConfigSetting>) q.list();
	}

	@Override
	public void saveSysConfigSetting(SysConfigSetting model) {
		getHibernateTemplate().saveOrUpdate(model);
	}

	@Override
	public Integer deleteSysConfigSetting(List<String> settingIds, String actionBy) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysConfigSetting scs ")
		  .append(" where 1=1 ")
		  .append(" and scs.settingId in (:settingIds) ");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("settingIds", settingIds);
		
	    List<SysConfigSetting> modelList = (List<SysConfigSetting>)q.list();
	    
	    Integer successCount = 0;
	    if (modelList != null && !modelList.isEmpty()) {
	    	for (SysConfigSetting model : modelList) {
	    		try {
	    			model.setDeleteFlag(MARK_AS_DELETE);
		    		model.setUpdateBy(actionBy);
		    		model.setUpdateTime(new Timestamp(new Date().getTime()));
		    		model.setDeleteBy(actionBy);
		    		model.setDeleteTime(new Timestamp(new Date().getTime()));
		    		
		    		getHibernateTemplate().saveOrUpdate(model);
		    		successCount++;
		    		
	    		} catch (Exception e) {
	    			log.error(e.toString(), e);
	    			e.printStackTrace();
	    			
	    			continue;
	    		}
	    	}
	    }
	    
		return successCount;
	}

	@Override
	public List<SysConfigSetting> findSysConfigSettingByVO(SysConfigSettingDAOVO daovo, Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from SysConfigSetting scs ")
		  .append(" where 1=1 ")
		  .append(" and deleteFlag = '"+Constants.DATA_MARK_NOT_DELETE+"' ");
		
		if (StringUtils.isNotBlank(daovo.getSettingName())) {
			sb.append(" and scs.settingName like :settingName ");
		}
		if (StringUtils.isNotBlank(daovo.getSettingValue())) {
			sb.append(" and scs.settingValue like :settingValue ");
		}
		if (StringUtils.isNotBlank(daovo.getSettingRemark())) {
			sb.append(" and scs.settingRemark like :settingRemark ");
		}
		
		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       scs.settingName like :searchValue ")
			  .append("       or ")
			  .append("       scs.settingValue like :searchValue ")
			  .append("       or ")
			  .append("       scs.settingRemark like :searchValue ")
			  .append("       or ")
			  .append("       scs.createBy like :searchValue ")
			  .append("       or ")
			  .append("       scs.updateBy like :searchValue ")
			  .append("     ) ");
		}
		
		if (StringUtils.isNotBlank(daovo.getOrderColumn())) {
			sb.append(" order by scs.").append(daovo.getOrderColumn()).append(" ").append(daovo.getOrderDirection());
			
		} else {
			sb.append(" order by scs.settingName asc, scs.settingValue asc, scs.settingRemark asc ");
		}
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
	    if (startRow != null && pageLength != null) {
	    	q.setFirstResult(startRow);
		    q.setMaxResults(pageLength);
	    }
	    
	    if (StringUtils.isNotBlank(daovo.getSettingName())) {
	    	q.setParameter("settingName", "%".concat(daovo.getSettingName()).concat("%"));
		}
		if (StringUtils.isNotBlank(daovo.getSettingValue())) {
			q.setParameter("settingValue", "%".concat(daovo.getSettingValue()).concat("%"));
		}
		if (StringUtils.isNotBlank(daovo.getSettingRemark())) {
			q.setParameter("settingRemark", "%".concat(daovo.getSettingRemark()).concat("%"));
		}
	    if (StringUtils.isNotBlank(daovo.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(daovo.getSearchValue()).concat("%"));
	    }
	    
		return (List<SysConfigSetting>)q.list();
	}

	@Override
	public long countSysConfigSettingByVO(SysConfigSettingDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(scs.settingId) ")
		  .append(" from SysConfigSetting scs ")
		  .append(" where 1=1 ")
		  .append(" and deleteFlag = '"+Constants.DATA_MARK_NOT_DELETE+"' ");
		
		if (StringUtils.isNotBlank(daovo.getSettingName())) {
			sb.append(" and scs.settingName like :settingName ");
		}
		if (StringUtils.isNotBlank(daovo.getSettingValue())) {
			sb.append(" and scs.settingValue like :settingValue ");
		}
		if (StringUtils.isNotBlank(daovo.getSettingRemark())) {
			sb.append(" and scs.settingRemark like :settingRemark ");
		}
		
		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       scs.settingName like :searchValue ")
			  .append("       or ")
			  .append("       scs.settingValue like :searchValue ")
			  .append("       or ")
			  .append("       scs.settingRemark like :searchValue ")
			  .append("       or ")
			  .append("       scs.createBy like :searchValue ")
			  .append("       or ")
			  .append("       scs.updateBy like :searchValue ")
			  .append("     ) ");
		}
		
	    Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
	    if (StringUtils.isNotBlank(daovo.getSettingName())) {
	    	q.setParameter("settingName", "%".concat(daovo.getSettingName()).concat("%"));
		}
		if (StringUtils.isNotBlank(daovo.getSettingValue())) {
			q.setParameter("settingValue", "%".concat(daovo.getSettingValue()).concat("%"));
		}
		if (StringUtils.isNotBlank(daovo.getSettingRemark())) {
			q.setParameter("settingRemark", "%".concat(daovo.getSettingRemark()).concat("%"));
		}
	    if (StringUtils.isNotBlank(daovo.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(daovo.getSearchValue()).concat("%"));
	    }
	    
		return DataAccessUtils.longResult(q.list());
	}
}
