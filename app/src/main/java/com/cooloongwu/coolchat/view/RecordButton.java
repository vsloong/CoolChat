package com.cooloongwu.coolchat.view;

import android.content.Context;
import android.os.Environment;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 自定义录音按钮
 * Created by CooLoongWu on 2017-1-3 17:29.
 */

public class RecordButton extends Button {

    private static String audioPath = Environment.getExternalStorageDirectory() + "/CoolChat/record/";
    private static final int MAX_LENGTH = 1000 * 60;   // 最大录音时长1000*60;
    private static final int MIN_LENGTH = 1000;        // 最小录音时长1000;
    private OnFinishedRecordListener onFinishedRecordListener;

    public RecordButton(Context context) {
        super(context);
    }

    public void setOnFinishRocordListener(OnFinishedRecordListener onFinishRocordListener) {
        this.onFinishedRecordListener = onFinishRocordListener;
    }

    private void init() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //initDialogAndStartRecord();
                //startRecord();
                break;
            case MotionEvent.ACTION_UP:
                //finishRecord();
                break;
            case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
                //cancelRecord();
                break;
        }
        return true;
    }

    private void startRecord() {

    }

    private void finishRecord() {

    }

    private void cancelRecord() {

    }

    public interface OnFinishedRecordListener {
        public void onFinishedRecord(String audioPath);
    }

}
