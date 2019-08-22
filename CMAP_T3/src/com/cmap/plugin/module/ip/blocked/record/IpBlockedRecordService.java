package com.cmap.plugin.module.ip.blocked.record;

import java.util.List;
import com.cmap.exception.ServiceLayerException;

public interface IpBlockedRecordService {

    /**
     * 取得符合條件資料筆數
     * @param irVO
     * @return
     * @throws ServiceLayerException
     */
    public long countModuleBlockedIpList(IpBlockedRecordVO irVO) throws ServiceLayerException;

    /**
     * 查詢符合條件資料
     * @param irVO
     * @return
     * @throws ServiceLayerException
     */
    public List<IpBlockedRecordVO> findModuleBlockedIpList(IpBlockedRecordVO irVO, Integer startRow, Integer pageLength) throws ServiceLayerException;
}
