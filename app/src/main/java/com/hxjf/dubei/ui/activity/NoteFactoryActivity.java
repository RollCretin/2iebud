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
import com.hxjf.dubei.bean.NoteFactoryClassifyBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.fragment.NoteFactoryItemFragment;
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

public class NoteFactoryActivity extends BaseActivity {
    @BindView(R.id.note_factory_back)
    ImageView noteFactoryBack;
    @BindView(R.id.note_factory_search)
    ImageView noteFactorySearch;
    @BindView(R.id.note_factory_tablayout)
    TabLayout noteFactoryTablayout;
    @BindView(R.id.note_factory_viewpager)
    ViewPager noteFactoryViewpager;

    private ArrayList<String> list_title;
    private ArrayList<Fragment> list_fragment;
    private ArrayList<String> itemList;
    private List<NoteFactoryClassifyBean.ResponseDataBean.MenuInfoBean> menuInfoList;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_note_factory;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        boolean searchSwitch = SPUtils.getBoolean(NoteFactoryActivity.this, "searchSwitch", false);
        if (searchSwitch){
            noteFactorySearch.setVisibility(View.VISIBLE);
        }

        Call<NoteFactoryClassifyBean> noteFactoryClassifyCall = ReaderRetroift.getInstance(this).getApi().noteFactoryClassifyCall("noteFactoryClassify");
        noteFactoryClassifyCall.enqueue(new Callback<NoteFactoryClassifyBean>() {
            @Override
            public void onResponse(Call<NoteFactoryClassifyBean> call, Response<NoteFactoryClassifyBean> response) {
                NoteFactoryClassifyBean bean = response.body();
                if (bean != null && bean.getResponseData() != null && bean.getResponseData().getMenuInfo() != null){
                    menuInfoList = bean.getResponseData().getMenuInfo();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<NoteFactoryClassifyBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        list_fragment = new ArrayList<Fragment>();
        list_title = new ArrayList<String>();
        //添加最新
        list_title.add("最新");
        NoteFactoryItemFragment recentfragment = new NoteFactoryItemFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("ITEM_DATA", "recent");
        recentfragment.setArguments(bundle1);
        list_fragment.add(recentfragment);
        for (int i = 0; i < menuInfoList.size(); i++) {
            NoteFactoryItemFragment itemfragment = new NoteFactoryItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ITEM_DATA", menuInfoList.get(i).getText());
            itemfragment.setArguments(bundle);
            list_fragment.add(itemfragment);
            list_title.add(menuInfoList.get(i).getValue());
        }
        noteFactoryTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        noteFactoryTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (NoteFactoryClassifyBean.ResponseDataBean.MenuInfoBean bean: menuInfoList) {
            noteFactoryTablayout.addTab(noteFactoryTablayout.newTab().setText(bean.getValue()));
        }
        MyTablayoutAdapter myAdapter = new MyTablayoutAdapter(this.getSupportFragmentManager(),list_fragment,list_title);
        noteFactoryViewpager.setAdapter(myAdapter);
        noteFactoryTablayout.setupWithViewPager(noteFactoryViewpager);
    }

    @OnClick({R.id.note_factory_back, R.id.note_factory_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.note_factory_back:
                finish();
                break;
            case R.id.note_factory_search:
                Intent intent = new Intent(this,SearchActivity.class);
                intent.putExtra("position",3);
                startActivity(intent);
                break;
        }
    }
}
