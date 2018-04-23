package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.LauncherChallengeBean;
import com.hxjf.dubei.bean.ShareInfoBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/8.
 */

public class SettingChallengeSuccessActivity extends BaseActivity {
    @BindView(R.id.setting_challenge_success_home)
    TextView settingChallengeSuccessHome;
    @BindView(R.id.setting_challenge_success_cover)
    ImageView settingChallengeSuccessCover;
    @BindView(R.id.setting_challenge_success_name)
    TextView settingChallengeSuccessName;
    @BindView(R.id.setting_challenge_success_money)
    TextView settingChallengeSuccessMoney;
    @BindView(R.id.setting_challenge_success_time)
    TextView settingChallengeSuccessTime;
    @BindView(R.id.setting_challenge_success_wx)
    TextView settingChallengeSuccessWx;
    @BindView(R.id.setting_challenge_success_pengyouquan)
    TextView settingChallengeSuccessPengyouquan;
    private LauncherChallengeBean.ResponseDataBean dataBean;
    private ShareInfoBean.ResponseDataBean shareinfo;
    private UMWeb web;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_setting_challenge_success;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        settingChallengeSuccessWx.setClickable(false);
        settingChallengeSuccessPengyouquan.setClickable(false);

        Intent intent = getIntent();
        dataBean = (LauncherChallengeBean.ResponseDataBean) intent.getSerializableExtra("dataBean");
        Glide.with(this).load(ReaderRetroift.IMAGE_URL+ dataBean.getBookInfo().getCoverPath()).into(settingChallengeSuccessCover);//封面
        settingChallengeSuccessName.setText(dataBean.getBookInfo().getName());//书名
        settingChallengeSuccessMoney.setText("挑战金 ¥"+ dataBean.getCMoney());
        String startTime = dataBean.getStartTime();
        String endTime = dataBean.getEndTime();
        StringBuilder sbStartTime = new StringBuilder();
        StringBuilder sbEndTime = new StringBuilder();
        for (int i = 0; i < startTime.length(); i++) {
            sbStartTime.append(startTime.charAt(i));
            if (i == 3 || i == 5){
                sbStartTime.append(".");
            }
        }
        for (int i = 0; i < endTime.length(); i++) {
            sbEndTime.append(endTime.charAt(i));
            if (i == 3 || i == 5){
                sbEndTime.append(".");
            }
        }
        settingChallengeSuccessTime.setText("参赛时间："+sbStartTime.toString()+"~"+sbEndTime.toString());

        //获取分享信息
        Call<ShareInfoBean> shareInfoCall = ReaderRetroift.getInstance(this).getApi().shareInfoCall(dataBean.getId(), "challenge");
        shareInfoCall.enqueue(new Callback<ShareInfoBean>() {
            @Override
            public void onResponse(Call<ShareInfoBean> call, Response<ShareInfoBean> response) {
                ShareInfoBean bean = response.body();
                if(bean != null){
                    shareinfo = bean.getResponseData();
                    settingChallengeSuccessWx.setClickable(true);
                    settingChallengeSuccessPengyouquan.setClickable(true);

                    web = new UMWeb(shareinfo.getUrl());
                    web.setTitle(shareinfo.getTitle());//标题
                    web.setThumb(new UMImage(SettingChallengeSuccessActivity.this, ReaderRetroift.IMAGE_URL + shareinfo.getImagePath()));  //缩略图
                    web.setDescription(shareinfo.getSummary());//描述
                }
            }

            @Override
            public void onFailure(Call<ShareInfoBean> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.setting_challenge_success_home, R.id.setting_challenge_success_wx, R.id.setting_challenge_success_pengyouquan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_challenge_success_home:
                Intent intent = new Intent(SettingChallengeSuccessActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.setting_challenge_success_wx:
                new ShareAction(SettingChallengeSuccessActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
                break;
            case R.id.setting_challenge_success_pengyouquan:
                new ShareAction(SettingChallengeSuccessActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
                break;
        }
    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(SettingChallengeSuccessActivity.this, "分享成功" , Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SettingChallengeSuccessActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SettingChallengeSuccessActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };
}
