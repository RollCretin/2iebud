package com.hxjf.dubei.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MoreBookBorrowAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/10/12.
 */

public class SearchCollectBookActivity extends BaseActivity {
    @BindView(R.id.search_collect_book_search)
    ImageView searchCollectBookSearch;
    @BindView(R.id.search_collect_book_bookname)
    EditText searchCollectBookBookname;
    @BindView(R.id.search_collect_book_cancel)
    TextView searchCollectBookCancel;
    @BindView(R.id.search_collect_book_list)
    PullToRefreshListView searchCollectBookList;
    @BindView(R.id.search_collect_book_tagfl_lately)
    TagFlowLayout searchCollectBookTagflLately;
    @BindView(R.id.search_collect_book_ll_lately_search)
    LinearLayout searchCollectBookLlLatelySearch;
    @BindView(R.id.search_collect_book_tagfl_hot)
    TagFlowLayout searchCollectBookTagflHot;
    @BindView(R.id.search_collect_book_ll_hot_search)
    LinearLayout searchCollectBookLlHotSearch;
    @BindView(R.id.search_collect_book_empty_book)
    TextView searchCollectBookEmptyBook;
    private MoreBookBorrowAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search_collect_book;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        //ToDo 最近搜索 最热搜索 隐藏
        searchCollectBookLlLatelySearch.setVisibility(View.GONE);
        searchCollectBookLlHotSearch.setVisibility(View.GONE);

        //设置搜索键盘
        searchCollectBookBookname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchCollectBookActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //TODO
                    Toast.makeText(SearchCollectBookActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        ArrayList<String> strlist = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strlist.add(i+"");
        }
//        adapter = new MoreBookBorrowAdapter(SearchCollectBookActivity.this,strlist);
//        searchCollectBookList.setAdapter(adapter);
//        searchCollectBookList.setMode(PullToRefreshBase.Mode.BOTH);
//        searchCollectBookList.setOnRefreshListener(mListViewOnRefreshListener2);
        searchCollectBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchCollectBookActivity.this,BookBorrowDetailActivity.class);
//                intent.putExtra("bookFavoriteId", contentBean.getId());
                startActivity(intent);
                Toast.makeText(SearchCollectBookActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private PullToRefreshBase.OnRefreshListener2<ListView> mListViewOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        /**
         * 下拉刷新回调
         * @param refreshView
         */
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            searchCollectBookList.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    responseData.clear();
//                    mockList(0);
                    searchCollectBookList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
                }
            }, 1500);
        }

        /**
         * 上拉加载更多回调
         * @param refreshView
         */
        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            //模拟延时三秒加载更多数据
            searchCollectBookList.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    if (!isLast) {
//                        currentPage += 1;
//                        mockList(currentPage);
//                    } else {
//                        Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
//                    }
                    searchCollectBookList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    @OnClick({R.id.search_collect_book_search, R.id.search_collect_book_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_collect_book_search:
                break;
            case R.id.search_collect_book_cancel:
                finish();
                break;
        }
    }
}
