package com.example.qgchat.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/27.
 */

public class DBUserItemMsg extends DataSupport {
    private int id;
    private String sign;
    private DBUserList dbUserList;

    public DBUserList getDbUserList() {
        return dbUserList;
    }

    public void setDbUserList(DBUserList dbUserList) {
        this.dbUserList = dbUserList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
