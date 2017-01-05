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
     * @param dpVal   参数
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
