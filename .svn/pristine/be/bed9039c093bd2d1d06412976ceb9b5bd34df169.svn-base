package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MoreBookBorrowAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.FavoriteBookListbean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.StatusBarUtils;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/9/20.
 * 图书借阅
 */

public class MoreBookBorrowActivity extends BaseActivity {
    @BindView(R.id.book_borrow_back)
    ImageView bookBorrowBack;
    @BindView(R.id.book_borrow_listview)
    PullToRefreshListView bookBorrowListview;
    @BindView(R.id.book_borrow_progress)
    ProgressBar bookBorrowProgress;
    @BindView(R.id.book_borrow_empty_book)
    TextView bookBorrowEmptyBook;
    private MoreBookBorrowAdapter adapter;
    private boolean isLast;
    private int currentPage = 1;
    private List<FavoriteBookListbean.ResponseDataBean.ContentBean> contentBeanList;
    private double longitude;
    private double latitude;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_more_book_borrow;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //设置状态栏的颜色
        StatusBarUtils.setWindowStatusBarColor(this, R.color.note_bg);
        String loc = SPUtils.getString(this, "location", "114.07/22.62");
        String[] split = loc.split("/");
        longitude = Double.valueOf(split[0]);
        latitude = Double.valueOf(split[1]);
        Call<FavoriteBookListbean> favoriteBookListCall = NewDiscoveryRetrofit.getInstance(this).getApi().favoriteBookListCall(longitude, latitude, 1, 10);
        favoriteBookListCall.enqueue(new Callback<FavoriteBookListbean>() {
            @Override
            public void onResponse(Call<FavoriteBookListbean> call, Response<FavoriteBookListbean> response) {
                FavoriteBookListbean bookListbean = response.body();
                if (bookListbean != null) {
                    if (bookListbean.getResponseCode() == 1 && bookListbean.getResponseData() != null) {
                        contentBeanList = bookListbean.getResponseData().getContent();
                        isLast = bookListbean.getResponseData().isLast();
                        initData();
                    } else {
                        Toast.makeText(MoreBookBorrowActivity.this, bookListbean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteBookListbean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        bookBorrowProgress.setVisibility(View.GONE);
        bookBorrowEmptyBook.setVisibility(View.GONE);
        if (contentBeanList.size() == 0) {
            bookBorrowEmptyBook.setVisibility(View.VISIBLE);
        }
        adapter = new MoreBookBorrowAdapter(this, contentBeanList);
        bookBorrowListview.setAdapter(adapter);
        bookBorrowListview.setMode(PullToRefreshBase.Mode.BOTH);
        bookBorrowListview.setOnRefreshListener(mListViewOnRefreshListener2);
        bookBorrowListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteBookListbean.ResponseDataBean.ContentBean contentBean = contentBeanList.get(position - 1);
                Intent intent = new Intent(MoreBookBorrowActivity.this, BookBorrowDetailActivity.class);
                intent.putExtra("bookFavoriteId", contentBean.getId());
                startActivity(intent);
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
            bookBorrowListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(1);
                    bookBorrowListview.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
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
            bookBorrowListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(MoreBookBorrowActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    bookBorrowListview.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<FavoriteBookListbean> favoriteBookListCall = NewDiscoveryRetrofit.getInstance(this).getApi().favoriteBookListCall(longitude, latitude, currentPage, 10);
        favoriteBookListCall.enqueue(new Callback<FavoriteBookListbean>() {
            @Override
            public void onResponse(Call<FavoriteBookListbean> call, Response<FavoriteBookListbean> response) {
                FavoriteBookListbean newlistbean = response.body();
                if (newlistbean != null) {
                    if (newlistbean.getResponseCode() == 1 && newlistbean.getResponseData() != null) {
                        if (i == 1) {
                            contentBeanList.clear();
                            currentPage = 1;
                        }
                        List<FavoriteBookListbean.ResponseDataBean.ContentBean> newcontentBeanList = newlistbean.getResponseData().getContent();
                        isLast = newlistbean.getResponseData().isLast();
                        for (int j = 0; j < newcontentBeanList.size(); j++) {
                            contentBeanList.add(newcontentBeanList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MoreBookBorrowActivity.this, newlistbean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<FavoriteBookListbean> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.book_borrow_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_borrow_back:
                finish();
                break;
            /*case R.id.book_borrow_search:
                Intent intent = new Intent(MoreBookBorrowActivity.this, SearchCollectBookActivity.class);
                startActivity(intent);
                break;*/
        }
    }

}
