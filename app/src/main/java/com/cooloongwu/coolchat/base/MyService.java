package com.cooloongwu.coolchat.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.cooloongwu.coolchat.R;
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

                    int fromId = jsonObject.getInt("fromId");
                    if (!(fromId == AppConfig.getUserId(MyService.this))) {
                        String fromName = jsonObject.getString("fromName");
                        String fromAvatar = jsonObject.getString("fromName");
                        String content = jsonObject.getString("content");
                        showNotification("", fromName, content);
                    }

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

    /**
     * 通知栏展示消息
     *
     * @param avatar  头像URL
     * @param titile  标题（好友名、群组名）
     * @param content 内容
     */
    private void showNotification(String avatar, String titile, String content) {
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle(titile)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setTicker(titile + "：" + content); //通知首次出现在通知栏，带上升动画效果

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(100, builder.build());
    }

}
