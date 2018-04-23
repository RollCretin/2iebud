package com.hxjf.dubei.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.GalleryAdapter;
import com.hxjf.dubei.bean.BookFriendBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.NewDiscoveryBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ImLoginHelper;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.ui.activity.BookBorrowDetailActivity;
import com.hxjf.dubei.ui.activity.MoreBookBorrowActivity;
import com.hxjf.dubei.ui.activity.MoreFriendActivity;
import com.hxjf.dubei.ui.activity.MoreWonderfulActivity;
import com.hxjf.dubei.ui.activity.MoreZoneActivity;
import com.hxjf.dubei.ui.activity.PersonDetailActivity;
import com.hxjf.dubei.ui.activity.WonderfulDetailActivity;
import com.hxjf.dubei.ui.activity.ZoneDetailActivity;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.PermissionUtil;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.widget.MySwipeRefreshLayout;
import com.hxjf.dubei.widget.myGallery;
import com.umeng.socialize.utils.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Created by Chen_Zhang on 2017/9/14.
 */

public class ZoneFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AMapLocationListener {
    private static final int REQUEST_CODE = 1001;
    private static final int REQUEST_PERMISSION_SEETING = 1003;
    private static final int REQUEST_PERMISSION_CODE_LOCATION = 1004;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String TAG = "ZoneFragment";
    private static LocationManager locationManager;
    private static String locationProvider;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    @BindView(R.id.zone_address)
    TextView zoneAddress;
    @BindView(R.id.zone_tag)
    TextView zoneTag;
    @BindView(R.id.zone_recommend_friend_container)
    LinearLayout zoneRecommendFriendContainer;
    @BindView(R.id.zone_wonderful_activity_arrow)
    LinearLayout zoneWonderfulActivityArrow;
    @BindView(R.id.zone_wonderful_activity_container)
    LinearLayout zoneWonderfulActivityContainer;
    @BindView(R.id.zone_book_borrow)
    LinearLayout zoneBookBorrow;
    @BindView(R.id.zone_book_borrow_container)
    LinearLayout zoneBookBorrowContainer;
    @BindView(R.id.zone_recommend_friend)
    LinearLayout zoneRecommendFriend;
    @BindView(R.id.zone_detail_zone_name)
    RelativeLayout zoneDetailZoneName;
    @BindView(R.id.zone_more)
    ImageView zoneMore;
    @BindView(R.id.zone_refresh)
    MySwipeRefreshLayout zoneRefresh;
    @BindView(R.id.zone_discovery)
    TextView zoneDiscovery;
    @BindView(R.id.zone_gallery)
    myGallery zoneGallery;
    private GalleryAdapter adapter;
    private String[] mPermissionList;
    private String[] locationPermissionList;
    private double longitude;//经度
    private double latitude;//纬度
    private List<NewDiscoveryBean.ResponseDataBean.BookbarsBean> bookbars;
    private List<NewDiscoveryBean.ResponseDataBean.ActivitiesBean> activities;
    private List<NewDiscoveryBean.ResponseDataBean.BookFavoritesBean> bookFavorites;
    private List<BookFriendBean.ResponseDataBean.ContentBean> bookFriendList;
    private ArrayList<TextView> tvList;
    private double distanceKM = 0.0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_zone_content, null);
        ButterKnife.bind(this, view);
        init();
        zoneRefresh.setOnRefreshListener(this);
        zoneRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));

        requestPermission();

        return view;
    }

    private void requestPermission() {
        //申请位置权限
        locationPermissionList = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        PermissionUtil.checkPermission2(this, getActivity().getWindow().getDecorView(), locationPermissionList, REQUEST_PERMISSION_CODE_LOCATION, new PermissionUtil.permissionInterface() {
            @Override
            public void success() {
                //定位
                location();
            }

        });
    }

    private void init() {
        zoneRefresh.setRefreshing(true);
    }


    private void initData() {
        Call<NewDiscoveryBean> newDiscoveryCall = NewDiscoveryRetrofit.getInstance(getActivity()).getApi().newDiscoveryCall(longitude, latitude);
        newDiscoveryCall.enqueue(new Callback<NewDiscoveryBean>() {
            @Override
            public void onResponse(Call<NewDiscoveryBean> call, Response<NewDiscoveryBean> response) {
                NewDiscoveryBean newDiscoveryBean = response.body();
                if (newDiscoveryBean != null) {
                        zoneRefresh.setRefreshing(false);
                    if (newDiscoveryBean.getResponseCode() == 1 && newDiscoveryBean.getResponseData() != null) {
                        bookbars = newDiscoveryBean.getResponseData().getBookbars();
                        activities = newDiscoveryBean.getResponseData().getActivities();
                        bookFavorites = newDiscoveryBean.getResponseData().getBookFavorites();
                        initbookbar();
                        initActivity();
                        initBookFavorite();
                    } else {
                        Toast.makeText(getActivity(), newDiscoveryBean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
                android.util.Log.d(TAG, "onResponse: "+newDiscoveryBean);
            }

            @Override
            public void onFailure(Call<NewDiscoveryBean> call, Throwable t) {
                android.util.Log.d(TAG, "onFailure: "+t.toString());
            }
        });

        //推荐书友
        Call<BookFriendBean> recommendFriendCall = NewDiscoveryRetrofit.getInstance(getActivity()).getApi().RecommendFriendCall(longitude, latitude, 1, 3);
        recommendFriendCall.enqueue(new Callback<BookFriendBean>() {
            @Override
            public void onResponse(Call<BookFriendBean> call, Response<BookFriendBean> response) {
                BookFriendBean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null) {
                        bookFriendList = bean.getResponseData().getContent();
                        initFriend();
                    } else {
                        Toast.makeText(getActivity(), bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookFriendBean> call, Throwable t) {
                android.util.Log.d(TAG, "initFriendonFailure: "+t.toString());
            }
        });

    }

    private void initFriend() {
        //推荐书友
        zoneRecommendFriendContainer.removeAllViews();
        if(bookFriendList.size() == 0){
            zoneRecommendFriend.setVisibility(View.GONE);
            zoneRecommendFriendContainer.setVisibility(View.GONE);
            return;
        }else{
            zoneRecommendFriend.setVisibility(View.VISIBLE);
            zoneRecommendFriendContainer.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < bookFriendList.size(); i++) {
            View child = View.inflate(getActivity(), R.layout.item_zone_friend, null);
            final BookFriendBean.ResponseDataBean.ContentBean contentBean = bookFriendList.get(i);
            ImageView pratroit = (ImageView) child.findViewById(R.id.friend_pratroit);
            TextView name = (TextView) child.findViewById(R.id.friend_name);
            TextView bookcount = (TextView) child.findViewById(R.id.friend_book_count);
            TextView distance = (TextView) child.findViewById(R.id.friend_distance);
            TextView tag1 = (TextView) child.findViewById(R.id.friend_tag1);
            TextView tag2 = (TextView) child.findViewById(R.id.friend_tag2);
            TextView tag3 = (TextView) child.findViewById(R.id.friend_tag3);
            tvList = new ArrayList<>();
            tvList.add(tag1);
            tvList.add(tag2);
            tvList.add(tag3);
            TextView message = (TextView) child.findViewById(R.id.friend_btn_message);
            Glide.with(getActivity()).load(NewDiscoveryRetrofit.IMAGE_URL + contentBean.getHeadPhoto()).into(pratroit);
            name.setText(contentBean.getNickName());
            bookcount.setText(contentBean.getBookCount() + "本藏书");
//            Double miles = Double.valueOf(contentBean.getDistance());
//            if (miles >= 1000){
//                distanceKM = miles / 1000.0;
//                DecimalFormat format = new DecimalFormat("######0.00");
//                distance.setText(format.format(distanceKM) + " KM");
//            }else {
//                distance.setText((new Double(miles)).intValue() + " M");
//            }
            distance.setText(contentBean.getCityDistrict());

            //todo 标签
            String tag = contentBean.getTag();
            if (tag != null && !"".equals(tag)) {
                String[] split = tag.split("、");

                for (int j = 0; j < split.length; j++) {
                    tvList.get(j).setVisibility(View.VISIBLE);
                    tvList.get(j).setText(split[j]);
                }
            }

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //私信
                    YWIMKit imKit = ImLoginHelper.getInstance().getIMKit();
                    Intent intent = imKit.getChattingActivityIntent(contentBean.getUserId(), ImLoginHelper.getInstance().getAPP_KEY());
                    startActivity(intent);
                }
            });
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PersonDetailActivity.class);
                    intent.putExtra("userId", contentBean.getUserId());
                    startActivity(intent);
                }
            });
            zoneRecommendFriendContainer.addView(child);
        }
    }

    private void initBookFavorite() {
        //图书借阅
        if (bookFavorites.size() == 0 ){
            zoneBookBorrow.setVisibility(View.GONE);
        }
        zoneBookBorrowContainer.removeAllViews();
        for (int i = 0; i < bookFavorites.size(); i++) {
            View child = View.inflate(getActivity(), R.layout.item_zone_book, null);
            final NewDiscoveryBean.ResponseDataBean.BookFavoritesBean bookFavoritesBean = bookFavorites.get(i);
            ImageView bookBg = (ImageView) child.findViewById(R.id.zone_book_image);
            TextView bookName = (TextView) child.findViewById(R.id.zone_book_name);
            TextView bookScore = (TextView) child.findViewById(R.id.zone_book_score);
            Glide.with(getActivity()).load(NewDiscoveryRetrofit.IMAGE_URL + bookFavoritesBean.getCover()).into(bookBg);
            bookName.setText(bookFavoritesBean.getName());
            bookScore.setText(bookFavoritesBean.getDoubanScore() + "分/豆瓣");
            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(DensityUtil.dip2px(getActivity(), 15), DensityUtil.dip2px(getActivity(), 20), 0, DensityUtil.dip2px(getActivity(), 15));
            param.gravity = Gravity.TOP;
            child.setLayoutParams(param);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BookBorrowDetailActivity.class);
                    intent.putExtra("bookFavoriteId", bookFavoritesBean.getId());
                    startActivity(intent);
                }
            });
            zoneBookBorrowContainer.addView(child);

        }
    }

    private void initActivity() {
        //精彩活动
        if (activities.size() == 0 ){
            zoneWonderfulActivityArrow.setVisibility(View.GONE);
        }
        zoneWonderfulActivityContainer.removeAllViews();
        for (int i = 0; i < activities.size(); i++) {
            View child = View.inflate(getActivity(), R.layout.item_zone_activity, null);
            final NewDiscoveryBean.ResponseDataBean.ActivitiesBean activitiesBean = activities.get(i);
            ImageView activityBg = (ImageView) child.findViewById(R.id.item_zone_activity_bg);
            TextView activityTitle = (TextView) child.findViewById(R.id.item_zone_activity_title);
            TextView activityTime = (TextView) child.findViewById(R.id.item_zone_activity_time);
            Glide.with(getActivity()).load(NewDiscoveryRetrofit.IMAGE_URL + activitiesBean.getBanner()).into(activityBg);
            activityTitle.setText(activitiesBean.getTitle());
            activityTime.setText(activitiesBean.getTime());
            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(DensityUtil.dip2px(getActivity(), 15), DensityUtil.dip2px(getActivity(), 15), 0, DensityUtil.dip2px(getActivity(), 20));
            param.gravity = Gravity.TOP;
            child.setLayoutParams(param);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WonderfulDetailActivity.class);
                    intent.putExtra("activityId", activitiesBean.getId());
                    startActivity(intent);
                }
            });

            zoneWonderfulActivityContainer.addView(child);
        }
    }

    private void initbookbar() {
        adapter = new GalleryAdapter(getActivity(), bookbars);
        zoneGallery.setAdapter(adapter);
        zoneGallery.setOnItemSelectedListener(this);
        zoneGallery.setAnimationDuration(0);
        zoneGallery.setUnselectedAlpha((float) 0.3);
        zoneGallery.setOnItemClickListener(this);
    }

    private void location() {
        //高德定位
//        初始化定位
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
        //连续定位--两个小时定位一次
        mLocationOption.setInterval(1000 * 3600 * 2);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //启动定位
        mLocationClient.startLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE_LOCATION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {//有权限
                location();
            } else {
                //没有权限
                if (!PermissionUtil.shouldShowPermissions(getActivity(), permissions)) {//这个返回false 表示勾选了不再提示
                    Snackbar.make(zoneAddress, "请去设置界面设置权限",
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
                    Snackbar.make(zoneAddress, "请允许权限请求！",
                            Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    ActivityCompat
                                            .requestPermissions(getActivity(), mPermissionList,
                                                    REQUEST_PERMISSION_CODE_LOCATION);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectItem(position);  //当滑动时，事件响应，调用适配器中的这个方法。
        NewDiscoveryBean.ResponseDataBean.BookbarsBean bookbarsBean = bookbars.get(position);
        zoneAddress.setText(bookbarsBean.getName());
        zoneTag.setText(bookbarsBean.getTag());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewDiscoveryBean.ResponseDataBean.BookbarsBean bookbarsBean = bookbars.get(position);
        Intent bookbarintent = new Intent(getActivity(), ZoneDetailActivity.class);
        bookbarintent.putExtra("bookbarId", bookbarsBean.getId());
        startActivity(bookbarintent);
    }

    @OnClick({R.id.zone_wonderful_activity_arrow, R.id.zone_book_borrow, R.id.zone_recommend_friend, R.id.zone_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zone_wonderful_activity_arrow:
                Intent activityIntent = new Intent(getActivity(), MoreWonderfulActivity.class);
                startActivity(activityIntent);
                break;
            case R.id.zone_book_borrow:
                //图书借阅
                Intent bookIntent = new Intent(getActivity(), MoreBookBorrowActivity.class);
                startActivity(bookIntent);
                break;
            case R.id.zone_recommend_friend:
                Intent friendIntent = new Intent(getActivity(),MoreFriendActivity.class);
                startActivity(friendIntent);
                break;
            case R.id.zone_more:
                Intent intent = new Intent(getActivity(), MoreZoneActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onRefresh() {
        zoneRefresh.setRefreshing(true);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);

                Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
            }
        }
    }


    //声明定位回调监听器
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.d(TAG, "onLocationChanged: " + aMapLocation);
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                longitude = aMapLocation.getLongitude();
                latitude = aMapLocation.getLatitude();
                String city = aMapLocation.getCity();
                String address = aMapLocation.getAddress();
                android.util.Log.d(TAG, "onLocationChanged: 经度："+aMapLocation.getLongitude()+"维度："+aMapLocation.getLatitude());
//                Toast.makeText(getActivity(), "纬度：" + aMapLocation.getLatitude() + "   经度：" + aMapLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                SPUtils.putString(getActivity(), "location", longitude + "/" + latitude);
                initData();
                recordLoc(city, address);
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败 的原因，errInfo是错误信息，详见错误码表。
                Log.e(TAG, "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        } else {
            Log.d(TAG, "onLocationChanged: 定位失败");
        }
    }

    private void recordLoc(String city, String address) {
        //记录用户定位
        //SP获取bindbean
        String bindbeanStr = SPUtils.getString(getActivity(), "bindbean", "");
        Gson gson = new Gson();
        final UserDetailBean userbean = gson.fromJson(bindbeanStr, UserDetailBean.class);
        Call<ModifyBean> recordLocCall = NewDiscoveryRetrofit.getInstance(getActivity()).getApi().recordLocCall(userbean.getResponseData().getId(),
                longitude,
                latitude,
                city,
                address
        );
        recordLocCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1) {

                    } else {
//                        Toast.makeText(getActivity(), bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        android.util.Log.d(TAG, "onResponse: "+bean.getResponseMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {
                android.util.Log.d(TAG, "onFailure: "+t.toString());
            }
        });
    }
}
