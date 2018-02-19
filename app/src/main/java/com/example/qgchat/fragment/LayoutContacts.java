package com.example.qgchat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qgchat.R;
import com.example.qgchat.activity.NewFriendsMsgActivity;
import com.example.qgchat.util.PreferencesUtil;
import com.example.qgchat.view.ContactItemView;
import com.hyphenate.easeui.ui.EaseContactListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutContacts extends EaseContactListFragment {
    private static final String TAG = "LayoutContacts";
    private View rootView;
    private boolean isFirstLoad = true;
    private ContactItemView applicationItem;
    private PreferencesUtil preferencesUtil;

    public LayoutContacts() {
        // Required empty public constructor
    }

    @Override
    protected void initView() {
        super.initView();
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        listView.addHeaderView(headerView);
//        //add loading view
//        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
//        contentContainer.addView(loadingView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_contacts, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void refresh() {
        super.refresh();
        if (preferencesUtil == null) {
            preferencesUtil = new PreferencesUtil(getActivity());
        }
        int unreadMsgCount=preferencesUtil.getUnreadMsgCount();
        if(unreadMsgCount > 0){
            applicationItem.showUnreadMsgView();
        }else{
            applicationItem.hideUnreadMsgView();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        hideTitleBar();
        hideSoftKeyboard();
    }

    protected class HeaderItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.application_item:
                    // 进入申请与通知页面
                    startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
