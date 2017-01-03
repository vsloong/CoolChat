package com.cooloongwu.coolchat.utils;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.AppConfig;

import java.io.File;
import java.io.IOException;

/**
 * 录音的工具类
 * Created by CooLoongWu on 2016-10-7.
 */
public class AudioRecorderUtils {
    //文件路径
    private String filePath;
    //文件夹路径
    private String FolderPath;
    private Context context;
    private String TAG = "AudioRecorderUtils";

    private MediaRecorder mMediaRecorder;
    private static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;

    private static int[] res = {R.mipmap.icon_chat_audio};

    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    /**
     * 文件存储默认sdcard/record
     */
    private AudioRecorderUtils() {
        //默认保存路径为/sdcard/record/下
        this(Environment.getExternalStorageDirectory() + "/CoolChat/record/");
    }

    public AudioRecorderUtils(Context context) {
        this();
        this.context = context;
    }

    private AudioRecorderUtils(String filePath) {
        File path = new File(filePath);
        if (!path.exists())
            path.mkdirs();
        this.FolderPath = filePath;
    }

    private long startTime;
    private long endTime;

    /**
     * 开始录音 使用amr格式
     * 录音文件
     */
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            //让程序睡眠0.5秒
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            filePath = FolderPath + AppConfig.getUserId(context) + "-" + TimeUtils.getCurrentTime() + ".amr";
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.e("录音开始时间", "startTime" + startTime);
        } catch (IllegalStateException | IOException e) {
            Log.e(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } finally {
            cancelRecord();
        }
    }

    /**
     * 停止录音
     */
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;

        long time = endTime - startTime;
        audioStatusUpdateListener.onStop(filePath, (time / 1000) + "");
        filePath = "";
        return time;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        File file = new File(filePath);
        if (file.exists())
            file.delete();
        filePath = "";
    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            int BASE = 1;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - startTime);
                }
            }
            int SPACE = 100;
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        public void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param filePath 保存路径
         */
        public void onStop(String filePath, String audioLength);
    }
}
