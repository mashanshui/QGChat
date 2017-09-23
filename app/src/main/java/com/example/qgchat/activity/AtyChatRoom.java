package com.example.qgchat.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qgchat.R;
import com.example.qgchat.adapter.AdapterChatRoom;
import com.example.qgchat.bean.ChatMsg;
import com.example.qgchat.bean.UserItemMsg;
import com.example.qgchat.util.StateButton;
import com.example.qgchat.util.UltimateBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AtyChatRoom extends BaseActivity {

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chatRoomRecycleView)
    RecyclerView chatRoomRecycleView;
    @BindView(R.id.myMsg)
    EditText myMsg;
    @BindView(R.id.btnSend)
    StateButton btnSend;

    private AdapterChatRoom adapter;
    private List<ChatMsg> chatMsgList = new ArrayList<>();
    private UserItemMsg msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_chat_room);
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
        msg=getIntent().getParcelableExtra("userItemMsg");
        msg.getUsername();

        setToolbarTitle("消息");
        initAdapter();
        initView();
    }

    private void setToolbarTitle(String title) {
        titleText.setText(title);
    }


    private void initView() {
        myMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0) {
                    btnSend.setClickable(true);
                    btnSend.setNormalBackgroundColor(ContextCompat.getColor(AtyChatRoom.this, R.color.colorPrimary));
                } else {
                    btnSend.setClickable(false);
                    btnSend.setNormalBackgroundColor(ContextCompat.getColor(AtyChatRoom.this, R.color.sendButtonColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMsg msg = new ChatMsg();
                msg.setContent(myMsg.getText().toString());
                msg.setMsgType(ChatMsg.TYPE_SENT);
                chatMsgList.add(msg);
                myMsg.setText("");
                adapter.notifyItemInserted(chatMsgList.size()-1);
                chatRoomRecycleView.scrollToPosition(chatMsgList.size()-1);
            }
        });
    }

    private void initAdapter() {

        ChatMsg msg = new ChatMsg();
        msg.setContent("hellow world!");
        msg.setMsgType(ChatMsg.TYPE_SENT);
        chatMsgList.add(msg);

        adapter = new AdapterChatRoom(chatMsgList);
        chatRoomRecycleView.setAdapter(adapter);
        chatRoomRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }
}