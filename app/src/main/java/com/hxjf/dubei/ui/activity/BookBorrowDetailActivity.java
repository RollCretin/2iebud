package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.FavoriteBookDetailBean;
import com.hxjf.dubei.network.ImLoginHelper;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.StatusBarUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/9/21.
 */

public class BookBorrowDetailActivity extends BaseActivity {
    @BindView(R.id.book_borrow_detail_back)
    ImageView bookBorrowDetailBack;
    @BindView(R.id.book_borrow_detail_image)
    ImageView bookBorrowDetailImage;
    @BindView(R.id.book_borrow_detail_name)
    TextView bookBorrowDetailName;
    @BindView(R.id.book_borrow_detail_author)
    TextView bookBorrowDetailAuthor;
    @BindView(R.id.book_borrow_detail_ratingbar)
    RatingBar bookBorrowDetailRatingbar;
    @BindView(R.id.book_borrow_detail_score)
    TextView bookBorrowDetailScore;
    @BindView(R.id.book_borrow_detail_pratorit)
    CircleImageView bookBorrowDetailPratorit;
    @BindView(R.id.book_borrow_detail_who)
    TextView bookBorrowDetailWho;
    @BindView(R.id.book_borrow_detail_location)
    TextView bookBorrowDetailLocation;
    @BindView(R.id.book_borrow_detail_address)
    TextView bookBorrowDetailAddress;
    @BindView(R.id.book_borrow_detail_ll_address)
    LinearLayout bookBorrowDetailLlAddress;
    @BindView(R.id.book_borrow_detail_collect_arrow)
    LinearLayout bookBorrowDetailCollectArrow;
    @BindView(R.id.book_borrow_detail_collect_container)
    LinearLayout bookBorrowDetailCollectContainer;
    @BindView(R.id.book_borrow_detail_button)
    Button bookBorrowDetailButton;
    @BindView(R.id.book_borrow_detail_ll_button)
    LinearLayout bookBorrowDetailLlButton;
    @BindView(R.id.book_borrow_detail_number)
    TextView bookBorrowDetailNumber;
    @BindView(R.id.book_borrow_detail_bookpocket)
    ImageView bookBorrowDetailBookpocket;
    @BindView(R.id.my_collect_book_detail_rule)
    TextView myCollectBookDetailRule;
    @BindView(R.id.book_borrow_detail_plain)
    TextView bookBorrowDetailPlain;
    @BindView(R.id.book_borrow_detail_add_bookpocket)
    Button bookBorrowDetailAddBookpocket;
    private Double longitude;
    private Double latitude;
    private FavoriteBookDetailBean.ResponseDataBean responseData;
    private Double distance = 0.0;
    private StringBuffer sb = new StringBuffer();
    private double distanceKM;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_book_borrow_detail;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //设置状态栏的颜色
        StatusBarUtils.setWindowStatusBarColor(this, R.color.note_bg);
        String loc = SPUtils.getString(this, "location", "114.07/22.62");
        String[] split = loc.split("/");
        longitude = Double.valueOf(split[0]);
        latitude = Double.valueOf(split[1]);

        Intent intent = getIntent();
        String bookId = intent.getStringExtra("bookFavoriteId");
        Log.d("id...", "init: " + bookId);
        Call<FavoriteBookDetailBean> favoriteBookDetailCall = NewDiscoveryRetrofit.getInstance(this).getApi().favoriteBookDetailCall(longitude, latitude, bookId);
        favoriteBookDetailCall.enqueue(new Callback<FavoriteBookDetailBean>() {
            @Override
            public void onResponse(Call<FavoriteBookDetailBean> call, Response<FavoriteBookDetailBean> response) {
                FavoriteBookDetailBean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null) {
                        responseData = bean.getResponseData();
                        initData();

                    } else {
                        Toast.makeText(BookBorrowDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteBookDetailBean> call, Throwable t) {
                Log.d("id...", "onFailure: " + t.toString());
            }
        });

    }

