package com.cmap.plugin.module.firewall;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.Constants;
import com.cmap.dao.impl.BaseDaoHibernate;

@Repository("firewallDAO")
@Transactional
public class FirewallDAOImpl extends BaseDaoHibernate implements FirewallDAO {

    @Override
    public List<ModuleFirewallLogSetting> getFirewallLogSetting(String settingName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mfls ")
          .append(" from ModuleFirewallLogSetting mfls ")
          .append(" where 1=1 ")
          .append(" and mfls.settingName = :settingName ")
          .append(" order by mfls.orderNo asc ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("settingName", settingName);

        return (List<ModuleFirewallLogSetting>)q.list();
    }

    @Override
    public long countFirewallLogFromDB(FirewallVO fVO, List<String> searchLikeField,
            String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(mfl.id) ")
          .append(" from ").append(tableName).append(" mfl ")
          .append(" where 1=1 ");

        if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
            sb.append(" and mfl.dev_name = :devName ");
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
            sb.append(" and mfl.src_ip = :querySrcIp ");
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
            sb.append(" and mfl.src_port = :querySrcPort ");
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
            sb.append(" and mfl.dst_ip = :queryDstIp ");
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
            sb.append(" and mfl.dst_port = :queryDstPort ");
        }
        /*
        if (StringUtils.isNotBlank(fVO.getQueryDateBegin())) {
            sb.append(" and (mfl.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and mfl.date < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ");
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
            sb.append(" and (mfl.time >= :beginTime and mfl.time < :endTime) ");
        }
        */
        if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
            sb.append(" and (mfl.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and mfl.time >= :beginTime) ");
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
            sb.append(" and (mfl.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and mfl.time < :endTime) ");
        }

