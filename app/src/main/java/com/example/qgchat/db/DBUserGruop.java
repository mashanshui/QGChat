package com.example.qgchat.db;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/27.
 */

public class DBUserGruop extends DataSupport{
    private int id;
    private String groupName;
    private String onlineUserCount;
    private String allUserCount;
    //建立一对多的连接
    private List<DBUserList> dbUserLists = new ArrayList<DBUserList>();

    public List<DBUserList> getDbUserLists() {
        return dbUserLists;
    }

    public void setDbUserLists(List<DBUserList> dbUserLists) {
        this.dbUserLists = dbUserLists;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOnlineUserCount() {
        return onlineUserCount;
    }

    public void setOnlineUserCount(String onlineUserCount) {
        this.onlineUserCount = onlineUserCount;
    }

    public String getAllUserCount() {
        return allUserCount;
    }

    public void setAllUserCount(String allUserCount) {
        this.allUserCount = allUserCount;
    }
}
