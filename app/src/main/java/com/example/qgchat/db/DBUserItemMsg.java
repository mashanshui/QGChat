package com.example.qgchat.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/27.
 */

public class DBUserItemMsg extends DataSupport {
    private int id;
    private String chatObj;
    private String iconURL;
    private String username;
    private List<DBChatMsg> dbChatMsgList = new ArrayList<>();

    public String getChatObj() {
        return chatObj;
    }

    public void setChatObj(String chatObj) {
        this.chatObj = chatObj;
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

    public List<DBChatMsg> getDbChatMsgList() {
        return dbChatMsgList;
    }

    public void setDbChatMsgList(List<DBChatMsg> dbChatMsgList) {
        this.dbChatMsgList = dbChatMsgList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
