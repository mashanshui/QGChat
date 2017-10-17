package com.example.qgchat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.qgchat.activity.AtyChatRoom;
import com.example.qgchat.bean.ReceivedMsg;
import com.example.qgchat.db.DBChatMsg;
import com.example.qgchat.util.DBUtil;
import com.example.qgchat.util.NotificationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class QGService extends Service {
    private QGBinder mBinder = new QGBinder();

    public QGService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ReceivedMsg receivedMsg) {
        Intent intent = new Intent(this, AtyChatRoom.class);
        intent.putExtra("friend_account", receivedMsg.getChatObj());
        DBChatMsg msg = new DBChatMsg();
        msg.setMsgType(receivedMsg.getMsgType());
        msg.setContent(receivedMsg.getContent());
        DBUtil.saveChatMsg(receivedMsg.getChatObj(),msg);
        if (NotificationUtil.isBackground(this)) {
            NotificationUtil.setChatMsgNotification(this,receivedMsg.getUsername(),receivedMsg.getContent(),intent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class QGBinder extends Binder {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
