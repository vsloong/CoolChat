package com.cooloongwu.coolchat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.AppConfig;
import com.squareup.picasso.Picasso;


public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initToolbar();
        initViews();

        test();
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

    private void test() {

    }


}
