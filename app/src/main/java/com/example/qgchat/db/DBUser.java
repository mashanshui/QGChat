package com.example.qgchat.db;

import org.litepal.crud.DataSupport;

public class DBUser extends DataSupport{
	private String account;
	private String username;
	private String iconURL;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
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
}
