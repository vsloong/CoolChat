package com.cooloongwu.qupai;

import android.content.Context;
import android.util.Log;

import com.duanqu.qupai.bean.QupaiUploadTask;
import com.duanqu.qupai.upload.UploadService;

import java.io.File;

/**
 * 视频上传类
 * Created by CooLoongWu on 2016-10-14 13:27.
 */

public class QupaiUpload {

    /**
     * 创建一个上传任务
     *
     * @param context     上下文
     * @param uuid        随机生成的UUID
     * @param videoFile   完整视频文件
     * @param thumbnail   缩略图
     * @param accessToken 通过调用鉴权得到token
     * @param space       开发者生成的Quid，必须要和token保持一致
     * @param share       是否公开 0公开分享 1私有(default) 公开类视频不需要AccessToken授权
     * @param tags        标签 多个标签用 "," 分隔符
     * @param description 视频描述
     * @return QupaiUploadTask
     */
    public static QupaiUploadTask createUploadTask(Context context, String uuid, File videoFile, File thumbnail, String accessToken,
                                                   String space, int share, String tags, String description) {
        UploadService uploadService = UploadService.getInstance();
        return uploadService.createTask(context, uuid, videoFile, thumbnail,
                accessToken, space, share, tags, description);
    }

    /**
     * 开始上传
     *
     * @param data 上传任务的task
     */
    public static void startUpload(QupaiUploadTask data) {
        try {
            UploadService uploadService = UploadService.getInstance();
            uploadService.startUpload(data);
        } catch (IllegalArgumentException exc) {
            Log.d("趣拍云上传错误", "Missing some arguments. " + exc.getMessage());
        }
    }
}
