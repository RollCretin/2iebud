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
import com.hxjf.dubei.bean.BookListClassifyBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.fragment.BooklistItemFragment;
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
 * Created by Chen_Zhang on 2017/7/14.
 */

public class BookListActivity extends BaseActivity {
    @BindView(R.id.preferred_booklist_back)
    ImageView preferredBooklistBack;
    @BindView(R.id.preferred_booklist_serach)
    ImageView preferredBooklistSerach;
    @BindView(R.id.preferred_booklist_tablayout)
    TabLayout preferredBooklistTablayout;
    @BindView(R.id.preferred_booklist_viewpager)
    ViewPager preferredBooklistViewpager;

    private ArrayList<String> list_title;
    private ArrayList<Fragment> list_fragment;
    private ArrayList<String> itemList;
    private List<BookListClassifyBean.ResponseDataBean.MenuInfoBean> menuInfoList;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_booklist;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        boolean searchSwitch = SPUtils.getBoolean(BookListActivity.this, "searchSwitch", false);
        if (searchSwitch){
            preferredBooklistSerach.setVisibility(View.VISIBLE);
        }
        //获取数据
        Call<BookListClassifyBean> bookListClassifyCall = ReaderRetroift.getInstance(this).getApi().BookListClassifyCall("bookListClassify");
        bookListClassifyCall.enqueue(new Callback<BookListClassifyBean>() {
            @Override
            public void onResponse(Call<BookListClassifyBean> call, Response<BookListClassifyBean> response) {
                BookListClassifyBean bean = response.body();
                if (bean != null && bean.getResponseData() != null && bean.getResponseData().getMenuInfo() != null){
                    menuInfoList = bean.getResponseData().getMenuInfo();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BookListClassifyBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        list_title = new ArrayList<>();
        list_fragment = new ArrayList<Fragment>();
        //添加最新
        list_title.add("最新");
        BooklistItemFragment cerrentfragment = new BooklistItemFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("ITEM_DATA", "recent");
        cerrentfragment.setArguments(bundle1);
        list_fragment.add(cerrentfragment);

        for (int i = 0; i < menuInfoList.size(); i++) {
            BooklistItemFragment itemfragment = new BooklistItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ITEM_DATA", menuInfoList.get(i).getText());
            itemfragment.setArguments(bundle);
            list_fragment.add(itemfragment);
            list_title.add(menuInfoList.get(i).getValue());
        }
        preferredBooklistTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        preferredBooklistTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (BookListClassifyBean.ResponseDataBean.MenuInfoBean bean: menuInfoList) {
            preferredBooklistTablayout.addTab(preferredBooklistTablayout.newTab().setText(bean.getValue()));
        }
        MyTablayoutAdapter myAdapter = new MyTablayoutAdapter(this.getSupportFragmentManager(),list_fragment,list_title);
        preferredBooklistViewpager.setAdapter(myAdapter);
        preferredBooklistTablayout.setupWithViewPager(preferredBooklistViewpager);

    }

    @OnClick({R.id.preferred_booklist_back, R.id.preferred_booklist_serach})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.preferred_booklist_back:
                finish();
                break;
            case R.id.preferred_booklist_serach:
                Intent intent = new Intent(this,SearchActivity.class);
                intent.putExtra("position",1);
                startActivity(intent);
                break;
        }
    }
}
