package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.utils.TimeUtils;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        login();
    }

    private void login() {
        if (AppConfig.getQiniuToken(this).isEmpty()) {
            goLogin();
        } else {
            if (TimeUtils.canLogin(this)) {
                goMain();
            } else {
                goLogin();
            }
        }
    }

    private void goLogin() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                finish();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    private void goMain() {
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
