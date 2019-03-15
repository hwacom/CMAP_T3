package com.cmap.plugin.module.netflow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DataPollerDAO;
import com.cmap.dao.DeviceDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DataPollerMapping;
import com.cmap.model.DataPollerSetting;
import com.cmap.model.DeviceList;
import com.cmap.service.CommonService;
import com.cmap.service.DataPollerService;
import com.cmap.service.vo.CommonServiceVO;
import com.cmap.service.vo.DataPollerServiceVO;

@Service("netFlowService")
@Transactional
public class NetFlowServiceImpl implements NetFlowService {
	@Log
	private static Logger log;

	@Autowired
	private NetFlowDAO netFlowDAO;

	@Autowired
	private DataPollerService dataPollerService;

	@Autowired
	private DataPollerDAO dataPollerDAO;

	@Autowired
	private DeviceDAO deviceDAO;

	@Autowired
	private CommonService commonService;

	private String getTodayTableName() {
		String tableName = Env.DATA_POLLER_NET_FLOW_TABLE_BASE_NAME;
		/*
		 * Y181207, Ken Lin
		 * 因資料量過於龐大，拆分不同星期寫入不同TABLE，一張TABLE儲存一天資料
		 */
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);	//取得當前系統時間是星期幾 (Sunday=1、Monday=2、...)
		tableName += "_" + StringUtils.leftPad(String.valueOf(dayOfWeek), 3, '0'); //TABLE流水編碼部分補0成3碼(ex:1→001)

