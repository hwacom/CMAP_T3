package com.cmap.service;

import java.util.List;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.DataPollerServiceVO;

public interface DataPollerService {

	public static final String FIELD_TYPE_SOURCE = "SOURCE";
	public static final String FIELD_TYPE_TARGET = "TARGET";

	public List<String> getFieldName(String settingId, String fieldType) throws ServiceLayerException;

	public DataPollerServiceVO excutePolling(String settingId) throws ServiceLayerException;
}
