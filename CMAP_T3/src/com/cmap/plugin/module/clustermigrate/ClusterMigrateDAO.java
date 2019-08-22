package com.cmap.plugin.module.clustermigrate;

import java.util.List;
import com.cmap.dao.BaseDAO;

public interface ClusterMigrateDAO extends BaseDAO {

    /**
     * 查找 Module_Cluster_Migrate_Setting 設定資料
     * @param settingName
     * @return
     */
    public List<ModuleClusterMigrateSetting> getClusterMigrateSetting(String settingName);

    /**
     * 查找 Module_Cluster_Migrate_Log 紀錄 by PK (logId)
     * @param logId
     * @return
     */
    public ModuleClusterMigrateLog findClusterMigrateLogByLogId(Integer logId);

    /**
     * 查找 Module_Cluster_Migrate_Log 紀錄
     * @param logId PK
     * @param dateStr 切換寫入日期
     * @param processFlag 執行狀態
     * @return
     */
    public List<ModuleClusterMigrateLog> findClusterMigrateLog(
            Integer logId, String dateStr, String migrateFromCluster, List<String> processFlag);

    public void updateProcessFlag(ModuleClusterMigrateLog logEntity);

    public Integer insertModuleClusterMigrateLog(ModuleClusterMigrateLog logEntity);
}
