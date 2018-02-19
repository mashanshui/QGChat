package com.example.qgchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.qgchat.db.DBInviteMessage;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_welcome);

        DBInviteMessage new_friend = new DBInviteMessage();
        new_friend.setStatus(DBInviteMessage.BEAPPLYED);
        new_friend.setReason("reason");
        Date date=new Date();
        new_friend.setTime(date.getTime());
        new_friend.setFrom("188985");
        new_friend.save();
        List<DBInviteMessage> msgs = DataSupport.findAll(DBInviteMessage.class);
        for (DBInviteMessage msg : msgs) {
            Log.e(TAG, "onCreate: "+msg.getStatus());
        }

//        DBUserList dbUserList = new DBUserList();
//        dbUserList.setUsername("mashanshui");
//        dbUserList.setIconURL("");
//        dbUserList.setAccount("1889545");
//        dbUserList.save();
//        List<DBUserList> lists = new ArrayList<>();
//        lists.add(dbUserList);
//
//        DBUserGruop gruop = new DBUserGruop();
//        gruop.setAllUserCount("0");
//        gruop.setGroupName("mss");
//        gruop.setOnlineUserCount("0");
//        gruop.setDbUserLists(lists);
//        gruop.save();
//        LogUtil.i("info",String.valueOf(gruop.getId()));
//
//        List<DBUserGruop> groupList= DataSupport.findAll(DBUserGruop.class,true);
//        DBUserGruop gruop1 = groupList.get(0);
//        List<DBUserList> lists1=gruop1.getDbUserLists();
//        if (lists1.size() > 0) {
//            LogUtil.i("info",lists1.get(0).getUsername());
//        }
//        List<DBUserList> userLists = DataSupport.findAll(DBUserList.class);
//        Log.i("info", String.valueOf(userLists.size()));
//        for (DBUserList userList : userLists) {
//            Log.i("info", "onCreate: "+userList.getAccount());
//        }
//        DBChatMsg msg = new DBChatMsg();
//        msg.setContent("sdf");
//        msg.setMsgType(DBChatMsg.TYPE_SENT);
//        msg.save();
//        List<DBChatMsg> chatMsgs =DataSupport.findAll(DBChatMsg.class);
//        Log.i("info", "onCreate: "+chatMsgs.get(0).getContent());

//        int i=itemMsg.updateAll("chatObj=?","13956821111");
//        Log.i("info", "saveChatMsg: "+String.valueOf(i));

//        UserItemMsg dbUserItemMsg = new UserItemMsg();
//        dbUserItemMsg.setIconURL("sadf");
//        dbUserItemMsg.setSign("sdfsd");
//        dbUserItemMsg.setUsername("sefdsd");
//        dbUserItemMsg.save();
//        LogUtil.i("info",String.valueOf(dbUserItemMsg.getId()));

//        AccessNetwork network = new AccessNetwork(this);
//        LogUtil.i("info",String.valueOf(network.getNetworkStateName()));

//        DBChatMsg msg = new DBChatMsg();
//        msg.setIconURL("asd");
//        msg.setUsername("asdfas");
//        msg.setChatObj("asd");
//        msg.setContent("asd");
//        msg.setMsgType(DBChatMsg.TYPE_RECEIVED);
//        msg.save();
//        Log.i(TAG, "onCreate: "+String.valueOf(msg.getId()));

    }
}
