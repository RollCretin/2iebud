package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.SPUtils;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/19.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        super.init();

        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Call<UserDetailBean> userDetailBeanCall = ReaderRetroift.getInstance(SplashActivity.this).getApi().myDetailCall();
        userDetailBeanCall.enqueue(new Callback<UserDetailBean>() {
            @Override
            public void onResponse(Call<UserDetailBean> call, Response<UserDetailBean> response) {
                UserDetailBean bean = response.body();
                if(bean != null && bean.getResponseCode() == 1){
                    Gson gson = new Gson();//本地持久存储 splash判断
                    String jsonstr = gson.toJson(bean);
                    SPUtils.putString(SplashActivity.this, "bindbean", jsonstr);
                }
            }

            @Override
            public void onFailure(Call<UserDetailBean> call, Throwable t) {

            }
        });

        Timer timer = new Timer();
        //判断用户是否登陆
        //登陆状态下 --> MainActivity
        final Intent mainIntent = new Intent(this, MainActivity.class);
        //未登陆状态下 --> LoginActivity
        final Intent loginIntent = new Intent(this, LoginActivity.class);

        //SP获取bindbean
        String bindbeanStr = SPUtils.getString(this, "bindbean", "");
        Gson gson = new Gson();
        final UserDetailBean userbean = gson.fromJson(bindbeanStr, UserDetailBean.class);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (userbean != null && userbean.getResponseCode() == 1 && userbean.getResponseData().isHasBookTag()) {
                    startActivity(mainIntent);
                    finish();
                } else {
                    startActivity(loginIntent);
                    finish();
                }
            }
        };
        timer.schedule(task, 2000);
    }


}
