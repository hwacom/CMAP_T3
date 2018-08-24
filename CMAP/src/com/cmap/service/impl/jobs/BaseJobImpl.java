package com.cmap.service.impl.jobs;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Env;
import com.cmap.dao.SysJobLogDAO;
import com.cmap.model.SysJobLog;
import com.cmap.utils.impl.ApplicationContextUtil;
import com.cmap.utils.impl.CommonUtils;

@Service("baseJobImpl")
@Transactional
public class BaseJobImpl {
	private static Logger log = LoggerFactory.getLogger(BaseJobImpl.class);

	private SysJobLogDAO sysJobLogDAO;

	public enum Result {
		SUCCESS,
		PARTIAL_SUCCESS,
		FAILED
	}

	protected void insertSysJobLog(
			String jobId, JobExecutionContext context, Result result, String recordsNum,
			Timestamp startTime, Timestamp endTime, String remark) {
		if (context != null) {

			try {
				if (sysJobLogDAO == null) {
					sysJobLogDAO = (SysJobLogDAO)ApplicationContextUtil.getBean("sysJobLogDAO");
				}

				final JobDetail jobDetail = context.getJobDetail();
				final JobKey jobKey = jobDetail.getKey();

				final Trigger trigger = context.getTrigger();
				final TriggerKey triggerKey = trigger.getKey();

				final String schedName = context.getScheduler().getSchedulerName();
				final String triggerName = triggerKey.getName();
				final String triggerGroup = triggerKey.getGroup();
				final String jobName = jobKey.getName();
				final String jobGroup = jobKey.getGroup();
				final String cronExpression = "";
				final Class<?> jobClass = jobDetail.getJobClass();
				final JobDataMap jobDataMap = jobDetail.getJobDataMap();
				int priority = trigger.getPriority();
				int misfireInstruction = trigger.getMisfireInstruction();
				Date prevFireTime = trigger.getPreviousFireTime();
				Date nextFireTime = trigger.getNextFireTime();

				SysJobLog entity = new SysJobLog(
						UUID.randomUUID().toString()											//log_id
						,schedName
						,triggerName
						,triggerGroup
						,jobName
						,jobGroup
						,result != null ? result.toString() : result.FAILED.toString()
								,recordsNum
								,startTime
								,endTime
								,CommonUtils.calculateSpendTime(startTime, endTime)						//spend_time_in_seconds
								,""																		//cron_expression
								,jobClass.getCanonicalName()											//job_class_name
								,""																		//job_data_map
								,priority
								,(short)misfireInstruction
								,prevFireTime != null ? new Timestamp(prevFireTime.getTime()) : null
										,nextFireTime != null ? new Timestamp(nextFireTime.getTime()) : null
												,remark
												,new Timestamp((new Date()).getTime())
												,Env.USER_NAME_JOB
						);

				sysJobLogDAO.insertSysJobLog(entity);

			} catch (Exception e) {
				log.error(e.toString(), e);
			}
		}
	}
}
