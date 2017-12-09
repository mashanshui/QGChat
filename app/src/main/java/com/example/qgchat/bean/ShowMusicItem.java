package com.example.qgchat.bean;

/**
 * Created by Administrator on 2016/12/4.
 */

public class ShowMusicItem {
    public static final int PLAY = 1;
    public static final int STOP = 2;
    private int playState=STOP;
    private String songname;
    private String singername;
    private String duration;
    private String hash;

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
