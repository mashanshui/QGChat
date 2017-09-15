package com.example.qgchat.bean;

/**
 * Created by Administrator on 2017/8/27.
 */

public class UserMoments {
    public String iconURL;
    public String username;
    public String sendTime;
    public String shareContent;

    public UserMoments(String iconURL, String username, String sendTime, String shareContent) {
        this.iconURL = iconURL;
        this.username = username;
        this.sendTime = sendTime;
        this.shareContent = shareContent;
    }
}
