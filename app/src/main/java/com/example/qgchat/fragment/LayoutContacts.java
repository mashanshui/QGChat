package com.example.qgchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qgchat.R;
import com.hyphenate.easeui.ui.EaseContactListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutContacts extends EaseContactListFragment {
    private static final String TAG = "LayoutContacts";
    private View rootView;
    private boolean isFirstLoad = true;

    public LayoutContacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_contacts, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
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
    }
}
