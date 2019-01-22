package com.cmap.plugin.module.vmswitch;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.dao.impl.BaseDaoHibernate;

@Repository("vmSwitchDAOImpl")
@Transactional
public class VmSwitchDAOImpl extends BaseDaoHibernate implements VmSwitchDAO {

	@Override
	public ModuleVmNameMapping findVmNameMappingInfoByApiVmName(String apiVmName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select mvnm ")
		  .append(" from ModuleVmNameMapping mvnm ")
		  /*
		  .append("     ,ModuleVmNameMappingDetail mvnmd ")
		  .append("     ,ModuleVmEsxiSetting mves ")
		  */
		  .append(" where 1=1 ");
		  /*
		  .append(" and mvnm.mappingId = mvnmd.mappingId ")
		  .append(" and mvnmd.esxiSettingId = mves.settingId ");
		  */

		if (StringUtils.isNotBlank(apiVmName)) {
			sb.append(" and mvnm.nameOfApi = :apiVmName ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());

	    if (StringUtils.isNotBlank(apiVmName)) {
	    	q.setParameter("apiVmName", apiVmName);
		}

	    List<ModuleVmNameMapping> reList = (List<ModuleVmNameMapping>)q.list();

	    if (reList == null || (reList != null && reList.isEmpty())) {
	    	return null;

	    } else {
	    	return (ModuleVmNameMapping)q.list().get(0);
	    }
	}

	@Override
	public List<ModuleVmEsxiSetting> findAllVmEsxiSetting() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select mves ")
		  .append(" from ModuleVmEsxiSetting mves ")
		  .append(" where 1=1 ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());

		return (List<ModuleVmEsxiSetting>)q.list();
	}
}
