package com.hxjf.dubei.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MessageNoticeAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.MyMessageBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/28.
 */

public class NoticeActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener {

    @BindView(R.id.notice_back)
    ImageView noticeBack;
    @BindView(R.id.my_message_notice_list)
    ListView myMessageNoticeList;
    @BindView(R.id.my_message_refresh)
    SmartRefreshLayout myMessageRefresh;

    private boolean isLast = true;
    private int i = 0;

    private MessageNoticeAdapter adapter;
    private List<MyMessageBean.ResponseDataBean.ListBean> responseList;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_notice;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        myMessageRefresh.setOnRefreshListener(this);
        myMessageRefresh.setOnLoadmoreListener(this);

        //请求未读数据
        Call<MyMessageBean> myMessageCall = ReaderRetroift.getInstance(this).getApi().myMessageCall(i, 10);
        myMessageCall.enqueue(new Callback<MyMessageBean>() {


            @Override
            public void onResponse(Call<MyMessageBean> call, Response<MyMessageBean> response) {
                MyMessageBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    responseList = bean.getResponseData().getList();
                    isLast = bean.getResponseData().isIsLastPage();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<MyMessageBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        //设置消息为已读
        Call<ModifyBean> updateMessageCall = ReaderRetroift.getInstance(this).getApi().updateMessageCall();
        updateMessageCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });

        adapter = new MessageNoticeAdapter(this, responseList);
        myMessageNoticeList.setAdapter(adapter);
        //长按删除
        myMessageNoticeList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        myMessageNoticeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteMessage(position);
                return true;
            }
        });
        adapter.setOnItemDeleteClickListener(new MessageNoticeAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int i) {
                //跳转到个人详情中
                String msgFrom = responseList.get(i).getMsgFrom();
                if (!msgFrom.equals("DUBEI_ASSISTANT")) {
                    Intent intent = new Intent(NoticeActivity.this, PersonDetailActivity.class);
                    intent.putExtra("userId", msgFrom);
                    startActivity(intent);
                }
            }
        });
    }

    private void deleteMessage(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] strarr = {"删除消息", "取消"};
        builder.setItems(strarr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //删除消息
                    Call<ModifyBean> deleteMessageCall = ReaderRetroift.getInstance(NoticeActivity.this).getApi().deleteMessageCall(responseList.get(position).getId());
                    deleteMessageCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                if (bean.getResponseCode() == 1) {

                                    responseList.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(NoticeActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                }
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


    @OnClick(R.id.notice_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        //刷新
        refreshlayout.finishRefresh(2000);
        if (responseList != null) {
            responseList.clear();
        }
        refresh(0);
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        //加载更多
        refreshlayout.finishLoadmore(2000);
        if (!isLast) {
            i = i + 1;

            refresh(i);
        } else {
            Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
        }
    }

    private void refresh(int i) {
        Call<MyMessageBean> myMessageCall = ReaderRetroift.getInstance(this).getApi().myMessageCall(i, 10);
        myMessageCall.enqueue(new Callback<MyMessageBean>() {

            @Override
            public void onResponse(Call<MyMessageBean> call, Response<MyMessageBean> response) {
                MyMessageBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    List<MyMessageBean.ResponseDataBean.ListBean> list = bean.getResponseData().getList();
                    isLast = bean.getResponseData().isIsLastPage();
                    for (int i = 0; i < list.size(); i++) {
                        responseList.add(list.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MyMessageBean> call, Throwable t) {

            }
        });
    }

}
