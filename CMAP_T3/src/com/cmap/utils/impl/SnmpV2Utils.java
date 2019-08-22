package com.cmap.utils.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import com.cmap.Env;
import com.cmap.exception.ServiceLayerException;
import com.cmap.service.vo.ConfigInfoVO;
import com.cmap.service.vo.ScriptServiceVO;
import com.cmap.service.vo.StepServiceVO;
import com.cmap.utils.ConnectUtils;

public class SnmpV2Utils implements ConnectUtils {
	private static Logger log = LoggerFactory.getLogger(SnmpV2Utils.class);

	private static int version = SnmpConstants.version2c;
	private static String protocol = "udp";

	private static CommunityTarget target = null;
	private static DefaultUdpTransportMapping udpTransportMapping = null;
	private static Snmp snmp = null;

	@Override
	public boolean connect(final String udpAddress, final String community) throws Exception {
		try {
			target = new CommunityTarget();
			target.setCommunity(new OctetString(community));
			target.setAddress(GenericAddress.parse(udpAddress));
			target.setVersion(version);
			target.setTimeout(Env.SNMP_CONNECT_TIME_OUT); 	// milliseconds
			target.setRetries(Integer.valueOf(Env.RETRY_TIMES)); 			// retry 3次

			try {
				udpTransportMapping = new DefaultUdpTransportMapping();
				// 這裡一定要呼叫 listen, 才能收到結果
				udpTransportMapping.listen();
				snmp = new Snmp(udpTransportMapping);

			} catch (Exception e) {
				throw e;
			}

			return true;

		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;
		}
	}

	@Override
	public boolean login(String account, String password) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, List<VariableBinding>> pollData(List<String> oids, SNMP pollMethod) throws Exception {
		try {
		    switch (pollMethod) {
		        case GET:
		            return doSnmpGet(oids);
		        case WALK:
		            return doSnmpWalk(oids);
		        default:
		            return null;
		    }

		} catch (ServiceLayerException sle) {
		    throw sle;

		} catch (Exception e) {
		    log.error(e.toString(), e);
			throw e;
		}
	}
	
	@Override
	public Map<String, Map<String, String>> pollTableView(String oid, Map<String, String> entryMap) throws Exception {
		try {
			return snmpGetTable(oid, entryMap);
			//return doSnmpTableView(oids);

		} catch (ServiceLayerException sle) {
		    throw sle;

		} catch (Exception e) {
		    log.error(e.toString(), e);
			throw e;
		}
	}

	private Map<String, List<VariableBinding>> doSnmpGet(List<String> oids) throws ServiceLayerException {
	    Map<String, List<VariableBinding>> retMap = new HashMap<String, List<VariableBinding>>();
        List<VariableBinding> vbList = null;

        for (String oid : oids) {
            vbList = snmpGet(oid);
            // System.out.println("OID: "+oid+" >> count: "+vbList.size());

            retMap.put(oid, vbList);
        }
        return retMap;
	}

	private List<VariableBinding> snmpGet(String targetOid) throws ServiceLayerException {
		try {
			PDU pdu = new PDU();
			pdu.setType(PDU.GETNEXT);

			System.out.println("[oid]: "+targetOid);

			if (StringUtils.isBlank(targetOid)) {
				return null;
			}

			pdu.add(new VariableBinding(new OID(targetOid)));

			// 以同步的方式發送 snmp get, 會等待target 設定的 timeout 時間結束後
			// 就會以 Request time out 的方式 return 回來
			ResponseEvent response = snmp.send(pdu, target);
			// System.out.println("PeerAddress:" + response.getPeerAddress());
			PDU responsePdu = response.getResponse();

			if (responsePdu == null) {
				throw new Exception("Request time out");
			} else {
				System.out.println(" response pdu vb size is " + responsePdu.size());

				List<VariableBinding> datalist = new ArrayList<VariableBinding>();
				for (int i = 0; i < responsePdu.size(); i++) {
					VariableBinding vb = responsePdu.get(i);
					System.out.println(vb.getOid().toString()+": "+vb.getVariable().toString());
					datalist.add(vb);
				}
				return datalist;
			}

		} catch (Exception e) {
		    log.error(e.toString(), e);
            throw new ServiceLayerException("snmpGet error!");
		}
	}

	private Map<String, List<VariableBinding>> doSnmpWalk(List<String> oids) throws ServiceLayerException {
        Map<String, List<VariableBinding>> retMap = new HashMap<String, List<VariableBinding>>();
        List<VariableBinding> vbList = null;

        for (String oid : oids) {
            vbList = snmpWalk(oid);
            // System.out.println("OID: "+oid+" >> count: "+vbList.size());

            retMap.put(oid, vbList);
        }
        return retMap;
    }

