package com.cmap.plugin.module.netflow.statistics;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.cmap.exception.ServiceLayerException;
import com.cmap.plugin.module.netflow.NetFlowVO;

public interface NetFlowStatisticsService {

    public NetFlowVO executeNetFlowIpStatistics() throws ServiceLayerException;

    /**
     * 由 Data_Poller 呼叫，寫入/更新 IP 流量
     * @param groupId
     * @param statDate
     * @param ipTrafficMap
     * @throws ServiceLayerException
     */
    public void calculateIpTrafficStatistics(
            String groupId, Date statDate, Map<String, Map<String, Long>> ipTrafficMap) throws ServiceLayerException;

    /**
     * 取得 NET_FLOW 流量統計資料筆數
     * @param nfsVO
     * @return
     * @throws ServiceLayerException
     */
    public long countModuleIpTrafficStatistics(NetFlowStatisticsVO nfsVO) throws ServiceLayerException;

    /**
     * 查找 NET_FLOW 流量統計資料
     * @param nfsVO
     * @return
     * @throws ServiceLayerException
     */
    public List<NetFlowStatisticsVO> findModuleIpTrafficStatistics(NetFlowStatisticsVO nfsVO) throws ServiceLayerException;

    /**
     * 取得 NET_FLOW Session數 統計資料筆數
     * @param nfsVO
     * @return
     * @throws ServiceLayerException
     */
    public long countModuleSessionStatistics(NetFlowStatisticsVO nfsVO) throws ServiceLayerException;

    /**
     * 查找 NET_FLOW Session數 統計資料
     * @param nfsVO
     * @return
     * @throws ServiceLayerException
     */
    public List<NetFlowStatisticsVO> findModuleSessionStatistics(NetFlowStatisticsVO nfsVO) throws ServiceLayerException;
}
