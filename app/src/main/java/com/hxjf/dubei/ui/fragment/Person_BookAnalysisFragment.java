package com.hxjf.dubei.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BSBookAdapter;
import com.hxjf.dubei.bean.BSBookListBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/14.
 */

public class Person_BookAnalysisFragment extends Fragment {

    @BindView(R.id.bs_gv_book)
    GridView bsGvBook;
    @BindView(R.id.bs_tv_empty_book)
    TextView bsTvEmptyBook;
    private View view;
    private String userId;
    private List<BSBookListBean.ResponseDataBean> responseDataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_bs_book, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }
        Call<BSBookListBean> personChaiduListCall = ReaderRetroift.getInstance(getContext()).getApi().PersonChaiduListCall(userId);
        personChaiduListCall.enqueue(new Callback<BSBookListBean>() {
            @Override
            public void onResponse(Call<BSBookListBean> call, Response<BSBookListBean> response) {
                BSBookListBean bean = response.body();
                if (bean != null && bean.getResponseData() != null){
                    responseDataList = bean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BSBookListBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        //获取数据,判断有无数据
        if (responseDataList.size() != 0) {
            bsTvEmptyBook.setVisibility(View.GONE);
            bsGvBook.setVisibility(View.VISIBLE);

            BSBookAdapter myBookAdapter = new BSBookAdapter(bsGvBook,responseDataList, getContext());
            bsGvBook.setAdapter(myBookAdapter);
        }
    }
}
