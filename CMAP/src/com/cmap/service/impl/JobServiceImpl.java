package com.cmap.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.dao.QuartzDAO;
import com.cmap.model.QrtzCronTriggers;
import com.cmap.model.QrtzJobDetails;
import com.cmap.model.QrtzTriggers;
import com.cmap.service.BaseJobService;
import com.cmap.service.JobService;
import com.cmap.service.vo.JobServiceVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("jobService")
@Transactional
public class JobServiceImpl implements JobService {
	private static Log log = LogFactory.getLog(JobServiceImpl.class);
	
	@Autowired @Qualifier("Scheduler")
	private Scheduler scheduler;
	
	@Autowired
	private QuartzDAO quartzDAO;

	@Override
	public long countJobInfoByVO(JobServiceVO jsVO) throws Exception {
		long count = 0;
		
		try {
			count = quartzDAO.countQuartzDataByDAOVO(null);
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
		
		return count;
	}
	
	private String convertLongTime2DateStr(Long longTime) {
		return Constants.FORMAT_YYYYMMDD_HH24MI.format(new Date(longTime));
	}

	@Override
	public List<JobServiceVO> findJobInfoByVO(JobServiceVO jsVO) throws Exception {
		List<JobServiceVO> retList = new ArrayList<JobServiceVO>();
		
		try {
			List<Object[]> modelList = quartzDAO.findQuartzDataByDAOVO(null);
			
			QrtzTriggers qt;
			QrtzCronTriggers qct;
			QrtzJobDetails qjd;
			if (modelList != null && !modelList.isEmpty()) {
				System.out.println(modelList.size());
				
				JobServiceVO retVO;
				for (Object[] modelObj : modelList) {
					qt = modelObj[0] != null ? (QrtzTriggers)modelObj[0] : new QrtzTriggers();
					qct = modelObj[1] != null ? (QrtzCronTriggers)modelObj[1] : new QrtzCronTriggers();
					qjd = modelObj[2] != null ? (QrtzJobDetails)modelObj[2] : new QrtzJobDetails();
					
					retVO = new JobServiceVO();
					BeanUtils.copyProperties(qt, retVO);
					BeanUtils.copyProperties(qct, retVO);
					BeanUtils.copyProperties(qjd, retVO);
					
					retVO.setMisFireInstr(retVO.getMisFireInstr() == null ? new Short("0") : retVO.getMisFireInstr());
					retVO.set_preFireTime(convertLongTime2DateStr(qt.getPrevFireTime()));
					retVO.set_nextFireTime(convertLongTime2DateStr(qt.getNextFireTime()));
					retVO.set_startTime(convertLongTime2DateStr(qt.getStartTime()));
					retVO.set_endTime(convertLongTime2DateStr(qt.getEndTime()));
					
					retVO.set_jobData(IOUtils.toString(qjd.getJobData()));
					
					ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(qjd.getJobData()));
					JobDataMap jdm = (JobDataMap)ois.readObject();
					
					if (jdm != null && !jdm.isEmpty()) {
						String deviceListId = (String)jdm.get(Constants.QUARTZ_PARA_DEVICE_LIST_ID);
						String configType = (String)jdm.get(Constants.QUARTZ_PARA_CONFIG_TYPE);
						
						ObjectMapper mapper = new ObjectMapper();
						List<String> idList = mapper.readValue(deviceListId, new TypeReference<List<String>>(){});
						if (idList != null && !idList.isEmpty()) {
							for (String id : idList) {
								System.out.println("id: "+id);
							}
							
							System.out.println("configType: "+configType);
						}
					}
					
					
					
					retList.add(retVO);
				}
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}

		return retList;
	}
	
	public static BaseJobService getClass(String classname) throws Exception 
    {
        Class<?> class1 = Class.forName(classname);
        return (BaseJobService)class1.newInstance();
    }
	
	
	private JobDataMap composeJobDataMap(JobServiceVO jsVO) throws Exception {
		JobDataMap jobDataMap = new JobDataMap();
        if (StringUtils.equals(jsVO.getInputSchedType(), Constants.QUARTZ_SCHED_TYPE_BACKUP_CONFIG)) {
        	
        	ObjectMapper mapper = new ObjectMapper();
        	jobDataMap.put(Constants.QUARTZ_PARA_DEVICE_LIST_ID, mapper.writeValueAsString(jsVO.getInputDeviceListIds()));
        	jobDataMap.put(Constants.QUARTZ_PARA_CONFIG_TYPE, jsVO.getInputConfigType());
	        
        } else if (StringUtils.equals(jsVO.getInputSchedType(), Constants.QUARTZ_SCHED_TYPE_CLEAN_UP_FTP_FILE)) {
        	
        	
        } else if (StringUtils.equals(jsVO.getInputSchedType(), Constants.QUARTZ_SCHED_TYPE_CLEAN_UP_DB_DATA)) {
        	
        }
        
        return jobDataMap;
	}
	
	private void assignPriority(CronScheduleBuilder scheduleBuilder, JobServiceVO jsVO) {
		//miss fire policy
        switch (jsVO.getInputMisFirePolicy()) {
        	case CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING:
        		scheduleBuilder.withMisfireHandlingInstructionDoNothing();
        		break;
        	
        	case CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW:
        		scheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        		break;
        	
        	case CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:
        		scheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        		break;
        	
        	default:
        		scheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        		break;
        }
	}
	
	@Override
	public void addJob(JobServiceVO jsVO) throws Exception {
		try {
			//start scheduler
	        scheduler.start(); 
	        
	        String cronExpression = jsVO.getInputCronExpression();	//"0 0/30 * * * ?"
	        
	        //build job
	        JobDetail jobDetail = JobBuilder
									.newJob(getClass(jsVO.getInputClassName()).getClass())
									.withIdentity(jsVO.getInputJobName(), jsVO.getInputJobGroup())
									.usingJobData(composeJobDataMap(jsVO))
									.withDescription(jsVO.getInputDescription())
									.build();
	        
	        //build cron expression
	        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
	        
	        //assign priority
	        assignPriority(scheduleBuilder, jsVO);
	        
	        //build a new trigger with new cron expression 
	        CronTrigger trigger = 
	        		TriggerBuilder
	        			.newTrigger()
	        			.withIdentity(jsVO.getInputJobName(), jsVO.getInputJobGroup())
	        			.withSchedule(scheduleBuilder)
	        			.withPriority(jsVO.getInputPriority() != null ? jsVO.getInputPriority() : Env.QUARTZ_DEFAULT_PRIORITY)
	        			.withDescription(jsVO.getInputDescription())
	        			.build();

        
            scheduler.scheduleJob(jobDetail, trigger);
            
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
	}

	@Override
	public String pauseJob(List<JobServiceVO> jsVOList) throws Exception {
		int errorCount = 0;
		
		for (JobServiceVO jsVO : jsVOList) {
			try {
				scheduler.pauseJob(JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup()));
				
			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error(e.toString(), e);
				}
				e.printStackTrace();
				
				errorCount++;
				continue;
			}
		}
		
		if (errorCount == 0) {
			return composeMsg("選定暫停 {@} 組排程成功", "{@}", new Object[] {jsVOList.size()});
		} else {
			return composeMsg("選定暫停 {@} 組排程。成功: {@} 組；失敗: {@} 組", "{@}", new Object[] {jsVOList.size(), (jsVOList.size()-errorCount), errorCount});
		}
	}

