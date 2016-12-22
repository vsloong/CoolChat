package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.cooloongwu.coolchat.entity.Group;
import com.cooloongwu.coolchat.entity.GroupUsers;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.greendao.gen.GroupDao;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GroupProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String groupName;
    private String groupAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);

        initToolbar();
        initData();
        initViews();
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
        ArrayList<GroupUsers> listData = new ArrayList<>();
        List<GroupUsers> groupUserses = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            GroupUsers groupUsers = new GroupUsers();
            groupUsers.setUserName("用户" + i);
            groupUsers.setUserAvatar("http://a.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=91f6df96f503738dde1f0426862b9c67/6609c93d70cf3bc7b7ca6ddad200baa1cd112a39.jpg");
            groupUserses.add(groupUsers);
        }
        listData.addAll(groupUserses);
        GroupUsersAdapter adapter = new GroupUsersAdapter(this, listData);
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
        int groupId = intent.getIntExtra("id", 0);

        GroupDao groupDao = GreenDAOUtils.getInstance(this).getGroupDao();
        Group group = groupDao.queryBuilder()
                .where(GroupDao.Properties.GroupId.eq(groupId))
                .build()
                .unique();

        groupName = group.getGroupName();
        groupAvatar = group.getGroupAvatar();
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
