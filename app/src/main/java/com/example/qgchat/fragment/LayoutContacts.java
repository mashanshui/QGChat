package com.example.qgchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.qgchat.R;
import com.example.qgchat.adapter.ContactsRecycleAdapter;
import com.example.qgchat.bean.UserGroup;
import com.example.qgchat.bean.UserList;
import com.example.qgchat.util.GridItemDecoration;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutContacts extends Fragment {

    @BindView(R.id.contactsRecycleView)
    RecyclerView contactsRecycleView;
    Unbinder unbinder;
    private View rootView;
    private ContactsRecycleAdapter adapter;
    private ArrayList<MultiItemEntity> list;

    public LayoutContacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_contacts, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initAdapter();
        return rootView;
    }

    private void initAdapter() {
        list = generateData();
        adapter = new ContactsRecycleAdapter(list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        contactsRecycleView.setAdapter(adapter);
        contactsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //contactsRecycleView.addItemDecoration(new GridItemDecoration(getActivity(),R.drawable.recycleview_divider));
    }

    private ArrayList<MultiItemEntity> generateData() {
        int lv0Count = 9;
        int lv1Count = 3;

        ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (int i = 0; i < lv0Count; i++) {
            UserGroup group = new UserGroup("MY friend","1","1");
            for (int j = 0; j < lv1Count; j++) {
                group.addSubItem(new UserList("","shanshui"));
            }
            res.add(group);
        }
        return res;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
