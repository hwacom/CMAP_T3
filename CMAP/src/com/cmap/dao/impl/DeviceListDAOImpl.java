package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.dao.DeviceListDAO;
import com.cmap.dao.vo.DeviceListDAOVO;
import com.cmap.model.DeviceList;

@Repository("deviceListDAO")
@Transactional
public class DeviceListDAOImpl extends BaseDaoHibernate implements DeviceListDAO {

	@Override
	public DeviceList findDeviceListByDeviceListId(String deviceListId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DeviceList dl ")
		  .append(" where 1=1 ")
		  .append(" and dl.deviceListId = :deviceListId ");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("deviceListId", deviceListId);
	    
	    List<DeviceList> returnList = (List<DeviceList>)q.list();
	    
		return returnList.isEmpty() ? null : returnList.get(0);
	}
	
	@Override
	public DeviceList findDeviceListByGroupAndDeviceId(String groupId, String deviceId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from DeviceList dl ")
		  .append(" where 1=1 ")
		  .append(" and dl.groupId = :groupId ")
		  .append(" and dl.deviceId = :deviceId ");
		
		List<DeviceList> returnList = (List<DeviceList>)getHibernateTemplate()
															.findByNamedParam(
																	sb.toString(), 
																	new String[] {"groupId", "deviceId"}, 
																	new String[] {groupId, deviceId}
															 );
		return returnList.isEmpty() ? null : returnList.get(0);
	}
	
	@Override
	public long countDeviceListAndLastestVersionByDAOVO(DeviceListDAOVO dlDAOVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(distinct dl.DEVICE_LIST_ID) ")
		  .append("   from Device_List dl ")
		  .append("   left join ( ")
		  .append("     select cvi_0.* ")
		  .append("     from Config_Version_Info cvi_0 ")
		  .append("     where 1 = 1 ")
		  .append("     and (cvi_0.GROUP_ID, cvi_0.DEVICE_ID,cvi_0.VERSION_ID) ")
		  .append("     in ")
		  .append("     ( ")
		  .append("       select cvi_1.GROUP_ID, cvi_1.DEVICE_ID, max(cvi_1.VERSION_ID) ")
		  .append("       from Config_Version_Info cvi_1 ")
		  .append("       where 1 = 1 ")
		  .append("       and (cvi_1.GROUP_ID, cvi_1.DEVICE_ID, cvi_1.CREATE_TIME) ")
		  .append("       in ")
		  .append("       ( ")
		  .append("         select cvi_2.GROUP_ID, cvi_2.DEVICE_ID, max(cvi_2.CREATE_TIME) ")
		  .append("         from Config_Version_Info cvi_2 ")
		  .append("         where 1 = 1 ")
		  .append("         and cvi_2.DELETE_FLAG = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");
		
		if (StringUtils.isNotBlank(dlDAOVO.getQueryGroup())) {
			sb.append("    and cvi_2.GROUP_ID = :groupId ");
		} else {
			sb.append("     and cvi_2.GROUP_ID in (:groupId) ");
		}
		
		if (StringUtils.isNotBlank(dlDAOVO.getQueryDevice())) {
			sb.append("     and cvi_1.DEVICE_ID = :deviceId ");
		} else {
			sb.append("     and cvi_1.DEVICE_ID in (:deviceId) ");
		}
		
		sb.append("         group BY cvi_2.GROUP_ID, cvi_2.DEVICE_ID ")
		  .append("       ) ")
		  .append("       group by cvi_1.GROUP_ID, cvi_1.DEVICE_ID ")
		  .append("     ) ")
		  .append("   ) cvi ")
		  .append("   on dl.GROUP_ID = cvi.GROUP_ID ")
		  .append("   and dl.DEVICE_ID = cvi.DEVICE_ID ")
		  .append("   where 1 = 1 ")
		  .append("   and dl.DELETE_FLAG = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");
		
		if (StringUtils.isNotBlank(dlDAOVO.getQueryGroup())) {
			sb.append(" and dl.GROUP_ID = :groupId ");
		} else {
			sb.append(" and dl.GROUP_ID in (:groupId) ");
		}
		
		if (StringUtils.isNotBlank(dlDAOVO.getQueryDevice())) {
			sb.append(" and dl.DEVICE_ID = :deviceId ");
		} else {
			sb.append(" and dl.DEVICE_ID in (:deviceId) ");
		}
		
		if (StringUtils.isNotBlank(dlDAOVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       dl.GROUP_NAME like :searchValue ")
			  .append("       or ")
			  .append("       dl.DEVICE_NAME like :searchValue ")
			  .append("       or ")
			  .append("       dl.SYSTEM_VERSION like :searchValue ")
			  .append("       or ")
			  .append("       cvi.CONFIG_VERSION like :searchValue ")
			  .append("     ) ");
		}
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());
	    
	    q.setParameter("groupId", StringUtils.isNotBlank(dlDAOVO.getQueryGroup()) ? dlDAOVO.getQueryGroup() : dlDAOVO.getQueryGroupList());
	    q.setParameter("deviceId", StringUtils.isNotBlank(dlDAOVO.getQueryDevice()) ? dlDAOVO.getQueryDevice() : dlDAOVO.getQueryDeviceList());
	    
	    if (StringUtils.isNotBlank(dlDAOVO.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(dlDAOVO.getSearchValue()).concat("%"));
	    }
		
	    return DataAccessUtils.longResult(q.list());
	}
	
