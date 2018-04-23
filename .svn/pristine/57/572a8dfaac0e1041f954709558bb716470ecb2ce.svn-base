package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/23 0023.
 * 我的藏书详情
 */

public class MyBookBorrowDetailActivity extends BaseActivity {
    @BindView(R.id.my_collect_book_detail_back)
    ImageView myCollectBookDetailBack;
    @BindView(R.id.my_collect_book_detail_delete)
    TextView myCollectBookDetailDelete;
    @BindView(R.id.my_collect_book_detail_image)
    ImageView myCollectBookDetailImage;
    @BindView(R.id.my_collect_book_detail_switch)
    Switch myCollectBookDetailSwitch;
    @BindView(R.id.my_collect_book_detail_bookname)
    TextView myCollectBookDetailBookname;
    @BindView(R.id.my_collect_book_detail_author)
    TextView myCollectBookDetailAuthor;
    @BindView(R.id.my_collect_book_detail_ratingbar)
    RatingBar myCollectBookDetailRatingbar;
    @BindView(R.id.my_collect_book_detail_score)
    TextView myCollectBookDetailScore;
    @BindView(R.id.my_collect_book_detail_price)
    TextView myCollectBookDetailPrice;
    @BindView(R.id.my_collect_book_detail_press)
    TextView myCollectBookDetailPress;
    @BindView(R.id.my_collect_book_detail_updatetime)
    TextView myCollectBookDetailUpdatetime;
    @BindView(R.id.my_collect_book_detail_rule)
    TextView myCollectBookDetailRule;
    @BindView(R.id.my_collect_book_detail_explain)
    TextView myCollectBookDetailExplain;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_bookborrow_detail;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String bookFavoriteId = intent.getStringExtra("bookFavoriteId");
    }

    @OnClick({R.id.my_collect_book_detail_back, R.id.my_collect_book_detail_delete,R.id.my_collect_book_detail_rule, R.id.my_collect_book_detail_explain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_collect_book_detail_back:
                finish();
                break;
            case R.id.my_collect_book_detail_delete:
                break;
            case R.id.my_collect_book_detail_rule:
                break;
            case R.id.my_collect_book_detail_explain:
                Intent explainIntent = new Intent(this,SaveExplainActivity.class);
                startActivity(explainIntent);
                break;
        }
    }

}
