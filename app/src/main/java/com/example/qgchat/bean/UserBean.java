package com.example.qgchat.bean;

/**
 * Created by Administrator on 2017/10/14.
 */

public class UserBean {

    private String id;
    private String account;
    private String password;
    private String username;
    private String iconURL;
    private String hx_account;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getIconURL() {
        return iconURL;
    }
    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }
    public String getHx_account() {
        return hx_account;
    }
    public void setHx_account(String hx_account) {
        this.hx_account = hx_account;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", account=" + account + ", password=" + password + ", username=" + username
                + ", iconURL=" + iconURL + ", hx_account=" + hx_account + "]";
    }

}
