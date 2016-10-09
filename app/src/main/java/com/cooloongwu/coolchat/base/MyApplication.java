package com.cooloongwu.coolchat.base;

import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;

/**
 * 自定义Application类
 * Created by CooLoongWu on 2016-9-26 14:53.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initClient();
    }

    /**
     * 初始化全局静态Client
     */
    private void initClient() {
        Client.setClientGeneral(new AsyncHttpClient());
    }
}
