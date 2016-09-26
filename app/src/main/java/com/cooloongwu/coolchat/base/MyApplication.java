package com.cooloongwu.coolchat.base;

import android.app.Application;

import com.loopj.android.http.AsyncHttpClient;

/**
 * 自定义Application类
 * Created by CooLoongWu on 2016-9-26 14:53.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        initClient();
    }

    /**
     * 初始化全局静态Client
     */
    private void initClient() {
        Client.setClientGeneral(new AsyncHttpClient());
    }

    public static MyApplication getInstance() {
        return myApplication;
    }
}
