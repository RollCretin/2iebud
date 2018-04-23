package com.hxjf.dubei.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MyTablayoutAdapter;
import com.hxjf.dubei.bean.BookClassifyBean;
import com.hxjf.dubei.network.EnhancedCall;
import com.hxjf.dubei.network.EnhancedCallback;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.NoteFactoryActivity;
import com.hxjf.dubei.ui.activity.SearchActivity;
import com.hxjf.dubei.util.SPUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/5/19.
 */

public class BookshelfFragment extends Fragment {


    @BindView(R.id.bookshelf_iv_search)
    ImageView bookshelfIvSearch;
    @BindView(R.id.bookshelf_tablayout)
    TabLayout bookshelfTablayout;
    @BindView(R.id.bookshelf_viewpager)
    ViewPager bookshelfViewpager;
    @BindView(R.id.bookshelf_nonet)
    TextView bookshelfNonet;

    private MyTablayoutAdapter myAdapter;
    private List<Fragment> list_fragment;
    private List<String> list_title;
    private BS_BookFragment bsbookFragment;
    private BS_BookAnalysisFragment bsunscrambleFragment;
    private BS_NoteFactoryFragment bsnoteFactoryFragment;
    View view;
    private List<BookClassifyBean.ResponseDataBean.MenuInfoBean> menuInfoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_bookshelf_content, null);
        ButterKnife.bind(this, view);
        boolean searchSwitch = SPUtils.getBoolean(getActivity(), "searchSwitch", false);
        if (searchSwitch){
            bookshelfIvSearch.setVisibility(View.VISIBLE);
        }

        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        Call<BookClassifyBean> bookshelfTypeCall = ReaderRetroift.getInstance(getContext()).getApi().bookshelfClassifyCall("bookshelfType");
        EnhancedCall<BookClassifyBean> bookClassifyBeanEnhancedCall = new EnhancedCall<>(bookshelfTypeCall);
        bookClassifyBeanEnhancedCall.dataClassName(BookClassifyBean.class).enqueue(new EnhancedCallback<BookClassifyBean>() {
            @Override
            public void onResponse(Call<BookClassifyBean> call, Response<BookClassifyBean> response) {
                BookClassifyBean bean = response.body();
                if (bean != null) {
                    menuInfoList = bean.getResponseData().getMenuInfo();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BookClassifyBean> call, Throwable t) {
                Toast.makeText(getContext(), "请检查网络...", Toast.LENGTH_LONG).show();
                bookshelfTablayout.setVisibility(View.GONE);
                bookshelfViewpager.setVisibility(View.GONE);
                bookshelfNonet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetCache(BookClassifyBean bookClassifyBean) {
                Toast.makeText(getContext(), "请检查网络...", Toast.LENGTH_LONG).show();
                if (bookClassifyBean != null) {
                    menuInfoList = bookClassifyBean.getResponseData().getMenuInfo();
                    initData();
                }

            }
        });

    }

    private void initData() {
        //title列表
        list_title = new ArrayList<String>();
        //Fragment列表
        list_fragment = new ArrayList<Fragment>();
        for (int i = 0; i < menuInfoList.size(); i++) {
            list_title.add(menuInfoList.get(i).getValue());
        }

        //初始化fragment--书籍、好书拆读、笔记工厂
        bsbookFragment = new BS_BookFragment();
        bsunscrambleFragment = new BS_BookAnalysisFragment();
        bsnoteFactoryFragment = new BS_NoteFactoryFragment();

        list_fragment.add(bsbookFragment);
        list_fragment.add(bsunscrambleFragment);
        list_fragment.add(bsnoteFactoryFragment);

        //请求数据-- 书籍、拆读、笔记的数量


        bookshelfTablayout.setTabMode(TabLayout.MODE_FIXED);
        bookshelfTablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        bookshelfTablayout.addTab(bookshelfTablayout.newTab().setText(list_title.get(0)));
        bookshelfTablayout.addTab(bookshelfTablayout.newTab().setText(list_title.get(1)));
        bookshelfTablayout.addTab(bookshelfTablayout.newTab().setText(list_title.get(2)));

        bookshelfTablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(bookshelfTablayout, 30, 30);
            }
        });

        myAdapter = new MyTablayoutAdapter(this.getChildFragmentManager(), list_fragment, list_title);
        bookshelfViewpager.setAdapter(myAdapter);
        //tablayout绑定Viewpager
        bookshelfTablayout.setupWithViewPager(bookshelfViewpager);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.bookshelf_iv_search, R.id.bookshelf_nonet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bookshelf_iv_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.bookshelf_nonet:
                bookshelfTablayout.setVisibility(View.VISIBLE);
                bookshelfViewpager.setVisibility(View.VISIBLE);
                bookshelfNonet.setVisibility(View.GONE);
                init();
                break;
        }
    }
}
