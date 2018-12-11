package com.cmap.plugin.module.netflow;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.DeviceListDAO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DeviceList;
import com.cmap.service.DataPollerService;

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
	public long countNetFlowRecord(NetFlowVO nfVO, List<String> searchLikeField) throws ServiceLayerException {
		long retCount = 0;
		try {
			retCount = netFlowDAO.countNetFlowData(nfVO, searchLikeField, getSpecifyDayTableName(nfVO.getQueryDateBegin()));

		} catch (Exception e) {
			log.error(e.toString(), e);
			throw new ServiceLayerException("查詢失敗，請重新操作");
		}
		return retCount;
	}

	@Override
	public List<NetFlowVO> findNetFlowRecord(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField) throws ServiceLayerException {
		List<NetFlowVO> retList = new ArrayList<>();
		try {
			List<Object[]> dataList = netFlowDAO.findNetFlowData(nfVO, startRow, pageLength, searchLikeField, getSpecifyDayTableName(nfVO.getQueryDateBegin()));

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
								fValue = Objects.toString(data[dataIdx]);

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

}
