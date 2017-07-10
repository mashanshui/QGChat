package com.example.qgchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AtyLoginOrRegister extends AppCompatActivity {

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
        setContentView(R.layout.activity_aty_login_or_register);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @OnClick({R.id.btn_login, R.id.forget_password, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Intent intent = new Intent(AtyLoginOrRegister.this, AtyMain.class);
                startActivity(intent);
                break;
            case R.id.forget_password:
                break;
            case R.id.register:
                break;
        }
    }
}
