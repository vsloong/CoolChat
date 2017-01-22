package com.cooloongwu.coolchat.view;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.apkfuns.logutils.LogUtils;

import java.io.File;
import java.io.IOException;

/**
 * 自定义录音按钮
 * Created by CooLoongWu on 2017-1-3 17:29.
 */

public class RecordButton extends Button {

    private static String audioPath = Environment.getExternalStorageDirectory() + "/CoolChat/record/";
    private long startTime; //录音开始时间
    private String audioFileName = audioPath + startTime + ".amr";
    private static final int MAX_LENGTH = 1000 * 60;   // 最大录音时长1000*60;
    private static final int MIN_LENGTH = 1000;        // 最小录音时长1000;
    private float startX;
    private OnFinishedRecordListener onFinishedRecordListener;
    private MediaRecorder recorder;


    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnFinishRecordListener(OnFinishedRecordListener onFinishRecordListener) {
        this.onFinishedRecordListener = onFinishRecordListener;
    }

    private void init() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startRecord();
                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                if (endX - startX > 150) {
                    cancelRecord();
                } else {
                    finishRecord();
                }
                break;
            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
                cancelRecord();
                break;
        }
        return true;
    }

    /**
     * 开始录音
     */
    private void startRecord() {
        if (recorder == null)
            recorder = new MediaRecorder();
        try {
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(audioFileName);
            recorder.prepare();
            recorder.start();
            startTime = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("MediaRecorder start()报错");
        }
    }

    /**
     * 完成录音，需要保存
     */
    private void finishRecord() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }

        long intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_LENGTH) {
            File file = new File(audioFileName);
            file.delete();
            if (onFinishedRecordListener != null)
                onFinishedRecordListener.onCancelRecord("录音时间不得小于1秒");
            return;
        }

        if (intervalTime > MAX_LENGTH) {
            File file = new File(audioFileName);
            file.delete();
            if (onFinishedRecordListener != null)
                onFinishedRecordListener.onCancelRecord("录音时间不得大于60秒");
            return;
        }

        if (onFinishedRecordListener != null)
            onFinishedRecordListener.onFinishedRecord(audioFileName, String.valueOf(intervalTime / 1000));
    }

    /**
     * 取消录音，删除文件
     */
    private void cancelRecord() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        File file = new File(audioFileName);
        file.delete();
        if (onFinishedRecordListener != null)
            onFinishedRecordListener.onCancelRecord("录音取消");
    }

    public interface OnFinishedRecordListener {
        void onFinishedRecord(String audioFilePath, String audioLength);

        void onCancelRecord(String msg);
    }

}
