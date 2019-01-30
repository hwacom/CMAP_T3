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
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.QuartzDAO;
import com.cmap.dao.vo.QuartzDAOVO;
import com.cmap.exception.ConnectionException;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.QrtzCronTriggers;
import com.cmap.model.QrtzJobDetails;
import com.cmap.model.QrtzTriggers;
import com.cmap.service.BaseJobService;
import com.cmap.service.CommonService;
import com.cmap.service.JobService;
import com.cmap.service.vo.JobServiceVO;
import com.cmap.utils.impl.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("jobService")
@Transactional
public class JobServiceImpl implements JobService {
	@Log
	private static Logger log;

	@Autowired @Qualifier("Scheduler")
	private Scheduler scheduler;

	@Autowired
	private QuartzDAO quartzDAO;

	@Autowired
	private DeviceListDAO deviceListDAO;

	@Autowired
	private CommonService commonService;

	@Override
	public long countJobInfoByVO(JobServiceVO jsVO) throws ServiceLayerException {
		long count = 0;

		try {
			count = quartzDAO.countQuartzDataByDAOVO(null);

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return count;
	}

	private String convertLongTime2DateStr(Long longTime) {
		return Constants.FORMAT_YYYYMMDD_HH24MISS.format(new Date(longTime));
	}

	@Override
	public List<JobServiceVO> findJobInfoByVO(JobServiceVO jsVO) throws ServiceLayerException {
		List<JobServiceVO> retList = new ArrayList<>();

		try {
			QuartzDAOVO daoVO = new QuartzDAOVO();
			BeanUtils.copyProperties(jsVO, daoVO);

			List<Object[]> modelList = quartzDAO.findQuartzDataByDAOVO(daoVO);

			QrtzTriggers qt;
			QrtzCronTriggers qct;
			QrtzJobDetails qjd;
			if (modelList != null && !modelList.isEmpty()) {

				Map<String, String> menuItemMap = commonService.getMenuItem(Env.MENU_CODE_OF_SCHED_TYPE, false);

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

						final String groupIdsStr = groupIds != null ? String.join("\n", groupIds) : "";
						final String deviceIdsStr = deviceIds != null ? String.join("\n", deviceIds) : "";

						final String ftpName = (String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_NAME);
						final String ftpHost = (String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_HOST);
						final String ftpPort = (String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_PORT);
						final String ftpAccount = (String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_ACCOUNT);
						final String ftpPassword = (String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_PASSWORD);

						final List<String> sqls = (List<String>)jobDataMap.get(Constants.QUARTZ_PARA_SYS_CHECK_SQLS);

						final String dataPollerSettingId = (String)jobDataMap.get(Constants.QUARTZ_PARA_DATA_POLLER_SETTING_ID);
						final String localFileOperationSettingId = (String)jobDataMap.get(Constants.QUARTZ_PARA_JOB_FILE_OPERATION_SETTING_ID);

						final String sqlsStr = sqls != null ? String.join("\n", sqls) : "";

						retVO.setSchedType(schedType);
						retVO.setSchedTypeName(menuItemMap.get(schedType));
						retVO.setConfigType(configType);
						retVO.setGroupIdsStr(groupIdsStr);
						retVO.setDeviceIdsStr(deviceIdsStr);

						retVO.setFtpName(ftpName);
						retVO.setFtpHost(ftpHost);
						retVO.setFtpPort(ftpPort);
						retVO.setFtpAccount(ftpAccount);
						retVO.setFtpPassword(ftpPassword);

						retVO.setSysCheckSqlStr(sqlsStr);

						retVO.setDataPollerSettingId(dataPollerSettingId);
						retVO.setLocalFileOperationSettingId(localFileOperationSettingId);
					}

					retList.add(retVO);
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e);
		}

		return retList;
	}

	private Map<String, Object> convertJobData(QrtzJobDetails qjd) throws IOException, ClassNotFoundException {
		Map<String, Object> retMap = new HashMap<>();

		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(qjd.getJobData()));
		JobDataMap jdm = (JobDataMap)ois.readObject();

