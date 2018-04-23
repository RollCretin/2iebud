package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.CollectBookAdapter;
import com.hxjf.dubei.adapter.PersonCollectBookAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.FavoriteBookListbean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.StatusBarUtils;
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
 * Created by Chen_Zhang on 2017/9/20.
 */

public class ZoneCollectBookActivity extends BaseActivity {
    @BindView(R.id.zone_collect_book_name)
    TextView zoneCollectBookName;
    @BindView(R.id.zone_collect_book_back)
    ImageView zoneCollectBookBack;
    @BindView(R.id.zone_collect_book_listview)
    PullToRefreshListView zoneCollectBookListview;
    @BindView(R.id.zone_collect_book_progress)
    ProgressBar zoneCollectBookProgress;
    @BindView(R.id.zone_collect_book_empty)
    TextView zoneCollectBookEmpty;

    private Double longitude;
    private Double latitude;
    private String ownerId;
    private List<FavoriteBookListbean.ResponseDataBean.ContentBean> contentBeanList;
    private boolean isLast;
    private int currentPage = 1;
    private PersonCollectBookAdapter adapter;
    private String ownerName;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_zone_collect_book;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        ownerId = intent.getStringExtra("ownerId");
        ownerName = intent.getStringExtra("ownerName");
        zoneCollectBookName.setText(ownerName + "的藏书");
        //设置状态栏的颜色
        StatusBarUtils.setWindowStatusBarColor(this, R.color.note_bg);
        String loc = SPUtils.getString(this, "location", "114.07/22.62");
        String[] split = loc.split("/");
        longitude = Double.valueOf(split[0]);
        latitude = Double.valueOf(split[1]);
        Call<FavoriteBookListbean> personFavoriteBookListCall = NewDiscoveryRetrofit.getInstance(this).getApi().PersonFavoriteBookListCall(ownerId, longitude, latitude, 1, 10);
        personFavoriteBookListCall.enqueue(new Callback<FavoriteBookListbean>() {
            @Override
            public void onResponse(Call<FavoriteBookListbean> call, Response<FavoriteBookListbean> response) {
                FavoriteBookListbean bookListbean = response.body();
                if (bookListbean != null) {
                    if (bookListbean.getResponseCode() == 1 && bookListbean.getResponseData() != null) {
                        FavoriteBookListbean.ResponseDataBean responseData = bookListbean.getResponseData();
                        contentBeanList = responseData.getContent();
                        isLast = responseData.isLast();
                        initData();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteBookListbean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        zoneCollectBookProgress.setVisibility(View.GONE);
        zoneCollectBookEmpty.setVisibility(View.GONE);
        if (contentBeanList.size() == 0) {
            zoneCollectBookEmpty.setVisibility(View.VISIBLE);
        }
        adapter = new PersonCollectBookAdapter(this, contentBeanList);
        zoneCollectBookListview.setAdapter(adapter);
        zoneCollectBookListview.setMode(PullToRefreshBase.Mode.BOTH);
        zoneCollectBookListview.setOnRefreshListener(mListViewOnRefreshListener2);
        zoneCollectBookListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavoriteBookListbean.ResponseDataBean.ContentBean contentBean = contentBeanList.get(position - 1);
                Intent intent = new Intent(ZoneCollectBookActivity.this, BookBorrowDetailActivity.class);
                intent.putExtra("bookFavoriteId", contentBean.getId());
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
            zoneCollectBookListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockList(1);
                    zoneCollectBookListview.onRefreshComplete();//下拉刷新结束，下拉刷新头复位
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
            zoneCollectBookListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLast) {
                        currentPage += 1;
                        mockList(currentPage);
                    } else {
                        Toast.makeText(ZoneCollectBookActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                    }
                    zoneCollectBookListview.onRefreshComplete();//上拉加载更多结束，上拉加载头复位
                }
            }, 1500);
        }
    };

    private void mockList(final int i) {
        Call<FavoriteBookListbean> personFavoriteBookListCall = NewDiscoveryRetrofit.getInstance(this).getApi().PersonFavoriteBookListCall(ownerId, longitude, latitude, i, 10);
        personFavoriteBookListCall.enqueue(new Callback<FavoriteBookListbean>() {
            @Override
            public void onResponse(Call<FavoriteBookListbean> call, Response<FavoriteBookListbean> response) {
                FavoriteBookListbean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null) {
                        List<FavoriteBookListbean.ResponseDataBean.ContentBean> newcontentBeanList = bean.getResponseData().getContent();
                        if (i == 1) {
                            contentBeanList.clear();
                            currentPage = 1;
                        }
                        isLast = bean.getResponseData().isLast();
                        for (int j = 0; j < newcontentBeanList.size(); j++) {
                            contentBeanList.add(newcontentBeanList.get(j));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ZoneCollectBookActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteBookListbean> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.zone_collect_book_back)
    public void onClick() {
        finish();
    }

}
