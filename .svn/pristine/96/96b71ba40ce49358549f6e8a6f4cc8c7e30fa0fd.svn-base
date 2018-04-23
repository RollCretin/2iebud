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
import com.hxjf.dubei.adapter.BookListItemListAdapter;
import com.hxjf.dubei.bean.BookListbean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.BookListDetailActivity;
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

public class BooklistItemFragment extends Fragment {

    @BindView(R.id.booklist_item_list)
    PullToRefreshListView booklistItemList;
    @BindView(R.id.booklist_item_progress)
    ProgressBar booklistItemProgress;
    @BindView(R.id.booklist_item_empty_book)
    TextView booklistItemEmptyBook;

    private View view;
    private BookListItemListAdapter adapter;
    private String text;
    private List<BookListbean.ResponseDataBean.ContentBean> contentList;
    private boolean isLast;
    private int currentPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        text = bundle.getString("ITEM_DATA");
        view = inflater.inflate(R.layout.view_booklist_item, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (text.equals("recent")) {
            Call<BookListbean> recentListCall = ReaderRetroift.getInstance(getActivity()).getApi().RecentListCall(1, 10);
            recentListCall.enqueue(new Callback<BookListbean>() {
                @Override
                public void onResponse(Call<BookListbean> call, Response<BookListbean> response) {
                    BookListbean recentlistbean = response.body();
                    if (recentlistbean != null && recentlistbean.getResponseData() != null) {
                        contentList = recentlistbean.getResponseData().getContent();
                        isLast = recentlistbean.getResponseData().isLast();
                        initItemList();
                    }
                }

                @Override
                public void onFailure(Call<BookListbean> call, Throwable t) {

                }
            });
        } else {

            //请求数据
            Call<BookListbean> bookListCall = ReaderRetroift.getInstance(getActivity()).getApi().BookListCall(Integer.valueOf(text), currentPage, 10);
            bookListCall.enqueue(new Callback<BookListbean>() {
                @Override
                public void onResponse(Call<BookListbean> call, Response<BookListbean> response) {
                    BookListbean listbean = response.body();
                    if (listbean != null && listbean.getResponseData() != null) {
                        contentList = listbean.getResponseData().getContent();
                        isLast = listbean.getResponseData().isLast();
                        initItemList();
                    }
                }

                @Override
                public void onFailure(Call<BookListbean> call, Throwable t) {

                }
            });
        }

    }

    private void initItemList() {
        booklistItemProgress.setVisibility(View.GONE);
        if (contentList.size() == 0){
            booklistItemEmptyBook.setVisibility(View.VISIBLE);
        }
        adapter = new BookListItemListAdapter(getContext(), contentList);
        booklistItemList.setAdapter(adapter);
        booklistItemList.setMode(PullToRefreshBase.Mode.BOTH);
        booklistItemList.setOnRefreshListener(mListViewOnRefreshListener2);
        booklistItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookListbean.ResponseDataBean.ContentBean contentBean = contentList.get(position - 1);
                Intent intent = new Intent(getActivity(), BookListDetailActivity.class);
                intent.putExtra("booklistid", contentBean.getId());
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
            booklistItemList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(1);
                    booklistItemList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            booklistItemList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    booklistItemList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        if (text.equals("recent")) {
            Call<BookListbean> recentListCall = ReaderRetroift.getInstance(getActivity()).getApi().RecentListCall(i, 10);
            recentListCall.enqueue(new Callback<BookListbean>() {
                @Override
                public void onResponse(Call<BookListbean> call, Response<BookListbean> response) {
                    BookListbean recentlistbean = response.body();
                    if (recentlistbean != null && recentlistbean.getResponseData() != null) {
                        if (i == 1){
                            contentList.clear();
                            currentPage = 1;
                        }
                        List<BookListbean.ResponseDataBean.ContentBean> newcontentList = recentlistbean.getResponseData().getContent();
                        isLast = recentlistbean.getResponseData().isLast();
                        for (int j = 0; j < newcontentList.size(); j++) {
                            contentList.add(newcontentList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<BookListbean> call, Throwable t) {

                }
            });
        } else {
            Call<BookListbean> bookListCall = ReaderRetroift.getInstance(getActivity()).getApi().BookListCall(Integer.valueOf(text), i, 10);
            bookListCall.enqueue(new Callback<BookListbean>() {
                @Override
                public void onResponse(Call<BookListbean> call, Response<BookListbean> response) {
                    BookListbean listbean = response.body();
                    if (listbean != null) {
                        if (i == 1){
                            contentList.clear();
                            currentPage = 1;
                        }
                        isLast = listbean.getResponseData().isLast();
                        List<BookListbean.ResponseDataBean.ContentBean> newcontentList = listbean.getResponseData().getContent();
                        for (int j = 0; j < newcontentList.size(); j++) {
                            contentList.add(newcontentList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<BookListbean> call, Throwable t) {

                }
            });

        }
    }
}
