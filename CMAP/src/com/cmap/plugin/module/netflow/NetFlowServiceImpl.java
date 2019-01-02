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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DataPollerDAO;
import com.cmap.dao.DeviceListDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DataPollerMapping;
import com.cmap.model.DataPollerSetting;
import com.cmap.model.DeviceList;
import com.cmap.service.DataPollerService;
import com.cmap.service.vo.DataPollerServiceVO;
import com.nimbusds.oauth2.sdk.util.StringUtils;

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
	private DeviceListDAO deviceListDAO;

	private String getTodayTableName() {
		String tableName = Env.DATA_POLLER_NET_FLOW_TABLE_BASE_NAME;
		/*
		 * Y181207, Ken Lin
		 * 因資料量過於龐大，拆分不同星期寫入不同TABLE，一張TABLE儲存一天資料
		 */
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);	//取得當前系統時間是星期幾 (Sunday=1、Monday=2、...)
		tableName += "_" + dayOfWeek;

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
			tableName += "_" + dayOfWeek;

		} catch (ParseException e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("轉換查詢日期成Date物件時異常，queryDate >> " + date);
		}

		return tableName;
	}

	@Override
	public long countNetFlowRecordFromDB(NetFlowVO nfVO, List<String> searchLikeField) throws ServiceLayerException {
		long retCount = 0;
		try {
			retCount = netFlowDAO.countNetFlowDataFromDB(nfVO, searchLikeField, getSpecifyDayTableName(nfVO.getQueryDateBegin()));

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
			List<Object[]> dataList = netFlowDAO.findNetFlowDataFromDB(nfVO, startRow, pageLength, searchLikeField, getSpecifyDayTableName(nfVO.getQueryDateBegin()));

			if (dataList != null && !dataList.isEmpty()) {
				List<String> fieldList = dataPollerService.getFieldName(Env.SETTING_ID_OF_NET_FLOW, DataPollerService.FIELD_TYPE_SOURCE);

				if (fieldList == null || (fieldList != null && fieldList.isEmpty())) {
					throw new ServiceLayerException("查無欄位標題設定 >> Setting_Id: " + Env.SETTING_ID_OF_NET_FLOW);

				} else {
					fieldList.add(0, "GroupId");

					NetFlowVO vo;
					for (Object[] data : dataList) {
						vo = new NetFlowVO();

						for (int i=0; i<fieldList.size(); i++) {
							int fieldIdx = i;
							int dataIdx = i + 1;

							final String oriName = fieldList.get(fieldIdx);
							String fName = oriName.substring(0, 1).toLowerCase() + oriName.substring(1, oriName.length());

							String fValue = "";
							if (oriName.equals("Now") || oriName.equals("FromDateTime") || oriName.equals("ToDateTime")) {
								if (data[dataIdx] != null) {
									fValue = Constants.FORMAT_YYYYMMDD_HH24MISS.format(data[dataIdx]);
								}

							} else if (oriName.equals("Size")) {
								BigDecimal sizeByte = new BigDecimal(Objects.toString(data[dataIdx], "0"));

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

								fValue = convertedSize;

							} else if (oriName.equals("GroupId")) {
								String groupId = Objects.toString(data[dataIdx]);
								DeviceList device = deviceListDAO.findDeviceListByGroupAndDeviceId(groupId, null);

								if (device == null) {
									fValue = groupId;

								} else {
									fName = "groupName";
									fValue = device.getGroupName();
								}
							}
							else {
								fValue = Objects.toString(data[dataIdx]);
							}

							BeanUtils.setProperty(vo, fName, fValue);
						}

						retList.add(vo);
					}
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢失敗，請重新操作");
		}
		return retList;
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
