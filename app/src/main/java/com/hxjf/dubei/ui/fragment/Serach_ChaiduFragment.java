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
import com.hxjf.dubei.adapter.SearchChaiduAdapter;
import com.hxjf.dubei.bean.SearchBookBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.BookAnalysisDetailActivity;
import com.hxjf.dubei.ui.activity.SearchActivity;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/21.
 */

public class Serach_ChaiduFragment extends Fragment {
    View view;
    int cureentpage = 0;
    String name = "";

    @BindView(R.id.search_book)
    PullToRefreshListView searchBook;
    @BindView(R.id.search_empty_book)
    TextView searchEmptyBook;
    @BindView(R.id.search_book_progress)
    ProgressBar searchBookProgress;
    private List<SearchBookBean.ResponseDataBean.ListBean> chaidulist;
    private boolean isLastPage;
    private SearchChaiduAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_search_book, null);
        ButterKnife.bind(this, view);
        searchEmptyBook.setText("暂无对应拆读");
        init();
        return view;
    }

    private void init() {
        SearchActivity activity = (SearchActivity) getActivity();
        name = activity.getDataName();
        Call<SearchBookBean> searchBookCall = ReaderRetroift.getInstance(getActivity()).getApi().SearchBookCall(cureentpage, 10, name, "divideBook");
        searchBookCall.enqueue(new Callback<SearchBookBean>() {
            @Override
            public void onResponse(Call<SearchBookBean> call, Response<SearchBookBean> response) {
                SearchBookBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    chaidulist = bean.getResponseData().getList();
                    isLastPage = bean.getResponseData().isIsLastPage();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<SearchBookBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        searchBookProgress.setVisibility(View.GONE);
        if (chaidulist.size() != 0) {
            searchEmptyBook.setVisibility(View.GONE);
            searchBook.setVisibility(View.VISIBLE);
        }else{
            searchEmptyBook.setVisibility(View.VISIBLE);
        }
        adapter = new SearchChaiduAdapter(getActivity(), chaidulist);
        searchBook.setAdapter(adapter);
        searchBook.setMode(PullToRefreshBase.Mode.BOTH);
        searchBook.setOnRefreshListener(mListViewOnRefreshListener2);
        searchBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //书籍详情页
                Intent bookdetailIntent = new Intent(getActivity(), BookAnalysisDetailActivity.class);
                bookdetailIntent.putExtra("ChaiduId", chaidulist.get(position - 1).getId());
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
            searchBook.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mockList(cureentpage);

                    searchBook.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            searchBook.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLastPage) {
                        cureentpage += 1;
                        mockList(cureentpage);
                    } else {
                        Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    }

                    searchBook.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<SearchBookBean> searchBookCall = ReaderRetroift.getInstance(getActivity()).getApi().SearchBookCall(i, 10, name, "divideBook");
        searchBookCall.enqueue(new Callback<SearchBookBean>() {
            @Override
            public void onResponse(Call<SearchBookBean> call, Response<SearchBookBean> response) {
                SearchBookBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    if (i == 0){
                        chaidulist.clear();
                        cureentpage = 0;
                    }
                    List<SearchBookBean.ResponseDataBean.ListBean> list = bean.getResponseData().getList();
                    isLastPage = bean.getResponseData().isIsLastPage();
                    for (int j = 0; j < list.size(); j++) {
                        chaidulist.add(list.get(j));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchBookBean> call, Throwable t) {

            }
        });
    }
}
