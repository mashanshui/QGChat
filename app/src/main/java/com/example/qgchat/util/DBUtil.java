package com.example.qgchat.util;

import android.util.Log;

import com.example.qgchat.bean.GroupMessage;
import com.example.qgchat.bean.UserBean;
import com.example.qgchat.db.DBChatMsg;
import com.example.qgchat.db.DBUser;
import com.example.qgchat.db.DBUserGruop;
import com.example.qgchat.db.DBUserItemMsg;
import com.example.qgchat.db.DBUserList;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.crud.DataSupport.where;

/**
 * Created by Administrator on 2017/9/25.
 */

public class DBUtil {

    public static boolean saveUserGroup(GroupMessage message) {
        boolean isSuccess=false;
        List<GroupMessage.GroupBean> groupBeanList = message.getGroup();
        for (GroupMessage.GroupBean groupBean : groupBeanList) {
            DBUserGruop userGroup = new DBUserGruop();
            userGroup.setGroupName(groupBean.getGroup_name());
            List<GroupMessage.GroupBean.FriendDetailsBean> userLists = groupBean.getFriend_details();
            List<DBUserList> lists = new ArrayList<>();
            for (GroupMessage.GroupBean.FriendDetailsBean userList : userLists) {
                GroupMessage.GroupBean.FriendDetailsBean.FriendDetailBean detailBean=userList.getFriend_detail();
                DBUserList dbUserList = new DBUserList();
                dbUserList.setUsername(detailBean.getFriend_username());
                dbUserList.setIconURL(detailBean.getFriend_iconURL());
                dbUserList.setAccount(detailBean.getFriend_account());
//                Log.i("info", "saveUserGroup: "+detailBean.getFriend_account()+detailBean.getFriend_iconURL()+detailBean.getFriend_username());
                dbUserList.save();
                lists.add(dbUserList);
            }
            userGroup.setDbUserLists(lists);
            isSuccess=userGroup.save();
        }
        return isSuccess;
    }

    public static void saveChatMsg(String friend_account, DBChatMsg msg) {
        List<DBUserList> userLists = where("account=?", friend_account).find(DBUserList.class);
        List<DBUserItemMsg> userItemMsgs = where("chatObj=?", friend_account).find(DBUserItemMsg.class,true);
        msg.save();
        if (userItemMsgs.isEmpty()) {
            DBUserItemMsg dbUserItemMsg = new DBUserItemMsg();
            dbUserItemMsg.setChatObj(userLists.get(0).getAccount());
            dbUserItemMsg.setUsername(userLists.get(0).getUsername());
            dbUserItemMsg.setIconURL(userLists.get(0).getIconURL());
            dbUserItemMsg.getDbChatMsgList().add(msg);
            dbUserItemMsg.save();
        } else {
            DBUserItemMsg dbUserItemMsg=userItemMsgs.get(0);
//            List<DBChatMsg> chatMsg=dbUserItemMsg.getDbChatMsgList();
//            chatMsg.add(msg);
            dbUserItemMsg.getDbChatMsgList().add(msg);
            dbUserItemMsg.save();
//            Log.i("info", "saveChatMsg: "+String.valueOf(b));
//            DBUserItemMsg itemMsg = new DBUserItemMsg();
//            itemMsg.setDbChatMsgList(chatMsg);
//            int i=itemMsg.updateAll("chatObj=?",userLists.get(0).getAccount());
//            Log.i("info", "saveChatMsg: "+String.valueOf(i)+userLists.get(0).getAccount());
        }
    }

    public static void saveUser(UserBean bean){
        DBUser dbUser = new DBUser();
        dbUser.setUsername(bean.getUsername());
        dbUser.setIconURL(bean.getIconURL());
        dbUser.setAccount(bean.getAccount());
        dbUser.saveOrUpdate("account=?", bean.getAccount());
    }
}
