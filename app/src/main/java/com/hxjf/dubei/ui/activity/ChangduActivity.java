package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ApplyBean;
import com.hxjf.dubei.bean.ChangDuParamBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.PayResult;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.TimeUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/10/30.
 */

public class ChangduActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "ChangduActivity";
    @BindView(R.id.changdu_detail_back)
    ImageView changduDetailBack;
    @BindView(R.id.changdu_detail_protrait)
    CircleImageView changduDetailProtrait;
    @BindView(R.id.changdu_detail_logo)
    ImageView changduDetailLogo;
    @BindView(R.id.changdu_detail_rl)
    RelativeLayout changduDetailRl;
    @BindView(R.id.changdu_detail_name)
    TextView changduDetailName;
    @BindView(R.id.changdu_detail_desc_1)
    TextView changduDetailDesc1;
    @BindView(R.id.changdu_detail_desc_2)
    TextView changduDetailDesc2;
    @BindView(R.id.changdu_detail_open)
    Button changduDetailOpen;
    private UserDetailBean.ResponseDataBean responseDataBean;
    private ChangDuParamBean.ResponseDataBean changduparamDataBean;
    private ApplyBean applyBean;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_changdu;
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
        Call<UserDetailBean> myDetailCall = ReaderRetroift.getInstance(ChangduActivity.this).getApi().myDetailCall();
        myDetailCall.enqueue(new Callback<UserDetailBean>() {
            @Override
            public void onResponse(Call<UserDetailBean> call, Response<UserDetailBean> response) {
                UserDetailBean detailBean = response.body();
                if (detailBean != null && detailBean.getResponseData() != null){
                    responseDataBean = detailBean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<UserDetailBean> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
            }
        });

        changduDetailOpen.setClickable(false);
        //获取畅读卡类型以及单价
        Call<ChangDuParamBean> changDuParamCallCall = ReaderRetroift.getInstance(ChangduActivity.this).getApi().changDuParamCall("vipType");
        changDuParamCallCall.enqueue(new Callback<ChangDuParamBean>() {
            @Override
            public void onResponse(Call<ChangDuParamBean> call, Response<ChangDuParamBean> response) {
                ChangDuParamBean changDuParamBean = response.body();
                if (changDuParamBean !=  null && changDuParamBean.getResponseData() != null){
                    List<ChangDuParamBean.ResponseDataBean> changduparamList = changDuParamBean.getResponseData();
                    changduDetailOpen.setClickable(true);
                    changduparamDataBean = changduparamList.get(0);
                }
            }

            @Override
            public void onFailure(Call<ChangDuParamBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        Glide.with(this).load(ReaderRetroift.IMAGE_URL+responseDataBean.getNormalPath()).into(changduDetailProtrait);
        changduDetailName.setText(responseDataBean.getNickName());
        if (responseDataBean.getVipStatus() == 2){
            //已开通
            changduDetailLogo.setVisibility(View.VISIBLE);
            String vipOverTime = responseDataBean.getVipOverTime();
            String newTime = vipOverTime.replace("-", "");
            String[] difference2 = TimeUtil.getDifference2(newTime);
            changduDetailDesc1.setText("您的畅读卡"+vipOverTime.replace("-", ".")+"到期");
            changduDetailDesc2.setText("剩余"+difference2[0]+"天到期");
            changduDetailOpen.setText("续费");
            if (Integer.valueOf(difference2[0])>10){
                changduDetailOpen.setVisibility(View.GONE);
            }else{
                changduDetailOpen.setVisibility(View.VISIBLE);
            }
        }else{
            //未开通
            changduDetailLogo.setVisibility(View.GONE);
            changduDetailDesc1.setText("暂未开通畅读卡服务");
            changduDetailDesc2.setText("未开通畅读卡");
            changduDetailOpen.setText("开通");
        }

    }


    @OnClick({R.id.changdu_detail_back, R.id.changdu_detail_open})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.changdu_detail_back:
                finish();
                break;
            case R.id.changdu_detail_open:
                openOrRenew();
                break;
        }
    }

    private void openOrRenew() {
        final Dialog openChangduDialog = new Dialog(ChangduActivity.this, R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(ChangduActivity.this).inflate(R.layout.view_dialog_open_changdu, null);
        ImageView iv_cancel = (ImageView) contentView.findViewById(R.id.dialog_open_changdu_cancel);
        TextView tv_understand = (TextView) contentView.findViewById(R.id.dialog_open_changdu_understand);
        TextView tv_money = (TextView) contentView.findViewById(R.id.dialog_paychallenge_money);
        TextView tv_buy = (TextView) contentView.findViewById(R.id.dialog_open_changdu_buy);
        TextView tv_free = (TextView) contentView.findViewById(R.id.dialog_open_changdu_free);
        TextView tv_hint = (TextView) contentView.findViewById(R.id.dialog_open_changdu_hint);
        tv_hint.setVisibility(View.INVISIBLE);
        String value = changduparamDataBean.getValue();
        final double paymoney = Double.valueOf(value);
        DecimalFormat df = new DecimalFormat("######0.00");
        String formatMoney = df.format(paymoney);
        tv_money.setText(formatMoney);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangduDialog.dismiss();
            }
        });
        /*tv_understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangduDialog.dismiss();
                Intent changduIntent = new Intent(ChangduActivity.this,ChangduActivity.class);
                startActivity(changduIntent);
            }
        });*/
        tv_understand.setVisibility(View.GONE);
        tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangduDialog.dismiss();
                payChangDu();
            }
        });
        tv_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openChangduDialog.dismiss();
                final Dialog freeOpenDialog = new Dialog(ChangduActivity.this, R.style.myDialogTheme);
                final View contentView = LayoutInflater.from(ChangduActivity.this).inflate(R.layout.view_dialog_freeopen, null);
                ImageView iv_cancel = (ImageView) contentView.findViewById(R.id.dialog_freeopen_cancel);
                final TextView tv_des = (TextView) contentView.findViewById(R.id.dialog_freeopen_des);
                final TextView tv_weixin = (TextView) contentView.findViewById(R.id.dialog_freeopen_weixin);
                final TextView tv_openweixin = (TextView) contentView.findViewById(R.id.dialog_freeopen_openweixin);
                tv_weixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager c = (ClipboardManager) ChangduActivity.this.getSystemService(CLIPBOARD_SERVICE);
                        c.setText("读呗APP");
                        tv_des.setText("文字内容已复制，赶紧去微信公众号关注我们吧");
                        tv_openweixin.setVisibility(View.VISIBLE);
                        tv_weixin.setVisibility(View.GONE);
                    }
                });
                iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        freeOpenDialog.dismiss();
                    }
                });
                tv_openweixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到微信
                        /*String appId = "";//开发者平台ID
                        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId, false);

                        if (api.isWXAppInstalled()) {
                            JumpToBizProfile.Req req = new JumpToBizProfile.Req();
                            req.toUserName = "wx360c414d6abef40f"; // 公众号原始ID
                            req.extMsg = "";
                            req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE; // 普通公众号
                            api.sendReq(req);
                        }else{
                            Toast.makeText(getActivity(), "微信未安装", Toast.LENGTH_SHORT).show();
                        }*/
                        try {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");

                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(cmp);
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
// TODO: handle exception
                            Toast.makeText(ChangduActivity.this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                freeOpenDialog.setContentView(contentView);
                freeOpenDialog.show();
                WindowManager m = ChangduActivity.this.getWindowManager();
                Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
                android.view.WindowManager.LayoutParams p = freeOpenDialog.getWindow().getAttributes(); //获取对话框当前的参数值
                p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
                freeOpenDialog.getWindow().setAttributes(p); //设置生效

            }
        });
        openChangduDialog.setContentView(contentView);
        openChangduDialog.show();
    }

    private void payChangDu() {

        final Dialog payChallengeDialog = new Dialog(ChangduActivity.this, R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(ChangduActivity.this).inflate(R.layout.view_dialog_paychallenge, null);
        ImageView iv_cancle = (ImageView) contentView.findViewById(R.id.dialog_paychallenge_cancel);
        TextView tv_title = (TextView) contentView.findViewById(R.id.dialog_paychallenge_title);
        tv_title.setText("畅读卡");
        TextView tv_money = (TextView) contentView.findViewById(R.id.dialog_paychallenge_money);
        TextView tv_balance = (TextView) contentView.findViewById(R.id.dialog_paychallenge_balance);
        TextView tv_name = (TextView) contentView.findViewById(R.id.dialog_paychallenge_name);
        tv_name.setText("名称：");
        TextView tv_bookname = (TextView) contentView.findViewById(R.id.dialog_paychallenge_bookname);
        TextView tv_timetitle = (TextView) contentView.findViewById(R.id.dialog_paychallenge_time_title);
        tv_timetitle.setText("畅读时间：");
        TextView tv_time = (TextView) contentView.findViewById(R.id.dialog_paychallenge_time);
        final TextView tv_confirm = (TextView) contentView.findViewById(R.id.dialog_paychallenge_confirm);
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payChallengeDialog.dismiss();
            }
        });
        String value = changduparamDataBean.getValue();
        final double paymoney = Double.valueOf(value);
        DecimalFormat df = new DecimalFormat("######0.00");
        String formatMoney = df.format(paymoney);
        tv_money.setText("" + formatMoney);
        tv_bookname.setText("开通畅读卡服务");
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        calendar.setTimeInMillis(currentTimeMillis);
        //当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.");
        String today = format.format(calendar.getTime());
        calendar1.add(Calendar.DAY_OF_YEAR,365);
        String afteryear = format.format(calendar1.getTime());

        tv_time.setText(today + "~" +afteryear);
        DecimalFormat df1 = new DecimalFormat("######0.00");
        
        String account = df1.format(responseDataBean.getAccountMoney());
        tv_balance.setText("账户读币：" + account);
        if (responseDataBean.getAccountMoney() < paymoney) {
            tv_balance.setTextColor(getResources().getColor(R.color.red));
            tv_confirm.setText("支付宝充值");
        } else {
            tv_balance.setTextColor(getResources().getColor(R.color.gray));
        }
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responseDataBean.getAccountMoney() >= paymoney) {
                    payChallengeDialog.dismiss();
                    final ProgressDialog progressDialog = new ProgressDialog(ChangduActivity.this);
                    progressDialog.setTitle(null);
                    progressDialog.setMessage("正在支付...");
                    progressDialog.show();
                    //支付畅读卡
                    Call<ModifyBean> buyChangDuCall = ReaderRetroift.getInstance(ChangduActivity.this).getApi().buyChangDuCall(changduparamDataBean.getCode());
                    buyChangDuCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                if (bean.getResponseCode() == 1) {
                                    Toast.makeText(ChangduActivity.this, bean.getResponseMsg(), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                    init();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {
                            progressDialog.dismiss();
                        }
                    });
                }else{
                    //账户余额不足
                    payChallengeDialog.dismiss();
                    //充值窗口
                    recharge();
                }

            }
        });
        payChallengeDialog.setContentView(contentView);
        payChallengeDialog.show();

        WindowManager m = ChangduActivity.this.getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = payChallengeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
        payChallengeDialog.getWindow().setAttributes(p); //设置生效

    }

    private void recharge() {
        final Dialog rechargeDialog = new Dialog(ChangduActivity.this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(ChangduActivity.this).inflate(R.layout.view_dialog_recharge, null);
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
                Dialog detailDialog = new Dialog(ChangduActivity.this,R.style.myDialogTheme);
                View contentView = LayoutInflater.from(ChangduActivity.this).inflate(R.layout.view_dialog_account_detail, null);
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
                        Call<ApplyBean> applyBeanCall = ReaderRetroift.getInstance(ChangduActivity.this).getApi().applyMoney(rechargeprice);
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
                    Toast.makeText(ChangduActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                rechargeDialog.dismiss();
            }
        });

        rechargeDialog.setContentView(contentView);
        rechargeDialog.show();

        WindowManager m = ChangduActivity.this.getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = rechargeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
        rechargeDialog.getWindow().setAttributes(p); //设置生效
    }

    private void apply() {
        //调用支付宝支付
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ChangduActivity.this);
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
                        Toast.makeText(ChangduActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ChangduActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
}
