package com.cooloongwu.coolchat.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.entity.Chat;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.coolchat.utils.ImgUrlUtils;
import com.cooloongwu.greendao.gen.ChatDao;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * 一个Service类
 * Created by CooLoongWu on 2016-9-22 12:32.
 */

public class MyService extends Service {

    public static MyBinder myBinder = new MyBinder();
    private BroadcastReceiver connectionReceiver;
    private static boolean isClosed = true;
    private static WebSocketConnection webSocketConnection;
    private static WebSocketOptions options = new WebSocketOptions();

    //只在第一次创建时调用
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //startService一次则调用一次
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (connectionReceiver == null) {
            connectionReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo == null || !networkInfo.isAvailable()) {
                        Toast.makeText(getApplicationContext(), "网络已断开，请重新连接", Toast.LENGTH_SHORT).show();
                    } else {
                        if (webSocketConnection != null) {
                            webSocketConnection.disconnect();
                        }
                        if (isClosed) {
                            webSocketConnect();
                        }
                    }

                }
            };

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectionReceiver, intentFilter);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public static void closeWebsocket(boolean exitApp) {
        if (webSocketConnection != null && webSocketConnection.isConnected()) {
            webSocketConnection.disconnect();
            webSocketConnection = null;
        }
    }

    public void webSocketConnect() {
        webSocketConnection = new WebSocketConnection();
        try {
            String websocketHost = "ws://120.27.47.125:8283";
            webSocketConnection.connect(websocketHost, new WebSocketHandler() {

                //websocket启动时候的回调
                @Override
                public void onOpen() {
                    isClosed = false;
                    sendLoginMsg();
                }

                //websocket接收到消息后的回调
                @Override
                public void onTextMessage(String str) {
                    LogUtils.e("WebSocket消息：" + str);
                    handleMsg(str);
                }

                //websocket关闭时候的回调
                @Override
                public void onClose(int code, String reason) {
                    isClosed = true;
                    closeWebsocket(false);
                    webSocketConnect();

                    switch (code) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3://手动断开连接
//                            if (!isExitApp) {
//                                webSocketConnect();
//                            }
                            break;
                        case 4:
                            break;
                        /**
                         * 由于我在这里已经对网络进行了判断,所以相关操作就不在这里做了
                         */
                        case 5://网络断开连接
//                            closeWebsocket(false);
//                            webSocketConnect();
                            break;
                    }
                }
            }, options);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private void handleMsg(String strJson) {
        try {
            JSONObject jsonObject = new JSONObject(strJson);

            String type = jsonObject.getString("type");
            //广播到聊天相关页面
            if ("say".equals(type)) {
                //广播到主页面以及聊天页面更新信息
                EventBus.getDefault().post(jsonObject);

                /**
                 * 如果toWhich是friend，那么toId可能是自己的ID（朋友发的消息）或者朋友的Id（自己发的消息）
                 *                    那么fromId可能是自己的ID（自己发的消息）或者朋友的ID（朋友发的消息）
                 *
                 * 如果toWhich是group，那么toId就是群组的Id
                 *                   那么fromId可能是自己的ID（自己发的消息）或者群组中其他人的ID（群组中其他人发的消息）
                 */
                String toWhich = jsonObject.getString("toWhich");   //可能是friend或者group
                int toId = jsonObject.getInt("toId");             //可能是我自己的ID或者对方的ID
                int fromId = jsonObject.getInt("fromId");         //可能是我自己的ID或者对方的ID

                String fromAvatar = jsonObject.getString("fromAvatar");
                String fromName = jsonObject.getString("fromName");
                String content = jsonObject.getString("content");
                String contentType = jsonObject.getString("contentType");
                String time = jsonObject.getString("time");

                //把聊天数据保存
                Chat chat = new Chat();
                chat.setFromId(fromId);
                chat.setChatType(toWhich);
                chat.setFromAvatar(fromAvatar);
                chat.setFromName(fromName);
                chat.setContent(content);
                chat.setContentType(contentType);
                chat.setToId(toId);
                chat.setTime(time);
                //chatFriend.setIsRead(false);            //消息是否已读
                if ("audio".equals(contentType)) {
                    chat.setAudioLength(jsonObject.getString("audioLength"));
                }
                //保存聊天数据到本地数据库
                saveChatData(chat);

                //展示通知(如果不是自己发的也不是当前聊天的人或群组发的就提示)
                if (!(fromId == AppConfig.getUserId(MyService.this)
                        || fromId == AppConfig.getUserCurrentChatId(MyService.this))) {
                    switch (contentType) {
                        case "text":
                            showNotification(fromAvatar, fromName, content);
                            break;
                        case "image":
                            showNotification(fromAvatar, fromName, "[图片]");
                            break;
                        case "audio":
                            showNotification(fromAvatar, fromName, "[语音]");
                            break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class MyBinder extends Binder {
        public void sendMessage(JSONObject jsonObject) {
            if (!isClosed) {
                if (webSocketConnection.isConnected()) {
                    webSocketConnection.sendTextMessage(jsonObject.toString());
                }
            }

        }
    }

    /**
     * 告诉服务器我上线了
     */
    private void sendLoginMsg() {
        //发送数据示例
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "login");
            jsonObject.put("fromId", AppConfig.getUserId(this));
            jsonObject.put("toWhich", "server");
            myBinder.sendMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知栏展示消息
     *
     * @param avatar  头像URL
     * @param title   标题（好友名、群组名）
     * @param content 内容
     */
    private void showNotification(String avatar, String title, String content) {
        LogUtils.d("通知栏提示", content);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_conversation);
        remoteViews.setImageViewResource(R.id.conversation_avatar, R.mipmap.ic_launcher);

        final int NOTIFICATION_ID = 1993;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // Needed for the notification to work/show!!
                .setContentTitle(title)
                .setContentText(content)
                //.setContent(remoteViews)
                .setDefaults(Notification.DEFAULT_ALL);

//        if (Build.VERSION.SDK_INT > 16) {
//            notification.bigContentView = remoteViews;
//        }

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

        Picasso.with(this).load(ImgUrlUtils.getUrl(avatar))
                .into(remoteViews, R.id.conversation_avatar, NOTIFICATION_ID, notification);

    }

    private void saveChatData(Chat chat) {
        ChatDao chatDao = GreenDAOUtils.getInstance(this).getChatDao();
        chatDao.insert(chat);
    }


}
