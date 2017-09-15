package com.example.qgchat.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/27.
 */

public class DBUserList extends DataSupport {
    private int id;
    private String iconURL;
    private String username;
    //建立一对多的连接
    private DBUserGruop dbUserGruop;

    private List<DBUserMoments> dbUserMomentsList = new ArrayList<DBUserMoments>();

    private List<DBUserItemMsg> dbUserItemMsgList = new ArrayList<DBUserItemMsg>();


    public List<DBUserItemMsg> getDbUserItemMsgList() {
        return dbUserItemMsgList;
    }

    public void setDbUserItemMsgList(List<DBUserItemMsg> dbUserItemMsgList) {
        this.dbUserItemMsgList = dbUserItemMsgList;
    }

    public List<DBUserMoments> getDbUserMomentsList() {
        return dbUserMomentsList;
    }

    public void setDbUserMomentsList(List<DBUserMoments> dbUserMomentsList) {
        this.dbUserMomentsList = dbUserMomentsList;
    }

    public DBUserGruop getDbUserGruop() {
        return dbUserGruop;
    }

    public void setDbUserGruop(DBUserGruop dbUserGruop) {
        this.dbUserGruop = dbUserGruop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
