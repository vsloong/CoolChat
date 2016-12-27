package com.cooloongwu.coolchat.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.ContactAdapter;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.entity.Contact;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.coolchat.utils.ToastUtils;
import com.cooloongwu.greendao.gen.ContactDao;

import java.util.ArrayList;
import java.util.List;

public class FriendSearchActivity extends BaseActivity {

    private LinearLayout layout_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);

        initToolBar();
        initViews();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        layout_points = (LinearLayout) findViewById(R.id.layout_points);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        //设置显示提交按钮
        searchView.setSubmitButtonEnabled(true);
        //一开始就处于显示SearchView的状态
        searchView.setIconifiedByDefault(false);
        //设置一开始就显示输入框
        searchView.setIconified(true);
        searchView.clearFocus();
        SearchView.SearchAutoComplete editText = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFriend(query.trim());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    searchFriend(newText);
                }
                return false;
            }
        });
    }

    private void searchFriend(String friend) {
        ContactDao contactDao = GreenDAOUtils.getInstance(FriendSearchActivity.this).getContactDao();
        List<Contact> contacts = contactDao.queryBuilder()
                .whereOr(ContactDao.Properties.UserId.eq(friend), ContactDao.Properties.Name.eq(friend), ContactDao.Properties.Name.like("%" + friend + "%"))
                .build()
                .list();

        if (contacts.size() == 0) {
            ToastUtils.showShort(getApplicationContext(), "本地无好友，去网络搜索");
        } else {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            ArrayList<Contact> listData = new ArrayList<>();
            listData.addAll(contacts);
            ContactAdapter contactAdapter = new ContactAdapter(FriendSearchActivity.this, listData);
            recyclerView.setLayoutManager(new LinearLayoutManager(FriendSearchActivity.this));
            recyclerView.setAdapter(contactAdapter);
            contactAdapter.notifyDataSetChanged();
            layout_points.setVisibility(View.GONE);
        }
    }
}
