package com.cooloongwu.coolchat.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * App的各种配置类
 * Created by CooLoongWu on 2016-9-14 13:50.
 */
public class AppConfig {
    public static String DB_NAME = "CoolChat";

    /**
     * 存储修改用户id，默认为空
     */
    public static final String USER_ID = "user_id";
    public static final int defaultUserId = 0;

    /**
     * 存储修改用户昵称，默认为空
     */
    public static final String USER_NAME = "user_name";
    public static final String defaultUserName = "";

    /**
     * 存储修改用户性别，默认为空
     */
    public static final String USER_SEX = "user_sex";
    public static final String defaultUserSex = "";

    /**
     * 存储修改用户头像，默认为空
     */
    public static final String USER_AVATAR = "user_avatar";
    public static final String defaultUserAvatar = "";


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
     * 清空所有的用户信息
     *
     * @param context 上下文
     */
    public static void clearAllInfo(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().clear().apply();
    }
}
