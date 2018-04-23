package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MyTablayoutAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BookClassifyBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.fragment.HotBookItemFragment;
import com.hxjf.dubei.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/1.
 */

public class HotBookActivity extends BaseActivity {
    @BindView(R.id.hot_book_back)
    ImageView hotBookBack;
    @BindView(R.id.hot_book_serach)
    ImageView hotBookSerach;
    @BindView(R.id.hot_book_tablayout)
    TabLayout hotBookTablayout;
    @BindView(R.id.hot_book_viewpager)
    ViewPager hotBookViewpager;
    private List<BookClassifyBean.ResponseDataBean.MenuInfoBean> menuInfoList;
    private ArrayList<Fragment> list_fragment;
    private ArrayList<String> list_title;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_hot_book;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        boolean searchSwitch = SPUtils.getBoolean(HotBookActivity.this, "searchSwitch", false);
        if (searchSwitch){
            hotBookSerach.setVisibility(View.VISIBLE);
        }

        Call<BookClassifyBean> bookClassifyCall = ReaderRetroift.getInstance(this).getApi().bookClassifyCall("bookClassify");
        bookClassifyCall.enqueue(new Callback<BookClassifyBean>() {
            @Override
            public void onResponse(Call<BookClassifyBean> call, Response<BookClassifyBean> response) {
                BookClassifyBean bean = response.body();
                if (bean != null && bean.getResponseData() != null && bean.getResponseData().getMenuInfo() != null) {
                    menuInfoList = bean.getResponseData().getMenuInfo();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BookClassifyBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        list_fragment = new ArrayList<Fragment>();
        list_title = new ArrayList<String>();
        //添加最新
        list_title.add("最新");
        HotBookItemFragment recentfragment = new HotBookItemFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("ITEM_DATA", "recent");
        recentfragment.setArguments(bundle1);
        list_fragment.add(recentfragment);

        for (int i = 0; i < menuInfoList.size(); i++) {
           HotBookItemFragment itemfragment = new HotBookItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ITEM_DATA", menuInfoList.get(i).getText());
            itemfragment.setArguments(bundle);
            list_fragment.add(itemfragment);
            list_title.add(menuInfoList.get(i).getValue());
        }

        hotBookTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        hotBookTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (BookClassifyBean.ResponseDataBean.MenuInfoBean bean: menuInfoList) {
            hotBookTablayout.addTab(hotBookTablayout.newTab().setText(bean.getValue()));
        }
        MyTablayoutAdapter myAdapter = new MyTablayoutAdapter(this.getSupportFragmentManager(),list_fragment,list_title);
        hotBookViewpager.setAdapter(myAdapter);
        hotBookTablayout.setupWithViewPager(hotBookViewpager);
    }

    @OnClick({R.id.hot_book_back, R.id.hot_book_serach})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hot_book_back:
                finish();
                break;
            case R.id.hot_book_serach:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
