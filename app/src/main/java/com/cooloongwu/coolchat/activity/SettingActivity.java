package com.cooloongwu.coolchat.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.AppManager;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initToolbar();
        initView();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        TextView text_logout = (TextView) findViewById(R.id.text_logout);
        text_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_logout:
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {
        //清除SharedPreferences的数据
        AppConfig.clearAllInfo(SettingActivity.this);
        //清除数据库的数据
        GreenDAOUtils.getInstance(SettingActivity.this).clearAllData();
        //退出App
        AppManager.getInstance().exitApp();
    }
}
