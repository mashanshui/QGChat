package com.example.qgchat.loginAndregister;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.qgchat.activity.BaseActivity;
import com.example.qgchat.R;
import com.example.qgchat.loginAndregister.fragment.RegisterFragment1;
import com.example.qgchat.loginAndregister.fragment.RegisterFragment2;
import com.example.qgchat.loginAndregister.fragment.RegisterFragment3;
import com.example.qgchat.loginAndregister.fragment.RegisterFragment4;
import com.example.qgchat.util.UltimateBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AtyRegister extends BaseActivity {
    private static final String TAG = "AtyRegister";
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.fragment_layout)
    FrameLayout fragmentLayout;
    public String phoneNumber = null;
    public String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_register);
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
                } else if (getSupportFragmentManager().getBackStackEntryCount() ==1) {
                    finish();
                }
            }
        });
        replaceFragment(new RegisterFragment1());
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
    public void onMessage(RegisterFragment1.Number number) {
        if (number.isNumberComplete()) {
            RegisterFragment2 fragment2 = new RegisterFragment2();
            phoneNumber=number.getNumber();
            replaceFragment(fragment2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(RegisterFragment2.Code code) {
        if (code.isCode()) {
            RegisterFragment3 fragment3 = new RegisterFragment3();
            replaceFragment(fragment3);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(RegisterFragment3.Password password) {
        this.password = password.getPassword();
        RegisterFragment4 fragment4 = new RegisterFragment4();
        replaceFragment(fragment4);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(RegisterFragment4.Icon icon) {
        Intent intent = new Intent(AtyRegister.this, AtyLogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            getSupportFragmentManager().popBackStack();
        } else if (count ==1) {
            finish();
        }
    }
}
