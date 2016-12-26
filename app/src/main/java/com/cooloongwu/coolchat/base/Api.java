package com.cooloongwu.coolchat.base;

import android.content.Context;

import com.cooloongwu.coolchat.utils.AsyncHttpClientUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 软件所需的接口
 * Created by CooLoongWu on 2016-9-26 15:16.
 */

public class Api {
    private static final String URL_LOGIN = "login";
    private static final String URL_FRIENDS = "friends";
    private static final String URL_GROUPS = "groups";
    private static final String URL_GROUP_USERS = "groupUsers";

    /**
     * 登陆的接口
     *
     * @param context  上下文
     * @param userId   用户ID
     * @param password 密码
     * @param handler  处理
     */
    public static void login(Context context, String userId, String password, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.add("userId", userId);
        params.add("password", password);
        AsyncHttpClientUtils.post(context, URL_LOGIN, params, handler);
    }

    /**
     * 得到好友列表
     *
     * @param context 上下文
     * @param userId  用户ID
     * @param handler 处理
     */
    public static void getFriendsList(Context context, int userId, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.add("userId", String.valueOf(userId));
        AsyncHttpClientUtils.post(context, URL_FRIENDS, params, handler);
    }

    /**
     * 得到群组列表
     *
     * @param context 上下文
     * @param userId  用户ID
     * @param handler 处理
     */
    public static void getGroupsList(Context context, int userId, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.add("userId", String.valueOf(userId));
        AsyncHttpClientUtils.post(context, URL_GROUPS, params, handler);
    }

    /**
     * 得到群组用户列表
     *
     * @param context 上下文
     * @param groupId 群组ID
     * @param handler 处理
     */
    public static void getGroupUsersList(Context context, int groupId, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.add("groupId", String.valueOf(groupId));
        AsyncHttpClientUtils.post(context, URL_GROUP_USERS, params, handler);
    }
}
