package com.cmap.service;

import java.util.List;

import com.cmap.service.vo.JobServiceVO;

public interface JobService {

	/**
	 * 查找符合條件的資料筆數
	 * @param jsVO
	 * @return
	 * @throws Exception
	 */
	public long countJobInfoByVO(JobServiceVO jsVO) throws Exception; 
	
	/**
	 * 查找符合條件的資料
	 * @param jsVO
	 * @return
	 * @throws Exception
	 */
	public List<JobServiceVO> findJobInfoByVO(JobServiceVO jsVO) throws Exception;
	
	/**
	 * 查看JOB參數明細
	 * @param jsVO
	 * @return
	 * @throws Exception
	 */
	public JobServiceVO findJobDetails(JobServiceVO jsVO) throws Exception;
	
	/**
	 * 建立JOB
	 * @param jsVO
	 * @throws Exception
	 */
	public void addJob(JobServiceVO jsVO) throws Exception;

	/**
	 * 暫停JOB
	 * @param jsVOList
	 * @return
	 * @throws Exception
	 */
	public String pauseJob(List<JobServiceVO> jsVOList) throws Exception;
	
	/**
	 * 重啟JOB
	 * @param jsVOList
	 * @return
	 * @throws Exception
	 */
	public String resumeJob(List<JobServiceVO> jsVOList) throws Exception;
	
	/**
	 * 修改JOB設定
	 * @param jsVO
	 * @throws Exception
	 */
	public void modifyJob(JobServiceVO jsVO) throws Exception;
	
	/**
	 * 刪除JOB
	 * @param jsVOList
	 * @return
	 * @throws Exception
	 */
	public String deleteJob(List<JobServiceVO> jsVOList) throws Exception;
	
	/**
	 * 立即執行JOB
	 * @param jsVOList
	 * @return
	 * @throws Exception
	 */
	public String fireJobImmediately(List<JobServiceVO> jsVOList) throws Exception;
}
