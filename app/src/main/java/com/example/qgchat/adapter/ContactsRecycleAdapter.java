package com.example.qgchat.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.qgchat.R;
import com.example.qgchat.bean.UserGroup;
import com.example.qgchat.bean.UserList;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public class ContactsRecycleAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_USER_LIST = 0;
    public static final int TYPE_USER_GROUP = 1;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ContactsRecycleAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_USER_GROUP, R.layout.adapter_user_group);
        addItemType(TYPE_USER_LIST, R.layout.adapter_user_list);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_USER_GROUP:
                final UserGroup userGroup= (UserGroup) item;
                helper.setText(R.id.group_name, userGroup.groupName)
                        .setText(R.id.online_user_count, userGroup.onlineUserCount)
                        .setText(R.id.all_user_count, userGroup.allUserCount)
                        .setImageResource(R.id.is_expand, userGroup.isExpanded() ? R.drawable.ic_down_black_24dp : R.drawable.ic_right_black_24dp);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        //Log.d(TAG, "Level 1 item pos: " + pos);
                        if (userGroup.isExpanded()) {
                            collapse(pos, false);
                        } else {
                            expand(pos, false);
                        }
                    }
                });
                break;
            case TYPE_USER_LIST:
                final UserList userList = (UserList) item;
                helper.setText(R.id.item_username, userList.username);
                if (userList.iconURL != null && !userList.iconURL.equals("")) {
                    Glide.with(mContext).load(userList.iconURL).into((ImageView) helper.getView(R.id.item_avatar));
                }
                break;
        }
    }
}
