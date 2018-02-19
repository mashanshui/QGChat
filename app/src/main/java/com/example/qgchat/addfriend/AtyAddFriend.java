package com.example.qgchat.addfriend;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.qgchat.R;
import com.example.qgchat.activity.BaseActivity;
import com.example.qgchat.addfriend.fragment.SearchFragment;
import com.example.qgchat.bean.StatusResponse;
import com.example.qgchat.loginAndregister.AtyRegister;
import com.example.qgchat.socket.ParaseData;
import com.example.qgchat.util.EventBean;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.UltimateBar;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AtyAddFriend extends BaseActivity {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.fragment_layout)
    FrameLayout fragmentLayout;
    public String friendAccount = null;
    public String ownerAccount = null;
    private SharedPreferences preferences;
    public static final int REQUEST_SUCCESS = 0;
    public static final int REQUEST_FAIL = 1;
    public static final int INTERNET_FAIL = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissBufferDialog();
            Log.e(TAG, "handleMessage: " + msg.what);
            if (msg.what == REQUEST_SUCCESS) {
                setToast("添加成功");
                finish();
            } else if (msg.what == REQUEST_FAIL) {
                String description = msg.obj.toString();
                setToast(description);
            } else if (msg.what == INTERNET_FAIL) {
                setToast("网络异常");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_add_friend);
        ButterKnife.bind(this);
        setEventbus(true);
        preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
        ownerAccount = preferences.getString("account", null);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimary));
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().executePendingTransactions();
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    finish();
                }
            }
        });
        replaceFragment(new SearchFragment());
        getFragmentManager().executePendingTransactions();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(SearchFragment.SearchFriendEvent searchFriendEvent) {
        if (searchFriendEvent.isExist) {
            friendAccount = searchFriendEvent.friendAccount;
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AtyAddFriend.this);
//        dialogBuilder.setTitle("添加好友");
        dialogBuilder.setMessage("确认添加");
        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showBufferDialog();
//                addFriend();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().addContact(friendAccount, "no reason");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissBufferDialog();
                                    setToast("等待对方验证");
                                }
                            });
                        } catch (final HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissBufferDialog();
                                    setToast(e.getMessage());
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.setCancelable(true);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void addFriend() {
        String url = HttpUtil.addFriendURL + "?ownerAccount=" + ownerAccount + "&friendAccount=" + friendAccount;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = INTERNET_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                String responseData = response.body().string();
                Gson gson = new Gson();
                StatusResponse statusResponse = gson.fromJson(responseData, StatusResponse.class);
                if (statusResponse.getStatus().equals("ok")) {
                    message.what = REQUEST_SUCCESS;
                } else if (statusResponse.getStatus().equals("fail")) {
                    message.what = REQUEST_FAIL;
                    message.obj = statusResponse.getDescription();
                }
                handler.sendMessage(message);
            }
        });
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getSupportFragmentManager().popBackStack();
        } else if (count == 1) {
            finish();
        }
    }
}
