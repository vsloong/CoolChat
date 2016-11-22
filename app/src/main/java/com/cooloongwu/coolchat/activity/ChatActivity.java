package com.cooloongwu.coolchat.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.ChatAdapter;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.entity.Chat;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.coolchat.base.MyService;
import com.cooloongwu.coolchat.utils.AudioRecorderUtils;
import com.cooloongwu.coolchat.utils.TimeUtils;
import com.cooloongwu.greendao.gen.ChatDao;
import com.cooloongwu.qupai.QupaiSetting;
import com.cooloongwu.qupai.QupaiUpload;
import com.cooloongwu.qupai.RecordResult;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.qupai.upload.QupaiUploadListener;
import com.duanqu.qupai.upload.UploadService;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class ChatActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageButton imgbtn_emoji_keyboard;
    private ImageButton imgbtn_more_send_close;
    private ImageButton imgbtn_voice_keyboard;
    private Button btn_audio;
    private EditText edit_input;

    private boolean isSend = false;
    private boolean isMore = true;
    private boolean isClose = false;
    private boolean isEmoji = true;
    private boolean isKeyboard = false;
    private boolean isVoice = true;

    private ArrayList<Chat> chatListData = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private MyService.MyBinder myBinder;
    private AudioRecorderUtils audioRecorderUtils;

    private int chatId;
    private String chatType;

    private long startTime = 0;
    private float startX;

    private final int REQUEST_IMAGE = 0x01;
    private final int REQUEST_VIDEO = 0x02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        EventBus.getDefault().register(this);
        bindMyService();

        getData();
        initViews();
        initRecentChatData();
    }

    private void getData() {
        Intent intent = getIntent();
        chatId = intent.getIntExtra("chatId", 0);                 //好友或者群组的ID
        chatType = intent.getStringExtra("chatType");               //群组还是好友
        String chatName = intent.getStringExtra("chatName");        //群组名或者好友名
        Log.e("聊天信息", "当前在跟" + chatType + "：ID为" + chatId + "的" + chatName + "聊天");
        initToolbar(chatName);

        //保存当前聊天对象的信息
        AppConfig.setUserCurrentChatId(ChatActivity.this, chatId);
        AppConfig.setUserCurrentChatType(ChatActivity.this, chatType);
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
        adapter = new ChatAdapter(ChatActivity.this, chatListData);
        recyclerView.setAdapter(adapter);
        edit_input = (EditText) findViewById(R.id.edit_input);
        edit_input.addTextChangedListener(textWatcher);

        imgbtn_emoji_keyboard = (ImageButton) findViewById(R.id.imgbtn_emoji_keyboard);
        imgbtn_more_send_close = (ImageButton) findViewById(R.id.imgbtn_more_send_close);
        imgbtn_voice_keyboard = (ImageButton) findViewById(R.id.imgbtn_voice_keyboard);
        ImageButton imgbtn_gallery = (ImageButton) findViewById(R.id.imgbtn_gallery);
        ImageButton imgbtn_video = (ImageButton) findViewById(R.id.imgbtn_video);
        btn_audio = (Button) findViewById(R.id.btn_audio);

        imgbtn_emoji_keyboard.setOnClickListener(this);
        imgbtn_more_send_close.setOnClickListener(this);
        imgbtn_voice_keyboard.setOnClickListener(this);
        imgbtn_gallery.setOnClickListener(this);
        imgbtn_video.setOnClickListener(this);
        btn_audio.setOnClickListener(this);
        btn_audio.setOnTouchListener(this);
    }

    /**
     * 加载最近的聊天消息，默认5条（QQ是15条）
     */
    private void initRecentChatData() {
        //如果是和好友聊天
        Log.e("加载聊天数据", "好友");
        ChatDao chatFriendDao = GreenDAOUtils.getInstance(ChatActivity.this).getChatDao();
        List<Chat> chatFriends = chatFriendDao.queryBuilder()
                .where(ChatDao.Properties.ChatType.eq(chatType))
                .whereOr(ChatDao.Properties.FromId.eq(chatId), ChatDao.Properties.ToId.eq(chatId))
                .limit(5)
                .orderDesc(ChatDao.Properties.Time)
                .build()
                .list();
        //倒序排列下
        Collections.reverse(chatFriends);
        for (Chat chatFriend : chatFriends) {
            Log.e("聊天内容", chatFriend.getContent());
            Log.e("聊天内容类型", chatFriend.getContentType());
            if ("audio".equals(chatFriend.getContentType())) {
                Log.e("聊天语音长度", chatFriend.getAudioLength() + "");
            }
        }
        chatListData.addAll(chatFriends);
        adapter.notifyDataSetChanged();
        int itemCount = adapter.getItemCount() - 1;
        if (itemCount > 0) {
            recyclerView.smoothScrollToPosition(itemCount);
        }

    }

    @Subscribe
    public void onEventMainThread(JSONObject jsonObject) {
        Log.e("聊天页面收到消息", jsonObject.toString());
        try {

            /**
             * 如果toWhich是friend，那么toId可能是自己的ID（朋友发的消息）或者朋友的Id（自己发的消息）
             *                    那么fromId可能是自己的ID（自己发的消息）或者朋友的ID（朋友发的消息）
             *
             * 如果toWhich是group，那么toId就是群组的Id
             *                   那么fromId可能是自己的ID（自己发的消息）或者群组中其他人的ID（群组中其他人发的消息）
             */
            String toWhich = jsonObject.getString("toWhich");   //可能是friend或者group
            int toId = jsonObject.getInt("toId");             //可能是我自己的ID或者对方的ID
            int fromId = jsonObject.getInt("fromId");         //可能是我自己的ID或者对方的ID

            String fromAvatar = jsonObject.getString("fromAvatar");
            String fromName = jsonObject.getString("fromName");
            String content = jsonObject.getString("content");
            String contentType = jsonObject.getString("contentType");
            String time = jsonObject.getString("time");

            Log.e("消息类型", contentType);

            if (chatType.equals(toWhich)) {
                //跟当前聊天类型匹配，是群组或者好友的消息
                if ("friend".equals(toWhich)) {
                    //当前在跟好友聊天，需判断是不是当前好友的消息
                    if ((toId == chatId && fromId == AppConfig.getUserId(ChatActivity.this)) //我发给当前朋友的消息
                            || (toId == AppConfig.getUserId(ChatActivity.this) && fromId == chatId)//朋友发给我的消息
                            ) {
                        //是跟当前好友的聊天消息
                        List<Chat> chatFriends = new ArrayList<>();
                        Chat chatFriend = new Chat();
                        chatFriend.setFromId(fromId);
                        chatFriend.setFromAvatar(fromAvatar);
                        chatFriend.setFromName(fromName);
                        chatFriend.setContent(content);
                        chatFriend.setContentType(contentType);
                        chatFriend.setToId(toId);
                        chatFriend.setTime(time);
                        chatFriend.setIsRead(true);             //消息已读
                        if ("audio".equals(contentType)) {
                            chatFriend.setAudioLength(jsonObject.getString("audioLength"));
                        }

                        chatFriends.add(chatFriend);
                        chatListData.addAll(chatFriends);

                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } else {
                        showOtherMsg(fromName + "：" + content);
                    }
                } else {
                    //当前在跟群组聊天，需判断是不是当前群组的消息
                    if (chatId == toId) {
                        List<Chat> chatGroups = new ArrayList<>();
                        Chat chatGroup = new Chat();
                        chatGroup.setContent(content);
                        chatGroup.setContentType(contentType);
                        chatGroup.setFromAvatar(fromAvatar);
                        chatGroup.setFromId(fromId);
                        chatGroup.setTime(time);
                        chatGroup.setToId(toId);
                        chatGroup.setIsRead(true);
                        if ("audio".equals(contentType)) {
                            chatGroup.setAudioLength(jsonObject.getString("audioLength"));
                        }
                        chatGroups.add(chatGroup);
                        chatListData.addAll(chatGroups);

                        Message msg = new Message();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    } else {
                        //不是当前群组的聊天消息，提示来消息了即可
                        showOtherMsg(fromName + "：" + content);
                    }
                }
            } else {
                //跟当前聊天类型不匹配，比如当前在跟好友聊天，来的是群组消息
                showOtherMsg(fromName + "：" + content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Json解析", "出错了");
        }
    }

    /**
     * 提醒其他好友或者群组来消息了
     *
     * @param str 消息内容
     */
    private void showOtherMsg(String str) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("otherMsg", str);
        msg.setData(bundle);
        msg.what = 1;
        handler.sendMessage(msg);
    }

    /**
     * 发送文字消息
     */
    private void sendTextMessage() {
        //发送数据示例
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fromId", AppConfig.getUserId(ChatActivity.this));
            jsonObject.put("fromName", AppConfig.getUserName(ChatActivity.this));
            jsonObject.put("fromAvatar", AppConfig.getUserAvatar(ChatActivity.this));
            jsonObject.put("toWhich", chatType);
            jsonObject.put("toId", chatId);
            jsonObject.put("content", edit_input.getText().toString().trim());
            jsonObject.put("contentType", "text");
            jsonObject.put("time", TimeUtils.getCurrentTime());

            myBinder.sendMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片消息
     */
    private void sendImageMessage(File file) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(
                file, //文件
                null, //文件名
                AppConfig.getQiniuToken(ChatActivity.this),//token
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置。res中的key就是资源的名字
                        Log.e("七牛云", key + ",\r\n " + info + ",\r\n " + res);

                        //发送数据示例
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("fromId", AppConfig.getUserId(ChatActivity.this));
                            jsonObject.put("fromName", AppConfig.getUserName(ChatActivity.this));
                            jsonObject.put("fromAvatar", AppConfig.getUserAvatar(ChatActivity.this));
                            jsonObject.put("toWhich", chatType);
                            jsonObject.put("toId", chatId);
                            jsonObject.put("content", "http://oe98z0mhz.bkt.clouddn.com/" + res.getString("key"));
                            jsonObject.put("contentType", "image");
                            jsonObject.put("time", TimeUtils.getCurrentTime());

                            myBinder.sendMessage(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
    }

    /**
     * 发送语音消息
     */
    private void sendAudioMessage(File file, final String audioLength) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(
                file, //文件
                null, //文件名
                AppConfig.getQiniuToken(ChatActivity.this),//token
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置。res中的key就是资源的名字
                        Log.e("七牛云，语音", key + ",\r\n " + info + ",\r\n " + res);

                        //发送数据示例
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("fromId", AppConfig.getUserId(ChatActivity.this));
                            jsonObject.put("fromName", AppConfig.getUserName(ChatActivity.this));
                            jsonObject.put("fromAvatar", AppConfig.getUserAvatar(ChatActivity.this));
                            jsonObject.put("toWhich", chatType);
                            jsonObject.put("toId", chatId);
                            jsonObject.put("content", "http://oe98z0mhz.bkt.clouddn.com/" + res.getString("key"));
                            jsonObject.put("contentType", "audio");
                            jsonObject.put("audioLength", audioLength);
                            jsonObject.put("time", TimeUtils.getCurrentTime());

                            myBinder.sendMessage(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
    }

    /**
     * 发送视频消息
     */
    private void sendVideoMessage(String videoPath) {
        //发送数据示例
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fromId", AppConfig.getUserId(ChatActivity.this));
            jsonObject.put("fromName", AppConfig.getUserName(ChatActivity.this));
            jsonObject.put("fromAvatar", AppConfig.getUserAvatar(ChatActivity.this));
            jsonObject.put("toWhich", chatType);
            jsonObject.put("toId", chatId);
            jsonObject.put("content", videoPath);
            jsonObject.put("contentType", "video");
            jsonObject.put("time", TimeUtils.getCurrentTime());

            myBinder.sendMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化语音
     */
    private void initAudioListener() {
        if (audioRecorderUtils == null) {
            audioRecorderUtils = new AudioRecorderUtils(ChatActivity.this);
        }
        audioRecorderUtils.setOnAudioStatusUpdateListener(new AudioRecorderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {

            }

            @Override
            public void onStop(String filePath, String audioLength) {
                Log.e("录音结束", "文件位置：" + filePath + "\n录音长度：" + audioLength);
                sendAudioMessage(new File(filePath), audioLength);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_voice_keyboard:
                //如果是“展示键盘”的状态，那么点击后展示录音按钮，并关闭键盘
                if (isVoice) {
                    imgbtn_voice_keyboard.setImageResource(R.mipmap.conversation_btn_messages_keyboard);
                    edit_input.setVisibility(View.GONE);
                    btn_audio.setVisibility(View.VISIBLE);
                    isKeyboard = true;
                    isVoice = false;
                    hideKeyboard();
                    initAudioListener();
                    return;
                }
                if (isKeyboard) {
                    imgbtn_voice_keyboard.setImageResource(R.mipmap.conversation_btn_messages_voice);
                    edit_input.setVisibility(View.VISIBLE);
                    btn_audio.setVisibility(View.GONE);
                    isKeyboard = false;
                    isVoice = true;
                    showKeyboard();
                    return;
                }
                break;
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

            case R.id.imgbtn_gallery:
                openImageGallery();
                break;
            case R.id.imgbtn_video:
                openRecordPage();
                break;
            case R.id.imgbtn_more_send_close:
                //如果是“展示更多”的状态，那么点击后展示更多的按钮，按钮状态改为“关闭更多”
                if (isMore) {
                    Toast.makeText(ChatActivity.this, "展示更多", Toast.LENGTH_SHORT).show();
                    imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_close);
                    isMore = false;
                    isClose = true;
                    isSend = false;

                    hideKeyboard();
                    return;
                }
                //如果是“关闭更多”的状态，那么点击后关闭更多的按钮，按钮状态改为“展示更多”
                if (isClose) {
                    Toast.makeText(ChatActivity.this, "关闭更多", Toast.LENGTH_SHORT).show();
                    imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_more);
                    isClose = false;
                    isMore = true;
                    isSend = false;

                    return;
                }
                //如果是“发送消息”的状态，那么点击后发送消息，按钮状态改为“展示更多”状态，不关闭键盘
                if (isSend) {
                    sendTextMessage();
                    edit_input.setText("");
                    imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_more);
                    isSend = false;
                    isMore = true;
                    isClose = false;
                    return;
                }
                break;
            case R.id.btn_audio:
                Toast.makeText(ChatActivity.this, "点击了按钮", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
        if ("friend".equals(chatType)) {
            Intent intent = new Intent(ChatActivity.this, UserProfileActivity.class);
            intent.putExtra("id", chatId);
            startActivity(intent);
        } else {
            //跳转到群组
            startActivity(new Intent(ChatActivity.this, GroupProfileActivity.class));
        }
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
                    sendImageMessage(new File(path));
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

                //Toast.makeText(this, "视频路径:" + videoPath + "图片路径:" + thumbnails[0], Toast.LENGTH_SHORT).show();
                Log.e("趣拍视频地址", videoPath);
                Log.e("趣拍缩略图地址", thumbnails[0]);
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.getId() == R.id.btn_audio) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    audioRecorderUtils.startRecord();
                    Log.e("录音开始时间", TimeUtils.getCurrentTime());
                    startTime = System.currentTimeMillis();
                    startX = motionEvent.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = motionEvent.getX();
                    if (endX - startX > 150) {
                        Log.e("录音取消", "取消发送");
                        audioRecorderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        Toast.makeText(ChatActivity.this, "录音已取消", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("录音结束时间", TimeUtils.getCurrentTime());
                        long endTime = System.currentTimeMillis();
                        long audioLength = endTime - startTime;
                        if (audioLength < 1000) {
                            //如果录制时间大于1秒可以发送
                            audioRecorderUtils.cancelRecord();    //取消录音（不保存录音文件）
                            Toast.makeText(ChatActivity.this, "录音时间不得少于1秒", Toast.LENGTH_SHORT).show();
                        } else if (audioLength > 1000 * 60) {
                            //如果录制时间大于一分钟禁止发送
                            audioRecorderUtils.cancelRecord();    //取消录音（不保存录音文件）
                            Toast.makeText(ChatActivity.this, "录音时间不得多于1分钟", Toast.LENGTH_SHORT).show();
                        } else {
                            audioRecorderUtils.stopRecord();        //结束录音（保存录音文件），并发送
                            Log.e("录音时间长度", audioLength / 6000 + "秒");
                        }
                    }
                    break;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbindService(connection);

        //设置当前聊天对象，表示没有
        AppConfig.setUserCurrentChatId(ChatActivity.this, 0);
        AppConfig.setUserCurrentChatType(ChatActivity.this, "");
    }

    /**
     * 处理消息事件
     * 0：刷新数据
     * 1：提醒其他好友或群组来消息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    String otherMsg = bundle.getString("otherMsg");
                    Toast.makeText(ChatActivity.this, otherMsg, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

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
                isClose = false;
            } else {
                imgbtn_more_send_close.setImageResource(R.mipmap.conversation_btn_messages_more);
                isSend = false;
                isMore = true;
                isClose = false;
            }
        }
    };

    /**
     * 绑定服务
     */
    private void bindMyService() {
        Intent bindIntent = new Intent(ChatActivity.this, MyService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (MyService.MyBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

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
            Toast.makeText(this, "插件没有初始化，无法获取 QupaiService", Toast.LENGTH_LONG).show();
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
                Log.e("趣拍云上传进度", "uuid:" + uuid + "；进度：" + percentsProgress + "%");
                //progress.setProgress(percentsProgress);
            }

            @Override
            public void onUploadError(String uuid, int errorCode, String message) {
                Log.e("趣拍云上传失败", "uuid:" + uuid + "；错误信息：" + errorCode + message);
            }

            @Override
            public void onUploadComplte(String uuid, int responseCode, String responseMessage) {
                //http://{DOMAIN}/v/{UUID}.mp4?token={ACCESS-TOKEN}
                //progress.setVisibility(View.GONE);

                //这里返回的uuid是你创建上传任务时生成的uuid.开发者可以使用其他作为标识
                //videoUrl返回的是上传成功的视频地址,imageUrl是上传成功的图片地址
                String videoUrl = QupaiSetting.domain + "/v/" + responseMessage + ".mp4" + "?token=" + AppConfig.getQupaiToken(ChatActivity.this);
                String imageUrl = QupaiSetting.domain + "/v/" + responseMessage + ".jpg" + "?token=" + AppConfig.getQupaiToken(ChatActivity.this);
                Log.e("趣拍云上传成功", "视频地址：" + videoUrl);
                Log.e("趣拍云上传成功", "缩略图地址：" + imageUrl);

                sendVideoMessage(videoUrl);
            }
        });

        String uuid = UUID.randomUUID().toString();
        Log.e("趣拍云认证", "accessToken：" + AppConfig.getQupaiToken(ChatActivity.this) + "；space：" + AppConfig.getUserId(ChatActivity.this));

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
