package com.cmap.plugin.module.ip.mapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DeviceDAO;
import com.cmap.dao.vo.DeviceDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DeviceList;
import com.cmap.model.DeviceLoginInfo;
import com.cmap.model.MibOidMapping;
import com.cmap.plugin.module.netflow.NetFlowService;
import com.cmap.plugin.module.netflow.NetFlowVO;
import com.cmap.service.impl.CommonServiceImpl;
import com.cmap.utils.ConnectUtils;
import com.cmap.utils.impl.SnmpV2Utils;

@Service("ipMappingService")
public class IpMappingServiceImpl extends CommonServiceImpl implements IpMappingService {
    @Log
    private static Logger log;

    @Autowired
    private DeviceDAO deviceDAO;

    @Autowired
    private IpMappingDAO ipMappingDAO;

    @Autowired
    private NetFlowService netFlowService;

    /**
     * 逐筆Device撈取ArpTable資料
     * @param deviceL3
     * @return Map<String, Map<String, IpMappingServiceVO>>
     * 結構:
     * Map<String, Map<String, IpMappingServiceVO>>
     *   - [Key]    String: DeviceId值
     *   - [Value]  Map<String, IpMappingServiceVO>
     *       - [Key]    String: MAC值
     *       - [Value]  IpMappingServiceVO: InterfaceId、IpAddress
     * @throws ServiceLayerException
     */
    private Map<String, Map<String, IpMappingServiceVO>> pollingArpTable(List<DeviceList> deviceL3) throws ServiceLayerException {
        Map<String, Map<String, IpMappingServiceVO>> retMap = new HashMap<>();
        try {
        	// 準備ARP_TABLE的OID清單
        	List<MibOidMapping> arpTableOidMapping = ipMappingDAO.findMibOidMappingByNames(Arrays.asList(new String[] {Env.OID_NAME_OF_ARP_TABLE}));

        	if (arpTableOidMapping == null || (arpTableOidMapping != null && arpTableOidMapping.isEmpty())) {
        		throw new ServiceLayerException("未設定 Arp_Table OID!! >> [OID_NAME: atTable]");
        	}

        	String arpTableOid = arpTableOidMapping.get(0).getOidValue();

        	// 準備ARP_TABLE底下需要的Field的OID清單
        	Map<String, String> tableEntryMap = null;
        	List<MibOidMapping> arpTableEntryOidMapping = ipMappingDAO.findMibOidMappingOfTableEntryByNameLike(Env.OID_NAME_OF_ARP_TABLE);
        	if (arpTableEntryOidMapping != null && !arpTableEntryOidMapping.isEmpty()) {
        		tableEntryMap = new HashMap<>();
        		for (MibOidMapping mapping : arpTableEntryOidMapping) {
        			tableEntryMap.put(mapping.getOidName(), mapping.getOidValue());
        		}
        	}

        	ConnectUtils snmpUtils = new SnmpV2Utils();
        	// 迴圈跑該群組下的L3設備
            for (DeviceList device : deviceL3) {
            	String deviceListId = device.getDeviceListId();
                String groupId = device.getGroupId();
                String deviceId = device.getDeviceId();
                String deviceIp = device.getDeviceIp();

                // 取得設備 COMMUNITY_STRING 及 UDP_PORT 設定
                String communityString = null;
                Integer udpPort = null;
                DeviceLoginInfo loginInfo = findDeviceLoginInfo(deviceListId, groupId, deviceId);
                if (loginInfo != null) {
                	communityString = loginInfo.getCommunityString();
                	udpPort = loginInfo.getUdpPort();
                } else {
                	communityString = Env.DEFAULT_DEVICE_COMMUNITY_STRING;
                	udpPort = Env.DEFAULT_DEVICE_UDP_PORT;
                }

                String udpAddress = "udp:" + deviceIp + "/" + udpPort;
                // 連接設備
                snmpUtils.connect(udpAddress, communityString);

                // 撈取設備ARP_TABLE相關資料
                Map<String, Map<String, String>> arpTable = snmpUtils.pollTableView(arpTableOid, tableEntryMap);

                if (arpTable == null || (arpTable != null && arpTable.isEmpty())) {
                	// 該設備未撈取到資料則跳到下一個設備繼續處理
                	continue;
                }

                // 將撈取結果組成此Method回傳MAP格式
                Map<String, IpMappingServiceVO> macInfoMap = new HashMap<>();
                IpMappingServiceVO ipsVO = null;
                for (Map.Entry<String, Map<String, String>> arpTableMap : arpTable.entrySet()) {
                	ipsVO = new IpMappingServiceVO();

                	Map<String, String> arpTableEntryMap = arpTableMap.getValue();
                	String macAddress = arpTableEntryMap.get(Env.OID_NAME_OF_ARP_TABLE_MAC_ADDRESS);
                	String ipAddress = arpTableEntryMap.get(Env.OID_NAME_OF_ARP_TABLE_IP_ADDRESS);
                	String interfaceId = arpTableEntryMap.get(Env.OID_NAME_OF_ARP_TABLE_INTERFACE_ID);

                	if (StringUtils.isBlank(macAddress)) {
                		//TODO MAC_Address為Key值，若為空的話先跳過
                		continue;
                	}

                	ipsVO.setGroupId(groupId);
                	ipsVO.setDeviceId(deviceId);
                	ipsVO.setMacAddress(macAddress);
                	ipsVO.setIpAddress(ipAddress);
                	ipsVO.setInterfaceId(interfaceId);

                	macInfoMap.put(macAddress, ipsVO);
                }

                retMap.put(deviceId, macInfoMap);
            }

        } catch (Exception e) {
        	log.error(e.toString(), e);
        	throw new ServiceLayerException(e.toString());
        }
        return retMap;
    }

