package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MoreZoneListAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BookbarListBean;
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
 * Created by Chen_Zhang on 2017/9/19.
 */

public class MoreZoneActivity extends BaseActivity {
    @BindView(R.id.more_zone_back)
    ImageView moreZoneBack;
    @BindView(R.id.more_zone_listview)
    PullToRefreshListView moreZoneListview;
    @BindView(R.id.more_zone_progress)
    ProgressBar moreZoneProgress;
    @BindView(R.id.more_zone_empty)
    TextView moreZoneEmpty;
    private double longitude;
    private double latitude;
    private List<BookbarListBean.ResponseDataBean.ContentBean> contentBeanList;
    private boolean isLast;
    private int currentPage = 1;
    private MoreZoneListAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_more_zone;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.note_bg);
        String loc = SPUtils.getString(this, "location", "114.07/22.62");
        String[] split = loc.split("/");
        longitude = Double.valueOf(split[0]);
        latitude = Double.valueOf(split[1]);
        Call<BookbarListBean> bookbarListCall = NewDiscoveryRetrofit.getInstance(this).getApi().bookbarListCall(longitude, latitude, 1, 10);
        bookbarListCall.enqueue(new Callback<BookbarListBean>() {
            @Override
            public void onResponse(Call<BookbarListBean> call, Response<BookbarListBean> response) {
                BookbarListBean bean = response.body();
                if (bean != null){
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null){
                        contentBeanList = bean.getResponseData().getContent();
                        isLast = bean.getResponseData().isLast();
                        initData();
                    }else{
                        Toast.makeText(MoreZoneActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookbarListBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        moreZoneProgress.setVisibility(View.GONE);
        moreZoneEmpty.setVisibility(View.GONE);
        if (contentBeanList.size() == 0){
            moreZoneEmpty.setVisibility(View.VISIBLE);
        }
        adapter = new MoreZoneListAdapter(this, contentBeanList);
        moreZoneListview.setAdapter(adapter);
        moreZoneListview.setMode(PullToRefreshBase.Mode.BOTH);
        moreZoneListview.setOnRefreshListener(mListViewOnRefreshListener2);
        moreZoneListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookbarListBean.ResponseDataBean.ContentBean contentBean = contentBeanList.get(position - 1);
                Intent intent = new Intent(MoreZoneActivity.this,ZoneDetailActivity.class);
                intent.putExtra("bookbarId",contentBean.getId());
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
            moreZoneListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(1);
                    moreZoneListview.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
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
            moreZoneListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(MoreZoneActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    moreZoneListview.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<BookbarListBean> bookbarListCall = NewDiscoveryRetrofit.getInstance(this).getApi().bookbarListCall(longitude, latitude, i, 10);
        bookbarListCall.enqueue(new Callback<BookbarListBean>() {
            @Override
            public void onResponse(Call<BookbarListBean> call, Response<BookbarListBean> response) {
                BookbarListBean bean = response.body();
                if (bean!= null){
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null){
                        List<BookbarListBean.ResponseDataBean.ContentBean> newContentBeanList = bean.getResponseData().getContent();
                        isLast = bean.getResponseData().isLast();
                        if (i == 1) {
                            contentBeanList.clear();
                            currentPage = 1;
                        }
                        for (int j = 0; j < newContentBeanList.size(); j++) {
                            contentBeanList.add(newContentBeanList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(MoreZoneActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookbarListBean> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.more_zone_back)
    public void onClick() {
        finish();
    }

}
