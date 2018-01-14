package com.example.qgchat.addfriend.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qgchat.R;
import com.example.qgchat.addfriend.AtyAddFriend;
import com.example.qgchat.loginAndregister.AtyRegister;
import com.example.qgchat.socket.ParaseData;
import com.example.qgchat.util.EventBean;
import com.example.qgchat.util.StateButton;
import com.example.qgchat.util.StringUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.R.attr.button;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private MaterialEditText edt_number;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addfriend_fragment_search, container, false);
        StateButton btn_next= (StateButton) getActivity().findViewById(R.id.btn_next);
        btn_next.setText("下一步");
        edt_number = (MaterialEditText) view.findViewById(R.id.edt_number);
        getActivity().findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edt_number.getText().toString();
                if (!StringUtil.isEmpty(account)) {
                    ((AtyAddFriend) getActivity()).showBufferDialog();
                    //参数为要添加的好友的username和添加理由
                    try {
                        EMClient.getInstance().contactManager().addContact(account, "");
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入完整", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessage(EventBean.SerachFriendEvent serachFriendEvent) {
//        ((AtyAddFriend) getActivity()).dismissBufferDialog();
//        if (serachFriendEvent.isExist()) {
//            EventBus.getDefault().post(new Search(true,edt_number.getText().toString()));
//        } else {
//            Toast.makeText(getActivity(), "用户不存在", Toast.LENGTH_SHORT).show();
//        }
//    }

    public class Search{
        private boolean isSearch;
        private String account;

        public boolean isSearch() {
            return isSearch;
        }

        public void setSearch(boolean search) {
            isSearch = search;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public Search(boolean isSearch, String account) {
            this.isSearch = isSearch;
            this.account = account;
        }
    }
}
