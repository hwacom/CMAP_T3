package com.cmap.plugin.module.netflow.statistics;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DeviceDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DataPollerSetting;
import com.cmap.model.DeviceList;
import com.cmap.plugin.module.netflow.NetFlowDAO;
import com.cmap.plugin.module.netflow.NetFlowIpStat;
import com.cmap.plugin.module.netflow.NetFlowVO;
import com.cmap.service.impl.CommonServiceImpl;
import com.cmap.service.impl.jobs.BaseJobImpl.Result;
import com.cmap.utils.impl.CloseableHttpClientUtils;

@Service("netFlowStatisticsService")
public class NetFlowStatisticsServiceImpl extends CommonServiceImpl implements NetFlowStatisticsService {
    @Log
    private static Logger log;

    @Autowired
    private NetFlowDAO netFlowDAO;

    @Autowired
    private NetFlowStatisticsDAO netFlowStatisticsDAO;

    @Autowired
    private DeviceDAO deviceDAO;

    @Override
    public NetFlowVO executeNetFlowIpStatistics() throws ServiceLayerException {
        NetFlowVO retVO = new NetFlowVO();
        try {
            // Step 1. 取得流量異常設定值
            final String limitSize = Env.ABNORMAL_NET_FLOW_LIMIT_SIZE;
            final String limitSizeUnit = convertByteSizeUnit(
                    new BigDecimal(Env.ABNORMAL_NET_FLOW_LIMIT_SIZE), Env.NET_FLOW_SHOW_UNIT_OF_TOTOAL_FLOW).replace(" ", "");
            final String nowDateStr = Constants.FORMAT_YYYY_MM_DD_NOSYMBOL.format(new Date());

            // Step 2. 迴圈跑所有學校的NET_FLOW檢核SQL
            List<DataPollerSetting> dpsList = netFlowDAO.getHasAlreadySetUpNetFlowDataPollerInfo();

            if (dpsList == null || (dpsList != null && dpsList.isEmpty())) {
                // 如果都沒有已經設定好的 Data_Poller_Setting 設定則不做事
                retVO.setJobExcuteResult(Result.SUCCESS);
                retVO.setJobExcuteResultRecords("0");
                retVO.setJobExcuteRemark("尚未有符合設定的 Data_Poller_Setting (Data_Type = " + Env.DEFAULT_NET_FLOW_DATA_TYPE + ")");
                return retVO;
            }

            List<NetFlowIpStat> ipList = new ArrayList<>(); // 暫存所有學校超標的IP資料 for 最後寫入DB & 發送告警至PRTG
            for (DataPollerSetting dps : dpsList) {
                try {
                    final String specialVarSetting = dps.getSpecialVarSetting();

                    if (StringUtils.isBlank(specialVarSetting) || !StringUtils.contains(specialVarSetting, "GROUP_ID")) {
                        // 若 Special_Var_Setting 欄位為空或不含 GROUP_ID 設定則跳過
                        continue;
                    }

                    String groupId = null;

                    String[] args = specialVarSetting.split(Env.COMM_SEPARATE_SYMBOL);
                    for (String arg : args) {
                        if (StringUtils.contains(specialVarSetting, "GROUP_ID")) {
                            groupId = arg.split("=")[1];
                            break;
                        }
                    }

                    if (groupId == null) {
                        // 取不到 GROUP_ID 則跳過
                        continue;
                    }

                    // 取得 GROUP_ID 對應的 Net_Flow table 名稱
                    String tableName = netFlowDAO.findTargetTableNameByGroupId(groupId);

                    if (StringUtils.isBlank(tableName)) {
                        // 若 GROUP_ID 取不到對應的 table 名稱則跳過
                        continue;
                    }

                    // 取上傳超量資料
                    List<Object[]> uploadAlarmList = netFlowDAO.getUploadFlowExceedLimitSizeIpData(tableName, nowDateStr, limitSize);

                    // 取下載超量資料
                    List<Object[]> downloadAlarmList = netFlowDAO.getDownloadFlowExceedLimitSizeIpData(tableName, nowDateStr, limitSize);

                    if ((uploadAlarmList == null || (uploadAlarmList != null && uploadAlarmList.isEmpty()))
                            && (downloadAlarmList == null || (downloadAlarmList != null && downloadAlarmList.isEmpty()))) {
                        // 若上傳跟下載都無超量資料則跳過
                        continue;
                    }

                    List<Object[]> mergedList = new ArrayList<>();
                    mergedList.addAll(uploadAlarmList);
                    mergedList.addAll(downloadAlarmList);

                    // Step 3. 比對既有資料
                    String direction = null;
                    String ipAddr = null;
                    String ttlSize = null;

                    boolean hasRecorded = false;
                    for (Object[] upObj : mergedList) {
                        direction = Objects.toString(upObj[0]);
                        ipAddr = Objects.toString(upObj[1]);
                        ttlSize = Objects.toString(upObj[2]);

                        hasRecorded = netFlowDAO.chkFlowExceedIpHasAlreadyExistsInStatToday(groupId, nowDateStr, direction, ipAddr);

                        if (!hasRecorded) {
                            // Step 4. 若為新的超標資料，則將資料放入暫存List
                            Timestamp nowTime = new Timestamp((new Date()).getTime());

                            NetFlowIpStat ipEntity =
                                new NetFlowIpStat(
                                    UUID.randomUUID().toString(),
                                    nowDateStr,
                                    groupId,
                                    ipAddr,
                                    direction,
                                    new BigInteger(ttlSize),
                                    new BigInteger(limitSize),
                                    Constants.DATA_N,
                                    Constants.DATA_N,
                                    nowTime,
                                    Env.USER_NAME_JOB,
                                    nowTime,
                                    Env.USER_NAME_JOB
                                );

                            ipList.add(ipEntity);
                        }
                    }

                    uploadAlarmList = null;
                    downloadAlarmList = null;
                    mergedList = null;

                } catch (Exception e) {
                    // 發生非預期異常則跳下一筆設定
                    log.error(e.toString(), e);
                    continue;
                }
            }

            // Step 5. 將新的流量異常IP寫入DB & 透過URL將告警IP傳給PRTG
            if (!ipList.isEmpty()) {
                for (NetFlowIpStat ipStat : ipList) {
                    try {
                        // 寫入DB
                        try {
                            ipStat = netFlowDAO.saveNetFlowIpStat(ipStat);

                        } catch (Exception e) {
                            // 寫入失敗的跳下一筆，不發送PRTG，避免資料不對等
                            log.error(e.toString(), e);
                            throw new ServiceLayerException("寫入失敗!!");
                        }

                        // 發送PRTG
                        boolean sendSuccess = true;
                        try {
                            /*
                             * URL格式: http://[PRTG_IP_ADDR]:[PORT]/IP_Traffic_Alert_[GROUP_ID]?value=1&text=IP%20[IP_ADDR]%20Over%20[EXCEED_SIZE]
                             * [Ex]: http://163.19.163.165:5050/IP_Traffic_Alert_DNES?value=1&text=IP%20163.19.224.44%20Over%201GB
                             */
                            //TODO PRTG接收Server IP要by不同學校設定
                            String prtgServerIp = Env.NET_FLOW_IP_STAT_SEND_TO_PRTG_SERVER_IP;
                            String groupId = ipStat.getGroupId();
                            String ipAddr = ipStat.getIpAddr();
                            String url =
                                    prtgServerIp + "IP_Traffic_Alert_" + groupId + "?value=1&text=IP%20" + ipAddr + "%20Over%20" + limitSizeUnit;

                            sendAlarm2PRTG(url);

                            Thread.sleep(500); // 避免太密集發送

                        } catch (Exception e) {
                            // 發送失敗不處理，待後續重發機制再retry
                            log.error(e.toString(), e);
                            sendSuccess = false;
                        }

                        // 調整發送註記
                        if (sendSuccess) {
                            try {
                                ipStat.setSendPrtgFlag(Constants.DATA_Y);
                                ipStat.setUpdateTime(new Timestamp((new Date()).getTime()));
                                ipStat.setUpdateBy(Env.USER_NAME_JOB);
                                netFlowDAO.saveNetFlowIpStat(ipStat);

                            } catch (Exception e) {
                                // 調整註記失敗不作處理
                                log.error(e.toString(), e);
                            }
                        }

                    } catch (ServiceLayerException sle) {
                        // 單筆資料寫入異常跳過
                        continue;

                    } catch (Exception e) {
                        // 非預期異常跳過
                        log.error(e.toString(), e);
                        continue;
                    }
                }
            }

            retVO.setJobExcuteResult(Result.SUCCESS);
            retVO.setJobExcuteResultRecords(String.valueOf(ipList.size()));
            retVO.setJobExcuteRemark("Net_Flow IP traffic statistics success.");

            ipList = null;

            // Step 6. 重傳機制 (針對先前已寫入資料但發送PRTG失敗的)
            try {
                List<NetFlowIpStat> retryList = netFlowDAO.findNetFlowIpStat4Resend(nowDateStr, Constants.DATA_N);

                if (retryList != null && !retryList.isEmpty()) {
                    for (NetFlowIpStat retryStat : retryList) {
                        boolean sendSuccess = true;
                        try {
                            //TODO PRTG接收Server IP要by不同學校設定
                            String prtgServerIp = Env.NET_FLOW_IP_STAT_SEND_TO_PRTG_SERVER_IP;
                            String groupId = retryStat.getGroupId();
                            String ipAddr = retryStat.getIpAddr();
                            String url =
                                    prtgServerIp + "IP_Traffic_Alert_" + groupId + "?value=1&text=IP%20" + ipAddr + "%20Over%20" + limitSizeUnit;

                            sendAlarm2PRTG(url);

                            Thread.sleep(500); // 避免太密集發送

                        } catch (Exception e) {
                            // 發送失敗不處理，待後續重發機制再retry
                            log.error(e.toString(), e);
                            sendSuccess = false;
                        }

                        // 調整發送註記
                        if (sendSuccess) {
                            try {
                                retryStat.setSendPrtgFlag(Constants.DATA_Y);
                                retryStat.setUpdateTime(new Timestamp((new Date()).getTime()));
                                retryStat.setUpdateBy(Env.USER_NAME_JOB);
                                netFlowDAO.saveNetFlowIpStat(retryStat);

                            } catch (Exception e) {
                                // 調整註記失敗不作處理
                                log.error(e.toString(), e);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                // 重傳機制執行過程異常不處理
                log.error(e.toString(), e);
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("NET_FLOW IP 流量統計處理過程發生非預期異常!! (" + e.getMessage() + ")");
        }

        return retVO;
    }

    private void sendAlarm2PRTG(String _URL_) throws Exception {
        CloseableHttpClient httpclient = CloseableHttpClientUtils.prepare();

        HttpGet httpGet = new HttpGet(_URL_);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Env.HTTP_CONNECTION_TIME_OUT)            //設置連接逾時時間，單位毫秒。
                .setConnectionRequestTimeout(Env.HTTP_CONNECTION_TIME_OUT)  //設置從connect Manager獲取Connection 超時時間，單位毫秒。這個屬性是新加的屬性，因為目前版本是可以共用連接池的。
                .setSocketTimeout(Env.HTTP_SOCKET_TIME_OUT)                 //請求獲取資料的超時時間，單位毫秒。 如果訪問一個介面，多少時間內無法返回資料，就直接放棄此次調用。
                .build();
        httpGet.setConfig(requestConfig);

        log.info("Executing request " + httpGet.getRequestLine());

        // Create a custom response handler
        ResponseHandler<String> responseHandler = response -> {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + statusCode);
            }
        };

        httpclient.execute(httpGet, responseHandler);
    }

    @Override
    public void calculateIpTrafficStatistics(
            String groupId, Date statDate, Map<String, Map<String, Long>> ipTrafficMap) throws ServiceLayerException {

        if (ipTrafficMap != null && !ipTrafficMap.isEmpty()) {
            List<ModuleIpTrafficStatistics> entities = new ArrayList<>();

            ipTrafficMap.forEach(new BiConsumer<String, Map<String, Long>>() {
                @Override
                public void accept(String ipAddress, Map<String, Long> trafficMap) {
                    Long totalSize = trafficMap.containsKey(Constants.TOTAL) ? trafficMap.get(Constants.TOTAL) : 0;
                    Long downloadSize = trafficMap.containsKey(Constants.DOWNLOAD) ? trafficMap.get(Constants.DOWNLOAD) : 0;
                    Long uploadSize = trafficMap.containsKey(Constants.UPLOAD) ? trafficMap.get(Constants.UPLOAD) : 0;

                    // Step 1. 查找是否已有紀錄
                    ModuleIpTrafficStatistics entity =
                            netFlowStatisticsDAO.findModuleIpStatisticsByUK(groupId, statDate, ipAddress);

                    if (entity == null) {
                        entity = new ModuleIpTrafficStatistics();
                        entity.setGroupId(groupId);
                        entity.setStatDate(statDate);
                        entity.setIpAddress(ipAddress);
                        entity.setCreateTime(currentTimestamp());
                        entity.setCreateBy(getUserName());
                    }

                    long newTotalTraffic = entity.getTotalTraffic() + totalSize;
                    long newDownloadTraffic = entity.getDownloadTraffic() + downloadSize;
                    long newUploadTraffic = entity.getUploadTraffic() + uploadSize;

                    if (newTotalTraffic < 0 || newDownloadTraffic < 0 || newUploadTraffic < 0) {
                        log.error("************ [Net_Flow_Statistic.ERROR] "
                                + "ipAddress: " + ipAddress + " >> preTotalTraffic: " + entity.getTotalTraffic() + " >> totalSize: " + totalSize
                                + " >> preDownloadTraffic: " + entity.getDownloadTraffic() + " >> downloadSize: " + downloadSize
                                + " >> preUploadTraffic: " + entity.getUploadTraffic() + " >> uploadSize: " + uploadSize);

                    } else {
                        entity.setTotalTraffic(entity.getTotalTraffic() + totalSize);
                        entity.setDownloadTraffic(entity.getDownloadTraffic() + downloadSize);
                        entity.setUploadTraffic(entity.getUploadTraffic() + uploadSize);
                        entity.setUpdateTime(currentTimestamp());
                        entity.setUpdateBy(getUserName());
                        entities.add(entity);
                    }
                }
            });

            netFlowStatisticsDAO.saveOrUpdateModuleIpStatistics(entities);
        }
    }

    @Override
    public long countModuleIpTrafficStatistics(NetFlowStatisticsVO nfsVO) throws ServiceLayerException {
        long retVal = 0;
        try {
            retVal = netFlowStatisticsDAO.countModuleIpStatisticsRanking(nfsVO);

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return retVal;
    }

    @Override
    public List<NetFlowStatisticsVO> findModuleIpTrafficStatistics(NetFlowStatisticsVO nfsVO) throws ServiceLayerException {
        List<NetFlowStatisticsVO> retList = new ArrayList<>();
        try {
            List<Object[]> entities = netFlowStatisticsDAO.findModuleIpStatisticsRanking(nfsVO, nfsVO.getStartNum(), nfsVO.getPageLength());

            if (entities != null && !entities.isEmpty()) {
                Map<String, String> groupNameMap = new HashMap<>();

                // Step 1. 第一筆塞入 Total 列
                String ttlTotalTraffic = Objects.toString(entities.get(0)[6], "0");
                String ttlUploadTraffic = Objects.toString(entities.get(0)[7], "0");
                String ttlDownloadTraffic = Objects.toString(entities.get(0)[8], "0");
                NetFlowStatisticsVO vo = new NetFlowStatisticsVO();
                vo.setIpAddress("總計");
                vo.setPercent("100%");
                vo.setTotalTraffic(convertByteSizeUnit(new BigDecimal(ttlTotalTraffic), Env.NET_FLOW_SHOW_UNIT_OF_RESULT_DATA_SIZE));
                vo.setUploadTraffic(convertByteSizeUnit(new BigDecimal(ttlUploadTraffic), Env.NET_FLOW_SHOW_UNIT_OF_RESULT_DATA_SIZE));
                vo.setDownloadTraffic(convertByteSizeUnit(new BigDecimal(ttlDownloadTraffic), Env.NET_FLOW_SHOW_UNIT_OF_RESULT_DATA_SIZE));
                retList.add(vo);

                // Step 2. 第二筆開始塞入 Raw data
                entities.forEach(new Consumer<Object[]>() {
                    @Override
                    public void accept(Object[] entity) {
                        String ipAddress = Objects.toString(entity[0]);
                        String groupId = Objects.toString(entity[1]);
                        String percent = Objects.toString(entity[2], "0");
                        String totalTraffic = Objects.toString(entity[3], "0");
                        String uploadTraffic = Objects.toString(entity[4], "0");
                        String downloadTraffic = Objects.toString(entity[5], "0");

                        if (StringUtils.isBlank(ipAddress) || StringUtils.isBlank(groupId)) {
                            return;
                        }

                        NetFlowStatisticsVO vo = new NetFlowStatisticsVO();
                        vo.setIpAddress(ipAddress);
                        vo.setGroupId(groupId);

                        String groupName = "N/A";
                        /*
                         *  為避免每一筆查詢結果都要再查詢一次群組名稱(DeviceList.GroupName)
                         *  使用MAP紀錄下已查詢過的群組ID(groupId)，下一次就不須再做DB query
                         */
                        if (groupNameMap.containsKey(groupId)) {
                            groupName = groupNameMap.get(groupId);

                        } else {
                            DeviceList device = deviceDAO.findDeviceListByGroupAndDeviceId(groupId, null);

                            if (device != null) {
                                groupName = device.getGroupName();
                                groupNameMap.put(groupId, groupName);
                            }
                        }
                        vo.setGroupName(groupName);
                        vo.setPercent(new BigDecimal(percent).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                        vo.setTotalTraffic(convertByteSizeUnit(new BigDecimal(totalTraffic), Env.NET_FLOW_SHOW_UNIT_OF_RESULT_DATA_SIZE));
                        vo.setUploadTraffic(convertByteSizeUnit(new BigDecimal(uploadTraffic), Env.NET_FLOW_SHOW_UNIT_OF_RESULT_DATA_SIZE));
                        vo.setDownloadTraffic(convertByteSizeUnit(new BigDecimal(downloadTraffic), Env.NET_FLOW_SHOW_UNIT_OF_RESULT_DATA_SIZE));
                        retList.add(vo);
                    }
                });
            }

        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new ServiceLayerException("查詢異常，請重新操作!");
        }
        return retList;
    }

    @Override
    public long countModuleSessionStatistics(NetFlowStatisticsVO nfsVO)
            throws ServiceLayerException {
        // TODO 自動產生的方法 Stub
        return 0;
    }

    @Override
    public List<NetFlowStatisticsVO> findModuleSessionStatistics(NetFlowStatisticsVO nfsVO)
            throws ServiceLayerException {
        // TODO 自動產生的方法 Stub
        return null;
    }
}
