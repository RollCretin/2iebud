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
import com.hxjf.dubei.adapter.SearchBookAdapter;
import com.hxjf.dubei.bean.SearchBookBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.BookDetailActivity;
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

public class Serach_BookFragment extends Fragment {
    View view;
    int cureentpage = 0;
    String name = "";

    @BindView(R.id.search_empty_book)
    TextView searchEmptyBook;
    @BindView(R.id.search_book)
    PullToRefreshListView searchBook;
    @BindView(R.id.search_book_progress)
    ProgressBar searchBookProgress;

    private List<SearchBookBean.ResponseDataBean.ListBean> booklist;
    private boolean isLast;
    private SearchBookAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_search_book, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        SearchActivity activity = (SearchActivity) getActivity();
        name = activity.getDataName();
        Call<SearchBookBean> searchBookCall = ReaderRetroift.getInstance(getActivity()).getApi().SearchBookCall(cureentpage, 10, name, "book");
        searchBookCall.enqueue(new Callback<SearchBookBean>() {
            @Override
            public void onResponse(Call<SearchBookBean> call, Response<SearchBookBean> response) {
                SearchBookBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    booklist = bean.getResponseData().getList();
                    isLast = bean.getResponseData().isIsLastPage();
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
        if (booklist.size() != 0) {
            searchEmptyBook.setVisibility(View.GONE);
            searchBook.setVisibility(View.VISIBLE);
        }else{
            searchEmptyBook.setVisibility(View.VISIBLE);
        }
        adapter = new SearchBookAdapter(getActivity(), booklist);
        searchBook.setAdapter(adapter);
        searchBook.setMode(PullToRefreshBase.Mode.BOTH);
        searchBook.setOnRefreshListener(mListViewOnRefreshListener2);
        searchBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //书籍详情页
                Intent bookdetailIntent = new Intent(getActivity(), BookDetailActivity.class);
                bookdetailIntent.putExtra("bookid", booklist.get(position - 1).getId());
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
                    if (!isLast) {
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
        Call<SearchBookBean> newsearchBookCall = ReaderRetroift.getInstance(getActivity()).getApi().SearchBookCall(i, 10, name, "book");
        newsearchBookCall.enqueue(new Callback<SearchBookBean>() {
            @Override
            public void onResponse(Call<SearchBookBean> call, Response<SearchBookBean> response) {
                SearchBookBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    if (i == 0){
                        booklist.clear();
                        cureentpage = 0;
                    }
                    isLast = bean.getResponseData().isIsLastPage();
                    List<SearchBookBean.ResponseDataBean.ListBean> newList = bean.getResponseData().getList();
                    for (int j = 0; j < newList.size(); j++) {
                        booklist.add(newList.get(j));
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
