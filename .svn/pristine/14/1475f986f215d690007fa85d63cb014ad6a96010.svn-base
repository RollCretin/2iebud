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
import com.hxjf.dubei.adapter.CanJoinChallengeListAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.CanJoinChallengeBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/9.
 */

public class CanJoinChallengeActivity extends BaseActivity {
    @BindView(R.id.join_challenge_back)
    ImageView joinChallengeBack;
    @BindView(R.id.join_challenge_list)
    PullToRefreshListView joinChallengeList;
    int currentpage = 0;
    @BindView(R.id.join_challenge_progress)
    ProgressBar joinChallengeProgress;
    @BindView(R.id.join_challenge_empty)
    TextView joinChallengeEmpty;
    private List<CanJoinChallengeBean.ResponseDataBean.ContentBean> contentBeanList = new ArrayList<>();
    private boolean isLast;
    private String bookId;
    private CanJoinChallengeListAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_can_join_challenge;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        Call<CanJoinChallengeBean> canJoinChallengeCall = ReaderRetroift.getInstance(this).getApi().canJoinChallengeCall(currentpage, 10, bookId);
        canJoinChallengeCall.enqueue(new Callback<CanJoinChallengeBean>() {
            @Override
            public void onResponse(Call<CanJoinChallengeBean> call, Response<CanJoinChallengeBean> response) {
                CanJoinChallengeBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    contentBeanList = bean.getResponseData().getContent();
                    isLast = bean.getResponseData().isLast();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<CanJoinChallengeBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        joinChallengeProgress.setVisibility(View.GONE);
        if (contentBeanList.size() == 0) {
            joinChallengeEmpty.setVisibility(View.VISIBLE);
        }
        adapter = new CanJoinChallengeListAdapter(contentBeanList, this);
        joinChallengeList.setAdapter(adapter);
        joinChallengeList.setMode(PullToRefreshBase.Mode.BOTH);
        joinChallengeList.setOnRefreshListener(mListViewOnRefreshListener2);
        joinChallengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (contentBeanList.get(position - 1).getBookInfo().getStatus() == 0) {
                    Toast.makeText(CanJoinChallengeActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CanJoinChallengeActivity.this, ChallengeDetailActivity.class);
                    intent.putExtra("challengeId", contentBeanList.get(position - 1).getId());
                    startActivity(intent);
                }
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
            //模拟延时三秒刷新
            joinChallengeList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(currentpage);
                    joinChallengeList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

                }
            }, 3000);
        }

        /**
         * 上拉加载更多回调
         * @param refreshView
         */
        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            //模拟延时三秒加载更多数据
            joinChallengeList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentpage += 1;
                        mockList(currentpage);
                    } else {
                        Toast.makeText(CanJoinChallengeActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    joinChallengeList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 3000);
        }
    };

    private void mockList(final int i) {
        Call<CanJoinChallengeBean> canJoinChallengeCall = ReaderRetroift.getInstance(this).getApi().canJoinChallengeCall(i, 10, bookId);
        canJoinChallengeCall.enqueue(new Callback<CanJoinChallengeBean>() {
            @Override
            public void onResponse(Call<CanJoinChallengeBean> call, Response<CanJoinChallengeBean> response) {
                CanJoinChallengeBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    if (i == 0){
                        contentBeanList.clear();
                        currentpage = 0;
                    }
                    isLast = bean.getResponseData().isLast();
                    List<CanJoinChallengeBean.ResponseDataBean.ContentBean> newcontentList = bean.getResponseData().getContent();
                    for (int j = 0; j < newcontentList.size(); j++) {
                        contentBeanList.add(newcontentList.get(j));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CanJoinChallengeBean> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.join_challenge_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
