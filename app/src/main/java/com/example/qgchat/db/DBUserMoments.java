package com.example.qgchat.db;

import org.litepal.crud.DataSupport;

import java.sql.Date;

/**
 * Created by Administrator on 2017/8/27.
 */

public class DBUserMoments extends DataSupport {
    private int id;
    private Date sendTime;
    private String shareContent;
    private DBUserList dbUserList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public DBUserList getDbUserList() {
        return dbUserList;
    }

    public void setDbUserList(DBUserList dbUserList) {
        this.dbUserList = dbUserList;
    }
}
