package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.cooloongwu.coolchat.R;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private int chatId;
    private String chatName;
    private String chatType;
    private String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initData();
        initViews();
    }

    private void initData() {
        Intent intent = getIntent();
        chatName = intent.getStringExtra("name");
        avatar = intent.getStringExtra("avatar");
        chatId = intent.getIntExtra("id", 0);
        chatType = intent.getStringExtra("type");
        String sex = intent.getStringExtra("sex");

        initToolbar(chatName);
    }

    private void initToolbar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        ImageButton imgbtn_call = (ImageButton) findViewById(R.id.imgbtn_call);
        ImageButton imgbtn_video = (ImageButton) findViewById(R.id.imgbtn_video);
        ImageButton imgbtn_message = (ImageButton) findViewById(R.id.imgbtn_message);
        imgbtn_call.setOnClickListener(this);
        imgbtn_video.setOnClickListener(this);
        imgbtn_message.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_call:
                break;
            case R.id.imgbtn_video:
                break;
            case R.id.imgbtn_message:
                Intent intent = new Intent();
                intent.setClass(UserProfileActivity.this, ChatActivity.class);
                intent.putExtra("chatId", chatId);
                intent.putExtra("chatName", chatName);
                intent.putExtra("chatType", chatType);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
