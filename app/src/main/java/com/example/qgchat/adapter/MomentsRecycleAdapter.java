package com.example.qgchat.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.qgchat.R;
import com.example.qgchat.bean.ShowMusicItem;
import com.example.qgchat.bean.UserMoments;

import java.util.List;

/**
 * Created by Administrator on 2017/8/27.
 */

public class MomentsRecycleAdapter extends BaseQuickAdapter<ShowMusicItem,BaseViewHolder> {

    public MomentsRecycleAdapter(@Nullable List<ShowMusicItem> data) {
        super(R.layout.adapter_moments,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShowMusicItem item) {
        helper.setText(R.id.musicName, item.getSongname())
                .setText(R.id.musicArtist, item.getSingername())
                .setText(R.id.musicTime, item.getDuration());
//        if (item.iconURL != null && !item.iconURL.equals("")) {
//            Glide.with(mContext).load(item.iconURL).into((ImageView) helper.getView(R.id.user_headImage));
//        }
    }
}
