package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.SMSBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ReaderApi;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.ScreenSizeUtil;
import com.hxjf.dubei.widget.LoadDialog;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/18.
 */

public class MobileVerificationActivity extends BaseActivity {
    @BindView(R.id.verfic_back)
    ImageView verficBack;
    @BindView(R.id.verfic_et_phone)
    EditText verficEtPhone;
    @BindView(R.id.verfic_et_code)
    EditText verficEtCode;
    @BindView(R.id.verfic_btn_getcode)
    Button verficBtnGetcode;
    @BindView(R.id.verfic_btn_start)
    Button verficBtnStart;
    @BindView(R.id.verfic_dubi_purpose)
    TextView verficPurpose;
    private SMSBean smsbean;

    private int responseCode;
    private ModifyBean modifyBean;
    private UserDetailBean userbean;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_mobile_verification;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        //SP获取bindbean--设置用户手机号
        String bindbeanStr = SPUtils.getString(MobileVerificationActivity.this, "bindbean", "");
        Gson gson = new Gson();
        userbean = gson.fromJson(bindbeanStr, UserDetailBean.class);
        /*//清除所有cookie
        SharedPreferences sp = this.getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();*/

        verficBtnStart.setEnabled(false);
        verficEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidTelNumber(verficEtPhone.getText().toString())) {
                    verficBtnStart.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @OnClick({R.id.verfic_back, R.id.verfic_btn_getcode, R.id.verfic_btn_start, R.id.verfic_dubi_purpose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verfic_back:
                finish();
                break;
            case R.id.verfic_btn_getcode:
                //判断手机号
                if (isValidTelNumber(verficEtPhone.getText().toString())) {
                    //获取验证码
                    getCode();
                } else {
                    Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.verfic_btn_start:
                startReader();
                break;
            case R.id.verfic_dubi_purpose:
                //跳过手机验证
                verficPurpose();
                break;
        }
    }

    private void verficPurpose() {
        final Dialog detailDialog = new Dialog(MobileVerificationActivity.this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(MobileVerificationActivity.this).inflate(R.layout.view_dialog_dubi_use, null);
        TextView done = (TextView) contentView.findViewById(R.id.dialog_dubi_use);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDialog.dismiss();
            }
        });
        detailDialog.setContentView(contentView);
        detailDialog.show();
        android.view.WindowManager.LayoutParams p = detailDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (ScreenSizeUtil.getScreenWidth(MobileVerificationActivity.this) * 0.7); //宽度设置为屏幕的
        detailDialog.getWindow().setAttributes(p); //设置生效
    }

    /**
     * 登录
     */
    private void startReader() {
        if (verficEtCode.getText().toString() == null) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadDialog.show(this);
        final Call<ModifyBean> registerFGetDubi = ReaderRetroift.getInstance(MobileVerificationActivity.this).getApi().registerFGetDubi(verficEtPhone.getText().toString(), verficEtCode.getText().toString());
        registerFGetDubi.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1) {

                        userbean.getResponseData().setTelephone(verficEtPhone.getText().toString());
                        Gson gson = new Gson();//本地持久存储 splash判断
                        String jsonstr = gson.toJson(bean);
                        SPUtils.putString(MobileVerificationActivity.this, "bindbean", jsonstr);
                        //跳转到领取成功界面
                        LoadDialog.dismiss(MobileVerificationActivity.this);
                        Intent registerIntent = new Intent(MobileVerificationActivity.this,registerSuccessActivity.class);
                        startActivity(registerIntent);
                        finish();
                    } else {
                        LoadDialog.dismiss(MobileVerificationActivity.this);
                        Toast.makeText(MobileVerificationActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
    }

    private void getDubi(final String number, String validNum) {

    }

    private void getCode() {
        loadData();
        MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);
        myCountDownTimer.start();
    }

    /**
     * 获取
     */
    private void loadData() {
        ReaderApi api = ReaderRetroift.getInstance(this).getApi();
        Call<SMSBean> smsCall = api.getSMSBean(verficEtPhone.getText().toString());
        smsCall.enqueue(new Callback<SMSBean>() {
            @Override
            public void onResponse(Call<SMSBean> call, Response<SMSBean> response) {
                smsbean = response.body();
                if (smsbean != null) {
                    String responseMsg = smsbean.getResponseMsg();
                    Toast.makeText(MobileVerificationActivity.this, responseMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SMSBean> call, Throwable t) {
            }
        });

    }

    // 判断手机号码是否有效
    public boolean isValidTelNumber(String telNumber) {
        if (!TextUtils.isEmpty(telNumber)) {
            String regex = "(\\+\\d+)?1[34578]\\d{9}$";
            return Pattern.matches(regex, telNumber);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            verficBtnGetcode.setEnabled(false);
            verficBtnGetcode.setText(l / 1000 + "s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            verficBtnGetcode.setText("重新获取验证码");
            //设置可点击
            verficBtnGetcode.setEnabled(true);
        }
    }


}
