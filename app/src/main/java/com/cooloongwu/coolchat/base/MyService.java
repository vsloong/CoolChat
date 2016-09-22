package com.cooloongwu.coolchat.base;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cooloongwu.coolchat.socket.SocketCallback;
import com.cooloongwu.coolchat.socket.SocketConnect;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 一个Service类
 * Created by CooLoongWu on 2016-9-22 12:32.
 */

public class MyService extends Service {

    private MyBinder myBinder = new MyBinder();
    private SocketConnect socketConnect;

    //只在第一次创建时调用
    @Override
    public void onCreate() {
        super.onCreate();
        initSocket();
    }

    //startService一次则调用一次
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    private void initSocket() {
        socketConnect = new SocketConnect(new SocketCallback() {
            @Override
            public void connected() {
                Log.e("Service Socket", "已连接");
            }

            @Override
            public void receive(byte[] buffer) {
                String strJson = new String(buffer);
                Log.e("Service Socket", "获取的数据：" + strJson);
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    EventBus.getDefault().post(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void disconnect() {
                Log.e("Service Socket", "已断开");
            }
        });
        socketConnect.setRemoteAddress("121.42.187.66", 8282);
        new Thread(socketConnect).start();
    }

    public class MyBinder extends Binder {
        public void sendMessage(JSONObject jsonObject) {
            socketConnect.write((jsonObject.toString() + "\n").getBytes());
        }
    }
}
