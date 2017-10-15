package com.example.qgchat.util;

import com.example.qgchat.bean.GroupMessage;
import com.example.qgchat.bean.UserBean;
import com.example.qgchat.db.DBUser;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/10/9.
 */

public class BeanUtil {
    public static GroupMessage handleGroupMessageResponse(String response) {
        Gson gson = new Gson();
        GroupMessage groupMessage = gson.fromJson(response, GroupMessage.class);
        return groupMessage;
    }

    public static UserBean handleUserBeanResponse(String response) {
        Gson gson = new Gson();
        UserBean userBean = gson.fromJson(response, UserBean.class);
        return userBean;
    }
}
