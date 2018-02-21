package com.example.qgchat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.example.qgchat.activity.NewFriendsMsgActivity;
import com.example.qgchat.bean.StatusResponse;
import com.example.qgchat.db.DBInviteMessage;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.PreferencesUtil;
import com.google.gson.Gson;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MyApplication extends Application {
    public static Context applicationContext;
    private PreferencesUtil preferencesUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        preferencesUtil = new PreferencesUtil(this);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59fd1d6b");
        LitePal.initialize(this);

        EMOptions options = new EMOptions();
        options.setAutoLogin(true);
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        EaseUI.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
            sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
//            refreshUIWithContacts();
        }

        @Override
        public void onContactDeleted(final String username) {
            sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
//            refreshUIWithContacts();
        }

        @Override
        public void onContactInvited(final String username, String reason) {
            DBInviteMessage new_friend = new DBInviteMessage();
            new_friend.setStatus(DBInviteMessage.BEINVITEED);
            new_friend.setReason(reason);
            new_friend.setTime(System.currentTimeMillis());
            new_friend.setFrom(username);
            new_friend.save();
            preferencesUtil.setUnreadMsgCount(preferencesUtil.getUnreadMsgCount() + 1);
            EMMessage emMessage = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
                @Override
                public String getDisplayedText(EMMessage message) {
                    return username+"请求添加你为好友";
                }

                @Override
                public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                    return username+"请求添加你为好友";
                }

                @Override
                public String getTitle(EMMessage message) {
                    return "好友请求";
                }

                @Override
                public int getSmallIcon(EMMessage message) {
                    return 0;
                }

                @Override
                public Intent getLaunchIntent(EMMessage message) {
                    return new Intent(applicationContext, NewFriendsMsgActivity.class);
                }
            });
            EaseUI.getInstance().getNotifier().onNewMsg(emMessage);
//            refreshUIWithContacts();
            sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            DBInviteMessage new_friend = new DBInviteMessage();
            new_friend.setStatus(DBInviteMessage.BEAGREED);
            new_friend.setTime(System.currentTimeMillis());
            new_friend.setFrom(username);
            new_friend.save();
//            refreshUIWithContacts();
            sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
            addFriend(preferencesUtil.getAccount(),username);
        }

        @Override
        public void onFriendRequestDeclined(String username) {
            DBInviteMessage new_friend = new DBInviteMessage();
            new_friend.setStatus(DBInviteMessage.BEREFUSED);
            new_friend.setTime(System.currentTimeMillis());
            new_friend.setFrom(username);
            new_friend.save();
        }

    }

    private void addFriend(String ownerAccount,String friendAccount) {
        String url = HttpUtil.addFriendURL + "?ownerAccount=" + ownerAccount + "&friendAccount=" + friendAccount;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                String responseData = response.body().string();
                Gson gson = new Gson();
                StatusResponse statusResponse = gson.fromJson(responseData, StatusResponse.class);
            }
        });
    }
}
