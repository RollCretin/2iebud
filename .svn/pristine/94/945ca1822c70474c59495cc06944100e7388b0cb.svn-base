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
import com.hxjf.dubei.adapter.MyChallengeListAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.MyChallengeBean;
import com.hxjf.dubei.network.ReaderRetroift;
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
 * Created by Chen_Zhang on 2017/8/8.
 */

public class MyChallengeActivity extends BaseActivity {
    @BindView(R.id.my_challenge_back)
    ImageView myChallengeBack;
    @BindView(R.id.my_challenge_list)
    PullToRefreshListView myChallengeList;
    @BindView(R.id.my_challenge_progress)
    ProgressBar myChallengeProgress;
    @BindView(R.id.my_challenge_empty)
    TextView myChallengeEmpty;

    private List<MyChallengeBean.ResponseDataBean.ContentBean> contentBeanList;
    private MyChallengeListAdapter adapter;
    private int currentPage = 0;
    private boolean isLast;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_challenge;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        final Call<MyChallengeBean> myChallengeCall = ReaderRetroift.getInstance(this).getApi().myChallengeCall(currentPage, 10);
        myChallengeCall.enqueue(new Callback<MyChallengeBean>() {

            @Override
            public void onResponse(Call<MyChallengeBean> call, Response<MyChallengeBean> response) {
                MyChallengeBean bean = response.body();
                if (bean != null) {
                    contentBeanList = bean.getResponseData().getContent();
                    isLast = bean.getResponseData().isLast();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<MyChallengeBean> call, Throwable t) {


            }
        });
    }

    private void initData() {
        myChallengeProgress.setVisibility(View.GONE);
        if (contentBeanList.size() == 0) {
            myChallengeEmpty.setVisibility(View.VISIBLE);
        }
        adapter = new MyChallengeListAdapter(MyChallengeActivity.this, contentBeanList);
        myChallengeList.setAdapter(adapter);
        myChallengeList.setMode(PullToRefreshBase.Mode.BOTH);
        myChallengeList.setOnRefreshListener(mListViewOnRefreshListener2);
        myChallengeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyChallengeBean.ResponseDataBean.ContentBean contentBean = contentBeanList.get(position - 1);
                if (contentBean.getBookInfo().getStatus() == 0) {
                    Toast.makeText(MyChallengeActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MyChallengeActivity.this, ChallengeDetailActivity.class);
                    intent.putExtra("challengeId", contentBean.getId());
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
            myChallengeList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(currentPage);
                    myChallengeList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
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
            myChallengeList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(MyChallengeActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    myChallengeList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 3000);
        }
    };

    private void mockList(final int i) {
        final Call<MyChallengeBean> myChallengeCall = ReaderRetroift.getInstance(this).getApi().myChallengeCall(i, 10);
        myChallengeCall.enqueue(new Callback<MyChallengeBean>() {

            @Override
            public void onResponse(Call<MyChallengeBean> call, Response<MyChallengeBean> response) {
                MyChallengeBean bean = response.body();
                if (bean != null) {
                    if (i == 0){
                        contentBeanList.clear();
                        currentPage = 0;
                    }
                    isLast = bean.getResponseData().isLast();
                    List<MyChallengeBean.ResponseDataBean.ContentBean> newcontentList = bean.getResponseData().getContent();
                    for (int j = 0; j < newcontentList.size(); j++) {
                        contentBeanList.add(newcontentList.get(j));

                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MyChallengeBean> call, Throwable t) {


            }
        });
    }

    @OnClick(R.id.my_challenge_back)
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
