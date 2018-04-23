package com.hxjf.dubei.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BSNoteAdapter;
import com.hxjf.dubei.bean.BSNoteListBean;
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

public class Person_NoteFactoryFragment extends Fragment {

    @BindView(R.id.bs_gv_note_factory)
    GridView bsGvNoteFactory;
    @BindView(R.id.bs_tv_empty_book)
    TextView bsTvEmptyBook;
    private String userId;
    private List<BSNoteListBean.ResponseDataBean> responseDataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_bs_note_factory, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getString("userId");
        }
        Call<BSNoteListBean> bsNoteListCall = ReaderRetroift.getInstance(getActivity()).getApi().personNoteListCall(userId);
        bsNoteListCall.enqueue(new Callback<BSNoteListBean>() {

            @Override
            public void onResponse(Call<BSNoteListBean> call, Response<BSNoteListBean> response) {
                BSNoteListBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    responseDataList = bean.getResponseData();
                    initData();
                }else{
                    Toast.makeText(getContext(), bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BSNoteListBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        if (responseDataList.size() != 0) {
            bsTvEmptyBook.setVisibility(View.GONE);
            bsGvNoteFactory.setVisibility(View.VISIBLE);
            BSNoteAdapter bsNoteAdapter = new BSNoteAdapter(responseDataList, getContext());
            bsGvNoteFactory.setAdapter(bsNoteAdapter);
        }
    }
}
