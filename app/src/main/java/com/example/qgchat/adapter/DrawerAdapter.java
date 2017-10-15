package com.example.qgchat.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.qgchat.R;
import com.example.qgchat.bean.DrawerList;

import java.util.List;

/**
 * Created by Administrator on 2017/10/14.
 */

public class DrawerAdapter extends BaseQuickAdapter<DrawerList,BaseViewHolder> {


    public DrawerAdapter(@Nullable List<DrawerList> data) {
        super(R.layout.item_drawer_normal, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DrawerList item) {
        helper.setText(R.id.tv,item.getResTitleId())
                .setBackgroundRes(R.id.iv,item.getResIconId());
    }
}
