package com.cooloongwu.coolchat.utils;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.base.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

/**
 * WebSocket工具类
 * Created by CooLoongWu on 2017-1-16.
 */

public class WebSocketUtils extends okhttp3.WebSocketListener {

    private WebSocket webSocket;
    private JSONObject jsonObject;

    private static WebSocketUtils webSocketUtils = null;

    @Override
    public void onOpen(okhttp3.WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        this.webSocket = webSocket;
        LogUtils.e("onOpen");
    }

    @Override
    public void onMessage(okhttp3.WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        LogUtils.e("onMessage" + "String" + text);
    }

    @Override
    public void onMessage(okhttp3.WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        LogUtils.e("onMessage" + "ByteString");
    }

    @Override
    public void onClosing(okhttp3.WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        webSocket.close(1000, null);
        webSocketUtils = null;
        LogUtils.e("onClosing");
    }

    @Override
    public void onClosed(okhttp3.WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        LogUtils.e("onClosed");
    }

    @Override
    public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        LogUtils.e("onFailure");
    }

    /**
     * 初始化WebSocket服务器
     */
    private void run() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        String url = "ws://120.27.47.125:8283";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newWebSocket(request, this);
    }

    /**
     * 获取全局的WebSocketUtils类
     *
     * @return WebSocketUtils
     */
    public static WebSocketUtils getWebSocket() {
        if (webSocketUtils == null) {
            webSocketUtils = new WebSocketUtils();
            webSocketUtils.run();
        }
        return webSocketUtils;
    }

    public boolean sendMessage(String s) {
        return webSocket.send(s);
    }


}
