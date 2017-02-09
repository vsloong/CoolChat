package com.cooloongwu.coolchat.utils;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.MyService;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 发送消息的工具类
 * Created by CooLoongWu on 2017-2-9 17:32.
 */

public class SendMessageUtils {

    /**
     * 发送文字消息
     */
    public static void sendTextMessage(Context context, String msg) {
        //发送数据示例
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "say");
            jsonObject.put("fromId", AppConfig.getUserId(context));
            jsonObject.put("fromName", AppConfig.getUserName(context));
            jsonObject.put("fromAvatar", AppConfig.getUserAvatar(context));
            jsonObject.put("toWhich", AppConfig.getUserCurrentChatType(context));
            jsonObject.put("toId", AppConfig.getUserCurrentChatId(context));
            jsonObject.put("content", msg);
            jsonObject.put("contentType", "text");
            jsonObject.put("time", TimeUtils.getCurrentTime());

            MyService.myBinder.sendMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片消息
     */
    public static void sendImageMessage(final Context context, File file) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(
                file, //文件
                null, //文件名
                AppConfig.getQiniuToken(context),//token
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置。res中的key就是资源的名字
                        LogUtils.e("七牛云", key + ",\r\n " + info + ",\r\n " + res);

                        //发送数据示例
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("type", "say");
                            jsonObject.put("fromId", AppConfig.getUserId(context));
                            jsonObject.put("fromName", AppConfig.getUserName(context));
                            jsonObject.put("fromAvatar", AppConfig.getUserAvatar(context));
                            jsonObject.put("toWhich", AppConfig.getUserCurrentChatType(context));
                            jsonObject.put("toId", AppConfig.getUserCurrentChatId(context));
                            jsonObject.put("content", "http://oe98z0mhz.bkt.clouddn.com/" + res.getString("key"));
                            jsonObject.put("contentType", "image");
                            jsonObject.put("time", TimeUtils.getCurrentTime());

                            MyService.myBinder.sendMessage(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
    }

    /**
     * 发送语音消息
     */
    public static void sendAudioMessage(final Context context, File file, final String audioLength) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(
                file, //文件
                null, //文件名
                AppConfig.getQiniuToken(context),//token
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置。res中的key就是资源的名字
                        LogUtils.e("七牛云，语音", key + ",\r\n " + info + ",\r\n " + res);

                        //发送数据示例
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("type", "say");
                            jsonObject.put("fromId", AppConfig.getUserId(context));
                            jsonObject.put("fromName", AppConfig.getUserName(context));
                            jsonObject.put("fromAvatar", AppConfig.getUserAvatar(context));
                            jsonObject.put("toWhich", AppConfig.getUserCurrentChatType(context));
                            jsonObject.put("toId", AppConfig.getUserCurrentChatId(context));
                            jsonObject.put("content", "http://oe98z0mhz.bkt.clouddn.com/" + res.getString("key"));
                            jsonObject.put("contentType", "audio");
                            jsonObject.put("audioLength", audioLength);
                            jsonObject.put("time", TimeUtils.getCurrentTime());

                            MyService.myBinder.sendMessage(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
    }

    /**
     * 发送视频消息
     */
    public static void sendVideoMessage(Context context, String videoPath) {
        //发送数据示例
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "say");
            jsonObject.put("fromId", AppConfig.getUserId(context));
            jsonObject.put("fromName", AppConfig.getUserName(context));
            jsonObject.put("fromAvatar", AppConfig.getUserAvatar(context));
            jsonObject.put("toWhich", AppConfig.getUserCurrentChatType(context));
            jsonObject.put("toId", AppConfig.getUserCurrentChatId(context));
            jsonObject.put("content", videoPath);
            jsonObject.put("contentType", "video");
            jsonObject.put("time", TimeUtils.getCurrentTime());

            MyService.myBinder.sendMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
