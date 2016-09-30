package com.cooloongwu.coolchat.utils;

import android.content.Context;

import com.cooloongwu.coolchat.base.AppConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转化的工具类
 * Created by CooLoongWu on 2016-9-27 14:28.
 */

public class TimeUtils {

    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 管理是否在有效期内可登录
     *
     * @return 能或者否
     */
    public static boolean canLogin(Context context) {
        long currentTime = System.currentTimeMillis();
        return currentTime <= AppConfig.getUserLoginTime(context) + 3600000;
    }
}
