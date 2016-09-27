package com.cooloongwu.coolchat.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.ConversationAdapter;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.BaseFragment;
import com.cooloongwu.coolchat.entity.Contact;
import com.cooloongwu.coolchat.entity.Conversation;
import com.cooloongwu.coolchat.utils.TimeUtils;
import com.cooloongwu.greendao.gen.ContactDao;
import com.cooloongwu.greendao.gen.ConversationDao;
import com.cooloongwu.greendao.gen.DaoMaster;
import com.cooloongwu.greendao.gen.DaoSession;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 对话列表界面
 */
public class ConversationFragment extends BaseFragment {


    private ConversationAdapter adapter;
    private ArrayList<Conversation> listData = new ArrayList<>();

    private RecyclerView recyclerView;
    private LinearLayout layout_initiatechat;

    private DaoSession daoSession;
    private ConversationDao conversationDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        initViews(view);
        initGreenDAO();
        initListData();
        return view;
    }

    /**
     * 加载聊天会话列表页的数据
     */
    private void initListData() {
        //加载聊天列表数据
        conversationDao = daoSession.getConversationDao();
        List<Conversation> conversations = conversationDao.queryBuilder().build().list();
        listData.clear();
        listData.addAll(conversations);
        adapter.notifyDataSetChanged();     //为什么更新数据经常报错

        if (listData.isEmpty()) {
            layout_initiatechat.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layout_initiatechat.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layout_initiatechat = (LinearLayout) view.findViewById(R.id.layout_initiatechat);

        adapter = new ConversationAdapter(getActivity(), listData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化GreenDAO的一些操作
     */
    private void initGreenDAO() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getActivity(), AppConfig.getUserDB(getActivity()), null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();

    }

    /**
     * 将数据插入数据库
     */
    private void insertOrUpdateConversationDB(Conversation conversation) {
        conversationDao = daoSession.getConversationDao();
        Conversation result = conversationDao.queryBuilder()
                .where(ConversationDao.Properties.MultiId.eq(conversation.getMultiId()))
                .build()
                .unique();
        if (result != null) {
            result.setContent(conversation.getContent());
            result.setContentType(conversation.getContentType());
            conversationDao.update(result);
        } else {
            conversationDao.insert(conversation);
        }
        //更新页面
        handleConversation.sendEmptyMessage(0);
    }

    /**
     * 将数据从数据库删除
     */
    private void deleteFromDB(Conversation conversation) {
        Log.e("要删除的ID", conversation.getId() + "");
        conversationDao.delete(conversation);
        //更新页面
        handleConversation.sendEmptyMessage(0);
    }


    /**
     * 删除数据后的更新页面
     *
     * @param event 事件
     */
    @Subscribe
    public void onEventMainThread(Conversation event) {
        //更新页面
        deleteFromDB(event);
    }

    /**
     * 接收到消息后的更新页面
     *
     * @param jsonObject 数据
     * @throws JSONException JSON异常
     */
    @Subscribe
    public void onEventMainThread(JSONObject jsonObject) throws JSONException {
        int chatId;                     //默认与其他人或者群组聊天的ID（非自己）
        String chatName = "";           //默认其他人或者群组的名称（非自己）
        String chatAvatar = "";         //默认其他人或者群组的头像（非自己）
        String chatType = jsonObject.getString("toWhich");   //聊天的类型
        int fromId = jsonObject.getInt("fromId");
        int toId = jsonObject.getInt("toId");
        String content = jsonObject.getString("content");
        String contentType = jsonObject.getString("contentType");
        if ("friend".equals(chatType)) {
            //如果是好友发来的那么要判断好友ID是fromId还是toId
            if (fromId == AppConfig.getUserId(getActivity())) {
                chatId = toId;
            } else {
                chatId = fromId;
            }
            //根据ID去查询好友的其他信息
            ContactDao contactDao = daoSession.getContactDao();
            Contact contact = contactDao.queryBuilder()
                    .where(ContactDao.Properties.UserId.eq(chatId))
                    .build()
                    .unique();
            Log.e("得到的联系人的数据", contact.getName() + "");
            chatName = contact.getName();
            chatAvatar = contact.getAvatar();
        } else {
            //如果在跟群组聊天，那么toId就是群组的ID
            chatId = toId;
            chatType = "group";
        }
        Conversation conversation = new Conversation(null, chatId, chatName, chatAvatar, chatType, content, contentType, TimeUtils.getCurrentTime());

        insertOrUpdateConversationDB(conversation);
    }

    /**
     * 更新页面
     */
    private Handler handleConversation = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initListData();
        }
    };
}
