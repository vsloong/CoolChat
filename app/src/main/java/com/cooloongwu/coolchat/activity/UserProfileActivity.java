package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.entity.Contact;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.greendao.gen.ContactDao;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    private int chatId;
    private String chatName;

    private TextView text_name_remark;  //大字显示的名字
    private TextView text_nickname;     //小字显示的名字
    private ImageView img_avatar;

    private ContactDao contactDao;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initToolbar();
        initViews();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        chatId = intent.getIntExtra("id", 0);

        contactDao = GreenDAOUtils.getInstance(this).getContactDao();
        contact = contactDao.queryBuilder()
                .where(ContactDao.Properties.UserId.eq(chatId))
                .build()
                .unique();

        String avatar = contact.getAvatar();
        //得到备注名，如果有则大字显示备注，小字显示昵称；否则只大字显示好友昵称
        chatName = contact.getRemarkName();
        String nickName = contact.getName();

        if (TextUtils.isEmpty(chatName)) {
            text_name_remark.setText(nickName);
            text_nickname.setText("");
        } else {
            text_name_remark.setText(chatName);
            text_nickname.setText("昵称：" + nickName);
        }

        if (!avatar.isEmpty()) {
            Picasso.with(this).load(avatar).into(img_avatar);
        }
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
        text_name_remark = (TextView) findViewById(R.id.text_name_remark);
        text_nickname = (TextView) findViewById(R.id.text_nickname);
        img_avatar = (ImageView) findViewById(R.id.img_avatar);
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

        switch (item.getItemId()) {
            case R.id.action_remark:
                Intent intent = new Intent(UserProfileActivity.this, NameActivity.class);
                intent.putExtra("type", "remarkName");
                startActivityForResult(intent, NameActivity.REQUEST_REMARKNAME);
                break;
            default:
                break;
        }
        return true;
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
                String chatType = "friend";
                intent.putExtra("chatType", chatType);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NameActivity.REQUEST_REMARKNAME) {
            String name = data.getStringExtra("name");
            contact.setRemarkName(name);
            contactDao.update(contact);
            initData();
        }
    }
}
