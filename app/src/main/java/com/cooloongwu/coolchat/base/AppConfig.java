package com.cooloongwu.coolchat.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.apkfuns.logutils.LogUtils;

/**
 * App的各种配置类
 * Created by CooLoongWu on 2016-9-14 13:50.
 */
public class AppConfig {


    private static final String defaultDB = "CoolChat";

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

    /**
     * 获取用户的数据库（为每一个用户建立一个数据库）
     *
     * @param context 上下文
     * @return 数据库名
     */
    public static String getUserDB(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int userId = preferences.getInt(USER_ID, defaultUserId);
        return defaultDB + userId;
    }

    /**
     * 存储获取用户的ID
     *
     * @param context 上下文
     * @param userId  userId
     */
    public static void setUserId(Context context, int userId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(USER_ID, userId).apply();
    }

    public static int getUserId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(USER_ID, defaultUserId);
    }

    /**
     * 存储获取用户的昵称
     *
     * @param context  上下文
     * @param userName 昵称
     */
    public static void setUserName(Context context, String userName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_NAME, userName).apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_NAME, defaultUserName);
    }

    /**
     * 存储获取用户的性别
     *
     * @param context 上下文
     * @param sex     性别
     */
    public static void setUserSex(Context context, String sex) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_SEX, sex).apply();
    }

    public static String getUserSex(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_SEX, defaultUserSex);
    }

    /**
     * 存储获取用户的头像url
     *
     * @param context 上下文
     * @param url     图片地址
     */
    public static void setUserAvatar(Context context, String url) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_AVATAR, url).apply();
    }

    public static String getUserAvatar(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_AVATAR, defaultUserAvatar);
    }

    /**
     * 存储获取用户七牛的token
     *
     * @param context 上下文
     * @param token   token
     */
    public static void setQiniuToken(Context context, String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_TOKEN_QINIU, token).apply();
    }

    public static String getQiniuToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_TOKEN_QINIU, defaultUserQiniuToken);
    }

    /**
     * 存储获取用户趣拍的token
     *
     * @param context 上下文
     * @param token   token
     */
    public static void setQupaiToken(Context context, String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_TOKEN_QUPAI, token).apply();
    }

    public static String getQupaiToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_TOKEN_QUPAI, defaultUserQupaiToken);
    }

    /**
     * 存储获取用户的登录时间
     *
     * @param context 上下文
     * @param time    token
     */
    public static void setUserLoginTime(Context context, long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong(USER_LOGIN_TIME, time).apply();
    }

    public static long getUserLoginTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(USER_LOGIN_TIME, defaultUserLoginTime);
    }

    /**
     * 存储获取用户当前聊天好友或者群组的ID
     *
     * @param context 上下文
     * @param chatId  好友或者群组ID
     */
    public static void setUserCurrentChatId(Context context, int chatId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(USER_CURRENT_CHAT_ID, chatId).apply();
    }

    public static int getUserCurrentChatId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(USER_CURRENT_CHAT_ID, defaultUserCurrentChatId);
    }

    /**
     * 存储获取用户当前聊天好友或者群组的类型
     *
     * @param context  上下文
     * @param chatType 好友或者群组
     */
    public static void setUserCurrentChatType(Context context, String chatType) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(USER_CURRENT_CHAT_TYPE, chatType).apply();
    }

    public static String getUserCurrentChatType(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USER_CURRENT_CHAT_TYPE, defaultUserCurrentChatType);
    }

    /**
     * 清空所有的用户信息
     *
     * @param context 上下文
     */
    public static void clearAllInfo(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().clear().apply();
    }
}
