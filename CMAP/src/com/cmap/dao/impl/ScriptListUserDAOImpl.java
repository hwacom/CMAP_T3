package com.cmap.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.comm.enums.ScriptType;
import com.cmap.dao.ScriptListDAO;
import com.cmap.dao.vo.ScriptListDAOVO;

@Repository
@Transactional
public class ScriptListUserDAOImpl extends BaseDaoHibernate implements ScriptListDAO {

	private List<ScriptListDAOVO> transScriptListUser2DAOVO(List<Object[]> modelList) {
		List<ScriptListDAOVO> retList = new ArrayList<ScriptListDAOVO>();

		/*
		ScriptListDAOVO daoVO;
		for (Object[] obj : modelList) {
			ScriptListUser scriptListUser = (ScriptListUser)obj[0];
			ScriptUserMapping scriptUserMapping = (ScriptUserMapping)obj[1];

			daoVO = new ScriptListDAOVO();
			BeanUtils.copyProperties(scriptListUser, daoVO);
			daoVO.setScriptTypeId(scriptListUser.getScriptType().getScriptTypeId());
			daoVO.setScriptTypeName(scriptListUser.getScriptType().getScriptTypeName());
			daoVO.setScriptMode(scriptListUser.getScriptMode());
			daoVO.setSystemVersion(scriptUserMapping.getSystemVersion());
			daoVO.setScriptStepOrder(String.valueOf(scriptListUser.getScriptStepOrder()));
			daoVO.setHeadCuttingLines(scriptListUser.getHeadCuttingLines() != null ? String.valueOf(scriptListUser.getHeadCuttingLines()) : null);
			daoVO.setTailCuttingLines(scriptListUser.getTailCuttingLines() != null ? String.valueOf(scriptListUser.getTailCuttingLines()) : null);
			daoVO.setCreateTimeStr(Constants.FORMAT_YYYYMMDD_HH24MISS.format(scriptListUser.getCreateTime()));
			daoVO.setUpdateTimeStr(Constants.FORMAT_YYYYMMDD_HH24MISS.format(scriptListUser.getUpdateTime()));

			retList.add(daoVO);
		}
		 */

		return retList;
	}

	@Override
	public List<ScriptListDAOVO> findScriptListByScriptCode(String scriptCode) {
		/*
		StringBuffer sb = new StringBuffer();
		sb.append(" select slu, sum ")
		.append(" from ScriptListUser slu ")
		.append("     ,ScriptUserMapping sum ")
		.append(" where 1=1 ")
		.append(" and slu.scriptCode = sum.scriptCode ")
		.append(" and slu.scriptType.scriptTypeId = sum.scriptType.scriptTypeId ")
		.append(" and slu.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		.append(" and sum.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		.append(" and slu.scriptType.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(slDAOVO.getScriptListId())) {
			sb.append(" and slu.scriptType.scriptTypeId = :scriptTypeId ");
		}
		if (StringUtils.isNotBlank(slDAOVO.getScriptCode())) {
			sb.append(" and slu.scriptCode = :scriptCode ");
		}
		if (StringUtils.isNotBlank(slDAOVO.getSearchValue())) {
			sb.append(" and ( ")
			.append("   ( slu.scriptName like ':searchValue') OR ")
			.append("   ( slu.scriptType.scriptTypeName like ':searchValue') OR ")
			.append("   ( slu.scriptUserMapping.systemVersion like ':searchValue') ")
			.append(" ) ");
		}
		sb.append(" order by slu.scriptType.scriptTypeName, slu.scriptName, slu.scriptStepOrder asc ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createQuery(sb.toString());

		if (StringUtils.isNotBlank(slDAOVO.getScriptListId())) {
			q.setParameter("scriptTypeId", slDAOVO.getScriptListId());
		}
		if (StringUtils.isNotBlank(slDAOVO.getScriptCode())) {
			q.setParameter("scriptCode", slDAOVO.getScriptCode());
		}
		if (StringUtils.isNotBlank(slDAOVO.getSearchValue())) {
			q.setParameter("searchValue", "%".concat(slDAOVO.getSearchValue()).concat("%"));
		}

		return transScriptListUser2DAOVO((List<Object[]>)q.list());
		 */
		return null;
	}

	@Override
	public String findDefaultScriptCodeBySystemVersion(ScriptType scriptType, String systemVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countScriptList(ScriptListDAOVO slDAOVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(distinct slu.scriptCode) ")
		.append(" from ScriptListUser slu ")
		.append(" where 1=1 ")
		.append(" and slu.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
		.append(" and slu.scriptType.deleteFlag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(slDAOVO.getScriptListId())) {
			sb.append(" and slu.scriptType.scriptTypeId = :scriptTypeId ");
		}
		if (StringUtils.isNotBlank(slDAOVO.getScriptCode())) {
			sb.append(" and slu.scriptCode = :scriptCode ");
		}
		if (StringUtils.isNotBlank(slDAOVO.getSearchValue())) {
			sb.append(" and ( ")
			.append("   ( slu.scriptName like ':searchValue') OR ")
			.append("   ( slu.scriptType.scriptTypeName like ':searchValue') OR ")
			.append("   ( slu.scriptUserMapping.systemVersion like ':searchValue') ")
			.append(" ) ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query<?> q = session.createNativeQuery(sb.toString());

		if (StringUtils.isNotBlank(slDAOVO.getScriptListId())) {
			q.setParameter("scriptTypeId", slDAOVO.getScriptListId());
		}
		if (StringUtils.isNotBlank(slDAOVO.getScriptCode())) {
			q.setParameter("scriptCode", slDAOVO.getScriptCode());
		}
		if (StringUtils.isNotBlank(slDAOVO.getSearchValue())) {
			q.setParameter("searchValue", "%".concat(slDAOVO.getSearchValue()).concat("%"));
		}

		return DataAccessUtils.longResult(q.list());
	}

}
