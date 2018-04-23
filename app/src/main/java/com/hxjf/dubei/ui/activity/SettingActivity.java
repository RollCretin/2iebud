package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.network.ImLoginHelper;
import com.hxjf.dubei.util.CacheUtils;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.ScreenSizeUtil;
import com.hxjf.dubei.util.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/27.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.setting_back)
    ImageView settingBack;
    @BindView(R.id.setting_clean_cache)
    RelativeLayout settingCleanCache;
    @BindView(R.id.setting_about_reader)
    RelativeLayout settingAboutReader;
    @BindView(R.id.setting_feedback)
    RelativeLayout settingFeedback;
    @BindView(R.id.setting_unlogin)
    TextView settingUnlogin;
    @BindView(R.id.setting_cache_number)
    TextView settingCacheNumber;

    private String cacheSize;
    private boolean cleaned;
    private Handler mHandler = new Handler();
    private UMShareAPI mShareAPI;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        mShareAPI = UMShareAPI.get(this);
        //获取缓存大小回显
        try {
            cacheSize = CacheUtils.getTotalCacheSize(this);
            settingCacheNumber.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.setting_back, R.id.setting_clean_cache, R.id.setting_about_reader, R.id.setting_feedback, R.id.setting_unlogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_clean_cache:
                //清理缓存大小
                cleanCache();
                break;
            case R.id.setting_about_reader:
                Intent aboutIntent = new Intent(this,AboutReaderActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.setting_feedback:
                //意见反馈
                FeedbackAPI.openFeedbackActivity();
                /*Intent feedbackIntent = new Intent(this,FeedbackAactivity.class);
                startActivity(feedbackIntent);*/
                break;
            case R.id.setting_unlogin:
                logout();
                break;
        }
    }

    private void cleanCache() {
        if (cleaned) {
            ToastUtils.showToast(SettingActivity.this, "已清理，稍后再试");
            return;
        }else{
            CacheUtils.clearAllCache(this);
            mHandler.postDelayed(new Runnable() {
                @Override public void run() {
                    ToastUtils.showToast(SettingActivity.this,"清理完成");
                    settingCacheNumber.setText("0.00 B");
                    cleaned = true;
                }
            }, 500);
        }
    }

    private void logout() {
        //弹出对话框
        final Dialog bottomDialog = new Dialog(SettingActivity.this,R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_logout, null);
        TextView quit = (TextView) contentView.findViewById(R.id.dialog_logout_quit);
        TextView cancel = (TextView) contentView.findViewById(R.id.dialog_logout_cancel);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                //微信注销
                mShareAPI.deleteOauth(SettingActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                //清除cookie,清空用户信息
                SharedPreferences cookies_prefs = getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
                cookies_prefs.edit().clear().commit();
                SPUtils.remove(SettingActivity.this, "bindbean");
                //阿里注销
                IYWLoginService loginService = ImLoginHelper.getInstance().getIMKit().getLoginService();
                loginService.logout(new IWxCallback() {
                    @Override
                    public void onSuccess(Object... objects) {
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i) {

                    }
                });

                //将栈中所有activity清空，跳到login页面
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });

        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - ScreenSizeUtil.Dp2Px(this, 16f);
        params.bottomMargin = ScreenSizeUtil.Dp2Px(this, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

    }
    UMAuthListener umAuthListener =new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };
}
