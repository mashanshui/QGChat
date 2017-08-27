package com.example.qgchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.qgchat.util.UltimateBar;

import butterknife.ButterKnife;

public class AtyWelcome extends AppCompatActivity {

    private static final int DELAY = 2000;
    private static final int GO_GUIDE = 0;
    private static final int GO_HOME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_welcome);
        ButterKnife.bind(this);
        ImageView welcome_image = (ImageView) findViewById(R.id.welcome_image);
        Glide.with(this).load(R.drawable.shot).into(welcome_image);
        initLoad();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            UltimateBar ultimateBar = new UltimateBar(this);
            ultimateBar.setHintBar();
        }
    }

    private void initLoad() {
        SharedPreferences preferences=getSharedPreferences("qgchat",MODE_PRIVATE);
        boolean welcome = preferences.getBoolean("welcome", true);
        Log.i("info",String.valueOf(welcome));
        if (welcome) {
            handler.sendEmptyMessageDelayed(GO_GUIDE,DELAY);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("welcome",false);
            editor.apply();
        } else {
            handler.sendEmptyMessageDelayed(GO_HOME, DELAY);
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==GO_GUIDE){
                goGuide();
            } else if (msg.what == GO_HOME) {
                goHome();
            }
        }
    };

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
