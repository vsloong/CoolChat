package com.cooloongwu.coolchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.activity.UserProfileActivity;
import com.cooloongwu.coolchat.base.BaseFragment;


/**
 * 联系人界面
 */
public class ContactFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        LinearLayout layout_callme = (LinearLayout) view.findViewById(R.id.layout_callme);

        layout_callme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserProfileActivity.class);
                intent.putExtra("name", "CooLoongWu2");
                startActivity(intent);
            }
        });
    }

}
