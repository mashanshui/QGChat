package com.example.qgchat.socket;

/**
 * Created by Administrator on 2017/9/9.
 */

public class LoginEvent {
    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public LoginEvent(boolean isLogin) {

        this.isLogin = isLogin;
    }
}