    /**
     * 逐筆Device撈取MacTable資料
     * @param deviceL2
     * @return Map<String, Map<String, IpMappingServiceVO>>
     * 結構:
     * Map<String, Map<String, IpMappingServiceVO>>
     *   - [Key]    String: DeviceId值
     *   - [Value]  Map<String, IpMappingServiceVO>
     *       - [Key]    String: MAC值
     *       - [Value]  IpMappingServiceVO: PortId
     * @throws ServiceLayerException
     */
    private Map<String, Map<String, IpMappingServiceVO>> pollingMacTable(List<DeviceList> deviceL2) throws ServiceLayerException {
        Map<String, Map<String, IpMappingServiceVO>> retMap = new HashMap<>();
        try {
        	// 準備MAC_TABLE的OID清單
        	List<MibOidMapping> macTableOidMapping = ipMappingDAO.findMibOidMappingByNames(Arrays.asList(new String[] {Env.OID_NAME_OF_MAC_TABLE}));

        	if (macTableOidMapping == null || (macTableOidMapping != null && macTableOidMapping.isEmpty())) {
        		throw new ServiceLayerException("未設定 Mac_Table OID!! >> [OID_NAME: dot1dTpFdbTable]");
        	}

        	String macTableOid = macTableOidMapping.get(0).getOidValue();

        	// 準備MAC_TABLE底下需要的Field的OID清單
        	Map<String, String> entryMap = null;
        	List<MibOidMapping> macTableEntryOidMapping = ipMappingDAO.findMibOidMappingOfTableEntryByNameLike(Env.OID_NAME_OF_MAC_TABLE);
        	if (macTableEntryOidMapping != null && !macTableEntryOidMapping.isEmpty()) {
        		entryMap = new HashMap<>();
        		for (MibOidMapping mapping : macTableEntryOidMapping) {
        			entryMap.put(mapping.getOidName(), mapping.getOidValue());
        		}
        	}

        	ConnectUtils snmpUtils = new SnmpV2Utils();
        	// 迴圈跑該群組下的L2設備
            for (DeviceList device : deviceL2) {
            	String deviceListId = device.getDeviceListId();
                String groupId = device.getGroupId();
                String deviceId = device.getDeviceId();
                String deviceIp = device.getDeviceIp();

                String communityString = null;
                Integer udpPort = null;
                DeviceLoginInfo loginInfo = findDeviceLoginInfo(deviceListId, groupId, deviceId);
                if (loginInfo != null) {
                	communityString = loginInfo.getCommunityString();
                	udpPort = loginInfo.getUdpPort();
                } else {
                	communityString = Env.DEFAULT_DEVICE_COMMUNITY_STRING;
                	udpPort = Env.DEFAULT_DEVICE_UDP_PORT;
                }

                String udpAddress = "udp:" + deviceIp + "/" + udpPort;
                snmpUtils.connect(udpAddress, communityString);

                Map<String, Map<String, String>> macTable = snmpUtils.pollTableView(macTableOid, entryMap);

                if (macTable == null || (macTable != null && macTable.isEmpty())) {
                	// 該設備未撈取到資料則跳到下一個設備繼續處理
                	continue;
                }

                // 查找要排除的PORT清單
                Map<String, String> excludePortMap = null;
                List<ModuleMacTableExcludePort> excludePortList = ipMappingDAO.findModuleMacTableExcludePort(groupId, deviceId);

                if (excludePortList != null && !excludePortList.isEmpty()) {
                	excludePortMap = new HashMap<>();

                	for (ModuleMacTableExcludePort entity : excludePortList) {
                		excludePortMap.put(entity.getPortId(), entity.getRemark());
                	}
                }

                // 將撈取結果組成此Method回傳MAP格式
                Map<String, IpMappingServiceVO> macInfoMap = new HashMap<>();
                IpMappingServiceVO ipsVO = null;
                for (Map.Entry<String, Map<String, String>> macTableMap : macTable.entrySet()) {
                	ipsVO = new IpMappingServiceVO();

                	Map<String, String> macTableEntryMap = macTableMap.getValue();
                	String macAddress = macTableEntryMap.get(Env.OID_NAME_OF_MAC_TABLE_MAC_ADDRESS);
                	String portId = macTableEntryMap.get(Env.OID_NAME_OF_MAC_TABLE_PORT_ID);

                	if (StringUtils.isBlank(macAddress)) {
                		//TODO MAC_Address為Key值，若為空的話先跳過
                		continue;
                	} else if (excludePortMap != null && !excludePortMap.isEmpty() && excludePortMap.containsKey(portId)) {
                		// 若PORT_ID在排除清單內則跳過
                		continue;
                	}

                	ipsVO.setGroupId(groupId);
                	ipsVO.setDeviceId(deviceId);
                	ipsVO.setMacAddress(macAddress);
                	ipsVO.setPortId(portId);

                	macInfoMap.put(macAddress, ipsVO);
                }

                retMap.put(deviceId, macInfoMap);
            }


        } catch (Exception e) {
        	log.error(e.toString(), e);
        	throw new ServiceLayerException(e.toString());
        }
        return retMap;
    }

