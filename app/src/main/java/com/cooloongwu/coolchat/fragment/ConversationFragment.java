package com.cooloongwu.coolchat.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.ConversationAdapter;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.BaseFragment;
import com.cooloongwu.coolchat.entity.Conversation;
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

    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
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

    @Subscribe
    public void onEventMainThread(Conversation event) {
        initListData();
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
        devOpenHelper = new DaoMaster.DevOpenHelper(getActivity(), AppConfig.DB_NAME, null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        conversationDao = daoSession.getConversationDao();
    }

    /**
     * 将数据插入数据库
     */
    private void insertIntoDB(Conversation conversation) {
        conversationDao.insert(conversation);
        //initListData();
    }

    /**
     * 将数据从数据库删除
     */
    private void deleteFromDB(Conversation conversation) {
        //conversationDao.deleteByKey(conversation.getId());
        conversationDao.deleteAll();
    }

    @Subscribe
    public void onEventMainThread(JSONObject jsonObject) throws JSONException {
        int chatId = 0;             //默认与其他人或者群组聊天的ID（非自己）
        //String chatName = "";       //默认其他人或者群组的名称（非自己）
        String chatType = jsonObject.getString("toWhich");   //聊天的类型
        int fromId = jsonObject.getInt("fromId");
        int toId = jsonObject.getInt("toId");
        String fromName = jsonObject.getString("fromName");
        //String toName = jsonObject.getString("toName");
        String fromAvatar = jsonObject.getString("fromAvatar");
        //String toAvatar = jsonObject.getString("toAvatar");
        String content = jsonObject.getString("content");
        String contentType = jsonObject.getString("contentType");
        if ("friend".equals(chatType)) {
            //如果是好友发来的那么要判断好友ID是fromId还是toId
            if (fromId == AppConfig.getUserId(getActivity())) {
                chatId = toId;
                //chatName = toName;
            } else {
                chatId = fromId;
                //chatName = fromName;
            }
        } else {
            //如果在跟群组聊天，那么toId就是群组的ID
            chatId = toId;
            //chatName = fromName;
            chatType = "group";
        }
        Conversation conversation = new Conversation(null, chatId, "一个名字", "头像", chatType, content, contentType, "12:23");

        insertIntoDB(conversation);
    }
}
