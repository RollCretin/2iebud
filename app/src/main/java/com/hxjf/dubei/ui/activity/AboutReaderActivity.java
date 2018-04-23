package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.util.UpdateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/28.
 */

public class AboutReaderActivity extends BaseActivity {
    @BindView(R.id.about_back)
    ImageView aboutBack;
    @BindView(R.id.about_agreeement)
    TextView aboutAgreeement;
    @BindView(R.id.about_version)
    TextView aboutVersion;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_about_reader;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //获取版本号
        String currentVersion = UpdateUtils.getVersion(this);
        aboutVersion.setText("版本 "+currentVersion);
    }


    @OnClick({R.id.about_back, R.id.about_agreeement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_back:
                finish();
                break;
            case R.id.about_agreeement:
                Intent intent = new Intent(this, agreementActivity.class);
                intent.putExtra("url", "http://test1.17dubei.com/agreement.html");
                startActivity(intent);
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
