package com.cmap.plugin.module.clustermigrate;

import com.cmap.exception.ServiceLayerException;

public interface ClusterMigrateService {

    public static final String CLUSTER_NAME = "CLUSTER_NAME";
    public static final String MASTER_SERVER_IP = "MASTER_SERVER_IP";
    public static final String MASTER_SERVER_PORT = "MASTER_SERVER_PORT";
    public static final String MASTER_SERVER_CONNECT_MODE = "MASTER_SERVER_CONNECT_MODE";
    public static final String MASTER_SERVER_LOGIN_ACCOUNT = "MASTER_SERVER_LOGIN_ACCOUNT";
    public static final String MASTER_SERVER_LOGIN_PASSWORD = "MASTER_SERVER_LOGIN_PASSWORD";

    public static final String JOB_KEY_GROUP = "JOB_KEY_GROUP";
    public static final String JOB_KEY_NAME = "JOB_KEY_NAME";

    /**
     * 設定準備切換的Cluster
     * @param migrateClusterName
     * @return
     * @throws ServiceLayerException
     */
    public ClusterMigrateVO settingMigrate(String migrateClusterName, boolean resumeJob) throws ServiceLayerException;

    /**
     * 開始執行Cluster切換 for JOB
     * @return
     * @throws ServiceLayerException
     */
    public ClusterMigrateVO executeClusterMigrate() throws ServiceLayerException;

    /**
     * 開始執行Cluster切換
     * @param logId (UI可指定要切換的設定logId；排程啟動此欄位傳入null表示所有尚未執行的紀錄都執行)
     * @return
     * @throws ServiceLayerException
     */
    public ClusterMigrateVO doClusterMigrate(Integer logId) throws ServiceLayerException;

    /**
     * 【第一階段】先嘗試重啟 Service
     * @param serviceName
     * @return
     * @throws ServiceLayerException
     */
    public ClusterMigrateVO doServiceRestart(String serviceName) throws ServiceLayerException;

    /**
     * 【第二階段】再執行 Cluster migrate
     * @param migrateClusterName
     * @return
     * @throws ServiceLayerException
     */
    public ClusterMigrateVO doClusterMigrateImmediately(String migrateClusterName) throws ServiceLayerException;

    /**
     * 【第三階段】前兩個步驟都無法恢復正常時，直接重啟 Server
     * @param rebootName
     * @return
     * @throws ServiceLayerException
     */
    public ClusterMigrateVO doReboot(String rebootServerName) throws ServiceLayerException;
}
