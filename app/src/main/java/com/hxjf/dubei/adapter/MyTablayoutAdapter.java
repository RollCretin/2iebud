package com.hxjf.dubei.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/6/9.
 */

public class MyTablayoutAdapter extends FragmentPagerAdapter {

    List<Fragment> list_fragment; //fragment列表
    List<String> list_title; //tab列表

    public MyTablayoutAdapter(FragmentManager fm, List<Fragment> list_fragment,List<String> list_title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_title = list_title;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_title.get(position);
    }
}
