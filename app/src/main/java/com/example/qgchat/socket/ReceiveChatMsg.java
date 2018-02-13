package com.example.qgchat.socket;


import android.util.Log;

import com.example.qgchat.bean.ReceivedMsg;
import com.example.qgchat.util.EventBean;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


class ReceiveChatMsg {


    public void delMessage(String msg) {
        if (msg != null) {
            String action = getAction(msg);
            switch (action) {
                case "ACKLOGIN": {
                    dealLogin(msg);
                    break;
                }
                case "REGISTER": {
                    dealRegister(msg);
                    break;
                }
                case "ACKCHATMSG": {
                    dealAckChatMsg(msg);
                    break;
                }

                case "RECEIVECHATMSG": {
                    dealChatMsg(msg);
                    break;
                }

                case "ACK_SEARCH_FRIEND":{
                    dealSearchFriend(msg);
                    break;
                }
                case "ACK_SEARCH_FRIEND_TRUE":{
                    dealSearchFriendTrue(msg);
                    break;
                }
                default:
                    dealError();
                    break;
            }
        }
    }

    private void dealSearchFriendTrue(String msg) {
        String p = "\\[ACK_SEARCH_FRIEND_TRUE\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);

    }

    private void dealSearchFriend(String msg) {
        String p = "\\[ACK_SEARCH_FRIEND\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);

    }

    private void dealAckChatMsg(String msg) {
    }

    private void dealRegister(String msg) {
        String p = "\\[REGISTER\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            EventBus.getDefault().post(new EventBean.RegisterEvent(matcher.group(1).equals("1")));
        }
    }


    public void dealLogin(String msg) {
        String p = "\\[ACKLOGIN\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
//            Log.i("info", "dealLogin: ");
            EventBus.getDefault().post(new LoginEvent(matcher.group(1).equals("1")));
        }
    }

    public void dealChatMsg(String msg) {
        String chatObj = null;
        String username = null;
        String iconURL = null;
        String MsgType = null;
        String content = null;

        String p = "\\[RECEIVECHATMSG\\]:\\[(.*), (.*), (.*), (.*), (.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            chatObj = matcher.group(1);
            username = matcher.group(2);
            iconURL = matcher.group(3);
            MsgType = matcher.group(4);
            content = matcher.group(5);
            int type = Integer.parseInt(MsgType);
            EventBus.getDefault().post(new ReceivedMsg(type,iconURL,username,content,chatObj));
        }
        Log.i("info", "dealChatMsg: "+chatObj+username+iconURL+MsgType+content);
    }


    private void dealError() {
    }

    public String getAction(String msg) {
        String p = "\\[(.*)\\]:";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "error";
        }
    }
}
