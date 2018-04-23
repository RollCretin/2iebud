package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BooklistAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.MyBookListBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/6/21.
 */

public class MyBookListActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.booklist_scrollView)
    PullToZoomListViewEx booklistScrollView;
    @BindView(R.id.booklist_no_date_back)
    ImageView booklistNoDateBack;
    @BindView(R.id.booklist_no_data)
    RelativeLayout booklistNoData;
    @BindView(R.id.no_booklist_bg)
    RelativeLayout noBooklistBg;

    private List<String> booklist;
    private List<MyBookListBean.ResponseDataBean> responseData;
    private TextView booklistEmpty;
    private ListView booklistListView;
    private BooklistAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_book_list;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //获取数据
        Call<MyBookListBean> myBookListCall = ReaderRetroift.getInstance(this).getApi().myBookListCall();
        myBookListCall.enqueue(new Callback<MyBookListBean>() {
            @Override
            public void onResponse(Call<MyBookListBean> call, Response<MyBookListBean> response) {
                MyBookListBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    responseData = bean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<MyBookListBean> call, Throwable t) {

            }
        });

        View headView = LayoutInflater.from(this).inflate(R.layout.booklist_head_view, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.booklist_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.booklist_content_view, null, false);

        ImageView booklistBack = (ImageView) headView.findViewById(R.id.booklist_back);
        booklistEmpty = (TextView) contentView.findViewById(R.id.booklist_empty);
        booklistListView = (ListView) contentView.findViewById(R.id.booklist_listView);

        booklistBack.setOnClickListener(this);

        booklistScrollView.setHeaderView(headView);
        booklistScrollView.setZoomView(zoomView);
        int[] screenWidthAndHeight = DensityUtil.getScreenWidthAndHeight(this);
        int mScreenWidth = screenWidthAndHeight[0];
        int mScreenHeight = screenWidthAndHeight[1];
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (4.0F * (mScreenHeight / 15.0F)));
        booklistScrollView.setHeaderLayoutParams(localObject);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mScreenWidth, (int) (4.0F * (mScreenHeight / 15.0F)));
        noBooklistBg.setLayoutParams(layoutParams);


    }

    private void initData() {
        booklistNoData.setVisibility(View.GONE);
        adapter = new BooklistAdapter(this, responseData);
        booklistScrollView.setAdapter(adapter);

        booklistScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //书单详情页
                //第一项是zoomview
                if (position != 0) {
                    String id1 = responseData.get(position - 1).getId();
                    if (responseData.get(position - 1).getStatus() == 0) {
                        Toast.makeText(MyBookListActivity.this, "这条书单已下架", Toast.LENGTH_SHORT).show();
                    } else {
                        startBookListActivity(id1);

                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                Call<MyBookListBean> myBookListCall = ReaderRetroift.getInstance(this).getApi().myBookListCall();
                myBookListCall.enqueue(new Callback<MyBookListBean>() {
                    @Override
                    public void onResponse(Call<MyBookListBean> call, Response<MyBookListBean> response) {
                        MyBookListBean bean = response.body();
                        if (bean != null) {
                            if (bean.getResponseData() == null) {
                                responseData.clear();
                            } else {
                                responseData.clear();
                                List<MyBookListBean.ResponseDataBean> newresponData = bean.getResponseData();
                                for (int i = 0; i < newresponData.size(); i++) {
                                    responseData.add(newresponData.get(i));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyBookListBean> call, Throwable t) {

                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.booklist_back:
                finish();
                break;
        }
    }

    private void startBookListActivity(String id) {
        Intent intent = new Intent(this, BookListDetailActivity.class);
        intent.putExtra("booklistid", id);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.booklist_no_date_back)
    public void onClick() {
        finish();
    }

}
