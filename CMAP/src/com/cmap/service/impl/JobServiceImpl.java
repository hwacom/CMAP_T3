package com.cmap.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.QuartzDAO;
import com.cmap.dao.vo.QuartzDAOVO;
import com.cmap.model.QrtzCronTriggers;
import com.cmap.model.QrtzJobDetails;
import com.cmap.model.QrtzTriggers;
import com.cmap.service.BaseJobService;
import com.cmap.service.JobService;
import com.cmap.service.vo.JobServiceVO;
import com.cmap.utils.impl.CommonUtils;
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
	
	@Autowired
	private DeviceListDAO deviceListDAO;

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
			QuartzDAOVO daoVO = new QuartzDAOVO();
			daoVO.setJobKeyName(jsVO.getJobKeyName());
			daoVO.setJobKeyGroup(jsVO.getJobKeyGroup());
			
			List<Object[]> modelList = quartzDAO.findQuartzDataByDAOVO(daoVO);
			
			QrtzTriggers qt;
			QrtzCronTriggers qct;
			QrtzJobDetails qjd;
			if (modelList != null && !modelList.isEmpty()) {
				
				JobServiceVO retVO;
				for (Object[] modelObj : modelList) {
					qt = modelObj[0] != null ? (QrtzTriggers)modelObj[0] : new QrtzTriggers();
					qct = modelObj[1] != null ? (QrtzCronTriggers)modelObj[1] : new QrtzCronTriggers();
					qjd = modelObj[2] != null ? (QrtzJobDetails)modelObj[2] : new QrtzJobDetails();
					
					retVO = new JobServiceVO();
					BeanUtils.copyProperties(qt, retVO);
					BeanUtils.copyProperties(qct, retVO);
					BeanUtils.copyProperties(qjd, retVO);
					
					retVO.set_preFireTime(convertLongTime2DateStr(qt.getPrevFireTime()));
					retVO.set_nextFireTime(convertLongTime2DateStr(qt.getNextFireTime()));
					retVO.set_startTime(convertLongTime2DateStr(qt.getStartTime()));
					retVO.set_endTime(convertLongTime2DateStr(qt.getEndTime()));
					
					retVO.set_jobData(new String(qjd.getJobData()));
					
					Map<String, Object> jobDataMap = convertJobData(qjd);
					
					if (jobDataMap != null && !jobDataMap.isEmpty()) {
						final String schedType = (String)jobDataMap.get(Constants.QUARTZ_PARA_SCHED_TYPE);
						final String configType = (String)jobDataMap.get(Constants.QUARTZ_PARA_CONFIG_TYPE);
						final List<String> groupIds = (List<String>)jobDataMap.get(Constants.QUARTZ_PARA_GROUP_ID);
						final List<String> deviceIds = (List<String>)jobDataMap.get(Constants.QUARTZ_PARA_DEVICE_ID);
						
						final String groupIdsStr = String.join("\n", groupIds);
						final String deviceIdsStr = String.join("\n", deviceIds);
						
						retVO.setSchedType(schedType);
						retVO.setConfigType(configType);
						retVO.setGroupIdsStr(groupIdsStr);
						retVO.setDeviceIdsStr(deviceIdsStr);
					}
					
					retList.add(retVO);
				}
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			throw e;
		}

		return retList;
	}
	
	private Map<String, Object> convertJobData(QrtzJobDetails qjd) throws IOException, ClassNotFoundException {
		Map<String, Object> retMap = new HashMap<String, Object>();
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(qjd.getJobData()));
		JobDataMap jdm = (JobDataMap)ois.readObject();
		
		if (jdm != null && !jdm.isEmpty()) {
			final String schedType = (String)jdm.get(Constants.QUARTZ_PARA_SCHED_TYPE);
			final String groupId = (String)jdm.get(Constants.QUARTZ_PARA_GROUP_ID);
			final String deviceId = (String)jdm.get(Constants.QUARTZ_PARA_DEVICE_ID);
			final String configType = (String)jdm.get(Constants.QUARTZ_PARA_CONFIG_TYPE);
			
			ObjectMapper mapper = new ObjectMapper();
			List<String> groupIds = mapper.readValue(groupId, new TypeReference<List<String>>(){});
			List<String> deviceIds = mapper.readValue(deviceId, new TypeReference<List<String>>(){});
			
			retMap.put(Constants.QUARTZ_PARA_SCHED_TYPE, schedType);
			retMap.put(Constants.QUARTZ_PARA_CONFIG_TYPE, configType);
			retMap.put(Constants.QUARTZ_PARA_GROUP_ID, groupIds != null ? groupIds : new ArrayList<String>());
			retMap.put(Constants.QUARTZ_PARA_DEVICE_ID, deviceIds != null ? deviceIds : new ArrayList<String>());
		}
		
		return retMap;
	}
	
	@Override
	public JobServiceVO findJobDetails(JobServiceVO jsVO) throws Exception {
		JobServiceVO retVO = new JobServiceVO();
		
		try {
			QuartzDAOVO daoVO = new QuartzDAOVO();
			daoVO.setJobKeyGroup(jsVO.getJobKeyGroup());
			daoVO.setJobKeyName(jsVO.getJobKeyName());
			
			List<Object[]> modelList = quartzDAO.findQuartzDataByDAOVO(daoVO);
			
			if (modelList != null && !modelList.isEmpty()) {
				Object[] modelObj = modelList.get(0);
				
				QrtzJobDetails qjd = modelObj[2] != null ? (QrtzJobDetails)modelObj[2] : new QrtzJobDetails();
				
				Map<String, Object> jobDataMap = convertJobData(qjd);
				
				if (jobDataMap != null && !jobDataMap.isEmpty()) {
					final String schedType = (String)jobDataMap.get(Constants.QUARTZ_PARA_SCHED_TYPE);
					final String configType = (String)jobDataMap.get(Constants.QUARTZ_PARA_CONFIG_TYPE);
					final List<String> groupIds = (List<String>)jobDataMap.get(Constants.QUARTZ_PARA_GROUP_ID);
					final List<String> deviceIds = (List<String>)jobDataMap.get(Constants.QUARTZ_PARA_DEVICE_ID);
					
					List<Object[]> objList = null;
					StringBuffer groupIdsStr = new StringBuffer();
					if (groupIds != null && !groupIds.isEmpty()) {
						objList = deviceListDAO.getGroupIdAndNameByGroupIds(groupIds);
						for (Object[] obj : objList) {
							groupIdsStr.append("[ ").append(obj[0]).append(" ] :: ").append(obj[1]).append("\n");
						}
					}
					
					StringBuffer deviceIdsStr = new StringBuffer();
					if (deviceIds != null && !deviceIds.isEmpty()) {
						objList = deviceListDAO.getDeviceIdAndNameByDeviceIds(deviceIds);
						for (Object[] obj : objList) {
							deviceIdsStr.append("[ ").append(obj[0]).append(" ] :: ").append(obj[1]).append("\n");
						}
					}
					
					retVO.setSchedType(schedType);
					retVO.setConfigType(configType);
					retVO.setGroupIdsStr(groupIdsStr.toString());
					retVO.setDeviceIdsStr(deviceIdsStr.toString());
				}
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			throw e;
		}

		return retVO;
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
        	jobDataMap.put(Constants.QUARTZ_PARA_GROUP_ID, mapper.writeValueAsString(jsVO.getInputGroupIds()));
        	jobDataMap.put(Constants.QUARTZ_PARA_DEVICE_ID, mapper.writeValueAsString(jsVO.getInputDeviceIds()));
        	jobDataMap.put(Constants.QUARTZ_PARA_CONFIG_TYPE, jsVO.getInputConfigType());
        	jobDataMap.put(Constants.QUARTZ_PARA_SCHED_TYPE, jsVO.getInputSchedType());
	        
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
	        String cronExpression = jsVO.getInputCronExpression();	//"0 0/30 * * * ?"
	        
	        //build job
	        JobDetail jobDetail = JobBuilder
									.newJob(
										getClass(
											Env.SCHED_TYPE_CLASS_MAPPING.get(jsVO.getInputSchedType())
										).getClass())
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
            
            //start scheduler
            if (!scheduler.isStarted()) {
            	scheduler.start(); 
            }
	        
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
			
			throw e;
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
		
		return composeMsg("暫停", jsVOList.size(), errorCount);
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
		
		return composeMsg("恢復", jsVOList.size(), errorCount);
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
		
		return composeMsg("刪除", jsVOList.size(), errorCount);
	}
	
	private String getMsg() {
		return "選定{0} {1} 組排程。成功: {2} 組；失敗: {3} 組";
	}
	
	private String composeMsg(final String action, final int totalCount, final int errorCount) {
		final int successCount = totalCount - errorCount;
		return CommonUtils.converMsg(getMsg(), new Object[] {action, totalCount, successCount, errorCount});
	}
}
