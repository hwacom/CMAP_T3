package com.cmap.plugin.module.firewall;

import java.util.List;
import java.util.Map;
import com.cmap.exception.ServiceLayerException;

public interface FirewallService {

    public List<String> getFieldNameList(String queryType, String fieldType);

    public List<FirewallVO> findFirewallLogSetting(String settingName);

    public long countFirewallLogRecordFromDB(FirewallVO fVO, Map<String, List<String>> fieldsMap) throws ServiceLayerException;

    public List<FirewallVO> findFirewallLogRecordFromDB(
            FirewallVO fVO, Integer startRow, Integer pageLength, Map<String, List<String>> fieldsMap) throws ServiceLayerException;

    public long countFirewallLogRecordFromDBbyAll(FirewallVO fVO, Map<String, List<String>> fieldsMap) throws ServiceLayerException;

    public List<FirewallVO> findFirewallLogRecordFromDBbyAll(
            FirewallVO fVO, Integer startRow, Integer pageLength, Map<String, List<String>> fieldsMap) throws ServiceLayerException;
}
