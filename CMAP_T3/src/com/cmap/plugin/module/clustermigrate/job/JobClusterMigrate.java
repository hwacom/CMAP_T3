package com.cmap.plugin.module.clustermigrate.job;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cmap.plugin.module.clustermigrate.ClusterMigrateService;
import com.cmap.plugin.module.clustermigrate.ClusterMigrateVO;
import com.cmap.service.BaseJobService;
import com.cmap.service.impl.jobs.BaseJobImpl;
import com.cmap.utils.impl.ApplicationContextUtil;

@DisallowConcurrentExecution
public class JobClusterMigrate extends BaseJobImpl implements BaseJobService {
    private static Logger log = LoggerFactory.getLogger(JobClusterMigrate.class);

    private ClusterMigrateService clusterMigrateService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final String JOB_ID = UUID.randomUUID().toString();
        Timestamp startTime = new Timestamp((new Date()).getTime());
        ClusterMigrateVO cmVO = new ClusterMigrateVO();

        clusterMigrateService = (ClusterMigrateService)ApplicationContextUtil.getBean("clusterMigrateService");

        try {
            cmVO = clusterMigrateService.executeClusterMigrate();

        } catch (Exception e) {
            log.error("JID:["+JOB_ID+"] >> "+e.toString(), e);

            cmVO.setJobExcuteResult(Result.FAILED);
            cmVO.setJobExcuteResultRecords("0");
            cmVO.setJobExcuteRemark(e.getMessage() + ", JID:["+JOB_ID+"]");

        } finally {
            Timestamp endTime = new Timestamp((new Date()).getTime());

            super.insertSysJobLog(JOB_ID, context, cmVO.getJobExcuteResult(), cmVO.getJobExcuteResultRecords(), startTime, endTime, cmVO.getJobExcuteRemark());
        }
    }
}
