package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.AchievementBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.widget.WaveView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/15.
 */

public class PersonAchievementActivity extends BaseActivity {


    @BindView(R.id.wave)
    WaveView wave;
    @BindView(R.id.achievement_iv_return)
    ImageView achievementIvReturn;
    @BindView(R.id.achievement_iv_share)
    ImageView achievementIvShare;
    @BindView(R.id.achievemnt_protrait)
    CircleImageView achievemntProtrait;
    @BindView(R.id.achievemnt_name)
    TextView achievemntName;
    @BindView(R.id.achievemnt_time)
    TextView achievemntTime;
    @BindView(R.id.achievemnt_lead)
    TextView achievemntLead;
    @BindView(R.id.achievement_jingxuan)
    TextView achievementJingxuan;
    @BindView(R.id.achievement_book)
    TextView achievementBook;
    @BindView(R.id.achievemnt_note)
    TextView achievemntNote;
    @BindView(R.id.achievement_share_layout)
    LinearLayout achievementShareLayout;
    @BindView(R.id.test)
    LinearLayout test;
    @BindView(R.id.achievemnt_erweima)
    ImageView achievemntErweima;
    private String userId;
    private AchievementBean.ResponseDataBean responseData;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_person_achievement;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        achievementIvShare.setClickable(false);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        if (userId == null) {
            Call<AchievementBean> achievementCall = ReaderRetroift.getInstance(this).getApi().achievementCall();
            achievementCall.enqueue(new Callback<AchievementBean>() {
                @Override
                public void onResponse(Call<AchievementBean> call, Response<AchievementBean> response) {
                    AchievementBean bean = response.body();
                    if (bean != null) {
                        responseData = bean.getResponseData();
                        initData();
                    }
                }

                @Override
                public void onFailure(Call<AchievementBean> call, Throwable t) {

                }
            });
        } else {

            Call<AchievementBean> personAchievementCall = ReaderRetroift.getInstance(this).getApi().personAchievementCall(userId);
            personAchievementCall.enqueue(new Callback<AchievementBean>() {
                @Override
                public void onResponse(Call<AchievementBean> call, Response<AchievementBean> response) {
                    AchievementBean bean = response.body();
                    if (bean != null) {
                        responseData = bean.getResponseData();
                        initData();
                    }
                }

                @Override
                public void onFailure(Call<AchievementBean> call, Throwable t) {

                }
            });
        }

        wave.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {

            }
        });

    }

    private void initData() {
        achievementIvShare.setClickable(true);
        if (responseData != null){
            GlideLoadUtils.getInstance().glideLoad(this,ReaderRetroift.IMAGE_URL + responseData.getNormalPath(),achievemntProtrait,0);
        }else{
            Glide.with(this).load(R.mipmap.dubei_dafult_pratroit).into(achievemntProtrait);
        }
        achievemntName.setText(responseData.getNickName());
        int readTime = responseData.getReadTime();
        int hour = readTime / 3600;
        int minute = (readTime - hour * 3600) / 60;
        achievemntTime.setText(hour + "小时" + minute + "分");
        if (readTime < 60) {
            achievemntLead.setText("领先0%");
        } else {
            achievemntLead.setText("领先" + responseData.getBeyondPercent());
        }
        achievementJingxuan.setText(responseData.getReadDivideBookNum() + "");
        achievementBook.setText(responseData.getReadBookNum() + "");
        achievemntNote.setText(responseData.getReadNoteNum() + "");

    }

    @OnClick({R.id.achievement_iv_return, R.id.achievement_iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.achievement_iv_return:
                finish();
                break;
            case R.id.achievement_iv_share:
                share();
                break;
        }
    }

    private void share() {
        Glide.with(PersonAchievementActivity.this).load(R.mipmap.download_qrcode).into(achievemntErweima);
        final Dialog bottomDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_share_achievement, null);
        TextView tv_wx = (TextView) contentView.findViewById(R.id.dialog_share_wx);
        TextView tv_pengyouquan = (TextView) contentView.findViewById(R.id.dialog_share_pengyouquan);
        Button btn_cancel = (Button) contentView.findViewById(R.id.dialog_share_cancel);
        tv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享微信好友,分享成功之后调用分享接口
                Bitmap bitmap = convertViewToBitmap(PersonAchievementActivity.this, achievementShareLayout);
                savePhotoToSDCard(bitmap, "/sdcard/screenshot", responseData.getNickName());
                File file = new File("/sdcard/screenshot/" + responseData.getNickName() + ".png");
                UMImage image = new UMImage(PersonAchievementActivity.this, file);
                image.compressStyle = UMImage.CompressStyle.SCALE;
                new ShareAction(PersonAchievementActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).withText(responseData.getNickName() + "的成就").setCallback(shareListener).withMedia(image).share();
                bottomDialog.dismiss();
            }
        });
        tv_pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享到盆友圈
                Bitmap bitmap = convertViewToBitmap(PersonAchievementActivity.this, achievementShareLayout);
                savePhotoToSDCard(bitmap, "/sdcard/screenshot", responseData.getNickName());
                File file = new File("/sdcard/screenshot/" + responseData.getNickName() + ".png");
                UMImage image = new UMImage(PersonAchievementActivity.this, file);
                image.compressStyle = UMImage.CompressStyle.SCALE;
                new ShareAction(PersonAchievementActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withText(responseData.getNickName() + "的成就").setCallback(shareListener).withMedia(image).share();
                bottomDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(PersonAchievementActivity.this).load(R.mipmap.achievement_bottom_logo).into(achievemntErweima);
                bottomDialog.dismiss();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(params);

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCancelable(false);
        bottomDialog.setCanceledOnTouchOutside(false);
        bottomDialog.show();
    }

    public static Bitmap convertViewToBitmap(Context context, View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        //利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        //把view中的内容绘制在画布上
        view.draw(canvas);
        return bitmap;
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
            Glide.with(PersonAchievementActivity.this).load(R.mipmap.achievement_bottom_logo).into(achievemntErweima);
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Glide.with(PersonAchievementActivity.this).load(R.mipmap.achievement_bottom_logo).into(achievemntErweima);
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Glide.with(PersonAchievementActivity.this).load(R.mipmap.achievement_bottom_logo).into(achievemntErweima);
        }
    };

    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File photoFile = new File(path, photoName + ".png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
