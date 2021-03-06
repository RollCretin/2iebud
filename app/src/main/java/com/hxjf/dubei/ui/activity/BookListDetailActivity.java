package com.hxjf.dubei.ui.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ApplyBean;
import com.hxjf.dubei.bean.BalanceBean;
import com.hxjf.dubei.bean.ChangDuParamBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.ShareInfoBean;
import com.hxjf.dubei.bean.ShudanDetailBean;
import com.hxjf.dubei.bean.praiseBean;
import com.hxjf.dubei.network.PayResult;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.widget.DownloadProgressDialog;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.geometerplus.android.fbreader.FBReader;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/6/22.
 */

public class BookListDetailActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 5;
    private static final int RESULT_LOAD_IMAGE = 1;
    Boolean flag = true;
    @BindView(R.id.booklist_detail_cover)
    ImageView booklistDetailCover;
    @BindView(R.id.booklist_detail_title)
    TextView booklistDetailTitle;
    @BindView(R.id.booklist_detail_count)
    TextView booklistDetailCount;
    @BindView(R.id.booklist_detail_back)
    ImageView booklistDetailBack;
    @BindView(R.id.booklist_detail_des)
    TextView booklistDetailDes;
    @BindView(R.id.booklist_detail_book_container)
    LinearLayout booklistDetailBookContainer;
    @BindView(R.id.booklist_detail_cb_like)
    CheckBox booklistDetailCbLike;
    @BindView(R.id.booklist_detail_like_count)
    TextView booklistDetailLikeCount;
    @BindView(R.id.booklist_detail_share)
    ImageView booklistDetailShare;
    @BindView(R.id.booklist_detail_share_count)
    TextView booklistDetailShareCount;
    @BindView(R.id.booklist_detail_profile_more)
    TextView booklistDetailProfileMore;
    @BindView(R.id.booklist_detail_pratroit)
    CircleImageView booklistDetailPratroit;
    @BindView(R.id.booklist_detail_name)
    TextView booklistDetailName;

    private List<String> booklist;
    private View childView;
    private praiseBean praisebean;
    private String booklistid;
    private List<ShudanDetailBean.ResponseDataBean.BooksBean> booksList;
    private ShudanDetailBean.ResponseDataBean responseDataBean;
    private ShareInfoBean.ResponseDataBean shareinfo;
    private DownloadProgressDialog downloaddialog;
    private double accountMoney;
    private ChangDuParamBean.ResponseDataBean changduparamDataBean;
    private ApplyBean applyBean;
    private String moneyvalue;
    private long lastClick;
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
                        Toast.makeText(BookListDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(BookListDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
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
            Toast.makeText(BookListDetailActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(BookListDetailActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(BookListDetailActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_booklist_detail;
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

        //请求之前禁止点赞
        booklistDetailCbLike.setClickable(false);
        Intent intent = getIntent();
        booklistid = intent.getStringExtra("booklistid");
        Call<ShudanDetailBean> shudanDetailBeanCall = ReaderRetroift.getInstance(this).getApi().ShudanDetailCall(booklistid, 0, 30, true);
        shudanDetailBeanCall.enqueue(new Callback<ShudanDetailBean>() {
            @Override
            public void onResponse(Call<ShudanDetailBean> call, Response<ShudanDetailBean> response) {
                ShudanDetailBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    responseDataBean = bean.getResponseData();
                    booksList = bean.getResponseData().getBooks();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<ShudanDetailBean> call, Throwable t) {

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initData() {
        booklistDetailBookContainer.removeAllViews();
        booklistDetailCbLike.setClickable(true);
        booklistDetailCbLike.setChecked(responseDataBean.isCollected());
        if (this != null && !BookListDetailActivity.this.isDestroyed()) {
            Glide.with(this).load(ReaderRetroift.IMAGE_URL + responseDataBean.getCover()).into(booklistDetailCover);
            Glide.with(this).load(ReaderRetroift.IMAGE_URL + responseDataBean.getOwnerHeadPath()).into(booklistDetailPratroit);
        }
        booklistDetailTitle.setText(responseDataBean.getTitle());
        booklistDetailCount.setText("【共" + responseDataBean.getBookCount() + "本】");
        booklistDetailLikeCount.setText("" + responseDataBean.getCollectCount());
        booklistDetailShareCount.setText("" + responseDataBean.getShareTotal());
        booklistDetailDes.setText(Html.fromHtml(responseDataBean.getSummary()));
        if (responseDataBean.getOwnerName() != null && responseDataBean.getOwnerName().length() != 0) {
            booklistDetailName.setText(Html.fromHtml(responseDataBean.getOwnerName()));
        }

        for (int i = 0; i < booksList.size(); i++) {
            childView = View.inflate(this, R.layout.item_booklist_detail, null);
            final ShudanDetailBean.ResponseDataBean.BooksBean responseDataBean1 = booksList.get(i);

            ImageView ivImage = (ImageView) childView.findViewById(R.id.item_booklist_image);
            TextView tvName = (TextView) childView.findViewById(R.id.item_booklist_name);
            TextView tvAuthor = (TextView) childView.findViewById(R.id.item_booklist_author);
            tvName.setText(responseDataBean1.getName());
            tvAuthor.setText(responseDataBean1.getAuthor());
            GlideLoadUtils.getInstance().glideLoad(this, ReaderRetroift.IMAGE_URL + responseDataBean1.getCover(), ivImage, 0);

            final TextView tvread = (TextView) childView.findViewById(R.id.item_booklist_startread);
            final CheckBox cbAdd = (CheckBox) childView.findViewById(R.id.item_booklist_add);
            boolean inBookShelf = responseDataBean1.isInBookShelf();
            if (inBookShelf) {
                tvread.setVisibility(View.VISIBLE);
                cbAdd.setVisibility(View.GONE);
            } else {
                tvread.setVisibility(View.GONE);
                cbAdd.setVisibility(View.VISIBLE);
            }
            cbAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        addBookShelf(responseDataBean1.getId());
                        cbAdd.setClickable(false);
                        TimerTask task = new TimerTask() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        cbAdd.setVisibility(View.GONE);
                                        tvread.setVisibility(View.VISIBLE);

                                    }
                                });
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 300);


                    }

                }
            });
            tvread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (System.currentTimeMillis() - lastClick <= 1000) {
                        return;
                    }
                    lastClick = System.currentTimeMillis();
                    if (responseDataBean1.getStatus() == 0) {
                        Toast.makeText(BookListDetailActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
                    } else if (responseDataBean1.isCanFreeRead()) {
                        Call<ModifyBean> startReadCall = ReaderRetroift.getInstance(BookListDetailActivity.this).getApi().startReadCall(responseDataBean1.getId());
                        startReadCall.enqueue(new Callback<ModifyBean>() {
                            @Override
                            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                                ModifyBean bean = response.body();
                                if (bean != null && bean.getResponseCode() == 1) {
                                    //直接阅读
                                    read(responseDataBean1);
                                } else if (bean != null && bean.getResponseCode() == 0) {
                                    Toast.makeText(BookListDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ModifyBean> call, Throwable t) {

                            }
                        });
                    } else {
                        //书籍不可免费阅读
                        //获取畅读卡类型以及单价
                        Call<ChangDuParamBean> changDuParamCallCall = ReaderRetroift.getInstance(BookListDetailActivity.this).getApi().changDuParamCall("vipType");
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
                        Call<BalanceBean> balanceCall = ReaderRetroift.getInstance(BookListDetailActivity.this).getApi().getBalanceCall();
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
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进入书籍详情页
                    Intent intent = new Intent(BookListDetailActivity.this, BookDetailActivity.class);
                    intent.putExtra("bookid", responseDataBean1.getId());
                    startActivity(intent);
                }
            });
            booklistDetailBookContainer.addView(childView);
        }


        booklistDetailCbLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //收藏
                    Call<ModifyBean> collectBooklistCall = ReaderRetroift.getInstance(BookListDetailActivity.this).getApi().collectBooklist(booklistid);
                    collectBooklistCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                Toast.makeText(BookListDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                int i = Integer.valueOf(booklistDetailLikeCount.getText().toString());
                                booklistDetailLikeCount.setText("" + (i + 1));
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {

                        }
                    });
                } else {
                    //取消收藏
                    Call<ModifyBean> cancelCollectBooklistCall = ReaderRetroift.getInstance(BookListDetailActivity.this).getApi().cancelCollectBooklist(booklistid);
                    cancelCollectBooklistCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                Toast.makeText(BookListDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                int i = Integer.valueOf(booklistDetailLikeCount.getText().toString());
                                booklistDetailLikeCount.setText("" + (i - 1));
                                Intent in = new Intent();
                                in.putExtra("result", "res");
                                setResult(RESULT_OK, in);
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {

                        }
                    });

                }
            }
        });
    }

    private void openChangduDialog() {

        //弹出购买畅读卡窗口
        final Dialog openChangduDialog = new Dialog(BookListDetailActivity.this, R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(BookListDetailActivity.this).inflate(R.layout.view_dialog_open_changdu, null);
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
                Intent changduIntent = new Intent(BookListDetailActivity.this, ChangduActivity.class);
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
                final Dialog freeOpenDialog = new Dialog(BookListDetailActivity.this, R.style.myDialogTheme);
                final View contentView = LayoutInflater.from(BookListDetailActivity.this).inflate(R.layout.view_dialog_freeopen, null);
                ImageView iv_cancel = (ImageView) contentView.findViewById(R.id.dialog_freeopen_cancel);
                final TextView tv_des = (TextView) contentView.findViewById(R.id.dialog_freeopen_des);
                final TextView tv_weixin = (TextView) contentView.findViewById(R.id.dialog_freeopen_weixin);
                final TextView tv_openweixin = (TextView) contentView.findViewById(R.id.dialog_freeopen_openweixin);
                tv_weixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager c = (ClipboardManager) BookListDetailActivity.this.getSystemService(CLIPBOARD_SERVICE);
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
                            Toast.makeText(BookListDetailActivity.this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                freeOpenDialog.setContentView(contentView);
                freeOpenDialog.show();
                WindowManager m = BookListDetailActivity.this.getWindowManager();
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
        final Dialog payChallengeDialog = new Dialog(BookListDetailActivity.this, R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(BookListDetailActivity.this).inflate(R.layout.view_dialog_paychallenge, null);
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
                    final ProgressDialog progressDialog = new ProgressDialog(BookListDetailActivity.this);
                    progressDialog.setTitle(null);
                    progressDialog.setMessage("正在支付...");
                    progressDialog.show();
                    //支付畅读卡
                    if (changduparamDataBean == null) {
                        return;
                    }
                    Call<ModifyBean> buyChangDuCall = ReaderRetroift.getInstance(BookListDetailActivity.this).getApi().buyChangDuCall(changduparamDataBean.getCode());
                    buyChangDuCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                if (bean.getResponseCode() == 1) {
                                    Toast.makeText(BookListDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_LONG).show();
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

        WindowManager m = BookListDetailActivity.this.getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = payChallengeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
        payChallengeDialog.getWindow().setAttributes(p); //设置生效

    }

    private void recharge() {

        final Dialog rechargeDialog = new Dialog(BookListDetailActivity.this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(BookListDetailActivity.this).inflate(R.layout.view_dialog_recharge, null);
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
                Dialog detailDialog = new Dialog(BookListDetailActivity.this, R.style.myDialogTheme);
                View contentView = LayoutInflater.from(BookListDetailActivity.this).inflate(R.layout.view_dialog_account_detail, null);
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
                        Call<ApplyBean> applyBeanCall = ReaderRetroift.getInstance(BookListDetailActivity.this).getApi().applyMoney(rechargeprice);
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
                    Toast.makeText(BookListDetailActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                rechargeDialog.dismiss();
            }
        });

        rechargeDialog.setContentView(contentView);
        rechargeDialog.show();

        WindowManager m = BookListDetailActivity.this.getWindowManager();
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
                PayTask alipay = new PayTask(BookListDetailActivity.this);
                Map<String, String> result = alipay.payV2(applyBean.getResponseData().getRequestParams(), true);

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

    private void read(ShudanDetailBean.ResponseDataBean.BooksBean responseDataBean1) {
        //判断下载路径是否为空
        if (responseDataBean1.getPath() == null || "".equals(responseDataBean1.getPath())) {
            Toast.makeText(BookListDetailActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
            return;
        }
        //先判断本地是否有缓存
        File file = new File(Environment.getExternalStorageDirectory().getPath() + responseDataBean1.getPath());
        if (!file.exists()) {
            //下载
            download(responseDataBean1);
        } else {
            Intent intent = new Intent();
            String paths = Environment.getExternalStorageDirectory().getPath() + responseDataBean1.getPath();
            Uri uri = Uri.parse(paths);
            intent.setClass(BookListDetailActivity.this, FBReader.class);
            intent.setData(uri);
            Bundle bundle = new Bundle();
            bundle.putSerializable("list_bookdetail", responseDataBean1);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private void download(final ShudanDetailBean.ResponseDataBean.BooksBean responseDataBean1) {
        downloaddialog = new DownloadProgressDialog(BookListDetailActivity.this);
        downloaddialog.setCancelable(false);
        downloaddialog.setCanceledOnTouchOutside(false);
        downloaddialog.show();
        FileDownloader.setup(this);
        FileDownloader.getImpl().create(ReaderRetroift.BASE_URL + "/download" + responseDataBean1.getPath())
                .setPath(Environment.getExternalStorageDirectory().getPath() + responseDataBean1.getPath())
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
                        Intent intent = new Intent();
                        String paths = Environment.getExternalStorageDirectory().getPath() + responseDataBean1.getPath();
                        Uri uri = Uri.parse(paths);
                        intent.setClass(BookListDetailActivity.this, FBReader.class);
                        intent.setData(uri);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("list_bookdetail", responseDataBean1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Toast.makeText(BookListDetailActivity.this, "下载失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        downloaddialog.dismiss();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    private void addBookShelf(String bookid) {
        //请求添加到书架
        Call<ModifyBean> addBookshelfCall = ReaderRetroift.getInstance(this).getApi().bookAddBookshelf(bookid);
        addBookshelfCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    Toast.makeText(BookListDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });

    }

    @OnClick({R.id.booklist_detail_cover, R.id.booklist_detail_back, R.id.booklist_detail_des, R.id.booklist_detail_share, R.id.booklist_detail_profile_more, R.id.booklist_detail_pratroit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.booklist_detail_back:
                finish();
                break;
            case R.id.booklist_detail_des:

                break;
            case R.id.booklist_detail_profile_more:
                setExtend();
                break;
            case R.id.booklist_detail_share:
                share();
                break;
            case R.id.booklist_detail_pratroit:
                Intent personIntent = new Intent(this, PersonDetailActivity.class);
                personIntent.putExtra("userId", responseDataBean.getOwnerId());
                startActivity(personIntent);
                break;
        }
    }

    private void share() {
        //获取分享信息
        Call<ShareInfoBean> shareInfoCall = ReaderRetroift.getInstance(this).getApi().shareInfoCall(booklistid, "booklist");
        shareInfoCall.enqueue(new Callback<ShareInfoBean>() {
            @Override
            public void onResponse(Call<ShareInfoBean> call, Response<ShareInfoBean> response) {
                ShareInfoBean bean = response.body();
                if (bean != null) {
                    shareinfo = bean.getResponseData();
                    shareDialog();
                }

            }

            @Override
            public void onFailure(Call<ShareInfoBean> call, Throwable t) {

            }
        });
    }

    private void shareDialog() {
        final UMWeb web = new UMWeb(shareinfo.getUrl());
        web.setTitle(shareinfo.getTitle());//标题
        web.setThumb(new UMImage(BookListDetailActivity.this, ReaderRetroift.IMAGE_URL + shareinfo.getImagePath()));  //缩略图
        String summary = shareinfo.getSummary();
        web.setDescription(Html.fromHtml(summary).toString());//描述

        final Dialog bottomDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_share, null);
        TextView tv_wx = (TextView) contentView.findViewById(R.id.dialog_share_wx);
        TextView tv_pengyouquan = (TextView) contentView.findViewById(R.id.dialog_share_pengyouquan);
        Button btn_cancel = (Button) contentView.findViewById(R.id.dialog_share_cancel);
        tv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享微信好友,分享成功之后调用分享接口
                new ShareAction(BookListDetailActivity.this)
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
                new ShareAction(BookListDetailActivity.this)
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

    private void setExtend() {
        if (flag) {
            flag = false;
            booklistDetailDes.setEllipsize(null); // 展开
            booklistDetailDes.setSingleLine(flag);
            booklistDetailProfileMore.setText("收起");
        } else {
            flag = true;
            booklistDetailDes.setMaxLines(4);
            booklistDetailDes.setEllipsize(TextUtils.TruncateAt.END); // 收缩
            booklistDetailProfileMore.setText("查看更多");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
