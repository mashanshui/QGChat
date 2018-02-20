package com.example.qgchat.addfriend.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qgchat.R;
import com.example.qgchat.addfriend.AtyAddFriend;
import com.example.qgchat.view.StateButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment2 extends Fragment {
    private MaterialEditText edt_message;

    public SearchFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.addfriend_fragment_search2, container, false);
        edt_message = view.findViewById(R.id.edt_message);
        StateButton btn_next= (StateButton) getActivity().findViewById(R.id.btn_next);
        btn_next.setText("下一步");
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AtyAddFriend) getActivity()).hideSoftKeyboard();
                String message = edt_message.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    EventBus.getDefault().post(new SearchMessage(message));
                } else {
                    Toast.makeText(getActivity(), "请输入完整", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public class SearchMessage{
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public SearchMessage(String message) {
            this.message = message;
        }
    }

}
