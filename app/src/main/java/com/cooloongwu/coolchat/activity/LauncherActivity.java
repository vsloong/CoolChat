package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.BaseActivity;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        goNext();
    }

    private void goNext() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                finish();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }
}
