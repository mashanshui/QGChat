package com.example.qgchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qgchat.R;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatRoomFragment extends EaseChatFragment {


    public ChatRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        hideTitleBar();
    }

}
