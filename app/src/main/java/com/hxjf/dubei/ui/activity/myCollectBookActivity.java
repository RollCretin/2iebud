package com.hxjf.dubei.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.CollectBookAdapter;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.FavoriteBookListbean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.util.PermissionUtil;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.StatusBarUtils;
import com.hxjf.dubei.zxing.android.CaptureActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/12/25.
 */

public class myCollectBookActivity extends BaseActivity implements OnRefreshListener, OnLoadmoreListener,CollectBookAdapter.Callback {
    private static final int REQUEST_CODE = 1001;
    private static final int REQUEST_PERMISSION_CODE_TAKE_PIC = 1002;
    private static final int REQUEST_PERMISSION_SEETING = 1003;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    @BindView(R.id.my_collect_book_name)
    TextView myCollectBookName;
    @BindView(R.id.my_collect_book_back)
    ImageView myCollectBookBack;
    @BindView(R.id.my_collect_book_plus)
    ImageView myCollectBookPlus;
    @BindView(R.id.my_collect_book_progress)
    ProgressBar myCollectBookProgress;
    @BindView(R.id.my_collect_book_empty)
    TextView myCollectBookEmpty;
    @BindView(R.id.my_collect_book_listview)
    ListView myCollectBookListview;
    @BindView(R.id.my_collect_book_refresh)
    SmartRefreshLayout myCollectBookRefresh;

    private double longitude;
    private double latitude;
    private String userId;
    private List<FavoriteBookListbean.ResponseDataBean.ContentBean> contentBeanList;
    private CollectBookAdapter adapter;
    private boolean isLast;
    private int currentPage;
    private String[] mPermissionList;
    private int i = 1;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_collect_book;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        myCollectBookRefresh.setOnRefreshListener(this);
        myCollectBookRefresh.setOnLoadmoreListener(this);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.note_bg);
        String loc = SPUtils.getString(this, "location", "114.07/22.62 ");
        String[] split = loc.split("/");
        longitude = Double.valueOf(split[0]);
        latitude = Double.valueOf(split[1]);
        //SP获取bindbean
        String bindbeanStr = SPUtils.getString(this, "bindbean", "");
        Gson gson = new Gson();
        UserDetailBean userbean = gson.fromJson(bindbeanStr, UserDetailBean.class);
        if (userbean != null) {
            userId = userbean.getResponseData().getId();
            Log.d("userId...", "init: "+userId);
        }
        Call<FavoriteBookListbean> favoriteBookListCall = NewDiscoveryRetrofit.getInstance(this).getApi().PersonFavoriteBookListCall(userId, longitude, latitude, 1, 10);
        favoriteBookListCall.enqueue(new Callback<FavoriteBookListbean>() {
            @Override
            public void onResponse(Call<FavoriteBookListbean> call, Response<FavoriteBookListbean> response) {
                FavoriteBookListbean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null) {
                        contentBeanList = bean.getResponseData().getContent();
                        isLast = bean.getResponseData().isLast();
                        initData();
                    } else {
                        Toast.makeText(myCollectBookActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<FavoriteBookListbean> call, Throwable t) {
                Log.d("isbn", "onFailure: "+t.toString());
            }
        });

    }

    private void initData() {
        myCollectBookProgress.setVisibility(View.GONE);
        myCollectBookEmpty.setVisibility(View.GONE);
        if (contentBeanList.size() == 0) {
            myCollectBookEmpty.setVisibility(View.VISIBLE);
        }
        adapter = new CollectBookAdapter(this, contentBeanList,this);
        myCollectBookListview.setAdapter(adapter);
        myCollectBookListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入我的藏书详情MyBookBorrowDetailActivity
                FavoriteBookListbean.ResponseDataBean.ContentBean contentBean = contentBeanList.get(position - 1);
//                Intent intent = new Intent(myCollectBookActivity.this, MyBookBorrowDetailActivity.class);
                Intent intent = new Intent(myCollectBookActivity.this, BookBorrowDetailActivity.class);
                intent.putExtra("bookFavoriteId", contentBean.getId());
                startActivity(intent);
            }
        });
        myCollectBookListview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        //刷新
        refreshlayout.finishRefresh(2000);
        mockList(1);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        //加载更多
        refreshlayout.finishLoadmore(2000);
        if (!isLast) {
            i = i + 1;
            mockList(i);
        } else {
            Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
        }
    }

    private void mockList(final int i) {
        Call<FavoriteBookListbean> personFavoriteBookListCall = NewDiscoveryRetrofit.getInstance(this).getApi().PersonFavoriteBookListCall(userId, longitude, latitude, i, 10);
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
                        Toast.makeText(myCollectBookActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteBookListbean> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.my_collect_book_back, R.id.my_collect_book_plus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_collect_book_back:
                finish();
                break;
            case R.id.my_collect_book_plus:
                requestPermission();
                break;
        }
    }

    private void requestPermission() {
        mPermissionList = new String[]{Manifest.permission.CAMERA};
        PermissionUtil.checkPermission(myCollectBookActivity.this, myCollectBookPlus, mPermissionList, REQUEST_PERMISSION_CODE_TAKE_PIC, new PermissionUtil.permissionInterface() {
            @Override
            public void success() {
                Intent intent = new Intent(myCollectBookActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);

                Toast.makeText(myCollectBookActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE_TAKE_PIC) {
            if (PermissionUtil.verifyPermissions(grantResults)) {//有权限
                Intent intent = new Intent(myCollectBookActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                //没有权限
                if (!PermissionUtil.shouldShowPermissions(myCollectBookActivity.this, permissions)) {//这个返回false 表示勾选了不再提示
                    Snackbar.make(myCollectBookPlus, "请去设置界面设置权限",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("去设置", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSION_SEETING);
                                }
                            })
                            .show();
                } else {
                    //表示没有权限 ,但是没勾选不再提示
                    Snackbar.make(myCollectBookPlus, "请允许权限请求！",
                            Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    ActivityCompat
                                            .requestPermissions(myCollectBookActivity.this, mPermissionList,
                                                    REQUEST_PERMISSION_CODE_TAKE_PIC);
                                }
                            })
                            .show();
                    requestPermission();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void click(View v) {
        int position = (int) v.getTag();
        FavoriteBookListbean.ResponseDataBean.ContentBean contentBean = contentBeanList.get(position);
        Intent intent = new Intent(myCollectBookActivity.this, BookBorrowDetailActivity.class);
        intent.putExtra("bookFavoriteId", contentBean.getId());
        startActivity(intent);
    }

    @Override
    public void delete(View v) {
        final int position = (int) v.getTag();
        FavoriteBookListbean.ResponseDataBean.ContentBean contentBean = contentBeanList.get(position);
        Call<ModifyBean> deleteCollectBookCall = NewDiscoveryRetrofit.getInstance(this).getApi().deleteCollectBookCall(contentBean.getId());
        deleteCollectBookCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null){
                        contentBeanList.remove(position);

                        Toast.makeText(myCollectBookActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {
                Toast.makeText(myCollectBookActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
