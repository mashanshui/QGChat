package com.example.qgchat.bean;

import android.os.Parcel;
import android.os.Parcelable;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/8/7.
 */

public class UserItemMsg {

    private String chatObj;
    private String iconURL;
    private String username;
    private String sign;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