		if (jdm != null && !jdm.isEmpty()) {
			final String schedType = (String)jdm.get(Constants.QUARTZ_PARA_SCHED_TYPE);
			final String groupId = (String)jdm.get(Constants.QUARTZ_PARA_GROUP_ID);
			final String deviceId = (String)jdm.get(Constants.QUARTZ_PARA_DEVICE_ID);
			final String configType = (String)jdm.get(Constants.QUARTZ_PARA_CONFIG_TYPE);
			final String ftpName = (String)jdm.get(Constants.QUARTZ_PARA_FTP_NAME);
			final String ftpHost = (String)jdm.get(Constants.QUARTZ_PARA_FTP_HOST);
			final String ftpPort = (String)jdm.get(Constants.QUARTZ_PARA_FTP_PORT);
			final String ftpAccount = (String)jdm.get(Constants.QUARTZ_PARA_FTP_ACCOUNT);
			final String ftpPassword = (String)jdm.get(Constants.QUARTZ_PARA_FTP_PASSWORD);
			final List<String> sysChecksqls = (List<String>)jdm.get(Constants.QUARTZ_PARA_SYS_CHECK_SQLS);
			final String dataPollerSettingId = (String)jdm.get(Constants.QUARTZ_PARA_DATA_POLLER_SETTING_ID);
			final String localFileOperationSettingId = (String)jdm.get(Constants.QUARTZ_PARA_JOB_FILE_OPERATION_SETTING_ID);

			ObjectMapper mapper = new ObjectMapper();
			List<String> groupIds = groupId != null ? mapper.readValue(groupId, new TypeReference<List<String>>(){}) : null;
			List<String> deviceIds = deviceId != null ? mapper.readValue(deviceId, new TypeReference<List<String>>(){}) : null;

			retMap.put(Constants.QUARTZ_PARA_SCHED_TYPE, schedType);
			retMap.put(Constants.QUARTZ_PARA_CONFIG_TYPE, configType);
			retMap.put(Constants.QUARTZ_PARA_GROUP_ID, groupIds != null ? groupIds : new ArrayList<String>());
			retMap.put(Constants.QUARTZ_PARA_DEVICE_ID, deviceIds != null ? deviceIds : new ArrayList<String>());
			retMap.put(Constants.QUARTZ_PARA_FTP_NAME, ftpName);
			retMap.put(Constants.QUARTZ_PARA_FTP_HOST, ftpHost);
			retMap.put(Constants.QUARTZ_PARA_FTP_PORT, ftpPort);
			retMap.put(Constants.QUARTZ_PARA_FTP_ACCOUNT, ftpAccount);
			retMap.put(Constants.QUARTZ_PARA_FTP_PASSWORD, ftpPassword);
			retMap.put(Constants.QUARTZ_PARA_SYS_CHECK_SQLS, sysChecksqls);
			retMap.put(Constants.QUARTZ_PARA_DATA_POLLER_SETTING_ID, dataPollerSettingId);
			retMap.put(Constants.QUARTZ_PARA_JOB_FILE_OPERATION_SETTING_ID, localFileOperationSettingId);
		}

