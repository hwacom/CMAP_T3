package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ProvisionLogDAOVO;
import com.cmap.model.ProvisionLogDevice;
import com.cmap.model.ProvisionLogMaster;
import com.cmap.model.ProvisionLogRetry;
import com.cmap.model.ProvisionLogStep;

public interface ProvisionLogDAO {

	/**
	 * 依查詢條件查找符合的資料筆數
	 * @param daovo
	 * @return
	 */
	public long countProvisionLogByDAOVO(ProvisionLogDAOVO daovo);

	/**
	 * 依查詢條件查找供裝紀錄
	 * @param daovo
	 * @return
	 */
	public List<ProvisionLogMaster> findProvisionLogByDAOVO(ProvisionLogDAOVO daovo);

	/**
	 * 新增供裝紀錄
	 * @param master
	 * @param steps
	 * @param devices
	 */
	public void insertProvisionLog(ProvisionLogMaster master, List<ProvisionLogStep> steps, List<ProvisionLogDevice> devices, List<ProvisionLogRetry> retrys);
}
