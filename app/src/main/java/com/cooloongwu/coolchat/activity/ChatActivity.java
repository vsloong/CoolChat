package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.base.BaseActivity;

public class ChatActivity extends BaseActivity {

    private ImageButton imgbtn_emoji_keyboard;
    private ImageButton imgbtn_more_send_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getData();
        initViews();
    }

    private void getData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("name");
        String avatar = intent.getStringExtra("avatar");
        initToolbar(title);
    }

    private void initToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        EditText edit_input = (EditText) findViewById(R.id.edit_input);
        imgbtn_emoji_keyboard = (ImageButton) findViewById(R.id.imgbtn_emoji_keyboard);
        imgbtn_more_send_close = (ImageButton) findViewById(R.id.imgbtn_more_send_close);
        edit_input.addTextChangedListener(textWatcher);

    }

    /**
     * 监听输入框的变化，根据字数变化切换按钮
     */
    private TextWatcher textWatcher = new TextWatcher() {
        private CharSequence sequence;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            sequence = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (sequence.length() > 0) {
                imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_send);
            } else {
                imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_more);
            }
        }
    };
}
