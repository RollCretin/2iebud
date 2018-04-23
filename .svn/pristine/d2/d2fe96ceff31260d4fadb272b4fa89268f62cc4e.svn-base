package com.hxjf.dubei.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MyTablayoutAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.hotWordBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.fragment.Serach_BookFragment;
import com.hxjf.dubei.ui.fragment.Serach_BookListFragment;
import com.hxjf.dubei.ui.fragment.Serach_ChaiduFragment;
import com.hxjf.dubei.ui.fragment.Serach_NoteFragment;
import com.hxjf.dubei.util.SPUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/21.
 */

public class SearchActivity extends BaseActivity {
    private static final String TAG = "haha";
    @BindView(R.id.search_iv_search)
    ImageView searchIvSearch;
    @BindView(R.id.search_et_bookname)
    EditText searchEtBookname;
    @BindView(R.id.search_tv_cancel)
    TextView searchTvCancel;
    @BindView(R.id.search_tablayout)
    TabLayout searchTablayout;
    @BindView(R.id.search_viewpager)
    ViewPager searchViewpager;
    @BindView(R.id.search_tagfl_lately)
    TagFlowLayout searchTagflLately;
    @BindView(R.id.search_ll_lately_search)
    LinearLayout searchLlLatelySearch;
    @BindView(R.id.search_tagfl_hot)
    TagFlowLayout searchTagflHot;
    @BindView(R.id.search_ll_hot_search)
    LinearLayout searchLlHotSearch;
    private List<hotWordBean.ResponseDataBean> hotList;
    private List<String> latelyList;
    private List<String> newlist = new ArrayList<>();
    private String name = "";
    private int position = 0;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);
        //热词
        Call<hotWordBean> hotWordCall = ReaderRetroift.getInstance(this).getApi().hotWordCall();
        hotWordCall.enqueue(new Callback<hotWordBean>() {
            @Override
            public void onResponse(Call<hotWordBean> call, Response<hotWordBean> response) {
                hotWordBean bean = response.body();
                if (bean != null) {
                    hotList = bean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<hotWordBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        //获取最近搜索词语
        String str = SPUtils.getString(SearchActivity.this, "latelyList", "");
        Gson gson = new Gson();
        latelyList = gson.fromJson(str, new TypeToken<List<String>>() {
        }.getType());
        if (latelyList == null) {
            latelyList = new ArrayList<>();
            searchLlLatelySearch.setVisibility(View.GONE);
        } else {
            //最近搜索--10个
            for (int i = 0; i < latelyList.size(); i++) {
                if (i < 10) {
                    String s = latelyList.get(i);
                    newlist.add(latelyList.get(i));
                }
            }
            searchTagflLately.setAdapter(new TagAdapter<String>(SearchActivity.this.newlist) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    TextView tv = (TextView) View.inflate(SearchActivity.this, R.layout.search_board, null);
                    tv.setText(o);
                    tv.setTextColor(getResources().getColor(R.color.gray_text));
                    return tv;
                }
            });

            searchTagflLately.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                @Override
                public void onSelected(Set<Integer> selectPosSet) {
                    for (Integer index :
                            selectPosSet) {
                        setDataName(SearchActivity.this.latelyList.get(index));
                        searchEtBookname.setText(SearchActivity.this.latelyList.get(index));
                        search();
                    }

                }
            });


        }
        //热门搜索
        searchTagflHot.setAdapter(new TagAdapter<hotWordBean.ResponseDataBean>(hotList) {
            @Override
            public View getView(FlowLayout parent, int position, hotWordBean.ResponseDataBean o) {
                TextView tv = (TextView) View.inflate(SearchActivity.this, R.layout.search_board, null);
                tv.setText(o.getId());
                tv.setTextColor(getResources().getColor(R.color.gray));
                return tv;
            }
        });
        searchTagflHot.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer index : selectPosSet) {
                    setDataName(SearchActivity.this.hotList.get(index).getId());
                    searchEtBookname.setText(SearchActivity.this.hotList.get(index).getId());
                    search();
                }
            }
        });

        searchEtBookname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    name = searchEtBookname.getText().toString();
                    search();
                }
                return false;
            }
        });
    }


    @OnClick({R.id.search_iv_search, R.id.search_tv_cancel, R.id.search_et_bookname})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_search:
                name = searchEtBookname.getText().toString();
                search();
                break;
            case R.id.search_tv_cancel:
                finish();
                break;
            case R.id.search_et_bookname:
                break;
        }
    }

    private void search() {
        //软键盘收缩
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEtBookname.getWindowToken(), 0);
        //搜索记录中添加
        if (latelyList.size() == 0){
            latelyList.add(0,searchEtBookname.getText().toString());
        }else if (!searchEtBookname.getText().toString().equals(latelyList.get(0))){
            latelyList.add(0,searchEtBookname.getText().toString());
        }
        searchTablayout.setVisibility(View.VISIBLE);
        searchViewpager.setVisibility(View.VISIBLE);
        searchLlLatelySearch.setVisibility(View.GONE);
        searchLlHotSearch.setVisibility(View.GONE);



        //TabLayout分类
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add("书籍");
        titleList.add("书单");
        titleList.add("好书拆读");
        titleList.add("笔记工厂");

        //ViewPage分类
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        Serach_BookFragment bookfragment = new Serach_BookFragment(); //--书籍
        Serach_BookListFragment booklistfragment = new Serach_BookListFragment();//--书单
        Serach_ChaiduFragment chaidufragment = new Serach_ChaiduFragment(); //--拆读
        Serach_NoteFragment notefragment = new Serach_NoteFragment(); //--笔记工厂
        fragmentList.add(bookfragment);
        fragmentList.add(booklistfragment);
        fragmentList.add(chaidufragment);
        fragmentList.add(notefragment);

        searchTablayout.setTabMode(TabLayout.MODE_FIXED);
        searchTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        for (String str : titleList) {
            searchTablayout.addTab(searchTablayout.newTab().setText(str));
        }

        MyTablayoutAdapter myAdapter = new MyTablayoutAdapter(this.getSupportFragmentManager(), fragmentList, titleList);
        searchViewpager.setAdapter(myAdapter);
        searchTablayout.setupWithViewPager(searchViewpager);
        //设置位置
        searchTablayout.getTabAt(position).select();
        searchViewpager.setCurrentItem(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将搜索历史保存到本地
        if (latelyList != null) {
            Gson gson = new Gson();
            String str = gson.toJson(latelyList);
            SPUtils.putString(this, "latelyList", str);
        }
    }

    public String getDataName() {
        return name;
    }

    public void setDataName(String name) {
        this.name = name;
    }

}
