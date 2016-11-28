package com.cooloongwu.coolchat.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 网络请求类
 * Created by CooLoongWu on 2016-9-26 14:55.
 */

public class AsyncHttpClientUtils {

    private static AsyncHttpClient clientGeneral;
    private static final int CONNECT_TIME = 8 * 1000;
    private static final int RESPONSE_TIME = 10 * 1000;

    public static final String SERVER_IP = "120.27.47.125";
    public static final int SERVER_PORT = 8282;
    public static final int SERVER_PORT_WEBSOCKET = 8283;

    private static final String SERVER_HTTP = "http://" + SERVER_IP;
    private static final String TEST_SERVER = SERVER_HTTP + "/coolchat/";          //APP接口测试服务器
    private static final String OFFICIAL_SERVER = SERVER_HTTP + "/coolchat/";      //APP接口正式服务器

    private static String BASE_SERVER = OFFICIAL_SERVER;

    /**
     * 设置静态Client
     *
     * @param client 客户端
     */
    public static void setClientGeneral(AsyncHttpClient client) {
        clientGeneral = client;
        clientGeneral.setConnectTimeout(CONNECT_TIME);
        clientGeneral.setResponseTimeout(RESPONSE_TIME);
    }

    /**
     * get方法
     *
     * @param context 上下文
     * @param url     接口
     * @param handler 处理
     */
    public static void get(Context context, String url, AsyncHttpResponseHandler handler) {
        clientGeneral.get(context, BASE_SERVER + url, handler);
    }

    /**
     * post方法
     *
     * @param context 上下文
     * @param url     接口地址
     * @param params  参数
     * @param handler 处理
     */
    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler handler) {
        clientGeneral.post(context, BASE_SERVER + url, params, handler);
    }

}
