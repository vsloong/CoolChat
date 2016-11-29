package com.cooloongwu.qupai;

import android.content.Context;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.base.AppConfig;
import com.duanqu.qupai.auth.AuthService;
import com.duanqu.qupai.auth.QupaiAuthListener;

/**
 * 趣拍云的配置
 * Created by CooLoongWu on 2016-10-14 09:10.
 */

public class QupaiAuth {

    private static QupaiAuth instance;

    public static QupaiAuth getInstance() {
        if (instance == null) {
            instance = new QupaiAuth();
        }
        return instance;
    }

    /**
     * 鉴权 建议只调用一次,在demo里面为了测试调用了多次 得到accessToken，通常一个用户对应一个token
     *
     * @param context 上下文
     */
    public void initAuth(final Context context) {
        AuthService service = AuthService.getInstance();
        service.setQupaiAuthListener(new QupaiAuthListener() {
            @Override
            public void onAuthError(int errorCode, String message) {
                LogUtils.e("趣拍云认证失败：" + "ErrorCode" + errorCode + "；message" + message);
            }

            @Override
            public void onAuthComplte(int responseCode, String responseMessage) {
                LogUtils.e("趣拍云认证成功：" + "responseCode" + responseCode + "；message" + responseMessage);
                AppConfig.setQupaiToken(context, responseMessage);
            }
        });
        String appKey = "20c8241fb3e0c95";
        String appSecret = "149edead851b4331a0eb4207542a9a3e";
        String space = String.valueOf(AppConfig.getUserId(context));
        service.startAuth(context, appKey, appSecret, space);
    }
}
