package com.cooloongwu.coolchat.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 单位的转换
 * Created by CooLoongWu on 2017-1-5 17:24.
 */

public class DensityUtils {

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpVal   dp参数
     * @return 转换结果
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxVal   px参数
     * @return 转换结果
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spVal   sp参数
     * @return 转换结果
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxVal   px参数
     * @return 转换结果
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
