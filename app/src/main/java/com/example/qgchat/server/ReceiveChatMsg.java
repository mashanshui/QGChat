package com.example.qgchat.server;


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
                    //dealRegister(msg);
                    break;
                }
                case "DRESSUP": {
                    //dealDressUp(msg);
                    break;
                }
                case "GETDRESSUP": {
                    //dealGetDressUp(msg);
                    break;
                }
                case "PROFILE": {
                    //dealProfile(msg);
                    break;
                }
                case "GETPROFILE": {
                    //dealGetProfile(msg);
                    break;
                }
                case "GETFRIENDLIST": {
                    //dealGetFriendList(msg);
                    break;
                }
                case "GETGROUPLIST": {
                    //dealGetGroupList(msg);
                    break;
                }
                case "GETFRIENDPROFILE": {
                    //dealGetFriendProfile(msg);
                    break;
                }
                case "STATE": {
                    //dealState(msg);
                    break;
                }
                case "CHATMSG": {
                    dealChatMsg(msg);
                    break;
                }
                case "USERLIST": {
                    //dealUserList(msg);
                    break;
                }
                case "ADDFRIEND": {
                    //dealAddFriend(msg);
                    break;
                }
                case "GROUPMEMBERLIST": {
                    //dealGroupMemberList(msg);
                    break;
                }
                case "ADDGROUP": {
                    //dealAddGroup(msg);
                    break;
                }
                case "GETALLGROUPLIST": {
                    //dealGetAllGroupList(msg);
                    break;
                }
                default:
                    dealError();
                    break;
            }
        }
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


    public void dealLogin(String msg) {
        String p = "\\[ACKLOGIN\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            EventBus.getDefault().post(new LoginEvent(matcher.group(1).equals("1")));
        }
    }

    public void dealChatMsg(String msg) {

        String sendName = null;
        String content = null;
        String avatarID = null;
        String fileType = null;
        String group = null;

        ServerManager.getServerManager().setMessage(null);
        String p = "\\[GETCHATMSG\\]:\\[(.*), (.*), (.*), (.*), (.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            sendName = matcher.group(1);
            content = matcher.group(2);
            avatarID = matcher.group(3);
            fileType = matcher.group(4);
            group = matcher.group(5);
        }
    }
}
