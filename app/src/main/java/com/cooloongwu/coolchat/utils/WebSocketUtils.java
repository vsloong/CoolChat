package com.cooloongwu.coolchat.utils;

import com.apkfuns.logutils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

/**
 * WebSocket工具类
 * Created by CooLoongWu on 2017-1-16.
 */

public class WebSocketUtils implements WebSocketListener {

    private WebSocket webSocket;
    private JSONObject jsonObject;

    private static WebSocketUtils webSocketUtils = null;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
    }

    @Override
    public void onFailure(IOException e, Response response) {

    }

    @Override
    public void onMessage(ResponseBody message) throws IOException {
        LogUtils.e("MESSAGE>>>>>>>" + String.valueOf(message));
        try {
            jsonObject = new JSONObject(String.valueOf(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPong(Buffer payload) {

    }

    @Override
    public void onClose(int code, String reason) {
        try {
            webSocket.close(1000, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        webSocketUtils = null;
        LogUtils.e("Close:" + code + reason);
    }

    /**
     * 初始化WebSocket服务器
     */
    private void run() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();
        Request request = new Request.Builder().url("ws://120.27.47.125:8283").build();
        client.newCall(request);
        client.dispatcher().executorService().shutdown();
    }


}
