package com.example.qgchat;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MyApplication extends Application {
    public static Context applicationContext;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
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

        Helper.getInstance().init(applicationContext);
        
    }

    public static MyApplication getInstance() {
        return instance;
    }


}
