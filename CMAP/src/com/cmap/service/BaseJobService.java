package com.cmap.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface BaseJobService extends Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException;

	/*
	public void backupConfigFiles() throws ServiceLayerException;

	public void cleanUpFtpConfigFiles() throws ServiceLayerException;

	public void cleanUpTableRecords() throws ServiceLayerException;

	public void moveTableRecords2Backup() throws ServiceLayerException;
	 */
}
