package com.cmap.plugin.module.ip.mapping;

import java.util.List;
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
import com.cmap.annotation.Log;
import com.cmap.dao.impl.BaseDaoHibernate;
import com.cmap.model.MibOidMapping;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Repository("ipMappingDAO")
@Transactional
public class IpMappingDAOImpl extends BaseDaoHibernate implements IpMappingDAO {
    @Log
    private static Logger log;

    @Autowired
    @Qualifier("secondSessionFactory")
    private SessionFactory secondSessionFactory;

    @Override
    public List<ModuleMacTableExcludePort> findModuleMacTableExcludePort(String groupId, String deviceId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ModuleMacTableExcludePort mmtep ")
          .append(" where 1=1 ")
          .append(" and mmtep.groupId = :groupId ")
          .append(" and mmtep.deviceId = :deviceId ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("groupId", groupId);
        q.setParameter("deviceId", deviceId);
        return (List<ModuleMacTableExcludePort>)q.list();
    }

    @Override
    public List<MibOidMapping> findMibOidMappingByNames(List<String> oidNames) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from MibOidMapping mom ")
          .append(" where 1=1 ")
          .append(" and mom.oidName in (:oidNames) ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameterList("oidNames", oidNames);
        return (List<MibOidMapping>)q.list();
    }

	@Override
	public List<MibOidMapping> findMibOidMappingOfTableEntryByNameLike(String tableOidName) {
		StringBuffer sb = new StringBuffer();
        sb.append(" from MibOidMapping mom ")
          .append(" where 1=1 ")
          .append(" and mom.oidName like :tableOidName ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("tableOidName", tableOidName + ".%");
        return (List<MibOidMapping>)q.list();
	}

	@Override
	public List<Object[]> findEachIpAddressLastestModuleIpMacPortMapping(String groupId) {
		/*
		 * SELECT 
				m1.group_id
			   ,m1.ip_address
			   ,m1.mac_address
			   ,m1.port_id
			FROM `module_ip_mac_port_mapping` m1
			    ,(SELECT
			      	mm.group_id	
			       ,mm.ip_address
			       ,max(create_time) create_time
			      FROM `module_ip_mac_port_mapping` mm
			      WHERE 1=1
			      and mm.group_id = '15931'
			      group BY
			      	mm.group_id	
			       ,mm.ip_address
			     ) m2
			WHERE 1=1
			AND m1.group_id = m2.group_id
			AND m1.ip_address = m2.ip_address
			AND m1.create_time = m2.create_time
			AND m1.group_id = '15931'
		 */
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ")
		  .append("   m1.group_id, m1.device_id, m1.ip_address, m1.mac_address, m1.port_id ")
		  .append(" FROM Module_Ip_Mac_Port_Mapping m1 ")
		  .append("     ,(SELECT ")
		  .append("         mm.group_id, mm.device_id, mm.ip_address, max(create_time) create_time ")
		  .append("       FROM Module_Ip_Mac_Port_Mapping mm ")
		  .append("       WHERE 1=1 ")
		  .append("       AND mm.group_id = :groupId ")
		  .append("       GROUP BY ")
		  .append("         mm.group_id, mm.device_id, mm.ip_address ")
		  .append("      ) m2 ")
		  .append(" WHERE 1=1 ")
          .append(" AND m1.group_id = m2.group_id ")
          .append(" AND m1.device_id = m2.device_id ")
          .append(" AND m1.ip_address = m2.ip_address ")
          .append(" AND m1.create_time = m2.create_time ")
          .append(" AND m1.group_id = :groupId ");

      Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
      Query<?> q = session.createNativeQuery(sb.toString());
      q.setParameter("groupId", groupId);
      return (List<Object[]>)q.list();
	}

	@Override
	public long countModuleIpMacPortMappingChange(IpMappingServiceVO imsVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ")
		  .append("   count(distinct mc.MAPPING_ID) ")
		  .append(" FROM Module_Ip_Mac_Port_Mapping_Change mc ")
		  .append("     ,Device_List dl ")
		  .append("     ,Device_Port_Info dpi ")
		  .append(" WHERE 1=1 ")
          .append(" AND mc.group_id = dl.GROUP_ID ")
          .append(" AND mc.device_id = dl.DEVICE_ID ")
          .append(" AND dl.device_model = dpi.DEVICE_MODEL ")
          .append(" AND mc.port_id = dpi.PORT_ID ");
		
		if (StringUtils.isNotBlank(imsVO.getQueryGroup())) {
			sb.append(" AND mc.group_id = :groupId ");
		}
		if (StringUtils.isNotBlank(imsVO.getQueryDevice())) {
			sb.append(" AND mc.device_id = :deviceId ");
		}
		if (StringUtils.isNotBlank(imsVO.getQueryIp())) {
			sb.append(" AND mc.ip_address = :ipAddress ");
		}
		if (StringUtils.isNotBlank(imsVO.getQueryMac())) {
			sb.append(" AND mc.mac_address = :macAddress ");
		}
		if (StringUtils.isNotBlank(imsVO.getQueryPort())) {
			sb.append(" AND dpi.port_name = :portName ");
		}
		if (StringUtils.isNotBlank(imsVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       dl.group_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.device_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.device_model like :searchValue ")
			  .append("       or ")
			  .append("       mc.ip_address like :searchValue ")
			  .append("		  or ")
			  .append("       mc.mac_address like :searchValue ")
			  .append("		  or ")
			  .append("       dpi.port_name like :searchValue ")
			  .append("     ) ");
		}

		Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }
        
		Query<?> q = session.createNativeQuery(sb.toString());
		if (StringUtils.isNotBlank(imsVO.getQueryGroup())) {
			q.setParameter("groupId", imsVO.getQueryGroup());
		}
		if (StringUtils.isNotBlank(imsVO.getQueryDevice())) {
			q.setParameter("deviceId", imsVO.getQueryDevice());
		}
		if (StringUtils.isNotBlank(imsVO.getQueryIp())) {
			q.setParameter("ipAddress", imsVO.getQueryIp());
		}
		if (StringUtils.isNotBlank(imsVO.getQueryMac())) {
			q.setParameter("macAddress", imsVO.getQueryMac());
		}
		if (StringUtils.isNotBlank(imsVO.getQueryPort())) {
			q.setParameter("portName", imsVO.getQueryPort());
		}
		if (StringUtils.isNotBlank(imsVO.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(imsVO.getSearchValue()).concat("%"));
	    }
		return DataAccessUtils.longResult(q.list());
	}

	@Override
	public List<Object[]> findModuleIpMacPortMappingChange(IpMappingServiceVO imsVO, Integer startRow,
			Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ")
		  .append("   mc.RECORD_DATE ")
		  .append("  ,mc.RECORD_TIME ")
		  .append("  ,mc.GROUP_ID ")
		  .append("  ,dl.GROUP_NAME ")
		  .append("  ,mc.DEVICE_ID ")
		  .append("  ,dl.DEVICE_NAME ")
		  .append("  ,dl.DEVICE_MODEL ")
		  .append("  ,mc.IP_ADDRESS ")
		  .append("  ,mc.MAC_ADDRESS ")
		  .append("  ,mc.PORT_ID ")
		  .append("  ,dpi.PORT_NAME ")
		  .append(" FROM Module_Ip_Mac_Port_Mapping_Change mc ")
		  .append("     ,Device_List dl ")
		  .append("     ,Device_Port_Info dpi ")
		  .append(" WHERE 1=1 ")
          .append(" AND mc.group_id = dl.GROUP_ID ")
          .append(" AND mc.device_id = dl.DEVICE_ID ")
          .append(" AND dl.device_model = dpi.DEVICE_MODEL ")
          .append(" AND mc.port_id = dpi.PORT_ID ");
		
		if (StringUtils.isNotBlank(imsVO.getQueryGroup())) {
			sb.append(" AND mc.group_id = :groupId ");
		}
		if (StringUtils.isNotBlank(imsVO.getQueryDevice())) {
			sb.append(" AND mc.device_id = :deviceId ");
		}
		if (StringUtils.isNotBlank(imsVO.getQueryIp())) {
			sb.append(" AND mc.ip_address = :ipAddress ");
		}
		if (StringUtils.isNotBlank(imsVO.getQueryMac())) {
			sb.append(" AND mc.mac_address = :macAddress ");
		}
		if (StringUtils.isNotBlank(imsVO.getQueryPort())) {
			sb.append(" AND dpi.port_name = :portName ");
		}
		if (StringUtils.isNotBlank(imsVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       dl.group_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.device_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.device_model like :searchValue ")
			  .append("       or ")
			  .append("       mc.ip_address like :searchValue ")
			  .append("		  or ")
			  .append("       mc.mac_address like :searchValue ")
			  .append("		  or ")
			  .append("       dpi.port_name like :searchValue ")
			  .append("     ) ");
		}
		
		// "","Create_Time","Group_Name","Device_Name","Device_Model","Ip_Address","Mac_Address","Port"
		String orderColumn = imsVO.getOrderColumn();
		String orderDirection = imsVO.getOrderDirection();
		sb.append(" ORDER BY ");

		switch (orderColumn) {
			case "Create_Time":
				sb.append(" dl.GROUP_NAME, dl.DEVICE_NAME, mc.IP_ADDRESS, mc.CREATE_TIME ").append(orderDirection);
				break;
				
			case "Group_Name":
				sb.append(" dl.GROUP_NAME ").append(orderDirection).append(", dl.DEVICE_NAME, mc.IP_ADDRESS, mc.CREATE_TIME ");
				break;
				
			case "Device_Name":
				sb.append(" dl.GROUP_NAME, dl.DEVICE_NAME ").append(orderDirection).append(", mc.IP_ADDRESS, mc.CREATE_TIME ");
				break;
				
			case "Ip_Address":
				sb.append(" dl.GROUP_NAME, dl.DEVICE_NAME, mc.IP_ADDRESS ").append(orderDirection).append(", mc.CREATE_TIME ");
				break;
				
			case "Mac_Address":
				sb.append(" dl.GROUP_NAME, dl.DEVICE_NAME, mc.Mac_Address ").append(orderDirection).append(", mc.IP_ADDRESS, mc.CREATE_TIME ");
				break;
				
			case "Port":
				sb.append(" dl.GROUP_NAME, dl.DEVICE_NAME, dpi.PORT_NAME ").append(orderDirection).append(", mc.IP_ADDRESS, mc.CREATE_TIME ");
				break;
				
			default:
				sb.append(" dl.GROUP_NAME, dl.DEVICE_NAME, mc.IP_ADDRESS, mc.CREATE_TIME desc");
				break;
		}
		
		Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }
        
		Query<?> q = session.createNativeQuery(sb.toString());
		if (StringUtils.isNotBlank(imsVO.getQueryGroup())) {
			q.setParameter("groupId", imsVO.getQueryGroup());
		}
		if (StringUtils.isNotBlank(imsVO.getQueryDevice())) {
			q.setParameter("deviceId", imsVO.getQueryDevice());
		}
		if (StringUtils.isNotBlank(imsVO.getQueryIp())) {
			q.setParameter("ipAddress", imsVO.getQueryIp());
		}
		if (StringUtils.isNotBlank(imsVO.getQueryMac())) {
			q.setParameter("macAddress", imsVO.getQueryMac());
		}
		if (StringUtils.isNotBlank(imsVO.getQueryPort())) {
			q.setParameter("portName", imsVO.getQueryPort());
		}
		if (StringUtils.isNotBlank(imsVO.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(imsVO.getSearchValue()).concat("%"));
	    }
		if (startRow != null && pageLength != null) {
	    	q.setFirstResult(startRow);
		    q.setMaxResults(pageLength);
	    }
		return (List<Object[]>)q.list();
	}

	@Override
	public List<Object[]> findNearlyModuleIpMacPortMappingByTime(String groupId, String ipAddress, String date, String time) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ")
		  .append("   mimpm.mapping_id ")
		  .append("  ,mimpm.group_id ")
		  .append("  ,dl.group_name ")
		  .append("  ,mimpm.device_id ")
		  .append("  ,dl.device_name ")
		  .append("  ,dl.device_model ")
		  .append("  ,mimpm.port_id ")
		  .append("  ,dpi.port_name ")
		  .append(" FROM Module_Ip_Mac_Port_Mapping mimpm ")
		  .append("     ,Device_List dl ")
		  .append("     ,Device_Port_Info dpi ")
		  .append(" WHERE 1=1 ")
          .append(" AND mimpm.group_id = dl.GROUP_ID ")
          .append(" AND mimpm.device_id = dl.DEVICE_ID ")
          .append(" AND dl.device_model = dpi.DEVICE_MODEL ")
          .append(" AND mimpm.port_id = dpi.PORT_ID ")
          .append(" AND mimpm.GROUP_ID = :groupId ")
          .append(" AND mimpm.IP_ADDRESS = :ipAddress ")
          .append(" AND mimpm.RECORD_DATE = :queryDate ")
          .append(" AND mimpm.RECORD_TIME < :queryTime ")
          .append(" ORDER BY ")
          .append(" 	mimpm.RECORD_TIME DESC ")
          .append(" LIMIT 1 ");
		
		Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }
        
		Query<?> q = session.createNativeQuery(sb.toString());
		q.setParameter("groupId", groupId);
		q.setParameter("ipAddress", ipAddress);
		q.setParameter("queryDate", date);
		q.setParameter("queryTime", time);
		
		return (List<Object[]>)q.list();
	}
}
