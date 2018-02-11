package com.example.qgchat.addfriend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.example.qgchat.R;
import com.example.qgchat.activity.BaseActivity;
import com.example.qgchat.addfriend.fragment.SearchFragment;
import com.example.qgchat.socket.ParaseData;
import com.example.qgchat.util.EventBean;
import com.example.qgchat.util.UltimateBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AtyAddFriend extends BaseActivity {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.fragment_layout)
    FrameLayout fragmentLayout;
    public String searchAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_add_friend);
        ButterKnife.bind(this);
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
    public void onMessage(SearchFragment.Search search) {
        if (search.isSearch()) {
            searchAccount = search.getAccount();
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
                ParaseData.sendSearchFriendTrue(searchAccount);
            }
        });
        dialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.setCancelable(true);
        AlertDialog dialog=dialogBuilder.create();
        dialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(EventBean.SerachFriendEventTrue search) {
        dismissBufferDialog();
        if (search.isAdd()) {
            setToast("添加成功");
            finish();
        } else {
            setToast("添加失败");
        }
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
