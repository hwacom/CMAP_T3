package com.cmap.service;

import java.util.List;

import com.cmap.service.vo.JobServiceVO;

public interface JobService {

	public long countJobInfoByVO(JobServiceVO jsVO) throws Exception; 
	
	public List<JobServiceVO> findJobInfoByVO(JobServiceVO jsVO) throws Exception;
	
	public void addJob(JobServiceVO jsVO) throws Exception;

	public String pauseJob(List<JobServiceVO> jsVOList) throws Exception;
	
	public String resumeJob(List<JobServiceVO> jsVOList) throws Exception;
	
	public void modifyJob(JobServiceVO jsVO) throws Exception;
	
	public String deleteJob(List<JobServiceVO> jsVOList) throws Exception;
}
