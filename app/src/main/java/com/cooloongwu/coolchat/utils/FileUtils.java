package com.cooloongwu.coolchat.utils;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    /**
     * 给指定的文件名按照时间命名
     */
    private static SimpleDateFormat OUTGOING_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");

    /**
     * 得到指定的Video保存路径
     *
     * @return video路径
     */
    private static File getDoneVideoPath() {
        String DIR_NAME = "/CoolChat/video";
        File dir = new File(Environment.getExternalStorageDirectory() + DIR_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 得到输出的Video保存路径
     *
     * @return video路径
     */
    public static String newOutgoingFilePath() {
        String str = OUTGOING_DATE_FORMAT.format(new Date());
        return getDoneVideoPath() + "video_" + str + ".mp4";
    }

}
