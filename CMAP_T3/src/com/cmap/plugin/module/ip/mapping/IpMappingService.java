package com.cmap.plugin.module.ip.mapping;

import java.util.Date;
import java.util.List;
import com.cmap.exception.ServiceLayerException;

public interface IpMappingService {

    public IpMappingServiceVO executeIpMappingPolling(String jobId, Date executeDate, String groupId) throws ServiceLayerException;

    public long countModuleIpMacPortMappingChange(IpMappingServiceVO imsVO) throws ServiceLayerException;

    public List<IpMappingServiceVO> findModuleIpMacPortMappingChange(IpMappingServiceVO imsVO) throws ServiceLayerException;

    public IpMappingServiceVO findMappingDataFromNetFlow(String groupId, String dataId, String fromDateTime, String type) throws ServiceLayerException;
}
