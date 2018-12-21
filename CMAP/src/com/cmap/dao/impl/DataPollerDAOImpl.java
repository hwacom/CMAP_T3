package com.cmap.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.dao.DataPollerDAO;
import com.cmap.model.DataPollerMapping;
import com.cmap.model.DataPollerSetting;

@Repository("dataPollerDAO")
@Transactional
public class DataPollerDAOImpl extends BaseDaoHibernate implements DataPollerDAO {

	@Override
	public DataPollerSetting findDataPollerSettingBySettingId(String settingId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DataPollerSetting dps ")
		  .append(" where 1=1 ")
		  .append(" and dps.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and dps.settingId = :settingId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("settingId", settingId);

	    List<DataPollerSetting> retList = (List<DataPollerSetting>)q.list();

	    if (retList != null && !retList.isEmpty()) {
	    	return retList.get(0);
	    } else {
	    	return null;
	    }
	}

	@Override
	public DataPollerMapping findDataPollerMappingBySettingIdAndSourceColumnName(String settingId, String sourceColumnName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DataPollerMapping dpm ")
		  .append(" where 1=1 ")
		  .append(" and dpm.settingId = :settingId ")
		  .append(" and dpm.sourceColumnName = :sourceColumnName ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("settingId", settingId);
	    q.setParameter("sourceColumnName", sourceColumnName);

	    List<DataPollerMapping> retList = (List<DataPollerMapping>)q.list();

	    if (retList != null && !retList.isEmpty()) {
	    	return retList.get(0);
	    } else {
	    	return null;
	    }
	}

	@Override
	public List<String> findTargetTableName(String settingId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct dpm.target_Table_Name ")
		  .append(" from Data_Poller_Setting dps ")
		  .append("     ,Data_Poller_Mapping dpm ")
		  .append(" where 1=1 ")
		  .append(" and dps.setting_id = dpm.setting_id ")
		  .append(" and dps.delete_Flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and dps.setting_Id = :settingId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());
	    q.setParameter("settingId", settingId);

	    return (List<String>)q.list();
	}
}
