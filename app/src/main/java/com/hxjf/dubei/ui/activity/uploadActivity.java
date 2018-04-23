package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.AddCollectBookBean;
import com.hxjf.dubei.bean.DoubanDetailBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.widget.LoadDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/12/27.
 */

public class uploadActivity extends BaseActivity {
    @BindView(R.id.zone_collect_book_name)
    TextView zoneCollectBookName;
    @BindView(R.id.zone_collect_book_back)
    ImageView zoneCollectBookBack;
    @BindView(R.id.upload_book_imageview)
    ImageView uploadBookImageview;
    @BindView(R.id.book_borrow_detail_ratingbar)
    RatingBar bookBorrowDetailRatingbar;
    @BindView(R.id.book_borrow_detail_score)
    TextView bookBorrowDetailScore;
    @BindView(R.id.book_borrow_detail_button)
    Button bookBorrowDetailButton;
    @BindView(R.id.book_borrow_detail_ll_button)
    LinearLayout bookBorrowDetailLlButton;
    @BindView(R.id.book_borrow_detail_author)
    TextView bookBorrowDetailAuthor;
    @BindView(R.id.book_borrow_detail_publisher)
    TextView bookBorrowDetailPublisher;
    private String isbn;
    private DoubanDetailBean.ResponseDataBean responseData;
    private Double longitude;
    private Double latitude;
    private String userId;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_upload;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        isbn = intent.getStringExtra("codedContent");
        String loc = SPUtils.getString(this, "location", "114.07/22.62");
        String[] split = loc.split("/");
        longitude = Double.valueOf(split[0]);
        latitude = Double.valueOf(split[1]);
        //SP获取bindbean
        String bindbeanStr = SPUtils.getString(this, "bindbean", "");
        Gson gson = new Gson();
        UserDetailBean userbean = gson.fromJson(bindbeanStr, UserDetailBean.class);
        if (userbean != null) {
            userId = userbean.getResponseData().getId();
        }
        Call<DoubanDetailBean> doubanDetailCall = NewDiscoveryRetrofit.getInstance(this).getApi().doubanDetailCall(isbn);
        doubanDetailCall.enqueue(new Callback<DoubanDetailBean>() {
            @Override
            public void onResponse(Call<DoubanDetailBean> call, Response<DoubanDetailBean> response) {
                DoubanDetailBean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null) {
                        responseData = bean.getResponseData();
                        initData();
                    } else {
                        Toast.makeText(uploadActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DoubanDetailBean> call, Throwable t) {

            }
        });


    }

    private void initData() {
        zoneCollectBookName.setText(responseData.getName());
        String doubanScore = responseData.getDoubanScore();
        bookBorrowDetailScore.setText(doubanScore + "分/豆瓣");
        bookBorrowDetailRatingbar.setRating((float) (Double.valueOf(doubanScore) / 2.0));
        bookBorrowDetailAuthor.setText(responseData.getAuthor());
        bookBorrowDetailPublisher.setText(responseData.getPublisher());
        Glide.with(this).load(responseData.getCover()).into(uploadBookImageview);

    }


    @OnClick({R.id.zone_collect_book_back, R.id.book_borrow_detail_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zone_collect_book_back:
                finish();
                break;
            case R.id.book_borrow_detail_button:
                upload();
                break;
        }
    }

    private void upload() {
        LoadDialog.show(this,"正在上传...");
        Call<AddCollectBookBean> addCollectBookCall = NewDiscoveryRetrofit.getInstance(this).getApi().addCollectBookCall(userId, isbn, "", longitude, latitude);
        addCollectBookCall.enqueue(new Callback<AddCollectBookBean>() {
            @Override
            public void onResponse(Call<AddCollectBookBean> call, Response<AddCollectBookBean> response) {
                AddCollectBookBean bean = response.body();
                if (bean != null){
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null){
                        Intent intent = new Intent(uploadActivity.this ,myCollectBookActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        LoadDialog.dismiss(uploadActivity.this);
                    }else{
                        Toast.makeText(uploadActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddCollectBookBean> call, Throwable t) {

            }
        });
    }


}
