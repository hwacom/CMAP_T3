package com.cmap.plugin.module.vmswitch;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.dao.impl.BaseDaoHibernate;

@Repository("vmSwitchDAOImpl")
@Transactional
public class VmSwitchDAOImpl extends BaseDaoHibernate implements VmSwitchDAO {

	@Override
	public ModuleVmNameMapping findVmNameMappingInfoByApiVmName(String apiVmName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select mvnm ")
		  .append(" from ModuleVmNameMapping mvnm ")
		  /*
		  .append("     ,ModuleVmNameMappingDetail mvnmd ")
		  .append("     ,ModuleVmEsxiSetting mves ")
		  */
		  .append(" where 1=1 ");
		  /*
		  .append(" and mvnm.mappingId = mvnmd.mappingId ")
		  .append(" and mvnmd.esxiSettingId = mves.settingId ");
		  */

		if (StringUtils.isNotBlank(apiVmName)) {
			sb.append(" and mvnm.nameOfApi = :apiVmName ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());

	    if (StringUtils.isNotBlank(apiVmName)) {
	    	q.setParameter("apiVmName", apiVmName);
		}

	    List<ModuleVmNameMapping> reList = (List<ModuleVmNameMapping>)q.list();

	    if (reList == null || (reList != null && reList.isEmpty())) {
	    	return null;

	    } else {
	    	return (ModuleVmNameMapping)q.list().get(0);
	    }
	}

	@Override
	public List<ModuleVmEsxiSetting> findAllVmEsxiSetting() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select mves ")
		  .append(" from ModuleVmEsxiSetting mves ")
		  .append(" where 1=1 ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());

		return (List<ModuleVmEsxiSetting>)q.list();
	}

    @Override
    public void saveOrUpdateProcessLog(ModuleVmProcessLog moduleVmProcessLog) {
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        session.saveOrUpdate(moduleVmProcessLog);
        session.close();
    }

    @Override
    public List<ModuleVmProcessLog> findModuleVmProcessLogByLogKey(String logKey) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mvpl ")
          .append(" from ModuleVmProcessLog mvpl ")
          .append(" where 1=1 ")
          .append(" and mvpl.logKey = :logKey ")
          .append(" order by mvpl.orderNo ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("logKey", logKey);

        return (List<ModuleVmProcessLog>)q.list();
    }

    @Override
    public List<ModuleVmProcessLog> findNotPushedModuleVmProcessLogByLogKey(String logKey) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mvpl ")
          .append(" from ModuleVmProcessLog mvpl ")
          .append(" where 1=1 ")
          .append(" and mvpl.logKey = :logKey ")
          .append(" and mvpl.pushed = 'N' ")
          .append(" order by mvpl.orderNo ");

        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("logKey", logKey);

        List<ModuleVmProcessLog> retList = (List<ModuleVmProcessLog>)q.list();

        session.close();
        return retList;
    }

    @Override
    public int updateProcessLog(ModuleVmProcessLog moduleVmProcessLog) {
        StringBuffer sb = new StringBuffer();
        sb.append(" update from ModuleVmProcessLog mves ")
          .append(" set pushed = :pushed, updateBy = :updateBy, updateTime = NOW() ")
          .append(" where 1=1 ")
          .append(" and logId = :logId ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("pushed", moduleVmProcessLog.getPushed());
        q.setParameter("updateBy", moduleVmProcessLog.getUpdateBy());
        q.setParameter("logId", moduleVmProcessLog.getLogId());
        return q.executeUpdate();
    }

    @Override
    public ModuleVmSetting getVmSetting(String settingName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mvs ")
          .append(" from ModuleVmSetting mvs ")
          .append(" where 1=1 ")
          .append(" and mvs.settingName = :settingName ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("settingName", settingName);

        return (ModuleVmSetting)q.uniqueResult();
    }

    @Override
    public void updateVmSetting(ModuleVmSetting entity) {
        getHibernateTemplate().getSessionFactory().getCurrentSession().saveOrUpdate(entity);
    }
}
