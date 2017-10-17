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
import com.example.qgchat.bean.ReceivedMsg;
import com.example.qgchat.bean.SendMsg;
import com.example.qgchat.bean.UserItemMsg;
import com.example.qgchat.db.DBChatMsg;
import com.example.qgchat.db.DBUserItemMsg;
import com.example.qgchat.db.DBUserList;
import com.example.qgchat.socket.ParaseData;
import com.example.qgchat.util.DBUtil;
import com.example.qgchat.util.StateButton;
import com.example.qgchat.util.UltimateBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

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
    private List<DBChatMsg> chatMsgList = new ArrayList<>();
    private UserItemMsg msg;
    private String friend_account;

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
        friend_account = getIntent().getStringExtra("friend_account");
        List<DBUserList> userLists=DataSupport.where("account=?", friend_account).find(DBUserList.class);
        if (!userLists.isEmpty()) {
            setToolbarTitle(userLists.get(0).getUsername());
        }
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
                DBChatMsg msg = new DBChatMsg();
                msg.setContent(myMsg.getText().toString());
                msg.setMsgType(DBChatMsg.TYPE_SENT);
                chatMsgList.add(msg);
                DBUtil.saveChatMsg(friend_account,msg);

                SendMsg sendMsg = new SendMsg();
                sendMsg.setChatObj(friend_account);
                sendMsg.setContent(myMsg.getText().toString());
                sendMsg.setMsgType(SendMsg.TYPE_TEXT);
                myMsg.setText("");
                adapter.notifyItemInserted(chatMsgList.size()-1);
                chatRoomRecycleView.smoothScrollToPosition(chatMsgList.size()-1);
                ParaseData.sendChatMsg(sendMsg);
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(ReceivedMsg receivedMsg) {
        loadData();
        adapter.notifyItemInserted(chatMsgList.size()-1);
        chatRoomRecycleView.smoothScrollToPosition(chatMsgList.size()-1);
    }

    private void initAdapter() {
        loadData();
        adapter = new AdapterChatRoom(chatMsgList);
        chatRoomRecycleView.setAdapter(adapter);
        chatRoomRecycleView.setLayoutManager(new LinearLayoutManager(this));
        chatRoomRecycleView.scrollToPosition(chatMsgList.size()-1);
    }

    private void loadData() {
        List<DBUserItemMsg> dbUserItemMsgs = DataSupport.where("chatObj=?",friend_account).find(DBUserItemMsg.class,true);
        if (!dbUserItemMsgs.isEmpty()) {
            List<DBChatMsg> dbChatMsg=dbUserItemMsgs.get(0).getDbChatMsgList();
            for (DBChatMsg chatMsg : dbChatMsg) {
                chatMsgList.add(chatMsg);
            }
        }
    }

}
