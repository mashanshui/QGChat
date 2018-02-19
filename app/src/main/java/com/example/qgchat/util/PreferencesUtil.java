package com.example.qgchat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018-02-19.
 */

public class PreferencesUtil {
    private SharedPreferences sp;

    private Context mContext;

    public PreferencesUtil(Context mContext) {
        this.mContext = mContext;
        sp=mContext.getSharedPreferences("qgchat",mContext.MODE_PRIVATE);
    }

    public int getUnreadMsgCount(){
        return sp.getInt("unreadMsgCount", 0);
    }

    public void setUnreadMsgCount(int count){
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("unreadMsgCount", 0);
        editor.apply();
    }

    public String getAccount(){
        return sp.getString("account", null);
    }

    public void setAccount(String account) {
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("account",account);
        editor.apply();
    }
}
