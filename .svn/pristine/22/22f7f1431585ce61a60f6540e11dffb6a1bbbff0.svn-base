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
import com.hxjf.dubei.adapter.MoreFriendAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BookFriendBean;
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
 * Created by Administrator on 2018/3/8 0008.
 */

public class MoreFriendActivity extends BaseActivity {
    @BindView(R.id.more_friend_back)
    ImageView moreFriendBack;
    @BindView(R.id.more_friend_list)
    PullToRefreshListView moreFriendList;
    @BindView(R.id.more_friend_progress)
    ProgressBar moreFriendProgress;
    @BindView(R.id.more_friend_empty_friend)
    TextView moreFriendEmptyFriend;
    private double longitude;
    private double latitude;
    private List<BookFriendBean.ResponseDataBean.ContentBean> contentBeans;
    private Boolean isLast;
    private int currentPage = 1;
    private MoreFriendAdapter adapter;
    private PullToRefreshBase.OnRefreshListener2<ListView> mListViewOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        /**
         * 下拉刷新回调
         * @param refreshView
         */
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            moreFriendList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(1);
                    moreFriendList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
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
            moreFriendList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(MoreFriendActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    moreFriendList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_more_friend;
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
        Call<BookFriendBean> bookFriendBeanCall = NewDiscoveryRetrofit.getInstance(this).getApi().RecommendFriendCall(longitude, latitude, 1, 10);
        bookFriendBeanCall.enqueue(new Callback<BookFriendBean>() {
            @Override
            public void onResponse(Call<BookFriendBean> call, Response<BookFriendBean> response) {
                BookFriendBean bookFriendBean = response.body();
                if(bookFriendBean != null ){
                    if (bookFriendBean.getResponseCode() == 1 && bookFriendBean.getResponseData() != null) {
                        contentBeans = bookFriendBean.getResponseData().getContent();
                        isLast = bookFriendBean.getResponseData().isLast();
                        initData();
                    } else {
                        Toast.makeText(MoreFriendActivity.this, bookFriendBean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookFriendBean> call, Throwable throwable) {

            }
        });
    }

    private void initData() {
        moreFriendProgress.setVisibility(View.GONE);
        moreFriendEmptyFriend.setVisibility(View.GONE);
        if (contentBeans.size() == 0) {
            moreFriendEmptyFriend.setVisibility(View.VISIBLE);
        }
        adapter = new MoreFriendAdapter( contentBeans,this);
        moreFriendList.setAdapter(adapter);
        moreFriendList.setMode(PullToRefreshBase.Mode.BOTH);
        moreFriendList.setOnRefreshListener(mListViewOnRefreshListener2);
        moreFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookFriendBean.ResponseDataBean.ContentBean contentBean = contentBeans.get(position - 1);
                Intent frindintent = new Intent(MoreFriendActivity.this, PersonDetailActivity.class);
                frindintent.putExtra("userId", contentBean.getUserId());
                startActivity(frindintent);
            }
        });
    }

    private void mockList(final int i) {
        Call<BookFriendBean> bookFriendBeanCall = NewDiscoveryRetrofit.getInstance(this).getApi().RecommendFriendCall(longitude, latitude, 1, 10);
        bookFriendBeanCall.enqueue(new Callback<BookFriendBean>() {
            @Override
            public void onResponse(Call<BookFriendBean> call, Response<BookFriendBean> response) {
                BookFriendBean bookFriendBean = response.body();
                if(bookFriendBean != null ){
                    if (bookFriendBean.getResponseCode() == 1 && bookFriendBean.getResponseData() != null) {
                        if (i == 1) {
                            contentBeans.clear();
                            currentPage = 1;
                        }
                        List<BookFriendBean.ResponseDataBean.ContentBean> newcontentBeanList = bookFriendBean.getResponseData().getContent();
                        isLast = bookFriendBean.getResponseData().isLast();
                        for (int j = 0; j < newcontentBeanList.size(); j++) {
                            contentBeans.add(newcontentBeanList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MoreFriendActivity.this, bookFriendBean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookFriendBean> call, Throwable throwable) {

            }
        });
    }


    @OnClick(R.id.more_friend_back)
    public void onViewClicked() {
        finish();
    }
}
