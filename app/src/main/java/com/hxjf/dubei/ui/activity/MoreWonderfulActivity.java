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
import com.hxjf.dubei.adapter.MoreWonderfuladapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ActivityListBean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
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
 * Created by Chen_Zhang on 2017/12/12.
 */

public class MoreWonderfulActivity extends BaseActivity {
    @BindView(R.id.wonderful_activity_back)
    ImageView wonderfulActivityBack;
    @BindView(R.id.wonderful_activity_listview)
    PullToRefreshListView wonderfulActivityListview;
    @BindView(R.id.wonderful_activity_progress)
    ProgressBar wonderfulActivityProgress;
    @BindView(R.id.wonderful_activity_empty)
    TextView wonderfulActivityEmpty;

    private int currentPage = 1;

    private MoreWonderfuladapter adapter;
    private List<ActivityListBean.ResponseDataBean.ContentBean> contentBeanList;
    private boolean islast;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_more_wonderful;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Call<ActivityListBean> activityListCall = NewDiscoveryRetrofit.getInstance(this).getApi().activityListCall(1, 10);
        activityListCall.enqueue(new Callback<ActivityListBean>() {
            @Override
            public void onResponse(Call<ActivityListBean> call, Response<ActivityListBean> response) {
                ActivityListBean activityListBean = response.body();
                if (activityListBean != null) {
                    if (activityListBean.getResponseCode() == 1 && activityListBean.getResponseData() != null) {
                        contentBeanList = activityListBean.getResponseData().getContent();
                        islast = activityListBean.getResponseData().isLast();
                        initData();
                    } else {
                        Toast.makeText(MoreWonderfulActivity.this, activityListBean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityListBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        wonderfulActivityProgress.setVisibility(View.GONE);
        wonderfulActivityEmpty.setVisibility(View.GONE);
        if (contentBeanList.size() == 0) {
            wonderfulActivityEmpty.setVisibility(View.VISIBLE);
        }
        adapter = new MoreWonderfuladapter(this, contentBeanList);
        wonderfulActivityListview.setAdapter(adapter);
        wonderfulActivityListview.setMode(PullToRefreshBase.Mode.BOTH);
        wonderfulActivityListview.setOnRefreshListener(mListViewOnRefreshListener2);
        wonderfulActivityListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String activityId = contentBeanList.get(position - 1).getId();
                Intent intent = new Intent(MoreWonderfulActivity.this, WonderfulDetailActivity.class);
                intent.putExtra("activityId", activityId);
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
            wonderfulActivityListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(1);
                    wonderfulActivityListview.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
                }
            }, 1500);
        }

        /**
         * 上拉加载更多回调
         * @param refreshView
         */
        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            //模拟延时加载更多数据
            wonderfulActivityListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!islast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(MoreWonderfulActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    wonderfulActivityListview.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<ActivityListBean> activityListCall = NewDiscoveryRetrofit.getInstance(this).getApi().activityListCall(i, 10);
        activityListCall.enqueue(new Callback<ActivityListBean>() {
            @Override
            public void onResponse(Call<ActivityListBean> call, Response<ActivityListBean> response) {
                ActivityListBean activityListBean = response.body();
                if (activityListBean != null) {
                    if (activityListBean.getResponseCode() == 1 && activityListBean.getResponseData() != null) {
                        if (i == 1) {
                            contentBeanList.clear();
                            currentPage = 1;
                        }
                        List<ActivityListBean.ResponseDataBean.ContentBean> newcontentBeanList = activityListBean.getResponseData().getContent();
                        islast = activityListBean.getResponseData().isLast();
                        for (int j = 0; j < newcontentBeanList.size(); j++) {
                            contentBeanList.add(newcontentBeanList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MoreWonderfulActivity.this, activityListBean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ActivityListBean> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.wonderful_activity_back)
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
