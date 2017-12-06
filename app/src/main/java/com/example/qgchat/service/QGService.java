package com.example.qgchat.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.qgchat.activity.AtyChatRoom;
import com.example.qgchat.bean.ReceivedMsg;
import com.example.qgchat.db.DBChatMsg;
import com.example.qgchat.loginAndregister.AtyLogin;
import com.example.qgchat.socket.LoginEvent;
import com.example.qgchat.socket.ParaseData;
import com.example.qgchat.socket.ServerManager;
import com.example.qgchat.util.ActivityCollector;
import com.example.qgchat.util.DBUtil;
import com.example.qgchat.util.NotificationUtil;
import com.example.qgchat.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class QGService extends Service {
    private QGBinder mBinder = new QGBinder();
    public ServerManager serverManager = ServerManager.getServerManager();
    public static String account = null;
    public String password = null;

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ServerManager.Connection connection) {
        if (connection.isConnection()) {
            autoLogin();
        }
    }

    /**
     * @param login 登录结果的返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent login) {
        SharedPreferences preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
        /** 是否登陆过，也就是是否有缓存的帐号密码 */
        boolean logined = preferences.getBoolean("login", false);
        if (login.isLogin() && logined) {
            account = preferences.getString("account", "");
            serverManager.setAccount(account);
        } else {
            goLogin();
        }
    }


    private void goLogin() {
        Intent intent = new Intent(ActivityCollector.getTopActivity(), AtyLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void autoLogin() {
        SharedPreferences preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
        account = preferences.getString("account", "");
        password = preferences.getString("password", "");
        if (!StringUtil.isEmpty(account,password)) {
//            Log.i("info", "service autoLogin: "+account+password);
            ParaseData.requestLogin(account, password);
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
