package com.example.qgchat.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/7.
 */

public class UserItemMsg implements Parcelable {
    private String iconURL;
    private String username;
    private String sign;

    public UserItemMsg() {
    }

    protected UserItemMsg(Parcel in) {
        iconURL = in.readString();
        username = in.readString();
        sign = in.readString();
    }

    public static final Creator<UserItemMsg> CREATOR = new Creator<UserItemMsg>() {
        @Override
        public UserItemMsg createFromParcel(Parcel in) {
            return new UserItemMsg(in);
        }

        @Override
        public UserItemMsg[] newArray(int size) {
            return new UserItemMsg[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iconURL);
        dest.writeString(username);
        dest.writeString(sign);
    }
}
