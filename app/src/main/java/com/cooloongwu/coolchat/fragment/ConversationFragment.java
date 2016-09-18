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
        //初始化数据库并操作
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getActivity(), AppConfig.DB_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        ConversationDao conversationDao = daoSession.getConversationDao();

        //插入一条数据
        //Conversation conversation = new Conversation(null, "742420210", "龙隆蟀舞", "", "你好", "12:34", "group");
        //conversationDao.insert(conversation);
        //删除一条数据
        //conversationDao.deleteByKey(conversations.get(0).getId());

        List<Conversation> conversations = conversationDao.queryBuilder().build().list();

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
}
