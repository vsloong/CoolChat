package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.ChatAdapter;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.entity.Chat;
import com.cooloongwu.coolchat.entity.Conversation;
import com.cooloongwu.coolchat.entity.Group;
import com.cooloongwu.coolchat.utils.DisplayUtils;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.coolchat.utils.KeyboardUtils;
import com.cooloongwu.coolchat.utils.SendMessageUtils;
import com.cooloongwu.coolchat.utils.ToastUtils;
import com.cooloongwu.coolchat.view.RecordFragment;
import com.cooloongwu.coolchat.view.emoticons.ChatMoreFragment;
import com.cooloongwu.coolchat.view.emoticons.EmojiFragment;
import com.cooloongwu.emoji.entity.Emoji;
import com.cooloongwu.emoji.utils.EmojiTextUtils;
import com.cooloongwu.greendao.gen.ChatDao;
import com.cooloongwu.greendao.gen.ConversationDao;
import com.cooloongwu.greendao.gen.GroupDao;
import com.cooloongwu.qupai.QupaiSetting;
import com.cooloongwu.qupai.QupaiUpload;
import com.cooloongwu.qupai.RecordResult;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.qupai.upload.QupaiUploadListener;
import com.duanqu.qupai.upload.UploadService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class ChatActivity extends BaseActivity implements View.OnClickListener, EmojiFragment.OnEmojiClickListener {

    private ImageButton imgbtn_send;
    private ImageButton imgbtn_more_close;
    private EditText edit_input;
    private static TextView text_unread_msg;
    private LinearLayout layout_multi;

    private boolean isMore = true;
    private boolean isClose = false;

    private ArrayList<Chat> chatListData = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private static RecyclerView recyclerView;
    private static ChatAdapter adapter;
    private MyHandler handler = new MyHandler(ChatActivity.this);

    private Toolbar toolbar;
    private int chatId;
    private String chatType;
    private String chatName;

    //在当前页面接收到另一个好友或者群组的聊天消息
    private int otherChatId;
    private String otherChatType;
    private String otherChatName;

    private final int REQUEST_IMAGE = 0x01;
    private final int REQUEST_VIDEO = 0x02;

    private long latestId = 0;

    private EmojiFragment emojiFragment;
    private ChatMoreFragment chatMoreFragment;
    private RecordFragment recordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EventBus.getDefault().register(this);
        getData();
        initViews();

        isSetBoardHeight();
        initRecentChatData(chatType, chatId);
    }

    private void isSetBoardHeight() {
        if (AppConfig.getKeyboardHeight(ChatActivity.this) == 0) {
            KeyboardUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            DisplayUtils.init(ChatActivity.this).detectKeyboardHeight();
        }
    }

    private void getData() {
        Intent intent = getIntent();
        chatId = intent.getIntExtra("chatId", 0);                       //好友或者群组的ID
        chatType = intent.getStringExtra("chatType");                   //群组还是好友
        chatName = intent.getStringExtra("chatName");                   //群组名或者好友名
        LogUtils.e("聊天信息" + "当前在跟" + chatType + "：ID为" + chatId + "的" + chatName + "聊天");
        initToolbar(chatName);

        //保存当前聊天对象的信息
        AppConfig.setUserCurrentChatId(ChatActivity.this, chatId);
        AppConfig.setUserCurrentChatType(ChatActivity.this, chatType);
    }

    private void initToolbar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        //layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(ChatActivity.this, chatListData);
        recyclerView.setAdapter(adapter);
        edit_input = (EditText) findViewById(R.id.edit_input);
        edit_input.addTextChangedListener(textWatcher);
        edit_input.setOnClickListener(this);
        text_unread_msg = (TextView) findViewById(R.id.text_unread_msg);

        layout_multi = (LinearLayout) findViewById(R.id.layout_multi);
        ImageButton imgbtn_emoji = (ImageButton) findViewById(R.id.imgbtn_emoji);
        imgbtn_send = (ImageButton) findViewById(R.id.imgbtn_send);
        imgbtn_send.setClickable(false);
        imgbtn_more_close = (ImageButton) findViewById(R.id.imgbtn_more_close);
        ImageButton imgbtn_voice = (ImageButton) findViewById(R.id.imgbtn_voice);
        ImageButton imgbtn_gallery = (ImageButton) findViewById(R.id.imgbtn_gallery);
        ImageButton imgbtn_video = (ImageButton) findViewById(R.id.imgbtn_video);


        text_unread_msg.setOnClickListener(this);
        imgbtn_emoji.setOnClickListener(this);
        imgbtn_send.setOnClickListener(this);
        imgbtn_more_close.setOnClickListener(this);
        imgbtn_voice.setOnClickListener(this);
        imgbtn_gallery.setOnClickListener(this);
        imgbtn_video.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initMoreChatData(chatType, chatId);
            }
        });
    }

    /**
     * 加载最近的聊天消息，默认5条（QQ是15条）
     */
    private void initRecentChatData(String chatType, int chatId) {
        ChatDao chatDao = GreenDAOUtils.getInstance(ChatActivity.this).getChatDao();
        List<Chat> chats;

        //从Id最大的往小查
        if ("friend".equals(chatType)) {
            chats = chatDao.queryBuilder()
                    .where(ChatDao.Properties.ChatType.eq(chatType))
                    .whereOr(ChatDao.Properties.FromId.eq(chatId), ChatDao.Properties.ToId.eq(chatId))
                    .limit(5)
                    .orderDesc(ChatDao.Properties.Time)
                    .build()
                    .list();
        } else {
            chats = chatDao.queryBuilder()
                    .where(ChatDao.Properties.ChatType.eq(chatType), ChatDao.Properties.ToId.eq(chatId))
                    .limit(5)
                    .orderDesc(ChatDao.Properties.Time)
                    .build()
                    .list();
        }

        if (!chats.isEmpty()) {
            latestId = chats.get(chats.size() - 1).getId();
            LogUtils.e("数据的索引" + latestId);
            //倒序排列下
            Collections.reverse(chats);
            //为了加载和其他人聊天信息的时候清空屏幕
            chatListData.clear();
            chatListData.addAll(chats);

            adapter.notifyDataSetChanged();
            int itemCount = adapter.getItemCount() - 1;
            if (itemCount > 0) {
                recyclerView.smoothScrollToPosition(itemCount);
            }
        }
    }

    /**
     * 下拉加载更多聊天信息
     */
    private void initMoreChatData(String chatType, int chatId) {
        ChatDao chatDao = GreenDAOUtils.getInstance(ChatActivity.this).getChatDao();
        List<Chat> chats;
        //从Id最大的往小查
        if ("friend".equals(chatType)) {
            chats = chatDao.queryBuilder()
                    .where(ChatDao.Properties.ChatType.eq(chatType), ChatDao.Properties.Id.lt(latestId))
                    .whereOr(ChatDao.Properties.FromId.eq(chatId), ChatDao.Properties.ToId.eq(chatId))
                    .limit(10)
                    .orderDesc(ChatDao.Properties.Time)
                    .build()
                    .list();
        } else {
            chats = chatDao.queryBuilder()
                    .where(ChatDao.Properties.ChatType.eq(chatType), ChatDao.Properties.Id.lt(latestId), ChatDao.Properties.ToId.eq(chatId))
                    .limit(10)
                    .orderDesc(ChatDao.Properties.Time)
                    .build()
                    .list();
        }
        if (!chats.isEmpty()) {
            latestId = chats.get(chats.size() - 1).getId();
            LogUtils.e("数据的索引" + latestId);
            Collections.reverse(chats);
            chatListData.addAll(0, chats);
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShort(getApplicationContext(), "没有更多数据了");
        }
        swipeRefreshLayout.setRefreshing(false);
    }


    @Subscribe
    public void onEventMainThread(JSONObject jsonObject) {
        try {

            /**
             * 如果toWhich是friend，那么toId可能是自己的ID（朋友发的消息）或者朋友的Id（自己发的消息）
             *                    那么fromId可能是自己的ID（自己发的消息）或者朋友的ID（朋友发的消息）
             *
             * 如果toWhich是group，那么toId就是群组的Id
             *                   那么fromId可能是自己的ID（自己发的消息）或者群组中其他人的ID（群组中其他人发的消息）
             */
            String toWhich = jsonObject.getString("toWhich");   //可能是friend或者group
            int toId = jsonObject.getInt("toId");               //可能是我自己的ID或者对方的ID
            int fromId = jsonObject.getInt("fromId");           //可能是我自己的ID或者对方的ID

            String fromAvatar = jsonObject.getString("fromAvatar");
            String fromName = jsonObject.getString("fromName");
            String content = jsonObject.getString("content");
            String contentType = jsonObject.getString("contentType");
            String time = jsonObject.getString("time");

            if (chatType.equals(toWhich)) {
                //跟当前聊天类型匹配，是群组或者好友的消息
                if ("friend".equals(toWhich)) {
                    //当前在跟好友聊天，需判断是不是当前好友的消息
                    if ((toId == chatId && fromId == AppConfig.getUserId(ChatActivity.this)) //我发给当前朋友的消息
                            || (toId == AppConfig.getUserId(ChatActivity.this) && fromId == chatId)//朋友发给我的消息
                            ) {
                        //是跟当前好友的聊天消息
                        List<Chat> chats = new ArrayList<>();
                        Chat chat = new Chat();
                        chat.setFromId(fromId);
                        chat.setFromAvatar(fromAvatar);
                        chat.setFromName(fromName);
                        chat.setContent(content);
                        chat.setContentType(contentType);
                        chat.setToId(toId);
                        chat.setTime(time);
                        chat.setIsRead(true);             //消息已读
                        if ("audio".equals(contentType)) {
                            chat.setAudioLength(jsonObject.getString("audioLength"));
                        }

                        chats.add(chat);
                        chatListData.addAll(chats);

                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } else {
                        //是好友信息，但不是当前聊天好友的
                        showOtherFriendMsg(fromName + "：" + content, toWhich, fromId, fromName);
                    }
                } else {
                    //当前在跟群组聊天，需判断是不是当前群组的消息
                    if (chatId == toId) {
                        List<Chat> chats = new ArrayList<>();
                        Chat chat = new Chat();
                        chat.setContent(content);
                        chat.setContentType(contentType);
                        chat.setFromAvatar(fromAvatar);
                        chat.setFromId(fromId);
                        chat.setTime(time);
                        chat.setToId(toId);
                        chat.setIsRead(true);
                        if ("audio".equals(contentType)) {
                            chat.setAudioLength(jsonObject.getString("audioLength"));
                        }
                        chats.add(chat);
                        chatListData.addAll(chats);

                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } else {
                        //不是当前群组的聊天消息，提示来消息了即可
                        showOtherGroupMsg(fromName + "：" + content, toWhich, toId);
                    }
                }
            } else {
                //跟当前聊天类型不匹配，比如：当前在跟好友聊天，来的是群组消息；当前跟群组聊天，来的是好友消息
                if ("friend".equals(chatType)) {
                    showOtherFriendMsg(fromName + "：" + content, toWhich, fromId, fromName);
                } else {
                    showOtherGroupMsg(fromName + "：" + content, toWhich, toId);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提醒其他好友或者群组来消息了
     *
     * @param str 消息内容
     */
    private void showOtherFriendMsg(String str, String chatType, int chatId, String chatName) {
        otherChatType = chatType;
        otherChatId = chatId;
        otherChatName = chatName;

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("otherMsg", str);
        msg.setData(bundle);
        msg.what = 1;
        handler.sendMessage(msg);
        handler.sendEmptyMessageDelayed(2, 5000);
    }

    /**
     * 提醒其他好友或者群组来消息了
     *
     * @param str 消息内容
     */
    private void showOtherGroupMsg(String str, String chatType, int chatId) {
        otherChatType = chatType;
        otherChatId = chatId;

        GroupDao groupDao = GreenDAOUtils.getInstance(ChatActivity.this).getGroupDao();
        Group group = groupDao.queryBuilder()
                .where(GroupDao.Properties.GroupId.eq(chatId))
                .build().unique();
        otherChatName = group.getGroupName();

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("otherMsg", otherChatName + "：" + str);
        msg.setData(bundle);
        msg.what = 1;
        handler.sendMessage(msg);
        handler.sendEmptyMessageDelayed(2, 5000);
    }


    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.imgbtn_voice:
                showMultiLayout();

                if (recordFragment == null) {
                    recordFragment = new RecordFragment();
                }
                fragmentTransaction.replace(R.id.layout_multi, recordFragment);
                fragmentTransaction.commit();
                break;

            case R.id.imgbtn_emoji:
                showMultiLayout();
                if (emojiFragment == null) {
                    emojiFragment = new EmojiFragment();
                }
                fragmentTransaction.replace(R.id.layout_multi, emojiFragment);
                fragmentTransaction.commit();
                break;

            case R.id.edit_input:
                layout_multi.postDelayed(hideMultiLayoutRunnable, 500);
                break;

            case R.id.imgbtn_gallery:
                hideMultiLayout();
                openImageGallery();
                break;

            case R.id.imgbtn_video:
                hideMultiLayout();
                openRecordPage();
                break;

            case R.id.imgbtn_send:
                SendMessageUtils.sendTextMessage(ChatActivity.this, edit_input.getText().toString().trim());
                edit_input.setText("");
                imgbtn_send.setClickable(false);
                break;

            case R.id.imgbtn_more_close:
                //如果是“展示更多”的状态，那么点击后展示更多的按钮，按钮状态改为“关闭更多”
                if (isMore) {
                    imgbtn_more_close.setImageResource(R.mipmap.conversation_btn_messages_close);
                    isMore = false;
                    isClose = true;

                    showMultiLayout();
                    //layout_multi.addView((View) getResources().getLayout(R.layout.layout_chat_more));
                    if (chatMoreFragment == null) {
                        chatMoreFragment = new ChatMoreFragment();
                    }
                    fragmentTransaction.replace(R.id.layout_multi, chatMoreFragment);
                    fragmentTransaction.commit();
                    return;
                }
                //如果是“关闭更多”的状态，那么点击后关闭更多的按钮，按钮状态改为“展示更多”
                if (isClose) {
                    imgbtn_more_close.setImageResource(R.mipmap.conversation_btn_messages_more);
                    isClose = false;
                    isMore = true;

                    hideMultiLayout();
                    return;
                }
                break;
            case R.id.text_unread_msg:
                text_unread_msg.setVisibility(View.GONE);
                //刷新当前页面，加载与点击的好友或者群组聊天
                chatId = otherChatId;
                chatType = otherChatType;
                chatName = otherChatName;

                toolbar.setTitle(chatName);
                initRecentChatData(chatType, chatId);

                //并将聊天列表页的未读消息数置为0
                ConversationDao conversationDao = GreenDAOUtils.getInstance(ChatActivity.this).getConversationDao();
                Conversation conversation = conversationDao.queryBuilder()
                        .where(ConversationDao.Properties.Type.eq(chatType), ConversationDao.Properties.MultiId.eq(chatId))
                        .build().unique();
                conversation.setUnReadNum(0);
                conversationDao.update(conversation);

                //通知聊天列表页更新
                EventBus.getDefault().post(new Conversation());
                //并重新指定当前聊天的对象
                AppConfig.setUserCurrentChatId(ChatActivity.this, chatId);
                AppConfig.setUserCurrentChatType(ChatActivity.this, chatType);
                break;
            default:
                break;
        }

        recyclerView.refreshDrawableState();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        if ("friend".equals(chatType)) {
            menu.getItem(0).setIcon(R.mipmap.icon_menu_profile_user);
        } else {
            menu.getItem(0).setIcon(R.mipmap.icon_menu_profile_group);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        if ("friend".equals(chatType)) {
            //跳转到好友个人资料页面
            intent.setClass(ChatActivity.this, UserProfileActivity.class);
        } else {
            //跳转到群组资料页面
            intent.setClass(ChatActivity.this, GroupProfileActivity.class);
        }
        intent.putExtra("id", chatId);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    /**
     * 处理图片的发送
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String path : paths) {
                    SendMessageUtils.sendImageMessage(ChatActivity.this, new File(path));
                }
            }
        }

        if (requestCode == REQUEST_VIDEO) {
            if (resultCode == RESULT_OK) {
                RecordResult result = new RecordResult(data);
                //得到视频地址，和缩略图地址的数组，返回十张缩略图
                String videoPath = result.getPath();
                String thumbnails[] = result.getThumbnail();
                result.getDuration();

                startUpload(videoPath, thumbnails[0]);

                /**
                 * 清除草稿,草稿文件将会删除。所以在这之前我们执行拷贝move操作。
                 * 上面的拷贝操作请自行实现，第一版本的copyVideoFile接口不再使用
                 */
//            QupaiService qupaiService = QupaiManager
//                    .getQupaiService(MainActivity.this);
//            qupaiService.deleteDraft(getApplicationContext(),data);

            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        handler.removeCallbacksAndMessages(null);

        //设置当前聊天对象，表示没有
        AppConfig.setUserCurrentChatId(ChatActivity.this, 0);
        AppConfig.setUserCurrentChatType(ChatActivity.this, "");
    }

    @Override
    public void onEmojiDelete() {
        String text = edit_input.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                edit_input.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                displayEditTextView();
                return;
            }
            edit_input.getText().delete(index, text.length());
            displayEditTextView();
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        edit_input.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            int index = edit_input.getSelectionStart();
            Editable editable = edit_input.getEditableText();
            if (index < 0) {
                editable.append(emoji.getContent());
            } else {
                editable.insert(index, emoji.getContent());
            }
        }
        displayEditTextView();
    }

    private void displayEditTextView() {
        try {
            edit_input.setText(EmojiTextUtils.getEditTextContent(edit_input.getText().toString().trim(), ChatActivity.this, edit_input));
            edit_input.setSelection(edit_input.getText().length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<ChatActivity> activityWeakReference;

        MyHandler(ChatActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChatActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        break;
                    case 1:
                        Bundle bundle = msg.getData();
                        String otherMsg = bundle.getString("otherMsg");
                        text_unread_msg.setVisibility(View.VISIBLE);
                        text_unread_msg.setText(otherMsg);
                        break;
                    case 2:
                        text_unread_msg.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 展示多功能布局
     */
    private void showMultiLayout() {
        //删除之前的所有其他视图
        layout_multi.removeAllViews();
        //更新表情栏高度和键盘高度相等
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_multi.getLayoutParams();
        if (params != null) {
            params.height = AppConfig.getKeyboardHeight(ChatActivity.this);
            layout_multi.setLayoutParams(params);
        }

        //显示多功能布局，隐藏键盘
        layout_multi.removeCallbacks(hideMultiLayoutRunnable);
        KeyboardUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        layout_multi.setVisibility(View.VISIBLE);
        KeyboardUtils.hideKeyboard(getCurrentFocus());
    }

    /**
     * 隐藏多功能布局
     */
    private void hideMultiLayout() {
        //隐藏表情栏
        layout_multi.removeAllViews();
        layout_multi.setVisibility(View.GONE);
        KeyboardUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private Runnable hideMultiLayoutRunnable = new Runnable() {
        @Override
        public void run() {
            hideMultiLayout();
        }
    };

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
                imgbtn_send.setImageResource(R.mipmap.conversation_btn_messages_send);
                imgbtn_send.setClickable(true);
            } else {
                imgbtn_send.setImageResource(R.mipmap.conversation_btn_messages_send_disable);
                imgbtn_send.setClickable(false);
            }
        }
    };

    /**
     * 打开图片库
     */
    private void openImageGallery() {
        MultiImageSelector.create()
                .showCamera(true) // show camera or not. true by default
                .count(9) // max select image size, 9 by default. used width #.multi()
                .multi() // multi、single mode, default mode is multi;
                .start(this, REQUEST_IMAGE);
    }

    /**
     * 打开短视频录制页面
     */
    private void openRecordPage() {
        QupaiService qupaiService = QupaiManager.getQupaiService(this);
        if (qupaiService == null) {
            ToastUtils.showShort(getApplicationContext(), "插件没有初始化，无法获取 QupaiService");
            return;
        }
        qupaiService.showRecordPage(this, REQUEST_VIDEO, true);
    }

    /**
     * 开始上传
     */
    private void startUpload(String videoPath, String thumbnailPath) {
        UploadService uploadService = UploadService.getInstance();
        uploadService.setQupaiUploadListener(new QupaiUploadListener() {
            @Override
            public void onUploadProgress(String uuid, long uploadedBytes, long totalBytes) {
                int percentsProgress = (int) (uploadedBytes * 100 / totalBytes);
                LogUtils.e("趣拍云上传进度", "uuid:" + uuid + "；进度：" + percentsProgress + "%");
                //progress.setProgress(percentsProgress);
            }

            @Override
            public void onUploadError(String uuid, int errorCode, String message) {
                LogUtils.e("趣拍云上传失败", "uuid:" + uuid + "；错误信息：" + errorCode + message);
            }

            @Override
            public void onUploadComplte(String uuid, int responseCode, String responseMessage) {
                //http://{DOMAIN}/v/{UUID}.mp4?token={ACCESS-TOKEN}
                //progress.setVisibility(View.GONE);

                //这里返回的uuid是你创建上传任务时生成的uuid.开发者可以使用其他作为标识
                //videoUrl返回的是上传成功的视频地址,imageUrl是上传成功的图片地址
                String videoUrl = QupaiSetting.domain + "/v/" + responseMessage + ".mp4" + "?token=" + AppConfig.getQupaiToken(ChatActivity.this);
                String imageUrl = QupaiSetting.domain + "/v/" + responseMessage + ".jpg" + "?token=" + AppConfig.getQupaiToken(ChatActivity.this);
                LogUtils.e("趣拍云上传成功", "视频地址：" + videoUrl);
                LogUtils.e("趣拍云上传成功", "缩略图地址：" + imageUrl);

                SendMessageUtils.sendVideoMessage(ChatActivity.this, videoUrl);
            }
        });

        String uuid = UUID.randomUUID().toString();
        LogUtils.e("趣拍云认证", "accessToken：" + AppConfig.getQupaiToken(ChatActivity.this) + "；space：" + AppConfig.getUserId(ChatActivity.this));

        QupaiUpload.startUpload(QupaiUpload.createUploadTask(
                this,
                uuid,
                new File(videoPath),
                new File(thumbnailPath),
                AppConfig.getQupaiToken(ChatActivity.this),
                String.valueOf(AppConfig.getUserId(ChatActivity.this)),
                QupaiSetting.shareType,
                QupaiSetting.tags,
                QupaiSetting.description)
        );
    }


}
