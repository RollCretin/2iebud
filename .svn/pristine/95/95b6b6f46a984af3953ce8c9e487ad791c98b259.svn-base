package com.hxjf.dubei.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.base.DuBeiApplication;
import com.hxjf.dubei.bean.BookClassifyBean;
import com.hxjf.dubei.bean.BookbarDetailBean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.util.ScreenSizeUtil;
import com.hxjf.dubei.util.StatusBarUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/9/15.
 */

public class ZoneDetailActivity extends BaseActivity {
    private static final int BOOK_PROFILE = 1;
    @BindView(R.id.zone_detaol_back)
    ImageView zoneDetaolBack;
    @BindView(R.id.zone_datail_name)
    TextView zoneDatailName;
    @BindView(R.id.zone_detail_tag)
    TextView zoneDetailTag;
    @BindView(R.id.zone_detail_convenientBanner)
    ConvenientBanner zoneDetailConvenientBanner;
    @BindView(R.id.zone_detail_opentime)
    TextView zoneDetailOpentime;
    @BindView(R.id.zone_detail_number)
    TextView zoneDetailNumber;
    @BindView(R.id.zone_detail_ll_opentime)
    LinearLayout zoneDetailLlOpentime;
    @BindView(R.id.zone_detail_address)
    TextView zoneDetailAddress;
    @BindView(R.id.zone_detail_information)
    RelativeLayout zoneDetailInformation;
    @BindView(R.id.zone_detail_first_page)
    LinearLayout zoneDetailFirstPage;
    @BindView(R.id.zone_detail_ll_place_information)
    LinearLayout zoneDetailLlPlaceInformation;
    @BindView(R.id.zone_detail_place_information)
    TextView zoneDetailPlaceInformation;
    @BindView(R.id.zone_detail_rentable)
    TextView zoneDetailRentable;
    @BindView(R.id.zone_detail_ll_applicable_scene)
    LinearLayout zoneDetailLlApplicableScene;
    @BindView(R.id.zone_detail_applicable_scene)
    TextView zoneDetailApplicableScene;
    @BindView(R.id.zone_detail_ll_container)
    LinearLayout zoneDetailLlContainer;
    @BindView(R.id.zone_detail_1)
    TextView zoneDetail1;
    @BindView(R.id.zone_detail_2)
    TextView zoneDetail2;
    @BindView(R.id.zone_detail_3)
    TextView zoneDetail3;
    @BindView(R.id.zone_detail_4)
    TextView zoneDetail4;
    @BindView(R.id.zone_detail_container_1)
    LinearLayout zoneDetailContainer1;
    @BindView(R.id.zone_detail_5)
    TextView zoneDetail5;
    @BindView(R.id.zone_detail_6)
    TextView zoneDetail6;
    @BindView(R.id.zone_detail_container_2)
    LinearLayout zoneDetailContainer2;
    @BindView(R.id.zone_detail_tv_information)
    WebView zoneDetailTvInformation;
    @BindView(R.id.book_detail_book_profile_more)
    TextView bookDetailBookProfileMore;
    @BindView(R.id.zone_detail_zone_service_arrow)
    LinearLayout zoneDetailZoneServiceArrow;
    @BindView(R.id.zone_detail_commodity_arrow)
    LinearLayout zoneDetailCommodityArrow;
    @BindView(R.id.zone_detail_commodity_container)
    LinearLayout zoneDetailCommodityContainer;
    @BindView(R.id.zone_detail_wonderful_activity_arrow)
    LinearLayout zoneDetailWonderfulActivityArrow;
    @BindView(R.id.zone_detail_wonderful_activity_container)
    LinearLayout zoneDetailWonderfulActivityContainer;
    @BindView(R.id.zone_detail_book_account)
    TextView zoneDetailBookAccount;
    @BindView(R.id.zone_detail_book_arrow)
    LinearLayout zoneDetailBookArrow;
    @BindView(R.id.zone_detail_book_container)
    LinearLayout zoneDetailBookContainer;
    @BindView(R.id.zone_detail_zone_ll_service_container)
    LinearLayout zoneDetailZoneLlServiceContainer;


