package com.cmap.service.impl.jobs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cmap.Constants;
import com.cmap.service.BaseJobService;
import com.cmap.service.VersionService;
import com.cmap.utils.impl.ApplicationContextUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@DisallowConcurrentExecution
public class JobBackupConfig implements BaseJobService {
	private static Log log = LogFactory.getLog(JobBackupConfig.class);
	
	private VersionService versionService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<String> deviceListIds = new ArrayList<String>();
		try {
			JobDataMap jMap = context.getJobDetail().getJobDataMap();
			String deviceListId = jMap.getString(Constants.QUARTZ_PARA_JSON_STR);
			System.out.println("deviceListId: "+deviceListId);
			
			String configType = jMap.getString(Constants.QUARTZ_PARA_CONFIG_TYPE);
			System.out.println("configType: "+configType);
			
			ObjectMapper mapper = new ObjectMapper();
			deviceListIds = mapper.readValue(deviceListId, new TypeReference<List<String>>(){});
			
			if (deviceListIds != null && !deviceListIds.isEmpty()) {
				versionService = (VersionService)ApplicationContextUtil.getBean("versionService");
				versionService.backupConfig(configType, deviceListIds, true);
			}
			
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(e.toString(), e);
			}
			e.printStackTrace();
		}
	}

}