    @Override
    public IpMappingServiceVO executeIpMappingPolling(String jobId, Date executeDate, String groupId) throws ServiceLayerException {
        IpMappingServiceVO retVO = new IpMappingServiceVO();
        Map<String, Map<String, IpMappingServiceVO>> L3ArpTableMap = null;
        Map<String, Map<String, IpMappingServiceVO>> L2MacTableMap = null;
        List<IpMappingServiceVO> ipMacPortMappingList = new ArrayList<>();

        try {
        	log.info("[IpMapping] <START> ================================================================================");
        	log.info("[IpMapping] jobId: " + jobId + " , executeDate: " + executeDate + " , groupId: " + groupId);
        	long beginTime = System.currentTimeMillis();
        	long startTime = System.currentTimeMillis();

            DeviceDAOVO daovo = new DeviceDAOVO();
            daovo.setGroupId(groupId);

            // Step 1. 撈取ArpTable資料 (僅針對L3 switch撈取)
            daovo.setDeviceLayer(Env.DEVICE_LAYER_L3);

            List<DeviceList> deviceL3 = deviceDAO.findDeviceListByDAOVO(daovo);
            if (deviceL3 != null && !deviceL3.isEmpty()) {
                L3ArpTableMap = pollingArpTable(deviceL3);
            } else {
            	throw new ServiceLayerException("查無L3設備!! (Group_ID: " + groupId + ")");
            }
            long endTime = System.currentTimeMillis();
            log.info("[IpMapping] Step 1. Polling L3 ARP_TABLE (Device*" + (deviceL3.size()) + ") >> Cost: " + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            // Step 2. 撈取MacTable資料 (僅針對L2 switch撈取)
            daovo.setDeviceLayer(Env.DEVICE_LAYER_L2);
            List<DeviceList> deviceL2 = deviceDAO.findDeviceListByDAOVO(daovo);
            if (deviceL2 != null && !deviceL2.isEmpty()) {
                L2MacTableMap = pollingMacTable(deviceL2);
            } else {
                throw new ServiceLayerException("查無L2設備!! (Group_ID: " + groupId + ")");
            }
            endTime = System.currentTimeMillis();
            log.info("[IpMapping] Step 2. Polling L2 MAC_TABLE (Device*" + (deviceL2.size()) + ") >> Cost: " + (endTime - startTime) + " ms");

            if ((L2MacTableMap == null || (L2MacTableMap != null && L2MacTableMap.isEmpty()))
            		|| (L3ArpTableMap == null || (L3ArpTableMap != null && L3ArpTableMap.isEmpty()))) {
            	// 若此群組下[L2設備都撈不到MAC_TABLE資料]或[L3設備都撈不到ARP_TABLE資料]則結束
            	return retVO;
            }

            startTime = System.currentTimeMillis();
            // Step 3. ArpTable & MacTable mapping處理
            //Map<String, String> tempMacMap = new HashMap<>();
            IpMappingServiceVO mappingVO = null;
            // 以L2設備為主
            for (Map.Entry<String, Map<String, IpMappingServiceVO>> L2DeviceEntry : L2MacTableMap.entrySet()) {
            	String L2DeviceId = L2DeviceEntry.getKey();
                Map<String, IpMappingServiceVO> L2DeviceMacTable = L2DeviceEntry.getValue();

                // 跑L2 device MacTable 資料
                for (Map.Entry<String, IpMappingServiceVO> L2MacTableEntry : L2DeviceMacTable.entrySet()) {
                    String macAddress = L2MacTableEntry.getKey();

                    /*
                    if (tempMacMap.containsKey(macAddress)) {
                    	// 若該MAC_ADDRESS已處理過則跳過
                    	continue;
                    } else {
                    	// 記錄下已經處理過的MAC_ADDRESS，同一個MAC_ADDRESS可能出現在不同L2設備中，只需處理一次
                    	tempMacMap.put(macAddress, Constants.DATA_Y);
                    }
                    */

                    IpMappingServiceVO mtEntryVO = L2MacTableEntry.getValue();

                    // 跑L3 取得MAC_ADDRESS對應IP_ADDRESS
                    for (Map.Entry<String, Map<String, IpMappingServiceVO>> L3ArpTableEntry : L3ArpTableMap.entrySet()) {
                        Map<String, IpMappingServiceVO> L3DeviceArpTable = L3ArpTableEntry.getValue();

                        // 若該Group底下L2 switch的MAC table不為空，且存在該MAC address資料才往下取得該MAC連接的PortID
                        if (L3DeviceArpTable != null && !L3DeviceArpTable.isEmpty()) {
                            if (L3DeviceArpTable.containsKey(macAddress)) {
                                IpMappingServiceVO atEntryVO = L3DeviceArpTable.get(macAddress);
                                String ipAddress = atEntryVO.getIpAddress();
                                String portId = mtEntryVO.getPortId();

                                mappingVO = new IpMappingServiceVO();
                                mappingVO.setExecuteDate(executeDate);
                                mappingVO.setGroupId(groupId);
                                mappingVO.setDeviceId(L2DeviceId);
                                mappingVO.setIpAddress(ipAddress);
                                mappingVO.setMacAddress(macAddress);
                                mappingVO.setPortId(portId);

                                ipMacPortMappingList.add(mappingVO);
                            }
                        }
                    }
                }
            }
            endTime = System.currentTimeMillis();
            log.info("[IpMapping] Step 3. ArpTable & MacTable mapping處理 >> Cost: " + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            // Step 4. 寫入Module_Arp_Table / Module_Mac_Table / Module_Ip_Mac_Port_Mapping資料
        	// 寫入Module_Arp_Table
        	List<ModuleArpTable> artTableList = new ArrayList<>();
        	ModuleArpTable arpTable = null;
        	for (Map<String, IpMappingServiceVO> deviceEntry : L3ArpTableMap.values()) {
        		for (IpMappingServiceVO vo : deviceEntry.values()) {
        			arpTable = new ModuleArpTable();
        			arpTable.setJobId(jobId);
        			arpTable.setpDate(executeDate);
        			arpTable.setpTime(executeDate);
        			arpTable.setGroupId(groupId);
        			arpTable.setDeviceId(vo.getDeviceId());
        			arpTable.setInterfaceId(vo.getInterfaceId());
        			arpTable.setMacAddr(vo.getMacAddress());
        			arpTable.setIpAddr(vo.getIpAddress());
        			arpTable.setCreateTime(currentTimestamp());
        			arpTable.setCreateBy(currentUserName());
        			arpTable.setUpdateTime(currentTimestamp());
        			arpTable.setUpdateBy(currentUserName());
        			artTableList.add(arpTable);
        		}
        	}

        	if (!artTableList.isEmpty()) {
        		ipMappingDAO.insertEntities(artTableList);
        	}
        	log.info("artTableList size: " + artTableList.size());
        	endTime = System.currentTimeMillis();
        	log.info("[IpMapping] Step 4-1.寫入Module_Arp_Table >> Cost: " + (endTime - startTime) + " ms");

        	startTime = System.currentTimeMillis();
        	// 寫入Module_Mac_Table
        	List<ModuleMacTable> macTableList = new ArrayList<>();
        	ModuleMacTable macTable = null;
        	for (Map<String, IpMappingServiceVO> deviceEntry : L2MacTableMap.values()) {
        		for (IpMappingServiceVO vo : deviceEntry.values()) {
        			macTable = new ModuleMacTable();
        			macTable.setJobId(jobId);
        			macTable.setpDate(executeDate);
        			macTable.setpTime(executeDate);
        			macTable.setGroupId(groupId);
        			macTable.setDeviceId(vo.getDeviceId());
        			macTable.setMacAddr(vo.getMacAddress());
        			macTable.setPortId(vo.getPortId());
        			macTable.setCreateTime(currentTimestamp());
        			macTable.setCreateBy(currentUserName());
        			macTable.setUpdateTime(currentTimestamp());
        			macTable.setUpdateBy(currentUserName());
        			macTableList.add(macTable);
        		}
        	}

        	if (!macTableList.isEmpty()) {
        		ipMappingDAO.insertEntities(macTableList);
        	}
        	log.info("macTableList size: " + macTableList.size());
        	endTime = System.currentTimeMillis();
        	log.info("[IpMapping] Step 4-2.寫入Module_Mac_Table >> Cost: " + (endTime - startTime) + " ms");

        	startTime = System.currentTimeMillis();
        	// 寫入Module_Ip_Mac_Port_Mapping
        	List<ModuleIpMacPortMapping> mappingList = new ArrayList<>();
        	ModuleIpMacPortMapping mapping = null;
        	for (IpMappingServiceVO vo : ipMacPortMappingList) {
        		mapping = new ModuleIpMacPortMapping();
        		mapping.setJobId(jobId);
        		mapping.setRecordDate(executeDate);
        		mapping.setRecordTime(executeDate);
        		mapping.setGroupId(groupId);
        		mapping.setDeviceId(vo.getDeviceId());
        		mapping.setIpAddress(vo.getIpAddress());
        		mapping.setMacAddress(vo.getMacAddress());
        		mapping.setPortId(vo.getPortId());
        		mapping.setCreateTime(currentTimestamp());
        		mapping.setCreateBy(currentUserName());
        		mapping.setUpdateTime(currentTimestamp());
        		mapping.setUpdateBy(currentUserName());
        		mappingList.add(mapping);
        	}

        	if (!mappingList.isEmpty()) {
        		ipMappingDAO.insertEntities(mappingList);
        	}
        	log.info("ipMacPortMappingList size: " + mappingList.size());
        	endTime = System.currentTimeMillis();
        	log.info("[IpMapping] Step 4-3.寫入Module_Ip_Mac_Port_Mapping >> Cost: " + (endTime - startTime) + " ms");

        	startTime = System.currentTimeMillis();
            // Step 5. 比對IP前一次Mapping紀錄，判斷是否有異動 & 寫入Module_Ip_Mac_Port_Mapping_Change資料
            // 撈出該群組下所有IP的最新MAPPING資料
            List<Object[]> latestIPMappingList = ipMappingDAO.findEachIpAddressLastestModuleIpMacPortMapping(groupId);

            List<ModuleIpMacPortMappingChange> mappingChangeList = new ArrayList<>();
            ModuleIpMacPortMappingChange mappingChange = null;
            if (latestIPMappingList == null || (latestIPMappingList != null && latestIPMappingList.isEmpty())) {
            	// 第一次寫入則直接拷貝mappingList資料
            	for (ModuleIpMacPortMapping entity : mappingList) {
            		mappingChange = new ModuleIpMacPortMappingChange();
            		BeanUtils.copyProperties(mappingChange, entity);
            		mappingChangeList.add(mappingChange);
            	}

            } else {
            	// 比對有無異動，有異動才寫入資料
            	for (ModuleIpMacPortMapping newMapping : mappingList) {
            		String deviceId = newMapping.getDeviceId();
            		String ipAddress = newMapping.getIpAddress();
            		String newMacAddress = newMapping.getMacAddress();
            		String newPortId = newMapping.getPortId();

            		if (StringUtils.isBlank(deviceId)) {
            			continue;
            		}

            		for (Object[] preMapping : latestIPMappingList) {
            			String pDeviceId = Objects.toString(preMapping[1]);
            			String pIpAddress = Objects.toString(preMapping[2]);
            			String preMacAddress = Objects.toString(preMapping[3]);
            			String prePortId = Objects.toString(preMapping[4]);

            			if (StringUtils.equals(deviceId, pDeviceId) && StringUtils.equals(ipAddress, pIpAddress)) {
            				// 相同 Device_ID & IP_Address下
            				if (!StringUtils.equals(newMacAddress, preMacAddress) || !StringUtils.equals(newPortId, prePortId)) {
            					// 若 MAC_Address 或 Port_ID 有異動則寫入
            					mappingChange = new ModuleIpMacPortMappingChange();
            					mappingChange.setJobId(jobId);
            					mappingChange.setRecordDate(executeDate);
            					mappingChange.setRecordTime(executeDate);
            					mappingChange.setGroupId(groupId);
            					mappingChange.setDeviceId(deviceId);
            					mappingChange.setIpAddress(ipAddress);
            					mappingChange.setMacAddress(newMacAddress);
            					mappingChange.setPortId(newPortId);
            					mappingChange.setCreateTime(currentTimestamp());
            					mappingChange.setCreateBy(currentUserName());
            					mappingChange.setUpdateTime(currentTimestamp());
            					mappingChange.setUpdateBy(currentUserName());
            					mappingChangeList.add(mappingChange);
            				}
            			}
            		}
            	}
            }

            if (!mappingChangeList.isEmpty()) {
            	ipMappingDAO.insertEntities(mappingChangeList);
            }
            log.info("ipMacPortMappingChangeList size: " + mappingChangeList.size());

            endTime = System.currentTimeMillis();
            long finishTime = System.currentTimeMillis();
        	log.info("[IpMapping] Step 5.比對IP前一次Mapping紀錄，判斷是否有異動 & 寫入Module_Ip_Mac_Port_Mapping_Change資料 >> Cost: " + (endTime - startTime) + " ms");
        	log.info("[IpMapping] jobId: " + jobId + " , groupId: " + groupId + " , COST: " + (finishTime - beginTime) + " ms");
        	log.info("[IpMapping] <END> ================================================================================");

        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return retVO;
    }

    @Override
    public long countModuleIpMacPortMappingChange(IpMappingServiceVO imsVO)
            throws ServiceLayerException {
    	long retVal = 0;
        try {
        	retVal = ipMappingDAO.countModuleIpMacPortMappingChange(imsVO);

        } catch (Exception e) {
        	log.error(e.toString(), e);
        	throw new ServiceLayerException("查詢異常，請重新操作");
        }
        return retVal;
    }

    @Override
    public List<IpMappingServiceVO> findModuleIpMacPortMappingChange(IpMappingServiceVO imsVO)
            throws ServiceLayerException {
    	List<IpMappingServiceVO> retList = new ArrayList<>();
        try {
        	int startRow = imsVO.getStartNum();
        	int pageLength = imsVO.getPageLength();
        	List<Object[]> objList = ipMappingDAO.findModuleIpMacPortMappingChange(imsVO, startRow, pageLength);

        	if (objList != null && !objList.isEmpty()) {
        		IpMappingServiceVO vo;
        		for (Object[] obj : objList) {
        			vo = new IpMappingServiceVO();

        			// 轉換 SQL Date & Time
        			java.sql.Date date = (java.sql.Date)obj[0];
        			java.sql.Time time = (java.sql.Time)obj[1];

        			Calendar dateCal = Calendar.getInstance();
        			dateCal.setTime(date);

        			Calendar timeCal = Calendar.getInstance();
        			timeCal.setTime(time);

        			dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        			dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        			dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

        			SimpleDateFormat FORMAT_YYYYMMDD_HH24MISS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        			vo.setDateTime(FORMAT_YYYYMMDD_HH24MISS.format(dateCal.getTime()));
        			vo.setGroupId(Objects.toString(obj[2]));
        			vo.setGroupName(Objects.toString(obj[3]));
        			vo.setDeviceId(Objects.toString(obj[4]));
        			vo.setDeviceName(Objects.toString(obj[5]));
        			vo.setDeviceModel(Objects.toString(obj[6]));
        			vo.setIpAddress(Objects.toString(obj[7]));
        			vo.setMacAddress(Objects.toString(obj[8]));
        			vo.setPortId(Objects.toString(obj[9]));
        			vo.setPortName(Objects.toString(obj[10]));
        			retList.add(vo);
        		}
        	}

        } catch (Exception e) {
        	log.error(e.toString(), e);
        	throw new ServiceLayerException("查詢異常，請重新操作");
        }
        return retList;
    }

	@Override
	public IpMappingServiceVO findMappingDataFromNetFlow(String groupId, String dataId, String fromDateTime, String type) throws ServiceLayerException {
		IpMappingServiceVO retVO = new IpMappingServiceVO();
		try {
			// Step 1. 先取得該筆 NET_FLOW 資料
			NetFlowVO nfVO = netFlowService.findNetFlowRecordByGroupIdAndDataId(groupId, dataId, fromDateTime);

			if (nfVO == null) {
				// 若查不到 NET_FLOW 則無法繼續流程
				throw new ServiceLayerException("取不到 NET_FLOW 資料!! (groupId: " + groupId + ", dataId: " + dataId + ")");
			}

			String ipAddress = null;
			switch (type) {
				case "S":	//Source_IP
					ipAddress = nfVO.getSourceIP();
					break;

				case "D":	//Destination_IP
					ipAddress = nfVO.getDestinationIP();
					break;
			}

			if (StringUtils.isBlank(ipAddress)) {
				// 若 IP 為空則無法繼續流程
				throw new ServiceLayerException("NET_FLOW 資料 IP_ADDRESS 為空!! (groupId: " + groupId + ", dataId: " + dataId + ", type: " + type + ")");
			}

			SimpleDateFormat FORMAT_YYYYMMDD_HH24MISS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			SimpleDateFormat FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat FORMAT_HH24_MI_SS = new SimpleDateFormat("HH:mm:ss");

			Date dateTime = FORMAT_YYYYMMDD_HH24MISS.parse(fromDateTime);

			String date = FORMAT_YYYY_MM_DD.format(dateTime);
			String time = FORMAT_HH24_MI_SS.format(dateTime);

			List<Object[]> dataList = ipMappingDAO.findNearlyModuleIpMacPortMappingByTime(groupId, ipAddress, date, time);

			if (dataList == null || (dataList != null && dataList.isEmpty())) {
				// 若查無 MAPPING 資料
				List<Object[]> groupList = deviceDAO.getGroupIdAndNameByGroupIds(Arrays.asList(groupId));

				String groupName = "N/A";
				if (groupList != null && !groupList.isEmpty()) {
					groupName = Objects.toString(groupList.get(0)[1]);
				}

				retVO.setGroupName(groupName);
				retVO.setDeviceName("N/A");
				retVO.setDeviceModel("N/A");
				retVO.setIpAddress(ipAddress);
				retVO.setPortName("N/A");
				retVO.setShowMsg("查無此IP對應Port紀錄");

			} else {
				Object[] data = dataList.get(0);
				retVO.setGroupName(Objects.toString(data[2]));
				retVO.setDeviceName(Objects.toString(data[4]));
				retVO.setDeviceModel(Objects.toString(data[5]));
				retVO.setIpAddress(ipAddress);
				retVO.setPortName(Objects.toString(data[7]));
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			retVO.setShowMsg("查找資料異常，請重新操作");
		}
		return retVO;
	}
}