	@Override
	public List<Object[]> findDeviceListAndLastestVersionByDAOVO(DeviceListDAOVO dlDAOVO, Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct ")
		  .append(composeSelectStr(Constants.NATIVE_FIELD_NAME_FOR_DEVICE_2, "dl"))
		  .append(", ")
		  .append(composeSelectStr(Constants.NATIVE_FIELD_NAME_FOR_VERSION_2, "cvi"))
		  .append(" from Device_List dl ")
		  .append(" left join ( ")
		  .append("     select cvi_0.* ")
		  .append("     from Config_Version_Info cvi_0 ")
		  .append("     where 1 = 1 ")
		  .append("     and (cvi_0.GROUP_ID, cvi_0.DEVICE_ID,cvi_0.VERSION_ID) ")
		  .append("     in ")
		  .append("     ( ")
		  .append("       select cvi_1.GROUP_ID, cvi_1.DEVICE_ID, max(cvi_1.VERSION_ID) ")
		  .append("   	  from Config_Version_Info cvi_1 ")
		  .append("   	  where 1 = 1 ")
		  .append("   	  and (cvi_1.GROUP_ID, cvi_1.DEVICE_ID, cvi_1.CREATE_TIME) ")
		  .append("   	  in ")
		  .append("   	  ( ")
		  .append("     	select cvi_2.GROUP_ID, cvi_2.DEVICE_ID, max(cvi_2.CREATE_TIME) ")
		  .append("     	from Config_Version_Info cvi_2 ")
		  .append("     	where 1 = 1 ")
		  .append("     	and cvi_2.DELETE_FLAG = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");
		
		if (StringUtils.isNotBlank(dlDAOVO.getQueryGroup())) {
			sb.append(" 	and cvi_2.GROUP_ID = :groupId ");
		} else {
			sb.append(" 	and cvi_2.GROUP_ID in (:groupId) ");
		}
		
		if (StringUtils.isNotBlank(dlDAOVO.getQueryDevice())) {
			sb.append(" 	and cvi_1.DEVICE_ID = :deviceId ");
		} else {
			sb.append(" 	and cvi_1.DEVICE_ID in (:deviceId) ");
		}
		
		sb.append("     	group BY cvi_2.GROUP_ID, cvi_2.DEVICE_ID ")
		  .append("   	  ) ")
		  .append("       group by cvi_1.GROUP_ID, cvi_1.DEVICE_ID ")
		  .append("     ) ")
		  .append(" ) cvi ")
		  .append(" on dl.GROUP_ID = cvi.GROUP_ID ")
		  .append(" and dl.DEVICE_ID = cvi.DEVICE_ID ")
		  .append(" where 1 = 1 ")
		  .append(" and dl.DELETE_FLAG = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");
		
		if (StringUtils.isNotBlank(dlDAOVO.getQueryGroup())) {
			sb.append(" and dl.GROUP_ID = :groupId ");
		} else {
			sb.append(" and dl.GROUP_ID in (:groupId) ");
		}
		
		if (StringUtils.isNotBlank(dlDAOVO.getQueryDevice())) {
			sb.append(" and dl.DEVICE_ID = :deviceId ");
		} else {
			sb.append(" and dl.DEVICE_ID in (:deviceId) ");
		}
		
		if (StringUtils.isNotBlank(dlDAOVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       dl.GROUP_NAME like :searchValue ")
			  .append("       or ")
			  .append("       dl.DEVICE_NAME like :searchValue ")
			  .append("       or ")
			  .append("       dl.SYSTEM_VERSION like :searchValue ")
			  .append("       or ")
			  .append("       cvi.CONFIG_VERSION like :searchValue ")
			  .append("     ) ");
		}
		
		if (StringUtils.isNotBlank(dlDAOVO.getOrderColumn())) {
			sb.append(" order by dl.").append(dlDAOVO.getOrderColumn()).append(" ").append(dlDAOVO.getOrderDirection());
			
		} else {
			sb.append(" order by dl.GROUP_NAME asc, dl.DEVICE_NAME asc ");
		}
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());
	    
	    q.setParameter("groupId", StringUtils.isNotBlank(dlDAOVO.getQueryGroup()) ? dlDAOVO.getQueryGroup() : dlDAOVO.getQueryGroupList());
	    q.setParameter("deviceId", StringUtils.isNotBlank(dlDAOVO.getQueryDevice()) ? dlDAOVO.getQueryDevice() : dlDAOVO.getQueryDeviceList());
	    
	    if (StringUtils.isNotBlank(dlDAOVO.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(dlDAOVO.getSearchValue()).concat("%"));
	    }
	    
	    if (startRow != null && pageLength != null) {
	    	q.setFirstResult(startRow);
		    q.setMaxResults(pageLength);
	    }
	    
	    List<Object[]> retList = (List<Object[]>)q.list();
	    
		return transObjList2ModelList4Device(retList);
	}

