package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MyTablayoutAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.ui.fragment.Person_BookAnalysisFragment;
import com.hxjf.dubei.ui.fragment.Person_BookFragment;
import com.hxjf.dubei.ui.fragment.Person_NoteFactoryFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/8/14.
 */

public class personBookListActivity extends BaseActivity {
    @BindView(R.id.person_booklist_return)
    ImageView personBooklistReturn;
    @BindView(R.id.person_booklist_name)
    TextView personBooklistName;
    @BindView(R.id.person_booklistshelf_tablayout)
    TabLayout personBooklistshelfTablayout;
    @BindView(R.id.person_booklist_viewpager)
    ViewPager personBooklistViewpager;
    private UserDetailBean.ResponseDataBean detailBean;
    private ArrayList<String> list_title;
    private ArrayList<Fragment> list_fragment;
    private Person_BookFragment personbookFragment;
    private Person_BookAnalysisFragment personbookAnalysisFragment;
    private Person_NoteFactoryFragment personnoteFactoryFragment;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_person_booklist;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        detailBean = (UserDetailBean.ResponseDataBean) intent.getSerializableExtra("detailBean");
        String userId = intent.getStringExtra("userId");
        personBooklistName.setText(detailBean.getNickName());

        //title列表
        list_title = new ArrayList<String>();
        list_title.add("书籍");
        list_title.add("好书拆读");
        list_title.add("笔记工厂");
        //Fragment列表
        list_fragment = new ArrayList<Fragment>();

        //初始化fragment--书籍、好书拆读、笔记工厂

        personbookFragment = new Person_BookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        personbookFragment.setArguments(bundle);

        personbookAnalysisFragment = new Person_BookAnalysisFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("userId", userId);
        personbookAnalysisFragment.setArguments(bundle1);

        personnoteFactoryFragment = new Person_NoteFactoryFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("userId", userId);
        personnoteFactoryFragment.setArguments(bundle);

        list_fragment.add(personbookFragment);
        list_fragment.add(personbookAnalysisFragment);
        list_fragment.add(personnoteFactoryFragment);

        //请求数据-- 书籍、拆读、笔记的数量

        personBooklistshelfTablayout.setTabMode(TabLayout.MODE_FIXED);
        personBooklistshelfTablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        personBooklistshelfTablayout.addTab(personBooklistshelfTablayout.newTab().setText(list_title.get(0)));
        personBooklistshelfTablayout.addTab(personBooklistshelfTablayout.newTab().setText(list_title.get(1)));
        personBooklistshelfTablayout.addTab(personBooklistshelfTablayout.newTab().setText(list_title.get(2)));

        MyTablayoutAdapter myAdapter = new MyTablayoutAdapter(this.getSupportFragmentManager(), list_fragment, list_title);
        personBooklistViewpager.setAdapter(myAdapter);
        //tablayout绑定Viewpager
        personBooklistshelfTablayout.setupWithViewPager(personBooklistViewpager);
    }

    @OnClick(R.id.person_booklist_return)
    public void onClick() {
        finish();
    }
}
