package com.cmap.plugin.module.netflow;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.cmap.dao.BaseDAO;
import com.cmap.model.DataPollerSetting;
import com.cmap.service.vo.DataPollerServiceVO;

public interface NetFlowDAO extends BaseDAO {

	public long countNetFlowDataFromDB(NetFlowVO nfVO, List<String> searchLikeField, String tableName);

	public List<Object[]> findNetFlowDataFromDB(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField, String tableName, String selectSql);

	public BigDecimal getTotalFlowOfQueryConditionsFromDB(NetFlowVO nfVO, List<String> searchLikeField, String tableName);

	public NetFlowVO findNetFlowDataFromFile(
			DataPollerSetting setting,
			Map<Integer, DataPollerServiceVO> fieldIdxMap,
			Map<String, DataPollerServiceVO> fieldVOMap,
			Map<String, NetFlowVO> queryMap,
			Integer startRow,
			Integer pageLength);

	/**
	 * 以GROUP_ID查找對應表取得要寫入的目標TABLE_NAME
	 * @param groupId
	 * @return
	 */
	public String findTargetTableNameByGroupId(String groupId);

	/**
	 * 取得已設定好Data_Poller_Setting的資料 (判斷方式: File_Name_Regex != Env.DEFAULT_NET_FLOW_FILE_NAME_REGEX)
	 * @return
	 */
	public List<DataPollerSetting> getHasAlreadySetUpNetFlowDataPollerInfo();

	/**
	 * 取得上傳流量(Source_IP)超過設定值的IP資料
	 * @param tableName
	 * @param limitSize
	 * @return
	 */
	public List<Object[]> getUploadFlowExceedLimitSizeIpData(String tableName, String nowDateStr, String limitSize);

	/**
	 * 取得下載流量(Destination_IP)超過設定值的IP資料
	 * @param tableName
	 * @param limitSize
	 * @return
	 */
	public List<Object[]> getDownloadFlowExceedLimitSizeIpData(String tableName, String nowDateStr, String limitSize);

	/**
	 * 檢查此超量IP在統計表內是否已寫過紀錄 (Group_Id + Now_Date_Str + Direction + Ip_Addr)
	 * @param groupId
	 * @param nowDateStr
	 * @param direction
	 * @param ipAddr
	 * @return
	 */
	public boolean chkFlowExceedIpHasAlreadyExistsInStatToday(String groupId, String nowDateStr, String direction, String ipAddr);

	public NetFlowIpStat findNetFlowIpStatByStatId(String statId);

	public NetFlowIpStat saveNetFlowIpStat(NetFlowIpStat netFlowIpStat);

	public void updateNetFlowIpStat(NetFlowIpStat netFlowIpStat);

	public List<NetFlowIpStat> findNetFlowIpStat4Resend(String nowDateStr, String sendPRTGFlag);
}
