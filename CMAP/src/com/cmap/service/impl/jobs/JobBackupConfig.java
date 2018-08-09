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
import com.cmap.dao.DeviceListDAO;
import com.cmap.model.DeviceList;
import com.cmap.service.BaseJobService;
import com.cmap.service.VersionService;
import com.cmap.utils.impl.ApplicationContextUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@DisallowConcurrentExecution
public class JobBackupConfig implements BaseJobService {
	private static Log log = LogFactory.getLog(JobBackupConfig.class);
	
	private VersionService versionService;
	
	private DeviceListDAO deviceListDAO;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<String> deviceListIds = new ArrayList<String>();
		List<String> groupIds = new ArrayList<String>();
		List<String> deviceIds = new ArrayList<String>();
		
		try {
			JobDataMap jMap = context.getJobDetail().getJobDataMap();
			
			String groupId = jMap.getString(Constants.QUARTZ_PARA_GROUP_ID);
			System.out.println("groupId: "+groupId);
			
			String deviceId = jMap.getString(Constants.QUARTZ_PARA_DEVICE_ID);
			System.out.println("deviceId: "+deviceId);
			
			String configType = jMap.getString(Constants.QUARTZ_PARA_CONFIG_TYPE);
			System.out.println("configType: "+configType);
			
			ObjectMapper mapper = new ObjectMapper();
			groupIds = mapper.readValue(groupId, new TypeReference<List<String>>(){});
			deviceIds = mapper.readValue(deviceId, new TypeReference<List<String>>(){});
			
			List<DeviceList> dList = null;
			if ((groupIds != null && !groupIds.isEmpty()) || (deviceIds != null && !deviceIds.isEmpty())) {
				deviceListDAO = (DeviceListDAO)ApplicationContextUtil.getBean("deviceListDAO");
				dList = deviceListDAO.findDistinctDeviceListByGroupIdsOrDeviceIds(groupIds, deviceIds);
				
				if (dList != null && !dList.isEmpty()) {
					for (DeviceList d : dList) {
						deviceListIds.add(d.getDeviceListId());
					}
				}
			}
			
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
