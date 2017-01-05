package com.cooloongwu.coolchat.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.activity.ChatActivity;

import java.lang.reflect.Field;

/**
 * 获取手机屏幕的宽高
 * Created by CooLoongWu on 2017-1-5 14:57.
 */

public class DisplayUtils {

    private static final float ROUND_CEIL = 0.5f;
    private static DisplayMetrics displayMetrics;
    private static Resources resources;

    public static void init(Context context) {
        if (displayMetrics == null) {
            resources = context.getResources();
            displayMetrics = context.getResources().getDisplayMetrics();
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @return 宽度（单位：像素）
     */
    public static int getScreenWidth() {
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 高度（单位：像素）
     */
    public static int getScreenHeight() {
        return displayMetrics.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public static int getStatusBarHeight() {
        final int defaultHeightInDp = 19;
        int height = DisplayUtils.dp2px(defaultHeightInDp);
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            height = resources.getDimensionPixelSize(Integer.parseInt(field.get(obj).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * dp 转 px
     *
     * @param dp dp值
     * @return 转换后的像素值
     */
    public static int dp2px(int dp) {
        return (int) (dp * displayMetrics.density + ROUND_CEIL);
    }

    public static int getKeyboardHeight(ChatActivity activity) {
        final int[] test = new int[1];
        final View activityRootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        if (activityRootView != null) {
            ViewTreeObserver viewTreeObserver = activityRootView.getViewTreeObserver();
            if (viewTreeObserver != null) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final Rect r = new Rect();
                        activityRootView.getWindowVisibleDisplayFrame(r);
                        int heightDiff = DisplayUtils.getScreenHeight() - (r.bottom - r.top);
                        LogUtils.e("键盘的高度（工具类）：" + (heightDiff - getStatusBarHeight()));

                        test[0] = heightDiff - getStatusBarHeight();
                    }
                });
            }
        }
        return test[0];
    }
}
