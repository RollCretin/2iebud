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
import com.hxjf.dubei.adapter.SearchNoteAdapter;
import com.hxjf.dubei.bean.SearchNoteBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.NoteDetailActivity;
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

public class Serach_NoteFragment extends Fragment {
    View view;
    int cureentpage = 0;
    String name = "";

    @BindView(R.id.search_book)
    PullToRefreshListView searchBook;
    @BindView(R.id.search_empty_book)
    TextView searchEmptyBook;
    @BindView(R.id.search_book_progress)
    ProgressBar searchBookProgress;
    private List<SearchNoteBean.ResponseDataBean.ContentBean> notelist;
    private boolean isLast;
    private SearchNoteAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_search_book, null);
        ButterKnife.bind(this, view);
        searchEmptyBook.setText("暂无对应笔记");
        init();
        return view;
    }

    private void init() {
        SearchActivity activity = (SearchActivity) getActivity();
        name = activity.getDataName();
        Call<SearchNoteBean> searchNoteCall = ReaderRetroift.getInstance(getActivity()).getApi().SearchNoteCall(cureentpage, 10, name, "noteFactory");
        searchNoteCall.enqueue(new Callback<SearchNoteBean>() {
            @Override
            public void onResponse(Call<SearchNoteBean> call, Response<SearchNoteBean> response) {
                SearchNoteBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    notelist = bean.getResponseData().getContent();
                    isLast = bean.getResponseData().isLast();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<SearchNoteBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        searchBookProgress.setVisibility(View.GONE);
        if (notelist.size() != 0) {
            searchEmptyBook.setVisibility(View.GONE);
            searchBook.setVisibility(View.VISIBLE);
        }else{
            searchEmptyBook.setVisibility(View.VISIBLE);
        }
        adapter = new SearchNoteAdapter(getActivity(), notelist);
        searchBook.setAdapter(adapter);
        searchBook.setMode(PullToRefreshBase.Mode.BOTH);
        searchBook.setOnRefreshListener(mListViewOnRefreshListener2);
        searchBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //笔记详情页
                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra("noteFactoryId", notelist.get(position - 1).getId());
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
        Call<SearchNoteBean> searchNoteCall = ReaderRetroift.getInstance(getActivity()).getApi().SearchNoteCall(i, 10, name, "noteFactory");
        searchNoteCall.enqueue(new Callback<SearchNoteBean>() {
            @Override
            public void onResponse(Call<SearchNoteBean> call, Response<SearchNoteBean> response) {
                SearchNoteBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    if (i == 0){
                        notelist.clear();
                        cureentpage = 0;
                    }
                    isLast = bean.getResponseData().isLast();
                    List<SearchNoteBean.ResponseDataBean.ContentBean> newList = bean.getResponseData().getContent();
                    for (int j = 0; j < newList.size(); j++) {
                        notelist.add(newList.get(j));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SearchNoteBean> call, Throwable t) {

            }
        });
    }
}
