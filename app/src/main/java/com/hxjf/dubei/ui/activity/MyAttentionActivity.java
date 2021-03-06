package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MyAttentionListAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.AttentionListBean;
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
 * Created by Administrator on 2018/4/9 0009.
 */

public class MyAttentionActivity extends BaseActivity {
    @BindView(R.id.my_attention_back)
    ImageView myAttentionBack;
    @BindView(R.id.my_attention_listview)
    PullToRefreshListView myAttentionListview;
    @BindView(R.id.my_attention_progress)
    ProgressBar myAttentionProgress;
    @BindView(R.id.my_attention_pempty)
    TextView myAttentionPempty;
    private List<AttentionListBean.ResponseDataBean> responseDataBeanList;
    private MyAttentionListAdapter adapter;
    private PullToRefreshBase.OnRefreshListener2<ListView> mListViewOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        /**
         * 下拉刷新回调
         * @param refreshView
         */
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            myAttentionListview.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    mockList(1);
//                    myAttentionListview.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
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
            myAttentionListview.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    if (!isLast) {
//                        currentPage += 1;
//                        mockList(currentPage);
//                    } else {
//                        Toast.makeText(MoreBookBorrowActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
//                    }
//                    myAttentionListview.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_attention;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);

        Call<AttentionListBean> myAttentionListCall = ReaderRetroift.getInstance(this).getApi().MyAttentionList();
        myAttentionListCall.enqueue(new Callback<AttentionListBean>() {
            @Override
            public void onResponse(Call<AttentionListBean> call, Response<AttentionListBean> response) {
                AttentionListBean bean = response.body();
                if (bean != null && bean.getResponseCode() == 1){
                    responseDataBeanList = bean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<AttentionListBean> call, Throwable throwable) {

            }
        });
    }

    private void initData() {
        myAttentionProgress.setVisibility(View.GONE);
        myAttentionPempty.setVisibility(View.GONE);
        if (responseDataBeanList.size() == 0) {
            myAttentionPempty.setVisibility(View.VISIBLE);
        }
        adapter = new MyAttentionListAdapter(this,responseDataBeanList);
        myAttentionListview.setAdapter(adapter);
        myAttentionListview.setMode(PullToRefreshBase.Mode.DISABLED);
        myAttentionListview.setOnRefreshListener(mListViewOnRefreshListener2);
        myAttentionListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AttentionListBean.ResponseDataBean responseDataBean = responseDataBeanList.get(position-1);
                Intent personIntent = new Intent(MyAttentionActivity.this,PersonDetailActivity.class);
                personIntent.putExtra("userId",responseDataBean.getAttentionUserId());
                startActivity(personIntent);
            }
        });

    }

    @OnClick(R.id.my_attention_back)
    public void onViewClicked() {
        finish();
    }
}
