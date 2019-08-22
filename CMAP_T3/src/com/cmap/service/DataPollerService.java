package com.cmap.service;

import java.util.List;
import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.DataPollerServiceVO;

public interface DataPollerService {

	public static final String FIELD_TYPE_SOURCE = "SOURCE";
	public static final String FIELD_TYPE_TARGET = "TARGET";

	public List<String> getFieldName(String settingId, String fieldType) throws ServiceLayerException;

	public String getStoreMethodByDataType(String dataType) throws ServiceLayerException;

	public String getRecordBySetting(String dataType) throws ServiceLayerException;

	public DataPollerServiceVO executePolling(String settingId) throws ServiceLayerException;

	/**
	 * 若 Data Poller 設定為資料解析與資料寫入DB流程分開，此方法為寫入DB流程入口方法
	 * @param settingId
	 * @return
	 * @throws ServiceLayerException
	 */
	public DataPollerServiceVO saveData2DB(String settingId) throws ServiceLayerException;
}
