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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooloongwu.coolchat.R;
import com.cooloongwu.coolchat.adapter.MyFragmentPagerAdapter;
import com.cooloongwu.coolchat.base.AppConfig;
import com.cooloongwu.coolchat.base.BaseActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private FloatingActionMenu fam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
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
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        startActivity(new Intent(MainActivity.this, TestActivity.class));
        showToast("暂未开发");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showToast(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int s = view.getId();
        switch (s) {
            case R.id.fab_create_group:
                fam.close(true);
                break;
            case R.id.fab_invite_friend:
                fam.close(true);
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
}
