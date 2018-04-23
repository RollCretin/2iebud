package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.base.DuBeiApplication;
import com.hxjf.dubei.bean.ActivityDetailBean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.util.StatusBarUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/9/21.
 */

public class WonderfulDetailActivity extends BaseActivity {
    private static final int BOOK_PROFILE = 1;
    @BindView(R.id.wonderful_detail_back)
    ImageView wonderfulDetailBack;
    @BindView(R.id.wonderful_detail_share)
    TextView wonderfulDetailShare;
    @BindView(R.id.wonderful_detail_name)
    TextView wonderfulDetailName;
    @BindView(R.id.wonderful_detail_tag)
    TextView wonderfulDetailTag;
    @BindView(R.id.wonderful_detail_pratroit)
    CircleImageView wonderfulDetailPratroit;
    @BindView(R.id.wonderful_detail_who)
    TextView wonderfulDetailWho;
    @BindView(R.id.wonderful_detail_image)
    ImageView wonderfulDetailImage;
    @BindView(R.id.wonderful_detail_time)
    TextView wonderfulDetailTime;
    @BindView(R.id.wonderful_detail_location)
    TextView wonderfulDetailLocation;
    @BindView(R.id.wonderful_detail_content)
    WebView wonderfulDetailContent;
    @BindView(R.id.wonderful_detail_ll_touxiang)
    LinearLayout wonderfulDetailLlTouxiang;

    private String activityId;
    private ActivityDetailBean.ResponseDataBean responseBean;
    private boolean profile_isShrink = true;
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
            Toast.makeText(WonderfulDetailActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(WonderfulDetailActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WonderfulDetailActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_wonderful_detail;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //设置状态栏的颜色
        StatusBarUtils.setWindowStatusBarColor(this, R.color.note_bg);
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        Call<ActivityDetailBean> activityDetailCall = NewDiscoveryRetrofit.getInstance(this).getApi().activityDetailCall(activityId);
        activityDetailCall.enqueue(new Callback<ActivityDetailBean>() {
            @Override
            public void onResponse(Call<ActivityDetailBean> call, Response<ActivityDetailBean> response) {
                ActivityDetailBean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null) {
                        responseBean = bean.getResponseData();
                        initData();
                    } else {
                        Toast.makeText(WonderfulDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ActivityDetailBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        wonderfulDetailName.setText(responseBean.getTitle());
        String tag = responseBean.getTag();
        if ("".equals(tag) || tag == null) {
            wonderfulDetailTag.setVisibility(View.GONE);
        } else {
            wonderfulDetailTag.setText(responseBean.getTag());
        }
        wonderfulDetailWho.setText(responseBean.getOwnerName());
        GlideLoadUtils.getInstance().glideLoad(this, NewDiscoveryRetrofit.IMAGE_URL + responseBean.getBanner(), wonderfulDetailImage, 0);
        GlideLoadUtils.getInstance().glideLoad(this, NewDiscoveryRetrofit.IMAGE_URL + responseBean.getOwnerHeadPhoto(), wonderfulDetailPratroit, 0);
        wonderfulDetailTime.setText(responseBean.getTime());
        wonderfulDetailLocation.setText(responseBean.getAddress());
        wonderfulDetailContent.setWebViewClient(new WebViewClient());
        wonderfulDetailContent.getSettings().setDefaultTextEncodingName("utf-8");
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(DensityUtil.dip2px(WonderfulDetailActivity.this, 15), 0, DensityUtil.dip2px(WonderfulDetailActivity.this, 15), 0);
        wonderfulDetailContent.setLayoutParams(param);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wonderfulDetailContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            wonderfulDetailContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        wonderfulDetailContent.loadData(getHtmlData(responseBean.getSummary()), "text/html; charset=utf-8", "utf-8");

    }

    //为html内容添加头
    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @OnClick({R.id.wonderful_detail_back, R.id.wonderful_detail_share, R.id.wonderful_detail_ll_touxiang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wonderful_detail_back:
                finish();
                break;
            case R.id.wonderful_detail_share:
                //分享
                share();
                break;
            case R.id.wonderful_detail_ll_touxiang:
                if (Integer.valueOf(responseBean.getOwnerType()) == 1){
                    Intent zoneIntent = new Intent(this,ZoneDetailActivity.class);
                    zoneIntent.putExtra("bookbarId",responseBean.getOwnerId());
                    startActivity(zoneIntent);
                }
                break;
        }
    }

    private void share() {
        final UMWeb web = new UMWeb(NewDiscoveryRetrofit.BASE_URL + responseBean.getShareUrl());
        web.setTitle(responseBean.getTitle());//标题
        web.setThumb(new UMImage(WonderfulDetailActivity.this, NewDiscoveryRetrofit.IMAGE_URL + responseBean.getBanner()));  //缩略图
        web.setDescription(responseBean.getIntro());//描述

        final Dialog bottomDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_share, null);
        TextView tv_wx = (TextView) contentView.findViewById(R.id.dialog_share_wx);
        TextView tv_pengyouquan = (TextView) contentView.findViewById(R.id.dialog_share_pengyouquan);
        Button btn_cancel = (Button) contentView.findViewById(R.id.dialog_share_cancel);
        tv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享微信好友,分享成功之后调用分享接口
                new ShareAction(WonderfulDetailActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
                bottomDialog.dismiss();
            }
        });
        tv_pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到盆友圈
                new ShareAction(WonderfulDetailActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)
                        .setCallback(shareListener)//回调监听器
                        .share();
                bottomDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(params);

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    private void OpenOrShrink(TextView loadView) {
        if (profile_isShrink) {
            //展开
            profile_isShrink = false;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            wonderfulDetailContent.setLayoutParams(params);
            loadView.setText("收起");
        } else {
            profile_isShrink = true;
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(DuBeiApplication.getContext(), 100));
            ViewGroup.LayoutParams layoutParams = wonderfulDetailContent.getLayoutParams();
            layoutParams.height = DensityUtil.dip2px(DuBeiApplication.getContext(), 100);
            wonderfulDetailContent.setLayoutParams(layoutParams);
            loadView.setText("查看更多");
        }
    }


}
