package com.hxjf.dubei.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BSNoteAdapter;
import com.hxjf.dubei.bean.BSNoteListBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.network.EnhancedCall;
import com.hxjf.dubei.network.EnhancedCallback;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.NoteDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/6/12.
 */

public class BS_NoteFactoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int REFRESH_COMPLETE = 100;
    @BindView(R.id.bs_gv_note_factory)
    GridView bsGvNoteFactory;
    @BindView(R.id.bs_tv_empty_book)
    TextView bsTvEmptyBook;
    @BindView(R.id.bs_note_refresh)
    SwipeRefreshLayout bsNoteRefresh;
    @BindView(R.id.bs_note_nonet)
    TextView bsNoteNonet;

    private BSNoteAdapter bsNoteAdapter;
    private List<BSNoteListBean.ResponseDataBean> responseDataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_bs_note_factory, null);
        ButterKnife.bind(this, view);
        bsNoteRefresh.setOnRefreshListener(this);
        bsNoteRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        bsNoteRefresh.setRefreshing(true);
        Call<BSNoteListBean> bsNoteListCall = ReaderRetroift.getInstance(getContext()).getApi().BSNoteListCall();
        EnhancedCall<BSNoteListBean> bsNoteListBeanEnhancedCall = new EnhancedCall<>(bsNoteListCall);
        bsNoteListBeanEnhancedCall.dataClassName(BSNoteListBean.class).enqueue(new EnhancedCallback<BSNoteListBean>() {
            @Override
            public void onResponse(Call<BSNoteListBean> call, Response<BSNoteListBean> response) {
                BSNoteListBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    responseDataList = bean.getResponseData();
                    bsNoteRefresh.setRefreshing(false);
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BSNoteListBean> call, Throwable t) {
                bsNoteRefresh.setRefreshing(false);
                Toast.makeText(getActivity(), "请检查网络..", Toast.LENGTH_LONG).show();
                bsNoteNonet.setVisibility(View.VISIBLE);
                bsGvNoteFactory.setVisibility(View.GONE);
            }

            @Override
            public void onGetCache(BSNoteListBean bsNoteListBean) {
                Toast.makeText(getActivity(), "请检查网络..", Toast.LENGTH_LONG).show();
                if (bsNoteListBean != null && bsNoteListBean.getResponseData() != null) {
                    responseDataList = bsNoteListBean.getResponseData();
                    bsNoteRefresh.setRefreshing(false);
                    initData();
                }

            }
        });
    }

    private void initData() {
        bsNoteAdapter = new BSNoteAdapter(responseDataList, getContext());
        bsGvNoteFactory.setAdapter(bsNoteAdapter);
        bsNoteAdapter.notifyDataSetChanged();
        if (responseDataList.size() == 0) {
            bsNoteAdapter.notifyDataSetChanged();
            bsTvEmptyBook.setVisibility(View.VISIBLE);
        } else {
            bsTvEmptyBook.setVisibility(View.GONE);
            bsGvNoteFactory.setVisibility(View.VISIBLE);
            bsGvNoteFactory.setAdapter(bsNoteAdapter);
            bsGvNoteFactory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BSNoteListBean.ResponseDataBean itemBean = responseDataList.get(position);
                    if (itemBean.getNoteFactoryInfo() == null || itemBean.getNoteFactoryInfo().getStatus() == 0) {
                        Toast.makeText(getActivity(), "该笔记已下架", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                        intent.putExtra("noteFactoryId", responseDataList.get(position).getNoteFactoryInfo().getId());
                        startActivity(intent);
                    }
                }
            });
            bsGvNoteFactory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    deleteNote(position);
                    return true;
                }
            });
        }
    }

    private void deleteNote(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] strarr = {"移出书架", "取消"};
        builder.setItems(strarr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    BSNoteListBean.ResponseDataBean bean = responseDataList.get(position);
                    String id = bean.getFactoryId();

                    Call<ModifyBean> removeCall = ReaderRetroift.getInstance(getActivity()).getApi().noteRemoveBS(id);
                    removeCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean body = response.body();
                            if (body != null) {
                                if (body.getResponseCode() == 1) {
                                    responseDataList.remove(position);
                                    bsNoteAdapter.notifyDataSetChanged();
                                    if (responseDataList.size() == 0) {
                                        bsTvEmptyBook.setVisibility(View.VISIBLE);
                                    }
                                }
                                    Toast.makeText(getActivity(), body.getResponseMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {
                        }
                    });
                } else {

                }
            }
        });
        builder.show();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    refresh();
                    break;

            }
        }

        ;
    };

    private void refresh() {
        init();
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.bs_note_nonet)
    public void onViewClicked() {
        bsNoteNonet.setVisibility(View.VISIBLE);
        bsGvNoteFactory.setVisibility(View.GONE);
        init();
    }
}
