package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018/4/24.
 */

public class WelcomeContributeActivity extends BaseActivity {
    @BindView(R.id.welcome_contribute_back)
    ImageView welcomeContributeBack;
    @BindView(R.id.welcome_contribute_email)
    TextView welcomeContributeEmail;
    @BindView(R.id.welcome_contribute_title)
    TextView welcomeContributeTitle;
    @BindView(R.id.welcome_contribute_des)
    TextView welcomeContributeDes;
    private String value;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_welcome_contribute;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        value = intent.getStringExtra("value");
        if (value.equals("创建书单")){
            welcomeContributeTitle.setText(getResources().getString(R.string.contributr_booklist_title));
            welcomeContributeDes.setText(getResources().getString(R.string.contributr_booklist));
        }else if (value.equals("拆读投稿")){
            welcomeContributeTitle.setText(getResources().getString(R.string.contributr_chiadu_title));
            welcomeContributeDes.setText(getResources().getString(R.string.contributr_chiadu));
        }else if (value.equals("笔记投稿")){
            welcomeContributeTitle.setText(getResources().getString(R.string.contributr_note_title));
            welcomeContributeDes.setText(getResources().getString(R.string.contributr_note));
        }
    }

    @OnClick(R.id.welcome_contribute_back)
    public void onViewClicked() {
        finish();
    }

}
