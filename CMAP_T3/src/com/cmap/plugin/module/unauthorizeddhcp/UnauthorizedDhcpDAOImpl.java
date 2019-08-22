package com.cmap.plugin.module.unauthorizeddhcp;

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
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Repository("unauthorizedDhcpDAO")
@Transactional
public class UnauthorizedDhcpDAOImpl extends BaseDaoHibernate implements UnauthorizedDhcpDAO {
	@Log
    private static Logger log;
	
	@Autowired
	@Qualifier("secondSessionFactory")
	private SessionFactory secondSessionFactory;

	@Override
	public long countUnauthorizedDhcpRecord(UnauthorizedDhcpServiceVO udsVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ")
		  .append("   count(distinct m4.record_date, m4.record_time, m4.group_id, m4.device_id, m4.port_id) ")
		  .append(" FROM Module_Ip_Mac_Port_Mapping m4 ")
		  .append("     ,( ")
		  .append("     	SELECT  ")
		  .append("     		m1.group_id  ")
		  .append("     	   ,max(m1.job_id) job_id  ")
		  .append("     	FROM Module_Ip_Mac_Port_Mapping m1  ")
		  .append("     	    ,(SELECT  ")
		  .append("     			mm.group_id	  ")
		  .append("     		   ,max(create_time) create_time  ")
		  .append("     		  FROM Module_Ip_Mac_Port_Mapping mm  ")
		  .append("     		  WHERE 1=1  ");
		if (StringUtils.isNotBlank(udsVO.getGroupId())) {
			sb.append("     		  and mm.group_id = :groupId ");
		}
		sb.append("     		  group BY  ")
		  .append("     		  	mm.group_id  ")
		  .append("     		 ) m2  ")
		  .append("     	WHERE 1=1  ")
		  .append("     	AND m1.group_id = m2.group_id  ")
		  .append("     	AND m1.create_time = m2.create_time  ");
		if (StringUtils.isNotBlank(udsVO.getGroupId())) {
			sb.append("     	AND m1.group_id = :groupId  ");
		}
		sb.append("       ) m3  ")
		  .append("      ,Device_List dl  ")
		  .append("      ,Device_Port_Info dpi  ")
		  .append(" WHERE 1=1 ")
          .append(" AND m4.group_id = m3.group_id ")
          .append(" AND m4.job_id = m3.job_id ")
          .append(" AND m4.group_id = dl.GROUP_ID  ")
          .append(" AND m4.device_id = dl.DEVICE_ID ")
          .append(" AND dl.device_model = dpi.DEVICE_MODEL  ")
          .append(" AND m4.port_id = dpi.PORT_ID ");
		if (StringUtils.isNotBlank(udsVO.getGroupId())) {
			sb.append(" AND m4.group_id = :groupId ");
		}
        sb.append(" GROUP BY ")
          .append(" 	m4.group_id ")
		  .append("    ,m4.device_id ")
		  .append("    ,m4.port_id ")
		  .append(" HAVING ")
		  .append(" 	count(distinct m4.mac_address) > 1 ");
		
		if (StringUtils.isNotBlank(udsVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       dl.group_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.device_name like :searchValue ")
			  .append("		  or ")
			  .append("       dpi.port_name like :searchValue ")
			  .append("     ) ");
		}

		Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }
        
		Query<?> q = session.createNativeQuery(sb.toString());
		if (StringUtils.isNotBlank(udsVO.getGroupId())) {
			q.setParameter("groupId", udsVO.getQueryGroup());
		}
		if (StringUtils.isNotBlank(udsVO.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(udsVO.getSearchValue()).concat("%"));
	    }
		return DataAccessUtils.longResult(q.list());
	}

