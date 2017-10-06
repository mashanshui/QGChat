package com.example.qgchat.db;


import org.litepal.crud.DataSupport;

public class DBChatMsg extends DataSupport{

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private int id;
    private int MsgType;
    private String iconURL;
    private String username;
    private String content;
    private String chatObj;

    private DBUserItemMsg dbUserItemMsg;

    public DBUserItemMsg getDbUserItemMsg() {
        return dbUserItemMsg;
    }

    public void setDbUserItemMsg(DBUserItemMsg dbUserItemMsg) {
        this.dbUserItemMsg = dbUserItemMsg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChatObj() {
        return chatObj;
    }

    public void setChatObj(String chatObj) {
        this.chatObj = chatObj;
    }
}
