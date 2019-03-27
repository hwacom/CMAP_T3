package com.cmap.dao.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.BaseDAO;
import com.cmap.model.ConfigVersionInfo;
import com.cmap.model.DeviceList;

public class BaseDaoHibernate extends HibernateDaoSupport implements BaseDAO {
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

	@Override
	public boolean insertEntity(Object entity) {
		boolean success = true;
		getHibernateTemplate().save(entity);
		return success;
	}

	@Override
	public boolean updateEntity(Object entity) {
		boolean success = true;
		getHibernateTemplate().update(entity);
		return success;
	}

	@Override
	public boolean insertEntities(List<? extends Object> entities) {
		boolean success = true;

		int count = 1;
		for (Object entity : entities) {
			getHibernateTemplate().save(entity);
			count++;

			if (count >= Env.DEFAULT_BATCH_INSERT_FLUSH_COUNT) {
				getHibernateTemplate().flush();
			}
		}

		return success;
	}

	@Override
	public boolean deleteEntity(Object entity) {
		// TODO 自動產生的方法 Stub
		return false;
	}

	@Override
	public boolean insertEntitiesByNativeSQL(List<String> nativeSQLs) {
		boolean success = true;

		int count = 1;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		for (String sql : nativeSQLs) {
			session.createNativeQuery(sql).executeUpdate();
			count++;

			if (count >= Env.DEFAULT_BATCH_INSERT_FLUSH_COUNT) {
				getHibernateTemplate().flush();
			}
		}

		return success;
	}

	@Override
	public boolean deleteEntitiesByNativeSQL(List<String> nativeSQLs) {
		boolean success = true;

		int count = 1;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		for (String sql : nativeSQLs) {
			session.createNativeQuery(sql).executeUpdate();
			count++;

			if (count >= Env.DEFAULT_BATCH_INSERT_FLUSH_COUNT) {
				getHibernateTemplate().flush();
			}
		}

		return success;
	}

	@Override
	public Integer loadDataInFile(String tableName, String filePath, String charset, String fieldsTerminatedBy,
			String linesTerminatedBy, String extraSetStr) {
		// LOAD DATA LOCAL INFILE 'D:\\Net_Flow_Log\\Streams Sensor_20181220164000_20181220164011.csv' INTO TABLE cmap.net_flow_raw_data_3 CHARACTER SET big5 FIELDS TERMINATED BY ','  LINES TERMINATED BY '\r\n';
		StringBuffer sql = new StringBuffer();
		sql.append(" LOAD DATA LOCAL INFILE ")
		   //.append(" :filePath ")
		   .append(" '").append(filePath).append("' ")
		   //.append(" INTO TABLE :tableName ");
		   .append(" INTO TABLE ").append(tableName).append(" ");

		if (StringUtils.isNotBlank(charset)) {
			//sql.append(" CHARACTER SET :charset ");
			sql.append(" CHARACTER SET ").append(charset).append(" ");
		}
		if (StringUtils.isNotBlank(fieldsTerminatedBy)) {
			//sql.append(" FIELDS TERMINATED BY :fieldsTerminatedBy ");
			sql.append(" FIELDS TERMINATED BY '").append(fieldsTerminatedBy).append("' ");
		}
		if (StringUtils.isNotBlank(linesTerminatedBy)) {
			//sql.append(" LINES TERMINATED BY :linesTerminatedBy ");
			sql.append(" LINES TERMINATED BY '").append(linesTerminatedBy).append("' ");
		}
		if (StringUtils.isNotBlank(extraSetStr)) {
			sql.append(extraSetStr);
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createNativeQuery(sql.toString());
		/*
		q.setParameter("filePath", filePath);
		q.setParameter("tableName", tableName);
		if (StringUtils.isNotBlank(charset)) {
			q.setParameter("charset", charset);
		}
		if (StringUtils.isNotBlank(fieldsTerminatedBy)) {
			q.setParameter("fieldsTerminatedBy", fieldsTerminatedBy);
		}
		if (StringUtils.isNotBlank(linesTerminatedBy)) {
			q.setParameter("linesTerminatedBy", linesTerminatedBy);
		}
		if (StringUtils.isNotBlank(extraSetStr)) {
			q.setParameter("extraSetStr", extraSetStr);
		}
		*/

		return q.executeUpdate();
	}

	@Override
	public boolean insertEntities2File(Path filePath, List<String> recordList, boolean appendFile) {
		long begin = System.currentTimeMillis();

		FileWriter fw = null;
		try {
			Path fileFolderPath = filePath.getParent();
			if (!Files.exists(fileFolderPath)) {
				Files.createDirectories(fileFolderPath);
			}

			fw = new FileWriter(filePath.toString(), appendFile);

			for (String content : recordList) {
				fw.write(content + "\r\n");
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
			return false;

		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					log.error(e.toString(), e);
				}
			}

			long end = System.currentTimeMillis();
			log.info("readTxt1方法,使用記憶體="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())+",使用時間毫秒="+(end-begin));

		}
		return true;
	}
}
