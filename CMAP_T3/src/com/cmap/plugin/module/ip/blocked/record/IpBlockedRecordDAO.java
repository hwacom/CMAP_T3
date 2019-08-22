package com.cmap.plugin.module.ip.blocked.record;

import java.util.List;

public interface IpBlockedRecordDAO {

    /**
     * 取得符合條件資料筆數
     * @param irVO
     * @return
     */
    public long countModuleBlockedIpList(IpBlockedRecordVO irVO);

    /**
     * 取得符合條件資料
     * @param irVO
     * @param startRow
     * @param pageLength
     * @return Object[0]:ModuleBlockedIpList
     *         Object[1]:DeviceList
     */
    public List<Object[]> findModuleBlockedIpList(IpBlockedRecordVO irVO, Integer startRow, Integer pageLength);
}
