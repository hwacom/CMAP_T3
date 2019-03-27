package com.cmap.dao;

import java.util.List;

import com.cmap.dao.vo.ProvisionLogDAOVO;
import com.cmap.model.ProvisionAccessLog;
import com.cmap.model.ProvisionLogDetail;
import com.cmap.model.ProvisionLogDevice;
import com.cmap.model.ProvisionLogMaster;
import com.cmap.model.ProvisionLogRetry;
import com.cmap.model.ProvisionLogStep;

public interface ProvisionLogDAO extends BaseDAO {

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
	public List<Object[]> findProvisionLogByDAOVO(ProvisionLogDAOVO daovo, Integer startRow, Integer pageLength);


	/**
	 * 新增供裝紀錄
	 * @param master
	 * @param details
	 * @param steps
	 * @param devices
	 */
	public void insertProvisionLog(ProvisionLogMaster master, List<ProvisionLogDetail> details, List<ProvisionLogStep> steps, List<ProvisionLogDevice> devices, List<ProvisionLogRetry> retrys);

	public ProvisionAccessLog findProvisionAccessLogById(String logId);

	/**
	 * 以 PK 欄位查找供裝步驟檔 >> Provision_Log_Step
	 * @param logStepId
	 * @return
	 */
	public ProvisionLogStep findProvisionLogStepById(String logStepId);
}
