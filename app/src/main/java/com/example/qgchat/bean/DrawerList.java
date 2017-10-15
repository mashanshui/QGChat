package com.example.qgchat.bean;

/**
 * Created by Administrator on 2017/10/14.
 */

public class DrawerList {
    private int resIconId;
    private int resTitleId;

    public DrawerList(int resIconId, int resTitleId) {
        this.resIconId = resIconId;
        this.resTitleId = resTitleId;
    }

    public int getResIconId() {
        return resIconId;
    }

    public void setResIconId(int resIconId) {
        this.resIconId = resIconId;
    }

    public int getResTitleId() {
        return resTitleId;
    }

    public void setResTitleId(int resTitleId) {
        this.resTitleId = resTitleId;
    }
}
