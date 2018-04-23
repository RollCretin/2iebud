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
import com.hxjf.dubei.adapter.HotBookAdapter;
import com.hxjf.dubei.bean.HotBookBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.BookDetailActivity;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/1.
 */

public class HotBookItemFragment extends Fragment {
    View view;
    @BindView(R.id.bookanalysis_item_list)
    PullToRefreshListView hotbookItemList;
    @BindView(R.id.bookanalysis_progress)
    ProgressBar bookanalysisProgress;
    @BindView(R.id.bookanalysis_empty_book)
    TextView bookanalysisEmptyBook;
    private String text;
    private int currentPage = 0;
    private boolean isLast;
    private List<HotBookBean.ResponseDataBean.ListBean> responseData;
    private HotBookAdapter adapter;

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
            Call<HotBookBean> hotBookCall = ReaderRetroift.getInstance(getActivity()).getApi().recentHotBookCall(currentPage, 10);
            hotBookCall.enqueue(new Callback<HotBookBean>() {
                @Override
                public void onResponse(Call<HotBookBean> call, Response<HotBookBean> response) {
                    HotBookBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null && bean.getResponseData().getList() != null) {
                        responseData = bean.getResponseData().getList();
                        isLast = bean.getResponseData().isIsLastPage();
                        initItemList();
                    }
                }

                @Override
                public void onFailure(Call<HotBookBean> call, Throwable t) {

                }
            });
        } else {
            //请求热门书籍数据
            Call<HotBookBean> hotBookCall = ReaderRetroift.getInstance(getActivity()).getApi().hotBookCall(currentPage, 10, Integer.valueOf(text));
            hotBookCall.enqueue(new Callback<HotBookBean>() {
                @Override
                public void onResponse(Call<HotBookBean> call, Response<HotBookBean> response) {
                    HotBookBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null && bean.getResponseData().getList() != null) {
                        responseData = bean.getResponseData().getList();
                        isLast = bean.getResponseData().isIsLastPage();
                        initItemList();
                    }
                }

                @Override
                public void onFailure(Call<HotBookBean> call, Throwable t) {

                }
            });
        }
    }

    private void initItemList() {
        bookanalysisProgress.setVisibility(View.GONE);
        if (responseData.size() == 0){
            bookanalysisEmptyBook.setVisibility(View.VISIBLE);
        }
        adapter = new HotBookAdapter(getContext(), responseData);
        hotbookItemList.setAdapter(adapter);
        hotbookItemList.setMode(PullToRefreshBase.Mode.BOTH);
        hotbookItemList.setOnRefreshListener(mListViewOnRefreshListener2);
        hotbookItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //书籍详情页
                Intent bookdetailIntent = new Intent(getActivity(), BookDetailActivity.class);
                bookdetailIntent.putExtra("bookid", responseData.get(position - 1).getId());
                startActivity(bookdetailIntent);
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
            hotbookItemList.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mockList(0);
                    hotbookItemList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
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
            hotbookItemList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    hotbookItemList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        if (text.equals("recent")){
            Call<HotBookBean> hotBookCall = ReaderRetroift.getInstance(getActivity()).getApi().recentHotBookCall(i, 10);
            hotBookCall.enqueue(new Callback<HotBookBean>() {
                @Override
                public void onResponse(Call<HotBookBean> call, Response<HotBookBean> response) {
                    HotBookBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null) {
                        if (i == 0) {
                            responseData.clear();
                            currentPage = 0;
                        }
                        isLast = bean.getResponseData().isIsLastPage();
                        List<HotBookBean.ResponseDataBean.ListBean> newList = bean.getResponseData().getList();
                        for (int j = 0; j < newList.size(); j++) {
                            responseData.add(newList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<HotBookBean> call, Throwable t) {

                }
            });
        }else{
            Call<HotBookBean> hotBookCall = ReaderRetroift.getInstance(getActivity()).getApi().hotBookCall(i, 10, Integer.valueOf(text));
            hotBookCall.enqueue(new Callback<HotBookBean>() {
                @Override
                public void onResponse(Call<HotBookBean> call, Response<HotBookBean> response) {
                    HotBookBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null) {
                        if (i == 0) {
                            responseData.clear();
                            currentPage = 0;
                        }
                        isLast = bean.getResponseData().isIsLastPage();
                        List<HotBookBean.ResponseDataBean.ListBean> newList = bean.getResponseData().getList();
                        for (int j = 0; j < newList.size(); j++) {
                            responseData.add(newList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<HotBookBean> call, Throwable t) {

                }
            });

        }
    }
}
