package com.example.qgchat.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.qgchat.adapter.ContactsRecycleAdapter;

/**
 * Created by Administrator on 2017/8/25.
 */

public class UserList implements MultiItemEntity{

    public String account;
    public String iconURL;
    public String username;

    public UserList(String account, String iconURL, String username) {
        this.account = account;
        this.iconURL = iconURL;
        this.username = username;
    }

    @Override
    public int getItemType() {
        return ContactsRecycleAdapter.TYPE_USER_LIST;
    }
}
