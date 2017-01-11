package com.cooloongwu.coolchat.base;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.entity.NetState;

import org.greenrobot.eventbus.EventBus;

/**
 * 用于检测网络状态的Service
 * Created by CooLoongWu on 2017-1-11 18:15.
 */

public class NetService extends Service {

    private BroadcastReceiver netReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                LogUtils.e("网络状态已经改变");

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable() && info.isConnected()) {
                    String name = info.getTypeName();
                    LogUtils.e("当前网络名称：" + name);
                    EventBus.getDefault().post(new NetState(true));
                    //TODO
                } else {
                    LogUtils.e("没有可用网络");
                    //TODO
                }
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netReceiver, mFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netReceiver);
    }
}
