package com.example.qgchat;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.permissionlib.*;
import com.example.qgchat.db.DBUserGruop;
import com.example.qgchat.db.DBUserItemMsg;
import com.example.qgchat.db.DBUserList;
import com.example.qgchat.listener.*;
import com.example.qgchat.listener.PermissionListener;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.LogUtil;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.permission;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_welcome);

//        DBUserList dbUserList = new DBUserList();
//        dbUserList.setUsername("mashanshui");
//        dbUserList.setIconURL("");
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
//
//        DBUserGruop gruop1 = DataSupport.find(DBUserGruop.class,3,true);
//        List<DBUserList> lists1=gruop.getDbUserLists();
//        if (lists1.size() > 0) {
//            LogUtil.i("info",lists1.get(0).getUsername());
//        }


//        DBUserItemMsg dbUserItemMsg = new DBUserItemMsg();
//        dbUserItemMsg.setIconURL("sadf");
//        dbUserItemMsg.setSign("sdfsd");
//        dbUserItemMsg.setUsername("sefdsd");
//        dbUserItemMsg.save();
//        LogUtil.i("info",String.valueOf(dbUserItemMsg.getId()));

//        AccessNetwork network = new AccessNetwork(this);
//        LogUtil.i("info",String.valueOf(network.getNetworkStateName()));

    }
}
