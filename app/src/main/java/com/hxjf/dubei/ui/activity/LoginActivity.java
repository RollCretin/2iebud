package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ImLoginHelper;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.widget.LoadDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/5/31.
 * 登录界面
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.splash_login)
    ImageView splashLogin;
    @BindView(R.id.splash_checkbox)
    CheckBox splashCheckbox;
    @BindView(R.id.splash_tv_role)
    TextView splashTvRole;
    @BindView(R.id.splash_agreement)
    TextView splashAgreement;
    private UMShareAPI mShareAPI;
    private String registrationID;
    private UserDetailBean.ResponseDataBean responbean;
    private YWIMKit imKit;
    private int guide;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        mShareAPI = UMShareAPI.get(this);
        registrationID = JPushInterface.getRegistrationID(this);
        splashCheckbox.setChecked(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调

    }


    @OnClick({R.id.splash_login, R.id.splash_tv_role, R.id.splash_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.splash_login:
                if (splashCheckbox.isChecked()) {
                    LoadDialog.show(this,"正在登录...");
                    mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);

                    /*Intent intent = new Intent(this,MobileVerificationActivity.class);
                    startActivity(intent);
                    finish();*/
                }
                break;
            case R.id.splash_tv_role:
                Boolean isCheck = splashCheckbox.isChecked();
                splashCheckbox.setChecked(!isCheck);
                break;
            case R.id.splash_agreement:
                Intent intent = new Intent(this, agreementActivity.class);
                intent.putExtra("url", "http://test1.17dubei.com/agreement.html");
                startActivity(intent);
                break;
        }
    }

    UMAuthListener umAuthListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         *
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String openid = data.get("openid");
            String unionid = data.get("unionid");
            String country = data.get("country");
            String province = data.get("province");
            String city = data.get("city");
            String gender = data.get("gender");
            int sex = gender.equals("男") ? 1 : 2;
            String profile_image_url = data.get("profile_image_url");
            String screen_name = data.get("screen_name");
            String accessToken = data.get("accessToken");
            String refreshToken = data.get("refreshToken");
            String expiration = data.get("expiration");
            Call<UserDetailBean> loginCall = ReaderRetroift.getInstance(LoginActivity.this).getApi().loginCall(openid, unionid, country, province, city, sex, profile_image_url,
                    screen_name, accessToken, refreshToken, registrationID);
            loginCall.enqueue(new Callback<UserDetailBean>() {
                @Override
                public void onResponse(Call<UserDetailBean> call, Response<UserDetailBean> response) {
                    UserDetailBean bean = response.body();
                    if (bean != null) {
                        if (bean.getResponseData() != null && bean.getResponseCode() == 1) {
                            responbean = bean.getResponseData();

                            Gson gson = new Gson();//本地持久存储 splash判断
                            String jsonstr = gson.toJson(bean);
                            SPUtils.putString(LoginActivity.this, "bindbean", jsonstr);

                            guide = SPUtils.getInt(LoginActivity.this, "guide", 1);
                            LoadDialog.dismiss(LoginActivity.this);
                             if (!bean.getResponseData().isHasBookTag()) {
                                Intent intent = new Intent(LoginActivity.this, SelectLabelActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            //登录阿里账号
                            loginIM();
                        } else {
                            LoadDialog.dismiss(LoginActivity.this);
                            Toast.makeText(LoginActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                            Toast.makeText(LoginActivity.this, "返回数据为空，请重新登陆！", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<UserDetailBean> call, Throwable t) {
                    LoadDialog.dismiss(LoginActivity.this);
                    Toast.makeText(LoginActivity.this, "网络状态不佳，请稍后重试！", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            LoadDialog.dismiss(LoginActivity.this);
            Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            LoadDialog.dismiss(LoginActivity.this);
            Toast.makeText(LoginActivity.this, "取消登录", Toast.LENGTH_LONG).show();
        }
    };

    private void loginIM() {
        //获取SDK对象
        ImLoginHelper.getInstance().initIMKit(responbean.getId());
        imKit = ImLoginHelper.getInstance().getIMKit();
        //登录
        IYWLoginService loginService = imKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(responbean.getId(), responbean.getPassword());
        loginService.login(loginParam, new IWxCallback() {
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.putInt(LoginActivity.this,"guide",guide+1);
    }
}
