package com.hxjf.dubei.ui.activity;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.AccountDetailListAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BalanceListBean;
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
 * Created by Chen_Zhang on 2017/7/14.
 */

public class AccountDetailActivity extends BaseActivity {
    @BindView(R.id.account_detail_back)
    ImageView accountDetailBack;
    @BindView(R.id.account_detail_list)
    PullToRefreshListView accountDetailList;
    private List<BalanceListBean.ResponseDataBean.ListBean> balanceList;
    private AccountDetailListAdapter adapter;
    private boolean isLast;
    private int currentPage = 0;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_account_detail;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //请求数据
        Call<BalanceListBean> balanceListBeanCall = ReaderRetroift.getInstance(AccountDetailActivity.this).getApi().balanceListBeanCall(0, 10);
        balanceListBeanCall.enqueue(new Callback<BalanceListBean>() {
            @Override
            public void onResponse(Call<BalanceListBean> call, Response<BalanceListBean> response) {
                BalanceListBean balanceListBean = response.body();
                if (balanceListBean != null && balanceListBean.getResponseData() != null){
                    balanceList = balanceListBean.getResponseData().getList();
                    isLast = balanceListBean.getResponseData().isIsLastPage();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BalanceListBean> call, Throwable t) {

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
            accountDetailList.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mockList(0);

                    accountDetailList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            accountDetailList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(AccountDetailActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }

                    accountDetailList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<BalanceListBean> balanceListBeanCall = ReaderRetroift.getInstance(AccountDetailActivity.this).getApi().balanceListBeanCall(i, 10);
        balanceListBeanCall.enqueue(new Callback<BalanceListBean>() {
            @Override
            public void onResponse(Call<BalanceListBean> call, Response<BalanceListBean> response) {
                BalanceListBean balanceListBean = response.body();
                if (balanceListBean != null && balanceListBean.getResponseData() != null){
                    if(i == 0){
                        balanceList.clear();
                        currentPage = 0;
                    }
                    isLast = balanceListBean.getResponseData().isIsLastPage();
                    List<BalanceListBean.ResponseDataBean.ListBean> newList = balanceListBean.getResponseData().getList();
                    for (int j = 0; j < newList.size(); j++) {
                        balanceList.add(newList.get(j));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BalanceListBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        adapter = new AccountDetailListAdapter(this, balanceList);
        accountDetailList.setAdapter(adapter);
        accountDetailList.setMode(PullToRefreshBase.Mode.BOTH);
        accountDetailList.setOnRefreshListener(mListViewOnRefreshListener2);
    }

    @OnClick(R.id.account_detail_back)
    public void onClick() {
        finish();
    }


}
