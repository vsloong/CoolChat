package com.cooloongwu.coolchat.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.MyApplication;

import java.lang.reflect.Field;

/**
 * 获取手机屏幕的宽高
 * Created by CooLoongWu on 2017-1-5 14:57.
 */

public class DisplayUtils {

    private static final float ROUND_CEIL = 0.5f;

    /**
     * 获取屏幕宽度
     *
     * @return 宽度（单位：像素）
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 高度（单位：像素）
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        final int defaultHeightInDp = 19;
        int height = DisplayUtils.dp2px(context, defaultHeightInDp);
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            height = context.getResources().getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * dp 转 px
     *
     * @param dpVal dp值
     * @return 转换后的像素值
     */
    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static void detectKeyboardHeight(final Context context) {
        Activity activity = (Activity) context;
        final View activityRootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        if (activityRootView != null) {
            ViewTreeObserver viewTreeObserver = activityRootView.getViewTreeObserver();
            if (viewTreeObserver != null) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final Rect r = new Rect();
                        activityRootView.getWindowVisibleDisplayFrame(r);
                        int heightDiff = DisplayUtils.getScreenHeight(context) - (r.bottom - r.top);
                        if (heightDiff - getStatusBarHeight(context) > 0) {
                            AppConfig.setKeyboardHeight(MyApplication.context(),
                                    heightDiff - getStatusBarHeight(context));
                        }
                    }
                });
            }
        }
    }
}
