package com.example.qgchat.loginAndregister.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qgchat.loginAndregister.AtyRegister;
import com.example.qgchat.R;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.StateButton;
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
public class RegisterFragment1 extends Fragment {
    private static final String TAG = "RegisterFragment1";

    @BindView(R.id.edt_number)
    MaterialEditText edtNumber;
    @BindView(R.id.btn_register)
    StateButton btnRegister;
    Unbinder unbinder;

    public static final int REQUEST_SUCCESS = 0;
    public static final int REQUEST_FAIL = 1;
    private View view;
    private String number=null;

    public RegisterFragment1() {
        // Required empty public constructor
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REQUEST_SUCCESS) {
                ((AtyRegister) getActivity()).dismissBufferDialog();
                EventBus.getDefault().post(new Number(true,number));
            } else if (msg.what == REQUEST_FAIL) {
                ((AtyRegister) getActivity()).dismissBufferDialog();
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_fragment1, container, false);
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
        number=edtNumber.getText().toString();
        if (number!=null && !number.equals("")) {
            String url=HttpUtil.sendCodeURL+"?number="+number;
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
                    String responsedata=response.body().string();
                    if (responsedata.equals("success")) {
                        message.what = REQUEST_SUCCESS;
                        handler.sendMessage(message);
                    } else if (responsedata.equals("fail")) {
                        message.what = REQUEST_FAIL;
                        handler.sendMessage(message);
                    }
                }
            });
        }
    }


    public class Number {
        private boolean numberComplete;
        private String number;

        public Number(boolean numberComplete, String number) {
            this.numberComplete = numberComplete;
            this.number = number;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public boolean isNumberComplete() {
            return numberComplete;
        }

        public void setNumberComplete(boolean numberComplete) {
            this.numberComplete = numberComplete;
        }
    }
}
