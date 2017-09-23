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
}
