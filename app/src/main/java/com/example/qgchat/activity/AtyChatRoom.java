package com.example.qgchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.qgchat.R;
import com.example.qgchat.fragment.ChatRoomFragment;
import com.example.qgchat.util.UltimateBar;
import com.hyphenate.easeui.EaseConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AtyChatRoom extends BaseActivity {

    public static AtyChatRoom activityInstance;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ChatRoomFragment chatFragment;
    private String toChatUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_chat_room);
        activityInstance = this;
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimary));
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        if (!TextUtils.isEmpty(toChatUsername)) {
            setToolbarTitle(toChatUsername);
        }
        initView();
    }

    private void initView() {
        chatFragment = new ChatRoomFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    private void setToolbarTitle(String title) {
        titleText.setText(title);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // enter to chat activity when click notification bar, here make sure only one chat activiy
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
}
