package com.cooloongwu.coolchat.utils;

/**
 * Created by CooLoongWu on 2017-3-14 18:13.
 */

public class ImgUrlUtils {

    public static String getUrl(String str) {
        if (null == str) {
            return "default";
        } else {
            if (str.trim().isEmpty()) {
                return "default";
            } else {
                return str;
            }
        }
    }
}
