package com.cooloongwu.coolchat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.AppConfig;
import com.duanqu.qupai.auth.AuthService;
import com.duanqu.qupai.auth.QupaiAuthListener;
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

        auth();
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

    private void auth() {
        AuthService authService = AuthService.getInstance();
        authService.setQupaiAuthListener(new QupaiAuthListener() {
            @Override
            public void onAuthError(int errorCode, String message) {
                Log.e("QupaiAuth", "错误码：" + errorCode + "；错误信息：" + message);
                Toast.makeText(MyProfileActivity.this, "趣拍云认证失败：" + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthComplte(int responseCode, String responseMessage) {
                Toast.makeText(MyProfileActivity.this, "趣拍云认证成功：" + responseMessage, Toast.LENGTH_SHORT).show();
            }
        });
        String appKey = "20c8241fb3e0c95";
        String appSecret = "149edead851b4331a0eb4207542a9a3e";
        authService.startAuth(
                MyProfileActivity.this,
                appKey,
                appSecret,
                String.valueOf(AppConfig.getUserId(MyProfileActivity.this)));
    }
}
