package com.cooloongwu.coolchat.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 吐司的工具类
 * Created by CooLoongWu on 2016-12-27 15:41.
 */

public class ToastUtils {

    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
