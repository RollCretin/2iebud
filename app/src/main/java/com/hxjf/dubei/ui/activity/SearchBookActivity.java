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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.SearchBookAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.SearchBookBean;
import com.hxjf.dubei.bean.hotWordBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.SPUtils;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;
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
 * Created by Chen_Zhang on 2017/8/31.
 */

public class SearchBookActivity extends BaseActivity {
    @BindView(R.id.search_book_search)
    ImageView searchBookSearch;
    @BindView(R.id.search_book_bookname)
    EditText searchBookBookname;
    @BindView(R.id.search_book_cancel)
    TextView searchBookCancel;
    @BindView(R.id.search_book_tagfl_lately)
    TagFlowLayout searchBookTagflLately;
    @BindView(R.id.search_book_ll_lately_search)
    LinearLayout searchBookLlLatelySearch;
    @BindView(R.id.search_book_tagfl_hot)
    TagFlowLayout searchBookTagflHot;
    @BindView(R.id.search_book_ll_hot_search)
    LinearLayout searchBookLlHotSearch;
    @BindView(R.id.search_book_empty_book)
    TextView searchBookEmptyBook;
    @BindView(R.id.search_book_list)
    PullToRefreshListView searchBookList;

    private List<hotWordBean.ResponseDataBean> hotList;
    private List<String> latelyList;
    private List<String> newlist = new ArrayList<>();
    private String name = "";
    private int currentPage = 0;
    private List<SearchBookBean.ResponseDataBean.ListBean> responseDataList ;
    private boolean isLast;
    private SearchBookAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search_book;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);

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
        String str = SPUtils.getString(SearchBookActivity.this, "latelyList", "");
        Gson gson = new Gson();
        latelyList = gson.fromJson(str, new TypeToken<List<String>>() {
        }.getType());
        if (latelyList == null) {
            latelyList = new ArrayList<>();
            searchBookLlLatelySearch.setVisibility(View.GONE);
        } else {
            //最近搜索--10个
            for (int i = 0; i < latelyList.size(); i++) {
                if (i < 10) {
                    String s = latelyList.get(i);
                    newlist.add(latelyList.get(i));
                }
            }
            searchBookTagflLately.setAdapter(new TagAdapter<String>(SearchBookActivity.this.newlist) {
                @Override
                public View getView(FlowLayout parent, int position, String o) {
                    TextView tv = (TextView) View.inflate(SearchBookActivity.this, R.layout.search_board, null);
                    tv.setText(o);
                    tv.setTextColor(getResources().getColor(R.color.gray_text));
                    return tv;
                }
            });

            searchBookTagflLately.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                @Override
                public void onSelected(Set<Integer> selectPosSet) {
                    for (Integer index :
                            selectPosSet) {
                        searchBookBookname.setText(SearchBookActivity.this.latelyList.get(index));
                        search(SearchBookActivity.this.latelyList.get(index));
                    }

                }
            });


        }
        //热门搜索
        searchBookTagflHot.setAdapter(new TagAdapter<hotWordBean.ResponseDataBean>(hotList) {
            @Override
            public View getView(FlowLayout parent, int position, hotWordBean.ResponseDataBean o) {
                TextView tv = (TextView) View.inflate(SearchBookActivity.this, R.layout.search_board, null);
                tv.setText(o.getId());
                tv.setTextColor(getResources().getColor(R.color.gray));
                return tv;
            }
        });
        searchBookTagflHot.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer index : selectPosSet) {
                    searchBookBookname.setText(SearchBookActivity.this.hotList.get(index).getId());
                    search(SearchBookActivity.this.hotList.get(index).getId());
                }
            }
        });

        searchBookBookname.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchBookActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search(searchBookBookname.getText().toString());
                }
                return false;
            }
        });
    }

    private void search(String name) {
        this.name = name;
        if (responseDataList != null){
            responseDataList.clear();
        }
        //软键盘收缩
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBookBookname.getWindowToken(), 0);
        //搜索记录中添加
        if (latelyList.size() == 0) {
            latelyList.add(0, searchBookBookname.getText().toString());
        } else if (!searchBookBookname.getText().toString().equals(latelyList.get(0))) {
            latelyList.add(0, searchBookBookname.getText().toString());
        }
        //最近搜索、热词隐藏
        searchBookLlLatelySearch.setVisibility(View.GONE);
        searchBookLlHotSearch.setVisibility(View.GONE);
        Call<SearchBookBean> searchBookCall = ReaderRetroift.getInstance(this).getApi().SearchBookCall(currentPage, 10, name, "book");
        searchBookCall.enqueue(new Callback<SearchBookBean>() {
            @Override
            public void onResponse(Call<SearchBookBean> call, Response<SearchBookBean> response) {
                SearchBookBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    SearchBookBean.ResponseDataBean responseData = bean.getResponseData();
                    responseDataList = responseData.getList();
                    isLast = responseData.isIsLastPage();
                    initBook();
                }
            }

            @Override
            public void onFailure(Call<SearchBookBean> call, Throwable t) {

            }
        });

    }

    private void initBook() {
        if (responseDataList.size() == 0){
            searchBookEmptyBook.setVisibility(View.VISIBLE);
        }else{
            searchBookEmptyBook.setVisibility(View.GONE);
            searchBookList.setVisibility(View.VISIBLE);
        }
            adapter = new SearchBookAdapter(this, responseDataList);
            searchBookList.setAdapter(adapter);
            searchBookList.setMode(PullToRefreshBase.Mode.BOTH);
            searchBookList.setOnRefreshListener(mListViewOnRefreshListener2);
            searchBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //书籍详情页
                    Intent bookdetailIntent = new Intent(SearchBookActivity.this, SelectBookActivity.class);
                    bookdetailIntent.putExtra("bookid", responseDataList.get(position-1).getId());
                    startActivity(bookdetailIntent);
                }
            });
        adapter.notifyDataSetChanged();
    }
    private PullToRefreshBase.OnRefreshListener2<ListView> mListViewOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        /**
         * 下拉刷新回调
         * @param refreshView
         */
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            searchBookList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(currentPage);
                    searchBookList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            searchBookList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(SearchBookActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    searchBookList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<SearchBookBean> newsearchBookCall = ReaderRetroift.getInstance(this).getApi().SearchBookCall(i, 10, name, "book");
        newsearchBookCall.enqueue(new Callback<SearchBookBean>() {
            @Override
            public void onResponse(Call<SearchBookBean> call, Response<SearchBookBean> response) {
                SearchBookBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    if (i == 0){
                        responseDataList.clear();
                        currentPage = 0;
                    }
                    isLast = bean.getResponseData().isIsLastPage();
                    List<SearchBookBean.ResponseDataBean.ListBean> newList = bean.getResponseData().getList();
                    for (int j = 0; j < newList.size(); j++) {
                        responseDataList.add(newList.get(j));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchBookBean> call, Throwable t) {

            }
        });
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

    @OnClick({R.id.search_book_search, R.id.search_book_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_book_search:
                search(searchBookBookname.getText().toString());
                break;
            case R.id.search_book_cancel:
                finish();
                break;
        }
    }

}