		return tableName;
	}

	private String getSpecifyDayTableName(String date) throws ServiceLayerException {
		String tableName = Env.DATA_POLLER_NET_FLOW_TABLE_BASE_NAME;
		try {
			Date queryDate = Constants.FORMAT_YYYY_MM_DD.parse(date);
			/*
			 * Y181207, Ken Lin
			 * 因資料量過於龐大，拆分不同星期寫入不同TABLE，一張TABLE儲存一天資料
			 */
			Calendar cal = Calendar.getInstance();
			cal.setTime(queryDate);

			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);	//取得當前系統時間是星期幾 (Sunday=1、Monday=2、...)
			tableName += "_" + StringUtils.leftPad(String.valueOf(dayOfWeek), 3, '0'); //TABLE流水編碼部分補0成3碼(ex:1→001)

		} catch (ParseException e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("轉換查詢日期成Date物件時異常，queryDate >> " + date);
		}

		return tableName;
	}

	private String getGroupIdMapping2TableName(String groupId) throws ServiceLayerException {
		String tableName = null;
		try {
			tableName = netFlowDAO.findTargetTableNameByGroupId(groupId);

			if (StringUtils.isBlank(tableName)) {
				throw new ServiceLayerException("查詢groupId對應TableName為空，groupId >> " + groupId);
			}

		} catch (ServiceLayerException sle) {
			log.error(sle.toString(), sle);

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("取得groupId對應TableName失敗，groupId >> " + groupId);
		}

		return tableName;
	}

	private String getQueryTableName(NetFlowVO nfVO) throws ServiceLayerException {
		final String groupId = nfVO.getQueryGroupId();
		String recordBy = dataPollerService.getRecordBySetting(Constants.DATA_TYPE_OF_NET_FLOW);

		String queryTableName = null;

		switch (recordBy) {
			case Constants.RECORD_BY_DAY:
				queryTableName = getSpecifyDayTableName(nfVO.getQueryDateBegin());
				break;

			case Constants.RECORD_BY_MAPPING:
				queryTableName = getGroupIdMapping2TableName(groupId);
				break;
		}

		return queryTableName;
	}

	@Override
	public long countNetFlowRecordFromDB(NetFlowVO nfVO, List<String> searchLikeField) throws ServiceLayerException {
		long retCount = 0;
		try {
			retCount = netFlowDAO.countNetFlowDataFromDB(nfVO, searchLikeField, getQueryTableName(nfVO));

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢失敗，請重新操作");
		}
		return retCount;
	}

	@Override
	public List<NetFlowVO> findNetFlowRecordFromDB(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField) throws ServiceLayerException {
		List<NetFlowVO> retList = new ArrayList<>();
		try {
			List<String> tableTitleField = dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_TARGET);
			StringBuffer queryFieldsSQL = new StringBuffer();

			for (int i=0; i<tableTitleField.size(); i++) {
				String fieldName = tableTitleField.get(i);
				queryFieldsSQL.append("`").append(fieldName).append("`");

				if (i < tableTitleField.size() - 1) {
					queryFieldsSQL.append(", ");
				}
			}

			Map<Integer, CommonServiceVO> protocolMap = commonService.getProtoclSpecMap();

			final String queryTable = getQueryTableName(nfVO);
			List<Object[]> dataList = netFlowDAO.findNetFlowDataFromDB(nfVO, startRow, pageLength, searchLikeField, queryTable, queryFieldsSQL.toString());

			if (dataList != null && !dataList.isEmpty()) {
				List<String> fieldList = dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_SOURCE);

				if (fieldList == null || (fieldList != null && fieldList.isEmpty())) {
					throw new ServiceLayerException("查無欄位標題設定 >> Setting_Id: " + Env.SETTING_ID_OF_NET_FLOW);

				} else {
					// fieldList.add(0, "GroupId");

					NetFlowVO vo;
					for (Object[] data : dataList) {
						vo = new NetFlowVO();

						for (int i=0; i<fieldList.size(); i++) {
							int fieldIdx = i;
							int dataIdx = i;

							final String oriName = fieldList.get(fieldIdx);
							String fName = oriName.substring(0, 1).toLowerCase() + oriName.substring(1, oriName.length());

							String fValue = "";
							if (oriName.equals("Now") || oriName.equals("FromDateTime") || oriName.equals("ToDateTime")) {
								if (data[dataIdx] != null) {
									fValue = Constants.FORMAT_YYYYMMDD_HH24MISS.format(data[dataIdx]);
								}

							} else if (oriName.equals("Size")) {
								BigDecimal sizeByte = new BigDecimal(Objects.toString(data[dataIdx], "0"));

								/*
								int scale = 1;
								BigDecimal sizeKb = sizeByte.divide(new BigDecimal("1024"), scale, RoundingMode.HALF_UP);
								BigDecimal sizeMb = (sizeByte.divide(new BigDecimal("1024"))).divide(new BigDecimal("1024"), scale, RoundingMode.HALF_UP);
								BigDecimal zeroSize = new BigDecimal("0.0");

								String convertedSize = "";
								if (sizeMb.compareTo(zeroSize) == 1) {
									convertedSize = sizeMb.toString() + " MB";

								} else {
									if (sizeKb.compareTo(zeroSize) == 1) {
										convertedSize = sizeKb.toString() + " KB";

									} else {
										convertedSize = sizeByte.toString() + " B";
									}
								}
								*/

								fValue = convertByteSizeUnit(sizeByte);

							} else if (oriName.equals("GroupId")) {
								String groupId = Objects.toString(data[dataIdx]);
								DeviceList device = deviceDAO.findDeviceListByGroupAndDeviceId(groupId, null);

								if (device == null) {
									fValue = groupId;

								} else {
									fName = "groupName";
									fValue = device.getGroupName();
								}

							} else if (oriName.equals("Protocol")) {
								String tmpStr = Objects.toString(data[dataIdx]);
								Integer protocolNo = tmpStr != null ? Integer.valueOf(tmpStr) : null;
								String protocolName = protocolMap.get(protocolNo).getProtocolName();

								fValue = protocolName;

							} else {
								fValue = Objects.toString(data[dataIdx]);
							}

							BeanUtils.setProperty(vo, fName, fValue);
						}

						retList.add(vo);
					}

					BigDecimal flowSum = netFlowDAO.getTotalFlowOfQueryConditionsFromDB(nfVO, searchLikeField, queryTable);
					String totalFlow = (flowSum == null) ? "N/A" : convertByteSizeUnit(flowSum);
					retList.get(0).setTotalFlow(totalFlow);	// 塞入總流量至第一筆VO內
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢失敗，請重新操作");
		}
		return retList;
	}

	private String convertByteSizeUnit(BigDecimal sizeByte) {
		int scale = 1;
		BigDecimal unitSize = new BigDecimal("1024");
		BigDecimal sizeKb = sizeByte.divide(unitSize, scale, RoundingMode.HALF_UP);
		BigDecimal sizeMb = (sizeByte.divide(unitSize)).divide(unitSize, scale, RoundingMode.HALF_UP);
		BigDecimal sizeGb = (sizeByte.divide(unitSize).divide(unitSize)).divide(unitSize, scale, RoundingMode.HALF_UP);
		BigDecimal sizeTb = (sizeByte.divide(unitSize).divide(unitSize).divide(unitSize)).divide(unitSize, scale, RoundingMode.HALF_UP);
		BigDecimal zeroSize = new BigDecimal("0.0");

		String convertedSize = "";
		if (sizeTb.compareTo(zeroSize) == 1) {
			convertedSize = sizeTb.toString() + " TB";

		} else if (sizeGb.compareTo(zeroSize) == 1) {
			convertedSize = sizeGb.toString() + " GB";

		} else if (sizeMb.compareTo(zeroSize) == 1) {
			convertedSize = sizeMb.toString() + " MB";

		} else if (sizeKb.compareTo(zeroSize) == 1) {
			convertedSize = sizeKb.toString() + " KB";

		} else {
			convertedSize = sizeByte.toString() + " B";
		}

		return convertedSize;
	}

	private Map<String, NetFlowVO> composeQueryMap(NetFlowVO nfVO) {
		Map<String, NetFlowVO> retMap = new HashMap<>();

		NetFlowVO vo = new NetFlowVO();
		vo.setQueryValue(nfVO.getQueryDate());
		vo.setQueryCondition(Constants.SYMBOL_EQUAL);
		retMap.put("FromDateTime", vo);

		vo = new NetFlowVO();
		vo.setQueryValue(nfVO.getQueryDate());
		vo.setQueryCondition(Constants.SYMBOL_EQUAL);
		retMap.put("ToDateTime", vo);

		if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
			vo = new NetFlowVO();
			vo.setQueryValue(nfVO.getQuerySourceIp());
			vo.setQueryCondition(Constants.SYMBOL_END_LIKE);
			retMap.put("SourceIP", vo);
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
			vo = new NetFlowVO();
			vo.setQueryValue(nfVO.getQuerySourcePort());
			vo.setQueryCondition(Constants.SYMBOL_EQUAL);
			retMap.put("SourcePort", vo);
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
			vo = new NetFlowVO();
			vo.setQueryValue(nfVO.getQueryDestinationIp());
			vo.setQueryCondition(Constants.SYMBOL_END_LIKE);
			retMap.put("DestinationIP", vo);
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
			vo = new NetFlowVO();
			vo.setQueryValue(nfVO.getQueryDestinationIp());
			vo.setQueryCondition(Constants.SYMBOL_EQUAL);
			retMap.put("DestinationPort", vo);
		}

		return retMap;
	}

	private NetFlowVO doQuery(
			DataPollerSetting setting,
			Map<Integer, DataPollerServiceVO> fieldIdxMap,
			Map<String, DataPollerServiceVO> fieldVOMap,
			Map<String, NetFlowVO> queryMap,
			Integer startRow,
			Integer pageLength) throws Exception {

		NetFlowVO retVO = new NetFlowVO();

		String mappingCode = setting.getMappingCode();
		List<DataPollerMapping> mappings = dataPollerDAO.findDataPollerMappingByMappingCode(mappingCode);

		if (mappings != null && !mappings.isEmpty()) {
			DataPollerServiceVO dpsVO = null;
			for (DataPollerMapping dpm : mappings) {
				dpsVO = new DataPollerServiceVO();
				BeanUtils.copyProperties(dpsVO, dpm);

				fieldIdxMap.put(dpm.getTargetFieldIdx(), dpsVO);
				fieldVOMap.put(dpm.getTargetFieldName(), dpsVO);

			}
		}

		retVO = netFlowDAO.findNetFlowDataFromFile(setting, fieldIdxMap, fieldVOMap, queryMap, startRow, pageLength);

		return retVO;
	}

	@Override
	public NetFlowVO findNetFlowRecordFromFile(NetFlowVO nfVO, Integer startRow, Integer pageLength) throws ServiceLayerException {
		NetFlowVO retVO = new NetFlowVO();
		try {
			String querySchoolId = nfVO.getQuerySchoolId();
			DataPollerSetting setting = dataPollerDAO.findDataPollerSettingByDataTypeAndQueryId(Constants.DATA_TYPE_OF_NET_FLOW, querySchoolId);

			Map<Integer, DataPollerServiceVO> fieldIdxMap = new HashMap<>();
			Map<String, DataPollerServiceVO> fieldVOMap = new HashMap<>();

			Map<String, NetFlowVO> queryMap = composeQueryMap(nfVO);

			if (setting == null) {
				List<DataPollerSetting> settings = dataPollerDAO.findDataPollerSettingByDataType(Constants.DATA_TYPE_OF_NET_FLOW);

				NetFlowVO tmpVO = null;
				for (DataPollerSetting dps : settings) {
					tmpVO = doQuery(dps, fieldIdxMap, fieldVOMap, queryMap, startRow, pageLength);
				}

			} else {
				retVO = doQuery(setting, fieldIdxMap, fieldVOMap, queryMap, startRow, pageLength);
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢失敗，請重新操作");
		}
		return retVO;
	}

}
