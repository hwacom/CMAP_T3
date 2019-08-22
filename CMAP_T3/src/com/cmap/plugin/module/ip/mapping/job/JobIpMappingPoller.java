package com.cmap.plugin.module.ip.mapping.job;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cmap.Constants;
import com.cmap.exception.ServiceLayerException;
import com.cmap.plugin.module.ip.mapping.IpMappingService;
import com.cmap.plugin.module.ip.mapping.IpMappingServiceVO;
import com.cmap.service.BaseJobService;
import com.cmap.service.impl.jobs.BaseJobImpl;
import com.cmap.utils.impl.ApplicationContextUtil;

@DisallowConcurrentExecution
public class JobIpMappingPoller extends BaseJobImpl implements BaseJobService {
    private static Logger log = LoggerFactory.getLogger(JobIpMappingPoller.class);

    private IpMappingService ipMappingService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final String JOB_ID = UUID.randomUUID().toString();
        Date startDate = new Date();
        Timestamp startTime = new Timestamp(startDate.getTime());
        IpMappingServiceVO imsVO = new IpMappingServiceVO();

        JobDataMap jMap = context.getJobDetail().getJobDataMap();
        final String groupIdVar = jMap.getString(Constants.QUARTZ_PARA_IP_MAC_PORT_MAPPING_POLLER_GROUP_ID);
        
        if (StringUtils.isBlank(groupIdVar)) {
        	throw new JobExecutionException("JobIpMappingPoller >> Group_ID為空!!");
        }
        String[] groupIds = groupIdVar.split(",");

        ipMappingService = (IpMappingService)ApplicationContextUtil.getBean("ipMappingService");

        for (String groupId : groupIds) {
            try {
                imsVO = ipMappingService.executeIpMappingPolling(JOB_ID, startDate, groupId);

            } catch (Exception e) {
                log.error("JID:["+JOB_ID+"] >> "+e.toString(), e);

                imsVO.setJobExcuteResult(Result.FAILED);
                imsVO.setJobExcuteResultRecords("0");
                imsVO.setJobExcuteRemark(e.getMessage() + ", JID:["+JOB_ID+"]");

            } finally {
                Timestamp endTime = new Timestamp((new Date()).getTime());

                super.insertSysJobLog(JOB_ID, context, imsVO.getJobExcuteResult(), imsVO.getJobExcuteResultRecords(), startTime, endTime, imsVO.getJobExcuteRemark());
            }
        }
    }
}
