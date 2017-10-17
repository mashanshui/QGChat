package com.example.qgchat.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/10/15.
 */

public class DBWeather extends DataSupport {

    private String account;
    /**
     * 温度
     */
    private String tmp;
    /**
     * 天气状况
     */
    private String txt;
    /**
     * 城市
     */
    private String city;
    /**
     * 风向
     */
    private String dir;
    /**
     * 风力
     */
    private String sc;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
