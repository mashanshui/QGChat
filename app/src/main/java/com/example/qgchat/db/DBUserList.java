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
    private String account;
    //建立一对多的连接
    private DBUserGruop dbUserGruop;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    private List<DBUserMoments> dbUserMomentsList = new ArrayList<DBUserMoments>();

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
