package com.hxjf.dubei.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MyTablayoutAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.ui.fragment.BS_BookAnalysisFragment;
import com.hxjf.dubei.ui.fragment.BS_BookFragment;
import com.hxjf.dubei.ui.fragment.BS_NoteFactoryFragment;
import com.hxjf.dubei.ui.fragment.Borrow_InFragment;
import com.hxjf.dubei.ui.fragment.Borrow_OutFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/2 0002.
 * 我的借阅
 */

public class MyBorrowDetailActivity extends BaseActivity {
    @BindView(R.id.my_borrow_tablayout)
    TabLayout myBorrowTablayout;
    @BindView(R.id.my_borrow_viewpager)
    ViewPager myBorrowViewpager;
    private List<Fragment> list_fragment;
    private List<String> list_title;
    private Borrow_OutFragment borrowOutFragment;
    private Borrow_InFragment borrowInFragment;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_borrow;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        //title列表
        list_title = new ArrayList<String>();
        list_title.add("借入");
        list_title.add("借出");
        //Fragment列表
        list_fragment = new ArrayList<Fragment>();

        //初始化fragment--书籍、好书拆读、笔记工厂
        borrowInFragment = new Borrow_InFragment();
        borrowOutFragment = new Borrow_OutFragment();

        list_fragment.add(borrowInFragment);
        list_fragment.add(borrowOutFragment);

        myBorrowTablayout.setTabMode(TabLayout.MODE_FIXED);
        myBorrowTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        myBorrowTablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(myBorrowTablayout, 70, 70);
            }
        });

        myBorrowTablayout.addTab(myBorrowTablayout.newTab().setText(list_title.get(0)));
        myBorrowTablayout.addTab(myBorrowTablayout.newTab().setText(list_title.get(1)));


            MyTablayoutAdapter myAdapter = new MyTablayoutAdapter(this.getSupportFragmentManager(), list_fragment, list_title);
        myBorrowViewpager.setAdapter(myAdapter);
        //tablayout绑定Viewpager
        myBorrowTablayout.setupWithViewPager(myBorrowViewpager);

    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }


}
