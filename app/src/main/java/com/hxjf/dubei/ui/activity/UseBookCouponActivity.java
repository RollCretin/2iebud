package com.hxjf.dubei.ui.activity;

import android.widget.ImageView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/23.
 */

public class UseBookCouponActivity extends BaseActivity {
    @BindView(R.id.my_book_coupon_back)
    ImageView myBookCouponBack;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_use_book_coupon;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.my_book_coupon_back)
    public void onClick() {
        finish();
    }
}
