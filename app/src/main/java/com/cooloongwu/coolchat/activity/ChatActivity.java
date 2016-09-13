package com.cooloongwu.coolchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.ChatAdapter;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.bean.ChatBean;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton imgbtn_emoji_keyboard;
    private ImageButton imgbtn_more_send_close;
    private EditText edit_input;

    private boolean isSend = false;
    private boolean isMore = true;
    private boolean isClose = false;
    private boolean isEmoji = true;
    private boolean isKeyboard = false;

    private ArrayList<ChatBean> listData = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getData();
        initViews();
        initData();
    }

    /**
     * 加载聊天会话页的数据
     */
    private void initData() {
        List<ChatBean> chatBeens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i % 2 == 0) {
                ChatBean chatBean = new ChatBean();
                chatBean.setType("self_text");
                chatBean.setName("我自己");
                chatBean.setContent("你好哇，哈哈");
                chatBeens.add(chatBean);
            } else {
                ChatBean chatBean = new ChatBean();
                chatBean.setType("peer_text");
                chatBean.setName("我朋友");
                chatBean.setContent("你也好哇");
                chatBeens.add(chatBean);
            }
        }
        listData.addAll(chatBeens);
        adapter.notifyDataSetChanged();
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        //layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(ChatActivity.this, listData);
        recyclerView.setAdapter(adapter);
        edit_input = (EditText) findViewById(R.id.edit_input);
        edit_input.addTextChangedListener(textWatcher);

        imgbtn_emoji_keyboard = (ImageButton) findViewById(R.id.imgbtn_emoji_keyboard);
        imgbtn_more_send_close = (ImageButton) findViewById(R.id.imgbtn_more_send_close);
        imgbtn_emoji_keyboard.setOnClickListener(this);
        imgbtn_more_send_close.setOnClickListener(this);
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
                isSend = true;
                isMore = false;
            } else {
                imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_more);
                isSend = false;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_emoji_keyboard:
                //如果是“展示表情”的状态，那么点击后展示表情，按钮状态改变为“显示键盘”，并关闭键盘
                if (isEmoji) {
                    Toast.makeText(ChatActivity.this, "展示表情,隐藏键盘", Toast.LENGTH_SHORT).show();
                    imgbtn_emoji_keyboard.setImageResource(R.mipmap.conversation_btn_messages_keyboard);
                    isEmoji = false;
                    isKeyboard = true;
                    hideKeyboard();
                    return;
                }
                //如果是“展示键盘”的状态，那么点击后展示键盘，按钮状态改为“展示表情”，并展示键盘
                if (isKeyboard) {
                    Toast.makeText(ChatActivity.this, "展示键盘，隐藏表情", Toast.LENGTH_SHORT).show();
                    imgbtn_emoji_keyboard.setImageResource(R.mipmap.conversation_btn_messages_emoji);
                    isKeyboard = false;
                    isEmoji = true;
                    showKeyboard();
                    return;
                }
                break;
            case R.id.imgbtn_more_send_close:
                //如果是“展示更多”的状态，那么点击后展示更多的按钮，按钮状态改为“关闭更多”
                if (isMore) {
                    Toast.makeText(ChatActivity.this, "展示更多", Toast.LENGTH_SHORT).show();
                    imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_close);
                    isMore = false;
                    isClose = true;
                    return;
                }
                //如果是“关闭更多”的状态，那么点击后关闭更多的按钮，按钮状态改为“展示更多”
                if (isClose) {
                    Toast.makeText(ChatActivity.this, "关闭更多", Toast.LENGTH_SHORT).show();
                    imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_more);
                    isClose = false;
                    isMore = true;
                    return;
                }
                //如果是“发送消息”的状态，那么点击后发送消息，按钮状态改为“展示更多”状态，并关闭键盘
                if (isSend) {
                    Toast.makeText(ChatActivity.this, "发送消息", Toast.LENGTH_SHORT).show();
                    sendMessage();
                    edit_input.setText("");
                    imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_more);
                    isSend = false;
                    isMore = true;
                    hideKeyboard();
                    return;
                }
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
        List<ChatBean> chatBeens = new ArrayList<>();
        ChatBean chatBean = new ChatBean();
        chatBean.setName("我自己");
        chatBean.setType("self_text");
        chatBean.setContent(edit_input.getText().toString().trim());
        chatBeens.add(chatBean);
        listData.addAll(chatBeens);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    /**
     * 显示键盘
     */
    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit_input, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 关闭键盘
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_input.getWindowToken(), 0);
    }
}
