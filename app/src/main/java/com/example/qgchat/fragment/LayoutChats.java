package com.example.qgchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qgchat.R;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutChats extends EaseConversationListFragment {
    protected View rootView;
    Unbinder unbinder;

    public LayoutChats() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_chats, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return super.onCreateView(inflater,container,savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        hideTitleBar();
        hideSoftKeyboard();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
