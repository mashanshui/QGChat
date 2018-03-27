package com.example.qgchat.loginAndregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qgchat.R;
import com.example.qgchat.activity.AtyMain;
import com.example.qgchat.activity.BaseActivity;
import com.example.qgchat.bean.UserBean;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.BeanUtil;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.UltimateBar;
import com.example.qgchat.view.ClearWriteEditText;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AtyLogin extends BaseActivity {
    @BindView(R.id.edt_account)
    ClearWriteEditText edtAccount;
    @BindView(R.id.edt_password)
    ClearWriteEditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.icon)
    ImageView icon;

    private String account = null;
    private String password = null;
    private SharedPreferences preferences;
    private static final int LOGIN_SUCCESS = 0;
    private static final int LOGIN_FAIL = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOGIN_SUCCESS) {
                start();
                finish();
            } else if (msg.what == LOGIN_FAIL) {
                dismissBufferDialog();
                setToast("帐号或密码错误");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_login);
        preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar();
        ButterKnife.bind(this);
        edtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11 && AccessNetwork.getNetworkState(AtyLogin.this) != AccessNetwork.INTERNET_NONE) {
                    String url = HttpUtil.getUserMessageURL + "?account=" + s.toString();
                    HttpUtil.sendOkHttpRequest(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result = response.body().string();
                            if (!TextUtils.isEmpty(result)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserBean bean = BeanUtil.handleUserBeanResponse(result);
                                        if (bean.getIconURL() != null) {
                                            Glide.with(AtyLogin.this).load(bean.getIconURL()).diskCacheStrategy(DiskCacheStrategy.NONE).into(icon);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
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
        EMClient.getInstance().login(account, password, new EMCallBack() {

            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().groupManager().loadAllGroups();
                handler.sendEmptyMessage(LOGIN_SUCCESS);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String error) {
                handler.sendEmptyMessage(LOGIN_FAIL);
            }
        });
    }

    private void start() {
        /**
         * 将是否登录设置为true
         * 将帐号密码保存在本地
         */
        dismissBufferDialog();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("account", account);
        editor.putString("password", password);
        editor.apply();
        Intent intent = new Intent(AtyLogin.this, AtyMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
