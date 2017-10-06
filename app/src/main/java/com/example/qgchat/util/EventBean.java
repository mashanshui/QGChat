package com.example.qgchat.util;

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
}
