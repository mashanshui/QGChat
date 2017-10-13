package com.example.qgchat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.qgchat.broadcast.NetworkChangeReceiver;
import com.example.qgchat.listener.PermissionListener;
import com.example.qgchat.loginAndregister.AtyLogin;
import com.example.qgchat.socket.LoginEvent;
import com.example.qgchat.socket.ParaseData;
import com.example.qgchat.socket.ServerManager;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.ActivityCollector;
import com.example.qgchat.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/29.
 */

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = "info";
    public ServerManager serverManager = ServerManager.getServerManager();
    private static PermissionListener permissionListener;
    private ProgressDialog dialog;
    public static String account = null;
    public String password = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public static void requestRuntimePermissions(String[] permissions, PermissionListener listener) {
        permissionListener = listener;
        Activity topActivity = ActivityCollector.getTopActivity();
        if (topActivity == null) {
            return;
        }
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(topActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(topActivity, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermission = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermission.add(permission);
                        }
                    }
                    if (deniedPermission.isEmpty()) {
                        permissionListener.onGranted();
                    } else {
                        permissionListener.onDenied(deniedPermission);
                    }
                }
                break;
        }
    }

    public void showBufferDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                HttpUtil.cancelOkHttpRequest();
            }
        });
    }

    public void dismissBufferDialog() {
        dialog.dismiss();
    }

    public void autoLogin() {
        if (serverManager.getAccount() == null) {
            //如果线程没有start过，进行start
            if (serverManager.getState()== Thread.State.NEW) {
                serverManager.start();
            }
            //如果socket没有连接，使用Thread的run方法再次启动
            if (serverManager.socket==null || serverManager.socket.isClosed()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        serverManager.run();
                    }
                }).start();
            }
            SharedPreferences preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
            account = preferences.getString("account", "");
            password = preferences.getString("password", "");
            ParaseData.requestLogin(account, password);
        }
    }

    /**
     * @param login 登录结果的返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent login) {
        if (login.isLogin()) {
            serverManager.setAccount(account);
            setToast("网络已连接");
        } else {
            goLogin();
        }
    }

    public void setToast(String value){
        Toast.makeText(this,value,Toast.LENGTH_SHORT).show();
    }

    private void goLogin() {
        Intent intent = new Intent(ActivityCollector.getTopActivity(), AtyLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(NetworkChangeReceiver.NetWorkChange change) {
        if (change.getNetworkState() != AccessNetwork.INTERNET_NONE) {
            autoLogin();
        }
    }

}
