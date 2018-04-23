package com.hxjf.dubei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BooKAnalysisListAdapter;
import com.hxjf.dubei.bean.ChaiduListBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.BookAnalysisDetailActivity;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/15.
 */

public class BookAnalysisItemFragment extends Fragment {
    View view;
    @BindView(R.id.bookanalysis_item_list)
    PullToRefreshListView bookanalysisItemList;
    @BindView(R.id.bookanalysis_progress)
    ProgressBar bookanalysisProgress;
    @BindView(R.id.bookanalysis_empty_book)
    TextView bookanalysisEmptyBook;
    private BooKAnalysisListAdapter adapter;
    private String text;
    private int currentPage = 1;
    private boolean isLast;
    private List<ChaiduListBean.ResponseDataBean.ContentBean> responseData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        text = bundle.getString("ITEM_DATA");
        view = inflater.inflate(R.layout.view_book_analysis_item, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (text.equals("recent")) {
            Call<ChaiduListBean> recentBookCall = ReaderRetroift.getInstance(getActivity()).getApi().RecentBookCall(2, currentPage, 10);
            recentBookCall.enqueue(new Callback<ChaiduListBean>() {
                @Override
                public void onResponse(Call<ChaiduListBean> call, Response<ChaiduListBean> response) {
                    ChaiduListBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null && bean.getResponseData().getContent() != null) {
                        responseData = bean.getResponseData().getContent();
                        isLast = bean.getResponseData().isLast();
                        initItemList();
                    }
                }

                @Override
                public void onFailure(Call<ChaiduListBean> call, Throwable t) {

                }
            });
        } else {
            //请求拆读列表数据
            Call<ChaiduListBean> chaiduListCall = ReaderRetroift.getInstance(getContext()).getApi().ChaiduListCall(Integer.valueOf(text), 2, currentPage, 10);
            chaiduListCall.enqueue(new Callback<ChaiduListBean>() {
                @Override
                public void onResponse(Call<ChaiduListBean> call, Response<ChaiduListBean> response) {
                    ChaiduListBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null && bean.getResponseData().getContent() != null) {
                        responseData = bean.getResponseData().getContent();
                        isLast = bean.getResponseData().isLast();
                        initItemList();
                    }
                }

                @Override
                public void onFailure(Call<ChaiduListBean> call, Throwable t) {

                }
            });
        }
    }

    private void initItemList() {
        bookanalysisProgress.setVisibility(View.GONE);
        if (responseData.size() == 0){
            bookanalysisEmptyBook.setVisibility(View.VISIBLE);
        }
        adapter = new BooKAnalysisListAdapter(getContext(), responseData);
        bookanalysisItemList.setAdapter(adapter);
        bookanalysisItemList.setMode(PullToRefreshBase.Mode.BOTH);
        bookanalysisItemList.setOnRefreshListener(mListViewOnRefreshListener2);
        bookanalysisItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChaiduListBean.ResponseDataBean.ContentBean responseDataBean = responseData.get(position - 1);
                String ChaiduId = responseDataBean.getId();
                Intent intent = new Intent(getActivity(), BookAnalysisDetailActivity.class);
                intent.putExtra("ChaiduId", ChaiduId);
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
            bookanalysisItemList.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mockList(1);

                    bookanalysisItemList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            bookanalysisItemList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    }

                    bookanalysisItemList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        if (text.equals("recent")){
            Call<ChaiduListBean> recentBookCall = ReaderRetroift.getInstance(getActivity()).getApi().RecentBookCall(2, currentPage, 10);
            recentBookCall.enqueue(new Callback<ChaiduListBean>() {
                @Override
                public void onResponse(Call<ChaiduListBean> call, Response<ChaiduListBean> response) {
                    ChaiduListBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null) {
                        if (i == 1){
                            responseData.clear();
                            currentPage = 1;
                        }
                        isLast = bean.getResponseData().isLast();
                        List<ChaiduListBean.ResponseDataBean.ContentBean> newList = bean.getResponseData().getContent();
                        for (int j = 0; j < newList.size(); j++) {
                            responseData.add(newList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ChaiduListBean> call, Throwable t) {

                }
            });
        }else{
            Call<ChaiduListBean> chaiduListCall = ReaderRetroift.getInstance(getContext()).getApi().ChaiduListCall(Integer.valueOf(text), 2, i, 10);
            chaiduListCall.enqueue(new Callback<ChaiduListBean>() {
                @Override
                public void onResponse(Call<ChaiduListBean> call, Response<ChaiduListBean> response) {
                    ChaiduListBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null) {
                        if (i == 1){
                            responseData.clear();
                            currentPage = 1;
                        }
                        isLast = bean.getResponseData().isLast();
                        List<ChaiduListBean.ResponseDataBean.ContentBean> newList = bean.getResponseData().getContent();
                        for (int j = 0; j < newList.size(); j++) {
                            responseData.add(newList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ChaiduListBean> call, Throwable t) {

                }
            });

        }
    }
}