	/**
	 * 1)responsePDU == null<br>
	 * 2)responsePDU.getErrorStatus() != 0<br>
	 * 3)responsePDU.get(0).getOid() == null<br>
	 * 4)responsePDU.get(0).getOid().size() < targetOID.size()<br>
	 * 5)targetOID.leftMostCompare(targetOID.size(),responsePDU.get(0).getOid())
	 * !=0<br>
	 * 6)Null.isExceptionSyntax(responsePDU.get(0).getVariable().getSyntax())<br>
	 * 7)responsePDU.get(0).getOid().compareTo(targetOID) <= 0<br>
	 */
	private List<VariableBinding> snmpWalk(String targetOid) throws ServiceLayerException {
		OID targetOID = new OID(targetOid);

		PDU requestPDU = new PDU();
		requestPDU.setType(PDU.GETNEXT);
		requestPDU.add(new VariableBinding(targetOID));

		try {
			List<VariableBinding> vblist = new ArrayList<VariableBinding>();
			boolean finished = false;
			while (!finished) {
				VariableBinding vb = null;
				ResponseEvent response = snmp.send(requestPDU, target);
				PDU responsePDU = response.getResponse();

				if (null == responsePDU) {
					System.out.println("responsePDU == null");
					finished = true;
					break;
				} else {
					vb = responsePDU.get(0);
				}
				// check finish
				finished = checkWalkFinished(targetOID, responsePDU, vb);
				if (!finished) {
					// System.out.println("vb:" + vb.toString());
					vblist.add(vb);
					// Set up the variable binding for the next entry.
					requestPDU.setRequestID(new Integer32(0));
					requestPDU.set(0, vb);
				}
			}
			// System.out.println("success finish snmp walk!");
			return vblist;

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("snmpWalk error!");
		}
	}