	@Override
	public void saveOrUpdateDeviceListByModel(List<DeviceList> entityList) {
		for (DeviceList entity : entityList) {
			getHibernateTemplate().saveOrUpdate(entity);
		}
	}

	@Override
	public List<DeviceList> findDistinctDeviceListByGroupIdsOrDeviceIds(List<String> groupIds, List<String> deviceIds) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct dl ")
		  .append(" from DeviceList dl ")
		  .append(" where 1=1 ")
		  .append(" and ( ");
		
		if (groupIds != null && !groupIds.isEmpty()) {
			sb.append("   dl.groupId in (:groupIds) ");
		}
		if ((groupIds != null && !groupIds.isEmpty()) && (deviceIds != null && !deviceIds.isEmpty())) {
			sb.append("   or ");
		}
		if (deviceIds != null && !deviceIds.isEmpty()) {
			sb.append("   dl.deviceId in (:deviceIds) ");
		} 
		sb.append(" ) ")
		  .append(" order by ");
		
		if (groupIds != null && !groupIds.isEmpty()) {
			sb.append(" dl.groupId asc ");
		}
		if ((groupIds != null && !groupIds.isEmpty()) && (deviceIds != null && !deviceIds.isEmpty())) {
			sb.append(" , ");
		}
		if (deviceIds != null && !deviceIds.isEmpty()) {
			sb.append(" dl.deviceId asc ");
		}
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
	    if (groupIds != null && !groupIds.isEmpty()) {
	    	q.setParameter("groupIds", groupIds);
	    }
	    if (deviceIds != null && !deviceIds.isEmpty()) {
	    	q.setParameter("deviceIds", deviceIds);
	    }
	    
		return (List<DeviceList>)q.list();
	}

	@Override
	public List<Object[]> getGroupIdAndNameByGroupIds(List<String> groupIds) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct dl.groupId, dl.groupName ")
		  .append(" from DeviceList dl ")
		  .append(" where 1=1 ");
		
		if (groupIds != null && !groupIds.isEmpty()) {
			sb.append(" and dl.groupId in (:groupIds) ");
		}
		
		sb.append(" order by dl.groupId asc ");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
	    if (groupIds != null && !groupIds.isEmpty()) {
	    	q.setParameter("groupIds", groupIds);
	    }
	    
		return (List<Object[]>)q.list();
	}

	@Override
	public List<Object[]> getDeviceIdAndNameByDeviceIds(List<String> deviceIds) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct dl.deviceId, dl.deviceName ")
		  .append(" from DeviceList dl ")
		  .append(" where 1=1 ");
		
		if (deviceIds != null && !deviceIds.isEmpty()) {
			sb.append(" and dl.deviceId in (:deviceIds) ");
		}
		
		sb.append(" order by dl.deviceId asc ");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    
	    if (deviceIds != null && !deviceIds.isEmpty()) {
	    	q.setParameter("deviceIds", deviceIds);
	    }
	    
		return (List<Object[]>)q.list();
	}
}
