package com.cmap.service;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.FileOperationServiceVO;

public interface FileOperationService {

	public FileOperationServiceVO executeFileOperation(String settingId) throws ServiceLayerException;
}
