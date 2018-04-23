package com.hxjf.dubei.ui.activity;

import android.content.Intent;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.util.SPUtils;

import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Chen_Zhang on 2017/11/8.
 */

public class GuideActivity extends BaseActivity {
    private BGABanner mBackgroundBanner;
    private BGABanner mForegroundBanner;
    private int guide;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_guide;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        guide = SPUtils.getInt(GuideActivity.this, "guide", 0);
        if (guide != 0){
            startActivity(new Intent(GuideActivity.this, SplashActivity.class));
            finish();
        }
        initView();
        setListener();
        processLogic();
    }
    private void initView() {
        setContentView(R.layout.activity_guide);
        mBackgroundBanner = (BGABanner) findViewById(R.id.banner_guide_background);
        mForegroundBanner = (BGABanner) findViewById(R.id.banner_guide_foreground);
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter,0, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                SPUtils.putInt(GuideActivity.this,"guide",guide+1);
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void processLogic() {
        // 设置数据源
        mBackgroundBanner.setData(R.mipmap.uoko_guide_background_1, R.mipmap.uoko_guide_background_2, R.mipmap.uoko_guide_background_3);
        mForegroundBanner.setData(R.mipmap.uoko_guide_foreground_1, R.mipmap.uoko_guide_foreground_2, R.mipmap.uoko_guide_foreground_3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        mBackgroundBanner.setBackgroundResource(android.R.color.white);
    }
}
