package com.example.qgchat.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.qgchat.adapter.ContactsRecycleAdapter;

/**
 * Created by Administrator on 2017/8/25.
 */

public class UserGroup extends AbstractExpandableItem<UserList> implements MultiItemEntity {

    public String groupName;
    public String onlineUserCount;
    public String allUserCount;

    public UserGroup(String groupName, String onlineUserCount, String allUserCount) {
        this.groupName = groupName;
        this.onlineUserCount = onlineUserCount;
        this.allUserCount = allUserCount;
    }

    @Override
    public int getItemType() {
        return ContactsRecycleAdapter.TYPE_USER_GROUP;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
