package com.cooloongwu.coolchat.base;

import android.app.Application;

import com.cooloongwu.greendao.gen.DaoSession;
import com.loopj.android.http.AsyncHttpClient;

/**
 * 自定义Application类
 * Created by CooLoongWu on 2016-9-26 14:53.
 */

public class MyApplication extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initClient();
        initGreenDAO();
    }

    /**
     * 初始化全局静态Client
     */
    private void initClient() {
        Client.setClientGeneral(new AsyncHttpClient());
    }

    /**
     * 初始化全局GreenDAO操作
     */
    private void initGreenDAO() {
        GreenDAO.initOpenHelper(this, AppConfig.getUserDB(this));
    }
}
