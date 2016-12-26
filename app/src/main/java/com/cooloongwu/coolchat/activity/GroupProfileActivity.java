package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.GroupUsersAdapter;
import com.cooloongwu.coolchat.base.Api;
import com.cooloongwu.coolchat.entity.Group;
import com.cooloongwu.coolchat.entity.GroupUsers;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.greendao.gen.GroupDao;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class GroupProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String groupName;
    private String groupAvatar;
    private int groupId;

    private ArrayList<GroupUsers> listData = new ArrayList<>();
    private GroupUsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);

        initToolbar();
        initData();
        initViews();
        getGroupUsersList();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("群组信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViews() {
        TextView text_name = (TextView) findViewById(R.id.text_name);
        ImageView img_group_avatar = (ImageView) findViewById(R.id.img_group_avatar);

        LinearLayout layout_change_name = (LinearLayout) findViewById(R.id.layout_change_name);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(GroupProfileActivity.this, 4));

        adapter = new GroupUsersAdapter(this, listData);
        recyclerView.setAdapter(adapter);

        layout_change_name.setOnClickListener(this);
        text_name.setText(groupName);
        if (!TextUtils.isEmpty(groupAvatar)) {
            Picasso.with(this)
                    .load(groupAvatar)
                    .into(img_group_avatar);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        groupId = intent.getIntExtra("id", 0);

        GroupDao groupDao = GreenDAOUtils.getInstance(this).getGroupDao();
        Group group = groupDao.queryBuilder()
                .where(GroupDao.Properties.GroupId.eq(groupId))
                .build()
                .unique();

        groupName = group.getGroupName();
        groupAvatar = group.getGroupAvatar();
    }

    private void getGroupUsersList() {
        Api.getGroupUsersList(GroupProfileActivity.this, groupId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e("群组用户的数据：" + response.toString());

                try {
                    JSONArray users = response.getJSONArray("users");
                    List<GroupUsers> groupUserses = new ArrayList<>();
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        GroupUsers groupUsers = new GroupUsers();
                        groupUsers.setUserId(user.getInt("id"));
                        groupUsers.setUserName(user.getString("name"));
                        groupUsers.setUserAvatar(user.getString("avatar"));
                        groupUsers.setUserSex(user.getString("sex"));
                        groupUserses.add(groupUsers);
                    }
                    listData.addAll(groupUserses);
                    adapter.notifyDataSetChanged();
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
            case R.id.layout_change_name:
                Intent intent = new Intent(GroupProfileActivity.this, NameActivity.class);
                intent.putExtra("type", "changeGroupName");
                intent.putExtra("name", groupName);
                startActivityForResult(intent, NameActivity.REQUEST_CHANGEGROUPNAME);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NameActivity.REQUEST_CHANGEGROUPNAME && resultCode == NameActivity.REQUEST_CHANGEGROUPNAME) {
            if (data.hasExtra("name")) {
                LogUtils.e("修改的后的群组名", data.getStringExtra("name"));
            }
        } else {
            LogUtils.e("修改的后的群组名", "未修改");
        }

    }
}
