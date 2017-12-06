package com.example.qgchat.util;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.qgchat.R;

import java.util.List;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by Administrator on 2017/10/13.
 */

public class NotificationUtil {

    /**
     * @param context
     * @param title
     * @param content
     * @param intent
     * 显示弹框提醒消息(类似qq)
     */
    public static void setChatMsgNotification(Context context,String title,String content,Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.headicon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.headicon))
                .setDefaults(NotificationCompat.DEFAULT_ALL) //系统自动选择声音灯光和震动
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent,true)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        manager.notify(1, notification);

        //                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .setVibrate(new long[]{0,1000})  //震动
//                .setLights(Color.GREEN,1000,1000)  //led灯
    }

    /**
     * @param context
     * @return 是否处于后台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (TextUtils.equals(appProcess.processName, context.getPackageName())) {
//                Log.i("info", "isBackground: "+appProcess.importance);
                boolean isBackground = (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE);
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                return isBackground || isLockedState;
            }
        }
        return false;
    }
}
