package com.cooloongwu.coolchat.utils;

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
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }
}
