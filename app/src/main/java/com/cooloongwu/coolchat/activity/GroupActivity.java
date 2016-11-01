package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.GroupAdapter;
import com.cooloongwu.coolchat.base.Api;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.entity.Group;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layout_create_group;
    private RecyclerView recyclerView;
    private TextView text_group_num;
    private ArrayList<Group> listData = new ArrayList<>();
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initToolbar();
        initViews();
        initData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("群组");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        TextView text_create_group = (TextView) findViewById(R.id.text_create_group);
        text_group_num = (TextView) findViewById(R.id.text_group_num);
        layout_create_group = (LinearLayout) findViewById(R.id.layout_create_group);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(GroupActivity.this));
        adapter = new GroupAdapter(this, listData);
        recyclerView.setAdapter(adapter);
        text_create_group.setOnClickListener(this);
    }

    private void initData() {
        Api.getGroupsList(GroupActivity.this, AppConfig.getUserId(GroupActivity.this), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("获取群组列表成功", response.toString());
                try {
                    int status = response.getInt("status");
                    switch (status) {
                        case 1:
                            JSONArray jsonArray = response.getJSONArray("groups");
                            int groupNum = jsonArray.length();
                            text_group_num.setText("群组（" + groupNum + "）");
                            if (groupNum > 0) {
                                layout_create_group.setVisibility(View.GONE);
                                List<Group> groups = new ArrayList<>();
                                for (int i = 0; i < groupNum; i++) {
                                    JSONObject tempGroup = jsonArray.getJSONObject(i);
                                    Group group = new Group();
                                    group.setGroupId(tempGroup.getInt("groupId"));
                                    group.setGroupName(tempGroup.getString("groupName"));
                                    group.setGroupAvatar(tempGroup.getString("groupAvatar"));
                                    groups.add(group);
                                }
                                listData.addAll(groups);
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        case 0:
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_create_group:
                startActivity(new Intent(GroupActivity.this, CreateGroupActivity.class));
                break;
            default:
                break;
        }
    }
}
