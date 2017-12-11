package com.example.qgchat.util;

import com.example.qgchat.bean.ShowMusicItem;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/7.
 */

public class MusicUtil {
    /**
     * @param keyword 当用户查询的时候加载显示的信息
     */
    public static void getShowMusic(final String keyword) {
        String u = null;
        try {
            u = new String(keyword.getBytes(), "utf-8");
            u = URLEncoder.encode(u, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://mobilecdn.kugou.com/api/v3/search/song?iscorrect=1&showtype=14&tag=1&version=8415&keyword=" + u + "&highlight=em&plat=0&sver=5&correct=1&page=1&pagesize=20&with_res_tag=1";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<ShowMusicItem> musicItems = parseShowJSON(response.body().string());
                EventBus.getDefault().post(new EventBean.MusicListMessage(musicItems));
            }
        });
    }

    public static List<ShowMusicItem> parseShowJSON(String response) {
        List<ShowMusicItem> musicItems = new ArrayList<>();
        try {
            response = response.replace("<!--KG_TAG_RES_START-->", "");
            response = response.replace("<!--KG_TAG_RES_END-->", "");
            //Log.i("info", response);
            //将服务器返回的数据传入到一个JSONArray对象中
            JSONObject start1 = new JSONObject(response);
            JSONObject start2 = start1.getJSONObject("data");
            JSONArray jsonArray = start2.getJSONArray("info");
            //遍历这个数组
            for (int i = 0; i < jsonArray.length(); i++) {
                //从JsonArray中取出JSONObject对象
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //获取JSONObject对象中的数据
                String songname = jsonObject.getString("songname");
                String singername = jsonObject.getString("singername");
                String duration = jsonObject.getString("duration");
                String hash = jsonObject.getString("hash");
                songname = songname.replace("<em>", "");
                songname = songname.replace("</em>", "");
                ShowMusicItem showItem = new ShowMusicItem();
                showItem.setSongname(songname);
                showItem.setSingername(singername);
                showItem.setDuration(duration);
                showItem.setHash(hash);
                musicItems.add(showItem);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return musicItems;
    }


    /**
     * @param hash 当用户点击歌曲的时候进行播放
     *             也就是获取歌曲的链接
     */
    public static void getplayMusic(final String hash) {

        String url = "http://m.kugou.com/app/i/getSongInfo.php?hash=" + hash + "&cmd=playInfo";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String[] music = parsePlayJSON(response.body().string());
                EventBus.getDefault().post(new EventBean.MusicUrl(music[0], music[1]));
            }
        });
    }


    public static String[] parsePlayJSON(String response) {
        String musicUrl = null;
        String imageUrl = null;
        try {
            response = response.replace("<!--KG_TAG_RES_START-->", "");
            response = response.replace("<!--KG_TAG_RES_END-->", "");
            //Log.i("info", response);
            //将服务器返回的数据传入到一个JSONArray对象中
            JSONObject object = new JSONObject(response);
            musicUrl = object.getString("url");
            imageUrl = object.getString("imgUrl");
            String imageSize = "200";
            imageUrl = imageUrl.replace("{size}",imageSize);
            //Log.i("info", musicUrl+"   "+ImageUrl);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new String[]{musicUrl, imageUrl};
    }

}
