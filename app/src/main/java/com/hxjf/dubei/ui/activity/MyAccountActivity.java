package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ApplyBean;
import com.hxjf.dubei.bean.BalanceBean;
import com.hxjf.dubei.bean.WithdrawBean;
import com.hxjf.dubei.network.AuthResult;
import com.hxjf.dubei.network.PayResult;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.ScreenSizeUtil;
import com.hxjf.dubei.widget.LoadDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/13.
 */

public class MyAccountActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @BindView(R.id.my_account_back)
    ImageView myAccountBack;
    @BindView(R.id.my_account_num)
    TextView myAccountNum;
    @BindView(R.id.my_account_withdraw)
    TextView myAccountWithdraw;
    @BindView(R.id.my_account_recharge)
    TextView myAccountRecharge;
    @BindView(R.id.my_account_detail)
    TextView myAccountDetail;
    @BindView(R.id.my_account_text)
    TextView myAccountText;
    @BindView(R.id.my_account_refresh)
    SwipeRefreshLayout myAccountRefresh;


    private ArrayList<CheckBox> cbList;
    private ApplyBean applyBean;
    private String balanceStr;
    private double balance;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_account;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        myAccountRefresh.setRefreshing(true);
        myAccountRefresh.setOnRefreshListener(this);
        myAccountRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));

        //账户余额
        Call<BalanceBean> getBalanceCall = ReaderRetroift.getInstance(MyAccountActivity.this).getApi().getBalanceCall();
        getBalanceCall.enqueue(new Callback<BalanceBean>() {

            @Override
            public void onResponse(Call<BalanceBean> call, Response<BalanceBean> response) {
                BalanceBean balanceBean = response.body();
                if (balanceBean != null && balanceBean.getResponseData() != null){
                    balance = balanceBean.getResponseData().getAccountMoney();
                    DecimalFormat df = new DecimalFormat("######0.00");
                    balanceStr = df.format(balanceBean.getResponseData().getAccountMoney());
                    myAccountNum.setText(balanceStr);
                    myAccountRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<BalanceBean> call, Throwable t) {
                myAccountRefresh.setRefreshing(false);
            }
        });

    }


    @OnClick({R.id.my_account_back, R.id.my_account_withdraw, R.id.my_account_recharge, R.id.my_account_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_account_back:
                finish();
                break;
            case R.id.my_account_withdraw:
               //读币用途
                accountDetail();
                break;
            case R.id.my_account_recharge:
                //充值
                recharge();
                break;
            case R.id.my_account_detail:
                //账户明细
                Intent detailAccountIntent = new Intent(this, AccountDetailActivity.class);
                startActivity(detailAccountIntent);
                break;
        }
    }

    private void accountDetail() {

        final Dialog detailDialog = new Dialog(MyAccountActivity.this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.view_dialog_dubi_use, null);
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
        p.width = (int) (ScreenSizeUtil.getScreenWidth(MyAccountActivity.this) * 0.7); //宽度设置为屏幕的
        detailDialog.getWindow().setAttributes(p); //设置生效
    }

    private void withdraw() {
        //提现
        final Dialog withdrawialog = new Dialog(this,R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_withdraw, null);
        ImageView iv_cancel = (ImageView) contentView.findViewById(R.id.dialog_withdraw_cancel);
        final EditText tv_money = (EditText) contentView.findViewById(R.id.dialog_withdraw_money);
        tv_money.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        final TextView tv_reminder = (TextView) contentView.findViewById(R.id.dialog_withdraw_reminder);
        final EditText tv_account = (EditText) contentView.findViewById(R.id.dialog_withdraw_account);
        final EditText tv_name = (EditText) contentView.findViewById(R.id.dialog_withdraw_name);
        TextView tv_confirm = (TextView) contentView.findViewById(R.id.dialog_withdraw_confirm);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判空
                if (tv_money.getText() == null){
                    Toast.makeText(MyAccountActivity.this, "提现金额不能为空", Toast.LENGTH_SHORT).show();
                }else if(tv_account.getText() == null){
                    Toast.makeText(MyAccountActivity.this, "提现账户不能为空", Toast.LENGTH_SHORT).show();
                }else if(tv_name.getText() == null){
                    Toast.makeText(MyAccountActivity.this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    Double withdrawMoney = Double.valueOf(tv_money.getText().toString());
                    String withdrawAccount = tv_account.getText().toString();
                    String withdrawName = tv_name.getText().toString();
                    //发起提现
                    withdrawialog.dismiss();
                    LoadDialog.show(MyAccountActivity.this);
                    Call<WithdrawBean> withdrawCall = ReaderRetroift.getInstance(MyAccountActivity.this).getApi().withdrawCall(withdrawMoney, withdrawAccount, withdrawName);
                    withdrawCall.enqueue(new Callback<WithdrawBean>() {
                        @Override
                        public void onResponse(Call<WithdrawBean> call, Response<WithdrawBean> response) {
                            WithdrawBean bean = response.body();
                            if(bean != null && bean.getResponseCode() == 1){
                                Toast.makeText(MyAccountActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                withdrawialog.dismiss();
                                //刷新余额 提现详情
                                LoadDialog.dismiss(MyAccountActivity.this);
                                init();
//                                withDrawDetail();
                            }else{
                                Toast.makeText(MyAccountActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                LoadDialog.dismiss(MyAccountActivity.this);
                            }
                        }

                        @Override
                        public void onFailure(Call<WithdrawBean> call, Throwable t) {

                        }
                    });
                }

            }
        });
//        可提现金额
        String moneyStr = new String("可提现"+balanceStr +"元");
        SpannableString ss = new SpannableString(moneyStr);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_money.setHint(new SpannedString(ss));
        tv_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        tv_money.setText(s);
                        tv_money.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    tv_money.setText(s);
                    tv_money.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        tv_money.setText(s.subSequence(0, 1));
                        tv_money.setSelection(1);
                        return;
                    }
                }

                if (!s.toString().equals("") && Double.valueOf(s.toString()) > balance){
                    tv_reminder.setVisibility(View.VISIBLE);
                }else{
                    tv_reminder.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String please_account = "请输入支付宝账户";
        SpannableString ss1 = new SpannableString(please_account);
        AbsoluteSizeSpan ass1 = new AbsoluteSizeSpan(12,true);//设置字体大小 true表示单位是sp
        ss1.setSpan(ass1, 0, ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_account.setHint(new SpannedString(ss1));

        String please_name = "请输入真实姓名";
        SpannableString ss2 = new SpannableString(please_name);
        AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(12,true);//设置字体大小 true表示单位是sp
        ss2.setSpan(ass2, 0, ss2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_name.setHint(new SpannedString(ss2));


        withdrawialog.setContentView(contentView);
        withdrawialog.show();

    }

    private void withDrawDetail() {
        Intent intent = new Intent(MyAccountActivity.this,WithdrawDetailActivity.class);
        startActivity(intent);
    }

    private void recharge() {
        final Dialog rechargeDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_recharge, null);
        ImageView cacel = (ImageView) contentView.findViewById(R.id.dialog_recharge_cancel);
        TextView infomation = (TextView) contentView.findViewById(R.id.dialog_recharge_information);
        final CheckBox cb_10 = (CheckBox) contentView.findViewById(R.id.dialog_recharge_8);
        final CheckBox cb_20 = (CheckBox) contentView.findViewById(R.id.dialog_recharge_18);
        final CheckBox cb_40 = (CheckBox) contentView.findViewById(R.id.dialog_recharge_40);
        final CheckBox cb_60 = (CheckBox) contentView.findViewById(R.id.dialog_recharge_60);
        final CheckBox cb_98 = (CheckBox) contentView.findViewById(R.id.dialog_recharge_98);
        final CheckBox cb_128 = (CheckBox) contentView.findViewById(R.id.dialog_recharge_128);
        final TextView tv_dubi_8 = (TextView) contentView.findViewById(R.id.dialog_recharge_dubi_8);
        final TextView tv_dubi_18 = (TextView) contentView.findViewById(R.id.dialog_recharge_dubi_18);
        final TextView tv_dubi_40 = (TextView) contentView.findViewById(R.id.dialog_recharge_dubi_40);
        final TextView tv_dubi_60 = (TextView) contentView.findViewById(R.id.dialog_recharge_dubi_60);
        final TextView tv_dubi_98 = (TextView) contentView.findViewById(R.id.dialog_recharge_dubi_98);
        final TextView tv_dubi_128 = (TextView) contentView.findViewById(R.id.dialog_recharge_dubi_128);
        final TextView tv_money_8 = (TextView) contentView.findViewById(R.id.dialog_recharge_money_8);
        final TextView tv_money_18 = (TextView) contentView.findViewById(R.id.dialog_recharge_money_18);
        final TextView tv_money_40 = (TextView) contentView.findViewById(R.id.dialog_recharge_money_40);
        final TextView tv_money_60 = (TextView) contentView.findViewById(R.id.dialog_recharge_money_60);
        final TextView tv_money_98 = (TextView) contentView.findViewById(R.id.dialog_recharge_money_98);
        final TextView tv_money_128 = (TextView) contentView.findViewById(R.id.dialog_recharge_money_128);

        TextView confirm = (TextView) contentView.findViewById(R.id.dialog_recharge_confirm);
        cbList = new ArrayList<>();
        cbList.add(cb_10);
        cbList.add(cb_20);
        cbList.add(cb_40);
        cbList.add(cb_60);
        cbList.add(cb_98);
        cbList.add(cb_128);

        infomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog detailDialog = new Dialog(MyAccountActivity.this,R.style.myDialogTheme);
                View contentView = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.view_dialog_account_detail, null);
                detailDialog.setContentView(contentView);
                detailDialog.show();
            }
        });


        cb_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_10.isChecked()){
                    tv_dubi_8.setTextColor(getResources().getColor(R.color.blue));
                    tv_money_8.setTextColor(getResources().getColor(R.color.blue));
                    tv_dubi_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_128.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_128.setTextColor(getResources().getColor(R.color.gray));
                    cb_20.setChecked(false);
                    cb_40.setChecked(false);
                    cb_60.setChecked(false);
                    cb_98.setChecked(false);
                    cb_128.setChecked(false);
                }else{
                    tv_dubi_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_8.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_20.isChecked()){
                    tv_dubi_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_18.setTextColor(getResources().getColor(R.color.blue));
                    tv_money_18.setTextColor(getResources().getColor(R.color.blue));
                    tv_dubi_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_128.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_128.setTextColor(getResources().getColor(R.color.gray));
                    cb_10.setChecked(false);
                    cb_40.setChecked(false);
                    cb_60.setChecked(false);
                    cb_98.setChecked(false);
                    cb_128.setChecked(false);
                }else{
                    tv_dubi_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_18.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_40.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_40.isChecked()){
                    tv_dubi_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_40.setTextColor(getResources().getColor(R.color.blue));
                    tv_money_40.setTextColor(getResources().getColor(R.color.blue));
                    tv_dubi_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_128.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_128.setTextColor(getResources().getColor(R.color.gray));
                    cb_20.setChecked(false);
                    cb_10.setChecked(false);
                    cb_60.setChecked(false);
                    cb_98.setChecked(false);
                    cb_128.setChecked(false);
                }else{
                    tv_dubi_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_40.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_60.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_60.isChecked()){
                    tv_dubi_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_60.setTextColor(getResources().getColor(R.color.blue));
                    tv_money_60.setTextColor(getResources().getColor(R.color.blue));
                    tv_dubi_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_128.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_128.setTextColor(getResources().getColor(R.color.gray));
                    cb_20.setChecked(false);
                    cb_40.setChecked(false);
                    cb_10.setChecked(false);
                    cb_98.setChecked(false);
                    cb_128.setChecked(false);
                }else{
                    tv_dubi_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_60.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_98.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_98.isChecked()){
                    tv_dubi_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_98.setTextColor(getResources().getColor(R.color.blue));
                    tv_money_98.setTextColor(getResources().getColor(R.color.blue));
                    tv_dubi_128.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_128.setTextColor(getResources().getColor(R.color.gray));
                    cb_20.setChecked(false);
                    cb_40.setChecked(false);
                    cb_60.setChecked(false);
                    cb_10.setChecked(false);
                    cb_128.setChecked(false);
                }else{
                    tv_dubi_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_98.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_128.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_128.isChecked()){
                    tv_dubi_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_dubi_128.setTextColor(getResources().getColor(R.color.blue));
                    tv_money_128.setTextColor(getResources().getColor(R.color.blue));
                    cb_20.setChecked(false);
                    cb_40.setChecked(false);
                    cb_60.setChecked(false);
                    cb_98.setChecked(false);
                    cb_10.setChecked(false);
                }else{
                    tv_dubi_128.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_128.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });


        cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechargeDialog.dismiss();
            }
        });
        infomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog detailDialog = new Dialog(MyAccountActivity.this,R.style.myDialogTheme);
                View contentView = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.view_dialog_account_detail, null);
                detailDialog.setContentView(contentView);
                detailDialog.show();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            float rechargeprice = 0f;

            @Override
            public void onClick(View v) {
                for (CheckBox cb : cbList) {
                    if (cb.isChecked()) {
                        if (cb.equals(cb_10)){
                            rechargeprice = 8.0f;
                        }else if (cb.equals(cb_20)){
                            rechargeprice = 18.0f;
                        }else if (cb.equals(cb_40)){
                            rechargeprice = 40.0f;
                        }else if (cb.equals(cb_60)){
                            rechargeprice = 60.0f;
                        }else if (cb.equals(cb_98)){
                            rechargeprice = 98.0f;
                        }else if (cb.equals(cb_128)){
                            rechargeprice = 198.0f;
                        }
//                        rechargeprice = Float.valueOf(cb.getText().toString().replace("¥", ""));
                       //充值
                        Call<ApplyBean> applyBeanCall = ReaderRetroift.getInstance(MyAccountActivity.this).getApi().applyMoney(rechargeprice);
                        applyBeanCall.enqueue(new Callback<ApplyBean>() {
                            @Override
                            public void onResponse(Call<ApplyBean> call, Response<ApplyBean> response) {
                                applyBean = response.body();
                                if (applyBean != null && applyBean.getResponseCode() == 1){
                                    apply();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApplyBean> call, Throwable t) {

                            }
                        });
                    }
                }
                if(rechargeprice == 0){
                    Toast.makeText(MyAccountActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                rechargeDialog.dismiss();
            }
        });

        rechargeDialog.setContentView(contentView);
        android.view.WindowManager.LayoutParams p = rechargeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        rechargeDialog.getWindow().setAttributes(p);
        rechargeDialog.show();
    }

    private void apply() {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MyAccountActivity.this);
                Map<String, String> result = alipay.payV2(applyBean.getResponseData().getRequestParams(),true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(MyAccountActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        init();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(MyAccountActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        init();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(MyAccountActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(MyAccountActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    @Override
    public void onRefresh() {
        //刷新方法
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myAccountRefresh.setRefreshing(false);
            }
        },3000);
    }
}