    private void initData() {
        GlideLoadUtils.getInstance().glideLoad(this, NewDiscoveryRetrofit.IMAGE_URL + responseData.getCover(), bookBorrowDetailImage, 0);
        GlideLoadUtils.getInstance().glideLoad(this, NewDiscoveryRetrofit.IMAGE_URL + responseData.getOwnerHeadPhoto(), bookBorrowDetailPratorit, 0);
        bookBorrowDetailName.setText(responseData.getName());
        bookBorrowDetailAuthor.setText(responseData.getAuthor());
        Float doubanScore = Float.valueOf(responseData.getDoubanScore());
        bookBorrowDetailRatingbar.setRating((float) (doubanScore / 2.0));
        bookBorrowDetailScore.setText("豆瓣/" + responseData.getDoubanScore() + "分");
        bookBorrowDetailWho.setText(responseData.getOwnerName());
//        if (!"".equals(responseData.getDistance()) && responseData.getDistance() != null) {
//            distance = Double.valueOf(responseData.getDistance());
//        }
//        if (distance >= 1000) {
//            distanceKM = distance / 1000.0;
//            DecimalFormat format = new DecimalFormat("######0.00");
//            bookBorrowDetailLocation.setText("距离您" + format.format(distanceKM) + " KM");
//        } else {
//            bookBorrowDetailLocation.setText("距离您" + (new Double(distance)).intValue() + " M");
//        }
        bookBorrowDetailLocation.setText(responseData.getCityDistrict());

        bookBorrowDetailAddress.setText(responseData.getAddress());
        if (responseData.getOwnerType() != null && !"".equals(responseData.getOwnerType()) && Integer.valueOf(responseData.getOwnerType()) == 3) {
            //0:主人是用户；1-主人是书吧
            bookBorrowDetailButton.setText("向TA借阅");
            bookBorrowDetailLlAddress.setVisibility(View.GONE);
        } else if (responseData.getOwnerType() != null && !"".equals(responseData.getOwnerType()) && Integer.valueOf(responseData.getOwnerType()) == 1) {
            bookBorrowDetailButton.setText("带我去");
        }

        bookBorrowDetailNumber.setText("TA的藏书（" + responseData.getOwnerBookFavoriteCount() + "本）");
        List<FavoriteBookDetailBean.ResponseDataBean.OwnerBookFavoritesBean> ownerBookFavorites = responseData.getOwnerBookFavorites();
        //个人藏书
        for (int i = 0; i < ownerBookFavorites.size(); i++) {
            View child = View.inflate(this, R.layout.item_zone_book, null);
            final FavoriteBookDetailBean.ResponseDataBean.OwnerBookFavoritesBean bookFavoritesBean = ownerBookFavorites.get(i);
            ImageView bookBg = (ImageView) child.findViewById(R.id.zone_book_image);
            TextView bookName = (TextView) child.findViewById(R.id.zone_book_name);
            TextView bookScore = (TextView) child.findViewById(R.id.zone_book_score);
            GlideLoadUtils.getInstance().glideLoad(this, NewDiscoveryRetrofit.IMAGE_URL + bookFavoritesBean.getCover(), bookBg, 0);
            bookName.setText(bookFavoritesBean.getName());
            bookScore.setText(bookFavoritesBean.getDoubanScore() + "分/豆瓣");
            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(DensityUtil.dip2px(this, 15), DensityUtil.dip2px(this, 20), 0, DensityUtil.dip2px(this, 15));
            param.gravity = Gravity.TOP;
            child.setLayoutParams(param);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookBorrowDetailActivity.this, BookBorrowDetailActivity.class);
                    intent.putExtra("bookFavoriteId", bookFavoritesBean.getId());
                    startActivity(intent);
                    finish();
                }
            });
            bookBorrowDetailCollectContainer.addView(child);

        }

    }


    @OnClick({R.id.book_borrow_detail_back, R.id.book_borrow_detail_button, R.id.book_borrow_detail_collect_arrow, R.id.book_borrow_detail_ll_address, R.id.book_borrow_detail_pratorit,R.id.book_borrow_detail_bookpocket,R.id.my_collect_book_detail_rule,R.id.book_borrow_detail_add_bookpocket})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_borrow_detail_back:
                finish();
                break;
            case R.id.book_borrow_detail_button:
                if (responseData == null) {
                    return;
                }
                if (responseData.getOwnerType() != null && !"".equals(responseData.getOwnerType()) && Integer.valueOf(responseData.getOwnerType()) == 3) {
                    //0:主人是用户；1-主人是书吧
                    YWIMKit imKit = ImLoginHelper.getInstance().getIMKit();
                    Intent intent = imKit.getChattingActivityIntent(responseData.getOwnerId(), ImLoginHelper.getInstance().getAPP_KEY());
                    startActivity(intent);
                } else if (responseData.getOwnerType() != null && !"".equals(responseData.getOwnerType()) && Integer.valueOf(responseData.getOwnerType()) == 1) {
                    if (responseData == null) {
                        return;
                    }
                    Intent navigationintent = new Intent(this, navigationActivity.class);
                    navigationintent.putExtra("ownerName", responseData.getOwnerName());
                    List<Double> location = responseData.getLocation();
                    StringBuffer destsb = new StringBuffer();
                    if (location != null) {
                        for (int i = 0; i < location.size(); i++) {
                            destsb.append(location.get(i) + ",");
                        }
                        sb = destsb.deleteCharAt(destsb.length() - 1);
                    }

                    navigationintent.putExtra("dest", sb.toString());
                    navigationintent.putExtra("address", responseData.getAddress());
                    startActivity(navigationintent);
                }
                break;
            case R.id.book_borrow_detail_collect_arrow:
                Intent intent = new Intent(this, ZoneCollectBookActivity.class);
                intent.putExtra("ownerId", responseData.getOwnerId());
                intent.putExtra("ownerName", responseData.getOwnerName());
                startActivity(intent);
                break;
            case R.id.book_borrow_detail_ll_address:
                //跳转到导航
                if (responseData == null) {
                    return;
                }

                Intent navigationintent = new Intent(this, navigationActivity.class);
                navigationintent.putExtra("ownerName", responseData.getOwnerName());
                List<Double> location = responseData.getLocation();
                StringBuffer destsb = new StringBuffer();
                if (location != null) {
                    for (int i = 0; i < location.size(); i++) {
                        destsb.append(location.get(i) + ",");
                    }
                    sb = destsb.deleteCharAt(destsb.length() - 1);
                }

                navigationintent.putExtra("dest", sb.toString());
                navigationintent.putExtra("address", responseData.getAddress());
                startActivity(navigationintent);
                break;
            case R.id.book_borrow_detail_pratorit:
                if (responseData.getOwnerType() != null && !"".equals(responseData.getOwnerType()) && Integer.valueOf(responseData.getOwnerType()) == 0) {
                    //0:主人是用户；1-主人是书吧
                    Intent personIntent = new Intent(this, PersonDetailActivity.class);
                    personIntent.putExtra("userId", responseData.getOwnerId());
                    startActivity(personIntent);
                } else if (responseData.getOwnerType() != null && !"".equals(responseData.getOwnerType()) && Integer.valueOf(responseData.getOwnerType()) == 1) {
                    Intent zoneIntent = new Intent(this, ZoneDetailActivity.class);
                    zoneIntent.putExtra("bookbarId", responseData.getOwnerId());
                    startActivity(zoneIntent);
                }
                break;
            case R.id.book_borrow_detail_bookpocket:
                Intent bookpocketIntent = new Intent(BookBorrowDetailActivity.this,BookPocketActivity.class);
                startActivity(bookpocketIntent);
                break;
            case R.id.my_collect_book_detail_rule:
                break;
            case R.id.book_borrow_detail_add_bookpocket:
                break;
        }
    }

}
