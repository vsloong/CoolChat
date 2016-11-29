package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.entity.Group;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.greendao.gen.GroupDao;

public class GroupProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView text_name;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);

        initToolbar();
        initViews();
        initData();
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
        text_name = (TextView) findViewById(R.id.text_name);
        LinearLayout layout_change_name = (LinearLayout) findViewById(R.id.layout_change_name);

        layout_change_name.setOnClickListener(this);
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
        text_name.setText(groupName);
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
