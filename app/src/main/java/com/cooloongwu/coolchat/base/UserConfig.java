package com.cooloongwu.coolchat.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 用户的信息类等配置类
 * Created by CooLoongWu on 2016-12-21 16:15.
 */

public class UserConfig {

    private int userId;
    private String userName;
    private String userSex;
    private String userAvatar;
    private String userTokenOfQiniu;
    private String userTokenOfQupai;
    private long userLoginTime;
    private int userCurrentChatId;
    private String userCurrentChatType;

    /**
     * 存储修改用户id，默认为空
     */
    private static final String USER_ID = "user_id";
    private static final int defaultUserId = 0;

    /**
     * 存储修改用户昵称，默认为空
     */
    private static final String USER_NAME = "user_name";
    private static final String defaultUserName = "";

    /**
     * 存储修改用户性别，默认为空
     */
    private static final String USER_SEX = "user_sex";
    private static final String defaultUserSex = "";

    /**
     * 存储修改用户头像，默认为空
     */
    private static final String USER_AVATAR = "user_avatar";
    private static final String defaultUserAvatar = "";

    /**
     * 存储修改用户七牛云Token默认为空
     */
    private static final String USER_TOKEN_QINIU = "user_token_qiniu";
    private static final String defaultUserQiniuToken = "";

    /**
     * 存储修改用户趣拍云Token默认为空
     */
    private static final String USER_TOKEN_QUPAI = "user_token_qupai";
    private static final String defaultUserQupaiToken = "";

    /**
     * 存储修改用户登录时间
     */
    private static final String USER_LOGIN_TIME = "user_login_time";
    private static final long defaultUserLoginTime = 0;

    /**
     * 存储修改用户当前聊天对象的ID
     */
    private static final String USER_CURRENT_CHAT_ID = "user_current_chat_id";
    private static final int defaultUserCurrentChatId = 0;

    /**
     * 存储修改用户当前聊天对象的类型（群组还是好友）
     */
    private static final String USER_CURRENT_CHAT_TYPE = "user_current_chat_type";
    private static final String defaultUserCurrentChatType = "";

    private UserConfig(Builder builder) {
        userId = builder.userId;
        userName = builder.userName;
        userSex = builder.userSex;
        userAvatar = builder.userAvatar;
        userTokenOfQiniu = builder.userTokenOfQiniu;
        userTokenOfQupai = builder.userTokenOfQupai;
        userLoginTime = builder.userLoginTime;
        userCurrentChatId = builder.userCurrentChatId;
        userCurrentChatType = builder.userCurrentChatType;
    }

    public static class Builder {
        private int userId;
        private String userName;
        private String userSex;
        private String userAvatar;
        private String userTokenOfQiniu;
        private String userTokenOfQupai;
        private long userLoginTime;
        private int userCurrentChatId;
        private String userCurrentChatType;

        private SharedPreferences preferences;

        public Builder(Context context) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        public Builder setUserId(int userId) {
            this.userId = userId;
            preferences.edit().putInt(USER_ID, userId).apply();
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            preferences.edit().putString(USER_NAME, userName).apply();
            return this;
        }

        public Builder setUserSex(String userSex) {
            this.userSex = userSex;
            preferences.edit().putString(USER_SEX, userSex).apply();
            return this;
        }

        public Builder setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
            preferences.edit().putString(USER_AVATAR, userAvatar).apply();
            return this;
        }

        public Builder setUserTokenOfQiniu(String userTokenOfQiniu) {
            this.userTokenOfQiniu = userTokenOfQiniu;
            preferences.edit().putString(USER_TOKEN_QINIU, userTokenOfQiniu).apply();
            return this;
        }

        public Builder setUserTokenOfQupai(String userTokenOfQupai) {
            this.userTokenOfQupai = userTokenOfQupai;
            preferences.edit().putString(USER_TOKEN_QUPAI, userTokenOfQupai).apply();
            return this;
        }

        public Builder setUserLoginTime(long userLoginTime) {
            this.userLoginTime = userLoginTime;
            preferences.edit().putLong(USER_LOGIN_TIME, userLoginTime).apply();
            return this;
        }

        public Builder setUserCurrentChatId(int userCurrentChatId) {
            this.userCurrentChatId = userCurrentChatId;
            preferences.edit().putInt(USER_CURRENT_CHAT_ID, userCurrentChatId).apply();
            return this;
        }

        public Builder setUserCurrentChatType(String userCurrentChatType) {
            this.userCurrentChatType = userCurrentChatType;
            preferences.edit().putString(USER_CURRENT_CHAT_TYPE, userCurrentChatType).apply();
            return this;
        }

        public UserConfig build() {
            return new UserConfig(Builder.this);
        }
    }


}
