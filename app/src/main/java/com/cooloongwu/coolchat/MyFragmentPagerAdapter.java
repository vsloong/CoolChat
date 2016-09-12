package com.cooloongwu.coolchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
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
                return ConversationFragment.newInstance(position);
            case 1:
                return new ContactFragment();
            default:
                return ConversationFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
