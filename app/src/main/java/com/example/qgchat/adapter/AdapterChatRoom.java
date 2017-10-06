package com.example.qgchat.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.qgchat.R;
import com.example.qgchat.db.DBChatMsg;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */

public class AdapterChatRoom extends BaseQuickAdapter<DBChatMsg,BaseViewHolder> {

    public AdapterChatRoom(@Nullable List<DBChatMsg> data) {
        super(R.layout.adapter_chat_room,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DBChatMsg item) {
        if (item.getMsgType() == DBChatMsg.TYPE_RECEIVED) {
            helper.getView(R.id.send_layout).setVisibility(View.GONE);
            helper.getView(R.id.received_layout).setVisibility(View.VISIBLE);
            helper.setText(R.id.received_msg, item.getContent());
        } else if (item.getMsgType() == DBChatMsg.TYPE_SENT) {
            helper.getView(R.id.send_layout).setVisibility(View.VISIBLE);
            helper.getView(R.id.received_layout).setVisibility(View.GONE);
            helper.setText(R.id.send_msg, item.getContent());
        }
    }
}
