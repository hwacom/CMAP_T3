package com.cmap.plugin.module.netflow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.annotation.Log;
import com.cmap.dao.impl.BaseDaoHibernate;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.DataPollerSetting;
import com.cmap.service.vo.DataPollerServiceVO;

@Repository("netFlowDAO")
@Transactional
public class NetFlowDAOImpl extends BaseDaoHibernate implements NetFlowDAO {
	@Log
	private static Logger log;

	@Autowired
	@Qualifier("secondSessionFactory")
	private SessionFactory secondSessionFactory;

	@Override
	public long countNetFlowDataFromDB(NetFlowVO nfVO, List<String> searchLikeField, String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(data_id) ")
          .append(" from ").append(tableName).append(" nfrd ")
          .append(" where 1=1 ");

        if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
            sb.append(" and nfrd.group_id = :groupId ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
            sb.append(" and nfrd.source_ip = :querySourceIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
            sb.append(" and nfrd.source_port = :querySourcePort ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
            sb.append(" and nfrd.destination_ip = :queryDestinationIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
            sb.append(" and nfrd.destination_port = :queryDestinationPort ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
            sb.append(" and nfrd.sender_ip = :querySenderIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
            sb.append(" and ( nfrd.source_MAC = :queryMac ")
              .append("       or nfrd.destination_MAC = :queryMac ) ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
            //sb.append(" and (nfrd.now >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.now < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ");
            sb.append(" and nfrd.from_date = :queryDateStr ")
              .append(" and (nfrd.from_time >= :queryTimeBeginStr and nfrd.from_time < :queryTimeEndStr) ");
        }
        /*
        if (StringUtils.isNotBlank(nfVO.getQueryDateEnd())) {
            sb.append(" and ( nfrd.now < DATE_ADD(:endDate, INTERVAL 1 DAY) ")
              .append("       or nfrd.from_date_time < DATE_ADD(:endDate, INTERVAL 1 DAY) ")
              .append("       or nfrd.to_date_time < DATE_ADD(:endDate, INTERVAL 1 DAY) ) ");
        }
        */

        if (StringUtils.isNotBlank(nfVO.getSearchValue())) {
            StringBuffer sfb = new StringBuffer();
            sfb.append(" and ( ");

            int i = 0;
            for (String sField : searchLikeField) {
                sfb.append(sField).append(" like :searchValue ");

                if (i < searchLikeField.size() - 1) {
                    sfb.append(" or ");
                }

                i++;
            }

            sfb.append(" ) ");
            sb.append(sfb);
        }

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createNativeQuery(sb.toString());

        if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
            q.setParameter("groupId", nfVO.getQueryGroupId());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
            q.setParameter("querySourceIp", nfVO.getQuerySourceIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
            q.setParameter("querySourcePort", nfVO.getQuerySourcePort());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
            q.setParameter("queryDestinationIp", nfVO.getQueryDestinationIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
            q.setParameter("queryDestinationPort", nfVO.getQueryDestinationPort());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
            q.setParameter("querySenderIp", nfVO.getQuerySenderIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
            q.setParameter("queryMac", nfVO.getQueryMac());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
            q.setParameter("queryDateStr", nfVO.getQueryDateBegin());
            q.setParameter("queryTimeBeginStr", nfVO.getQueryTimeBegin());
            q.setParameter("queryTimeEndStr", nfVO.getQueryTimeEnd());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateEnd())) {
            q.setParameter("endDate", nfVO.getQueryDateEnd());
        }
        if (StringUtils.isNotBlank(nfVO.getSearchValue())) {
            q.setParameter("searchValue", "%".concat(nfVO.getSearchValue()).concat("%"));
        }

        return DataAccessUtils.longResult(q.list());
	}

	@Override
	public List<Object[]> findNetFlowDataFromDB(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField, String tableName, String selectSql) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");

        if (StringUtils.isNotBlank(selectSql)) {
            sb.append(selectSql);
        } else {
            sb.append("*");
        }

        sb.append(" from ").append(tableName).append(" nfrd ")
          .append(" where 1=1 ");

        if (StringUtils.isNotBlank(nfVO.getQueryDataId())) {
            sb.append(" and nfrd.data_id = :dataId ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
            sb.append(" and nfrd.group_id = :groupId ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
            sb.append(" and nfrd.source_ip = :querySourceIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
            sb.append(" and nfrd.source_port = :querySourcePort ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
            sb.append(" and nfrd.destination_ip = :queryDestinationIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
            sb.append(" and nfrd.destination_port = :queryDestinationPort ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
            sb.append(" and nfrd.sender_ip = :querySenderIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
            sb.append(" and ( nfrd.source_MAC = :queryMac ")
              .append("       or nfrd.destination_MAC = :queryMac ) ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
            //sb.append(" and (nfrd.now >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.now < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ");
            sb.append(" and nfrd.from_date = :queryDateStr ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryTimeBegin()) && StringUtils.isNotBlank(nfVO.getQueryTimeEnd())) {
            sb.append(" and (nfrd.from_time >= :queryTimeBeginStr and nfrd.from_time < :queryTimeEndStr) ");
        }
        /*
        if (StringUtils.isNotBlank(nfVO.getQueryDateEnd())) {
            sb.append(" and ( nfrd.now < DATE_ADD(:endDate, INTERVAL 1 DAY) ")
              .append("       or nfrd.from_date_time < DATE_ADD(:endDate, INTERVAL 1 DAY) ")
              .append("       or nfrd.to_date_time < DATE_ADD(:endDate, INTERVAL 1 DAY) ) ");
        }
        */

        if (StringUtils.isNotBlank(nfVO.getSearchValue())) {
            StringBuffer sfb = new StringBuffer();
            sfb.append(" and ( ");

            int i = 0;
            for (String sField : searchLikeField) {
                sfb.append(sField).append(" like :searchValue ");

                if (i < searchLikeField.size() - 1) {
                    sfb.append(" or ");
                }

                i++;
            }

            sfb.append(" ) ");
            sb.append(sfb);
        }

        if (StringUtils.isNotBlank(nfVO.getOrderColumn())) {
            sb.append(" order by nfrd.").append(nfVO.getOrderColumn()).append(" ").append(nfVO.getOrderDirection());

        } else {
            sb.append(" order by nfrd.from_time desc ");
        }

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createNativeQuery(sb.toString());

        if (StringUtils.isNotBlank(nfVO.getQueryDataId())) {
            q.setParameter("dataId", nfVO.getQueryDataId());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
            q.setParameter("groupId", nfVO.getQueryGroupId());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
            q.setParameter("querySourceIp", nfVO.getQuerySourceIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
            q.setParameter("querySourcePort", nfVO.getQuerySourcePort());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
            q.setParameter("queryDestinationIp", nfVO.getQueryDestinationIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
            q.setParameter("queryDestinationPort", nfVO.getQueryDestinationPort());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
            q.setParameter("querySenderIp", nfVO.getQuerySenderIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
            q.setParameter("queryMac", nfVO.getQueryMac());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
            q.setParameter("queryDateStr", nfVO.getQueryDateBegin());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryTimeBegin()) && StringUtils.isNotBlank(nfVO.getQueryTimeEnd())) {
            q.setParameter("queryTimeBeginStr", nfVO.getQueryTimeBegin());
            q.setParameter("queryTimeEndStr", nfVO.getQueryTimeEnd());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateEnd())) {
            q.setParameter("endDate", nfVO.getQueryDateEnd());
        }
        if (StringUtils.isNotBlank(nfVO.getSearchValue())) {
            q.setParameter("searchValue", "%".concat(nfVO.getSearchValue()).concat("%"));
        }
        if (startRow != null && pageLength != null) {
            q.setFirstResult(startRow);
            q.setMaxResults(pageLength);
        }

        return (List<Object[]>)q.list();
	}

	private String analyzeFileFormat(String format, Map<String, String> varMap) {
		final String lS = "[";
		final String rS = "]";

		if (format.contains(lS)) {
			String[] tmp = format.split(lS);

			if (StringUtils.isNotBlank(tmp[1])) {
				String tmpStr = tmp[1];

				if (!tmpStr.contains(rS)) {
					return format;
				}

				String varKey = tmpStr.split(rS)[0];

				if (varMap.containsKey(varKey)) {
					String key = lS + varKey + rS;
					String value = varMap.get(varKey);

					format = format.replace(key, value);
				}

				return analyzeFileFormat(format, varMap);

			} else {
				return format;
			}

		} else {
			return format;
		}
	}

	private String getFileName(String format, Map<String, String> varMap) {
		String fileName = analyzeFileFormat(format, varMap);
		return fileName;
	}

	@Override
	public NetFlowVO findNetFlowDataFromFile(DataPollerSetting setting, Map<Integer, DataPollerServiceVO> fieldIdxMap, Map<String, DataPollerServiceVO> fieldVOMap, Map<String, NetFlowVO> queryMap, Integer startRow, Integer pageLength) {
		NetFlowVO retVO = new NetFlowVO();
		long start = System.currentTimeMillis();

		int totalCount = 0;
		BufferedReader br = null;
		try {
			final String querySchoolId = queryMap.get(Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_SCHOOL_ID).getQueryValue();
			final String queryDateYYYYMMDD = queryMap.get(Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_YYYYMMDD).getQueryValue();
			final String filePathBase = setting.getStoreFileDir();
			final String fileExtName = Env.FILE_EXTENSION_NAME_OF_NET_FLOW;

			// 組合檔名格式參數
			Map<String, String> varMap = new HashMap<>();
			varMap.put(Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_SCHOOL_ID, querySchoolId);
			varMap.put(Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_YYYYMMDD, queryDateYYYYMMDD);
			varMap.put(Constants.DATA_POLLER_STORE_FILE_NAME_FORMAT_OF_FILE_EXT, fileExtName);

			// Setting 設定內定義的檔案名稱格式(format)
			String fileNameFormat = setting.getStoreFileNameFormat();

			// 取得目標檔案名稱
			String targetFile = getFileName(fileNameFormat, varMap);
			Path targetFilePath = Paths.get(filePathBase + File.separator + targetFile);

			if (!Files.exists(targetFilePath) || Files.isDirectory(targetFilePath)) {
				throw new ServiceLayerException("檔案不存在 or 目標路徑為目錄非檔案 >> targetFilePath: " + targetFilePath.toString());
			}

			try {
				final String seperator = setting.getFieldsTerminatedBy();
				final String charset = setting.getFileCharset();

				/*
				 * 開始逐行讀取檔案 & 比對查詢條件
				 */
		        br = new BufferedReader(
		                new InputStreamReader(new FileInputStream(targetFilePath.toFile()), charset));

		        String[] fields = null;
		        while(br.ready())
		        {
		        	totalCount++;
		        	String line = br.readLine();
		        	fields = line.split(seperator);

		        	if (fields != null) {
		        		// 判斷是否符合查詢條件
		        		for (Map.Entry<String, NetFlowVO> entry : queryMap.entrySet()) {
		        			String qField = entry.getKey();
		        			String qValue = entry.getValue().getQueryValue();
		        			String qCondition = entry.getValue().getQueryCondition();

		        			if (!fieldVOMap.containsKey(qField)) {
		        				// 若欲查詢的欄位不存在於Mapping設定內則跳過
		        				log.error("[Net Flow]欲查詢的欄位不存在於Mapping設定內 >> qField: " + qField);
		        				continue;
		        			}

		        			DataPollerServiceVO dpsVO = fieldVOMap.get(qField);
		        			Integer qFieldIdx = dpsVO.getTargetFieldIdx();

		        			if (qFieldIdx > fields.length) {
		        				// 若欲查詢的欄位在Mapping設定內的Target_Field_Idx超過檔案讀取當前行數內容切割後的長度，則跳過
		        				log.error("[Net Flow]欲查詢的欄位於Mapping設定內的Target_Field_Idx超過實際檔案內容切割後陣列長度 >> qFieldIdx: " + qFieldIdx + ", line: " + line);
		        				continue;
		        			}

		        			String fType = dpsVO.getSourceColumnType();
		        			String fValue = fields[qFieldIdx];
		        			String fFormat = dpsVO.getSourceColumnJavaFormat();

		        			boolean matched = false;
		        			switch (qCondition) {
		        				case Constants.SYMBOL_EQUAL:			// 完全吻合
		        					if (StringUtils.startsWith(fType, Constants.FIELD_TYPE_OF_TIMESTAMP)) {
		        						matched = compareDate(fType, fValue, fFormat, qValue, qCondition);
		        					} else {
		        						matched = compareString(fValue, qValue, qCondition);
		        					}
		        					break;

		        				case Constants.SYMBOL_FRONT_END_LIKE:	// 前後模糊
		        				case Constants.SYMBOL_FRONT_LIKE:		// 前模糊
		        				case Constants.SYMBOL_END_LIKE:			// 後模糊
		        					matched = compareString(fValue, qValue, qCondition);
		        					break;

		        				case Constants.SYMBOL_GREATER_OR_EQUAL:	// 大於等於(用於日期)
		        				case Constants.SYMBOL_LESS_OR_EQUAL:	// 小於等於(用於日期)
		        					matched = compareDate(fType, fValue, fFormat, qValue, qCondition);
		        					break;
		        			}

		        			if (matched) {
		        				retVO.getMatchedList().add(transLine2VO(fieldIdxMap, line, seperator));
		        				break;
		        			}
		        		}
		        	}
		        }

		        retVO.setTotalCount(totalCount);

			} catch (Exception e ) {
				log.error(e.toString(), e);
				throw new ServiceLayerException("Net Flow 查詢異常");

			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						log.error(e.toString(), e);
					}
				}

				long end = System.currentTimeMillis();
		        log.info("NetFlowDAO.findNetFlowDataFromFile,使用記憶體="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())+",使用時間毫秒="+(end-start));
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retVO;
	}

	private NetFlowVO transLine2VO(Map<Integer, DataPollerServiceVO> fieldIdxMap, String line, String seperator) {
		NetFlowVO retVO = null;
		try {
			String[] fields = line.split(seperator);

			if (fields != null) {
				if (fields.length != fieldIdxMap.size()) {
					throw new ServiceLayerException("[Net Flow]轉換Line > VO異常，line切割後欄位數與Mapping設定的欄位數目不相等 >> line: " + line);
				}

				retVO = new NetFlowVO();
				for (int i=0; i<fields.length; i++) {
					if (!fieldIdxMap.containsKey(i)) {
						continue;
					}

					DataPollerServiceVO dpsVO = fieldIdxMap.get(i);
					String fName = dpsVO.getVoFieldName();
					String fValue = fields[i];
					String fType = dpsVO.getSourceColumnType();
					String fFormat = dpsVO.getSourceColumnJavaFormat();

					if (StringUtils.startsWith(fType, Constants.FIELD_TYPE_OF_TIMESTAMP)) {
						SimpleDateFormat sdf = new SimpleDateFormat(fFormat);
						fValue = Constants.FORMAT_YYYYMMDD_HH24MISS.format(sdf.parse(fValue));
					}

					BeanUtils.setProperty(retVO, fName, fValue);
				}
			}

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return retVO;
	}

	private boolean compareString(String fieldValue, String queryValue, String condition) {
		boolean chkResult = false;

		switch (condition) {
			case Constants.SYMBOL_EQUAL:		// 完全吻合
				chkResult = StringUtils.equals(fieldValue, queryValue);
				break;

			case Constants.SYMBOL_FRONT_END_LIKE:	// 前後模糊
				chkResult = StringUtils.contains(fieldValue, queryValue);
				break;

			case Constants.SYMBOL_FRONT_LIKE:	// 前模糊
				chkResult = StringUtils.startsWith(fieldValue, queryValue);
				break;

			case Constants.SYMBOL_END_LIKE:	// 後模糊
				chkResult = StringUtils.endsWith(fieldValue, queryValue);
				break;
		}

		return chkResult;
	}

	private boolean compareDate(String fieldType, String fieldValue, String dateFormat, String queryValue, String condition) {
		boolean chkResult = false;
		try {
			if (StringUtils.startsWith(fieldType, Constants.FIELD_TYPE_OF_TIMESTAMP)) {
				SimpleDateFormat fieldDateFormat = new SimpleDateFormat(dateFormat);
				Calendar fCal = Calendar.getInstance();
				fCal.setTime(fieldDateFormat.parse(fieldValue));

				SimpleDateFormat queryDateFormat = Constants.FORMAT_YYYY_MM_DD;
				Calendar qCal = Calendar.getInstance();
				qCal.setTime(queryDateFormat.parse(queryValue));

				switch (condition) {
					case Constants.SYMBOL_EQUAL:
						// 日期欄位相等比對下，將時分秒歸零後再比較
						fCal.set(fCal.get(Calendar.YEAR), fCal.get(Calendar.MONTH), fCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
						qCal.set(qCal.get(Calendar.YEAR), qCal.get(Calendar.MONTH), qCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

						chkResult = qCal.compareTo(fCal) == 0;
						break;

					case Constants.SYMBOL_GREATER_OR_EQUAL:
						chkResult = qCal.compareTo(fCal) >= 0;
						break;

					case Constants.SYMBOL_LESS_OR_EQUAL:
						chkResult = qCal.compareTo(fCal) <= 0;
						break;
				}
			}
		} catch (ParseException e) {
			log.error("Net Flow查詢比對日期條件過程，轉換日期格式異常 >> " + e.toString(), e);
		}

		return chkResult;
	}

	@Override
	public String findTargetTableNameByGroupId(String groupId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select table_name ")
          .append(" from Net_Flow_Table_Mapping ")
          .append(" where 1=1 ")
          .append(" and group_id = :groupId ");

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createNativeQuery(sb.toString());
        q.setParameter("groupId", groupId);

        List<Object> retList = (List<Object>)q.list();

        if (retList != null && !retList.isEmpty()) {
            return Objects.toString(retList.get(0), null);

        } else {
            return null;
        }
	}

	@Override
	public BigDecimal getTotalFlowOfQueryConditionsFromDB(NetFlowVO nfVO, List<String> searchLikeField, String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select sum(size) ")
          .append(" from ").append(tableName).append(" nfrd ")
          .append(" where 1=1 ");

        if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
            sb.append(" and nfrd.group_id = :groupId ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
            sb.append(" and nfrd.source_ip = :querySourceIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
            sb.append(" and nfrd.source_port = :querySourcePort ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
            sb.append(" and nfrd.destination_ip = :queryDestinationIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
            sb.append(" and nfrd.destination_port = :queryDestinationPort ");
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
            sb.append(" and nfrd.sender_ip = :querySenderIp ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
            sb.append(" and ( nfrd.source_MAC = :queryMac ")
              .append("       or nfrd.destination_MAC = :queryMac ) ");
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
            //sb.append(" and (nfrd.now >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.now < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ");
            sb.append(" and nfrd.from_date = :queryDateStr ")
              .append(" and (nfrd.from_time >= :queryTimeBeginStr and nfrd.from_time < :queryTimeEndStr) ");
        }
        /*
        if (StringUtils.isNotBlank(nfVO.getQueryDateEnd())) {
            sb.append(" and ( nfrd.now < DATE_ADD(:endDate, INTERVAL 1 DAY) ")
              .append("       or nfrd.from_date_time < DATE_ADD(:endDate, INTERVAL 1 DAY) ")
              .append("       or nfrd.to_date_time < DATE_ADD(:endDate, INTERVAL 1 DAY) ) ");
        }
        */

        if (StringUtils.isNotBlank(nfVO.getSearchValue())) {
            StringBuffer sfb = new StringBuffer();
            sfb.append(" and ( ");

            int i = 0;
            for (String sField : searchLikeField) {
                sfb.append(sField).append(" like :searchValue ");

                if (i < searchLikeField.size() - 1) {
                    sfb.append(" or ");
                }

                i++;
            }

            sfb.append(" ) ");
            sb.append(sfb);
        }

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createNativeQuery(sb.toString());

        if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
            q.setParameter("groupId", nfVO.getQueryGroupId());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
            q.setParameter("querySourceIp", nfVO.getQuerySourceIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
            q.setParameter("querySourcePort", nfVO.getQuerySourcePort());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
            q.setParameter("queryDestinationIp", nfVO.getQueryDestinationIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
            q.setParameter("queryDestinationPort", nfVO.getQueryDestinationPort());
        }
        if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
            q.setParameter("querySenderIp", nfVO.getQuerySenderIp());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
            q.setParameter("queryMac", nfVO.getQueryMac());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
            q.setParameter("queryDateStr", nfVO.getQueryDateBegin());
            q.setParameter("queryTimeBeginStr", nfVO.getQueryTimeBegin());
            q.setParameter("queryTimeEndStr", nfVO.getQueryTimeEnd());
        }
        if (StringUtils.isNotBlank(nfVO.getQueryDateEnd())) {
            q.setParameter("endDate", nfVO.getQueryDateEnd());
        }
        if (StringUtils.isNotBlank(nfVO.getSearchValue())) {
            q.setParameter("searchValue", "%".concat(nfVO.getSearchValue()).concat("%"));
        }

        List<BigDecimal> retList = (List<BigDecimal>)q.list();
        return (retList != null && !retList.isEmpty()) ? retList.get(0) : null;
	}

    @Override
    public List<DataPollerSetting> getHasAlreadySetUpNetFlowDataPollerInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append(" select dps ")
          .append(" from DataPollerSetting dps ")
          .append(" where 1=1 ")
          .append(" and dps.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
          .append(" and dps.fileNameRegex <> :fileNameRegex ")
          .append(" and dps.dataType = :dataType ");

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("fileNameRegex", Env.DEFAULT_NET_FLOW_FILE_NAME_REGEX);
        q.setParameter("dataType", Env.DEFAULT_NET_FLOW_DATA_TYPE);

        return (List<DataPollerSetting>)q.list();
    }

    @Override
    public List<Object[]> getUploadFlowExceedLimitSizeIpData(String tableName, String nowDateStr, String limitSize) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select 'UPLOAD', nfrd.source_ip, sum(nfrd.size) ")
          .append(" from ").append(tableName).append(" nfrd ")
          .append(" where 1=1 ")
          .append(" and nfrd.now_date_str = '").append(nowDateStr).append("' ")
          .append(" group by nfrd.source_ip ")
          .append(" having sum(nfrd.size) > ").append(limitSize);

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createNativeQuery(sb.toString());
        return (List<Object[]>)q.list();
    }

    @Override
    public List<Object[]> getDownloadFlowExceedLimitSizeIpData(String tableName, String nowDateStr, String limitSize) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select 'DOWNLOAD', nfrd.destination_ip, sum(nfrd.size) ")
          .append(" from ").append(tableName).append(" nfrd ")
          .append(" where 1=1 ")
          .append(" and nfrd.now_date_str = '").append(nowDateStr).append("' ")
          .append(" group by nfrd.destination_ip ")
          .append(" having sum(nfrd.size) > ").append(limitSize);

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createNativeQuery(sb.toString());
        return (List<Object[]>)q.list();
    }

    @Override
    public boolean chkFlowExceedIpHasAlreadyExistsInStatToday(String groupId, String nowDateStr,
        String direction, String ipAddr) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(statId) ")
          .append(" from NetFlowIpStat nfis ")
          .append(" where 1=1 ")
          .append(" and nfis.statDate = :nowDateStr ")
          .append(" and nfis.groupId = :groupId ")
          .append(" and nfis.ipAddr = :ipAddr ")
          .append(" and nfis.direction = :direction ");

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("nowDateStr", nowDateStr);
        q.setParameter("groupId", groupId);
        q.setParameter("ipAddr", ipAddr);
        q.setParameter("direction", direction);

        int size = ((Number)q.uniqueResult()).intValue();
        return (size == 0) ? false : true;
    }

    @Override
    public NetFlowIpStat saveNetFlowIpStat(NetFlowIpStat netFlowIpStat) {
        netFlowIpStat = (NetFlowIpStat)secondSessionFactory.getCurrentSession().merge(netFlowIpStat);
        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        session.save(netFlowIpStat);

        return netFlowIpStat;
    }

    @Override
    public void updateNetFlowIpStat(NetFlowIpStat netFlowIpStat) {
        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        session.update(netFlowIpStat);
    }

    @Override
    public List<NetFlowIpStat> findNetFlowIpStat4Resend(String nowDateStr, String sendPRTGFlag) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select nfis ")
          .append(" from NetFlowIpStat nfis ")
          .append(" where 1=1 ")
          .append(" and nfis.statDate = :nowDateStr ")
          .append(" and nfis.sendPrtgFlag = :sendPrtgFlag ");

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("nowDateStr", nowDateStr);
        q.setParameter("sendPrtgFlag", sendPRTGFlag);

        return (List<NetFlowIpStat>)q.list();
    }

    @Override
    public NetFlowIpStat findNetFlowIpStatByStatId(String statId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select nfis ")
          .append(" from NetFlowIpStat nfis ")
          .append(" where 1=1 ")
          .append(" and nfis.statId = :statId ");

        Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }

        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("statId", statId);

        return (NetFlowIpStat)q.uniqueResult();
    }
}
