package com.cmap.service;

import java.util.List;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.JobServiceVO;

public interface JobService {

	/**
	 * 查找符合條件的資料筆數
	 * @param jsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public long countJobInfoByVO(JobServiceVO jsVO) throws ServiceLayerException;

	/**
	 * 查找符合條件的資料
	 * @param jsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public List<JobServiceVO> findJobInfoByVO(JobServiceVO jsVO) throws ServiceLayerException;

	/**
	 * 查看JOB參數明細
	 * @param jsVO
	 * @return
	 * @throws ServiceLayerException
	 */
	public JobServiceVO findJobDetails(JobServiceVO jsVO) throws ServiceLayerException;

	/**
	 * 建立JOB
	 * @param jsVO
	 * @throws ServiceLayerException
	 */
	public void addJob(JobServiceVO jsVO) throws ServiceLayerException;

	/**
	 * 暫停JOB
	 * @param jsVOList
	 * @return
	 * @throws ServiceLayerException
	 */
	public String pauseJob(List<JobServiceVO> jsVOList) throws ServiceLayerException;

	/**
	 * 重啟JOB
	 * @param jsVOList
	 * @return
	 * @throws ServiceLayerException
	 */
	public String resumeJob(List<JobServiceVO> jsVOList) throws ServiceLayerException;

	/**
	 * 修改JOB設定
	 * @param jsVO
	 * @throws ServiceLayerException
	 */
	public void modifyJob(JobServiceVO jsVO) throws ServiceLayerException;

	/**
	 * 刪除JOB
	 * @param jsVOList
	 * @return
	 * @throws ServiceLayerException
	 */
	public String deleteJob(List<JobServiceVO> jsVOList) throws ServiceLayerException;

	/**
	 * 立即執行JOB
	 * @param jsVOList
	 * @return
	 * @throws ServiceLayerException
	 */
	public String fireJobImmediately(List<JobServiceVO> jsVOList) throws ServiceLayerException;
}
