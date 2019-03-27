package com.cmap.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmap.Constants;
import com.cmap.dao.ConfigDAO;
import com.cmap.dao.vo.ConfigVersionInfoDAOVO;
import com.cmap.model.ConfigContentSetting;
import com.cmap.model.ConfigVersionInfo;

@Repository
@Transactional
public class ConfigDAOImpl extends BaseDaoHibernate implements ConfigDAO {

	@Override
	public long countConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(distinct cvi.version_id) from ( ")
		  .append("   select cvi_1.* ")
		  .append("   from Config_Version_Info cvi_1 ")
		  .append("   where 1=1 ")
		  .append("   and cvi_1.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

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

		if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
			sb.append(" and cvi_1.config_Type = :configType ");
		}

		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
			sb.append(" and cvi_1.create_Time >= DATE_FORMAT(:beginDate_1, '%Y-%m-%d') ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
			sb.append(" and cvi_1.create_Time < DATE_ADD(:endDate_1, INTERVAL 1 DAY) ");
		}

		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
			sb.append(" union ( ")
			  .append("   select cvi_2.* from Config_Version_Info cvi_2 ")
			  .append("   where 1=1 ")
			  .append("   and cvi_2.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
			  .append("   and cvi_2.group_Id = :groupId_2 ");

			if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice2())) {
				sb.append(" and cvi_2.device_Id = :deviceId_2 ");
			} else {
				sb.append(" and cvi_2.device_Id in (:deviceId_2) ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
				sb.append(" and cvi_2.config_Type = :configType ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin2())) {
				sb.append(" and cvi_2.create_Time >= DATE_FORMAT(:beginDate_2, '%Y-%m-%d') ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd2())) {
				sb.append(" and cvi_2.create_Time < DATE_ADD(:endDate_2, INTERVAL 1 DAY) ");
			}
			sb.append(" ) ");
		}
		sb.append(" ) cvi ")
		  .append("  ,Device_List dl ")
		  .append("  where 1 = 1 ")
		  .append("  and cvi.group_id = dl.group_id ")
		  .append("  and cvi.device_id = dl.device_id ")
		  .append("  and dl.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(cviDAOVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       cvi.group_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.device_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.system_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_version like :searchValue ")
			  .append("		  or ")
			  .append("       cvi.config_type like :searchValue ")
			  .append("     ) ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());

	    q.setParameter("groupId_1", StringUtils.isNotBlank(cviDAOVO.getQueryGroup1()) ? cviDAOVO.getQueryGroup1() : cviDAOVO.getQueryGroup1List());
	    q.setParameter("deviceId_1", StringUtils.isNotBlank(cviDAOVO.getQueryDevice1()) ? cviDAOVO.getQueryDevice1() : cviDAOVO.getQueryDevice1List());

	    if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
	    	q.setParameter("configType", cviDAOVO.getQueryConfigType());
		}

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

	@Override
	public long countConfigVersionInfoByDAOVO4New(ConfigVersionInfoDAOVO cviDAOVO) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(distinct cvi.version_id) from ( ")
		  .append("   select cvi_1.* ")
		  .append("   from Config_Version_Info cvi_1 ")
		  .append("   where 1 = 1 ")
		  .append("   and (cvi_1.config_version, cvi_1.group_id, cvi_1.device_id, cvi_1.config_Type) ")
		  .append("   in ( ")
		  .append("         select max(cvi_11.config_version), cvi_11.group_id, cvi_11.device_id, cvi_11.config_Type ")
		  .append("         from Config_Version_Info cvi_11 ")
		  .append("         where 1 = 1 ")
		  .append("         and (cvi_11.create_time, cvi_11.group_id, cvi_11.device_id, cvi_11.config_Type) ")
		  .append("         in ( ")
		  .append("               select max(cvi_12.create_time), cvi_12.group_id, cvi_12.device_id, cvi_12.config_Type ")
		  .append("               from Config_Version_Info cvi_12 ")
		  .append("               where 1 = 1 ")
		  .append("               and cvi_12.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup1())) {
			sb.append("           and cvi_12.group_Id = :groupId_1 ");
		} else {
			sb.append("           and cvi_12.group_Id in (:groupId_1) ");
		}

		if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice1())) {
			sb.append("           and cvi_12.device_Id = :deviceId_1 ");
		} else {
			sb.append("           and cvi_12.device_Id in (:deviceId_1) ");
		}

		if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
			sb.append(" 		  and cvi_12.config_Type = :configType ");
		}

		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
			sb.append("           and cvi_12.create_Time >= DATE_FORMAT(:beginDate_1, '%Y-%m-%d') ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
			sb.append("           and cvi_12.create_Time < DATE_ADD(:endDate_1, INTERVAL 1 DAY) ");
		}

