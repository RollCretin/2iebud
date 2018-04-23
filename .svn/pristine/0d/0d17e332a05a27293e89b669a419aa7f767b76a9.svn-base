package com.hxjf.dubei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BorrowInOutAdapter;
import com.hxjf.dubei.ui.activity.PersonBorrowDetailActivity;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/5 0005.
 */

public class Borrow_OutFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.borrow_out_listview)
    PullToRefreshListView borrowOutListview;
    @BindView(R.id.borrow_out_tv_empyt)
    TextView borrowOutTvEmpyt;
    private PullToRefreshBase.OnRefreshListener2<ListView> mListViewOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        /**
         * 下拉刷新回调
         * @param refreshView
         */
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            borrowOutListview.postDelayed(new Runnable() {
                @Override
                public void run() {

//                    mockList(1);

                    borrowOutListview.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            borrowOutListview.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    if (!isLast) {
//                        currentPage += 1;
//                        mockList(currentPage);
//                    } else {
//                        Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
//                    }

                    borrowOutListview.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_fragment_borrow_out, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        BorrowInOutAdapter adapter = new BorrowInOutAdapter(list, getActivity());
        borrowOutListview.setAdapter(adapter);
        borrowOutListview.setOnClickListener(this);
        borrowOutListview.setMode(PullToRefreshBase.Mode.BOTH);
        borrowOutListview.setOnRefreshListener(mListViewOnRefreshListener2);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PersonBorrowDetailActivity.class);
        startActivity(intent);
    }
}


