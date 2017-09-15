package com.example.qgchat.loginAndregister.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qgchat.R;
import com.example.qgchat.loginAndregister.AtyRegister;
import com.example.qgchat.util.StateButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment3 extends Fragment {


    @BindView(R.id.edt_password)
    MaterialEditText edtPassword;
    @BindView(R.id.edt_reply_password)
    MaterialEditText edtReplyPassword;
    @BindView(R.id.btn_register)
    StateButton btnRegister;
    Unbinder unbinder;

    private String password1=null;
    private String password2=null;

    public RegisterFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_fragment3, container, false);
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
        if (password1 != null && password2 != null && !password1.equals("") && !password2.equals("")) {
            if (password1.equals(password2)) {

                EventBus.getDefault().post(new Password(password1));
            } else {
                Toast.makeText(getActivity(), "密码不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "请输入完整", Toast.LENGTH_SHORT).show();
        }
    }

    public class Password{
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
}