        if (StringUtils.isNotBlank(fVO.getSearchValue())) {
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

        if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
            q.setParameter("devName", fVO.getQueryDevName());
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
            q.setParameter("querySrcIp", fVO.getQuerySrcIp());
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
            q.setParameter("querySrcPort", fVO.getQuerySrcPort());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
            q.setParameter("queryDstIp", fVO.getQueryDstIp());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
            q.setParameter("queryDstPort", fVO.getQueryDstPort());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateBegin())) {
            q.setParameter("beginDate", fVO.getQueryDateBegin());
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
            q.setParameter("beginTime", fVO.getQueryTimeBegin());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateEnd())) {
            q.setParameter("endDate", fVO.getQueryDateEnd());
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
            q.setParameter("endTime", fVO.getQueryTimeEnd());
        }
        if (StringUtils.isNotBlank(fVO.getSearchValue())) {
            q.setParameter("searchValue", "%".concat(fVO.getSearchValue()).concat("%"));
        }

        return DataAccessUtils.longResult(q.list());
    }

    @Override
    public List<Object[]> findFirewallLogFromDB(FirewallVO fVO, Integer startRow,
            Integer pageLength, List<String> searchLikeField, String tableName, String selectSql) {
        StringBuffer sb = new StringBuffer();

        sb.append(" select ");

        if (StringUtils.isNotBlank(selectSql)) {
            sb.append(selectSql);
        } else {
            sb.append("*");
        }

        sb.append(" from ").append(tableName).append(" mfl ")
          .append(" where 1=1 ");

        if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
            sb.append(" and mfl.dev_name = :devName ");
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
            sb.append(" and mfl.src_ip = :querySrcIp ");
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
            sb.append(" and mfl.src_port = :querySrcPort ");
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
            sb.append(" and mfl.dst_ip = :queryDstIp ");
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
            sb.append(" and mfl.dst_port = :queryDstPort ");
        }
        /*
        if (StringUtils.isNotBlank(fVO.getQueryDateBegin())) {
            sb.append(" and (mfl.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and mfl.date < DATE_ADD(:beginDate, INTERVAL 1 DAY)) ");
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
            sb.append(" and (mfl.time >= :beginTime and mfl.time < :endTime) ");
        }
        */
        if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
            sb.append(" and (mfl.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and mfl.time >= :beginTime) ");
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
            sb.append(" and (mfl.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and mfl.time < :endTime) ");
        }

        if (StringUtils.isNotBlank(fVO.getSearchValue())) {
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

        String orderColumnName = fVO.getOrderColumn();
        String orderDirection = fVO.getOrderDirection();
        if (StringUtils.isNotBlank(orderColumnName) && !StringUtils.equals(orderColumnName, "DATE") && !StringUtils.equals(orderColumnName, "TIME")) {
            sb.append(" order by mfl.").append(orderColumnName).append(" ").append(orderDirection);
        } else {
            sb.append(" order by mfl.date ").append(orderDirection).append(", mfl.time ").append(orderDirection);
        }

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createNativeQuery(sb.toString());

        if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
            q.setParameter("devName", fVO.getQueryDevName());
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
            q.setParameter("querySrcIp", fVO.getQuerySrcIp());
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
            q.setParameter("querySrcPort", fVO.getQuerySrcPort());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
            q.setParameter("queryDstIp", fVO.getQueryDstIp());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
            q.setParameter("queryDstPort", fVO.getQueryDstPort());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateBegin())) {
            q.setParameter("beginDate", fVO.getQueryDateBegin());
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
            q.setParameter("beginTime", fVO.getQueryTimeBegin());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateEnd())) {
            q.setParameter("endDate", fVO.getQueryDateEnd());
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
            q.setParameter("endTime", fVO.getQueryTimeEnd());
        }
        if (StringUtils.isNotBlank(fVO.getSearchValue())) {
            q.setParameter("searchValue", "%".concat(fVO.getSearchValue()).concat("%"));
        }
        if (startRow != null && pageLength != null) {
            q.setFirstResult(startRow);
            q.setMaxResults(pageLength);
        }

        return (List<Object[]>)q.list();
    }

    @Override
    public long countFirewallLogFromDBbyAll(FirewallVO fVO, Map<String, List<String>> searchLikeFieldMap) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(alltb.id) ")
          .append(" from ( ")
          .append("   select app.id ")
          .append("   from module_firewall_log_app app ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and app.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
              sb.append(" and app.src_ip = :querySrcIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
              sb.append(" and app.src_port = :querySrcPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
              sb.append(" and app.dst_ip = :queryDstIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
              sb.append(" and app.dst_port = :queryDstPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (app.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and app.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (app.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and app.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_APP);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("app.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }

        sb.append("   union all ")
          .append("   select forwarding.id ")
          .append("   from module_firewall_log_forwarding forwarding ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and forwarding.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
              sb.append(" and forwarding.src_ip = :querySrcIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
              sb.append(" and forwarding.src_port = :querySrcPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
              sb.append(" and forwarding.dst_ip = :queryDstIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
              sb.append(" and forwarding.dst_port = :queryDstPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (forwarding.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and forwarding.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (forwarding.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and forwarding.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_FORWARDING);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("forwarding.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }

        sb.append("   union all ")
          .append("   select intrusion.id ")
          .append("   from module_firewall_log_intrusion intrusion ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and intrusion.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
              sb.append(" and intrusion.src_ip = :querySrcIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
              sb.append(" and intrusion.src_port = :querySrcPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
              sb.append(" and intrusion.dst_ip = :queryDstIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
              sb.append(" and intrusion.dst_port = :queryDstPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (intrusion.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and intrusion.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (intrusion.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and intrusion.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_INTRUSION);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("intrusion.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }

        sb.append("   union all ")
          .append("   select sys.id ")
          .append("   from module_firewall_log_system sys ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and sys.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (sys.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and sys.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (sys.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and sys.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_SYSTEM);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("sys.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }

        sb.append("   union all ")
          .append("   select webfilter.id ")
          .append("   from module_firewall_log_webfilter webfilter ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and webfilter.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
              sb.append(" and webfilter.src_ip = :querySrcIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
              sb.append(" and webfilter.src_port = :querySrcPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
              sb.append(" and webfilter.dst_ip = :queryDstIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
              sb.append(" and webfilter.dst_port = :queryDstPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (webfilter.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and webfilter.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (webfilter.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and webfilter.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_WEBFILTER);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("webfilter.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }
        sb.append(") alltb ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createNativeQuery(sb.toString());

        if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
            q.setParameter("devName", fVO.getQueryDevName());
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
            q.setParameter("querySrcIp", fVO.getQuerySrcIp());
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
            q.setParameter("querySrcPort", fVO.getQuerySrcPort());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
            q.setParameter("queryDstIp", fVO.getQueryDstIp());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
            q.setParameter("queryDstPort", fVO.getQueryDstPort());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateBegin())) {
            q.setParameter("beginDate", fVO.getQueryDateBegin());
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
            q.setParameter("beginTime", fVO.getQueryTimeBegin());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateEnd())) {
            q.setParameter("endDate", fVO.getQueryDateEnd());
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
            q.setParameter("endTime", fVO.getQueryTimeEnd());
        }
        if (StringUtils.isNotBlank(fVO.getSearchValue())) {
            q.setParameter("searchValue", "%".concat(fVO.getSearchValue()).concat("%"));
        }

        return DataAccessUtils.longResult(q.list());
    }

    @Override
    public List<Object[]> findFirewallLogFromDBbyAll(FirewallVO fVO, Integer startRow, Integer pageLength,
            Map<String, String> selectSqlMap, Map<String, List<String>> searchLikeFieldMap) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select alltb.* ")
          .append(" from ( ")
          .append("   select ").append(selectSqlMap.get(Constants.FIREWALL_LOG_TYPE_APP))
          .append("   from module_firewall_log_app app ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and app.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
              sb.append(" and app.src_ip = :querySrcIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
              sb.append(" and app.src_port = :querySrcPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
              sb.append(" and app.dst_ip = :queryDstIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
              sb.append(" and app.dst_port = :queryDstPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (app.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and app.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (app.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and app.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_APP);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("app.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }

        sb.append("   union all ")
          .append("   select ").append(selectSqlMap.get(Constants.FIREWALL_LOG_TYPE_FORWARDING))
          .append("   from module_firewall_log_forwarding forwarding ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and forwarding.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
              sb.append(" and forwarding.src_ip = :querySrcIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
              sb.append(" and forwarding.src_port = :querySrcPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
              sb.append(" and forwarding.dst_ip = :queryDstIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
              sb.append(" and forwarding.dst_port = :queryDstPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (forwarding.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and forwarding.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (forwarding.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and forwarding.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_FORWARDING);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("forwarding.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }

        sb.append("   union all ")
          .append("   select ").append(selectSqlMap.get(Constants.FIREWALL_LOG_TYPE_INTRUSION))
          .append("   from module_firewall_log_intrusion intrusion ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and intrusion.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
              sb.append(" and intrusion.src_ip = :querySrcIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
              sb.append(" and intrusion.src_port = :querySrcPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
              sb.append(" and intrusion.dst_ip = :queryDstIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
              sb.append(" and intrusion.dst_port = :queryDstPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (intrusion.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and intrusion.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (intrusion.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and intrusion.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_INTRUSION);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("intrusion.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }

        sb.append("   union all ")
          .append("   select ").append(selectSqlMap.get(Constants.FIREWALL_LOG_TYPE_SYSTEM))
          .append("   from module_firewall_log_system sys ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and sys.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (sys.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and sys.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (sys.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and sys.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_SYSTEM);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("sys.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }

        sb.append("   union all ")
          .append("   select ").append(selectSqlMap.get(Constants.FIREWALL_LOG_TYPE_WEBFILTER))
          .append("   from module_firewall_log_webfilter webfilter ")
          .append("   where 1=1 ");

          if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
              sb.append(" and webfilter.dev_name = :devName ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
              sb.append(" and webfilter.src_ip = :querySrcIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
              sb.append(" and webfilter.src_port = :querySrcPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
              sb.append(" and webfilter.dst_ip = :queryDstIp ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
              sb.append(" and webfilter.dst_port = :queryDstPort ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateBegin()) && StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
              sb.append(" and (webfilter.date >= DATE_FORMAT(:beginDate, '%Y-%m-%d') and webfilter.time >= :beginTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getQueryDateEnd()) && StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
              sb.append(" and (webfilter.date <= DATE_FORMAT(:endDate, '%Y-%m-%d') and webfilter.time < :endTime) ");
          }
          if (StringUtils.isNotBlank(fVO.getSearchValue())) {
              StringBuffer sfb = new StringBuffer();
              sfb.append(" and ( ");

              List<String> searchLikeField = searchLikeFieldMap.get(Constants.FIREWALL_LOG_TYPE_WEBFILTER);
              int i = 0;
              for (String sField : searchLikeField) {
                  sfb.append("webfilter.").append(sField).append(" like :searchValue ");

                  if (i < searchLikeField.size() - 1) {
                      sfb.append(" or ");
                  }

                  i++;
              }

              sfb.append(" ) ");
              sb.append(sfb);
          }
        sb.append(") alltb ");

        String orderColumnName = fVO.getOrderColumn();
        String orderDirection = StringUtils.isBlank(fVO.getOrderDirection()) ? "desc" : fVO.getOrderDirection();
        if (StringUtils.isNotBlank(orderColumnName) && !StringUtils.equals(orderColumnName, "DATE") && !StringUtils.equals(orderColumnName, "TIME")) {
            sb.append(" order by alltb.").append(orderColumnName).append(" ").append(orderDirection);

        } else {
            sb.append(" order by alltb.date ").append(orderDirection).append(", alltb.time ").append(orderDirection);
        }

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createNativeQuery(sb.toString());

        if (StringUtils.isNotBlank(fVO.getQueryDevName())) {
            q.setParameter("devName", fVO.getQueryDevName());
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcIp())) {
            q.setParameter("querySrcIp", fVO.getQuerySrcIp());
        }
        if (StringUtils.isNotBlank(fVO.getQuerySrcPort())) {
            q.setParameter("querySrcPort", fVO.getQuerySrcPort());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstIp())) {
            q.setParameter("queryDstIp", fVO.getQueryDstIp());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDstPort())) {
            q.setParameter("queryDstPort", fVO.getQueryDstPort());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateBegin())) {
            q.setParameter("beginDate", fVO.getQueryDateBegin());
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeBegin())) {
            q.setParameter("beginTime", fVO.getQueryTimeBegin());
        }
        if (StringUtils.isNotBlank(fVO.getQueryDateEnd())) {
            q.setParameter("endDate", fVO.getQueryDateEnd());
        }
        if (StringUtils.isNotBlank(fVO.getQueryTimeEnd())) {
            q.setParameter("endTime", fVO.getQueryTimeEnd());
        }
        if (StringUtils.isNotBlank(fVO.getSearchValue())) {
            q.setParameter("searchValue", "%".concat(fVO.getSearchValue()).concat("%"));
        }
        if (startRow != null && pageLength != null) {
            q.setFirstResult(startRow);
            q.setMaxResults(pageLength);
        }

        return (List<Object[]>)q.list();
    }
}
