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
import com.example.qgchat.bean.StatusResponse;
import com.example.qgchat.loginAndregister.AtyRegister;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.view.StateButton;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    public static final int REQUEST_SUCCESS = 0;
    public static final int REQUEST_FAIL = 1;

    public RegisterFragment3() {
        // Required empty public constructor
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REQUEST_SUCCESS) {
                ((AtyRegister) getActivity()).dismissBufferDialog();
                EventBus.getDefault().post(new Password(password1));
            } else if (msg.what == REQUEST_FAIL) {
                ((AtyRegister) getActivity()).dismissBufferDialog();
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
                requestRegister(number, password1, username);
            } else {
                Toast.makeText(getActivity(), "密码不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "请输入完整", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestRegister(String number, String password, String username) {
        String url= HttpUtil.registerURL+"?number="+number+"&password="+password+"&username="+username;
        ((AtyRegister) getActivity()).showBufferDialog();
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = REQUEST_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                String responseData=response.body().string();
                Gson gson=new Gson();
                StatusResponse statusResponse=gson.fromJson(responseData,StatusResponse.class);
                if (statusResponse.getCode().equals("200")) {
                    message.what = REQUEST_SUCCESS;
                    handler.sendMessage(message);
                } else if (statusResponse.getCode().equals("400")) {
                    message.what = REQUEST_FAIL;
                    handler.sendMessage(message);
                }
            }
        });
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

}
