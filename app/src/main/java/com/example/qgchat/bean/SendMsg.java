package com.example.qgchat.bean;

/**
 * Created by Administrator on 2017/9/24.
 */

public class SendMsg {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    private int MsgType;
    private String content;
    private String chatObj;

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
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
