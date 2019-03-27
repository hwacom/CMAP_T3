package com.cmap.service.impl.jobs;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmap.Constants;
import com.cmap.service.BaseJobService;
import com.cmap.service.FileOperationService;
import com.cmap.service.vo.FileOperationServiceVO;
import com.cmap.utils.impl.ApplicationContextUtil;

@DisallowConcurrentExecution
public class JobFileOperation extends BaseJobImpl implements BaseJobService {
	private static Logger log = LoggerFactory.getLogger(JobFileOperation.class);

	private FileOperationService fileOperationService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final String JOB_ID = UUID.randomUUID().toString();
		Timestamp startTime = new Timestamp((new Date()).getTime());
		FileOperationServiceVO fosVO = new FileOperationServiceVO();

		try {
			JobDataMap jMap = context.getJobDetail().getJobDataMap();
			final String settingId = jMap.getString(Constants.QUARTZ_PARA_JOB_FILE_OPERATION_SETTING_ID);

			fileOperationService = (FileOperationService)ApplicationContextUtil.getBean("fileOperationService");
			fosVO = fileOperationService.executeFileOperation(settingId);

		} catch (Exception e) {
			log.error("JID:["+JOB_ID+"] >> "+e.toString(), e);

		} finally {
			Timestamp endTime = new Timestamp((new Date()).getTime());

			super.insertSysJobLog(JOB_ID, context, fosVO.getJobExcuteResult(), fosVO.getJobExcuteResultRecords(), startTime, endTime, fosVO.getJobExcuteRemark());
		}
	}

}
