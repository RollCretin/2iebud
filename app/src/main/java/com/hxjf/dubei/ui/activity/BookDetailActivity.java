package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ApplyBean;
import com.hxjf.dubei.bean.BalanceBean;
import com.hxjf.dubei.bean.BookDetailBean;
import com.hxjf.dubei.bean.ChangDuParamBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.ShareInfoBean;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/7/11.
 */

public class BookDetailActivity extends BaseActivity {
    private static final int BOOK_PROFILE = 0;
    private static final int AUTHOR_PROFILE = 1;
    private static final int CATALOG_PROFILE = 2;
    private static final int TYPE_BOOK_DETAIL = 100;
    private static final int TYPE_CHALLENGE_DETAIL = 200;
    private static final int SDK_PAY_FLAG = 5;
    @BindView(R.id.book_detail_back)
    ImageView bookDetailBack;
    @BindView(R.id.book_detail_share)
    ImageView bookDetailShare;
    @BindView(R.id.book_detail_bookimage)
    ImageView bookDetailBookimage;
    @BindView(R.id.book_detail_bookname)
    TextView bookDetailBookname;
    @BindView(R.id.book_detail_bookauthor)
    TextView bookDetailBookauthor;
    @BindView(R.id.book_detail_ratingbar)
    RatingBar bookDetailRatingbar;//五分制
    @BindView(R.id.book_detail_score)
    TextView bookDetailScore;
    @BindView(R.id.book_detail_press)
    TextView bookDetailPress;
    @BindView(R.id.book_detail_textnumber)
    TextView bookDetailTextnumber;
    @BindView(R.id.book_detail_book_profile)
    TextView bookDetailBookProfile;
    @BindView(R.id.book_detail_book_profile_more)
    TextView bookDetailBookProfileMore;
    @BindView(R.id.book_detail_author_profile)
    TextView bookDetailAuthorProfile;
    @BindView(R.id.book_detail_author_profile_more)
    TextView bookDetailAuthorProfileMore;
    @BindView(R.id.book_detail_catalog_profile)
    TextView bookDetailCatalogProfile;
    @BindView(R.id.book_detail_catalog_profile_more)
    TextView bookDetailCatalogProfileMore;
    @BindView(R.id.book_detail_start_read)
    Button bookDetailStartRead;
    @BindView(R.id.challenge_select)
    Button challengeSelect;
    @BindView(R.id.ll_challenge_select)
    LinearLayout llChallengeSelect;
    @BindView(R.id.ll_book_detail)
    LinearLayout llBookDetail;
    @BindView(R.id.book_detail_add_challenge)
    FloatingActionButton bookDetailAddChallenge;
    @BindView(R.id.book_detail_ll_catalog)
    LinearLayout bookDetailLlCatalog;

    Boolean book_isShrink = true;
    Boolean author_isShrink = true;
    Boolean catalog_isShrink = true;
    @BindView(R.id.book_detail_ll_book_profile)
    LinearLayout bookDetailLlBookProfile;
    @BindView(R.id.book_detail_ll_author_profile)
    LinearLayout bookDetailLlAuthorProfile;
    @BindView(R.id.book_detail_pratroit)
    CircleImageView bookDetailPratroit;
    @BindView(R.id.book_detail_fromwho)
    TextView bookDetailFromwho;

