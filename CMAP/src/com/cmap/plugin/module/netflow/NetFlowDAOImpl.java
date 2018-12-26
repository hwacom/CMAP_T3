package com.cmap.plugin.module.netflow;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.impl.BaseDaoHibernate;

@Repository("netFlowDAO")
@Transactional
public class NetFlowDAOImpl extends BaseDaoHibernate implements NetFlowDAO {

	@Override
	public long countNetFlowData(NetFlowVO nfVO, List<String> searchLikeField, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(data_id) ")
		  .append(" from ").append(tableName).append(" nfrd ")
		  .append(" where 1=1 ");

		if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
			sb.append(" and nfrd.group_id = :groupId ");
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
			sb.append(" and nfrd.source_ip like :querySourceIp ");
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
			sb.append(" and nfrd.source_port = :querySourcePort ");
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
			sb.append(" and nfrd.destination_ip like :queryDestinationIp ");
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
			sb.append(" and nfrd.destination_port = :queryDestinationPort ");
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
			sb.append(" and nfrd.sender_ip like :querySenderIp ");
		}
		if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
			sb.append(" and ( nfrd.source_MAC like :queryMac ")
			  .append("       or nfrd.destination_MAC like :queryMac ) ");
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
			sb.append(" and ( (nfrd.now >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.now < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ")
			  .append("       or (nfrd.from_date_time >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.from_date_time < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ")
			  .append("       or (nfrd.to_date_time >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.to_date_time < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ) ");
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

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());

	    if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
	    	q.setParameter("groupId", nfVO.getQueryGroupId());
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
			q.setParameter("querySourceIp", nfVO.getQuerySourceIp().concat("%"));
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
			q.setParameter("querySourcePort", nfVO.getQuerySourcePort());
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
			q.setParameter("queryDestinationIp", nfVO.getQueryDestinationIp().concat("%"));
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
			q.setParameter("queryDestinationPort", nfVO.getQueryDestinationPort());
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
			q.setParameter("querySenderIp", nfVO.getQuerySenderIp().concat("%"));
		}
		if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
			q.setParameter("queryMac", nfVO.getQueryMac().concat("%"));
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
			q.setParameter("beginDate", nfVO.getQueryDateBegin());
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
	public List<Object[]> findNetFlowData(NetFlowVO nfVO, Integer startRow, Integer pageLength, List<String> searchLikeField, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select * ")
		  .append(" from ").append(tableName).append(" nfrd ")
		  .append(" where 1=1 ");

		if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
			sb.append(" and nfrd.group_id = :groupId ");
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
			sb.append(" and nfrd.source_ip like :querySourceIp ");
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
			sb.append(" and nfrd.source_port = :querySourcePort ");
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
			sb.append(" and nfrd.destination_ip like :queryDestinationIp ");
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
			sb.append(" and nfrd.destination_port = :queryDestinationPort ");
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
			sb.append(" and nfrd.sender_ip like :querySenderIp ");
		}
		if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
			sb.append(" and ( nfrd.source_MAC like :queryMac ")
			  .append("       or nfrd.destination_MAC like :queryMac ) ");
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
			sb.append(" and ( (nfrd.now >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.now < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ")
			  .append("       or (nfrd.from_date_time >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.from_date_time < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ")
			  .append("       or (nfrd.to_date_time >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and nfrd.to_date_time < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ) ");
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
			sb.append(" order by nfrd.to_date_time desc ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());

	    if (StringUtils.isNotBlank(nfVO.getQueryGroupId())) {
	    	q.setParameter("groupId", nfVO.getQueryGroupId());
		}
	    if (StringUtils.isNotBlank(nfVO.getQuerySourceIp())) {
			q.setParameter("querySourceIp", nfVO.getQuerySourceIp().concat("%"));
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySourcePort())) {
			q.setParameter("querySourcePort", nfVO.getQuerySourcePort());
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDestinationIp())) {
			q.setParameter("queryDestinationIp", nfVO.getQueryDestinationIp().concat("%"));
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDestinationPort())) {
			q.setParameter("queryDestinationPort", nfVO.getQueryDestinationPort());
		}
		if (StringUtils.isNotBlank(nfVO.getQuerySenderIp())) {
			q.setParameter("querySenderIp", nfVO.getQuerySenderIp().concat("%"));
		}
		if (StringUtils.isNotBlank(nfVO.getQueryMac())) {
			q.setParameter("queryMac", nfVO.getQueryMac().concat("%"));
		}
		if (StringUtils.isNotBlank(nfVO.getQueryDateBegin())) {
			q.setParameter("beginDate", nfVO.getQueryDateBegin());
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
}
