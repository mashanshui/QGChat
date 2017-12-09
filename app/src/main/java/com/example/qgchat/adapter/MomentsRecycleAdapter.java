package com.example.qgchat.adapter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.example.qgchat.R;
import com.example.qgchat.bean.ShowMusicItem;
import com.example.qgchat.util.HttpUtil;
import com.example.qgchat.util.MusicUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/27.
 */

public class MomentsRecycleAdapter extends BaseQuickAdapter<ShowMusicItem,BaseViewHolder> {

    private ImageView imageView;
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            String imageUrl = msg.toString();
            if (imageUrl != null && !imageUrl.equals("")) {
                Glide.with(mContext).load(imageUrl).into(imageView);
            }
        }
    };

    public MomentsRecycleAdapter(@Nullable List<ShowMusicItem> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<ShowMusicItem>() {
            @Override
            protected int getItemType(ShowMusicItem showMusicItem) {
                return showMusicItem.getPlayState();
            }
        });
        getMultiTypeDelegate()
                .registerItemType(ShowMusicItem.STOP, R.layout.adapter_moments)
                .registerItemType(ShowMusicItem.PLAY, R.layout.adapter_moments2);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShowMusicItem item) {
        switch (helper.getItemViewType()) {
            case ShowMusicItem.PLAY:
                helper.setText(R.id.musicName, item.getSongname())
                        .setText(R.id.musicArtist, item.getSingername())
                        .addOnClickListener(R.id.music_control);
                imageView=helper.getView(R.id.musicImage);
                getplayMusic(item.getHash());
                break;
            case ShowMusicItem.STOP:
                int songtime=Integer.parseInt(item.getDuration());
                int second=songtime%60;
                int minute=songtime/60;
                helper.setText(R.id.musicName, item.getSongname())
                        .setText(R.id.musicArtist, item.getSingername())
                        .setText(R.id.musicTime, String.valueOf(minute)+":"+String.valueOf(second));
                break;
        }
    }

    public void getplayMusic(final String hash) {

        String url = "http://m.kugou.com/app/i/getSongInfo.php?hash=" + hash + "&cmd=playInfo";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String[] music = MusicUtil.parsePlayJSON(response.body().string());
                Message message = Message.obtain();
                message.obj=music[1];
                handler.sendMessage(message);
            }
        });
    }
}
