package com.example.qgchat.loginAndregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qgchat.R;
import com.example.qgchat.activity.AtyMain;
import com.example.qgchat.activity.BaseActivity;
import com.example.qgchat.bean.UserBean;
import com.example.qgchat.socket.LoginEvent;
import com.example.qgchat.socket.ParaseData;
import com.example.qgchat.socket.ServerManager;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.BeanUtil;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.StateButton;
import com.example.qgchat.util.UltimateBar;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.qgchat.service.QGService.account;
import static java.lang.System.load;

public class AtyLogin extends BaseActivity {
    @BindView(R.id.icon)
    CircleImageView icon;
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
    private String account=null;
    private String password=null;

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
                    String url = HttpUtil.getAccountMessageURL + "?account=" + s.toString();
                    HttpUtil.sendOkHttpRequest(url, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result = response.body().string();
                            if (!result.equals("failed")) {
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
        ParaseData.requestLogin(account, password);
    }


    /**
     * 重写BaseAcivity的接收消息的方法，在这个活动中执行下面的逻辑，而不是BaseAcivity的逻辑
     *
     * @param login 登录结果的返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent login) {
        SharedPreferences preferences = getSharedPreferences("qgchat", MODE_PRIVATE);
        /** 是否登陆过，也就是是否有缓存的帐号密码 */
        boolean logined = preferences.getBoolean("login", false);
        if (login.isLogin() && !logined) {
            /**
             * 将是否登录设置为true
             * 将帐号密码保存在本地
             */
            dismissBufferDialog();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("login", true);
            editor.putString("account", account);
            editor.putString("password", password);
            editor.apply();
            serverManager.setAccount(account);
            Intent intent = new Intent(AtyLogin.this, AtyMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            dismissBufferDialog();
            setToast("帐号或密码错误");
        }
    }
}
