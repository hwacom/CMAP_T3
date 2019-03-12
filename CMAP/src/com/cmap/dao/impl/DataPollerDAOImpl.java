package com.cmap.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.dao.DataPollerDAO;
import com.cmap.model.DataPollerMapping;
import com.cmap.model.DataPollerScriptSetting;
import com.cmap.model.DataPollerSetting;
import com.cmap.model.DataPollerTargetSetting;

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


	@Override
	public List<DataPollerMapping> findDataPollerMappingByMappingCode(String mappingCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DataPollerMapping dpm ")
		  .append(" where 1=1 ")
		  .append(" and dpm.mappingCode = :mappingCode ")
		  .append(" order by dpm.mappingId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("mappingCode", mappingCode);

	    List<DataPollerMapping> retList = (List<DataPollerMapping>)q.list();
		return retList;
	}

	@Override
	public List<DataPollerSetting> findDataPollerSettingByDataType(String dataType) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DataPollerSetting dps ")
		  .append(" where 1=1 ")
		  .append(" and dps.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and dps.dataType = :dataType ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("dataType", dataType);

	    List<DataPollerSetting> retList = (List<DataPollerSetting>)q.list();
	    return retList;
	}

	@Override
	public DataPollerSetting findDataPollerSettingByDataTypeAndQueryId(String dataType, String queryId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DataPollerSetting dps ")
		  .append(" where 1=1 ")
		  .append(" and dps.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and dps.dataType = :dataType ")
		  .append(" and dps.queryId = :queryId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("dataType", dataType);
	    q.setParameter("queryId", queryId);

	    List<DataPollerSetting> retList = (List<DataPollerSetting>)q.list();

	    if (retList != null && !retList.isEmpty()) {
	    	return retList.get(0);
	    } else {
	    	return null;
	    }
	}

	@Override
	public List<DataPollerTargetSetting> findDataPollerTargetSettingByTargetSettingCode(String targetSettingCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DataPollerTargetSetting dpts ")
		  .append(" where 1=1 ")
		  .append(" and dpts.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and dpts.targetSettingCode = :targetSettingCode ")
		  .append(" order by dpts.orderNo ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("targetSettingCode", targetSettingCode);

	    List<DataPollerTargetSetting> retList = (List<DataPollerTargetSetting>)q.list();
	    return retList;
	}

	@Override
	public List<DataPollerScriptSetting> findDataPollerScriptSettingByScriptSettingCode(String scriptSettingCode) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DataPollerScriptSetting dpss ")
		  .append(" where 1=1 ")
		  .append(" and dpss.scriptSettingCode = :scriptSettingCode ")
		  .append(" order by dpss.executeOrder ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("scriptSettingCode", scriptSettingCode);

	    List<DataPollerScriptSetting> retList = (List<DataPollerScriptSetting>)q.list();
	    return retList;
	}
}
