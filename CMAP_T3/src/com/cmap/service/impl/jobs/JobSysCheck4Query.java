package com.cmap.service.impl.jobs;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmap.service.BaseJobService;
import com.cmap.service.SysCheckService;

@DisallowConcurrentExecution
public class JobSysCheck4Query extends BaseJobImpl implements BaseJobService {
	private static Logger log = LoggerFactory.getLogger(JobSysCheck4Query.class);

	SysCheckService sysCheckService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final String JOB_ID = UUID.randomUUID().toString();
		Result result = Result.SUCCESS;
		Timestamp startTime = new Timestamp((new Date()).getTime());

		try {


		} catch (Exception e) {
			result = Result.FAILED;
			log.error("JID:["+JOB_ID+"] >> "+e.toString(), e);

		} finally {
			Timestamp endTime = new Timestamp((new Date()).getTime());

			super.insertSysJobLog(JOB_ID, context, result, null, startTime, endTime, null);
		}

	}

}
