package com.cmap.dao.impl;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.cmap.dao.GroupSubnetDAO;
import com.cmap.model.GroupSubnetSetting;

@Repository
@Transactional
public class GroupSubnetDAOImpl extends BaseDaoHibernate implements GroupSubnetDAO {

    @Override
    public List<GroupSubnetSetting> getGroupSubnetSettingByGroupId(String groupId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from GroupSubnetSetting gss ")
          .append(" where 1=1 ")
          .append(" and gss.groupId = :groupId ");

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query<?> q = session.createQuery(sb.toString());
        q.setParameter("groupId", groupId);

        return (List<GroupSubnetSetting>)q.list();
    }
}
