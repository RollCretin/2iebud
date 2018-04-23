package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BookDetailBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.widget.DrawableCenterTextView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/9/22.
 */

public class SelectBookActivity extends BaseActivity {


    @BindView(R.id.select_book_back)
    ImageView selectBookBack;
    @BindView(R.id.select_book_bookimage)
    ImageView selectBookBookimage;
    @BindView(R.id.select_book_bookname)
    TextView selectBookBookname;
    @BindView(R.id.select_book_bookauthor)
    TextView selectBookBookauthor;
    @BindView(R.id.select_book_number)
    TextView selectBookNumber;
    @BindView(R.id.book_detail_textnumber)
    DrawableCenterTextView bookDetailTextnumber;
    @BindView(R.id.select_book_profile)
    TextView selectBookProfile;
    @BindView(R.id.select_book_author_profile)
    TextView selectBookAuthorProfile;
    @BindView(R.id.select_book_catalog_profile)
    TextView selectBookCatalogProfile;
    @BindView(R.id.select_book_scrollview)
    ScrollView selectBookScrollview;
    @BindView(R.id.challenge_select)
    Button challengeSelect;
    @BindView(R.id.ll_challenge_select)
    LinearLayout llChallengeSelect;
    @BindView(R.id.select_book_ll_catalog)
    LinearLayout selectBookLlCatalog;

    private int height = 0;
    private SlidrInterface slidrInterface;
    private String bookid;
    private BookDetailBean.ResponseDataBean responseData;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_select_book;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bookid = intent.getStringExtra("bookid");
        challengeSelect.setClickable(false);
        int primary = getResources().getColor(R.color.primary);
        int secondary = getResources().getColor(R.color.primary);
        SlidrConfig.Builder mBuilder = new SlidrConfig.Builder().primaryColor(primary)
                .secondaryColor(secondary)
                .scrimColor(Color.BLACK)
                .position(SlidrPosition.TOP)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(5f)
                .distanceThreshold(.35f);

        SlidrConfig mSlidrConfig = mBuilder.build();
        slidrInterface = Slidr.attach(this, mSlidrConfig);

        selectBookScrollview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        height++;
                        break;
                }

                if (event.getAction() == MotionEvent.ACTION_UP && height > 0) {

                    height = 0;
                    View view = ((ScrollView) v).getChildAt(0);

                    if (v.getScrollY() == 0) {
                        slidrInterface.unlock();
                    } else {
                        slidrInterface.lock();
                    }
                }
                return false;
            }
        });

        //请求获取书籍数据
        Call<BookDetailBean> bookDetailCall = ReaderRetroift.getInstance(this).getApi().BookDetailCall(bookid);
        bookDetailCall.enqueue(new Callback<BookDetailBean>() {
            @Override
            public void onResponse(Call<BookDetailBean> call, Response<BookDetailBean> response) {
                BookDetailBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    responseData = bean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BookDetailBean> call, Throwable t) {
            }
        });
    }

    private void initData() {
        challengeSelect.setClickable(true);
        Glide.with(this).load(ReaderRetroift.IMAGE_URL + responseData.getCover()).into(selectBookBookimage);//封面
        selectBookBookname.setText(responseData.getName());//书名
        selectBookBookauthor.setText(responseData.getAuthor());//作者
        int wordNumber = responseData.getWordNumber();//字数
        float newNumber = (float) wordNumber / 10000;
        DecimalFormat df = new DecimalFormat("######0.00");
        selectBookNumber.setText("（约" + df.format(newNumber) + "万字）");
        selectBookProfile.setText(responseData.getContentAbout());//书籍简介
        selectBookAuthorProfile.setText(responseData.getAuthorAbout());//作者简介
        if (responseData.getCatalog() == null || "".equals(responseData.getCatalog())){
            selectBookLlCatalog.setVisibility(View.GONE);
        }else{
            selectBookLlCatalog.setVisibility(View.VISIBLE);
        }
        selectBookCatalogProfile.setText(responseData.getCatalog());//目录
    }


    @OnClick({R.id.select_book_back, R.id.challenge_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_book_back:
                finish();
                break;
            case R.id.challenge_select:
                if (responseData != null && responseData.getStatus() == 0){
                    Toast.makeText(this, "该书已下架", Toast.LENGTH_SHORT).show();
                }
                Intent setchallengeintent = new Intent(this, SettingChallegeDetailActivity.class);
                setchallengeintent.putExtra("bookId", responseData.getId());
                setchallengeintent.putExtra("bookName", responseData.getName());
                startActivity(setchallengeintent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
