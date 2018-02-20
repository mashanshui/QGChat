package com.example.qgchat.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.qgchat.R;
import com.example.qgchat.listener.PermissionListener;
import com.example.qgchat.loginAndregister.AtyLogin;
import com.example.qgchat.socket.ServerManager;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.UltimateBar;
import com.hyphenate.chat.EMClient;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AtyWelcome extends BaseActivity {
    private static final String TAG = "AtyWelcome";
    private static final int DELAY = 800;
    private static final int GO_GUIDE = 0;
    private static final int GO_HOME = 1;
    private static final int GO_LOGIN = 2;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_welcome);
        ButterKnife.bind(this);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
        ImageView welcome_image = (ImageView) findViewById(R.id.welcome_image);
        Glide.with(this).load(R.drawable.shot).into(welcome_image);
        preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
        requestRuntimePermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.RECORD_AUDIO,
        }, new PermissionListener() {
            @Override
            public void onGranted() {
                initLoad();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                finish();
            }
        });
    }


    private void initLoad() {
        /**
         * 是否是第一次启动应用，是就跳转到引导界面
         */
        boolean welcome = preferences.getBoolean("welcome", true);
        /** 是否登陆过，也就是是否有缓存的帐号密码 */
        boolean login = EMClient.getInstance().isLoggedInBefore();
        if (welcome) {
            handler.sendEmptyMessageDelayed(GO_GUIDE, DELAY);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("welcome", false);
            editor.apply();
        } else if (!login) {
            handler.sendEmptyMessageDelayed(GO_LOGIN, DELAY);
        } else if (login) {
            EMClient.getInstance().chatManager().loadAllConversations();
            EMClient.getInstance().groupManager().loadAllGroups();
            handler.sendEmptyMessageDelayed(GO_HOME, DELAY);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GO_GUIDE) {
                goGuide();
            } else if (msg.what == GO_LOGIN) {
                goLogin();
            } else if (msg.what == GO_HOME) {
                goHome();
            }
        }
    };

    private void goLogin() {
        Intent intent = new Intent(AtyWelcome.this, AtyLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void goHome() {
        Intent intent = new Intent(AtyWelcome.this, AtyMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goGuide() {
        Intent intent = new Intent(AtyWelcome.this, AtyGuide.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
