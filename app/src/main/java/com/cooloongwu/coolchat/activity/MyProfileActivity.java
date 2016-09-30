package com.cooloongwu.coolchat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.AppConfig;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;


public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initToolbar();
        initViews();
        //test();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("资料编辑");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        ImageView profile_img_avatar = (ImageView) findViewById(R.id.profile_img_avatar);
        TextView profile_text_name = (TextView) findViewById(R.id.profile_text_name);
        TextView profile_text_sex = (TextView) findViewById(R.id.profile_text_sex);

        Picasso.with(MyProfileActivity.this)
                .load(AppConfig.getUserAvatar(MyProfileActivity.this))
                .into(profile_img_avatar);
        profile_text_name.setText(AppConfig.getUserName(MyProfileActivity.this));
        profile_text_sex.setText(AppConfig.getUserSex(MyProfileActivity.this));
    }

    /**
     * 上传图片等到千牛云
     */
    private void test() {
        UploadManager uploadManager = new UploadManager();
        File file = new File("/storage/emulated/0/Pictures/Screenshots/S60930-111330.jpg");
        uploadManager.put(
                file, //文件
                null, //文件名
                AppConfig.getUserToken(MyProfileActivity.this),//token
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置。res中的key就是资源的名字
                        Log.e("七牛云", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }
}
