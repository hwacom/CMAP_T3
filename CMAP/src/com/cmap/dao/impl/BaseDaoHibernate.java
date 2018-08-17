package com.cmap.dao.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.cmap.Constants;
import com.cmap.annotation.Log;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.model.DeviceList;

public class BaseDaoHibernate extends HibernateDaoSupport {
	@Log
	private static Logger log;

	protected static final String MARK_AS_DELETE = "Y";

	@Autowired
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	protected String composeSelectStr(String[] fields, String alias) {
		StringBuffer sb = new StringBuffer();

		int idx = 0;
		for (String field : fields) {
			sb.append(" ").append(alias).append(".").append(field);

			if (idx != fields.length-1) {
				sb.append(",");
			}

			idx++;
		}

		return sb.toString();
	}

	/**
	 * 因部分查詢SQL語法HQL不支援，採用Native SQL寫法，查詢結果為Object陣列List
	 * 透過此方法將List<Object[]>轉換為List<ConfigVersionInfo>
	 */
	protected List<Object[]> transObjList2ModelList4Version(List<Object[]> objList) {
		List<Object[]> retList = new ArrayList<>();

		try {
			if (objList != null && !objList.isEmpty()) {

				int versionFieldLength = Constants.HQL_FIELD_NAME_FOR_VERSION.length;
				int deviceFieldLength = Constants.HQL_FIELD_NAME_FOR_DEVICE.length;
				ConfigVersionInfo cviModel;
				DeviceList dlModel;
				for (Object[] objArray : objList) {
					if (objArray != null) {
						cviModel = new ConfigVersionInfo();
						dlModel = new DeviceList();

						for (int i = 0; i<versionFieldLength; i++) {
							new PropertyDescriptor(Constants.HQL_FIELD_NAME_FOR_VERSION[i], cviModel.getClass()).getWriteMethod().invoke(cviModel, objArray[i]);

							try {
								new PropertyDescriptor(Constants.HQL_FIELD_NAME_FOR_VERSION[i], dlModel.getClass()).getWriteMethod().invoke(dlModel, objArray[i]);
							} catch (IntrospectionException ie) {
								//僅塞入Group_Id、Group_Name、Device_Id、Device_Name至DeviceList model，其餘欄位不處理
							}
						}

						for (int j = versionFieldLength; j<(versionFieldLength+deviceFieldLength); j++) {
							new PropertyDescriptor(Constants.HQL_FIELD_NAME_FOR_DEVICE[j-versionFieldLength], dlModel.getClass()).getWriteMethod().invoke(dlModel, objArray[j]);
						}

						retList.add(new Object[] {cviModel, dlModel});
					}
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return retList;
	}

	/**
	 * 因部分查詢SQL語法HQL不支援，採用Native SQL寫法，查詢結果為Object陣列List
	 * 透過此方法將List<Object[]>轉換為List<ConfigVersionInfo>
	 */
	protected List<Object[]> transObjList2ModelList4Device(List<Object[]> objList) {
		List<Object[]> retList = new ArrayList<>();

		try {
			if (objList != null && !objList.isEmpty()) {

				int deviceFieldLength = Constants.HQL_FIELD_NAME_FOR_DEVICE_2.length;
				int versionFieldLength = Constants.HQL_FIELD_NAME_FOR_VERSION_2.length;
				ConfigVersionInfo cviModel;
				DeviceList dlModel;
				for (Object[] objArray : objList) {
					if (objArray != null) {
						cviModel = new ConfigVersionInfo();
						dlModel = new DeviceList();

						for (int i = 0; i<deviceFieldLength; i++) {
							new PropertyDescriptor(Constants.HQL_FIELD_NAME_FOR_DEVICE_2[i], dlModel.getClass()).getWriteMethod().invoke(dlModel, objArray[i]);

							try {
								new PropertyDescriptor(Constants.HQL_FIELD_NAME_FOR_DEVICE_2[i], cviModel.getClass()).getWriteMethod().invoke(cviModel, objArray[i]);
							} catch (IntrospectionException ie) {
								//僅塞入Group_Id、Group_Name、Device_Id、Device_Name至ConfigVersionInfo model，其餘欄位不處理
							}
						}

						for (int j = deviceFieldLength; j<(deviceFieldLength+versionFieldLength); j++) {
							new PropertyDescriptor(Constants.HQL_FIELD_NAME_FOR_VERSION_2[j-deviceFieldLength], cviModel.getClass()).getWriteMethod().invoke(cviModel, objArray[j]);
						}

						retList.add(new Object[] {cviModel, dlModel});
					}
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}

		return retList;
	}
}
