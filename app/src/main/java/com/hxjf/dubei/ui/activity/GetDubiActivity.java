package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.widget.FloatView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/24.
 */

public class GetDubiActivity extends BaseActivity {
    @BindView(R.id.float_view)
    FloatView floatView;
    @BindView(R.id.float_view_bg)
    RelativeLayout floatViewBg;
    @BindView(R.id.get_dubi_return)
    ImageView getDubiReturn;
    @BindView(R.id.float_view_ll_yueli)
    LinearLayout floatViewLlYueli;
    @BindView(R.id.get_dubi_whatis_dubi)
    TextView getDubiWhatisDubi;
    @BindView(R.id.get_dubi_promote_yueli)
    TextView getDubiPromoteYueli;
    @BindView(R.id.get_dubi_share)
    TextView getDubiShare;
    @BindView(R.id.get_dubi_account)
    TextView getDubiAccount;
    @BindView(R.id.get_dubi_user_container)
    LinearLayout getDubiUserContainer;
    @BindView(R.id.get_dubi_yueli)
    TextView getDubiYueli;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_get_dubi;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) floatViewBg.getLayoutParams();
        Params.height = (int) (0.7 * DensityUtil.getScreenWidthAndHeight(this)[1]);
        floatViewBg.setLayoutParams(Params);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) floatViewLlYueli.getLayoutParams();
        layoutParams.setMargins(0, (int) (0.7 * DensityUtil.getScreenWidthAndHeight(this)[1]) - DensityUtil.dip2px(this, 24), DensityUtil.dip2px(this, 5), 0);
        floatViewLlYueli.setLayoutParams(layoutParams);

        List<Float> list = new ArrayList<>();
        list.add((float) 1.567);
        list.add((float) 0.261);
        list.add((float) 2.455);
        list.add((float) 12.44);
        list.add((float) 3.21);
        list.add((float) 1.789);
        list.add((float) 0.244);
        list.add((float) 2.356);
        list.add((float) 2.425);
        floatView.setList(list);
        floatView.setOnItemClickListener(new FloatView.OnItemClickListener() {
            @Override
            public void itemClick(int position, Number value) {
                Toast.makeText(GetDubiActivity.this, "当前是第" + position + "个，其值是" + value.floatValue(), Toast.LENGTH_SHORT).show();
            }
        });

        //添加排行榜
        getDubiUserContainer.removeAllViews();
        for (int i = 0; i < 8; i++) {
            View itemRank = View.inflate(this, R.layout.item_getdubi_user_rank, null);
            TextView rank = (TextView) itemRank.findViewById(R.id.item_getdubi_user_rank);
            TextView name = (TextView) itemRank.findViewById(R.id.item_getdubi_user_name);
            TextView yueli = (TextView) itemRank.findViewById(R.id.item_getdubi_user_yueli);
            TextView dubi = (TextView) itemRank.findViewById(R.id.item_getdubi_user_dubi);
            switch (i){
                case 0:
                    rank.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.rank_gold),null,null,null);
                    break;
                case 1:
                    rank.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.rank_silver),null,null,null);
                    break;
                case 2:
                    rank.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.rank_copper),null,null,null);
                    break;
            }
            if (i>2){
                rank.setText(i+1+"");
                rank.setPadding(DensityUtil.dip2px(this,6),0,0,0);
                rank.setCompoundDrawablesWithIntrinsicBounds(null,null,null,    null);
            }
            getDubiUserContainer.addView(itemRank);
        }

    }

    @OnClick({R.id.get_dubi_return, R.id.get_dubi_whatis_dubi, R.id.get_dubi_promote_yueli, R.id.get_dubi_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_dubi_return:
                finish();
                break;
            case R.id.get_dubi_whatis_dubi:
            case R.id.get_dubi_share:
                Intent shareIntent = new Intent(this,WhatIsDubiActivity.class);
                startActivity(shareIntent);
                break;
            case R.id.get_dubi_promote_yueli:
                //提升读币
                Intent dubiIntent = new Intent(this,PromoteDubiActivity.class);
                startActivity(dubiIntent);
                break;

        }
    }
}
