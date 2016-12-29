package com.cooloongwu.coolchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.MyFragmentPagerAdapter;
import com.cooloongwu.coolchat.base.Api;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.cooloongwu.coolchat.entity.Contact;
import com.cooloongwu.coolchat.entity.Group;
import com.cooloongwu.coolchat.utils.GreenDAOUtils;
import com.cooloongwu.coolchat.utils.ToastUtils;
import com.cooloongwu.greendao.gen.ContactDao;
import com.cooloongwu.greendao.gen.GroupDao;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private FloatingActionMenu fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        getFriendsList();
        getGroupsList();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fam = (FloatingActionMenu) findViewById(R.id.fam);
        FloatingActionButton fab_invite_friend = (FloatingActionButton) findViewById(R.id.fab_invite_friend);
        FloatingActionButton fab_create_group = (FloatingActionButton) findViewById(R.id.fab_create_group);
        FloatingActionButton fab_new_chat = (FloatingActionButton) findViewById(R.id.fab_new_chat);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_fab_bg);

        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });

        fab_invite_friend.setOnClickListener(this);
        fab_create_group.setOnClickListener(this);
        fab_new_chat.setOnClickListener(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ImageButton imgbtn_edit = (ImageButton) navigationView.getHeaderView(0).findViewById(R.id.imgbtn_edit);
        ImageView img_avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        TextView text_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.text_name);
        text_name.setText(AppConfig.getUserName(MainActivity.this));
        Picasso.with(MainActivity.this)
                .load(AppConfig.getUserAvatar(MainActivity.this))
                .into(img_avatar);

        imgbtn_edit.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.main_tab_icon_conversation));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.main_tab_icon_contact));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), 2); //表示有两个选项
        viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager);//不使用该方法是因为它会把Tab的图标弄没，暂不清楚怎么回事
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            default:
                showToast("暂未开发");
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                startActivity(new Intent(MainActivity.this, TestActivity.class));
                break;
        }
        return true;
    }

    private void showToast(String msg) {
        ToastUtils.showShort(getApplicationContext(), msg);
    }

    @Override
    public void onClick(View view) {
        int s = view.getId();
        switch (s) {
            case R.id.fab_create_group:
                fam.close(true);
                Intent intent = new Intent(MainActivity.this, NameActivity.class);
                intent.putExtra("type", "createGroup");
                startActivity(intent);

                break;
            case R.id.fab_invite_friend:
                fam.close(true);
                startActivity(new Intent(MainActivity.this, FriendSearchActivity.class));
                break;
            case R.id.fab_new_chat:
                fam.close(true);
                break;
            case R.id.imgbtn_edit:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
                    }
                };
                Handler handler = new Handler();
                handler.postDelayed(runnable, 500);
                break;
            default:
                break;
        }
    }

    private void getFriendsList() {
        Api.getFriendsList(MainActivity.this, AppConfig.getUserId(MainActivity.this), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e("获取朋友列表成功：" + response.toString());
                try {
                    int status = response.getInt("status");
                    switch (status) {
                        case 0:
                            //没有好友
                            break;
                        case 1:
                            //有好友
                            JSONArray jsonArray = response.getJSONArray("friends");
                            List<Contact> contacts = new ArrayList<>();
                            int friendsNum = jsonArray.length();
                            for (int i = 0; i < friendsNum; i++) {
                                JSONObject user = jsonArray.getJSONObject(i);
                                Contact contact = new Contact();
                                contact.setUserId(user.getInt("userId"));
                                contact.setName(user.getString("name"));
                                contact.setAvatar(user.getString("avatar"));
                                contact.setSex(user.getString("sex"));
                                contacts.add(contact);

                                insertOrUpdateContactDB(contact);
                            }
                            break;
                        default:
                            break;
                    }

                    //通知联系人Fragment页面刷新
                    EventBus.getDefault().post(new Contact());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                LogUtils.e("获取朋友数据失败", "" + statusCode);
            }
        });
    }

    private void getGroupsList() {
        Api.getGroupsList(MainActivity.this, AppConfig.getUserId(MainActivity.this), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e("获取群组列表成功：" + response.toString());
                try {
                    int status = response.getInt("status");
                    switch (status) {
                        case 0:
                            //没有群组
                            break;
                        case 1:
                            //有群组
                            JSONArray jsonArray = response.getJSONArray("groups");
                            int groupNum = jsonArray.length();
                            if (groupNum > 0) {
                                List<Group> groups = new ArrayList<>();
                                for (int i = 0; i < groupNum; i++) {
                                    JSONObject tempGroup = jsonArray.getJSONObject(i);
                                    Group group = new Group();
                                    group.setGroupId(tempGroup.getInt("groupId"));
                                    group.setGroupName(tempGroup.getString("groupName"));
                                    group.setGroupAvatar(tempGroup.getString("groupAvatar"));
                                    groups.add(group);

                                    insertOrUpdateGroupDB(group);
                                }
                            }
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
                LogUtils.e("获取群组数据失败", "" + statusCode);
            }
        });
    }

    /**
     * 插入或者更新朋友数据
     *
     * @param contact 实体类
     */
    private void insertOrUpdateContactDB(Contact contact) {
        ContactDao contactDao = GreenDAOUtils.getInstance(MainActivity.this).getContactDao();
        Contact result = contactDao.queryBuilder()
                .where(ContactDao.Properties.UserId.eq(contact.getUserId()))        //判断是否有该ID
                .build()
                .unique();
        if (result != null) {
            //如果有该用户
            result.setSex(contact.getSex());
            result.setName(contact.getName());
            result.setAvatar(contact.getAvatar());
            result.setPhone(contact.getPhone());
            contactDao.update(result);
        } else {
            contactDao.insert(contact);
        }
    }

    /**
     * 插入或者更新群组数据
     *
     * @param group 实体类
     */
    private void insertOrUpdateGroupDB(Group group) {
        GroupDao groupDao = GreenDAOUtils.getInstance(MainActivity.this).getGroupDao();
        Group result = groupDao.queryBuilder()
                .where(GroupDao.Properties.GroupId.eq(group.getGroupId()))      //判断是否有该ID
                .build()
                .unique();
        if (result != null) {
            //如果有该用户
            result.setGroupAvatar(group.getGroupAvatar());
            result.setGroupName(group.getGroupName());
            result.setCreateTime(group.getCreateTime());
            groupDao.update(result);
        } else {
            groupDao.insert(group);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(MainActivity.this, FriendSearchActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
