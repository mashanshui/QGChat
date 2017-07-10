package com.example.qgchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import butterknife.ButterKnife;

public class AtyWelcome extends AppCompatActivity {

    private static final int DELAY = 2000;
    private static final int GO_GUIDE = 0;
    private static final int GO_HOME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_welcome);
        ButterKnife.bind(this);
        initLoad();
    }

    private void initLoad() {
        SharedPreferences preferences=getSharedPreferences("qgchat",MODE_PRIVATE);
        boolean welcome = preferences.getBoolean("welcome", true);
        if (welcome) {
            handler.sendEmptyMessageDelayed(GO_GUIDE,DELAY);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("welcome",false);
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
        startActivity(intent);
    }

    private void goGuide() {
        Intent intent = new Intent(AtyWelcome.this, AtyGuide.class);
        startActivity(intent);
    }
}
