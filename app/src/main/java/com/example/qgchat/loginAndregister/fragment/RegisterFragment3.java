package com.example.qgchat.loginAndregister.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qgchat.R;
import com.example.qgchat.loginAndregister.AtyRegister;
import com.example.qgchat.util.EventBean;
import com.example.qgchat.socket.ParaseData;
import com.example.qgchat.util.StateButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment3 extends Fragment {
    private static final String TAG = "RegisterFragment3";

    @BindView(R.id.edt_password)
    MaterialEditText edtPassword;
    @BindView(R.id.edt_reply_password)
    MaterialEditText edtReplyPassword;
    @BindView(R.id.btn_register)
    StateButton btnRegister;
    Unbinder unbinder;
    @BindView(R.id.edt_username)
    MaterialEditText edtUsername;

    private String password1 = null;
    private String password2 = null;
    private String username = null;
    private String number=null;

    public RegisterFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment3, container, false);
        number = ((AtyRegister)getActivity()).phoneNumber;
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        password1 = edtPassword.getText().toString();
        password2 = edtReplyPassword.getText().toString();
        username = edtUsername.getText().toString();
        if (password1 != null && password2 != null && username!=null && !password1.equals("") && !password2.equals("") && !username.equals("")) {
            if (password1.equals(password2)) {
                ((AtyRegister) getActivity()).showBufferDialog();
                ParaseData.requestRegister(number, password1, username);
            } else {
                Toast.makeText(getActivity(), "密码不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "请输入完整", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(EventBean.RegisterEvent registerEvent) {
        ((AtyRegister) getActivity()).dismissBufferDialog();
        if (registerEvent.isRegister()) {
            EventBus.getDefault().post(new Password(password1));
        } else {
            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    public class Password {
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Password(String password) {
            this.password = password;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
