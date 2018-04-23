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
import com.hxjf.dubei.ui.fragment.BookAnalysisItemFragment;
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
 * Created by Chen_Zhang on 2017/7/15.
 */

public class BookAnalysisActivity extends BaseActivity {
    @BindView(R.id.book_analysis_back)
    ImageView bookAnalysisBack;
    @BindView(R.id.book_analysis_serach)
    ImageView bookAnalysisSerach;
    @BindView(R.id.book_analysis_tablayout)
    TabLayout bookAnalysisTablayout;
    @BindView(R.id.book_analysis_viewpager)
    ViewPager bookAnalysisViewpager;

    private ArrayList<Fragment> list_fragment;
    private ArrayList<String> list_title;
    private List<BookClassifyBean.ResponseDataBean.MenuInfoBean> menuInfoList;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_book_analysis;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        boolean searchSwitch = SPUtils.getBoolean(BookAnalysisActivity.this, "searchSwitch", false);
        if (searchSwitch){
            bookAnalysisSerach.setVisibility(View.VISIBLE);
        }
        Call<BookClassifyBean> bookClassifyCall = ReaderRetroift.getInstance(this).getApi().bookClassifyCall("bookClassify");
        bookClassifyCall.enqueue(new Callback<BookClassifyBean>() {
            @Override
            public void onResponse(Call<BookClassifyBean> call, Response<BookClassifyBean> response) {
                BookClassifyBean bean = response.body();
                if (bean != null && bean.getResponseData()!= null && bean.getResponseData().getMenuInfo() != null){
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
        BookAnalysisItemFragment recentfragment = new BookAnalysisItemFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("ITEM_DATA", "recent");
        recentfragment.setArguments(bundle1);
        list_fragment.add(recentfragment);

        for (int i = 0; i < menuInfoList.size(); i++) {
            BookAnalysisItemFragment itemfragment = new BookAnalysisItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ITEM_DATA", menuInfoList.get(i).getText());
            itemfragment.setArguments(bundle);
            list_fragment.add(itemfragment);
            list_title.add(menuInfoList.get(i).getValue());
        }

        bookAnalysisTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        bookAnalysisTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (BookClassifyBean.ResponseDataBean.MenuInfoBean bean: menuInfoList) {
            bookAnalysisTablayout.addTab(bookAnalysisTablayout.newTab().setText(bean.getValue()));
        }
        MyTablayoutAdapter myAdapter = new MyTablayoutAdapter(this.getSupportFragmentManager(),list_fragment,list_title);
        bookAnalysisViewpager.setAdapter(myAdapter);
        bookAnalysisTablayout.setupWithViewPager(bookAnalysisViewpager);


    }

    @OnClick({R.id.book_analysis_back, R.id.book_analysis_serach})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_analysis_back:
                finish();
                break;
            case R.id.book_analysis_serach:
                Intent intent = new Intent(this,SearchActivity.class);
                intent.putExtra("position",2);
                startActivity(intent);
                break;
        }
    }
}
