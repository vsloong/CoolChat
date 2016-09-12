package com.cooloongwu.coolchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Fragment的适配器类
 * Created by CooLoongWu on 2016-9-10 16:20.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private int pageCount;

    public MyFragmentPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.pageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ConversationFragment();
            case 1:
                return new ContactFragment();
            default:
                return new ConversationFragment();
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