		return retMap;
	}

	@Override
	public JobServiceVO findJobDetails(JobServiceVO jsVO) throws ServiceLayerException {
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
					Map<String, String> menuItemMap = commonService.getMenuItem(Env.MENU_CODE_OF_SCHED_TYPE, false);

					final String schedType = (String)jobDataMap.get(Constants.QUARTZ_PARA_SCHED_TYPE);
					final String configType = (String)jobDataMap.get(Constants.QUARTZ_PARA_CONFIG_TYPE);

					if (StringUtils.equals(schedType, Constants.QUARTZ_SCHED_TYPE_BACKUP_CONFIG)) {

						final List<String> groupIds = (List<String>)jobDataMap.get(Constants.QUARTZ_PARA_GROUP_ID);
						final List<String> deviceIds = (List<String>)jobDataMap.get(Constants.QUARTZ_PARA_DEVICE_ID);

						List<Object[]> objList = null;
						StringBuffer groupIdsStr = new StringBuffer();
						if (groupIds != null && !groupIds.isEmpty()) {
							objList = deviceListDAO.getGroupIdAndNameByGroupIds(groupIds);

							if (objList == null || (objList != null && objList.isEmpty())) {
								objList = new ArrayList<>();

								Object[] obj;
								for (String groupId : groupIds) {
									obj = new Object[] {groupId, "Group ID 不存在"};
									objList.add(obj);
								}
							}

							for (Object[] obj : objList) {
								groupIdsStr.append("[ ").append(obj[0]).append(" ] :: ").append(obj[1]).append("\n");
							}
						}

						StringBuffer deviceIdsStr = new StringBuffer();
						if (deviceIds != null && !deviceIds.isEmpty()) {
							objList = deviceListDAO.getDeviceIdAndNameByDeviceIds(deviceIds);

							if (objList == null || (objList != null && objList.isEmpty())) {
								objList = new ArrayList<>();

								Object[] obj;
								for (String deviceId : deviceIds) {
									obj = new Object[] {deviceId, "Device ID 不存在"};
									objList.add(obj);
								}
							}

							for (Object[] obj : objList) {
								deviceIdsStr.append("[ ").append(obj[0]).append(" ] :: ").append(obj[1]).append("\n");
							}
						}

						retVO.setGroupIdsStr(groupIdsStr.toString());
						retVO.setDeviceIdsStr(deviceIdsStr.toString());

					} else if (StringUtils.equals(schedType, Constants.QUARTZ_SCHED_TYPE_UPLOAD_BACKUP_CONFIG_FILE_2_FTP)) {

						retVO.setFtpName((String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_NAME));
						retVO.setFtpHost((String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_HOST));
						retVO.setFtpPort((String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_PORT));
						retVO.setFtpAccount((String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_ACCOUNT));
						retVO.setFtpPassword((String)jobDataMap.get(Constants.QUARTZ_PARA_FTP_PASSWORD));

					} else if (StringUtils.equals(schedType, Constants.QUARTZ_SCHED_TYPE_SYS_CHECK_UPDATE)
							|| StringUtils.equals(schedType, Constants.QUARTZ_SCHED_TYPE_SYS_CHECK_QUERY)) {

						List<String> sqls = (List<String>)jobDataMap.get(Constants.QUARTZ_PARA_SYS_CHECK_SQLS);

						StringBuffer sqlStr = new StringBuffer();
						for (String sql : sqls) {
							sqlStr.append(sql).append("\n");
						}
						retVO.setSysCheckSqlStr(sqlStr.toString());

					} else if (StringUtils.equals(schedType, Constants.QUARTZ_SCHED_TYPE_DATA_POLLER)
							|| StringUtils.equals(schedType, Constants.QUARTZ_SCHED_TYPE_LOCAL_FILE_OPERATION)) {

						String dataPollerSettingId =
								jobDataMap.containsKey(Constants.QUARTZ_PARA_DATA_POLLER_SETTING_ID) ?
										(String)jobDataMap.get(Constants.QUARTZ_PARA_DATA_POLLER_SETTING_ID) : null;

						String localFileOperationSettingId =
								jobDataMap.containsKey(Constants.QUARTZ_PARA_JOB_FILE_OPERATION_SETTING_ID) ?
										(String)jobDataMap.get(Constants.QUARTZ_PARA_JOB_FILE_OPERATION_SETTING_ID) : null;

						retVO.setDataPollerSettingId(dataPollerSettingId);
						retVO.setLocalFileOperationSettingId(localFileOperationSettingId);
					}

					retVO.setSchedType(schedType);
					retVO.setSchedTypeName(menuItemMap.get(schedType));
					retVO.setConfigType(configType);
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e);
		}

		return retVO;
	}

	public static BaseJobService getClass(String classname) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Class<?> class1 = Class.forName(classname);
		return (BaseJobService)class1.newInstance();
	}

	private JobDataMap composeJobDataMap(JobServiceVO jsVO) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(Constants.QUARTZ_PARA_SCHED_TYPE, jsVO.getInputSchedType());

		switch (jsVO.getInputSchedType()) {
			case Constants.QUARTZ_SCHED_TYPE_BACKUP_CONFIG:
				jobDataMap.put(Constants.QUARTZ_PARA_DEVICE_LIST_ID, mapper.writeValueAsString(jsVO.getInputDeviceListIds()));
				jobDataMap.put(Constants.QUARTZ_PARA_GROUP_ID, mapper.writeValueAsString(jsVO.getInputGroupIds()));
				jobDataMap.put(Constants.QUARTZ_PARA_DEVICE_ID, mapper.writeValueAsString(jsVO.getInputDeviceIds()));
				jobDataMap.put(Constants.QUARTZ_PARA_CONFIG_TYPE, jsVO.getInputConfigType());
				break;

			case Constants.QUARTZ_SCHED_TYPE_CLEAN_UP_FTP_FILE:
				break;

			case Constants.QUARTZ_SCHED_TYPE_CLEAN_UP_DB_DATA:
				break;

			case Constants.QUARTZ_SCHED_TYPE_SYS_CHECK_UPDATE:
				jobDataMap.put(Constants.QUARTZ_PARA_SYS_CHECK_SQLS, jsVO.getInputSysCheckSql());
				break;

			case Constants.QUARTZ_SCHED_TYPE_SYS_CHECK_QUERY:
				jobDataMap.put(Constants.QUARTZ_PARA_SYS_CHECK_SQLS, jsVO.getInputSysCheckSql());
				break;

			case Constants.QUARTZ_SCHED_TYPE_UPLOAD_BACKUP_CONFIG_FILE_2_FTP:
				jobDataMap.put(Constants.QUARTZ_PARA_FTP_NAME, jsVO.getInputFtpName());
				jobDataMap.put(Constants.QUARTZ_PARA_FTP_HOST, jsVO.getInputFtpHost());
				jobDataMap.put(Constants.QUARTZ_PARA_FTP_PORT, jsVO.getInputFtpPort());
				jobDataMap.put(Constants.QUARTZ_PARA_FTP_ACCOUNT, jsVO.getInputFtpAccount());
				jobDataMap.put(Constants.QUARTZ_PARA_FTP_PASSWORD, jsVO.getInputFtpPassword());
				break;

			case Constants.QUARTZ_SCHED_TYPE_DATA_POLLER:
				jobDataMap.put(Constants.QUARTZ_PARA_DATA_POLLER_SETTING_ID, jsVO.getInputDataPollerSettingId());
				break;

			case Constants.QUARTZ_SCHED_TYPE_LOCAL_FILE_OPERATION:
				jobDataMap.put(Constants.QUARTZ_PARA_JOB_FILE_OPERATION_SETTING_ID, jsVO.getInputLocalFileOperationSettingId());
				break;
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
	public void addJob(JobServiceVO jsVO) throws ServiceLayerException {
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
					.forJob(jsVO.getInputJobName(), jsVO.getInputJobGroup())
					.build();

			System.out.println("JobKey exists: " + scheduler.checkExists(jobDetail.getKey()));

			if (scheduler.checkExists(jobDetail.getKey())) {
				scheduler.deleteJob(jobDetail.getKey());
			}

			System.out.println("Trigger exists: " + scheduler.checkExists(trigger.getKey()));

			scheduler.scheduleJob(jobDetail, trigger);

			//start scheduler
			if (!scheduler.isStarted()) {
				scheduler.start();
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException(e);
		}
	}

	@Override
	public String pauseJob(List<JobServiceVO> jsVOList) throws ServiceLayerException {
		int errorCount = 0;

		for (JobServiceVO jsVO : jsVOList) {
			try {
				scheduler.pauseJob(JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup()));
				scheduler.pauseTrigger(TriggerKey.triggerKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup()));

			} catch (Exception e) {
				log.error(e.toString(), e);

				errorCount++;
				continue;
			}
		}

		return composeMsg("暫停", jsVOList.size(), errorCount);
	}

	@Override
	public String resumeJob(List<JobServiceVO> jsVOList) throws ServiceLayerException {
		int errorCount = 0;

		for (JobServiceVO jsVO : jsVOList) {
			try {
				scheduler.resumeTrigger(TriggerKey.triggerKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup()));
				scheduler.resumeJob(JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup()));

			} catch (Exception e) {
				log.error(e.toString(), e);

				errorCount++;
				continue;
			}
		}

		return composeMsg("恢復", jsVOList.size(), errorCount);
	}

	@Override
	public void modifyJob(JobServiceVO jsVO) throws ServiceLayerException {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());
			JobKey jobKey = JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());

			if (!scheduler.checkExists(triggerKey) || !scheduler.checkExists(jobKey)) {
				throw new ServiceLayerException("欲修改的排程不存在，請重新操作");
			}

			List<JobServiceVO> jobVOs = new ArrayList<>();
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

			Set<CronTrigger> triggersForJob = new HashSet<>();
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

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("排程修改失敗");
		}
	}

	@Override
	public String deleteJob(List<JobServiceVO> jsVOList) throws ServiceLayerException {
		int errorCount = 0;

		for (JobServiceVO jsVO : jsVOList) {
			try {
				TriggerKey triggerKey = TriggerKey.triggerKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());
				JobKey jobKey = JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());

				scheduler.pauseTrigger(triggerKey);
				scheduler.unscheduleJob(triggerKey);
				scheduler.deleteJob(jobKey);

			} catch (Exception e) {
				log.error(e.toString(), e);

				errorCount++;
				continue;
			}
		}

		return composeMsg("刪除", jsVOList.size(), errorCount);
	}

	private String getMsg() {
		return "選定{0} {1} 組排程。<br>成功: {2} 組；失敗: {3} 組";
	}

	private String composeMsg(final String action, final int totalCount, final int errorCount) {
		final int successCount = totalCount - errorCount;
		return CommonUtils.converMsg(getMsg(), new Object[] {action, totalCount, successCount, errorCount});
	}

	@Override
	public String fireJobImmediately(List<JobServiceVO> jsVOList) throws ServiceLayerException {
		int errorCount = 0;

		for (JobServiceVO jsVO : jsVOList) {
			try {

				TriggerKey triggerKey = TriggerKey.triggerKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());
				JobKey jobKey = JobKey.jobKey(jsVO.getJobKeyName(), jsVO.getJobKeyGroup());

				if (!scheduler.checkExists(triggerKey) || !scheduler.checkExists(jobKey)) {
					throw new Exception("欲執行的排程不存在，請重新操作");
				}

				scheduler.triggerJob(jobKey);

			} catch (Exception e) {
				if (e instanceof ConnectionException) {
					log.error(e.toString());

				} else {
					log.error(e.toString(), e);
				}

				errorCount++;
				continue;
			}
		}

		return composeMsg("執行", jsVOList.size(), errorCount);
	}
}
