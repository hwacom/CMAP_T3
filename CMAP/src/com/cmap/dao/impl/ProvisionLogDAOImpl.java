package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.ProvisionLogDAO;
import com.cmap.dao.vo.ProvisionLogDAOVO;
import com.cmap.model.ProvisionAccessLog;
import com.cmap.model.ProvisionLogDetail;
import com.cmap.model.ProvisionLogDevice;
import com.cmap.model.ProvisionLogMaster;
import com.cmap.model.ProvisionLogRetry;
import com.cmap.model.ProvisionLogStep;

@Repository("provisionLogDAOImpl")
@Transactional
public class ProvisionLogDAOImpl extends BaseDaoHibernate implements ProvisionLogDAO {

	@Override
	public long countProvisionLogByDAOVO(ProvisionLogDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(distinct plm.log_master_id) ")
		  .append(" from Provision_Log_Master plm ")
		  .append("     ,Provision_Log_Detail pld ")
		  .append("     ,Provision_Log_Step pls ")
		  .append("     ,Provision_Log_Device pldc ")
		  .append("     ,Script_Info si ")
		  .append("     ,Device_List dl ");

		sb.append(" where 1=1 ")
		  .append(" and plm.log_master_id = pld.log_master_id ")
		  .append(" and pld.log_detail_id = pls.log_detail_id ")
		  .append(" and pls.log_step_id = pldc.log_step_id ")
		  .append(" and pls.script_code = si.script_code ")
		  .append(" and pldc.device_list_id = dl.device_list_id ");

		if (StringUtils.isNotBlank(daovo.getQueryGroupId())) {
			sb.append(" and dl.group_Id = :groupId ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryDeviceId())) {
			sb.append(" and dl.device_Id = :deviceId ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryBeginTimeStart())) {
			sb.append(" and plm.begin_time >= DATE_FORMAT(:beginDate, '%Y-%m-%d') ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryBeginTimeEnd())) {
			sb.append(" and plm.begin_time < DATE_ADD(:endDate, INTERVAL 1 DAY) ");
		}

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       plm.create_by like :searchValue ")
			  .append("       or ")
			  .append("       dl.group_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.device_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.system_version like :searchValue ")
			  .append("       or ")
			  .append("       si.script_name like :searchValue ")
			  .append("       or ")
			  .append("       plm.remark like :searchValue ")
			  .append("     ) ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createNativeQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getQueryGroupId())) {
			q.setParameter("groupId", daovo.getQueryGroupId());
		}
		if (StringUtils.isNotBlank(daovo.getQueryDeviceId())) {
			q.setParameter("deviceId", daovo.getQueryDeviceId());
		}
		if (StringUtils.isNotBlank(daovo.getQueryBeginTimeStart())) {
			q.setParameter("beginDate", daovo.getQueryBeginTimeStart());
		}
		if (StringUtils.isNotBlank(daovo.getQueryBeginTimeEnd())) {
			q.setParameter("endDate", daovo.getQueryBeginTimeEnd());
		}
		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(daovo.getSearchValue()).concat("%"));
	    }

		return DataAccessUtils.longResult(q.list());
	}

	@Override
	public List<Object[]> findProvisionLogByDAOVO(ProvisionLogDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct")
		  .append("   plm.log_master_id, pld.log_detail_id, pls.log_step_id, pldc.log_device_id, ") //index: 0~3
		  .append("   plm.begin_time, ")		//index: 4
		  .append("   plm.create_by, ")			//index: 5
		  .append("   dl.group_name, ")			//index: 6
		  .append("   dl.device_name, ")		//index: 7
		  .append("   dl.system_version, ")		//index: 8
		  .append("   si.script_name, ")		//index: 9
		  .append("   plm.remark, ")			//index: 10
		  .append("   pls.result, ")			//index: 11
		  .append("   pls.process_log ")		//index: 12
		  .append(" from Provision_Log_Master plm ")
		  .append("     ,Provision_Log_Detail pld ")
		  .append("     ,Provision_Log_Step pls ")
		  .append("     ,Provision_Log_Device pldc ")
		  .append("     ,Script_Info si ")
		  .append("     ,Device_List dl ");

		sb.append(" where 1=1 ")
		  .append(" and plm.log_master_id = pld.log_master_id ")
		  .append(" and pld.log_detail_id = pls.log_detail_id ")
		  .append(" and pls.log_step_id = pldc.log_step_id ")
		  .append(" and pls.script_code = si.script_code ")
		  .append(" and pldc.device_list_id = dl.device_list_id ");

		if (StringUtils.isNotBlank(daovo.getQueryGroupId())) {
			sb.append(" and dl.group_Id = :groupId ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryDeviceId())) {
			sb.append(" and dl.device_Id = :deviceId ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryBeginTimeStart())) {
			sb.append(" and plm.begin_time >= DATE_FORMAT(:beginDate, '%Y-%m-%d') ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryBeginTimeEnd())) {
			sb.append(" and plm.begin_time < DATE_ADD(:endDate, INTERVAL 1 DAY) ");
		}

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       plm.create_by like :searchValue ")
			  .append("       or ")
			  .append("       dl.group_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.device_name like :searchValue ")
			  .append("       or ")
			  .append("       dl.system_version like :searchValue ")
			  .append("       or ")
			  .append("       si.script_name like :searchValue ")
			  .append("       or ")
			  .append("       plm.remark like :searchValue ")
			  .append("     ) ");
		}

		if (StringUtils.isNotBlank(daovo.getOrderColumn())) {
			sb.append(" order by ").append(daovo.getOrderColumn()).append(" ").append(daovo.getOrderDirection());

		} else {
			sb.append(" order by plm.begin_time desc, dl.group_name asc, dl.device_name asc ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createNativeQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getQueryGroupId())) {
			q.setParameter("groupId", daovo.getQueryGroupId());
		}
		if (StringUtils.isNotBlank(daovo.getQueryDeviceId())) {
			q.setParameter("deviceId", daovo.getQueryDeviceId());
		}
		if (StringUtils.isNotBlank(daovo.getQueryBeginTimeStart())) {
			q.setParameter("beginDate", daovo.getQueryBeginTimeStart());
		}
		if (StringUtils.isNotBlank(daovo.getQueryBeginTimeEnd())) {
			q.setParameter("endDate", daovo.getQueryBeginTimeEnd());
		}
		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(daovo.getSearchValue()).concat("%"));
	    }

		return (List<Object[]>)q.list();
	}

	@Override
	public void insertProvisionLog(ProvisionLogMaster master, List<ProvisionLogDetail> details, List<ProvisionLogStep> steps,
			List<ProvisionLogDevice> devices, List<ProvisionLogRetry> retrys) {

		if (master != null) {
			getHibernateTemplate().save(master);
		}

		if (details != null) {
			for (ProvisionLogDetail detail : details) {
				getHibernateTemplate().save(detail);
			}
		}

		if (steps != null) {
			for (ProvisionLogStep step : steps) {
				getHibernateTemplate().save(step);
			}
		}

		if (devices != null) {
			for (ProvisionLogDevice device : devices) {
				getHibernateTemplate().save(device);
			}
		}

		if (retrys != null) {
			for (ProvisionLogRetry retry : retrys) {
				getHibernateTemplate().save(retry);
			}
		}
	}

	@Override
	public ProvisionAccessLog findProvisionAccessLogById(String logId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ProvisionAccessLog ")
		  .append(" where 1=1 ")
		  .append(" and logId = :logId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("logId", logId);

		List<ProvisionAccessLog> entities = (List<ProvisionAccessLog>)q.list();
		return (entities != null && !entities.isEmpty()) ? entities.get(0) : null;
	}

	@Override
	public ProvisionLogStep findProvisionLogStepById(String logStepId) {
		return getHibernateTemplate().getSessionFactory().getCurrentSession().get(ProvisionLogStep.class, logStepId);
	}
}
