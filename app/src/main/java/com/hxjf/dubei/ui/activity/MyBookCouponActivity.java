package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BookCouponListAdapter;
import com.hxjf.dubei.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/23.
 */

public class MyBookCouponActivity extends BaseActivity {
    @BindView(R.id.my_book_coupon_question)
    ImageView myBookCouponQuestion;
    @BindView(R.id.my_book_coupon_back)
    ImageView myBookCouponBack;
    @BindView(R.id.my_book_coupon_list)
    ListView myBookCouponList;
    private List<String> couponlist;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_book_coupon;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //请求数据
        //TODO

        //假数据
        couponlist = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            couponlist.add(i+"");
        }
        BookCouponListAdapter adapter = new BookCouponListAdapter(this,couponlist);
        myBookCouponList.setAdapter(adapter);

    }

    @OnClick({R.id.my_book_coupon_question, R.id.my_book_coupon_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_book_coupon_question:
                Intent intent = new Intent(this,UseBookCouponActivity.class);
                startActivity(intent);
                break;
            case R.id.my_book_coupon_back:
                finish();
                break;
        }
    }
}
