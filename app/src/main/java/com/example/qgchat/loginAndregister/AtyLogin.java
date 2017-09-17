package com.example.qgchat.loginAndregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.qgchat.AtyMain;
import com.example.qgchat.BaseActivity;
import com.example.qgchat.R;
import com.example.qgchat.server.LoginEvent;
import com.example.qgchat.server.ParaseData;
import com.example.qgchat.server.ServerManager;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.StateButton;
import com.example.qgchat.util.UltimateBar;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AtyLogin extends BaseActivity {
    private static final String TAG = "AtyLogin";
    private ServerManager serverManager = ServerManager.getServerManager();
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.btn_login)
    StateButton btnLogin;
    @BindView(R.id.edt_account)
    MaterialEditText edtAccount;
    @BindView(R.id.edt_password)
    MaterialEditText edtPassword;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.register)
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_login);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setColorBar(ContextCompat.getColor(this, R.color.colorPrimary));
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @OnClick({R.id.btn_login, R.id.forget_password, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (AccessNetwork.getNetworkState(this) != AccessNetwork.INTERNET_NONE) {
                    showBufferDialog();
                    login();
                } else {
                    setToast("找不到可用网络");
                }
                break;
            case R.id.forget_password:
                break;
            case R.id.register:
                register();
                break;
        }
    }

    private void register() {
        Intent intent = new Intent(AtyLogin.this, AtyRegister.class);
        startActivity(intent);
    }

    private void login() {
        account = edtAccount.getText().toString();
        password = edtPassword.getText().toString();
        ParaseData.requestLogin(account, password);
    }


    /**
     * 重写BaseAcivity的接收消息的方法，在这个活动中执行下面的逻辑，而不是BaseAcivity的逻辑
     * @param login 登录结果的返回
     */
    @Override
    public void onMessageEvent(LoginEvent login) {
        if (login.isLogin()) {
            /**
             * 将是否登录设置为true
             * 将帐号密码保存在本地
             */
            dismissBufferDialog();
            SharedPreferences preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("login", true);
            editor.putString("account", account);
            editor.putString("password", password);
            editor.apply();
            serverManager.setAccount(account);
            Intent intent = new Intent(AtyLogin.this, AtyMain.class);
            startActivity(intent);
        } else {
            setToast("帐号或密码错误");
        }
    }
}