	/**
	 * check snmp walk finish
	 * @param resquestPDU
	 * @param targetOID
	 * @param responsePDU
	 * @param vb
	 * @return
	 */
	private boolean checkWalkFinished(OID targetOID, PDU responsePDU, VariableBinding vb) {
		boolean finished = false;
		if (responsePDU.getErrorStatus() != 0) {
			// System.out.println("responsePDU.getErrorStatus() != 0 ");
			// System.out.println(responsePDU.getErrorStatusText());
			finished = true;
		} else if (vb.getOid() == null) {
			// System.out.println("vb.getOid() == null");
			finished = true;
		} else if (vb.getOid().size() < targetOID.size()) {
			// System.out.println("vb.getOid().size() < targetOID.size()");
			finished = true;
		} else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
			// System.out.print("["+CommonUtils.FORMAT_YYYYMMDDHHMISS.format(new Date())+"] ");
			// System.out.println("targetOID.leftMostCompare() != 0");
			finished = true;
		} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
			// System.out.println("Null.isExceptionSyntax(vb.getVariable().getSyntax())");
			finished = true;
		} else if (vb.getOid().compareTo(targetOID) <= 0) {
			// System.out.println("Variable received is not "
			// 		+ "lexicographic successor of requested " + "one:");
			// System.out.println(vb.toString() + " <= " + targetOID);
			finished = true;
		}
		return finished;

	}
	
	public Map<String, Map<String, String>> snmpGetTable(String oid, Map<String, String> entryMap) throws Exception {
		Map<String, Map<String, String>> tableMap = new HashMap<>();
        try {
            int maxRepetitions = 100;

            PDUFactory pF = new DefaultPDUFactory(PDU.GETNEXT);

            TableUtils tableUtils = new TableUtils(snmp, pF);
            tableUtils.setMaxNumRowsPerPDU(maxRepetitions);

            OID[] columns = new OID[1];
            columns[0] = new VariableBinding(new OID(oid)).getOid();
            //OID lowerBoundIndex = new OID(lowerBound);
            //OID upperBoundIndex = new OID(upperBound);

            //System.out.println("Vector Bulk SNMP oid= " + columns[0]);
            //System.out.println("Vector Bulk SNMP lower= " + lowerBoundIndex);
            //System.out.println("Vector Bulk SNMP upper= " + upperBoundIndex);

            List<TableEvent> snmpList = tableUtils.getTable(target, columns, null, null);
            //System.out.println("snmpList size : " + snmpList.size());

            for (int j = 0; j < snmpList.size(); j++) {
            	TableEvent event = snmpList.get(j);
            	VariableBinding[] vbs = event.getColumns();
            	
            	for (VariableBinding vb : vbs) {
            		String vbOID = vb.getOid().toString();
            		String vbValue = vb.getVariable().toString();
            		
            		for (Map.Entry<String, String> entry : entryMap.entrySet()) {
            			String oidKey = entry.getValue().substring(1);
            			String oidName = entry.getKey();
            			
            			if (vbOID.startsWith(oidKey)) {
            				String tableKey = vbOID.split(oidKey)[1];
            				
            				Map<String, String> tableEntry = null;
            				if (tableMap.containsKey(tableKey)) {
            					tableEntry = tableMap.get(tableKey);
            				} else {
            					tableEntry = new HashMap<>();
            				}
            				tableEntry.put(oidName, vbValue);
            				tableMap.put(tableKey, tableEntry);
            			}
            		}
            	}
            	//System.out.println("snmpList : " + snmpList.get(j));
            }

        } catch (Exception e) {
            log.error(e.toString(),e);
            throw e;
        }
        return tableMap;
    }

	public List<Map<String, String>> doSnmpTableView(List<String> oids) throws Exception {
        //结果集合
        List<Map<String,String>> mibOidToValueMap = new ArrayList<Map<String,String>>();
        //获取设备上指定OID的主键index的集合。
        List<String> instances = selectInstance(target, oids.get(0));

        /*
        DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        */
        try {
            snmp.listen();
            int instanceSize = instances.size();
            int oidSize = oids.size();
            //生成PDU
            PDU pdu = new PDU();
            //设置为GET操作
            pdu.setType(PDU.GET);
            for(int i = 0 ; i < instanceSize; i++){
                String index = instances.get(i);
                for(int j = 0; j < oidSize; j++){
                    //合成OID+主键index，加入到PDU中。
                    pdu.add(new VariableBinding(new OID(oids.get(j)+index)));
                }
                ResponseEvent respEvent = snmp.send(pdu, target);
                //解析响应数据
                PDU response = respEvent.getResponse();
                if (response != null) {
                    int reSize = response.size();
                    //创建TableView的一行数据
                    Map<String,String> row = new HashMap<String,String>(reSize);
                    //将主键加入到一行中
                    row.put("index", index.substring(1));
                    for (int k = 0; k < reSize; k++) {
                        VariableBinding vb = response.get(k);
                        //删除主键
                        String oid = vb.getOid().toString();
                        oid = oid.substring(0, oid.lastIndexOf('.'));
                        String value = vb.getVariable().toString();
                        //加入到一行中
                        row.put(oid, value);

                    }
                    mibOidToValueMap.add(row);
                }
                pdu.clear();
                pdu.setType(PDU.GET);
            }
        } catch (Exception e) {
        	log.error(e.toString(), e);
        } finally{
            snmp.close();
        }
        return mibOidToValueMap;
    }

	private List<String> selectInstance(CommunityTarget target,String targetOid) throws Exception {
        List<String> instances = new ArrayList<String>();
        /*
        TransportMapping transport = null;
        try {
            transport = new DefaultUdpTransportMapping();
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        Snmp snmp = new Snmp(transport);
        */

        try {
            //transport.listen();
            // 创建 PDU
            PDU pdu = new PDU();
            OID targetOID = new OID(targetOid);
            pdu.add(new VariableBinding(targetOID));

            boolean finished = false;
            //walk操作
            while (!finished) {
                VariableBinding vb = null;
                /** 向Agent发送PDU实施getNext操作，并接收Response*/
                ResponseEvent respEvent = snmp.getNext(pdu, target);
                /** 解析Response数据*/
                PDU response = respEvent.getResponse();
                if (null == response) {
                    finished = true;
                    break;
                } else {
                    vb = response.get(0);
                }
                /** 检查是否结束*/
                finished = checkWalkFinished(targetOID, pdu, vb);
                if (!finished) {
                    /**保存索引数据*/
                    String fullOid = vb.getOid().toString();
                    String index = fullOid.replace(targetOid, "");
                    instances.add(index);

                    /** 将variable binding设置到下一个*/
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                }
            }
        } catch (Exception e) {
        	log.error(e.toString(), e);
        } finally {
            snmp.close();
        }
        return instances;
    }

	@Override
	public boolean logout() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean disconnect() throws Exception {
		if (snmp != null) {
			try {
				snmp.close();
			} catch (IOException ex1) {
				snmp = null;
			}
		}
		if (udpTransportMapping != null) {
			try {
				udpTransportMapping.close();
			} catch (IOException ex2) {
				udpTransportMapping = null;
			}
		}

		return true;
	}

    @Override
    public boolean connect(String ipAddress, Integer port) throws Exception {
        // TODO 自動產生的方法 Stub
        return false;
    }

    @Override
    public List<String> sendCommands(List<ScriptServiceVO> scriptList, ConfigInfoVO configInfoVO,
            StepServiceVO ssVO) throws Exception {
        // TODO 自動產生的方法 Stub
        return null;
    }
}
