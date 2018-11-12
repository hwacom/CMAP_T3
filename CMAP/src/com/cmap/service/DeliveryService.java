package com.cmap.service;

import java.util.List;

import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.DeliveryServiceVO;

public interface DeliveryService {

	public long countDeviceList(DeliveryServiceVO dsVO) throws ServiceLayerException;

	public List<DeliveryServiceVO> findDeviceList(DeliveryServiceVO dsVO, Integer startRow, Integer pageLength) throws ServiceLayerException;

	public long countScriptList(DeliveryServiceVO dsVO) throws ServiceLayerException;

	public List<DeliveryServiceVO> findScriptList(DeliveryServiceVO dsVO, Integer startRow, Integer pageLength) throws ServiceLayerException;

	public DeliveryServiceVO getScriptInfoById(String scriptInfoId) throws ServiceLayerException;
}
