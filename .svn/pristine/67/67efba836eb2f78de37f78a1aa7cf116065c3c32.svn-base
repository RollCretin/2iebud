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
import com.hxjf.dubei.adapter.DiscoveryNoteListAdapter;
import com.hxjf.dubei.bean.NoteFactoryListBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.NoteDetailActivity;
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

public class NoteFactoryItemFragment extends Fragment {
    View view;
    @BindView(R.id.notefactory_item_list)
    PullToRefreshListView notefactoryItemList;
    @BindView(R.id.notefactory_item_progress)
    ProgressBar notefactoryItemProgress;
    @BindView(R.id.notefactory_item_empty_book)
    TextView notefactoryItemEmptyBook;
    private DiscoveryNoteListAdapter adapter;
    private String text;
    private List<NoteFactoryListBean.ResponseDataBean.ContentBean> contentBeanList;
    private boolean isLast;
    private int currentPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        text = bundle.getString("ITEM_DATA");
        view = inflater.inflate(R.layout.view_note_factory_item, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        if (text.equals("recent")) {
            Call<NoteFactoryListBean> recentNoteFactoryListCall = ReaderRetroift.getInstance(getActivity()).getApi().recentNoteFactoryListCall(currentPage, 10);
            recentNoteFactoryListCall.enqueue(new Callback<NoteFactoryListBean>() {
                @Override
                public void onResponse(Call<NoteFactoryListBean> call, Response<NoteFactoryListBean> response) {
                    NoteFactoryListBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null) {
                        contentBeanList = bean.getResponseData().getContent();
                        isLast = bean.getResponseData().isLast();
                        initItemList();
                    }
                }

                @Override
                public void onFailure(Call<NoteFactoryListBean> call, Throwable t) {

                }
            });

        } else {
            Call<NoteFactoryListBean> noteFactoryListCall = ReaderRetroift.getInstance(getContext()).getApi().noteFactoryListCall(Integer.valueOf(text), currentPage, 10);
            noteFactoryListCall.enqueue(new Callback<NoteFactoryListBean>() {

                @Override
                public void onResponse(Call<NoteFactoryListBean> call, Response<NoteFactoryListBean> response) {
                    NoteFactoryListBean bean = response.body();
                    if (bean != null && bean.getResponseData() != null) {
                        contentBeanList = bean.getResponseData().getContent();
                        isLast = bean.getResponseData().isLast();
                        initItemList();
                    }
                }

                @Override
                public void onFailure(Call<NoteFactoryListBean> call, Throwable t) {

                }
            });
        }

    }

    private void initItemList() {
        notefactoryItemProgress.setVisibility(View.GONE);
        if (contentBeanList.size() == 0){
            notefactoryItemEmptyBook.setVisibility(View.VISIBLE);
        }
        adapter = new DiscoveryNoteListAdapter(getContext(), contentBeanList);
        notefactoryItemList.setAdapter(adapter);
        notefactoryItemList.setMode(PullToRefreshBase.Mode.BOTH);
        notefactoryItemList.setOnRefreshListener(mListViewOnRefreshListener2);
        notefactoryItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteFactoryListBean.ResponseDataBean.ContentBean contentBean = contentBeanList.get(position - 1);
                String noteFactoryId = contentBean.getId();
                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra("noteFactoryId", noteFactoryId);
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
            notefactoryItemList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(1);
                    notefactoryItemList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            notefactoryItemList.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    加载更多 TODO
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    notefactoryItemList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    /**
     * @param i 第几页
     */
    private void mockList(final int i) {
        if (text.equals("recent")){
            Call<NoteFactoryListBean> recentNoteFactoryListCall = ReaderRetroift.getInstance(getActivity()).getApi().recentNoteFactoryListCall(currentPage, 10);
            recentNoteFactoryListCall.enqueue(new Callback<NoteFactoryListBean>() {
                @Override
                public void onResponse(Call<NoteFactoryListBean> call, Response<NoteFactoryListBean> response) {
                    NoteFactoryListBean bean = response.body();
                    if (bean != null) {
                        if(i == 1){
                            contentBeanList.clear();
                            currentPage = 1;
                        }
                        isLast = bean.getResponseData().isLast();
                        List<NoteFactoryListBean.ResponseDataBean.ContentBean> content = bean.getResponseData().getContent();
                        for (int j = 0; j < content.size(); j++) {
                            contentBeanList.add(content.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<NoteFactoryListBean> call, Throwable t) {

                }
            });
        }else{
            Call<NoteFactoryListBean> noteFactoryListCall = ReaderRetroift.getInstance(getContext()).getApi().noteFactoryListCall(Integer.valueOf(text), i, 10);
            noteFactoryListCall.enqueue(new Callback<NoteFactoryListBean>() {
                @Override
                public void onResponse(Call<NoteFactoryListBean> call, Response<NoteFactoryListBean> response) {
                    NoteFactoryListBean bean = response.body();
                    if (bean != null) {
                        if(i == 1){
                            contentBeanList.clear();
                            currentPage = 1;
                        }
                        isLast = bean.getResponseData().isLast();
                        List<NoteFactoryListBean.ResponseDataBean.ContentBean> content = bean.getResponseData().getContent();
                        for (int j = 0; j < content.size(); j++) {
                            contentBeanList.add(content.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<NoteFactoryListBean> call, Throwable t) {

                }
            });
        }
    }
}
