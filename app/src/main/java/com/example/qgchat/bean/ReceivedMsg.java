package com.example.qgchat.bean;

/**
 * Created by Administrator on 2017/9/24.
 */

public class ReceivedMsg {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    private int MsgType;
    private String iconURL;
    private String username;
    private String content;
    private String chatObj;

    public ReceivedMsg(int msgType, String iconURL, String username, String content, String chatObj) {
        MsgType = msgType;
        this.iconURL = iconURL;
        this.username = username;
        this.content = content;
        this.chatObj = chatObj;
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
