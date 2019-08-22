package com.cmap.plugin.module.clustermigrate;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.dao.impl.BaseDaoHibernate;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Repository("clusterMigrateDAO")
@Transactional
public class ClusterMigrateDAOImpl extends BaseDaoHibernate implements ClusterMigrateDAO {

    @Override
    public List<ModuleClusterMigrateSetting> getClusterMigrateSetting(String settingName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mcss ")
          .append(" from ModuleClusterMigrateSetting mcss ")
          .append(" where 1=1 ")
          .append(" and mcss.settingName = :settingName ")
          .append(" order by mcss.orderNo ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("settingName", settingName);

        return (List<ModuleClusterMigrateSetting>)q.list();
    }

    @Override
    public ModuleClusterMigrateLog findClusterMigrateLogByLogId(Integer logId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mcsl ")
          .append(" from ModuleClusterMigrateLog mcsl ")
          .append(" where 1=1 ")
          .append(" and mcsl.logId = :logId ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("logId", logId);

        return (ModuleClusterMigrateLog)q.uniqueResult();
    }

    @Override
    public List<ModuleClusterMigrateLog> findClusterMigrateLog(
            Integer logId, String dateStr, String migrateFromCluster, List<String> processFlag) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mcsl ")
          .append(" from ModuleClusterMigrateLog mcsl ")
          .append(" where 1=1 ");

        if (logId != null) {
            sb.append(" and mcsl.logId = :logId ");
        }
        if (StringUtils.isNotBlank(dateStr)) {
            sb.append(" and mcsl.dateStr = :dateStr ");
        }
        if (StringUtils.isNotBlank(migrateFromCluster)) {
            sb.append(" and mcsl.migrateFromCluster = :migrateFromCluster ");
        }
        if (processFlag != null && !processFlag.isEmpty()) {
            sb.append(" and mcsl.processFlag in (:processFlag) ");
        }

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());

        if (logId != null) {
            q.setParameter("logId", logId);
        }
        if (StringUtils.isNotBlank(dateStr)) {
            q.setParameter("dateStr", dateStr);
        }
        if (StringUtils.isNotBlank(migrateFromCluster)) {
            q.setParameter("migrateFromCluster", migrateFromCluster);
        }
        if (processFlag != null && !processFlag.isEmpty()) {
            q.setParameterList("processFlag", processFlag);
        }

        return (List<ModuleClusterMigrateLog>)q.list();
    }

    @Override
    public void updateProcessFlag(ModuleClusterMigrateLog logEntity) {
        StringBuffer sb = new StringBuffer();
        sb.append(" update Module_Cluster_Migrate_Log mcsl ")
          .append(" set mcsl.process_Flag = :processFlag ")
          .append("    ,mcsl.migrate_Start_Time = :migrateStartTime ")
          .append("    ,mcsl.update_Time = :updateTime ")
          .append("    ,mcsl.update_By = :updateBy ")
          .append(" where mcsl.log_Id = :logId ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createNativeQuery(sb.toString());
        q.setParameter("processFlag", logEntity.getProcessFlag());
        q.setParameter("migrateStartTime", logEntity.getMigrateStartTime());
        q.setParameter("updateTime", logEntity.getUpdateTime());
        q.setParameter("updateBy", logEntity.getUpdateBy());
        q.setParameter("logId", logEntity.getLogId());

        q.executeUpdate();
        session.flush();
    }

    @Override
    public Integer insertModuleClusterMigrateLog(ModuleClusterMigrateLog logEntity) {
        return (Integer)getHibernateTemplate().getSessionFactory().getCurrentSession().save(logEntity);
    }
}
