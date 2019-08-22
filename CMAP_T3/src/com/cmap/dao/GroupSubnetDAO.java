package com.cmap.dao;

import java.util.List;
import com.cmap.model.GroupSubnetSetting;

public interface GroupSubnetDAO {

    /**
     * 取得指定群組(GroupID)的網段設定
     * @param groupId
     * @return
     */
    public List<GroupSubnetSetting> getGroupSubnetSettingByGroupId(String groupId);
}
