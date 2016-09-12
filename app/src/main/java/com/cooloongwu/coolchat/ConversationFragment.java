package com.cooloongwu.coolchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cooloongwu.coolchat.adapter.ConversationAdapter;
import com.cooloongwu.coolchat.bean.ConversationBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 对话列表界面
 */
public class ConversationFragment extends Fragment {


    private ConversationAdapter adapter;
    private ArrayList<ConversationBean> listData = new ArrayList<>();

    private RecyclerView recyclerView;
    private LinearLayout layout_initiatechat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        initViews(view);
        initData();

        return view;
    }

    /**
     * 加载聊天会话列表页的数据
     */
    private void initData() {
        List<ConversationBean> conversationBeans = new ArrayList<>();
        ConversationBean conversationBean = new ConversationBean();
        conversationBean.setAvatar("");
        conversationBean.setName("CooLoongWu");
        conversationBean.setContent("龙隆蟀舞：加油，好好做哦！");
        conversationBean.setTime("16:05");
        conversationBeans.add(conversationBean);
        listData.addAll(conversationBeans);
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
}
