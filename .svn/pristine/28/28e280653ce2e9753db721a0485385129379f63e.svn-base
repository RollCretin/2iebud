package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MoreBookListAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.HotBookBean;
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
 * Created by Chen_Zhang on 2017/7/31.
 */

public class MoreBookActivity extends BaseActivity {
    private static final int TYPE_CHALLENGE_DETAIL = 200;
    @BindView(R.id.more_book_back)
    ImageView moreBookBack;
    @BindView(R.id.more_book_lv)
    PullToRefreshListView moreBookLv;
    int currentPage = 0;
    private List<HotBookBean.ResponseDataBean.ListBean> booklist;
    private boolean isLast;
    private MoreBookListAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_more_book;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        Call<HotBookBean> hotBookCall = ReaderRetroift.getInstance(this).getApi().hotBookCall(currentPage, 10);
        hotBookCall.enqueue(new Callback<HotBookBean>() {
            @Override
            public void onResponse(Call<HotBookBean> call, Response<HotBookBean> response) {
                HotBookBean bean = response.body();
                if (bean != null) {
                    booklist = bean.getResponseData().getList();
                    isLast = bean.getResponseData().isIsLastPage();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<HotBookBean> call, Throwable t) {

            }
        });


    }

    private void initData() {
        adapter = new MoreBookListAdapter(this, booklist);
        moreBookLv.setAdapter(adapter);
        moreBookLv.setOnRefreshListener(mListViewOnRefreshListener2);
        moreBookLv.setMode(PullToRefreshBase.Mode.BOTH);
        moreBookLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotBookBean.ResponseDataBean.ListBean bean = booklist.get(position-1);
                Intent intent = new Intent(MoreBookActivity.this,BookDetailActivity.class);
                intent.putExtra("bookid",bean.getId());
                intent.putExtra("type",TYPE_CHALLENGE_DETAIL);
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
            //模拟延时三秒刷新
            moreBookLv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(1);
                    moreBookLv.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            moreBookLv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast){
                        currentPage += 1;
                        mockList(currentPage);
                    }else{
                        Toast.makeText(MoreBookActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    moreBookLv.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<HotBookBean> hotBookCall = ReaderRetroift.getInstance(this).getApi().hotBookCall(i, 10);
        hotBookCall.enqueue(new Callback<HotBookBean>() {
            @Override
            public void onResponse(Call<HotBookBean> call, Response<HotBookBean> response) {
                HotBookBean bean = response.body();
                if (bean != null){
                    if (i == 0){
                        booklist.clear();
                        currentPage = 0;
                    }
                    isLast = bean.getResponseData().isIsLastPage();
                    List<HotBookBean.ResponseDataBean.ListBean> list = bean.getResponseData().getList();
                    for (int j = 0; j < list.size(); j++) {
                        booklist.add(list.get(j));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<HotBookBean> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.more_book_back)
    public void onClick() {
        finish();
    }

}
