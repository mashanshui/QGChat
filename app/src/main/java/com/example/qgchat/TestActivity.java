package com.example.qgchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.qgchat.db.DBChatMsg;
import com.example.qgchat.db.DBUserGruop;
import com.example.qgchat.db.DBUserItemMsg;
import com.example.qgchat.db.DBUserList;
import com.example.qgchat.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.example.qgchat.R.id.myMsg;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_welcome);

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
        DBChatMsg msg = new DBChatMsg();
        msg.setContent("sdf");
        msg.setMsgType(DBChatMsg.TYPE_SENT);
        msg.save();
//        List<DBChatMsg> chatMsgs =DataSupport.findAll(DBChatMsg.class);
//        Log.i("info", "onCreate: "+chatMsgs.get(0).getContent());
        List<DBChatMsg> chatMsgs = new ArrayList<>();
        chatMsgs.add(msg);
        DBUserItemMsg itemMsg = new DBUserItemMsg();
        itemMsg.setDbChatMsgList(chatMsgs);
        itemMsg.save();

        List<DBUserItemMsg> dbUserItemMsgs = DataSupport.findAll(DBUserItemMsg.class,true);
        Log.i("info", "loadData: ------------"+dbUserItemMsgs.get(0).getDbChatMsgList().get(0).getContent());
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