    private ArrayList<String> localImages;
    private BookbarDetailBean.ResponseDataBean responseDataBean;
    private Boolean profile_isShrink = true;
    private List<BookClassifyBean.ResponseDataBean.MenuInfoBean> sceneInfo;
    private List<BookClassifyBean.ResponseDataBean.MenuInfoBean> facilityInfo;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_zone_detail;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String bookbarId = intent.getStringExtra("bookbarId");
        sceneInfo = new ArrayList<>();
        facilityInfo = new ArrayList<>();
        //设置状态栏的颜色
        StatusBarUtils.setWindowStatusBarColor(ZoneDetailActivity.this, R.color.note_bg);
        Call<BookbarDetailBean> bookbarDetailCall = NewDiscoveryRetrofit.getInstance(this).getApi().bookbarDetailCall(bookbarId);
        bookbarDetailCall.enqueue(new Callback<BookbarDetailBean>() {
            @Override
            public void onResponse(Call<BookbarDetailBean> call, Response<BookbarDetailBean> response) {
                BookbarDetailBean bookbarDetailBean = response.body();
                if (bookbarDetailBean != null) {
                    if (bookbarDetailBean.getResponseCode() == 1 && bookbarDetailBean.getResponseData() != null) {
                        responseDataBean = bookbarDetailBean.getResponseData();
                        initData();
                    } else {
                        Toast.makeText(ZoneDetailActivity.this, bookbarDetailBean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookbarDetailBean> call, Throwable t) {

            }
        });

        //适用场景的类型
        Call<BookClassifyBean> scene = ReaderRetroift.getInstance(this).getApi().menuTypeClassifyCall("scene");
        scene.enqueue(new Callback<BookClassifyBean>() {
            @Override
            public void onResponse(Call<BookClassifyBean> call, Response<BookClassifyBean> response) {
                BookClassifyBean classifyBean = response.body();
                if (classifyBean != null && classifyBean.getResponseCode() == 1) {
                    BookClassifyBean.ResponseDataBean responseData = classifyBean.getResponseData();
                    sceneInfo = responseData.getMenuInfo();
                }
            }

            @Override
            public void onFailure(Call<BookClassifyBean> call, Throwable throwable) {

            }
        });
        //基础设施类型
        Call<BookClassifyBean> facility = ReaderRetroift.getInstance(this).getApi().menuTypeClassifyCall("facility");
        facility.enqueue(new Callback<BookClassifyBean>() {
            @Override
            public void onResponse(Call<BookClassifyBean> call, Response<BookClassifyBean> response) {
                BookClassifyBean classifyBean = response.body();
                if (classifyBean != null && classifyBean.getResponseCode() == 1) {
                    BookClassifyBean.ResponseDataBean responseData = classifyBean.getResponseData();
                    facilityInfo = responseData.getMenuInfo();
                }
            }

            @Override
            public void onFailure(Call<BookClassifyBean> call, Throwable throwable) {

            }
        });
    }

    private void initData() {
        localImages = new ArrayList<>();
        List<String> banners = responseDataBean.getBanners();
        for (String banner :
                banners) {
            localImages.add(NewDiscoveryRetrofit.IMAGE_URL + banner);
        }
        //设置zoneDetailFirstPage的高度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenSizeUtil.getScreenHeight(ZoneDetailActivity.this) - ScreenSizeUtil.getStatusBarHeight());
        zoneDetailFirstPage.setLayoutParams(params);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.BOTTOM;
        zoneDetailInformation.setLayoutParams(params1);
        zoneDetailConvenientBanner.setCanLoop(true);
        zoneDetailConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_LEFT);
        zoneDetailConvenientBanner.setCanLoop(false);
        //设置翻页的效果，不需要翻页效果可用不设
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        convenientBanner.setManualPageable(false);//设置不能手动影响
        zoneDetailBookAccount.setText("空间藏书(" + responseDataBean.getBookFavoriteCount() + "本)");
        zoneDatailName.setText(responseDataBean.getName());
        zoneDetailTag.setText(responseDataBean.getTag());
        zoneDetailOpentime.setText(responseDataBean.getOpenTime());
        zoneDetailAddress.setText(responseDataBean.getAddress());
        zoneDetailTvInformation.setWebViewClient(new WebViewClient());
        zoneDetailTvInformation.setHorizontalScrollBarEnabled(false);
        zoneDetailTvInformation.setVerticalScrollBarEnabled(false);
        zoneDetailTvInformation.getSettings().setDefaultTextEncodingName("utf-8");
        LinearLayout.LayoutParams webparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(ZoneDetailActivity.this, 220));
        webparam.setMargins(DensityUtil.dip2px(ZoneDetailActivity.this, 15), 0, DensityUtil.dip2px(ZoneDetailActivity.this, 15), 0);
        zoneDetailTvInformation.setLayoutParams(webparam);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            zoneDetailTvInformation.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            zoneDetailTvInformation.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        zoneDetailTvInformation.loadData(getHtmlData(responseDataBean.getSummary()), "text/html; charset=utf-8", "utf-8");
        zoneDetailNumber.setText(responseDataBean.getTelephone());
        //精彩活动列表
        if (responseDataBean.getActivities().size() == 0) {
            zoneDetailWonderfulActivityArrow.setVisibility(View.GONE);
        } else {
            zoneDetailWonderfulActivityArrow.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < responseDataBean.getActivities().size(); i++) {
            View child = View.inflate(ZoneDetailActivity.this, R.layout.item_zone_activity, null);
            final BookbarDetailBean.ResponseDataBean.ActivitiesBean activitiesBean = responseDataBean.getActivities().get(i);
            ImageView activityBg = (ImageView) child.findViewById(R.id.item_zone_activity_bg);
            TextView activityTitle = (TextView) child.findViewById(R.id.item_zone_activity_title);
            TextView activityTime = (TextView) child.findViewById(R.id.item_zone_activity_time);
            GlideLoadUtils.getInstance().glideLoad(this, NewDiscoveryRetrofit.IMAGE_URL + activitiesBean.getBanner(), activityBg, 0);
            activityTitle.setText(activitiesBean.getTitle());
            activityTime.setText(activitiesBean.getTime());
            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(DensityUtil.dip2px(ZoneDetailActivity.this, 15), DensityUtil.dip2px(ZoneDetailActivity.this, 15), 0, DensityUtil.dip2px(ZoneDetailActivity.this, 20));

            child.setLayoutParams(param);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ZoneDetailActivity.this, WonderfulDetailActivity.class);
                    intent.putExtra("activityId", activitiesBean.getId());
                    startActivity(intent);
                }
            });

            zoneDetailWonderfulActivityContainer.addView(child);
        }

        //图书借阅
        if (responseDataBean.getBookFavorites().size() == 0) {
            zoneDetailBookArrow.setVisibility(View.GONE);
        } else {
            zoneDetailBookArrow.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < responseDataBean.getBookFavorites().size(); i++) {
            View child = View.inflate(ZoneDetailActivity.this, R.layout.item_zone_book, null);
            final BookbarDetailBean.ResponseDataBean.BookFavoritesBean bookFavoritesBean = responseDataBean.getBookFavorites().get(i);
            ImageView bookBg = (ImageView) child.findViewById(R.id.zone_book_image);
            TextView bookName = (TextView) child.findViewById(R.id.zone_book_name);
            TextView bookScore = (TextView) child.findViewById(R.id.zone_book_score);
            GlideLoadUtils.getInstance().glideLoad(ZoneDetailActivity.this, NewDiscoveryRetrofit.IMAGE_URL + bookFavoritesBean.getCover(), bookBg, 0);
            bookScore.setText(bookFavoritesBean.getDoubanScore() + "分/豆瓣");
            bookName.setText(bookFavoritesBean.getName());
            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            param.setMargins(DensityUtil.dip2px(ZoneDetailActivity.this, 15), DensityUtil.dip2px(ZoneDetailActivity.this, 20), 0, DensityUtil.dip2px(ZoneDetailActivity.this, 15));
            param.gravity = Gravity.TOP;
            child.setLayoutParams(param);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ZoneDetailActivity.this, BookBorrowDetailActivity.class);
                    intent.putExtra("bookFavoriteId", bookFavoritesBean.getId());
                    startActivity(intent);
                }
            });
            zoneDetailBookContainer.addView(child);
        }
        //场地信息
        zoneDetailPlaceInformation.setText(responseDataBean.getAcreage() + " 平米 | 最多容纳 " + responseDataBean.getSeatCapacity() + " 人");
        //是否可租用
        int rent = responseDataBean.getRent();
        if (rent == 1) {
            zoneDetailRentable.setVisibility(View.VISIBLE);
        } else {
            zoneDetailRentable.setVisibility(View.GONE);
        }
        //适用场景
        StringBuffer sceneSb = new StringBuffer();
        List<Integer> sceneList = responseDataBean.getScene();
        if (sceneList == null || sceneList.size() == 0) {
            zoneDetailApplicableScene.setVisibility(View.GONE);
            zoneDetailLlApplicableScene.setVisibility(View.GONE);
        } else {
            zoneDetailApplicableScene.setVisibility(View.VISIBLE);
            zoneDetailLlApplicableScene.setVisibility(View.VISIBLE);
            for (int i = 0; i < sceneList.size(); i++) {
                Integer integer = sceneList.get(i);
                for (int j = 0; j < sceneInfo.size(); j++) {
                    Integer integer1 = Integer.valueOf(sceneInfo.get(j).getText());
                    if (integer == integer1) {
                        String value = sceneInfo.get(j).getValue();
                        sceneSb.append(value + " | ");
                    }
                }
            }
            String substring = sceneSb.toString().substring(0, sceneSb.toString().length() - 2);
            zoneDetailApplicableScene.setText(substring);
        }
        //基础设施
        int[] img = new int[]{R.mipmap.zone_wifi, R.mipmap.zone_kongtiao, R.mipmap.zone_open, R.mipmap.zone_baoxiang, R.mipmap.zone_touying, R.mipmap.zone_tingche};
        String[] zoneStr = new String[]{"WI-FI", "空调", "开放空间", "包厢", "投影仪", "免费停车场"};
        TextView[] tvs = new TextView[]{zoneDetail1, zoneDetail2, zoneDetail3, zoneDetail4, zoneDetail5, zoneDetail6};
        List<Integer> facility = responseDataBean.getFacility();
        if (facility != null) {

            int j = facility.size();
            if (j > 0) {
                if (j < 5) {
                    zoneDetailContainer1.setVisibility(View.VISIBLE);
                } else {
                    zoneDetailContainer1.setVisibility(View.VISIBLE);
                    zoneDetailContainer2.setVisibility(View.VISIBLE);
                }
            } else {
                zoneDetailLlContainer.setVisibility(View.GONE);
            }
            for (int i = 0; i < j; i++) {
                Integer integer = facility.get(i);
                tvs[i].setVisibility(View.VISIBLE);
                tvs[i].setText(zoneStr[integer - 1]);
                Drawable drawable = getResources().getDrawable(img[integer - 1]);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvs[i].setCompoundDrawables(null, drawable, null, null);
            }
        } else {
            zoneDetailLlContainer.setVisibility(View.GONE);
        }


        //空间服务
        List<BookbarDetailBean.ResponseDataBean.ServeListBean> serveList = responseDataBean.getServeList();
        if (serveList != null && serveList.size() != 0) {
            for (int i = 0; i < serveList.size(); i++) {
                BookbarDetailBean.ResponseDataBean.ServeListBean serveListBean = serveList.get(i);
                View child = View.inflate(ZoneDetailActivity.this, R.layout.item_zone_service, null);
                ImageView serviceBg = (ImageView) child.findViewById(R.id.item_zone_service_image);
                TextView serviceName = (TextView) child.findViewById(R.id.item_zone_service_name);
                GlideLoadUtils.getInstance().glideLoad(ZoneDetailActivity.this, NewDiscoveryRetrofit.IMAGE_URL + serveListBean.getNormalPath(), serviceBg, 0);
                serviceName.setText(serveListBean.getName());
                //item设置外边距
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                param.setMargins(DensityUtil.dip2px(ZoneDetailActivity.this, 15), DensityUtil.dip2px(ZoneDetailActivity.this, 20), 0, DensityUtil.dip2px(ZoneDetailActivity.this, 15));
                param.gravity = Gravity.TOP;
                child.setLayoutParams(param);
                zoneDetailZoneLlServiceContainer.addView(child);
            }
        } else {
            zoneDetailZoneLlServiceContainer.setVisibility(View.GONE);
            zoneDetailZoneServiceArrow.setVisibility(View.GONE);
        }
        //文创商品
        List<BookbarDetailBean.ResponseDataBean.ProductListBean> productList = responseDataBean.getProductList();
        if (productList != null && productList.size() != 0) {
            for (int i = 0; i < productList.size(); i++) {
                BookbarDetailBean.ResponseDataBean.ProductListBean productListBean = productList.get(i);
                View child = View.inflate(ZoneDetailActivity.this, R.layout.item_zone_service, null);
                ImageView commodityBg = (ImageView) child.findViewById(R.id.item_zone_service_image);
                TextView commodityName = (TextView) child.findViewById(R.id.item_zone_service_name);
                TextView commodityPrice = (TextView) child.findViewById(R.id.item_zone_service_price);
                commodityPrice.setVisibility(View.VISIBLE);
                GlideLoadUtils.getInstance().glideLoad(ZoneDetailActivity.this, NewDiscoveryRetrofit.IMAGE_URL + productListBean.getNormalPath(), commodityBg, 0);
                commodityName.setText(productListBean.getName());
                DecimalFormat df = new DecimalFormat("######0.00");
                String formatMoney = df.format(productListBean.getPrice());
                commodityPrice.setText("￥ " + formatMoney);
                //item设置外边距
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                param.setMargins(DensityUtil.dip2px(ZoneDetailActivity.this, 15), DensityUtil.dip2px(ZoneDetailActivity.this, 20), 0, DensityUtil.dip2px(ZoneDetailActivity.this, 15));
                param.gravity = Gravity.TOP;
                child.setLayoutParams(param);
                zoneDetailCommodityContainer.addView(child);
            }
        } else {
            zoneDetailCommodityContainer.setVisibility(View.GONE);
            zoneDetailCommodityArrow.setVisibility(View.GONE);
        }
    }

    //为html内容添加头
    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @OnClick({R.id.zone_detail_wonderful_activity_arrow, R.id.zone_detail_book_arrow, R.id.zone_detaol_back, R.id.book_detail_book_profile_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zone_detail_wonderful_activity_arrow:
                Intent activityintent = new Intent(this, MoreWonderfulActivity.class);
                startActivity(activityintent);
                break;
            case R.id.zone_detail_book_arrow:
                Intent intent = new Intent(this, ZoneCollectBookActivity.class);
                intent.putExtra("ownerId", responseDataBean.getId());
                intent.putExtra("ownerName", responseDataBean.getName());
                startActivity(intent);
                break;
            case R.id.zone_detaol_back:
                finish();
                break;
            case R.id.book_detail_book_profile_more:
//                OpenOrShrink(zoneDetailTvInformation, bookDetailBookProfileMore, profile_isShrink, BOOK_PROFILE);
                OpenOrShrink(bookDetailBookProfileMore);
                break;
        }
    }

    private void OpenOrShrink(TextView bookDetailBookProfileMore) {
        if (profile_isShrink) {
            //展开
            profile_isShrink = false;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(DensityUtil.dip2px(ZoneDetailActivity.this, 15), 0, DensityUtil.dip2px(ZoneDetailActivity.this, 15), 0);
            zoneDetailTvInformation.setLayoutParams(params);
            bookDetailBookProfileMore.setText("收起");
        } else {
            profile_isShrink = true;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(DuBeiApplication.getContext(), 220));
            params.setMargins(DensityUtil.dip2px(ZoneDetailActivity.this, 15), 0, DensityUtil.dip2px(ZoneDetailActivity.this, 15), 0);
            zoneDetailTvInformation.setLayoutParams(params);
            bookDetailBookProfileMore.setText("查看更多");
        }
    }

    private void OpenOrShrink(TextView showView, TextView loadView, Boolean isShrink, int sign) {
        if (isShrink) {
            //展开
            isShrink = false;
            showView.setEllipsize(null); // 展开
            showView.setSingleLine(isShrink);
            loadView.setText("收起");
        } else {
            isShrink = true;
            showView.setEllipsize(TextUtils.TruncateAt.END);
            showView.setMaxLines(4);
            loadView.setText("查看更多");
        }
        switch (sign) {
            case BOOK_PROFILE:
                profile_isShrink = isShrink;
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class LocalImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            View view = View.inflate(ZoneDetailActivity.this, R.layout.view_zone_detail_banner, null);
            imageView = (ImageView) view.findViewById(R.id.zone_detail_banner_image);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(ZoneDetailActivity.this).load(data).into(imageView);
        }
    }
}
