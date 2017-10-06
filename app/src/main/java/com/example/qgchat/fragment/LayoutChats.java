package com.example.qgchat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.qgchat.R;
import com.example.qgchat.adapter.ChatRecyclerAdapter;
import com.example.qgchat.bean.UserItemMsg;
import com.example.qgchat.broadcast.NetworkChangeReceiver;
import com.example.qgchat.util.AccessNetwork;
import com.example.qgchat.util.GridItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.media.CamcorderProfile.get;

/**
 * A simple {@link Fragment} subclass.
 */
public class LayoutChats extends Fragment {
    protected View rootView;
    @BindView(R.id.refresh_recycleView)
    EasyRefreshLayout refreshRecycleView;
    private List<UserItemMsg> userItemMsgList = new ArrayList<>();
    private ChatRecyclerAdapter adapter;
    @BindView(R.id.chatsRecycleView)
    RecyclerView chatsRecycleView;
    Unbinder unbinder;
    private View headview;
    private Intent intent;

    public LayoutChats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout_chats, container, false);
        headview = inflater.inflate(R.layout.recycleview_head_view, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initAdapter();
        initRefresh();
        return rootView;
    }

    private void initRefresh() {
        refreshRecycleView.setLoadMoreModel(LoadModel.NONE);
        refreshRecycleView.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        adapter.setNewData(data);
                        refreshRecycleView.refreshComplete();
                    }
                }, 1000);
            }
        });
    }

    private void initAdapter() {
        loadData();
        adapter = new ChatRecyclerAdapter(userItemMsgList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserItemMsg userItemMsg=userItemMsgList.get(position);
                startActivityForResult(intent,1);
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                return false;
            }
        });
        chatsRecycleView.addItemDecoration(new GridItemDecoration(getActivity(),R.drawable.recycleview_divider));
        chatsRecycleView.setAdapter(adapter);
        chatsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadData() {
        for (int i = 0; i < 12; i++) {
            UserItemMsg userItemMsg = new UserItemMsg();
            //userItemMsg.setIconURL();
            userItemMsg.setUsername("Tony Stark");
            userItemMsg.setSign("You know who I am !");
            userItemMsgList.add(userItemMsg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(NetworkChangeReceiver.NetWorkChange change) {
//        headview=layoutInflater.inflate(R.layout.recycleview_head_view, (ViewGroup) chatsRecycleView.getParent(), false);
        if (change.getNetworkState() == AccessNetwork.INTERNET_NONE) {
            adapter.addHeaderView(headview);
            chatsRecycleView.scrollToPosition(0);   //没有动画的滑动
           // chatsRecycleView.smoothScrollToPosition(0);   //有动画的滑动
        } else {
            adapter.removeHeaderView(headview);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
