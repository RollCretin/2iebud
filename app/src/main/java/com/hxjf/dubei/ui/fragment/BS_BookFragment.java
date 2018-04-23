package com.hxjf.dubei.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BSBookAdapter;
import com.hxjf.dubei.bean.ApplyBean;
import com.hxjf.dubei.bean.BSBookListBean;
import com.hxjf.dubei.bean.BalanceBean;
import com.hxjf.dubei.bean.ChangDuParamBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.network.EnhancedCall;
import com.hxjf.dubei.network.EnhancedCallback;
import com.hxjf.dubei.network.PayResult;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.ChangduActivity;
import com.hxjf.dubei.ui.activity.LoginActivity;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.widget.DownloadProgressDialog;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.geometerplus.android.fbreader.FBReader;

import java.io.File;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Chen_Zhang on 2017/6/12.
 * 书架-书籍模块
 */

public class BS_BookFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int REFRESH_COMPLETE = 100;
    private static final String TAG = "BS_BookFragment";
    private static final int SDK_PAY_FLAG = 1;


    ArrayList<String> book_list;
    BSBookAdapter myBookAdapter;
    View view;
    @BindView(R.id.bs_gv_book)
    GridView bsGvBook;
    @BindView(R.id.bs_refresh)
    SwipeRefreshLayout bsRefresh;
    @BindView(R.id.bs_tv_empty_book)
    TextView bsTvEmptyBook;
    @BindView(R.id.bs_book_nonet)
    TextView bsBookNonet;

    private List<BSBookListBean.ResponseDataBean> responseDataList;
    private BSBookListBean.ResponseDataBean responseData;
    private DownloadProgressDialog downloaddialog;
    private ChangDuParamBean.ResponseDataBean changduparamDataBean;
    private double accountMoney;
    private String moneyvalue;
    private ApplyBean applyBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_bs_book, null);
        ButterKnife.bind(this, view);
        bsRefresh.setOnRefreshListener(this);
        bsRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        return view;
    }


    private void init() {
        bsRefresh.setRefreshing(true);
        Call<BSBookListBean> bsBookListCall = ReaderRetroift.getInstance(getContext()).getApi().BSBookListCall();
        EnhancedCall<BSBookListBean> bsBookListBeanEnhancedCall = new EnhancedCall<>(bsBookListCall);
        bsBookListBeanEnhancedCall.dataClassName(BSBookListBean.class).enqueue(new EnhancedCallback<BSBookListBean>() {
            @Override
            public void onResponse(Call<BSBookListBean> call, Response<BSBookListBean> response) {
                BSBookListBean bean = response.body();
                if (bean != null && bean.getResponseCode() == 2) {
                    SharedPreferences cookies_prefs = getActivity().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
                    cookies_prefs.edit().clear().commit();
                    SPUtils.remove(getActivity(), "bindbean");
                    //将栈中所有activity清空，跳到login页面
                    Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (bean != null && bean.getResponseCode() == 1) {
                    responseDataList = bean.getResponseData();
                    bsRefresh.setRefreshing(false);
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BSBookListBean> call, Throwable t) {
                bsRefresh.setRefreshing(false);
                Toast.makeText(getActivity(), "请检查网络..", Toast.LENGTH_LONG).show();
                bsBookNonet.setVisibility(View.VISIBLE);
                bsGvBook.setVisibility(View.GONE);
            }

            @Override
            public void onGetCache(BSBookListBean bsBookListBean) {
                Toast.makeText(getContext(), "请检查网络...", Toast.LENGTH_LONG).show();
                if (bsBookListBean != null && bsBookListBean.getResponseCode() == 2) {
                    SharedPreferences cookies_prefs = getActivity().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
                    cookies_prefs.edit().clear().commit();
                    SPUtils.remove(getActivity(), "bindbean");
                    //将栈中所有activity清空，跳到login页面
                    Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (bsBookListBean != null && bsBookListBean.getResponseCode() == 1) {
                    responseDataList = bsBookListBean.getResponseData();
                    bsRefresh.setRefreshing(false);
                    initData();
                }
            }
        });

    }


    private void initData() {
        if (responseDataList.size() == 0) {
            bsTvEmptyBook.setVisibility(View.VISIBLE);
        }
        if (responseDataList.size() != 0) {
            bsTvEmptyBook.setVisibility(View.GONE);
            bsGvBook.setVisibility(View.VISIBLE);

            myBookAdapter = new BSBookAdapter(bsGvBook, responseDataList, getContext());
            bsGvBook.setAdapter(myBookAdapter);
            bsGvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    responseData = responseDataList.get(position);
                    if (responseData != null && responseData.getBookInfo() != null && responseData.getBookInfo().getStatus() == 0) {
                        Toast.makeText(getActivity(), "该书已下架", Toast.LENGTH_SHORT).show();
                    } else if (responseData.getBookInfo() == null) {
                        Toast.makeText(getActivity(), "该书已下架", Toast.LENGTH_SHORT).show();
                    } else if (responseData.isCanFreeRead()) {
                        //判断当前书籍是否可以免费阅读
                        Call<ModifyBean> startReadCall = ReaderRetroift.getInstance(getActivity()).getApi().startReadCall(responseData.getBookInfo().getId());
                        startReadCall.enqueue(new Callback<ModifyBean>() {
                            @Override
                            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                                ModifyBean bean = response.body();
                                if (bean != null && bean.getResponseCode() == 1) {
                                    //直接阅读
                                    read();
                                } else if (bean != null && bean.getResponseCode() == 0) {
                                    Toast.makeText(getActivity(), bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ModifyBean> call, Throwable t) {

                            }
                        });
                    } else {
                        //开通畅读卡
                        //获取畅读卡类型以及单价
                        Call<ChangDuParamBean> changDuParamCallCall = ReaderRetroift.getInstance(getActivity()).getApi().changDuParamCall("vipType");
                        changDuParamCallCall.enqueue(new Callback<ChangDuParamBean>() {
                            @Override
                            public void onResponse(Call<ChangDuParamBean> call, Response<ChangDuParamBean> response) {
                                ChangDuParamBean changDuParamBean = response.body();
                                if (changDuParamBean != null && changDuParamBean.getResponseData() != null) {
                                    List<ChangDuParamBean.ResponseDataBean> changduparamList = changDuParamBean.getResponseData();
                                    changduparamDataBean = changduparamList.get(0);
                                    openChangduDialog();
                                }
                            }

                            @Override
                            public void onFailure(Call<ChangDuParamBean> call, Throwable t) {

                            }
                        });
                        //账户余额
                        Call<BalanceBean> balanceCall = ReaderRetroift.getInstance(getActivity()).getApi().getBalanceCall();
                        balanceCall.enqueue(new Callback<BalanceBean>() {
                            @Override
                            public void onResponse(Call<BalanceBean> call, Response<BalanceBean> response) {
                                BalanceBean balanceBean = response.body();
                                if (balanceBean != null && balanceBean.getResponseData() != null) {
                                    accountMoney = balanceBean.getResponseData().getAccountMoney();
                                }
                            }

                            @Override
                            public void onFailure(Call<BalanceBean> call, Throwable t) {

                            }
                        });
                    }
                }
            });
            bsGvBook.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    BSBookListBean.ResponseDataBean responseDataBean = responseDataList.get(position);

                    if (responseDataBean != null && responseDataBean.getBookInfo() != null && responseDataBean.getBookInfo().getStatus() == 0) {
                        Toast.makeText(getActivity(), "该书已下架", Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        deleteBook(position);
                        return true;

                    }
                }
            });

        }
    }

    private void openChangduDialog() {
        //弹出购买畅读卡窗口
        final Dialog openChangduDialog = new Dialog(getActivity(), R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_open_changdu, null);
        ImageView iv_cancel = (ImageView) contentView.findViewById(R.id.dialog_open_changdu_cancel);
        TextView tv_understand = (TextView) contentView.findViewById(R.id.dialog_open_changdu_understand);
        TextView tv_money = (TextView) contentView.findViewById(R.id.dialog_paychallenge_money);
        TextView tv_buy = (TextView) contentView.findViewById(R.id.dialog_open_changdu_buy);
        TextView tv_free = (TextView) contentView.findViewById(R.id.dialog_open_changdu_free);
        if (changduparamDataBean != null) {
            moneyvalue = changduparamDataBean.getValue();
        }
        final double paymoney = Double.valueOf(moneyvalue);
        DecimalFormat df = new DecimalFormat("######0.00");
        String formatMoney = df.format(paymoney);
        tv_money.setText(formatMoney);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangduDialog.dismiss();
            }
        });
        tv_understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangduDialog.dismiss();
                Intent changduIntent = new Intent(getActivity(), ChangduActivity.class);
                startActivity(changduIntent);
            }
        });
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
                final Dialog freeOpenDialog = new Dialog(getActivity(), R.style.myDialogTheme);
                final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_freeopen, null);
                ImageView iv_cancel = (ImageView) contentView.findViewById(R.id.dialog_freeopen_cancel);
                final TextView tv_des = (TextView) contentView.findViewById(R.id.dialog_freeopen_des);
                final TextView tv_weixin = (TextView) contentView.findViewById(R.id.dialog_freeopen_weixin);
                final TextView tv_openweixin = (TextView) contentView.findViewById(R.id.dialog_freeopen_openweixin);
                tv_weixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager c = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
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
                            Toast.makeText(getActivity(), "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                freeOpenDialog.setContentView(contentView);
                freeOpenDialog.show();
                WindowManager m = getActivity().getWindowManager();
                Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
                WindowManager.LayoutParams p = freeOpenDialog.getWindow().getAttributes(); //获取对话框当前的参数值
                p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
                freeOpenDialog.getWindow().setAttributes(p); //设置生效
            }
        });
        openChangduDialog.setContentView(contentView);
        openChangduDialog.show();
    }

    private void payChangDu() {
        final Dialog payChallengeDialog = new Dialog(getActivity(), R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_paychallenge, null);
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
        calendar1.add(Calendar.DAY_OF_YEAR, 365);
        String afteryear = format.format(calendar1.getTime());

        tv_time.setText(today + "~" + afteryear);
        DecimalFormat df1 = new DecimalFormat("######0.00");
        String account = df1.format(accountMoney);
        tv_balance.setText("账户读币：" + account);
        if (accountMoney < paymoney) {
            tv_balance.setTextColor(getResources().getColor(R.color.red));
            tv_confirm.setText("支付宝充值");
        } else {
            tv_balance.setTextColor(getResources().getColor(R.color.gray));
        }
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountMoney >= paymoney) {
                    payChallengeDialog.dismiss();
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle(null);
                    progressDialog.setMessage("正在支付...");
                    progressDialog.show();
                    //支付畅读卡
                    Call<ModifyBean> buyChangDuCall = ReaderRetroift.getInstance(getActivity()).getApi().buyChangDuCall(changduparamDataBean.getCode());
                    buyChangDuCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                if (bean.getResponseCode() == 1) {
                                    Toast.makeText(getActivity(), bean.getResponseMsg(), Toast.LENGTH_LONG).show();
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

        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = payChallengeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
        payChallengeDialog.getWindow().setAttributes(p); //设置生效

    }

    private void recharge() {

        final Dialog rechargeDialog = new Dialog(getActivity(), R.style.myDialogTheme);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_recharge, null);
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
                Dialog detailDialog = new Dialog(getActivity(), R.style.myDialogTheme);
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_account_detail, null);
                detailDialog.setContentView(contentView);
                detailDialog.show();
            }
        });

        cb_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_10.isChecked()) {
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
                } else {
                    tv_dubi_8.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_8.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_20.isChecked()) {
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
                } else {
                    tv_dubi_18.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_18.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_40.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_40.isChecked()) {
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
                } else {
                    tv_dubi_40.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_40.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_60.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_60.isChecked()) {
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
                } else {
                    tv_dubi_60.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_60.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_98.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_98.isChecked()) {
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
                } else {
                    tv_dubi_98.setTextColor(getResources().getColor(R.color.gray));
                    tv_money_98.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        cb_128.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_128.isChecked()) {
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
                } else {
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
                        if (cb.equals(cb_10)) {
                            rechargeprice = 8.0f;
                        } else if (cb.equals(cb_20)) {
                            rechargeprice = 18.0f;
                        } else if (cb.equals(cb_40)) {
                            rechargeprice = 40.0f;
                        } else if (cb.equals(cb_60)) {
                            rechargeprice = 60.0f;
                        } else if (cb.equals(cb_98)) {
                            rechargeprice = 98.0f;
                        } else if (cb.equals(cb_128)) {
                            rechargeprice = 198.0f;
                        }
                        //充值
                        Call<ApplyBean> applyBeanCall = ReaderRetroift.getInstance(getActivity()).getApi().applyMoney(rechargeprice);
                        applyBeanCall.enqueue(new Callback<ApplyBean>() {
                            @Override
                            public void onResponse(Call<ApplyBean> call, Response<ApplyBean> response) {
                                applyBean = response.body();
                                if (applyBean != null && applyBean.getResponseCode() == 1) {
                                    apply();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApplyBean> call, Throwable t) {

                            }
                        });
                    }
                }
                if (rechargeprice == 0) {
                    Toast.makeText(getActivity(), "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                rechargeDialog.dismiss();
            }
        });

        rechargeDialog.setContentView(contentView);
        rechargeDialog.show();

        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = rechargeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
        rechargeDialog.getWindow().setAttributes(p); //设置生效
    }

    private void apply() {
        //调用支付宝支付
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(applyBean.getResponseData().getRequestParams(), true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                moneyHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler moneyHandler = new Handler() {
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
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void read() {
        //判断下载路径是否为空
        if (responseData.getBookInfo().getPath() == null || "".equals(responseData.getBookInfo().getPath())) {
            Toast.makeText(getActivity(), "该书已下架", Toast.LENGTH_SHORT).show();
            return;
        }
        //先判断本地是否有缓存
        File file = new File(Environment.getExternalStorageDirectory().getPath() + responseData.getBookInfo().getPath());
        if (!file.exists()) {
            //下载
            download();
        } else {
            Intent intent = new Intent();
            String paths = Environment.getExternalStorageDirectory().getPath() + responseData.getBookInfo().getPath();
            Uri uri = Uri.parse(paths);
            intent.setClass(getActivity(), FBReader.class);
            intent.setData(uri);
            Bundle bundle = new Bundle();
            //传递是否挑战书籍标识
            int challengeFlag = responseData.getChallengeFlag();
            intent.putExtra("challengeflag", challengeFlag);//0-没有挑战，1-挑战中
            bundle.putSerializable("bs_bookdetail", responseData);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void download() {
        downloaddialog = new DownloadProgressDialog(getActivity());
        downloaddialog.setCancelable(false);
        downloaddialog.setCanceledOnTouchOutside(false);
        downloaddialog.show();
        FileDownloader.setup(getContext());
        FileDownloader.getImpl().create(ReaderRetroift.BASE_URL + "/download" + responseData.getBookInfo().getPath())
                .setPath(Environment.getExternalStorageDirectory().getPath() + responseData.getBookInfo().getPath())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        downloaddialog.setProgress(totalBytes, soFarBytes);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        downloaddialog.dismiss();
                        Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        String paths = Environment.getExternalStorageDirectory().getPath() + responseData.getBookInfo().getPath();
                        Uri uri = Uri.parse(paths);
                        intent.setClass(getActivity(), FBReader.class);
                        intent.setData(uri);
                        Bundle bundle = new Bundle();
                        int challengeFlag = responseData.getChallengeFlag();
                        intent.putExtra("challengeflag", challengeFlag);//0-没有挑战，1-挑战中
                        bundle.putSerializable("bs_bookdetail", responseData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Toast.makeText(getActivity(), "下载出现错误" + e.toString(), Toast.LENGTH_SHORT).show();
                        downloaddialog.dismiss();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }


    private void deleteBook ( final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] strarr = {"移出书架", "取消"};
        builder.setItems(strarr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    BSBookListBean.ResponseDataBean bean = responseDataList.get(position);
                    String id = bean.getBookId();
                    Call<ModifyBean> removeCall = ReaderRetroift.getInstance(getActivity()).getApi().bookRemoveBS(id);
                    removeCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean body = response.body();
                            if (body != null) {
                                if (body.getResponseCode() == 1) {
                                        responseDataList.remove(position);
                                        myBookAdapter.notifyDataSetChanged();
                                    if (responseDataList.size() == 0) {
                                        bsTvEmptyBook.setVisibility(View.VISIBLE);
                                        bsGvBook.setVisibility(View.GONE);
                                    }
                                }
                                    Toast.makeText(getActivity(), body.getResponseMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {
                        }
                    });
                } else {

                }
            }
        });
        builder.show();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    refresh();
                    break;

            }
        }

        ;
    };

    private void refresh() {
        init();
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 3000);
    }


    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.bs_book_nonet)
    public void onViewClicked() {
        bsBookNonet.setVisibility(View.GONE);
        bsGvBook.setVisibility(View.VISIBLE);
        init();
    }
}
