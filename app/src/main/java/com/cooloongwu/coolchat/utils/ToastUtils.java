package com.cooloongwu.coolchat.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.cooloongwu.coolchat.R;
import com.tapadoo.alerter.Alerter;

/**
 * 吐司的工具类
 * Created by CooLoongWu on 2016-12-27 15:41.
 */

public class ToastUtils {

    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String message) {
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Alerter.create((Activity) context)
                .setText(message)
                .setBackgroundColor(R.color.colorAccent)
                .setDuration(1500)
                .show();
    }
}
