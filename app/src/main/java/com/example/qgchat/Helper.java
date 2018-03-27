package com.example.qgchat;

import android.content.Context;
import android.content.Intent;

import com.example.qgchat.activity.NewFriendsMsgActivity;
import com.example.qgchat.cache.UserCacheManager;
import com.example.qgchat.db.DBInviteMessage;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.PreferencesUtil;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2018-02-23.
 */

public class Helper {
    private static Helper instance = null;
    private EaseUI easeUI;
    private Context appContext;
    private PreferencesUtil preferencesUtil;

    public synchronized static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }

    public void init(Context context) {
        appContext = context;
        preferencesUtil = new PreferencesUtil(context);
        easeUI = EaseUI.getInstance();
        setEaseUIProviders();
        setContactListener();
    }

    private void setContactListener() {
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
    }

    private void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

    private EaseUser getUserInfo(String username){
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server

        // 从本地缓存中获取用户昵称头像
        EaseUser user = UserCacheManager.getEaseUser(username);

//        if(username.equals(EMClient.getInstance().getCurrentUser()))
//            return getUserProfileManager().getCurrentUserInfo();
//        user = getContactList().get(username);
//        if(user == null && getRobotList() != null){
//            user = getRobotList().get(username);
//        }

        // if user is not in your contacts, set inital letter for him/her
        if(user == null){
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }
        return user;
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
            appContext.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
//            refreshUIWithContacts();
        }

        @Override
        public void onContactDeleted(final String username) {
            appContext.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
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
                    return new Intent(appContext, NewFriendsMsgActivity.class);
                }
            });
            EaseUI.getInstance().getNotifier().onNewMsg(emMessage);
//            refreshUIWithContacts();
            appContext.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            DBInviteMessage new_friend = new DBInviteMessage();
            new_friend.setStatus(DBInviteMessage.BEAGREED);
            new_friend.setTime(System.currentTimeMillis());
            new_friend.setFrom(username);
            new_friend.save();
//            refreshUIWithContacts();
            appContext.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
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
//                Message message = new Message();
//                String responseData = response.body().string();
//                Gson gson = new Gson();
//                StatusResponse statusResponse = gson.fromJson(responseData, StatusResponse.class);
            }
        });
    }
}
