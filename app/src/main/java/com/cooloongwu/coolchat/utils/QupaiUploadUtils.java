package com.cooloongwu.coolchat.utils;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.qupai.QupaiSetting;
import com.cooloongwu.qupai.QupaiUpload;
import com.duanqu.qupai.upload.QupaiUploadListener;
import com.duanqu.qupai.upload.UploadService;

import java.io.File;
import java.util.UUID;

/**
 * 趣拍云视频上传工具类
 * Created by CooLoongWu on 2017-2-10 11:00.
 */

public class QupaiUploadUtils {

    /**
     * 开始上传
     */
    public static void startUpload(final Context context, String videoPath, String thumbnailPath) {
        UploadService uploadService = UploadService.getInstance();
        uploadService.setQupaiUploadListener(new QupaiUploadListener() {
            @Override
            public void onUploadProgress(String uuid, long uploadedBytes, long totalBytes) {
                int percentsProgress = (int) (uploadedBytes * 100 / totalBytes);
                LogUtils.e("趣拍云上传进度", "uuid:" + uuid + "；进度：" + percentsProgress + "%");
                //progress.setProgress(percentsProgress);
            }

            @Override
            public void onUploadError(String uuid, int errorCode, String message) {
                LogUtils.e("趣拍云上传失败", "uuid:" + uuid + "；错误信息：" + errorCode + message);
            }

            @Override
            public void onUploadComplte(String uuid, int responseCode, String responseMessage) {
                //http://{DOMAIN}/v/{UUID}.mp4?token={ACCESS-TOKEN}
                //progress.setVisibility(View.GONE);

                //这里返回的uuid是你创建上传任务时生成的uuid.开发者可以使用其他作为标识
                //videoUrl返回的是上传成功的视频地址,imageUrl是上传成功的图片地址
                String videoUrl = QupaiSetting.domain + "/v/" + responseMessage + ".mp4" + "?token=" + AppConfig.getQupaiToken(context);
                String imageUrl = QupaiSetting.domain + "/v/" + responseMessage + ".jpg" + "?token=" + AppConfig.getQupaiToken(context);
                LogUtils.e("趣拍云上传成功", "视频地址：" + videoUrl);
                LogUtils.e("趣拍云上传成功", "缩略图地址：" + imageUrl);

                SendMessageUtils.sendVideoMessage(context, videoUrl);
            }
        });

        String uuid = UUID.randomUUID().toString();
        LogUtils.e("趣拍云认证", "accessToken：" + AppConfig.getQupaiToken(context) + "；space：" + AppConfig.getUserId(context));

        QupaiUpload.startUpload(QupaiUpload.createUploadTask(
                context,
                uuid,
                new File(videoPath),
                new File(thumbnailPath),
                AppConfig.getQupaiToken(context),
                String.valueOf(AppConfig.getUserId(context)),
                QupaiSetting.shareType,
                QupaiSetting.tags,
                QupaiSetting.description)
        );
    }
}
