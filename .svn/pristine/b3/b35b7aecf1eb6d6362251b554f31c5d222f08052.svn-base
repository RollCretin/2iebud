package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/12/7.
 */

public class registerSuccessActivity extends BaseActivity {
    @BindView(R.id.success_give_back)
    ImageView successGiveBack;
    @BindView(R.id.success_give_home)
    Button successGiveHome;
    @BindView(R.id.success_give_account)
    Button successGiveAccount;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_register_success;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //SP中存取首页是否要刷新-1-要刷新，0-不要刷新
        SPUtils.putInt(registerSuccessActivity.this,"isRefresh",1);

        Call<UserDetailBean> userDetailBeanCall = ReaderRetroift.getInstance(registerSuccessActivity.this).getApi().myDetailCall();
        userDetailBeanCall.enqueue(new Callback<UserDetailBean>() {
            @Override
            public void onResponse(Call<UserDetailBean> call, Response<UserDetailBean> response) {
                UserDetailBean userDetailBean = response.body();
                if (userDetailBean != null && userDetailBean.getResponseCode() == 1) {
                    Gson gson = new Gson();//本地持久存储 splash判断
                    String jsonstr = gson.toJson(userDetailBean);
                    SPUtils.putString(registerSuccessActivity.this, "bindbean", jsonstr);
                }
            }

            @Override
            public void onFailure(Call<UserDetailBean> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.success_give_back, R.id.success_give_home, R.id.success_give_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.success_give_back:
                finish();
                break;
            case R.id.success_give_home:
                //回到首页
                finish();
                break;
            case R.id.success_give_account:
                //回到我的账户
                Intent accountIntent = new Intent(this, MyAccountActivity.class);
                startActivity(accountIntent);
                finish();
                break;
        }
    }
}