	@Override
	public List<Object[]> findUnauthorizedDhcpRecord(UnauthorizedDhcpServiceVO udsVO, Integer startRow,
			Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ")
		  .append("   m4.record_date ")
		  .append("  ,m4.record_time ")
		  .append("  ,m4.group_id ")
		  .append("  ,dl.group_name ")
		  .append("  ,m4.device_id ")
		  .append("  ,dl.device_name ")
		  .append("  ,m4.port_id ")
		  .append("  ,dpi.port_name ")
		  .append("  ,count(distinct m4.mac_address) ")
		  .append(" FROM Module_Ip_Mac_Port_Mapping m4 ")
		  .append("     ,( ")
		  .append("     	SELECT  ")
		  .append("     		m1.group_id  ")
		  .append("     	   ,max(m1.job_id) job_id  ")
		  .append("     	FROM Module_Ip_Mac_Port_Mapping m1  ")
		  .append("     	    ,(SELECT  ")
		  .append("     			mm.group_id	  ")
		  .append("     		   ,max(create_time) create_time  ")
		  .append("     		  FROM Module_Ip_Mac_Port_Mapping mm  ")
		  .append("     		  WHERE 1=1  ");
		if (StringUtils.isNotBlank(udsVO.getGroupId())) {
			sb.append("     		  and mm.group_id = :groupId ");
		}
		sb.append("     		  group BY  ")
		  .append("     		  	mm.group_id  ")
		  .append("     		 ) m2  ")
		  .append("     	WHERE 1=1  ")
		  .append("     	AND m1.group_id = m2.group_id  ")
		  .append("     	AND m1.create_time = m2.create_time  ");
		if (StringUtils.isNotBlank(udsVO.getGroupId())) {
			sb.append("     	AND m1.group_id = :groupId  ");
		}
		sb.append("       ) m3  ")
		  .append("      ,Device_List dl  ")
		  .append("      ,Device_Port_Info dpi  ")
		  .append(" WHERE 1=1 ")
          .append(" AND m4.group_id = m3.group_id ")
          .append(" AND m4.job_id = m3.job_id ")
          .append(" AND m4.group_id = dl.GROUP_ID  ")
          .append(" AND m4.device_id = dl.DEVICE_ID ")
          .append(" AND dl.device_model = dpi.DEVICE_MODEL  ")
          .append(" AND m4.port_id = dpi.PORT_ID ");
		if (StringUtils.isNotBlank(udsVO.getGroupId())) {
			sb.append(" AND m4.group_id = :groupId ");
		}
        sb.append(" GROUP BY ")
          .append(" 	m4.group_id ")
		  .append("    ,m4.device_id ")
		  .append("    ,m4.port_id ")
		  .append(" HAVING ")
		  .append(" 	count(distinct m4.mac_address) > 1 ");

		if (StringUtils.isNotBlank(udsVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       dl.group_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.device_name like :searchValue ")
			  .append("		  or ")
			  .append("       dpi.port_name like :searchValue ")
			  .append("     ) ");
		}
		String orderColumn = udsVO.getOrderColumn();
		String orderDirection = udsVO.getOrderDirection();
		sb.append(" ORDER BY ");
		
		switch (orderColumn) {
			case "Create_Time":
				sb.append(" m4.CREATE_TIME ").append(orderDirection).append(", dl.GROUP_NAME, dl.DEVICE_NAME, dpi.PORT_NAME");
				break;
				
			case "Group_Name":
				sb.append(" dl.GROUP_NAME ").append(orderDirection).append(", m4.CREATE_TIME desc, dl.DEVICE_NAME, dpi.PORT_NAME ");
				break;
				
			case "Device_Name":
				sb.append(" dl.GROUP_NAME, dl.DEVICE_NAME ").append(orderDirection).append(", m4.CREATE_TIME desc, dpi.PORT_NAME ");
				break;
				
			case "Port":
				sb.append(" dl.GROUP_NAME, dl.DEVICE_NAME, dpi.PORT_NAME ").append(orderDirection).append(", m4.CREATE_TIME desc ");
				break;
				
			default:
				sb.append(" dl.GROUP_NAME, m4.CREATE_TIME desc, dl.DEVICE_NAME, dpi.PORT_NAME");
				break;
		}
		
		Session session = secondSessionFactory.getCurrentSession();

        if (session.getTransaction().getStatus() == TransactionStatus.NOT_ACTIVE) {
            session.beginTransaction();
        }
        
		Query<?> q = session.createNativeQuery(sb.toString());
		if (StringUtils.isNotBlank(udsVO.getGroupId())) {
			q.setParameter("groupId", udsVO.getQueryGroup());
		}
		if (StringUtils.isNotBlank(udsVO.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(udsVO.getSearchValue()).concat("%"));
	    }
		if (startRow != null && pageLength != null) {
	    	q.setFirstResult(startRow);
		    q.setMaxResults(pageLength);
	    }
		return (List<Object[]>)q.list();
	}
}
