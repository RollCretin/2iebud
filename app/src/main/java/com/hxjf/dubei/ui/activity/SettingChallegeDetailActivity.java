package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ApplyBean;
import com.hxjf.dubei.bean.BalanceBean;
import com.hxjf.dubei.bean.ChallengeNumTimeBean;
import com.hxjf.dubei.bean.LauncherChallengeBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.network.PayResult;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.DensityUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/17.
 */

public class SettingChallegeDetailActivity extends BaseActivity {
    private static final int FLAG_STARTTIME = 1;
    private static final int FLAG_ENDTIME = 2;
    private static final String TAG = "SettingChallegeDetail";
    private static final int SDK_PAY_FLAG = 4;
    @BindView(R.id.setting_challege_bg)
    ImageView settingChallegeBg;
    @BindView(R.id.setting_challege_back)
    ImageView settingChallegeBack;
    @BindView(R.id.setting_challege_book_name)
    TextView settingChallegeBookName;
    @BindView(R.id.setting_challege_rl_book_name)
    RelativeLayout settingChallegeRlBookName;
    @BindView(R.id.setting_challege_pay_price)
    TextView settingChallegePayPrice;
    @BindView(R.id.setting_challege_rl_pay_price)
    RelativeLayout settingChallegeRlPayPrice;
    @BindView(R.id.setting_challege_font_num)
    TextView settingChallegeFontNum;
    @BindView(R.id.setting_challege_hour)
    TextView settingChallegeHour;
    @BindView(R.id.setting_challege_checkbox)
    CheckBox settingChallegeCheckbox;
    @BindView(R.id.setting_challege_rule)
    TextView settingChallegeRule;
    @BindView(R.id.setting_challege_launch_challege)
    Button settingChallegeLaunchChallege;
    @BindView(R.id.setting_challege_starttime)
    TextView settingChallegeStarttime;
    @BindView(R.id.setting_challege_endtime)
    TextView settingChallegeEndtime;
    @BindView(R.id.setting_challege_rl_time)
    RelativeLayout settingChallegeRlTime;
    @BindView(R.id.setting_challege_cancel)
    TextView settingChallegeCancel;
    private String bookId;
    private int num;
    private int theoryReadTime;
    private String bookName;
    private ArrayList<CheckBox> cbList;
    private Double money = 0.0;
    private Dialog startDialog;
    private Dialog enddialog;
    private Double doubleMoney;
    private double accountMoney;
    private ApplyBean applyBean;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_seeting_challege_detail;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //状态栏沉浸
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        bookName = intent.getStringExtra("bookName");
        settingChallegeCheckbox.setChecked(true);
        //判断这个书是否可以挑战
        Call<ModifyBean> judgeChallenge = ReaderRetroift.getInstance(this).getApi().judgeChallenge(bookId);
        judgeChallenge.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null && bean.getResponseCode() == 0) {
                    Toast.makeText(SettingChallegeDetailActivity.this, "对不起，您已经发起或者参与了此本书籍的挑战，您可以选择其他书籍发起阅读挑战", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
        //查询账户余额
        Call<BalanceBean> balanceCall = ReaderRetroift.getInstance(SettingChallegeDetailActivity.this).getApi().getBalanceCall();
        balanceCall.enqueue(new Callback<BalanceBean>() {
            @Override
            public void onResponse(Call<BalanceBean> call, Response<BalanceBean> response) {
                BalanceBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    accountMoney = bean.getResponseData().getAccountMoney();
                }
            }

            @Override
            public void onFailure(Call<BalanceBean> call, Throwable t) {

            }
        });

        Call<ChallengeNumTimeBean> challengNumTimeCall = ReaderRetroift.getInstance(this).getApi().ChallengNumTimeCall(bookId);
        challengNumTimeCall.enqueue(new Callback<ChallengeNumTimeBean>() {

            @Override
            public void onResponse(Call<ChallengeNumTimeBean> call, Response<ChallengeNumTimeBean> response) {
                ChallengeNumTimeBean bean = response.body();
                if (bean != null) {
                    ChallengeNumTimeBean.ResponseDataBean responseData = bean.getResponseData();
                    //字数
                    num = responseData.getNum();
                    //理论阅读时间
                    theoryReadTime = responseData.getTheoryReadTime();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<ChallengeNumTimeBean> call, Throwable t) {

            }
        });
    }

    private void initData() {
        startdialog(FLAG_STARTTIME);
        enddialog(FLAG_ENDTIME);
        settingChallegeBookName.setText(bookName);
        float newNumber = (float) num / 10000;
        DecimalFormat df = new DecimalFormat("######0.00");
        settingChallegeFontNum.setText("书籍" + df.format(newNumber) + "万字 预计需连续阅读");
        int newHour = theoryReadTime / 3600;
        int newMinute = (theoryReadTime - (newHour * 3600)) / 60;
        settingChallegeHour.setText(newHour + "小时" + newMinute + "分钟");
    }

    private void dialogMoney() {
        //设置金额
        final Dialog payDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_pay, null);
        Button pay = (Button) contentView.findViewById(R.id.btn_dialog_pay);
        final CheckBox pay_20 = (CheckBox) contentView.findViewById(R.id.dialog_pay_20);
        final CheckBox pay_30 = (CheckBox) contentView.findViewById(R.id.dialog_pay_30);
        final CheckBox pay_40 = (CheckBox) contentView.findViewById(R.id.dialog_pay_40);
        final CheckBox pay_50 = (CheckBox) contentView.findViewById(R.id.dialog_pay_50);
        final CheckBox pay_60 = (CheckBox) contentView.findViewById(R.id.dialog_pay_60);
        final CheckBox pay_70 = (CheckBox) contentView.findViewById(R.id.dialog_pay_70);

        cbList = new ArrayList<>();
        cbList.add(pay_20);
        cbList.add(pay_30);
        cbList.add(pay_40);
        cbList.add(pay_50);
        cbList.add(pay_60);
        cbList.add(pay_70);
        pay_20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pay_20.isChecked()) {
                    pay_30.setChecked(false);
                    pay_40.setChecked(false);
                    pay_50.setChecked(false);
                    pay_60.setChecked(false);
                    pay_70.setChecked(false);
                }
            }
        });
        pay_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pay_30.isChecked()) {
                    pay_20.setChecked(false);
                    pay_40.setChecked(false);
                    pay_50.setChecked(false);
                    pay_60.setChecked(false);
                    pay_70.setChecked(false);
                }
            }
        });
        pay_40.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pay_40.isChecked()) {
                    pay_20.setChecked(false);
                    pay_30.setChecked(false);
                    pay_50.setChecked(false);
                    pay_60.setChecked(false);
                    pay_70.setChecked(false);
                }
            }
        });
        pay_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pay_50.isChecked()) {
                    pay_20.setChecked(false);
                    pay_40.setChecked(false);
                    pay_30.setChecked(false);
                    pay_60.setChecked(false);
                    pay_70.setChecked(false);
                }
            }
        });
        pay_60.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pay_60.isChecked()) {
                    pay_30.setChecked(false);
                    pay_40.setChecked(false);
                    pay_50.setChecked(false);
                    pay_20.setChecked(false);
                    pay_70.setChecked(false);
                }
            }
        });
        pay_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pay_70.isChecked()) {
                    pay_30.setChecked(false);
                    pay_40.setChecked(false);
                    pay_50.setChecked(false);
                    pay_60.setChecked(false);
                    pay_20.setChecked(false);
                }
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CheckBox cb : cbList) {
                    if (cb.isChecked()) {
                        //TODO
                        String s = cb.getText().toString().replace("¥", "");
                        money = Double.valueOf(s);
                        settingChallegePayPrice.setText(cb.getText());
                        Drawable drawable= getResources().getDrawable(R.mipmap.dubi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        settingChallegePayPrice.setCompoundDrawables(drawable,null,null,null);
                        settingChallegePayPrice.setCompoundDrawablePadding(5);
                        payDialog.dismiss();
                    }
                }
            }
        });
        payDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(params);

        payDialog.getWindow().setGravity(Gravity.BOTTOM);
        payDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        payDialog.show();

    }

    @OnClick({R.id.setting_challege_back, R.id.setting_challege_rl_book_name, R.id.setting_challege_rl_pay_price, R.id.setting_challege_rule, R.id.setting_challege_launch_challege, R.id.setting_challege_starttime, R.id.setting_challege_endtime, R.id.setting_challege_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_challege_back:
                finish();
                break;
            case R.id.setting_challege_rl_book_name:
                finish();
                Intent intent = new Intent(SettingChallegeDetailActivity.this, ChallegeBookActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_challege_rl_pay_price:
                dialogMoney();
                break;
            case R.id.setting_challege_rule:
                Intent ruleintent = new Intent(this,agreementActivity.class);
                ruleintent.putExtra("url","http://test1.17dubei.com/rule.html");
                startActivity(ruleintent);
                break;
            case R.id.setting_challege_launch_challege:
                launchChallenge();
                break;
            case R.id.setting_challege_starttime:
                startDialog.show();
                break;
            case R.id.setting_challege_endtime:
                enddialog.show();
                break;
            case R.id.setting_challege_cancel:
                //取消 返回到最初界面
                Intent mainIntent = new Intent(SettingChallegeDetailActivity.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                break;
        }
    }

    private void launchChallenge() {
        String money = settingChallegePayPrice.getText().toString().replace("¥", "");
        String starttime = settingChallegeStarttime.getText().toString().replace("/", "");
        String endtime = settingChallegeEndtime.getText().toString().replace("/", "");
        if (money.length() == 0) {
            Toast.makeText(this, "请设置挑战金", Toast.LENGTH_SHORT).show();
        } else if (starttime.length() == 0) {
            Toast.makeText(this, "请设置开始时间", Toast.LENGTH_SHORT).show();
        } else if (endtime.length() == 0) {
            Toast.makeText(this, "请设置结束时间", Toast.LENGTH_SHORT).show();
        } else if (!settingChallegeCheckbox.isChecked()) {
            Toast.makeText(this, "挑战需同意挑战规则", Toast.LENGTH_SHORT).show();
        } else {
            doubleMoney = Double.valueOf(money);

            Call<LauncherChallengeBean> launcherChallengeCall = ReaderRetroift.getInstance(this).getApi().LauncherChallengeCall(bookId, starttime, endtime, doubleMoney);
            launcherChallengeCall.enqueue(new Callback<LauncherChallengeBean>() {
                @Override
                public void onResponse(Call<LauncherChallengeBean> call, Response<LauncherChallengeBean> response) {
                    LauncherChallengeBean bean = response.body();
                    if (bean != null && bean.getResponseCode() == 1) {
                        //发布成功再调用支付
                        payChallenge(bean.getResponseData());

                    } else if (bean.getResponseCode() == 0) {
                        Toast.makeText(SettingChallegeDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LauncherChallengeBean> call, Throwable t) {

                }
            });

        }
    }

    private void payChallenge(final LauncherChallengeBean.ResponseDataBean bean) {
        //挑战金dialog
        final Dialog payChallengeDialog = new Dialog(this, R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_paychallenge, null);
        ImageView iv_cancle = (ImageView) contentView.findViewById(R.id.dialog_paychallenge_cancel);
        TextView tv_money = (TextView) contentView.findViewById(R.id.dialog_paychallenge_money);
        TextView tv_balance = (TextView) contentView.findViewById(R.id.dialog_paychallenge_balance);
        TextView tv_bookname = (TextView) contentView.findViewById(R.id.dialog_paychallenge_bookname);
        TextView tv_time = (TextView) contentView.findViewById(R.id.dialog_paychallenge_time);
        final TextView tv_confirm = (TextView) contentView.findViewById(R.id.dialog_paychallenge_confirm);
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payChallengeDialog.dismiss();
            }
        });
        DecimalFormat df = new DecimalFormat("######0.00");
        String formatMoney = df.format(doubleMoney);
        tv_money.setText("" + formatMoney);
        tv_bookname.setText(bean.getBookInfo().getName());
        String startTime = settingChallegeStarttime.getText().toString().replace("/", ".");
        String endtime = settingChallegeEndtime.getText().toString().replace("/", ".");
        tv_time.setText(startTime + "~" + endtime);
        DecimalFormat df1 = new DecimalFormat("######0.00");
        String account = df1.format(accountMoney);
        tv_balance.setText("账户读币：" + account);
        if (accountMoney < doubleMoney) {
            tv_balance.setTextColor(getResources().getColor(R.color.red));
            tv_confirm.setText("支付宝充值");
        } else {
            tv_balance.setTextColor(getResources().getColor(R.color.gray));
        }
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountMoney >= doubleMoney) {
                    payChallengeDialog.dismiss();
                    final ProgressDialog progressDialog = new ProgressDialog(SettingChallegeDetailActivity.this);
                    progressDialog.setTitle(null);
                    progressDialog.setMessage("正在支付...");
                    progressDialog.show();
                    Call<ModifyBean> payChallenge = ReaderRetroift.getInstance(SettingChallegeDetailActivity.this).getApi().payChallenge(bean.getId(), money);
                    payChallenge.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean1 = response.body();
                            if (bean1 != null && bean1.getResponseCode() == 1) {
                                Intent intent = new Intent(SettingChallegeDetailActivity.this, SettingChallengeSuccessActivity.class);
                                intent.putExtra("dataBean", bean);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {

                        }
                    });
                } else {
                    //账户余额不足
                    payChallengeDialog.dismiss();
                    //充值窗口
                    recharge();
                }

            }
        });

        payChallengeDialog.setContentView(contentView);
        payChallengeDialog.show();

        WindowManager m = SettingChallegeDetailActivity.this.getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = payChallengeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
        payChallengeDialog.getWindow().setAttributes(p); //设置生效

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
        final ArrayList<CheckBox> cbList = new ArrayList<>();
        cbList.add(cb_10);
        cbList.add(cb_20);
        cbList.add(cb_40);
        cbList.add(cb_60);
        cbList.add(cb_98);
        cbList.add(cb_128);

        infomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog detailDialog = new Dialog(SettingChallegeDetailActivity.this,R.style.myDialogTheme);
                View contentView = LayoutInflater.from(SettingChallegeDetailActivity.this).inflate(R.layout.view_dialog_account_detail, null);
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
                        //充值
                        Call<ApplyBean> applyBeanCall = ReaderRetroift.getInstance(SettingChallegeDetailActivity.this).getApi().applyMoney(rechargeprice);
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
                    Toast.makeText(SettingChallegeDetailActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                rechargeDialog.dismiss();
            }
        });

        rechargeDialog.setContentView(contentView);
        rechargeDialog.show();
    }

    private void apply() {
        //调用支付宝支付
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(SettingChallegeDetailActivity.this);
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
                        Toast.makeText(SettingChallegeDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(SettingChallegeDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    private void startdialog(final int flag) {
        startDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_calendar, null);
        final TextView mTitle = (TextView) contentView.findViewById(R.id.title);
        final TextView confirm = (TextView) contentView.findViewById(R.id.dialog_calendar_confirm);

        CalendarDateView mCalendarDateView = (CalendarDateView) contentView.findViewById(R.id.calendarDateView);
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DensityUtil.dip2px(SettingChallegeDetailActivity.this, 48), DensityUtil.dip2px(SettingChallegeDetailActivity.this, 48));
                    convertView.setLayoutParams(params);
                }

                view = (TextView) convertView.findViewById(R.id.text);

                view.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    view.setTextColor(0xff9299a1);
                } else {
                    view.setTextColor(0xffffffff);
                }

                return convertView;
            }
        });
        int[] data = CalendarUtil.getYMD(new Date());
        mTitle.setText(data[0] + "/" + getDisPlayNumber(data[1]) + "/" + getDisPlayNumber(data[2]));
        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                mTitle.setText(bean.year + "/" + getDisPlayNumber(bean.moth) + "/" + getDisPlayNumber(bean.day));
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = mTitle.getText().toString();
                if (flag == FLAG_STARTTIME) {
                    //检查时间
                    boolean isweek = checksatrtData(start);
                    if (isweek) {
                        settingChallegeStarttime.setText(start);
                        startDialog.dismiss();
                    }
                } else {
                    boolean isMonth = checkendData(settingChallegeStarttime.getText().toString(), start);
                    if (isMonth) {
                        settingChallegeEndtime.setText(start);
                        startDialog.dismiss();
                    }
                }
            }
        });

        startDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(params);

        startDialog.getWindow().setGravity(Gravity.BOTTOM);
        startDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

    }

    private void enddialog(final int flag) {
        enddialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_calendar, null);
        final TextView mTitle = (TextView) contentView.findViewById(R.id.title);
        final TextView confirm = (TextView) contentView.findViewById(R.id.dialog_calendar_confirm);

        CalendarDateView mCalendarDateView = (CalendarDateView) contentView.findViewById(R.id.calendarDateView);
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DensityUtil.dip2px(SettingChallegeDetailActivity.this, 48), DensityUtil.dip2px(SettingChallegeDetailActivity.this, 48));
                    convertView.setLayoutParams(params);
                }

                view = (TextView) convertView.findViewById(R.id.text);

                view.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    view.setTextColor(0xff9299a1);
                } else {
                    view.setTextColor(0xffffffff);
                }

                return convertView;
            }
        });
        int[] data = CalendarUtil.getYMD(new Date());
        mTitle.setText(data[0] + "/" + getDisPlayNumber(data[1]) + "/" + getDisPlayNumber(data[2]));
        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                mTitle.setText(bean.year + "/" + getDisPlayNumber(bean.moth) + "/" + getDisPlayNumber(bean.day));
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = mTitle.getText().toString();
                if (flag == FLAG_STARTTIME) {
                    //检查时间
                    boolean isweek = checksatrtData(start);
                    if (isweek) {
                        settingChallegeStarttime.setText(start);
                        enddialog.dismiss();
                    }
                } else {
                    boolean isMonth = checkendData(settingChallegeStarttime.getText().toString(), start);
                    if (isMonth) {
                        settingChallegeEndtime.setText(start);
                        enddialog.dismiss();
                    }
                }
            }
        });

        enddialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(params);

        enddialog.getWindow().setGravity(Gravity.BOTTOM);
        enddialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    private boolean checksatrtData(String time) {
        //检查开始时间，是否在一周之内
        //获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        calendar.setTimeInMillis(currentTimeMillis);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String newStr = time.replace("/", "");
        try {
            Date parse = format.parse(newStr);
            long starttime = parse.getTime();
            int daycountNum = (int) ((starttime - currentTimeMillis) / (1000 * 60 * 60 * 24));//相差天数
            int hourcountNum = (int) ((starttime - currentTimeMillis) / (1000 * 60 * 60) % 24);//相差天数
            if (daycountNum >= 7) {
                Toast.makeText(this, "开始时间在一周以内", Toast.LENGTH_SHORT).show();
                return false;
            } else if (daycountNum < 1) {
                if (daycountNum == 0 && hourcountNum > 0) {
                    return true;
                }
                Toast.makeText(this, "开始时间为第二天之后", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkendData(String starttime, String time) {
        //检查结束时间，是否一个月以内
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        calendar.setTimeInMillis(currentTimeMillis);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String newStr = time.replace("/", "");
        try {
            Date parse = format.parse(newStr);
            long endtime = parse.getTime();
            if (starttime == null) {
                int daycountNum = (int) ((endtime - currentTimeMillis) / (1000 * 60 * 60 * 24));//相差天数
                if (daycountNum > 30) {
                    Toast.makeText(this, "周期为一个月以内", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (daycountNum < 0) {
                    Toast.makeText(this, "时间已过", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                starttime = starttime.replace("/", "");
                Date parse1 = format.parse(starttime);
                long selectstarttime = parse1.getTime();
                int daycountNum = (int) ((endtime - selectstarttime) / (1000 * 60 * 60 * 24));//相差天数
                if (daycountNum > 30) {
                    Toast.makeText(this, "周期为一个月以内", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (daycountNum < 0) {
                    Toast.makeText(this, "时间已过", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
