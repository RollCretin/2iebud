package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.ChallengeUserListAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ChallengeJoinUserListBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/4.
 */

public class ChallengeUserActivity extends BaseActivity {
    @BindView(R.id.challenge_user_list)
    PullToRefreshListView challengeUserList;
    int currentpage = 0;
    @BindView(R.id.challenge_user_return)
    ImageView challengeUserReturn;
    private List<ChallengeJoinUserListBean.ResponseDataBean.ContentBean> contentBeanList;
    private boolean isLast;
    private String challengeId;
    private ChallengeUserListAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_challenge_user;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        //请求挑战参赛者信息
        challengeId = intent.getStringExtra("challengeId");
        Call<ChallengeJoinUserListBean> challengeUserListCall = ReaderRetroift.getInstance(this).getApi().challengeUserListCall(currentpage, 10, challengeId);
        challengeUserListCall.enqueue(new Callback<ChallengeJoinUserListBean>() {

            @Override
            public void onResponse(Call<ChallengeJoinUserListBean> call, Response<ChallengeJoinUserListBean> response) {
                ChallengeJoinUserListBean bean = response.body();
                if (bean != null && bean.getResponseData()!=null) {
                    contentBeanList = bean.getResponseData().getContent();
                    isLast = bean.getResponseData().isLast();
                    Intent in = new Intent();
                    in.putExtra( "result", "res" );
                    setResult( RESULT_OK, in );
                    initData();
                }
            }

            @Override
            public void onFailure(Call<ChallengeJoinUserListBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        adapter = new ChallengeUserListAdapter(this,contentBeanList);
        challengeUserList.setMode(PullToRefreshBase.Mode.BOTH);
        challengeUserList.setAdapter(adapter);
        challengeUserList.setOnRefreshListener(mListViewOnRefreshListener2);
    }

    @OnClick(R.id.challenge_user_return)
    public void onClick() {
        finish();
    }
    private PullToRefreshBase.OnRefreshListener2<ListView> mListViewOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        /**
         * 下拉刷新回调
         * @param refreshView
         */
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            //模拟延时三秒刷新
            challengeUserList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(currentpage);
                    challengeUserList.onRefreshComplete();//下拉刷新结束，下拉刷新头复位

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
            challengeUserList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //TODO
                    if (!isLast){
                        currentpage += 1;
                        mockList(currentpage);
                    }else{
                        Toast.makeText(ChallengeUserActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    challengeUserList.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<ChallengeJoinUserListBean> challengeUserListCall = ReaderRetroift.getInstance(this).getApi().challengeUserListCall(i, 10, challengeId);
        challengeUserListCall.enqueue(new Callback<ChallengeJoinUserListBean>() {

            @Override
            public void onResponse(Call<ChallengeJoinUserListBean> call, Response<ChallengeJoinUserListBean> response) {
                ChallengeJoinUserListBean bean = response.body();
                if (bean != null && bean.getResponseData()!=null) {
                    if (i == 0){
                        contentBeanList.clear();
                        currentpage = 0;
                    }
                    List<ChallengeJoinUserListBean.ResponseDataBean.ContentBean> newcontentList = bean.getResponseData().getContent();
                    for (ChallengeJoinUserListBean.ResponseDataBean.ContentBean contentbean: newcontentList) {
                        contentBeanList.add(contentbean);
                    }
                    isLast = bean.getResponseData().isLast();
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ChallengeJoinUserListBean> call, Throwable t) {

            }
        });
    }
}
