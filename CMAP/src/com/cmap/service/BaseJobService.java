package com.cmap.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface BaseJobService extends Job {

	public void execute(JobExecutionContext context) throws JobExecutionException;
	
	/*
	public void backupConfigFiles() throws Exception;
	
	public void cleanUpFtpConfigFiles() throws Exception;
	
	public void cleanUpTableRecords() throws Exception;
	
	public void moveTableRecords2Backup() throws Exception;
	*/
}