		sb.append("               group by cvi_12.group_id, cvi_12.device_id, cvi_12.config_Type ")
		  .append("         ) ")
		  .append("         group by cvi_11.config_version, cvi_11.group_id, cvi_11.device_id, cvi_11.config_Type ")
		  .append("    ) ");

		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
			sb.append(" union ( ")
			  .append("   select cvi_2.* from Config_Version_Info cvi_2 ")
			  .append("   where 1 = 1 ")
			  .append("   and (cvi_2.config_version, cvi_2.group_id, cvi_2.device_id, cvi_2.config_Type) ")
			  .append("   in ( ")
			  .append("         select max(cvi_21.config_version), cvi_21.group_id, cvi_21.device_id, cvi_21.config_Type ")
			  .append("         from Config_Version_Info cvi_21 ")
			  .append("         where 1 = 1 ")
			  .append("         and (cvi_21.create_time, cvi_21.group_id, cvi_21.device_id, cvi_21.config_Type) ")
			  .append("         in ( ")
			  .append("               select max(cvi_22.create_time), cvi_22.group_id, cvi_22.device_id, cvi_22.config_Type ")
			  .append("               from Config_Version_Info cvi_22 ")
			  .append("               where 1 = 1 ")
			  .append("               and cvi_22.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
			  .append("               and cvi_22.group_Id = :groupId_2 ");

			if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice2())) {
				sb.append("           and cvi_22.device_Id = :deviceId_2 ");
			} else {
				sb.append("           and cvi_22.device_Id in (:deviceId_2) ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
				sb.append(" 		  and cvi_22.config_Type = :configType ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin2())) {
				sb.append("           and cvi_22.create_Time >= DATE_FORMAT(:beginDate_2, '%Y-%m-%d') ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd2())) {
				sb.append("           and cvi_22.create_Time < DATE_ADD(:endDate_2, INTERVAL 1 DAY) ");
			}
			sb.append("               group by cvi_22.group_id, cvi_22.device_id, cvi_22.config_Type ")
			  .append("         ) ")
			  .append("         group by cvi_21.config_version, cvi_21.group_id, cvi_21.device_id, cvi_21.config_Type ")
			  .append("    ) ")
			  .append(" ) ");
		}
		sb.append(" ) cvi ")
		  .append("  ,Device_List dl ")
		  .append("  where 1 = 1 ")
		  .append("  and cvi.group_id = dl.group_id ")
		  .append("  and cvi.device_id = dl.device_id ")
		  .append("  and dl.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(cviDAOVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       cvi.group_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.device_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.system_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_type like :searchValue ")
			  .append("     ) ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());

	    q.setParameter("groupId_1", StringUtils.isNotBlank(cviDAOVO.getQueryGroup1()) ? cviDAOVO.getQueryGroup1() : cviDAOVO.getQueryGroup1List());
	    q.setParameter("deviceId_1", StringUtils.isNotBlank(cviDAOVO.getQueryDevice1()) ? cviDAOVO.getQueryDevice1() : cviDAOVO.getQueryDevice1List());

	    if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
	    	q.setParameter("configType", cviDAOVO.getQueryConfigType());
		}

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

	@Override
	public List<Object[]> findConfigVersionInfoByDAOVO(ConfigVersionInfoDAOVO cviDAOVO, Integer startRow, Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct ")
		  .append(composeSelectStr(Constants.NATIVE_FIELD_NAME_FOR_VERSION, "cvi"))
		  .append(", ")
		  .append(composeSelectStr(Constants.NATIVE_FIELD_NAME_FOR_DEVICE, "dl"))
		  .append(" from ( ")
		  .append("   select cvi_1.* ")
		  .append("   from Config_Version_Info cvi_1 ")
		  .append("   where 1=1 ")
		  .append("   and cvi_1.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		/*
	     * 每日排程異地備份所有組態檔，不帶以下條件
	     */
		if (!cviDAOVO.isJobTrigger()) {
			if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup1())) {
				sb.append(" and cvi_1.group_Id = :groupId_1 ");
			} else if (cviDAOVO.getQueryGroup1List() != null && !cviDAOVO.getQueryGroup1List().isEmpty()) {
				sb.append(" and cvi_1.group_Id in (:groupId_1) ");
			}

			if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice1())) {
				sb.append(" and cvi_1.device_Id = :deviceId_1 ");
			} else if (cviDAOVO.getQueryDevice1List() != null && !cviDAOVO.getQueryDevice1List().isEmpty()) {
				sb.append(" and cvi_1.device_Id in (:deviceId_1) ");
			}
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryVersionId())) {
			sb.append(" and cvi_1.version_id = :versionId ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
			sb.append(" and cvi_1.config_Type = :configType ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
			sb.append(" and cvi_1.create_Time >= DATE_FORMAT(:beginDate_1, '%Y-%m-%d') ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
			sb.append(" and cvi_1.create_Time < DATE_ADD(:endDate_1, INTERVAL 1 DAY) ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
			sb.append(" union ( ")
			  .append("   select cvi_2.* ")
			  .append("   from Config_Version_Info cvi_2 ")
			  .append("   where 1=1 ")
			  .append("   and cvi_2.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
			  .append("   and cvi_2.group_id = :groupId_2 ");

			if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice2())) {
				sb.append(" and cvi_2.device_Id = :deviceId_2 ");
			} else if (cviDAOVO.getQueryDevice2List() != null && !cviDAOVO.getQueryDevice2List().isEmpty()) {
				sb.append(" and cvi_2.device_Id in (:deviceId_2) ");
			}

			if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
				sb.append(" and cvi_2.config_Type = :configType ");
			}

			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin2())) {
				sb.append(" and cvi_2.create_Time >= DATE_FORMAT(:beginDate_2, '%Y-%m-%d') ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd2())) {
				sb.append(" and cvi_2.create_Time < DATE_ADD(:endDate_2, INTERVAL 1 DAY) ");
			}
			sb.append(" ) ");
		}
		sb.append(" ) cvi ")
		  .append("  ,Device_List dl ")
		  .append("  where 1 = 1 ")
		  .append("  and cvi.group_id = dl.group_id ")
		  .append("  and cvi.device_id = dl.device_id ")
		  .append("  and dl.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(cviDAOVO.getQueryDeviceListId())) {
			sb.append(" and dl.device_list_id = :deviceListId ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       cvi.group_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.device_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.system_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_type like :searchValue ")
			  .append("     ) ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getOrderColumn())) {
			sb.append(" order by cvi.").append(cviDAOVO.getOrderColumn()).append(" ").append(cviDAOVO.getOrderDirection());

		} else {
			sb.append(" order by cvi.create_Time desc, cvi.group_Name asc, cvi.device_Name asc ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());

	    /*
	     * 每日排程異地備份所有組態檔，不帶以下條件
	     */
	    if (!cviDAOVO.isJobTrigger()) {
	    	if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup1()) || (cviDAOVO.getQueryGroup1List() != null && !cviDAOVO.getQueryGroup1List().isEmpty())) {
	    		q.setParameter("groupId_1", StringUtils.isNotBlank(cviDAOVO.getQueryGroup1()) ? cviDAOVO.getQueryGroup1() : cviDAOVO.getQueryGroup1List());
	    	}
	    	if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice1()) || (cviDAOVO.getQueryDevice1List() != null && !cviDAOVO.getQueryDevice1List().isEmpty())) {
	    		q.setParameter("deviceId_1", StringUtils.isNotBlank(cviDAOVO.getQueryDevice1()) ? cviDAOVO.getQueryDevice1() : cviDAOVO.getQueryDevice1List());
	    	}
	    }
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryVersionId())) {
			q.setParameter("versionId", cviDAOVO.getQueryVersionId());
		}
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryDeviceListId())) {
	    	q.setParameter("deviceListId", cviDAOVO.getQueryDeviceListId());
		}
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
	    	q.setParameter("configType", cviDAOVO.getQueryConfigType());
		}
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
	    	q.setParameter("beginDate_1", cviDAOVO.getQueryDateBegin1());
	    }
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
	    	q.setParameter("endDate_1", cviDAOVO.getQueryDateEnd1());
	    }
	    if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
	    	q.setParameter("groupId_2", cviDAOVO.getQueryGroup2());
	    	if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice2()) || (cviDAOVO.getQueryDevice2List() != null && !cviDAOVO.getQueryDevice2List().isEmpty())) {
	    		q.setParameter("deviceId_2", StringUtils.isNotBlank(cviDAOVO.getQueryDevice2()) ? cviDAOVO.getQueryDevice2() : cviDAOVO.getQueryDevice2List());
	    	}
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

		return transObjList2ModelList4Version(retList);
	}

	@Override
	public List<Object[]> findConfigVersionInfoByDAOVO4New(ConfigVersionInfoDAOVO cviDAOVO, Integer startRow,
			Integer pageLength) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct ")
		  .append(composeSelectStr(Constants.NATIVE_FIELD_NAME_FOR_VERSION, "cvi"))
		  .append(", ")
		  .append(composeSelectStr(Constants.NATIVE_FIELD_NAME_FOR_DEVICE, "dl"))
		  .append(" from ( ")
		  .append("   select cvi_1.* ")
		  .append("   from Config_Version_Info cvi_1 ")
		  .append("   where 1 = 1 ")
		  .append("   and (cvi_1.config_version, cvi_1.group_id, cvi_1.device_id, cvi_1.config_Type) ")
		  .append("   in ( ")
		  .append("         select max(cvi_11.config_version), cvi_11.group_id, cvi_11.device_id, cvi_11.config_Type ")
		  .append("         from Config_Version_Info cvi_11 ")
		  .append("         where 1 = 1 ")
		  .append("         and (cvi_11.create_time, cvi_11.group_id, cvi_11.device_id, cvi_11.config_Type) ")
		  .append("         in ( ")
		  .append("               select max(cvi_12.create_time), cvi_12.group_id, cvi_12.device_id, cvi_12.config_Type ")
		  .append("               from Config_Version_Info cvi_12 ")
		  .append("               where 1 = 1 ")
		  .append("               and cvi_12.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		/*
	     * 每日排程異地備份所有組態檔，不帶以下條件
	     */
		if (!cviDAOVO.isJobTrigger()) {
			if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup1())) {
				sb.append("           and cvi_12.group_Id = :groupId_1 ");
			} else {
				sb.append("           and cvi_12.group_Id in (:groupId_1) ");
			}

			if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice1())) {
				sb.append("           and cvi_12.device_Id = :deviceId_1 ");
			} else {
				sb.append("           and cvi_12.device_Id in (:deviceId_1) ");
			}
		}

		if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
			sb.append(" 		  and cvi_12.config_Type = :configType ");
		}

		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin1())) {
			sb.append("           and cvi_12.create_Time >= DATE_FORMAT(:beginDate_1, '%Y-%m-%d') ");
		}
		if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd1())) {
			sb.append("           and cvi_12.create_Time < DATE_ADD(:endDate_1, INTERVAL 1 DAY) ");
		}

		sb.append("               group by cvi_12.group_id, cvi_12.device_id, cvi_12.config_Type ")
		  .append("         ) ")
		  .append("         group by cvi_11.config_version, cvi_11.group_id, cvi_11.device_id, cvi_11.config_Type ")
		  .append("    ) ");

		if (StringUtils.isNotBlank(cviDAOVO.getQueryGroup2())) {
			sb.append(" union ( ")
			  .append("   select cvi_2.* ")
			  .append("   from Config_Version_Info cvi_2 ")
			  .append("   where 1 = 1 ")
			  .append("   and (cvi_2.config_version, cvi_2.group_id, cvi_2.device_id, cvi_2.config_Type) ")
			  .append("   in ( ")
			  .append("         select max(cvi_21.config_version), cvi_21.group_id, cvi_21.device_id, cvi_21.config_Type ")
			  .append("         from Config_Version_Info cvi_21 ")
			  .append("         where 1 = 1 ")
			  .append("         and (cvi_21.create_time, cvi_21.group_id, cvi_21.device_id, cvi_21.config_Type) ")
			  .append("         in ( ")
			  .append("               select max(cvi_22.create_time), cvi_22.group_id, cvi_22.device_id, cvi_22.config_Type ")
			  .append("               from Config_Version_Info cvi_22 ")
			  .append("               where 1 = 1 ")
			  .append("               and cvi_22.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ")
			  .append("               and cvi_22.group_Id = :groupId_2 ");

			if (StringUtils.isNotBlank(cviDAOVO.getQueryDevice2())) {
				sb.append("           and cvi_22.device_Id = :deviceId_2 ");
			} else {
				sb.append("           and cvi_22.device_Id in (:deviceId_2) ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
				sb.append(" 		  and cvi_22.config_Type = :configType ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateBegin2())) {
				sb.append("           and cvi_22.create_Time >= DATE_FORMAT(:beginDate_2, '%Y-%m-%d') ");
			}
			if (StringUtils.isNotBlank(cviDAOVO.getQueryDateEnd2())) {
				sb.append("           and cvi_22.create_Time < DATE_ADD(:endDate_2, INTERVAL 1 DAY) ");
			}
			sb.append("               group by cvi_22.group_id, cvi_22.device_id, cvi_22.config_Type ")
			  .append("         ) ")
			  .append("         group by cvi_21.config_version, cvi_21.group_id, cvi_21.device_id, cvi_21.config_Type ")
			  .append("    ) ")
			  .append(" ) ");
		}
		sb.append(" ) cvi ")
		  .append("  ,Device_List dl ")
		  .append("  where 1 = 1 ")
		  .append("  and cvi.group_id = dl.group_id ")
		  .append("  and cvi.device_id = dl.device_id ")
		  .append("  and dl.delete_flag = '").append(Constants.DATA_MARK_NOT_DELETE).append("' ");

		if (StringUtils.isNotBlank(cviDAOVO.getSearchValue())) {
			sb.append(" and ( ")
			  .append("       cvi.group_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.device_name like :searchValue ")
			  .append("       or ")
			  .append("       cvi.system_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_version like :searchValue ")
			  .append("       or ")
			  .append("       cvi.config_type like :searchValue ")
			  .append("     ) ");
		}

		if (StringUtils.isNotBlank(cviDAOVO.getOrderColumn())) {
			sb.append(" order by cvi.").append(cviDAOVO.getOrderColumn()).append(" ").append(cviDAOVO.getOrderDirection());

		} else {
			sb.append(" order by cvi.create_Time desc, cvi.group_Name asc, cvi.device_Name asc, cvi.config_Type asc ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createNativeQuery(sb.toString());

	    /*
	     * 每日排程異地備份所有組態檔，不帶以下條件
	     */
		if (!cviDAOVO.isJobTrigger()) {
			q.setParameter("groupId_1", StringUtils.isNotBlank(cviDAOVO.getQueryGroup1()) ? cviDAOVO.getQueryGroup1() : cviDAOVO.getQueryGroup1List());
		    q.setParameter("deviceId_1", StringUtils.isNotBlank(cviDAOVO.getQueryDevice1()) ? cviDAOVO.getQueryDevice1() : cviDAOVO.getQueryDevice1List());
		}

	    if (StringUtils.isNotBlank(cviDAOVO.getQueryConfigType())) {
	    	q.setParameter("configType", cviDAOVO.getQueryConfigType());
	    }
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

		return transObjList2ModelList4Version(retList);
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

	@Override
	public List<ConfigVersionInfo> findConfigVersionInfoByVersionIDs(List<String> versionIDs) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select cvi ")
		  .append(" from ConfigVersionInfo cvi ")
		  .append(" where 1=1 ")
		  .append(" and cvi.versionId in (:versionId) ");

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    q.setParameter("versionId", versionIDs);

		return (List<ConfigVersionInfo>)q.list();
	}

	@Override
	public void insertConfigVersionInfo(ConfigVersionInfo configVersionInfo) {
		getHibernateTemplate().save(configVersionInfo);
	}

	@Override
	public List<ConfigContentSetting> findConfigContentSetting(String settingType, String systemVersion, String deviceNameLike, String deviceListId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from ConfigContentSetting ccs ")
		  .append(" where 1=1 ");

		if (StringUtils.isNotBlank(settingType)) {
			sb.append(" and settingType = :settingType ");
		}
		if (StringUtils.isNotBlank(systemVersion)) {
			sb.append(" and systemVersion = :systemVersion ");
		}
		if (StringUtils.isNotBlank(deviceNameLike)) {
			sb.append(" and deviceNameLike like :deviceNameLike ");
		}
		if (StringUtils.isNotBlank(deviceListId)) {
			sb.append(" and deviceListId = :deviceListId ");
		}

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
	    Query<?> q = session.createQuery(sb.toString());
	    if (StringUtils.isNotBlank(settingType)) {
			q.setParameter("settingType", settingType);
		}
		if (StringUtils.isNotBlank(systemVersion)) {
			q.setParameter("systemVersion", systemVersion);
		}
		if (StringUtils.isNotBlank(deviceNameLike)) {
			q.setParameter("deviceNameLike", deviceNameLike);
		}
		if (StringUtils.isNotBlank(deviceListId)) {
			q.setParameter("deviceListId", deviceListId);
		}

		return (List<ConfigContentSetting>)q.list();
	}
}
