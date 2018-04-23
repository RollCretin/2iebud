package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BSBookListBean;
import com.hxjf.dubei.bean.BookDetailBean;
import com.hxjf.dubei.bean.DuBeiBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.ReadTimeBean;
import com.hxjf.dubei.bean.ShudanDetailBean;
import com.hxjf.dubei.network.ReaderRetroift;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/26.
 */

public class ReadDoneActivity extends BaseActivity {
    @BindView(R.id.read_done_image)
    ImageView readDoneImage;
    @BindView(R.id.read_done_time)
    TextView readDoneTime;
    @BindView(R.id.read_done_cb)
    CheckBox readDoneCb;
    private Integer theoryReadTotalTime;
    private Integer currentTime;
    private BookDetailBean.ResponseDataBean bookdetailbean;
    private BSBookListBean.ResponseDataBean bs_bookdetailbean;
    private String bookid;
    private int challengeflag;
    private ShudanDetailBean.ResponseDataBean.BooksBean list_bookdetailbean;
    private DuBeiBean.ResponseDataBean.ChallengeInfoBean.BookInfoBean challenge_bookdetailbean;
    private int commonReadTime;
    private int challengeReadTime;
    private int challengeFlag;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_read_done;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bookdetailbean = (BookDetailBean.ResponseDataBean) intent.getSerializableExtra("bookdetailBean");
        bs_bookdetailbean = (BSBookListBean.ResponseDataBean) intent.getSerializableExtra("bs_bookdetail");
        list_bookdetailbean = (ShudanDetailBean.ResponseDataBean.BooksBean) intent.getSerializableExtra("list_bookdetail");
        challenge_bookdetailbean = (DuBeiBean.ResponseDataBean.ChallengeInfoBean.BookInfoBean) intent.getSerializableExtra("challenge_bookdetail");

        challengeflag = intent.getIntExtra("challengeflag", 0);
        if (bookdetailbean != null) {
            bookid = bookdetailbean.getId();
        } else if (bs_bookdetailbean != null) {
            bookid = bs_bookdetailbean.getBookInfo().getId();
        } else if (list_bookdetailbean != null) {
            bookid = list_bookdetailbean.getId();
        } else if (challenge_bookdetailbean != null) {
            bookid = challenge_bookdetailbean.getId();
        }

        //获取这本书的正常理论阅读时间
        Call<ReadTimeBean> readTimeCall = ReaderRetroift.getInstance(this).getApi().bookReadTimeCall(bookid);
        readTimeCall.enqueue(new Callback<ReadTimeBean>() {
            @Override
            public void onResponse(Call<ReadTimeBean> call, Response<ReadTimeBean> response) {
                ReadTimeBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    int theoryReadTime = bean.getResponseData().getTheoryReadTime();
                    theoryReadTotalTime = Integer.valueOf(theoryReadTime);
                    commonReadTime = bean.getResponseData().getCommonReadTime();//正常阅读时间
                    challengeReadTime = bean.getResponseData().getChallengeReadTime();//挑战阅读时间
                    challengeFlag = bean.getResponseData().getChallengeFlag();//是否在挑战中(0-不在，1-在)
                    initData();
                }
            }

            @Override
            public void onFailure(Call<ReadTimeBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        //小时,分钟
        currentTime = (challengeFlag == 1 ? challengeReadTime : commonReadTime);
        int hour = currentTime / 3600;
        int minute = currentTime / 60;
        if (minute >= 60) {
            minute = minute - (minute / 60) * 60;
        }
        readDoneTime.setText(hour + "小时" + minute + "分钟");
        if (bookdetailbean != null) {
            Glide.with(this).load(ReaderRetroift.IMAGE_URL + bookdetailbean.getCover()).into(readDoneImage);
        } else if (bs_bookdetailbean != null) {
            Glide.with(this).load(ReaderRetroift.IMAGE_URL + bs_bookdetailbean.getBookInfo().getCoverPath()).into(readDoneImage);
        }
        if (theoryReadTotalTime > currentTime) {
            readDoneCb.setClickable(false);
            readDoneCb.setText("未达到理论阅读时间");
        } else {
            readDoneCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //阅读完成
                        readDone();
                        readDoneCb.setClickable(false);
                    }
                }
            });
        }
    }

    private void readDone() {
        Call<ModifyBean> readDoneCall = ReaderRetroift.getInstance(this).getApi().bookReadDone(bookid, 0);
        readDoneCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1) {
                        Toast.makeText(ReadDoneActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadDoneActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
    }
}
