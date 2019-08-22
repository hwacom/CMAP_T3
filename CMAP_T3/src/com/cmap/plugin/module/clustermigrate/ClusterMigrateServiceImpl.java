package com.cmap.plugin.module.clustermigrate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.comm.enums.ConnectionMode;
import com.cmap.comm.enums.ScriptType;
import com.cmap.exception.ServiceLayerException;
import com.cmap.service.DeliveryService;
import com.cmap.service.JobService;
import com.cmap.service.ScriptService;
import com.cmap.service.impl.CommonServiceImpl;
import com.cmap.service.vo.DeliveryParameterVO;
import com.cmap.service.vo.DeliveryServiceVO;
import com.cmap.service.vo.JobServiceVO;
import com.cmap.service.vo.ScriptServiceVO;

@Service("clusterMigrateService")
@Transactional
public class ClusterMigrateServiceImpl extends CommonServiceImpl implements ClusterMigrateService {
    @Log
    private static Logger log;

    @Autowired
    private ClusterMigrateDAO clusterMigrateDAO;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private JobService jobService;

    @Override
    public ClusterMigrateVO settingMigrate(String migrateClusterName, boolean resumeJob) throws ServiceLayerException {
        ClusterMigrateVO cmVO = new ClusterMigrateVO();
        try {
            final String nowDateStr = Constants.FORMAT_YYYY_MM_DD_NOSYMBOL.format(new Date());

            /*
             * Step 1. 檢查今天日期 + 傳入的 clusterName 是否已寫過紀錄，同一天同一台cluster只需寫入一筆待處理log (Process_Flag = O)
             */
            List<String> processFlag = new ArrayList<>();
            processFlag.add(Constants.PROCESS_OPEN);

            List<ModuleClusterMigrateLog> logList = clusterMigrateDAO.findClusterMigrateLog(
                    null, nowDateStr, migrateClusterName, processFlag);

            if (logList != null && !logList.isEmpty()) {
                //TODO 待確認PRTG若一直監控到異常是否會一直呼叫傳入要切換cluster的要求

            } else {
                /*
                 * Step 2. 寫入待處理log資料
                 */
                ModuleClusterMigrateLog logEntity = new ModuleClusterMigrateLog();
                logEntity.setDateStr(nowDateStr);
                logEntity.setMigrateFromCluster(migrateClusterName);
                logEntity.setProcessFlag(Constants.PROCESS_OPEN);
                logEntity.setMigrateResult(Constants.PROCESS_OPEN);
                logEntity.setRemark("Ready to migrate..");
                logEntity.setCreateTime(currentTimestamp());
                logEntity.setCreateBy(getUserName());
                logEntity.setUpdateTime(currentTimestamp());
                logEntity.setUpdateBy(getUserName());

                clusterMigrateDAO.insertEntity(logEntity);

                /*
                 * Step 3. 開啟 Cluster migrate 排程 (若有傳入要開啟 JOB 時)
                 */
                if (resumeJob) {
                    cmVO = initJobInfo(cmVO);

                    String jkName = cmVO.getJobKeyName();
                    String jkGroup = cmVO.getJobKeyGroup();

                    if (StringUtils.isNotBlank(jkGroup) && StringUtils.isNotBlank(jkName)) {
                        List<JobServiceVO> jsVOList = new ArrayList<>();
                        JobServiceVO jsVO = new JobServiceVO();
                        jsVO.setJobKeyName(cmVO.getJobKeyName());
                        jsVO.setJobKeyGroup(cmVO.getJobKeyGroup());
                        jsVOList.add(jsVO);

                        jobService.resumeJob(jsVOList);
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("寫入待切換cluster的log資料時異常 (" + e.getMessage() + ")");
        }

        return cmVO;
    }

    @Override
    public ClusterMigrateVO executeClusterMigrate() throws ServiceLayerException {
        ClusterMigrateVO jobVO = new ClusterMigrateVO();
        ClusterMigrateVO migrateVO = new ClusterMigrateVO();
        boolean needPauseJob = false;
        try {
            jobVO = initJobInfo(jobVO);
            migrateVO = doClusterMigrate(null);

            final String result = migrateVO.getProcessResultFlag();

            switch (result) {
                case Constants.RESULT_FINISH:
                    // Migrate 成功時才須將 JOB 暫停
                    needPauseJob = true;
                    break;

                default:
                    // Migrate 失敗時不關 JOB，讓 JOB 進行 retry
                    needPauseJob = false;
                    break;
            }

        } catch (Exception e) {
            if (!(e instanceof ServiceLayerException)) {
                log.error(e.toString(), e);
            }

        } finally {
            if (needPauseJob) {
                String jkName = jobVO.getJobKeyName();
                String jkGroup = jobVO.getJobKeyGroup();

                if (StringUtils.isNotBlank(jkGroup) && StringUtils.isNotBlank(jkName)) {
                    List<JobServiceVO> jsVOList = new ArrayList<>();
                    JobServiceVO jsVO = new JobServiceVO();
                    jsVO.setJobKeyName(jkName);
                    jsVO.setJobKeyGroup(jkGroup);
                    jsVOList.add(jsVO);

                    jobService.pauseJob(jsVOList);
                }
            }
        }
        return migrateVO;
    }

    private ClusterMigrateVO initJobInfo(ClusterMigrateVO cmVO) throws ServiceLayerException {
        String jobKeyGroup;
        String jobKeyName;

        try {
            List<ModuleClusterMigrateSetting> settings = clusterMigrateDAO.getClusterMigrateSetting(JOB_KEY_GROUP);

            if (settings == null || (settings != null && settings.isEmpty())) {
                throw new ServiceLayerException("未設定 JOB_KEY_GROUP");

            } else {
                jobKeyGroup = settings.get(0).getSettingValue();
            }

            settings = clusterMigrateDAO.getClusterMigrateSetting(JOB_KEY_NAME);

            if (settings == null || (settings != null && settings.isEmpty())) {
                throw new ServiceLayerException("未設定 JOB_KEY_NAME");

            } else {
                jobKeyName = settings.get(0).getSettingValue();
            }

            if (StringUtils.isBlank(jobKeyGroup) || StringUtils.isBlank(jobKeyName)) {
                throw new ServiceLayerException("未設定 JOB_KEY_GROUP 或 JOB_KEY_NAME");
            }

            cmVO.setJobKeyGroup(jobKeyGroup);
            cmVO.setJobKeyName(jobKeyName);

        } catch (ServiceLayerException sle) {
            throw sle;

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("非預期錯誤 (" + e.getMessage() + ")");
        }
        return cmVO;
    }

    @Override
    public ClusterMigrateVO doClusterMigrate(Integer logId) throws ServiceLayerException {
        ClusterMigrateVO cmVO = new ClusterMigrateVO();
        try {
            String migrateSuccessMsg = "Migrate success !!";
            /*
             * Step 1. 查找待處理或前次處理失敗的log紀錄
             */
            List<String> processFlag = null;

            // 排程啟動不會傳入logId，查出當天待處理(O)、前次處理失敗(E)、UI指定重作(R)的資料
            if (logId == null) {
                processFlag = new ArrayList<>();
                processFlag.add(Constants.PROCESS_OPEN);
                processFlag.add(Constants.PROCESS_ERROR);
                processFlag.add(Constants.PROCESS_RETRY);
            }

            // 從UI執行會帶入logId，此時不下日期條件
            final String dateStr = (logId == null) ? Constants.FORMAT_YYYY_MM_DD_NOSYMBOL.format(new Date()) : null;

            // 查找此次需要處理的log資料
            List<ModuleClusterMigrateLog> logList = clusterMigrateDAO.findClusterMigrateLog(logId, dateStr, null, processFlag);

            if (logList == null || (logList != null && logList.isEmpty())) {
                cmVO.setProcessResultFlag(Constants.RESULT_FINISH);
                cmVO.setProcessResultMsg("No need migrate.");
                cmVO.setProcessRemark("無須切換的 cluster");

            } else {
                /*
                 * 先取得所需的相關設定值 FOR 後續流程使用
                 * (1) 取得所有 cluster 名稱清單
                 * (2) 取得 Master Server 連線&登入相關資訊
                 */
                cmVO = initSetting(cmVO);

                // 準備後續供裝要連線的Server相關資訊
                Map<String, String> deviceInfo = new HashMap<>();
                deviceInfo.put(Constants.DEVICE_IP, cmVO.getServerIp());
                deviceInfo.put(Constants.DEVICE_PORT, cmVO.getServerPort());
                deviceInfo.put(Constants.DEVICE_LOGIN_ACCOUNT, cmVO.getLoginAccount());
                deviceInfo.put(Constants.DEVICE_LOGIN_PASSWORD, cmVO.getLoginPassword());

                // 供裝設備連線protocol方式
                final ConnectionMode _CONNECTION_MODE_ = cmVO.getConnectionMode();

                /*
                 * Step 2. 將這一批次要處理的log，全部都先調整處理狀態為處理中(Process_Flag = "*")
                 *         期間順便整理這一批次要migrate的cluster有哪些，可以migrate的cluster清單須排除掉
                 */
                for (ModuleClusterMigrateLog logEntity : logList) {
                    final String needMigrateCluster = logEntity.getMigrateFromCluster();

                    /*
                     *  將所有可用的cluster清單，排除掉這一批次要進行migrate的cluster，剩下的才是真的能migrate過去的cluster
                     *  [Ex]: 所有cluster編號為 a / b / c，若此次要作migrate的cluster有 a & c
                     *        >> 只剩下 b 可用來migrate，意指所有運作在 a & c cluster的服務都要migrate到 b
                     *
                     *  因為要進行 List element remove，因此採用 Iterator 方式實作，避免產生 ConcurrentModificationException 異常
                     *  (參考: https://www.cnblogs.com/dolphin0520/p/3933551.html)
                     */
                    Iterator<String> it = cmVO.getClusterList().iterator();

                    while (it.hasNext()) {
                        String enableCluster = it.next();

                        if (StringUtils.equals(enableCluster, needMigrateCluster)) {
                            it.remove();
                        }
                    }

                    ModuleClusterMigrateLog updateLog = clusterMigrateDAO.findClusterMigrateLogByLogId(logEntity.getLogId());
                    updateLog.setProcessFlag(Constants.PROCESS_ING);
                    updateLog.setMigrateStartTime(currentTimestamp());
                    updateLog.setUpdateTime(currentTimestamp());
                    updateLog.setUpdateBy(getUserName());

                    updateProcessFlagWithoutTx(updateLog); // 第一步將Flag調整為處理中(*)需跳脫transaction外
                }

                final List<String> enableClusterList = cmVO.getClusterList();

                if (enableClusterList == null || (enableClusterList != null && enableClusterList.isEmpty())) {
                    // 若這一批次要切換的 cluster 已等於所有 cluster，表示所有 cluster 都服務異常無法 migrate
                    String errorMsg = "目前沒有可以 migrate 的 cluster";

                    for (ModuleClusterMigrateLog entity : logList) {
                        updateLogStatus(entity, Constants.PROCESS_ERROR, Constants.RESULT_ERROR, errorMsg);
                    }

                    throw new ServiceLayerException(errorMsg);
                }

                DeliveryServiceVO deliveryVO;               // 供裝共用參數傳遞VO
                List<ClusterMigrateVO> migrateServiceList;  // 要migrate的服務清單
                String provisionReason;                     // 供裝原因

                int totalCount = logList.size();            // 這一批次共有幾個cluster要migrate
                int errorCount = 0;                         // migrate失敗的cluster數量

                for (ModuleClusterMigrateLog logEntity : logList) { // 迴圈跑每一筆要migrate的cluster
                    deliveryVO = null;
                    migrateServiceList = new ArrayList<>();
                    provisionReason = null;

                    final String migrateFromCluster = logEntity.getMigrateFromCluster();        // 要migrate的cluster
                    final String targetMigrateServiceName = logEntity.getMigrateServiceName();  // 要migrate的服務名稱 (若是第一次執行會是空值)

                    try {
                        /*
                         * Step 3. 判斷此筆log紀錄資料完整性決定作法
                         * (1) targetMigrateServiceName 有資料，表示為前一次排程啟動時切換失敗或USER操作UI指定此Service再做一次migrate，不走 4-1 & 4-2 步驟
                         * (2) targetMigrateServiceName 無資料，表示為第一次執行，須走 4-1 & 4-2 步驟
                         */
                        if (StringUtils.isBlank(targetMigrateServiceName)) {
                            /*
                             * Step 4-1. 呼叫供裝模組，登入設備取得 cluster status 資料
                             */
                            ScriptServiceVO clusterStatusScriptVO = scriptService.findDefaultScriptInfoByScriptTypeAndSystemVersion(
                                    ScriptType.CLUSTER_STATUS.toString(), Constants.DATA_STAR_SYMBOL);

                            if (clusterStatusScriptVO == null) {
                                throw new ServiceLayerException("查詢不到預設腳本 for 查看 cluster 狀態 >> scriptType: " + ScriptType.CLUSTER_STATUS);
                            }

                            provisionReason = "Cluster migrate >> 查看 cluster 狀態";
                            DeliveryParameterVO dpVO = new DeliveryParameterVO();
                            dpVO.setScriptInfoId(clusterStatusScriptVO.getScriptInfoId());
                            dpVO.setScriptCode(clusterStatusScriptVO.getScriptCode());
                            dpVO.setDeviceInfo(deviceInfo);
                            dpVO.setReason(provisionReason);

                            // 【Cluster migrate】取得 cluster 狀態
                            deliveryVO = deliveryService.doDelivery(_CONNECTION_MODE_, dpVO, true, "PRTG", provisionReason, false);

                            final List<String> cmdOutputList = deliveryVO.getCmdOutputList();
                            String cmdOutput = null;

                            if (cmdOutputList == null || (cmdOutputList != null && cmdOutputList.isEmpty())) {
                                throw new ServiceLayerException("查找 cluster 狀態結果為空");

                            } else {
                                cmdOutput = cmdOutputList.get(0);

                                if (StringUtils.isBlank(cmdOutput)) {
                                    throw new ServiceLayerException("查找 cluster 狀態結果為空");
                                }
                            }

                            String clusterStatus = null;

                            try {
                                clusterStatus = StringUtils.split(cmdOutput, Env.COMM_SEPARATE_SYMBOL)[1];

                            } catch (Exception e) {
                                log.error(e.toString(), e);
                                throw new ServiceLayerException("無法解析 cluster 狀態回傳結果 (" + e.getMessage() + ")");
                            }

                            /*
                             * Step 4-2. 解析目前有哪些服務跑在這準備要 migrate 的 cluster 上
                             */
                            migrateServiceList = analyzeServiceList(clusterStatus, migrateFromCluster);

                        } else {
                            // targetMigrateServiceName 不為空，表示至少已經跑過一次migrate但失敗，因此只要將這個服務再做一次migrate
                            ClusterMigrateVO migrateVO = new ClusterMigrateVO();
                            migrateVO.setMigrateFromCluster(migrateFromCluster);
                            migrateVO.setMigrateServiceName(targetMigrateServiceName);
                            migrateServiceList.add(migrateVO);
                        }

                        if (migrateServiceList == null || (migrateServiceList != null && migrateServiceList.isEmpty())) {
                            // 若此 cluster 目前無運作任何服務則跳過不須處理
                            String msg = "目前無運作在此 cluster 的服務需要 migrate";

                            // 調整處理狀態為C
                            updateLogStatus(logEntity, Constants.PROCESS_CLOSE, Constants.RESULT_FINISH, msg);

                            continue;
                        }

                        /*
                         * Step 4-3. 決定好每個服務要切換到哪一個 cluster
                         */
                        for (ClusterMigrateVO needMigrateVO : migrateServiceList) {
                            //TODO: 若有提供當有多台 cluster 可供 migrate 時，如何決定每個服務要切換到哪個 cluster 的邏輯判斷法則
                            //TODO: 預設都先取 setting 內設定的 order_no 順序，號碼越小的排序越前，並取排序最大的第一筆作 migrate 目標
                            final String migrateToCluster = enableClusterList.get(0);

                            needMigrateVO.setMigrateToCluster(migrateToCluster);
                        }

                        /*
                         * Step 5. 呼叫供裝模組，登入設備做 cluster migrate
                         */
                        ScriptServiceVO clusterMigrateScriptVO = scriptService.findDefaultScriptInfoByScriptTypeAndSystemVersion(
                                ScriptType.CLUSTER_MIGRATE.toString(), Constants.DATA_STAR_SYMBOL);

                        if (clusterMigrateScriptVO == null) {
                            throw new ServiceLayerException("查詢不到預設腳本 for cluster migrate >> scriptType: " + ScriptType.CLUSTER_MIGRATE);
                        }

                        provisionReason = "Cluster migrate >> 執行 cluster migrate";
                        DeliveryParameterVO dpVO = new DeliveryParameterVO();
                        dpVO.setScriptInfoId(clusterMigrateScriptVO.getScriptInfoId());
                        dpVO.setScriptCode(clusterMigrateScriptVO.getScriptCode());
                        dpVO.setDeviceInfo(deviceInfo);
                        dpVO.setReason(provisionReason);

                        final String scriptKey = clusterMigrateScriptVO.getActionScriptVariable();

                        List<String> varKey = (List<String>)transJSON2Object(scriptKey, List.class);
                        dpVO.setVarKey(varKey);

                        List<List<String>> varValue = new ArrayList<>();
                        List<String> valueList = null;

                        for (ClusterMigrateVO todoMigrateVO : migrateServiceList) {
                            valueList = new ArrayList<>();
                            valueList.add(todoMigrateVO.getMigrateServiceName());   // 指令參數1: 要 Migrate 的服務名稱
                            valueList.add(todoMigrateVO.getMigrateToCluster());     // 指令參數2: 要 Migrate 到哪一個 cluster
                            varValue.add(valueList);
                        }

                        dpVO.setVarValue(varValue);

                        deliveryService.doDelivery(_CONNECTION_MODE_, dpVO, true, "PRTG", provisionReason, false);


                        /*
                         * Step 6. 更新log紀錄處理狀態為已處理(Process_Flag = "C")，以及相關備註
                         */
                        List<ModuleClusterMigrateLog> insertLogEntities = new ArrayList<>();
                        ModuleClusterMigrateLog newLog;
                        for (ClusterMigrateVO mVO : migrateServiceList) {
                            newLog = new ModuleClusterMigrateLog();
                            BeanUtils.copyProperties(logEntity, newLog);
                            newLog.setProcessFlag(Constants.PROCESS_CLOSE);
                            newLog.setMigrateServiceName(mVO.getMigrateServiceName());
                            newLog.setMigrateToCluster(mVO.getMigrateToCluster());
                            newLog.setMigrateEndTime(currentTimestamp());
                            newLog.setMigrateResult(Constants.RESULT_FINISH);
                            newLog.setRemark(migrateSuccessMsg);
                            newLog.setUpdateTime(currentTimestamp());
                            newLog.setUpdateBy(getUserName());

                            insertLogEntities.add(newLog);
                        }

                        // by 此次 migrate 的 cluster 有哪些運作服務寫入 log
                        clusterMigrateDAO.insertEntities(insertLogEntities);

                        // 刪除最原始的一筆
                        clusterMigrateDAO.deleteEntity(logEntity);

                        insertLogEntities = null;

                    } catch (Exception e) {
                        log.error(e.toString(), e);

                        String remark = cmVO.getProcessRemark();
                        remark += "[Cluster=" + migrateFromCluster + "::執行 migrate 過程發生異常 (ERROR:" + e.getMessage() + "]";
                        cmVO.setProcessRemark(remark);

                        // 調整處理狀態為失敗(E)
                        updateLogStatus(logEntity, Constants.PROCESS_ERROR, Constants.RESULT_ERROR, e.getMessage());

                        errorCount++;
                        continue;
                    }
                }

                if (errorCount == 0) {
                    cmVO.setProcessResultFlag(Constants.RESULT_FINISH);
                    cmVO.setProcessResultMsg("Migrate success !!");

                } else if (errorCount > 0 && errorCount < totalCount) {
                    cmVO.setProcessResultFlag(Constants.RESULT_PARTIAL);
                    cmVO.setProcessResultMsg("Partial migrate success !!");

                } else {
                    cmVO.setProcessResultFlag(Constants.RESULT_ERROR);
                    cmVO.setProcessResultMsg("Migrate failed !!");
                }
            }

        } catch (Exception e) {
            log.error(e.toString(), e);

            cmVO.setProcessResultFlag(Constants.RESULT_ERROR);
            cmVO.setProcessResultMsg("Migrate failed !!");
            cmVO.setProcessRemark(e.getMessage());
        }
        return cmVO;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private void updateProcessFlagWithoutTx(ModuleClusterMigrateLog logEntity) {
        clusterMigrateDAO.updateProcessFlag(logEntity); // 第一步將Flag調整為處理中(*)需跳脫transaction外
    }

    private void updateLogStatus(ModuleClusterMigrateLog logEntity, String processFlag, String migrateResult, String remark) {
        logEntity.setProcessFlag(processFlag);
        logEntity.setMigrateEndTime(currentTimestamp());
        logEntity.setMigrateResult(migrateResult);
        logEntity.setRemark(remark);
        logEntity.setUpdateTime(currentTimestamp());
        logEntity.setUpdateBy(getUserName());
        clusterMigrateDAO.updateEntity(logEntity);
    }

    /**
     * 初始化取得相關參數設定值
     * @param cmVO
     * @return
     * @throws ServiceLayerException
     */
    private ClusterMigrateVO initSetting(ClusterMigrateVO cmVO) throws ServiceLayerException {
        List<String> clusterList = new ArrayList<>();
        String serverIp = null;
        String serverPort = null;
        ConnectionMode connectionMode = null;
        String loginAccount = null;
        String loginPassword = null;

        try {
            // 取得所有 cluster 名稱清單
            List<ModuleClusterMigrateSetting> settings = clusterMigrateDAO.getClusterMigrateSetting(CLUSTER_NAME);

            if (settings == null || (settings != null && settings.isEmpty())) {
                throw new ServiceLayerException("未設定 cluster 清單無法進行 migrate 作業 (setting_name = " + CLUSTER_NAME.toString() + ")");

            } else {
                clusterList = new ArrayList<>();

                for (ModuleClusterMigrateSetting setting : settings) {
                    final String settingClusterName = setting.getSettingValue();

                    if (StringUtils.isNotBlank(settingClusterName)) {
                        clusterList.add(settingClusterName);
                    }
                }

                if (clusterList == null || (clusterList != null && clusterList.isEmpty())) {
                    throw new ServiceLayerException("未設定 cluster 清單無法進行 migrate 作業 (setting_name = " + CLUSTER_NAME.toString() + ")");
                }
            }

            // 取得要執行供裝的 Server IP
            settings = clusterMigrateDAO.getClusterMigrateSetting(MASTER_SERVER_IP);
            if (settings == null || (settings != null && settings.isEmpty())) {
                throw new ServiceLayerException("未設定主伺服器連線IP (setting_name = " + MASTER_SERVER_IP.toString() + ")");

            } else {
                serverIp = settings.get(0).getSettingValue();
            }

            // 取得要執行供裝的 Server Port
            settings = clusterMigrateDAO.getClusterMigrateSetting(MASTER_SERVER_PORT);
            if (settings != null && !settings.isEmpty()) {
                serverPort = settings.get(0).getSettingValue();
            }

            // 取得要執行供裝的 Server 連線 Protocol 方式
            settings = clusterMigrateDAO.getClusterMigrateSetting(MASTER_SERVER_CONNECT_MODE);
            if (settings == null || (settings != null && settings.isEmpty())) {
                throw new ServiceLayerException("未設定主伺服器連線Protocol (setting_name = " + MASTER_SERVER_CONNECT_MODE.toString() + ")");

            } else {
                String protocol = settings.get(0).getSettingValue();

                switch (protocol) {
                    case Constants.TELNET:
                        connectionMode = ConnectionMode.TELNET;
                        break;

                    case Constants.SSH:
                        connectionMode = ConnectionMode.SSH;
                        break;
                }
            }

            // 取得要執行供裝的 Server 登入帳號
            settings = clusterMigrateDAO.getClusterMigrateSetting(MASTER_SERVER_LOGIN_ACCOUNT);
            if (settings == null || (settings != null && settings.isEmpty())) {
                throw new ServiceLayerException("未設定主伺服器連線登入帳號 (setting_name = " + MASTER_SERVER_LOGIN_ACCOUNT.toString() + ")");

            } else {
                loginAccount = settings.get(0).getSettingValue();
            }

            // 取得要執行供裝的 Server 登入密碼
            settings = clusterMigrateDAO.getClusterMigrateSetting(MASTER_SERVER_LOGIN_PASSWORD);
            if (settings == null || (settings != null && settings.isEmpty())) {
                throw new ServiceLayerException("未設定主伺服器連線登入密碼 (setting_name = " + MASTER_SERVER_LOGIN_PASSWORD.toString() + ")");

            } else {
                loginPassword = settings.get(0).getSettingValue();
            }

            // 塞回VO物件內
            cmVO.setClusterList(clusterList);
            cmVO.setServerIp(serverIp);
            cmVO.setServerPort(serverPort);
            cmVO.setConnectionMode(connectionMode);
            cmVO.setLoginAccount(loginAccount);
            cmVO.setLoginPassword(loginPassword);

            return cmVO;

        } catch (ServiceLayerException sle) {
            throw sle;

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("非預期錯誤 (" + e.getMessage() + ")");
        }
    }

    private List<ClusterMigrateVO> analyzeServiceList(String clusterStatus, String migrateFromCluster) throws ServiceLayerException {
        List<ClusterMigrateVO> needMigrateList = new ArrayList<>();
        try {
            if (clusterStatus == null || migrateFromCluster == null) {
                log.error("無法解析 cluster 狀態回傳結果 >> clusterStatus: " + clusterStatus + ", migrateFromCluster: " + migrateFromCluster);
                throw new ServiceLayerException("無法解析 cluster 狀態回傳結果");
            }

            String[] lines = clusterStatus.split("\r\n");

            for (String line : lines) {
                if (StringUtils.contains(line, migrateFromCluster)) {
                    String[] tmp = line.split(migrateFromCluster);
                    String leftSide = tmp[0];
                    String needMigrateServiceName = leftSide.split(" ")[0];

                    if (StringUtils.isNotBlank(needMigrateServiceName)) {
                        ClusterMigrateVO vo = new ClusterMigrateVO();
                        vo.setMigrateFromCluster(migrateFromCluster);
                        vo.setMigrateServiceName(needMigrateServiceName);

                        needMigrateList.add(vo);
                    }
                }
            }

        } catch (ServiceLayerException sle) {
            throw sle;

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("解析 cluster 運行服務名稱過程異常 (" + e.getMessage() + ")");
        }

        return needMigrateList;
    }

    @Override
    public ClusterMigrateVO doServiceRestart(String serviceName) throws ServiceLayerException {
        ClusterMigrateVO cmVO = new ClusterMigrateVO();
        try {
            /*
             * 先取得所需的相關設定值 FOR 後續流程使用
             * (1) 取得所有 cluster 名稱清單
             * (2) 取得 Master Server 連線&登入相關資訊
             */
            cmVO = initSetting(cmVO);

            // 準備後續供裝要連線的Server相關資訊
            Map<String, String> deviceInfo = new HashMap<>();
            deviceInfo.put(Constants.DEVICE_IP, cmVO.getServerIp());
            deviceInfo.put(Constants.DEVICE_PORT, cmVO.getServerPort());
            deviceInfo.put(Constants.DEVICE_LOGIN_ACCOUNT, cmVO.getLoginAccount());
            deviceInfo.put(Constants.DEVICE_LOGIN_PASSWORD, cmVO.getLoginPassword());

            // 供裝設備連線protocol方式
            final ConnectionMode _CONNECTION_MODE_ = cmVO.getConnectionMode();

            /*
             * Step 1. 呼叫供裝模組，登入設備取得 cluster status 資料
             */
            ScriptServiceVO serviceRestartScriptVO = scriptService.findDefaultScriptInfoByScriptTypeAndSystemVersion(
                    ScriptType.SERVICE_RESTART.toString(), Constants.DATA_STAR_SYMBOL);

            if (serviceRestartScriptVO == null) {
                throw new ServiceLayerException("查詢不到預設腳本 for 查看 cluster 狀態 >> scriptType: " + ScriptType.CLUSTER_STATUS);
            }

            DeliveryServiceVO deliveryVO;               // 供裝共用參數傳遞VO
            String provisionReason;                     // 供裝原因

            provisionReason = "Service restart";
            DeliveryParameterVO dpVO = new DeliveryParameterVO();
            dpVO.setScriptInfoId(serviceRestartScriptVO.getScriptInfoId());
            dpVO.setScriptCode(serviceRestartScriptVO.getScriptCode());
            dpVO.setDeviceInfo(deviceInfo);
            dpVO.setReason(provisionReason);

            final String scriptKey = serviceRestartScriptVO.getActionScriptVariable();

            List<String> varKey = (List<String>)transJSON2Object(scriptKey, List.class);
            dpVO.setVarKey(varKey);

            List<List<String>> varValue = new ArrayList<>();
            List<String> valueList = new ArrayList<>();
            valueList.add(serviceName);   // 指令參數1: 要重啟的服務名稱
            varValue.add(valueList);
            dpVO.setVarValue(varValue);

            // 【Service restart】重啟服務
            deliveryVO = deliveryService.doDelivery(_CONNECTION_MODE_, dpVO, true, "PRTG", provisionReason, false);

            cmVO.setProcessResultFlag(Constants.RESULT_FINISH);
            cmVO.setProcessResultMsg(serviceName + " 服務重啟成功");

        } catch (Exception e) {
            log.error(e.toString(),e);

            cmVO.setProcessResultFlag(Constants.RESULT_ERROR);
            cmVO.setProcessResultMsg(serviceName + " 服務重啟失敗");
        }
        return cmVO;
    }

    @Override
    public ClusterMigrateVO doClusterMigrateImmediately(String migrateClusterName)
            throws ServiceLayerException {
        ClusterMigrateVO cmVO = new ClusterMigrateVO();
        try {
            cmVO = settingMigrate(migrateClusterName, false);

            doClusterMigrate(null);

            cmVO.setProcessResultFlag(Constants.RESULT_FINISH);
            cmVO.setProcessResultMsg(migrateClusterName + " 已成功進行 migrate");

        } catch (Exception e) {
            log.error(e.toString(),e);

            cmVO.setProcessResultFlag(Constants.RESULT_FINISH);
            cmVO.setProcessResultMsg(migrateClusterName + "  migrate 失敗");
        }
        return cmVO;
    }

    @Override
    public ClusterMigrateVO doReboot(String rebootServerName) throws ServiceLayerException {
        ClusterMigrateVO cmVO = new ClusterMigrateVO();
        try {


        } catch (Exception e) {
            log.error(e.toString(),e);
        }
        return cmVO;
    }
}
