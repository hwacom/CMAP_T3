package com.cmap.dao.impl;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.ConfigVersionInfoDAO;
import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.model.ConfigVersionInfo;

@Repository
@Transactional
public class ConfigVersionInfoDAOImpl extends BaseDaoHibernate implements ConfigVersionInfoDAO {

	@Override
	public long countConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(cvi.version_id) from ( ")
		  .append("   select cvi_1.* from Config_Version_Info cvi_1 ")
		  .append("   where 1=1 ")
		  .append("   and cvi_1.delete_flag = 'N' ");
		
		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup1())) {
			sb.append(" and cvi_1.group_Id = :groupId_1 ");
		} else {
			sb.append(" and cvi_1.group_Id in (:groupId_1) ");
		}
		
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice1())) {
			sb.append(" and cvi_1.device_Id = :deviceId_1 ");
		} else {
			sb.append(" and cvi_1.device_Id in (:deviceId_1) ");
		}
		
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
			sb.append(" and cvi_1.create_Time >= DATE_FORMAT(:beginDate_1, '%Y-%m-%d') ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
			sb.append(" and cvi_1.create_Time < DATE_ADD(:endDate_1, INTERVAL 1 DAY) ");
		}
		
		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
			sb.append(" union ( ");
			sb.append("   select cvi_2.* from Config_Version_Info cvi_2 ")
			  .append("   where 1=1 ")
			  .append("   and cvi_2.delete_flag = 'N' ")
			  .append("   and cvi_2.group_Id = :groupId_2 ");
			
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice2())) {
				sb.append(" and cvi_2.device_Id = :deviceId_2 ");
			} else {
				sb.append(" and cvi_2.device_Id in (:deviceId_2) ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin2())) {
				sb.append(" and cvi_2.create_Time >= DATE_FORMAT(:beginDate_2, '%Y-%m-%d') ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd2())) {
				sb.append(" and cvi_2.create_Time < DATE_ADD(:endDate_2, INTERVAL 1 DAY) ");
			}
			sb.append(" ) ");
		}
		sb.append(" ) cvi ");
		
		if (StringUtils.isNotBlank(cviDAOVO.getSearchValue())) {
			sb.append(" where 1=1 ")
			  .append(" and ( ")
			  .append("       cvi.group_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.device_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.system_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_version like :searchValue ")
			  .append("     ) ");
		}
	    
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());
	    
	    q.setParameter("groupId_1", StringUtils.isNotBlank(cviDAOVO.getQueryGroup1()) ? cviDAOVO.getQueryGroup1() : cviDAOVO.getQueryGroup1List());
	    q.setParameter("deviceId_1", StringUtils.isNotBlank(cviDAOVO.getQueryDevice1()) ? cviDAOVO.getQueryDevice1() : cviDAOVO.getQueryDevice1List());
	    
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
	    	q.setParameter("beginDate_1", cviDAOVO.getQueryDateBegin1());
	    }
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
	    	q.setParameter("endDate_1", cviDAOVO.getQueryDateEnd1());
	    }
	    
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
	    	q.setParameter("groupId_2", cviDAOVO.getQueryGroup2());
	    	q.setParameter("deviceId_2", StringUtils.isNotBlank(cviDAOVO.getQueryDevice2()) ? cviDAOVO.getQueryDevice2() : cviDAOVO.getQueryDevice2List());
	    	
		    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin2())) {
		    	q.setParameter("beginDate_2", cviDAOVO.getQueryDateBegin2());
		    }
		    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd2())) {
		    	q.setParameter("endDate_2", cviDAOVO.getQueryDateEnd2());
		    }
	    }
	    
	    if (StringUtils.isNotBlank(cviDAOVO.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(cviDAOVO.getSearchValue()).concat("%"));
	    }
	    
	    return DataAccessUtils.longResult(q.list());
	}
	
	private String composeSelectStr(String alias) {
		StringBuffer sb = new StringBuffer();
		
		int idx = 0;
		for (String field : NATIVE_FIELD_NAME) {
			sb.append(" ").append(alias).append(".").append(field);
			
			if (idx != NATIVE_FIELD_NAME.length-1) {
				sb.append(",");
			}
			
			idx++;
		}

		return sb.toString();
	}
	
	/*
	 * 因部分查詢SQL語法HQL不支援，採用Native SQL寫法，查詢結果為Object陣列List
	 * 透過此方法將List<Object[]>轉換為List<ConfigVersionInfo>
	 */
	private List<ConfigVersionInfo> transObjList2ModelList(List<Object[]> objList) {
		List<ConfigVersionInfo> retList = new ArrayList<ConfigVersionInfo>();
		
		try {
			if (objList != null && !objList.isEmpty()) {
				
				ConfigVersionInfo cviModel;
				for (Object[] objArray : objList) {
					if (objArray != null) {
						cviModel = new ConfigVersionInfo();
						for (int i = 0; i<objArray.length; i++) {
							new PropertyDescriptor(HQL_FIELD_NAME[i], cviModel.getClass()).getWriteMethod().invoke(cviModel, objArray[i]);
						}
						
						retList.add(cviModel);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retList;
	}
	
	@Override
	public List<ConfigVersionInfo> findConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO, Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select cvi.* from ( ")
		  .append("   select ").append(composeSelectStr("cvi_1"))
		  .append("   from Config_Version_Info cvi_1 ")
		  .append("   where 1=1 ")
		  .append("   and cvi_1.delete_flag = 'N' ");
		
		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup1())) {
			sb.append(" and cvi_1.group_Id = :groupId_1 ");
		} else {
			sb.append(" and cvi_1.group_Id in (:groupId_1) ");
		}
		
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice1())) {
			sb.append(" and cvi_1.device_Id = :deviceId_1 ");
		} else {
			sb.append(" and cvi_1.device_Id in (:deviceId_1) ");
		}
		
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
			sb.append(" and cvi_1.create_Time >= DATE_FORMAT(:beginDate_1, '%Y-%m-%d') ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
			sb.append(" and cvi_1.create_Time < DATE_ADD(:endDate_1, INTERVAL 1 DAY) ");
		}
		
		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
			sb.append(" union ( ");
			sb.append("   select ").append(composeSelectStr("cvi_2"))
			  .append("   from Config_Version_Info cvi_2 ")
			  .append("   where 1=1 ")
			  .append("   and cvi_2.delete_flag = 'N' ")
			  .append("   and cvi_2.group_id = :groupId_2 ");
			
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice2())) {
				sb.append(" and cvi_2.device_Id = :deviceId_2 ");
			} else {
				sb.append(" and cvi_2.device_Id in (:deviceId_2) ");
			}
			
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin2())) {
				sb.append(" and cvi_2.create_Time >= DATE_FORMAT(:beginDate_2, '%Y-%m-%d') ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd2())) {
				sb.append(" and cvi_2.create_Time < DATE_ADD(:endDate_2, INTERVAL 1 DAY) ");
			}
			sb.append(" ) ");
		}
		sb.append(" ) cvi ");
		

		if (StringUtils.isNotBlank(cviDAOVO.getSearchValue())) {
			sb.append(" where 1=1 ")
			  .append(" and ( ")
			  .append("       cvi.group_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.device_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.system_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_version like :searchValue ")
			  .append("     ) ");
		}
		
		if (StringUtils.isNotBlank(cviDAOVO.getOrderColumn())) {
			sb.append(" order by cvi.").append(cviDAOVO.getOrderColumn()).append(" ").append(cviDAOVO.getOrderDirection());
			
		} else {
			sb.append(" order by cvi.create_Time desc, cvi.group_Name asc, cvi.device_Name asc ");
		}
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());
	    
	    q.setParameter("groupId_1", StringUtils.isNotBlank(cviDAOVO.getQueryGroup1()) ? cviDAOVO.getQueryGroup1() : cviDAOVO.getQueryGroup1List());
	    q.setParameter("deviceId_1", StringUtils.isNotBlank(cviDAOVO.getQueryDevice1()) ? cviDAOVO.getQueryDevice1() : cviDAOVO.getQueryDevice1List());
	    
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
	    	q.setParameter("beginDate_1", cviDAOVO.getQueryDateBegin1());
	    }
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
	    	q.setParameter("endDate_1", cviDAOVO.getQueryDateEnd1());
	    }
	    
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
	    	q.setParameter("groupId_2", cviDAOVO.getQueryGroup2());
	    	q.setParameter("deviceId_2", StringUtils.isNotBlank(cviDAOVO.getQueryDevice2()) ? cviDAOVO.getQueryDevice2() : cviDAOVO.getQueryDevice2List());
	    	
		    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin2())) {
		    	q.setParameter("beginDate_2", cviDAOVO.getQueryDateBegin2());
		    }
		    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd2())) {
		    	q.setParameter("endDate_2", cviDAOVO.getQueryDateEnd2());
		    }
	    }
	    
	    if (StringUtils.isNotBlank(cviDAOVO.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(cviDAOVO.getSearchValue()).concat("%"));
	    }
	    
	    if (startRow != null && pageLength != null) {
	    	q.setFirstResult(startRow);
		    q.setMaxResults(pageLength);
	    }
	    
	    List<Object[]> retList = (List<Object[]>)q.list();
	    
		return transObjList2ModelList(retList);
	}

	/*
	 * 刪除採化學刪除，僅調整刪除註記、不實際delete掉資料
	 * @see com.cmap.dao.ConfigVersionInfoDAO#deleteConfigVersionInfoByVersionIds(java.util.List, java.lang.String)
	 */
	@Override
	public Integer deleteConfigVersionInfoByVersionIds(List<String> versionIDs, String actionBy) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select cvi ")
		  .append(" from ConfigVersionInfo cvi ")
		  .append(" where 1=1 ")
		  .append(" and cvi.versionId in (:versionId) ");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("versionId", versionIDs);
		
	    List<ConfigVersionInfo> modelList = (List<ConfigVersionInfo>)q.list();
	    
	    if (modelList != null && !modelList.isEmpty()) {
	    	for (ConfigVersionInfo cvi : modelList) {
	    		cvi.setDeleteFlag(MARK_AS_DELETE);
	    		cvi.setUpdateBy(actionBy);
	    		cvi.setUpdateTime(new Timestamp(new Date().getTime()));
	    		cvi.setDeleteBy(actionBy);
	    		cvi.setDeleteTime(new Timestamp(new Date().getTime()));
	    		
	    		getHibernateTemplate().save(cvi);
	    	}
	    }
	    
		return null;
	}
}
