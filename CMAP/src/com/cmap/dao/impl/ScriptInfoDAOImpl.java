package com.cmap.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.Env;
import com.cmap.dao.ScriptInfoDAO;
import com.cmap.dao.vo.ScriptInfoDAOVO;
import com.cmap.exception.ServiceLayerException;
import com.cmap.model.ScriptInfo;

@Repository
@Transactional
public class ScriptInfoDAOImpl extends BaseDaoHibernate implements ScriptInfoDAO {

	@Override
	public long countScriptInfo(ScriptInfoDAOVO daovo) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(si.scriptInfoId) ")
		  .append(" from ScriptInfo si ")
		  .append(" where 1=1 ")
		  .append(" and si.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			sb.append(" and si.scriptType.scriptTypeId = :scriptTypeId ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeCode())) {
			sb.append(" and si.scriptType.scriptTypeCode = :scriptTypeCode ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptInfoId())) {
			sb.append(" and si.scriptInfoId = :scriptInfoId ");
		}
		if (StringUtils.isNotBlank(daovo.getQuerySystemDefault())) {
			sb.append(" and si.systemDefault = :systemDefault ");
		}
		if (daovo.isOnlySwitchPort()) {
			sb.append(" and si.scriptCode in (:scriptCode) ");
		}

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       si.scriptName like :searchValue ")
			  .append("       or ")
			  .append("       si.scriptType.scriptTypeName like :searchValue ")
			  .append("       or ")
			  .append("       si.systemVersion like :searchValue ")
			  .append("       or ")
			  .append("       si.actionScript like :searchValue ")
			  .append("       or ")
			  .append("       si.actionScriptRemark like :searchValue ")
			  .append("       or ")
			  .append("       si.checkScript like :searchValue ")
			  .append("       or ")
			  .append("       si.checkScriptRemark like :searchValue ")
			  .append("     ) ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			q.setParameter("scriptTypeId", daovo.getQueryScriptTypeId());
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeCode())) {
			q.setParameter("scriptTypeCode", daovo.getQueryScriptTypeCode());
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptInfoId())) {
			q.setParameter("scriptInfoId", daovo.getQueryScriptInfoId());
		}
		if (StringUtils.isNotBlank(daovo.getQuerySystemDefault())) {
			q.setParameter("systemDefault", daovo.getQuerySystemDefault());
		}
		if (daovo.isOnlySwitchPort()) {
			q.setParameterList("scriptCode", Env.DELIVERY_SWITCH_PORT_SCRIPT_CODE);
		}
		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(daovo.getSearchValue()).concat("%"));
	    }

		return DataAccessUtils.longResult(q.list());
	}

	@Override
	public List<ScriptInfo> findScriptInfo(ScriptInfoDAOVO daovo, Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ScriptInfo si ")
		  .append(" where 1=1 ")
		  .append(" and si.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			sb.append(" and si.scriptType.scriptTypeId = :scriptTypeId ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeCode())) {
			sb.append(" and si.scriptType.scriptTypeCode = :scriptTypeCode ");
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptInfoId())) {
			sb.append(" and si.scriptInfoId = :scriptInfoId ");
		}
		if (StringUtils.isNotBlank(daovo.getQuerySystemDefault())) {
			sb.append(" and si.systemDefault = :systemDefault ");
		}
		if (daovo.isOnlySwitchPort()) {
			sb.append(" and si.scriptCode in (:scriptCode) ");
		}

		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       si.scriptName like :searchValue ")
			  .append("       or ")
			  .append("       si.scriptType.scriptTypeName like :searchValue ")
			  .append("       or ")
			  .append("       si.systemVersion like :searchValue ")
			  .append("       or ")
			  .append("       si.actionScript like :searchValue ")
			  .append("       or ")
			  .append("       si.actionScriptRemark like :searchValue ")
			  .append("       or ")
			  .append("       si.checkScript like :searchValue ")
			  .append("       or ")
			  .append("       si.checkScriptRemark like :searchValue ")
			  .append("     ) ");
		}
		if (StringUtils.isNotBlank(daovo.getOrderColumn())) {
			sb.append(" order by ").append(daovo.getOrderColumn()).append(" ").append(daovo.getOrderDirection());

		} else {
			sb.append(" order by si.updateTime desc, si.scriptType.scriptTypeName asc ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeId())) {
			q.setParameter("scriptTypeId", daovo.getQueryScriptTypeId());
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptTypeCode())) {
			q.setParameter("scriptTypeCode", daovo.getQueryScriptTypeCode());
		}
		if (StringUtils.isNotBlank(daovo.getQueryScriptInfoId())) {
			q.setParameter("scriptInfoId", daovo.getQueryScriptInfoId());
		}
		if (StringUtils.isNotBlank(daovo.getQuerySystemDefault())) {
			q.setParameter("systemDefault", daovo.getQuerySystemDefault());
		}
		if (daovo.isOnlySwitchPort()) {
			q.setParameterList("scriptCode", Env.DELIVERY_SWITCH_PORT_SCRIPT_CODE);
		}
		if (StringUtils.isNotBlank(daovo.getSearchValue())) {
	    	q.setParameter("searchValue", "%".concat(daovo.getSearchValue()).concat("%"));
	    }
	    if (startRow != null && pageLength != null) {
	    	q.setFirstResult(startRow);
		    q.setMaxResults(pageLength);
	    }

		return (List<ScriptInfo>)q.list();
	}

	@Override
	public ScriptInfo findScriptInfoById(String scriptInfoId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ScriptInfo si ")
		  .append(" where 1=1 ")
		  .append(" and si.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and si.scriptInfoId = :scriptInfoId ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("scriptInfoId", scriptInfoId);

		List<ScriptInfo> retList = (List<ScriptInfo>)q.list();
		return (retList != null && !retList.isEmpty()) ? retList.get(0) : null;
	}

	@Override
	public ScriptInfo findDefaultScriptInfoByScriptTypeAndSystemVersion(String scriptType, String systemVersion) throws ServiceLayerException {
		StringBuffer sb = new StringBuffer();
		sb.append(" select si ")
		  .append(" from ScriptInfo si ")
		  .append("     ,ScriptDefaultMapping sdm ")
		  .append(" where 1=1 ")
		  .append(" and si.scriptCode = sdm.defaultScriptCode ")
		  .append(" and si.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and sdm.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		  .append(" and sdm.scriptType = :scriptType ")
		  .append(" and sdm.systemVersion = :systemVersion ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());
		q.setParameter("scriptType", scriptType);
		q.setParameter("systemVersion", systemVersion);

		List<ScriptInfo> retList = (List<ScriptInfo>)q.list();
		return (retList != null && !retList.isEmpty()) ? retList.get(0) : null;
	}
}
