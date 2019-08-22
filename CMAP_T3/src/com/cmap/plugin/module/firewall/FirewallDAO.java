package com.cmap.plugin.module.firewall;

import java.util.List;
import java.util.Map;

public interface FirewallDAO {

    /**
     * 查找 Module_Firewall_Log_Setting 設定資料
     * @param settingName
     * @return
     */
    public List<ModuleFirewallLogSetting> getFirewallLogSetting(String settingName);

    /**
     * 取得符合條件資料筆數 for 特定查詢類別
     * @param fVO
     * @param searchLikeField
     * @param tableName
     * @return
     */
    public long countFirewallLogFromDB(FirewallVO fVO, List<String> searchLikeField, String tableName);

    /**
     * 取得符合條件資料 for 特定查詢類別
     * @param fVO
     * @param startRow
     * @param pageLength
     * @param searchLikeField
     * @param tableName
     * @param selectSql
     * @return
     */
    public List<Object[]> findFirewallLogFromDB(
            FirewallVO fVO, Integer startRow, Integer pageLength, List<String> searchLikeField,
            String tableName, String selectSql);

    /**
     * 取得符合條件資料筆數 for 查詢類別為ALL
     * @param fVO
     * @param searchLikeField
     * @return
     */
    public long countFirewallLogFromDBbyAll(FirewallVO fVO, Map<String, List<String>> searchLikeFieldMap);

    /**
     * 取得符合條件資料 for 查詢類別為ALL
     * @param fVO
     * @param startRow
     * @param pageLength
     * @param searchLikeField
     * @return
     */
    public List<Object[]> findFirewallLogFromDBbyAll(
            FirewallVO fVO, Integer startRow, Integer pageLength, Map<String, String> selectSqlMap, Map<String, List<String>> searchLikeFieldMap);
}