	@Override
	public String resumeJob(List<JobServiceVO> jsVOList) throws Exception {
		int errorCount = 0;
		
		for (JobServiceVO jsVO : jsVOList) {
			try {
				scheduler.resumeJob(JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup()));
				
			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error(e.toString(), e);
				}
				e.printStackTrace();
				
				errorCount++;
				continue;
			}
		}
		
		if (errorCount == 0) {
			return composeMsg("選定恢復 {@} 組排程成功", "{@}", new Object[] {jsVOList.size()});
		} else {
			return composeMsg("選定恢復 {@} 組排程。成功: {@} 組；失敗: {@} 組", "{@}", new Object[] {jsVOList.size(), (jsVOList.size()-errorCount), errorCount});
		}
	}

	@Override
	public void modifyJob(JobServiceVO jsVO) throws Exception {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());
	        JobKey jobKey = JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());
	        
            if (!scheduler.checkExists(triggerKey) || !scheduler.checkExists(jobKey)) {
            	throw new Exception("欲修改的排程不存在，請重新操作");
            }
            
            List<JobServiceVO> jobVOs = new ArrayList<JobServiceVO>();
            jobVOs.add(jsVO);
            
            //build cron
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jsVO.getInputCronExpression());
            
            //assign priority
	        assignPriority(scheduleBuilder, jsVO);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            
            //build a new trigger with new cron
            trigger = trigger.getTriggerBuilder()
            				 .withIdentity(triggerKey)
            				 .withSchedule(scheduleBuilder)
            				 .withPriority(jsVO.getInputPriority() != null ? jsVO.getInputPriority() : Env.QUARTZ_DEFAULT_PRIORITY)
     	        			 .withDescription(jsVO.getInputDescription())
            				 .build();
            
            Set<CronTrigger> triggersForJob = new HashSet<CronTrigger>();
            triggersForJob.add(trigger);
            
            //build job
	        JobDetail jobDetail = scheduler.getJobDetail(jobKey)
	        							   .getJobBuilder()
	        							   .withIdentity(jsVO.getInputJobName(), jsVO.getInputJobGroup())
		   								   .usingJobData(composeJobDataMap(jsVO))
		   								   .withDescription(jsVO.getInputDescription())
		   								   .build();
            
            //scheduler.rescheduleJob(triggerKey, trigger);
	        
	        //reschedule job with new trigger >> set "true" for replace when jobKey already exists
	        scheduler.scheduleJob(jobDetail, triggersForJob, true);
	        
        } catch (SchedulerException e) {
        	if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
            throw new Exception("排程修改失敗");
        }
	}

	@Override
	public String deleteJob(List<JobServiceVO> jsVOList) throws Exception {
		int errorCount = 0;
		
		for (JobServiceVO jsVO : jsVOList) {
			try {
				TriggerKey triggerKey = TriggerKey.triggerKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());
				JobKey jobKey = JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());
				
				scheduler.pauseTrigger(triggerKey);
		        scheduler.unscheduleJob(triggerKey);
		        scheduler.deleteJob(jobKey);  
				
			} catch (Exception e) {
				if (log.isErrorEnabled()) {
					log.error(e.toString(), e);
				}
				e.printStackTrace();
				
				errorCount++;
				continue;
			}
		}
		
		if (errorCount == 0) {
			return composeMsg("選定刪除 {@} 組排程成功", "{@}", new Object[] {jsVOList.size()});
		} else {
			return composeMsg("選定刪除 {@} 組排程。成功: {@} 組；失敗: {@} 組", "{@}", new Object[] {jsVOList.size(), (jsVOList.size()-errorCount), errorCount});
		}
	}
	
	private String composeMsg(String oriStr, String symbol, Object[] replaceStrArray) {
		String[] splitArray = oriStr.split(symbol);
		
		if (splitArray != null && splitArray.length != 0) {
			StringBuffer sb = new StringBuffer();
			
			for (int i=0; i<splitArray.length; i++) {
				sb.append(splitArray[i]);
				
				try {
					sb.append(ObjectUtils.toString(replaceStrArray[i]));
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
			
			return sb.toString();
		}
		
		return oriStr;
	}
}
