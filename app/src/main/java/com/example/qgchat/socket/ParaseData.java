package com.example.qgchat.socket;

import android.content.Context;
import android.widget.Toast;

import com.example.qgchat.bean.SendMsg;
import com.example.qgchat.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.password;

public class ParaseData {

    private static final String TAG = "ParaseData";
    private static ServerManager serverManager = ServerManager.getServerManager();


    public static void requestLogin(String account,String password){
        if (StringUtil.isEmpty(account,password)) {
            return;
        }
        String msg = "[LOGIN]:[" + account + ", " + password + "]";
        serverManager.sendMessage(msg);
    }

    public static void requestRegister(String account,String password,String username){
        if (StringUtil.isEmpty(account,password,username)) {
            return;
        }
        String msg = "[REGISTER]:[" + account + ", " + password + ", " + username +"]";
        serverManager.sendMessage(msg);
    }

    public static void sendSearchFriend(String account) {
        if (StringUtil.isEmpty(account)) {
            return;
        }
        String msg = "[SEARCH_FRIEND]:[" + account + "]";
        serverManager.sendMessage(msg);
    }

    public static void sendSearchFriendTrue(String account) {
        if (StringUtil.isEmpty(account)) {
            return;
        }
        String msg = "[SEARCH_FRIEND_TRUE]:[" + account + "]";
        serverManager.sendMessage(msg);
    }

    public static void sendChatMsg(SendMsg sendMsg) {
        if (StringUtil.isEmpty(sendMsg.getChatObj(), sendMsg.getContent())) {
            return;
        }
        String msg= "[SENDCHATMSG]:[" + sendMsg.getChatObj() + ", " + sendMsg.getMsgType() + ", " + sendMsg.getContent() +"]";
        serverManager.sendMessage(msg);
    }

    public static String[] getDressUp(Context context, String username) {
        String[] strings = {"", ""};
        String msg = "[GETDRESSUP]:[" + username +"]";
        serverManager.sendMessage(msg);
        String ack = serverManager.getMessage();
        if (ack == null) {
            Toast.makeText(context, "load dress up failed", Toast.LENGTH_SHORT).show();
            return strings;
        }
        serverManager.setMessage(null);
        String p = "\\[ACKGETDRESSUP\\]:\\[(.*), (.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        if (matcher.find()) {
            strings[0] = matcher.group(1);
            strings[1] = matcher.group(2);
        }
        return strings;
    }

    public static String[] getProfile(Context context, String username) {
        String[] strings = {""};
        String msg = "[GETPROFILE]:[" + username + "]";
        serverManager.sendMessage(msg);
        String ack = serverManager.getMessage();
        if (ack == null) {
            Toast.makeText(context, "load profile failed", Toast.LENGTH_SHORT).show();
            return strings;
        }
        serverManager.setMessage("");
        String p = "\\[ACKGETPROFILE\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        if (matcher.find()) {
            strings[0] = matcher.group(1);
        }
        return strings;
    }

    public static List<String> getGroupList(Context context, String username) {
        List<String> strings = new ArrayList<>();
        String msg = "[GETGROUPLIST]:[" + username + "]";
        serverManager.sendMessage(msg);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ack = serverManager.getMessage();
        if (ack == null) {
            Toast.makeText(context, "load group list failed", Toast.LENGTH_SHORT).show();
            return strings;
        }
        serverManager.setMessage("");
        String p = "\\[ACKGETGROUPLIST\\]:\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        while (matcher.find()) {
            strings.add(matcher.group(1));
        }
        return strings;
    }

    public static List<String> getFriendList(Context context, String username) {
        List<String> strings = new ArrayList<>();
        String msg = "[GETFRIENDLIST]:[" + username + "]";
        serverManager.sendMessage( msg);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ack = serverManager.getMessage();
        if (ack == null) {
            Toast.makeText(context, "load friend list failed", Toast.LENGTH_SHORT).show();
            return strings;
        }
        serverManager.setMessage("");
        String p = "\\[ACKGETFRIENDLIST\\]:\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        while (matcher.find()) {
            strings.add(matcher.group(1));
        }
        return strings;
    }

    public static String[] getFriendProfile(Context context, String username) {
        String[] strings = {"", "", "", ""};
        String msg = "[GETFRIENDPROFILE]:[" + username + "]";
        serverManager.sendMessage(msg);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ack = serverManager.getMessage();
        if (ack == null) {
            Toast.makeText(context, "load friend profile failed", Toast.LENGTH_SHORT).show();
            return strings;
        }
        serverManager.setMessage(null);
        String p = "\\[ACKGETFRIENDPROFILE\\]:\\[(.*), (.*), (.*), (.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        if (matcher.find()) {
            strings[0] = matcher.group(1);
            strings[1] = matcher.group(2);
            strings[2] = matcher.group(3);
            strings[3] = matcher.group(4);
        }
        return strings;
    }

    public static List<String> getAllGroupList(Context context) {
        List<String> strings = new ArrayList<>();
        String msg = "[GETALLGROUPLIST]:[]";
        serverManager.sendMessage(msg);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ack = serverManager.getMessage();
        if (ack == null) {
            Toast.makeText(context, "load all group list failed", Toast.LENGTH_SHORT).show();
            return strings;
        }
        serverManager.setMessage("");
        String p = "\\[ACKGETALLGROUPLIST\\]:\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        while (matcher.find()) {
            strings.add(matcher.group(1));
        }
        return strings;
    }
}