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
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.greendao.gen.GroupDao;
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
        getGroupListFromDB();
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

    private void getGroupListFromDB() {

        GroupDao groupDao = GreenDAOUtils.getInstance(GroupActivity.this).getGroupDao();
        List<Group> groups = groupDao.queryBuilder().build().list();
        if (groups.size() > 0) {
            text_group_num.setText("群组（" + groups.size() + "）");
            layout_create_group.setVisibility(View.GONE);
            listData.addAll(groups);
            adapter.notifyDataSetChanged();
        }
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
