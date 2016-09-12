package com.cooloongwu.coolchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 对话列表界面
 */
public class ConversationFragment extends Fragment {

    public static final String ARG = "ConversationFragment";
    private int page;

    public static ConversationFragment newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARG, page);
        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }
}
