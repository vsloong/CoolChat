package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.utils.ToastUtils;
import com.squareup.picasso.Picasso;

public class FriendAddActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);

        initToolbar();
        initViews();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        ImageView img_avatar = (ImageView) findViewById(R.id.img_avatar);
        TextView text_name = (TextView) findViewById(R.id.text_name);
        Button btn_add = (Button) findViewById(R.id.btn_add);

        Intent intent = getIntent();
        text_name.setText(intent.getStringExtra("userName"));
        Picasso.with(this)
                .load(intent.getStringExtra("userAvatar"))
                .into(img_avatar);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                ToastUtils.showShort(FriendAddActivity.this, "点击了添加好友");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_report:
                ToastUtils.showShort(FriendAddActivity.this, "点击了举报菜单");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
