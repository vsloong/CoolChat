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
import com.cooloongwu.coolchat.base.GreenDAO;
import com.cooloongwu.coolchat.entity.Contact;
import com.cooloongwu.coolchat.entity.Conversation;
import com.cooloongwu.greendao.gen.ContactDao;
import com.cooloongwu.greendao.gen.ConversationDao;

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
        initListData();
        return view;
    }

    /**
     * 加载聊天会话列表页的数据
     */
    private void initListData() {
        //加载聊天列表数据
        conversationDao = GreenDAO.getInstance(getActivity()).getConversationDao();
        List<Conversation> conversations = conversationDao.queryBuilder().build().list();
        listData.clear();
        listData.addAll(conversations);
        adapter.notifyDataSetChanged();

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
     * 将数据插入或者更新数据库
     */
    private void insertOrUpdateConversationDB(Conversation conversation) {
        conversationDao = GreenDAO.getInstance(getActivity()).getConversationDao();
        Conversation result = conversationDao.queryBuilder()
                .where(ConversationDao.Properties.MultiId.eq(conversation.getMultiId()))
                .build()
                .unique();
        if (result != null) {
            result.setContent(conversation.getContent());
            result.setContentType(conversation.getContentType());
            result.setUnReadNum(conversation.getUnReadNum());
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
        if (event.getId() == null) {
            //只更新页面，主要是处理未读消息
            handleConversation.sendEmptyMessage(0);
        } else {
            //删除该数据，并更新页面
            deleteFromDB(event);
        }
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
        String time = jsonObject.getString("time");
        if ("friend".equals(chatType)) {
            //如果是好友发来的那么要判断好友ID是fromId还是toId
            if (fromId == AppConfig.getUserId(getActivity())) {
                chatId = toId;
            } else {
                chatId = fromId;
            }
            //根据ID去查询好友的其他信息
            ContactDao contactDao = GreenDAO.getInstance(getActivity()).getContactDao();
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

        int unReadNum;
        if (chatId == AppConfig.getUserCurrentChatId(getActivity())
                && chatType.equals(AppConfig.getUserCurrentChatType(getActivity()))) {
            //来的是跟当前好友或者群组的聊天消息，则未读消息数变为0
            unReadNum = 0;
            Log.e("未读消息无", "" + unReadNum);
        } else {
            //来的不是跟当前好友或者群组的聊天消息，则展示新消息的未读数
            unReadNum = getConversationUnread(chatId, chatType) + 1;
            Log.e("未读消息有", "" + unReadNum);
        }

        Conversation conversation = new Conversation(null,
                chatId,             //聊天对象的ID
                unReadNum,          //未读消息数量
                chatName,           //聊天对象的名称
                chatAvatar,         //聊天对象的头像
                chatType,           //聊天对象的类型（好友还是群组）
                content,            //聊天内容
                contentType,        //聊天内容类型（文字图片等）
                time                //聊天消息的时间
        );
        //将当前聊天信息插入到数据库
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

    /**
     * 得到未读消息数
     *
     * @param chatId   聊天对象ID
     * @param chatType 聊天类型
     * @return 未读消息数
     */
    private int getConversationUnread(int chatId, String chatType) {
        ConversationDao conversationDao = GreenDAO.getInstance(getActivity()).getConversationDao();
        Conversation result = conversationDao.queryBuilder()
                .where(ConversationDao.Properties.MultiId.eq(chatId), ConversationDao.Properties.Type.eq(chatType))
                //.and(ConversationDao.Properties.Type.eq(chatType),ConversationDao.Properties.MultiId.eq(chatId))
                .build()
                .unique();
        if (result != null) {
            return result.getUnReadNum();
        } else {
            return 0;
        }
    }
}