    private BookDetailBean.ResponseDataBean responseData;
    private String bookid;
    private String bookName;
    private int type;
    private boolean ischallenge;
    private ShareInfoBean.ResponseDataBean shareInfo;
    private boolean isreader;
    private DownloadProgressDialog downloaddialog;
    private ChangDuParamBean.ResponseDataBean changduparamDataBean;
    private double accountMoney;
    private ApplyBean applyBean;
    private long lastClick;
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
            Toast.makeText(BookDetailActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(BookDetailActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(BookDetailActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };
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
                        Toast.makeText(BookDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(BookDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_book_detail;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bookid = intent.getStringExtra("bookid");
        ischallenge = intent.getBooleanExtra("ischallenge", false);
        type = intent.getIntExtra("type", TYPE_BOOK_DETAIL);
        isreader = intent.getBooleanExtra("isreader", false);
        bookDetailStartRead.setEnabled(false);
        bookDetailAddChallenge.setClickable(false);
        //请求获取书籍数据
        Call<BookDetailBean> bookDetailCall = ReaderRetroift.getInstance(this).getApi().BookDetailCall(bookid);
        bookDetailCall.enqueue(new Callback<BookDetailBean>() {
            @Override
            public void onResponse(Call<BookDetailBean> call, Response<BookDetailBean> response) {
                BookDetailBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    responseData = bean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<BookDetailBean> call, Throwable t) {
            }
        });

        //判断一本书的挑战状态
        Call<ModifyBean> modifyBeanCall = ReaderRetroift.getInstance(this).getApi().judgeChallenge(bookid);
        modifyBeanCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    int responseCode = bean.getResponseCode();
                    if (responseCode == 0) {
                        //不可发起挑战
                        bookDetailAddChallenge.setVisibility(View.GONE);
                    } else {
                        bookDetailAddChallenge.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        if (ischallenge) {
            llChallengeSelect.setVisibility(View.VISIBLE);
            llBookDetail.setVisibility(View.GONE);
        }
        if (isreader) {
            llChallengeSelect.setVisibility(View.GONE);
            llBookDetail.setVisibility(View.GONE);
            bookDetailAddChallenge.setVisibility(View.GONE);
        }
        bookDetailStartRead.setEnabled(true);
        bookDetailAddChallenge.setClickable(true);
        GlideLoadUtils.getInstance().glideLoad(this, ReaderRetroift.IMAGE_URL + responseData.getCover(), bookDetailBookimage, 0);
        GlideLoadUtils.getInstance().glideLoad(this, ReaderRetroift.IMAGE_URL + responseData.getOwnerHeadPath(), bookDetailPratroit, 0);
        bookDetailFromwho.setText(responseData.getOwnerName());//拥有者
        bookName = responseData.getName();
        bookDetailBookname.setText(bookName);//书名
        bookDetailBookauthor.setText(responseData.getAuthor());//作者
        bookDetailRatingbar.setRating((float) (responseData.getDoubanScore() / 2.0));//豆瓣评分
        bookDetailScore.setText(responseData.getDoubanScore() + "分/豆瓣");
        bookDetailPress.setText(responseData.getPress());//出版社
        int wordNumber = responseData.getWordNumber();//字数
        float newNumber = (float) wordNumber / 10000;
        DecimalFormat df = new DecimalFormat("######0.00");
        bookDetailTextnumber.setText("约" + df.format(newNumber) + "万字");
        if (responseData.getContentAbout() == null || "".equals(responseData.getContentAbout())) {
            bookDetailLlBookProfile.setVisibility(View.GONE);
        } else {
            bookDetailLlBookProfile.setVisibility(View.VISIBLE);

        }
        bookDetailBookProfile.setText(responseData.getContentAbout());//书籍简介
        if (responseData.getAuthorAbout() == null || "".equals(responseData.getAuthorAbout())) {
            bookDetailLlAuthorProfile.setVisibility(View.GONE);
        } else {
            bookDetailLlAuthorProfile.setVisibility(View.VISIBLE);

        }
        bookDetailAuthorProfile.setText(responseData.getAuthorAbout());//作者简介
        if (responseData.getCatalog() == null || "".equals(responseData.getCatalog())) {
            bookDetailLlCatalog.setVisibility(View.GONE);
        } else {
            bookDetailLlCatalog.setVisibility(View.VISIBLE);
        }
        bookDetailCatalogProfile.setText(responseData.getCatalog());//目录
        boolean inBookShelf = responseData.isInBookShelf();//是否在书架中
        if (type == TYPE_CHALLENGE_DETAIL) {
            llBookDetail.setVisibility(View.GONE);
            llChallengeSelect.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.book_detail_back, R.id.book_detail_share, R.id.book_detail_add_challenge, R.id.book_detail_book_profile_more, R.id.book_detail_author_profile_more, R.id.book_detail_catalog_profile_more, R.id.book_detail_start_read, R.id.challenge_select,R.id.book_detail_pratroit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_detail_back:
                finish();
                break;
            case R.id.book_detail_share:
                share();
                break;
            case R.id.book_detail_add_challenge:
                Intent setchallengeintent = new Intent(this, SettingChallegeDetailActivity.class);
                setchallengeintent.putExtra("bookId", responseData.getId());
                setchallengeintent.putExtra("bookName", responseData.getName());
                startActivity(setchallengeintent);
                break;
            case R.id.book_detail_book_profile_more:
                OpenOrShrink(bookDetailBookProfile, bookDetailBookProfileMore, book_isShrink, BOOK_PROFILE);
                break;
            case R.id.book_detail_author_profile_more:
                OpenOrShrink(bookDetailAuthorProfile, bookDetailAuthorProfileMore, author_isShrink, AUTHOR_PROFILE);
                break;
            case R.id.book_detail_catalog_profile_more:
                OpenOrShrink(bookDetailCatalogProfile, bookDetailCatalogProfileMore, catalog_isShrink, CATALOG_PROFILE);
                break;
            case R.id.book_detail_start_read:
                if (System.currentTimeMillis() - lastClick <= 1000) {
                    return;
                }
                lastClick = System.currentTimeMillis();
                stratRead();
                break;
            case R.id.challenge_select:
                //发起挑战
                finish();
                Intent intent = new Intent(this, SettingChallegeDetailActivity.class);
                intent.putExtra("bookId", bookid);
                intent.putExtra("bookName", bookName);
                startActivity(intent);
                break;
            case R.id.book_detail_pratroit:
                Intent personIntent = new Intent(this,PersonDetailActivity.class);
                personIntent.putExtra("userId",responseData.getOwnerId());
                startActivity(personIntent);
                break;
        }
    }

    private void share() {
        //获取分享信息
        Call<ShareInfoBean> shareInfoCall = ReaderRetroift.getInstance(this).getApi().shareInfoCall(bookid, "book");
        shareInfoCall.enqueue(new Callback<ShareInfoBean>() {
            @Override
            public void onResponse(Call<ShareInfoBean> call, Response<ShareInfoBean> response) {
                ShareInfoBean bean = response.body();
                if (bean != null) {
                    shareInfo = bean.getResponseData();
                    shareDialog();
                }
            }

            @Override
            public void onFailure(Call<ShareInfoBean> call, Throwable t) {

            }
        });
    }

    private void shareDialog() {
        final UMWeb web = new UMWeb(shareInfo.getUrl());
        web.setTitle(shareInfo.getTitle());//标题
        web.setThumb(new UMImage(BookDetailActivity.this, ReaderRetroift.IMAGE_URL + shareInfo.getImagePath()));  //缩略图
        web.setDescription(shareInfo.getSummary());//描述

        final Dialog bottomDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_share, null);
        TextView tv_wx = (TextView) contentView.findViewById(R.id.dialog_share_wx);
        TextView tv_pengyouquan = (TextView) contentView.findViewById(R.id.dialog_share_pengyouquan);
        Button btn_cancel = (Button) contentView.findViewById(R.id.dialog_share_cancel);
        tv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享微信好友,分享成功之后调用分享接口
                new ShareAction(BookDetailActivity.this)
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
                new ShareAction(BookDetailActivity.this)
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

    private void stratRead() {
        if (responseData.getStatus() == 0) {
            Toast.makeText(this, "该书已下架", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断书籍是否能够免费阅读
        if (responseData.isCanFreeRead()) {
            Call<ModifyBean> startReadCall = ReaderRetroift.getInstance(BookDetailActivity.this).getApi().startReadCall(bookid);
            startReadCall.enqueue(new Callback<ModifyBean>() {
                @Override
                public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                    ModifyBean bean = response.body();
                    if (bean != null && bean.getResponseCode() == 1) {
                        //直接阅读
                        read();
                    } else if (bean != null && bean.getResponseCode() == 0) {
                        Toast.makeText(BookDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModifyBean> call, Throwable t) {

                }
            });
        } else {
            //开通畅读卡
            //获取畅读卡类型以及单价
            Call<ChangDuParamBean> changDuParamCallCall = ReaderRetroift.getInstance(BookDetailActivity.this).getApi().changDuParamCall("vipType");
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
            Call<BalanceBean> balanceCall = ReaderRetroift.getInstance(BookDetailActivity.this).getApi().getBalanceCall();
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

    private void openChangduDialog() {
        //弹出购买畅读卡窗口
        final Dialog openChangduDialog = new Dialog(BookDetailActivity.this, R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.view_dialog_open_changdu, null);
        ImageView iv_cancel = (ImageView) contentView.findViewById(R.id.dialog_open_changdu_cancel);
        TextView tv_understand = (TextView) contentView.findViewById(R.id.dialog_open_changdu_understand);
        TextView tv_money = (TextView) contentView.findViewById(R.id.dialog_paychallenge_money);
        TextView tv_buy = (TextView) contentView.findViewById(R.id.dialog_open_changdu_buy);
        TextView tv_free = (TextView) contentView.findViewById(R.id.dialog_open_changdu_free);
        TextView tv_hint = (TextView) contentView.findViewById(R.id.dialog_open_changdu_hint);
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
        tv_understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangduDialog.dismiss();
                Intent changduIntent = new Intent(BookDetailActivity.this, ChangduActivity.class);
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
                final Dialog freeOpenDialog = new Dialog(BookDetailActivity.this, R.style.myDialogTheme);
                final View contentView = LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.view_dialog_freeopen, null);
                ImageView iv_cancel = (ImageView) contentView.findViewById(R.id.dialog_freeopen_cancel);
                final TextView tv_des = (TextView) contentView.findViewById(R.id.dialog_freeopen_des);
                final TextView tv_weixin = (TextView) contentView.findViewById(R.id.dialog_freeopen_weixin);
                final TextView tv_openweixin = (TextView) contentView.findViewById(R.id.dialog_freeopen_openweixin);
                tv_weixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager c = (ClipboardManager) BookDetailActivity.this.getSystemService(CLIPBOARD_SERVICE);
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
                            Toast.makeText(BookDetailActivity.this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                freeOpenDialog.setContentView(contentView);
                freeOpenDialog.show();
                WindowManager m = BookDetailActivity.this.getWindowManager();
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
        final Dialog payChallengeDialog = new Dialog(BookDetailActivity.this, R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.view_dialog_paychallenge, null);
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
                    final ProgressDialog progressDialog = new ProgressDialog(BookDetailActivity.this);
                    progressDialog.setTitle(null);
                    progressDialog.setMessage("正在支付...");
                    progressDialog.show();
                    //支付畅读卡
                    Call<ModifyBean> buyChangDuCall = ReaderRetroift.getInstance(BookDetailActivity.this).getApi().buyChangDuCall(changduparamDataBean.getCode());
                    buyChangDuCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                if (bean.getResponseCode() == 1) {
                                    Toast.makeText(BookDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_LONG).show();
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

        WindowManager m = BookDetailActivity.this.getWindowManager();
        Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
        WindowManager.LayoutParams p = payChallengeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.9); //宽度设置为屏幕的0.8
        payChallengeDialog.getWindow().setAttributes(p); //设置生效


    }

    private void recharge() {

        final Dialog rechargeDialog = new Dialog(BookDetailActivity.this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.view_dialog_recharge, null);
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
                Dialog detailDialog = new Dialog(BookDetailActivity.this, R.style.myDialogTheme);
                View contentView = LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.view_dialog_account_detail, null);
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
                        Call<ApplyBean> applyBeanCall = ReaderRetroift.getInstance(BookDetailActivity.this).getApi().applyMoney(rechargeprice);
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
                    Toast.makeText(BookDetailActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                rechargeDialog.dismiss();
            }
        });

        rechargeDialog.setContentView(contentView);
        rechargeDialog.show();

        WindowManager m = BookDetailActivity.this.getWindowManager();
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
                PayTask alipay = new PayTask(BookDetailActivity.this);
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

    private void read() {
        //判断下载路径是否为空
        if (responseData.getPath() == null || "".equals(responseData.getPath())) {
            Toast.makeText(BookDetailActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
            return;
        }
        //先判断本地是否有缓存
        File file = new File(Environment.getExternalStorageDirectory().getPath() + responseData.getPath());
        if (!file.exists()) {
            //下载
            download();
        } else {
            Intent intent = new Intent();
            String paths = Environment.getExternalStorageDirectory().getPath() + responseData.getPath();
            Uri uri = Uri.parse(paths);
            intent.setClass(this, FBReader.class);
            intent.setData(uri);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookdetail", responseData);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void download() {
//        LoadDialog.show(this);
        downloaddialog = new DownloadProgressDialog(BookDetailActivity.this);
        downloaddialog.setCancelable(false);
        downloaddialog.setCanceledOnTouchOutside(false);
        downloaddialog.show();
        FileDownloader.setup(this);
        FileDownloader.getImpl().create(ReaderRetroift.BASE_URL + "/download" + responseData.getPath())
                .setPath(Environment.getExternalStorageDirectory().getPath() + responseData.getPath())
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
//                        LoadDialog.dismiss(BookDetailActivity.this);
                        downloaddialog.dismiss();
                        Toast.makeText(BookDetailActivity.this, "下载完成", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        String paths = Environment.getExternalStorageDirectory().getPath() + responseData.getPath();
                        Uri uri = Uri.parse(paths);
                        intent.setClass(BookDetailActivity.this, FBReader.class);
                        intent.setData(uri);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bookdetail", responseData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Toast.makeText(BookDetailActivity.this, "下载失败" + e.toString(), Toast.LENGTH_SHORT).show();
//                        LoadDialog.dismiss(BookDetailActivity.this);
                        downloaddialog.dismiss();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

  /*  DownloadCallback callback = new DownloadCallback() {
        @Override
        public void onStart(long l, long l1, float v) {

        }

        @Override
        public void onProgress(long l, long l1, float v) {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onCancel() {
            Toast.makeText(BookDetailActivity.this, "下载取消了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFinish(File file) {
            LoadDialog.dismiss(BookDetailActivity.this);
            Toast.makeText(BookDetailActivity.this, "书籍详情下载完成", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            String paths = Environment.getExternalStorageDirectory().getPath() + responseData.getPath();
            Uri uri = Uri.parse(paths);
            intent.setClass(BookDetailActivity.this, FBReader.class);
            intent.setData(uri);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookdetail", responseData);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onWait() {
            Toast.makeText(BookDetailActivity.this, "正在等待", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String s) {
            Toast.makeText(BookDetailActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };*/

    private void OpenOrShrink(TextView showView, TextView loadView, Boolean isShrink, int sign) {
        if (isShrink) {
            //展开
            isShrink = false;
            showView.setEllipsize(null); // 展开
            showView.setSingleLine(isShrink);
            loadView.setText("收起");
        } else {
            isShrink = true;
            showView.setEllipsize(TextUtils.TruncateAt.END);
            showView.setMaxLines(4);
            loadView.setText("查看更多");
        }
        switch (sign) {
            case BOOK_PROFILE:
                book_isShrink = isShrink;
                break;
            case AUTHOR_PROFILE:
                author_isShrink = isShrink;
                break;
            case CATALOG_PROFILE:
                catalog_isShrink = isShrink;
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
