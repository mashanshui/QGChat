package com.example.qgchat.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.qgchat.R;
import com.example.qgchat.bean.UserMoments;

import java.util.List;

/**
 * Created by Administrator on 2017/8/27.
 */

public class MomentsRecycleAdapter extends BaseQuickAdapter<UserMoments,BaseViewHolder> {

    public MomentsRecycleAdapter(@Nullable List<UserMoments> data) {
        super(R.layout.adapter_moments,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserMoments item) {
        helper.setText(R.id.user_name, item.username)
                .setText(R.id.send_time, item.sendTime)
                .setText(R.id.share_content, item.shareContent);

        if (item.iconURL != null && !item.iconURL.equals("")) {
            Glide.with(mContext).load(item.iconURL).into((ImageView) helper.getView(R.id.user_headImage));
        }
    }
}
