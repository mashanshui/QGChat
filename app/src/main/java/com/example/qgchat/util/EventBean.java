package com.example.qgchat.util;

import com.example.qgchat.bean.ShowMusicItem;

import java.util.List;

/**
 * Created by Administrator on 2017/9/15.
 */

public class EventBean {
    public static class RegisterEvent{
        private boolean isRegister;

        public boolean isRegister() {
            return isRegister;
        }

        public void setRegister(boolean register) {
            isRegister = register;
        }

        public RegisterEvent(boolean isRegister) {
            this.isRegister = isRegister;
        }
    }

    public static class SerachFriendEvent{
        private boolean isExist;

        public boolean isExist() {
            return isExist;
        }

        public void setExist(boolean exist) {
            isExist = exist;
        }

        public SerachFriendEvent(boolean isExist) {
            this.isExist = isExist;
        }
    }

    public static class SerachFriendEventTrue{
        private boolean isAdd;

        public SerachFriendEventTrue(boolean isAdd) {
            this.isAdd = isAdd;
        }

        public boolean isAdd() {
            return isAdd;
        }

        public void setAdd(boolean add) {
            isAdd = add;
        }
    }

    public static class MusicListMessage{
        private List<ShowMusicItem> showMusicItems;

        public List<ShowMusicItem> getShowMusicItems() {
            return showMusicItems;
        }

        public void setShowMusicItems(List<ShowMusicItem> showMusicItems) {
            this.showMusicItems = showMusicItems;
        }

        public MusicListMessage(List<ShowMusicItem> showMusicItems) {
            this.showMusicItems = showMusicItems;
        }
    }

    public static class MusicUrl{
        private String musicUrl;
        private String imageUrl;

        public MusicUrl(String musicUrl, String imageUrl) {
            this.musicUrl = musicUrl;
            this.imageUrl = imageUrl;
        }

        public String getMusicUrl() {
            return musicUrl;
        }

        public void setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
