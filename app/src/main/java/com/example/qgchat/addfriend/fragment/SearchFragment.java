package com.example.qgchat.addfriend.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qgchat.R;
import com.example.qgchat.addfriend.AtyAddFriend;
import com.example.qgchat.bean.StatusResponse;
import com.example.qgchat.loginAndregister.AtyRegister;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.view.StateButton;
import com.example.qgchat.util.StringUtil;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private MaterialEditText edt_number;
    public static final int REQUEST_SUCCESS = 0;
    public static final int REQUEST_FAIL = 1;
    public static final int INTERNET_FAIL=2;
    private String ownerAccount=null;
    private String friendAccount=null;

    public SearchFragment() {
        // Required empty public constructor
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ((AtyAddFriend) getActivity()).dismissBufferDialog();
            if (msg.what == REQUEST_SUCCESS) {
                EventBus.getDefault().post(new SearchFriendEvent(true,friendAccount));
            } else if (msg.what == REQUEST_FAIL) {
                Log.e(TAG, "handleMessage: ");
                String description=msg.obj.toString();
                Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
            } else if (msg.what == INTERNET_FAIL) {
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addfriend_fragment_search, container, false);
        StateButton btn_next= (StateButton) getActivity().findViewById(R.id.btn_next);
        btn_next.setText("下一步");
        ownerAccount=((AtyAddFriend)getActivity()).ownerAccount;
        edt_number = (MaterialEditText) view.findViewById(R.id.edt_number);
        getActivity().findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendAccount = edt_number.getText().toString();
                if (!StringUtil.isEmpty(friendAccount,ownerAccount)) {
                    ((AtyAddFriend) getActivity()).showBufferDialog();
                    //判断是否存在用户
                    checkFriend();
                } else {
                    Toast.makeText(getActivity(), "请输入完整", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void checkFriend() {
        String url=HttpUtil.checkFriendURL+"?ownerAccount="+ownerAccount+"&friendAccount="+friendAccount;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = INTERNET_FAIL;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                String responseData=response.body().string();
                Gson gson=new Gson();
                StatusResponse statusResponse=gson.fromJson(responseData,StatusResponse.class);
                Log.e(TAG, "onResponse: "+statusResponse.toString());
                if (statusResponse.getStatus().equals("ok")) {
                    message.what = REQUEST_SUCCESS;
                } else if (statusResponse.getStatus().equals("fail")) {
                    Log.e(TAG, "onResponse: "+statusResponse.getDescription());
                    message.what = REQUEST_FAIL;
                    message.obj=statusResponse.getDescription();
                }
                handler.sendMessage(message);
            }
        });
    }

    public class SearchFriendEvent{
        public boolean isExist;
        public String friendAccount;

        public SearchFriendEvent(boolean isExist, String friendAccount) {
            this.isExist = isExist;
            this.friendAccount = friendAccount;
        }
    }
}
