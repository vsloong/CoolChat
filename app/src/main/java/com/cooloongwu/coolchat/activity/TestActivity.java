package com.cooloongwu.coolchat.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cooloongwu.coolchat.R;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class TestActivity extends AppCompatActivity {

    private IjkMediaPlayer ijkMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private void initView() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.video_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();

        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setKeepInBackground(false);
        try {
            ijkMediaPlayer.setDataSource(this, Uri.parse("http://coolchat.s.qupai.me/v/971cb5c7-b497-4fb2-afaf-8c34b661a2c6.mp4"));
            ijkMediaPlayer.setDisplay(surfaceHolder);

            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    ijkMediaPlayer.setDisplay(surfaceHolder);
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                }
            });

            ijkMediaPlayer.prepareAsync();
            ijkMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ijkMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ijkMediaPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ijkMediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ijkMediaPlayer.release();
    }
}
