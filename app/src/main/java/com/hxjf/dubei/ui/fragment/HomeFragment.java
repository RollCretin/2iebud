package com.hxjf.dubei.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.CardViewAdapter;
import com.hxjf.dubei.adapter.pkListAdapter;
import com.hxjf.dubei.anim.RecyclerViewItemAnimator;
import com.hxjf.dubei.bean.Card;
import com.hxjf.dubei.bean.ChallengeNumBean;
import com.hxjf.dubei.bean.DuBeiBean;
import com.hxjf.dubei.bean.HomeChallengeDetailBean;
import com.hxjf.dubei.bean.PKListBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.EnhancedCall;
import com.hxjf.dubei.network.EnhancedCallback;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.ChallegeBookActivity;
import com.hxjf.dubei.ui.activity.ChallengeDetailActivity;
import com.hxjf.dubei.ui.activity.LoginActivity;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.ScreenSizeUtil;
import com.hxjf.dubei.widget.LoadDialog;
import com.hxjf.dubei.widget.MySwipeRefreshLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/5/19.
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final int REFRESH_COMPLETE = 100;
    private static final String TAG = "HomeFragment";
    LinearLayoutManager mLayoutManager;
    CardViewAdapter mAdapter;
    List<Card> mCardList = new ArrayList<>();

    @BindView(R.id.home_recyclerview)
    RecyclerView homeRecyclerview;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.home_tv_PK)
    TextView homeTvPK;
    @BindView(R.id.home_ll_PK)
    LinearLayout homeLlPK;
    @BindView(R.id.home_plus)
    ImageView homePlus;
    @BindView(R.id.home_lv_PKlist)
    ListView homeLvPKlist;
    @BindView(R.id.home_refresh)
    MySwipeRefreshLayout homeRefresh;
    @BindView(R.id.home_nonet)
    TextView homeNonet;
    @BindView(R.id.dragView)
    LinearLayout homeDragView;


    private InputStream inputStream;
    private FileOutputStream fileOutputStream;
    private String savefilePath;
    private File saveFile;
    private List<HomeChallengeDetailBean> challengeInfoList;
    private List<DuBeiBean.ResponseDataBean.ChallengeInfoBean> challengeInfoIdList;
    private List<DuBeiBean.ResponseDataBean.RecommendInfoBean> recommendInfoList;
    private List<PKListBean.ResponseDataBean.ContentBean> pkInfoList;
    private int count;
    private pkListAdapter pklistadapter;
    private Timer timer;
    int page = 0;
    private int giveReadCoin;
    private int currentFirst;
    private boolean isLast;
    private int lastItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_home_content, null);
        ButterKnife.bind(this, view);
        init();
        homeRefresh.setOnRefreshListener(this);
        homeRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        new LinearSnapHelper().attachToRecyclerView(homeRecyclerview);//使之滑动并居中停止
        homeRecyclerview.addItemDecoration(new SpacesItemDecoration(ScreenSizeUtil.Dp2Px(getContext(), 5)));//设置recycleView的间隔
        return view;
    }

    private void init() {
        timer = new Timer();
        homeRefresh.setRefreshing(true);
        int screenHeight = ScreenSizeUtil.getScreenHeight(getActivity());
        int statusBarHeight = DensityUtil.getStatusBarHeight(getActivity());
        //动态设置滑动面板的高度
        slidingLayout.setPanelHeight(screenHeight - statusBarHeight - DensityUtil.dip2px(getActivity(), 530));
        //homeLvPKlist禁止滚动
        homeLvPKlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });


        //首页数据请求
        Call<DuBeiBean> duBeiCall = ReaderRetroift.getInstance(getActivity()).getApi().duBeiCall();

        EnhancedCall<DuBeiBean> enhancedCall = new EnhancedCall<>(duBeiCall);
        enhancedCall.dataClassName(DuBeiBean.class).enqueue(new EnhancedCallback<DuBeiBean>() {
            @Override
            public void onResponse(Call<DuBeiBean> call, Response<DuBeiBean> response) {
                DuBeiBean bean = response.body();
                if (bean != null && bean.getResponseCode() == 2) {
                    SharedPreferences cookies_prefs = getActivity().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
                    cookies_prefs.edit().clear().commit();
                    SPUtils.remove(getActivity(), "bindbean");
                    //将栈中所有activity清空，跳到login页面
                    Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (bean != null && bean.getResponseData() != null && bean.getResponseCode() == 1) {
                    challengeInfoIdList = bean.getResponseData().getChallengeInfo();//这里只有id是有用的
                    recommendInfoList = bean.getResponseData().getRecommendInfo();
                    homeRefresh.setRefreshing(false);
                    initCount();
                } else {
                    if (bean != null) {
                        homeRefresh.setRefreshing(false);

                        Toast.makeText(getActivity(), bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DuBeiBean> call, Throwable t) {
                Toast.makeText(getContext(), "请检查网络...", Toast.LENGTH_LONG).show();
                homeRefresh.setRefreshing(false);
                homeNonet.setVisibility(View.VISIBLE);
                homeRecyclerview.setVisibility(View.GONE);
                homeLvPKlist.setVisibility(View.GONE);

            }

            @Override
            public void onGetCache(DuBeiBean bean) {
                Toast.makeText(getContext(), "请检查网络...", Toast.LENGTH_LONG).show();
                if (bean != null && bean.getResponseCode() == 2) {
                    SharedPreferences cookies_prefs = getActivity().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
                    cookies_prefs.edit().clear().commit();
                    SPUtils.remove(getActivity(), "bindbean");
                    //将栈中所有activity清空，跳到login页面
                    Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (bean != null && bean.getResponseData() != null && bean.getResponseCode() == 1) {
                    challengeInfoIdList = bean.getResponseData().getChallengeInfo();//这里只有id是有用的
                    recommendInfoList = bean.getResponseData().getRecommendInfo();
                    homeRefresh.setRefreshing(false);
                    initCount();
                } else {
                    if (bean != null) {
                        homeRefresh.setRefreshing(false);

                        Toast.makeText(getActivity(), bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //PK榜数据请求
        page = 0;
        Call<PKListBean> pkListBeanCall = ReaderRetroift.getInstance(getActivity()).getApi().PKListCall(page, 10);
        EnhancedCall<PKListBean> PKenhancedCall = new EnhancedCall<>(pkListBeanCall);
        PKenhancedCall.dataClassName(PKListBean.class).enqueue(new EnhancedCallback<PKListBean>() {
            @Override
            public void onResponse(Call<PKListBean> call, Response<PKListBean> response) {
                PKListBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    pkInfoList = bean.getResponseData().getContent();
                    isLast = bean.getResponseData().isLast();
                    pklistadapter = new pkListAdapter(getContext(), pkInfoList);
                    pklistadapter.notifyDataSetChanged();
                    homeLvPKlist.setAdapter(pklistadapter);
                    homeLvPKlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (pkInfoList.get(position).getChallengeUserList() != null) {
                                if (pkInfoList.get(position).getChallengeUserList().size() > 0) {
                                    if (pkInfoList.get(position).getBookInfo().getStatus() == 0) {
                                        Toast.makeText(getActivity(), "该书已下架", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent challengeDetailIntent = new Intent(getActivity(), ChallengeDetailActivity.class);
                                        challengeDetailIntent.putExtra("challengeId", pkInfoList.get(position).getChallengeUserList().get(0).getChallengeId());
                                        startActivity(challengeDetailIntent);
                                    }
                                }
                            }
                        }
                    });

                    //SlidingUpPanel添加监听器
                    slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                        @Override
                        public void onPanelSlide(View panel, float slideOffset) {

                        }

                        @Override
                        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                            //当PanelState的状态为EXPANDED，改变ActionBar样式
                            if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                                //当读呗PK榜移动到顶部，字体居中
                                homeTvPK.setGravity(Gravity.CENTER);
                                //homeLvPKlist允许滚动
                                homeLvPKlist.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_MOVE:
                                                return false;
                                            default:
                                                break;
                                        }
                                        return false;
                                    }
                                });
                            } else {
                                homeTvPK.setGravity(Gravity.NO_GRAVITY);
                                //homeLvPKlist禁止滚动
                                homeLvPKlist.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_MOVE:
                                                return true;
                                            default:
                                                break;
                                        }
                                        return true;
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PKListBean> call, Throwable t) {

            }

            @Override
            public void onGetCache(PKListBean bean) {
                if (bean != null && bean.getResponseData() != null) {
                    pkInfoList = bean.getResponseData().getContent();
                    isLast = bean.getResponseData().isLast();
                    pklistadapter = new pkListAdapter(getContext(), pkInfoList);
                    pklistadapter.notifyDataSetChanged();
                    homeLvPKlist.setAdapter(pklistadapter);
                    homeLvPKlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (pkInfoList.get(position).getChallengeUserList() != null) {
                                if (pkInfoList.get(position).getChallengeUserList().size() > 0) {
                                    if (pkInfoList.get(position).getBookInfo().getStatus() == 0) {
                                        Toast.makeText(getActivity(), "该书已下架", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent challengeDetailIntent = new Intent(getActivity(), ChallengeDetailActivity.class);
                                        challengeDetailIntent.putExtra("challengeId", pkInfoList.get(position).getChallengeUserList().get(0).getChallengeId());
                                        startActivity(challengeDetailIntent);
                                    }
                                }
                            }
                        }
                    });

                    //SlidingUpPanel添加监听器
                    slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                        @Override
                        public void onPanelSlide(View panel, float slideOffset) {

                        }

                        @Override
                        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                            //当PanelState的状态为EXPANDED，改变ActionBar样式
                            if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                                //当读呗PK榜移动到顶部，字体居中
                                homeTvPK.setGravity(Gravity.CENTER);
                                //homeLvPKlist允许滚动
                                homeLvPKlist.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_MOVE:
                                                return false;
                                            default:
                                                break;
                                        }
                                        return false;
                                    }
                                });
                            } else {
                                homeTvPK.setGravity(Gravity.NO_GRAVITY);
                                //homeLvPKlist禁止滚动
                                homeLvPKlist.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_MOVE:
                                                return true;
                                            default:
                                                break;
                                        }
                                        return true;
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        homeLvPKlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        Log.d(TAG, "onScrollStateChanged: 最后了");
                        if (!isLast) {
                            loadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;

            }
        });
    }


    private void initCount() {
        //请求挑战总次数
        Call<ChallengeNumBean> challengeNumCall = ReaderRetroift.getInstance(getActivity()).getApi().challengeNumCall();
        EnhancedCall<ChallengeNumBean> enhancedCall = new EnhancedCall<>(challengeNumCall);
        enhancedCall.dataClassName(ChallengeNumBean.class).enqueue(new EnhancedCallback<ChallengeNumBean>() {
            @Override
            public void onResponse(Call<ChallengeNumBean> call, Response<ChallengeNumBean> response) {
                ChallengeNumBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    count = bean.getResponseData().getCount();
                    initData(count);
                }
            }

            @Override
            public void onFailure(Call<ChallengeNumBean> call, Throwable t) {
                Toast.makeText(getActivity(), "请检查网络...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGetCache(ChallengeNumBean challengeNumBean) {
                count = challengeNumBean.getResponseData().getCount();
                initData(count);
            }
        });
    }


    private void initData(int num) {

        //SP获取bindbean--获取用户是否拿到赠送的读币（1.1.2版本后去除，改区块链阅读入口）
        String bindbeanStr = SPUtils.getString(getActivity(), "bindbean", "");
        Gson gson = new Gson();
        final UserDetailBean userbean = gson.fromJson(bindbeanStr, UserDetailBean.class);
        if (userbean != null && userbean.getResponseData() != null) {
            giveReadCoin = userbean.getResponseData().getGiveReadCoin();//0表示未领取，1表示领取读币
        }
        currentFirst = 0;//1-首位置区块链入口存在 0 - 不存在
        //左右滑动RecycleView
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        homeRecyclerview.setLayoutManager(mLayoutManager);
        mAdapter = new CardViewAdapter(getContext(), currentFirst, challengeInfoIdList, recommendInfoList);
        mAdapter.setData(num);
        homeRecyclerview.setAdapter(mAdapter);
        RecyclerViewItemAnimator mItemAnimator = new RecyclerViewItemAnimator();

        homeRecyclerview.setItemAnimator(mItemAnimator);
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new RequestTimerTask(), 1000, 1000);

        mAdapter.notifyDataSetChanged();


        mAdapter.setmOnItemClickListener(new CardViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //挑战信息点击进入挑战详情
                if (position < challengeInfoIdList.size()) {
                    if (challengeInfoIdList.get(position).getBookInfo().getStatus() == 0) {
                        Toast.makeText(getActivity(), "该书已下架", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), ChallengeDetailActivity.class);
                        intent.putExtra("challengeId", challengeInfoIdList.get(position).getChallengeUserList().get(0).getChallengeId());
                        startActivity(intent);
                    }
                }

            }
        });
    }

    private void loadMore() {
        LoadDialog.show(getActivity());
        page += 1;
        Call<PKListBean> pkListBeanCall = ReaderRetroift.getInstance(getActivity()).getApi().PKListCall(page, 10);
        pkListBeanCall.enqueue(new Callback<PKListBean>() {
            @Override
            public void onResponse(Call<PKListBean> call, Response<PKListBean> response) {
                PKListBean bean = response.body();
                if (bean != null) {
                    isLast = bean.getResponseData().isLast();
                    if (bean.getResponseData().getNumberOfElements() == 0) {
                        Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();

                    } else {
                        List<PKListBean.ResponseDataBean.ContentBean> newcontentBeanList = bean.getResponseData().getContent();
                        for (int i = 0; i < newcontentBeanList.size(); i++) {
                            PKListBean.ResponseDataBean.ContentBean contentBean = newcontentBeanList.get(i);
                            pkInfoList.add(contentBean);
                        }
                        pklistadapter.notifyDataSetChanged();
                    }
                    LoadDialog.dismiss(getActivity());
                }
            }

            @Override
            public void onFailure(Call<PKListBean> call, Throwable t) {

            }
        });
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mAdapter.notifyDataSetChanged();
            }
        }
    };

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
        //手动刷新数据
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        //判断是否要刷新
        int isRefresh = SPUtils.getInt(getActivity(), "isRefresh", 0);//0-不刷新，1-刷新
        if (isRefresh == 1) {
            refresh();
            //存一下下次不刷新
            SPUtils.putInt(getActivity(), "isRefresh", 0);
        }

    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 10000);
    }


    class RequestTimerTask extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    ;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }


    }

    @OnClick({R.id.home_plus,R.id.home_nonet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_plus:
                //选择挑战书籍
                Intent intent = new Intent(getActivity(), ChallegeBookActivity.class);
                startActivity(intent);
                break;
            case R.id.home_nonet:
                homeNonet.setVisibility(View.GONE);
                homeRecyclerview.setVisibility(View.VISIBLE);
                homeLvPKlist.setVisibility(View.VISIBLE);
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
                init();
                break;
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0) {
//                outRect.left = 4 * space;
                outRect.right = space;
                outRect.left = (ScreenSizeUtil.getScreenWidth(getContext()) - homeRecyclerview.getChildAt(0).getWidth()) / 2;
            } else if (parent.getChildPosition(view) == challengeInfoIdList.size() + recommendInfoList.size() + currentFirst) {
//                outRect.right = 4 * space;
                outRect.left = space;
                outRect.right = (ScreenSizeUtil.getScreenWidth(getContext()) - homeRecyclerview.getChildAt(0).getWidth()) / 2;
            } else {
                outRect.left = space;
                outRect.right = space;
            }
        }
    }

}
