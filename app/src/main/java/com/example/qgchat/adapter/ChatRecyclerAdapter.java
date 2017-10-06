package com.example.qgchat.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.qgchat.R;
import com.example.qgchat.bean.UserItemMsg;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ChatRecyclerAdapter extends BaseQuickAdapter<UserItemMsg,BaseViewHolder> {


    public ChatRecyclerAdapter(@Nullable List<UserItemMsg> data) {
        super(R.layout.adapter_item_user,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserItemMsg item) {
        helper.setText(R.id.tv_item_username, item.getUsername())
                .setText(R.id.tv_item_sign, item.getSign());
        if (item.getIconURL() != null && !item.getIconURL().equals("")) {
            Glide.with(mContext).load(item.getIconURL()).into((ImageView) helper.getView(R.id.iv_item_avatar));
        }

    }
}
