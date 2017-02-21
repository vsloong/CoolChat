package com.cooloongwu.coolchat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.utils.SendMessageUtils;
import com.cooloongwu.coolchat.utils.ToastUtils;
import com.cooloongwu.coolchat.view.RecordButton;

import java.io.File;

public class RecordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);

        initViews(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        RecordButton recordButton = (RecordButton) rootView.findViewById(R.id.btn_record);
        recordButton.setOnFinishRecordListener(new RecordButton.OnFinishedRecordListener() {

            @Override
            public void onFinishedRecord(String audioFilePath, String audioLength) {
                LogUtils.e("录音位置完成：位置：" + audioFilePath + "；长度：" + audioLength);
                SendMessageUtils.sendAudioMessage(getActivity(), new File(audioFilePath), audioLength);
            }

            @Override
            public void onCancelRecord(String msg) {
                ToastUtils.showShort(getActivity(), "录音取消");
                LogUtils.e("录音取消");
            }
        });
    }

}
