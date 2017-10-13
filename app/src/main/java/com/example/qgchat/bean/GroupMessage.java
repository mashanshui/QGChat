package com.example.qgchat.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/10.
 */

public class GroupMessage {
    /**
     * group_count : 1
     * group : [{"group_name":"我的好友","friend_details":[{"friend_detail":{"friend_account":"","friend_username":"","friend_iconURL":""}}]}]
     */

    private int group_count;
    private List<GroupBean> group;

    public int getGroup_count() {
        return group_count;
    }

    public void setGroup_count(int group_count) {
        this.group_count = group_count;
    }

    public List<GroupBean> getGroup() {
        return group;
    }

    public void setGroup(List<GroupBean> group) {
        this.group = group;
    }

    public static class GroupBean {
        /**
         * group_name : 我的好友
         * friend_details : [{"friend_detail":{"friend_account":"","friend_username":"","friend_iconURL":""}}]
         */

        private String group_name;
        private List<FriendDetailsBean> friend_details;

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public List<FriendDetailsBean> getFriend_details() {
            return friend_details;
        }

        public void setFriend_details(List<FriendDetailsBean> friend_details) {
            this.friend_details = friend_details;
        }

        public static class FriendDetailsBean {
            /**
             * friend_detail : {"friend_account":"","friend_username":"","friend_iconURL":""}
             */

            private FriendDetailBean friend_detail;

            public FriendDetailBean getFriend_detail() {
                return friend_detail;
            }

            public void setFriend_detail(FriendDetailBean friend_detail) {
                this.friend_detail = friend_detail;
            }

            public static class FriendDetailBean {
                /**
                 * friend_account :
                 * friend_username :
                 * friend_iconURL :
                 */

                private String friend_account;
                private String friend_username;
                private String friend_iconURL;

                public String getFriend_account() {
                    return friend_account;
                }

                public void setFriend_account(String friend_account) {
                    this.friend_account = friend_account;
                }

                public String getFriend_username() {
                    return friend_username;
                }

                public void setFriend_username(String friend_username) {
                    this.friend_username = friend_username;
                }

                public String getFriend_iconURL() {
                    return friend_iconURL;
                }

                public void setFriend_iconURL(String friend_iconURL) {
                    this.friend_iconURL = friend_iconURL;
                }
            }
        }
    }
}
