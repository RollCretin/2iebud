package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.hxjf.dubei.bean.BookDetailBean;
import com.hxjf.dubei.bean.ChallengeDetailBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.ShareInfoBean;
import com.hxjf.dubei.network.PayResult;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.util.TimeUtil;
import com.hxjf.dubei.widget.DownloadProgressDialog;
import com.hxjf.dubei.widget.LoadDialog;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
 * Created by Chen_Zhang on 2017/6/7.
 */

public class ChallengeDetailActivity extends BaseActivity {


    private static final String TAG = "ChallengeDetailActivity";
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.detail_iv_return)
    ImageView detailIvReturn;
    @BindView(R.id.detail_tv_state)
    TextView detailTvState;
    @BindView(R.id.detail_iv_share)
    ImageView detailIvShare;
    @BindView(R.id.detail_iv_book)
    ImageView detailIvBook;
    @BindView(R.id.detail_tv_book_name)
    TextView detailTvBookName;
    @BindView(R.id.detail_tv_author)
    TextView detailTvAuthor;
    @BindView(R.id.detail_tv_challenge_money)
    TextView detailTvChallengeMoney;
    @BindView(R.id.detail_tv_start_time)
    TextView detailTvStartTime;
    @BindView(R.id.detail_tv_end_time)
    TextView detailTvEndTime;
    @BindView(R.id.detail_match_people_count)
    TextView detailMatchPeopleCount;
    @BindView(R.id.detail_match_checkbox)
    CheckBox detailMatchCheckbox;
    @BindView(R.id.detail_match_rule)
    TextView detailMatchRule;
    @BindView(R.id.detail_match_challege_state)
    Button detailMatchChallegeState;
    @BindView(R.id.detail_match_accept_challege)
    Button detailMatchAcceptChallege;
    @BindView(R.id.detail_match_ll_challege)
    LinearLayout detailMatchLlChallege;
    @BindView(R.id.detail_match_people_container)
    LinearLayout detailMatchPeopleContainer;
    @BindView(R.id.detail_tv_question)
    TextView detailTvQuestion;
    @BindView(R.id.ll_detail_match_challege_state)
    LinearLayout llDetailMatchChallegeState;
    @BindView(R.id.detail_match_ll_rule)
    LinearLayout detailMatchLlRule;
    private ChallengeDetailBean.ResponseDataBean respondataBean;
    private List<ChallengeDetailBean.ResponseDataBean.ChallengeUserListBean> challengeUserList;
    private ChallengeDetailBean.ResponseDataBean.ChallengeUserListBean challengeUserListBean;
    Timer timer = new Timer();
    private String startTime;
    private String challengeId;
    private BookDetailBean bookDetailBean;
    private BookDetailBean.ResponseDataBean responseDataBean;
    private ShareInfoBean.ResponseDataBean shareinfo;
    private double accountMoney = 0.00;
    private ApplyBean applyBean;
    private DownloadProgressDialog downloaddialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_challenge_detail;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        challengeId = intent.getStringExtra("challengeId");
        detailMatchCheckbox.setChecked(true);//默认选中
        Call<ChallengeDetailBean> callengeDetailCall = ReaderRetroift.getInstance(this).getApi().callengeDetailCall(challengeId);
        callengeDetailCall.enqueue(new Callback<ChallengeDetailBean>() {
            @Override
            public void onResponse(Call<ChallengeDetailBean> call, Response<ChallengeDetailBean> response) {
                ChallengeDetailBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    respondataBean = bean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<ChallengeDetailBean> call, Throwable t) {

            }
        });

    }

    private void initData() {
        //账户余额
        Call<BalanceBean> balanceCall = ReaderRetroift.getInstance(ChallengeDetailActivity.this).getApi().getBalanceCall();
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

        final int challengeStatus = respondataBean.getChallengeStatus();//获取挑战状态
        int joinStatus = respondataBean.getJoinStatus();//获取加入状态
        detailTvState.setText(respondataBean.getChallengeStatusValue());//标题状态
        GlideLoadUtils.getInstance().glideLoad(this,ReaderRetroift.IMAGE_URL + respondataBean.getBookInfo().getCoverPath(),detailIvBook,0);
        detailIvBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //书籍详情页里
                if (respondataBean.getBookInfo().getStatus() == 0) {
                    Toast.makeText(ChallengeDetailActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
                } else {
                    Intent detailintent = new Intent(ChallengeDetailActivity.this, BookDetailActivity.class);
                    detailintent.putExtra("bookid", respondataBean.getBookId());
                    startActivity(detailintent);

                }
            }
        });
        detailTvBookName.setText(respondataBean.getBookInfo().getName());//书名
        detailTvAuthor.setText(respondataBean.getBookInfo().getAuthor());//作者
        String money = String.valueOf((int) respondataBean.getTotalMoney());
        detailTvChallengeMoney.setText("" + money);//挑战金总额
        startTime = respondataBean.getStartTime();
        StringBuilder startsb = new StringBuilder();
        for (int i = 0; i < startTime.length(); i++) {
            startsb.append(startTime.charAt(i));
            if (i == 3 || i == 5) {
                startsb.append(".");
            }
        }
        detailTvStartTime.setText(startsb.toString());
        String endTime = respondataBean.getEndTime();//结束时间
        StringBuilder endsb = new StringBuilder();
        for (int i = 0; i < endTime.length(); i++) {
            endsb.append(endTime.charAt(i));
            if (i == 3 || i == 5) {
                endsb.append(".");
            }
        }
        detailTvEndTime.setText(endsb.toString());
        detailMatchPeopleCount.setText(respondataBean.getTotal() + "人");
        detailMatchPeopleCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全部参赛者
                Intent listIntent = new Intent(ChallengeDetailActivity.this, ChallengeUserActivity.class);
                listIntent.putExtra("challengeId", challengeId);
                startActivityForResult(listIntent, 0);
            }
        });
        //挑战者列表
        challengeUserList = respondataBean.getChallengeUserList();
        detailMatchPeopleContainer.removeAllViews();
        for (int i = 0; i < challengeUserList.size(); i++) {
            challengeUserListBean = challengeUserList.get(i);
            View childView = View.inflate(this, R.layout.item_match_people, null);
            CircleImageView ivPortrait = (CircleImageView) childView.findViewById(R.id.item_match_portrait);
            TextView tvOriginator = (TextView) childView.findViewById(R.id.item_match_originator);
            TextView tvName = (TextView) childView.findViewById(R.id.item_match_name);
            TextView tvTime = (TextView) childView.findViewById(R.id.item_match_read_time);
            final CheckBox cbLike = (CheckBox) childView.findViewById(R.id.item_match_like);
            ImageView ivSuccess = (ImageView) childView.findViewById(R.id.item_match_challege_success);
            ImageView ivFailed = (ImageView) childView.findViewById(R.id.item_match_challege_failed);
            if (challengeStatus == 3) {
                if (challengeUserListBean.getResult() == 2) {
                    //挑战失败
                    ivFailed.setVisibility(View.VISIBLE);
                } else if (challengeUserListBean.getResult() == 4) {
                    //挑战成功
                    ivSuccess.setVisibility(View.VISIBLE);
                }
            }
            if (challengeUserListBean.getParticipantUser() != null) {
                Glide.with(this).load(ReaderRetroift.IMAGE_URL + challengeUserListBean.getParticipantUser().getNormalPath()).into(ivPortrait);//头像
                tvName.setText(challengeUserListBean.getParticipantUser().getNickName());//昵称
            }

            if (i != 0) {
                tvOriginator.setVisibility(View.GONE);//发起者
            }
            int readTime = challengeUserListBean.getReadTime();//时间
            int readhour = readTime / 3600;
            int readminute = readTime / 60;
            if (readhour == 0) {
                tvTime.setText(readminute + "分钟");
            } else {
                readminute = readminute - readhour*60;
                tvTime.setText(readhour + "小时" + readminute + "分钟");
            }
            final int[] praisenum = {challengeUserListBean.getTotalPraise()};

            cbLike.setText(praisenum[0] + "");
            cbLike.setChecked(challengeUserListBean.isPraiseFlag());
            detailMatchPeopleContainer.addView(childView);
        }
        for (int i = 0; i < challengeUserList.size(); i++) {
            final ChallengeDetailBean.ResponseDataBean.ChallengeUserListBean bean = challengeUserList.get(i);
            CircleImageView portrait = (CircleImageView) detailMatchPeopleContainer.getChildAt(i).findViewById(R.id.item_match_portrait);
            portrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Ta的详情页
                    Intent intent = new Intent(ChallengeDetailActivity.this, PersonDetailActivity.class);
                    intent.putExtra("userId", bean.getUserId());
                    startActivity(intent);
                }
            });
        }
        for (int i = 0; i < challengeUserList.size(); i++) {
            final int[] praisenum = {challengeUserList.get(i).getTotalPraise()};
            final CheckBox cb = (CheckBox) detailMatchPeopleContainer.getChildAt(i).findViewById(R.id.item_match_like);
            final int finalI = i;
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //点赞
                        Call<ModifyBean> challengePraiseCall = ReaderRetroift.getInstance(ChallengeDetailActivity.this).getApi().challengePraiseCall(challengeUserList.get(finalI).getId());
                        challengePraiseCall.enqueue(new Callback<ModifyBean>() {
                            @Override
                            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                                ModifyBean bean = response.body();
                                if (bean != null) {
                                    praisenum[0] += 1;
                                    cb.setText(praisenum[0] + "");
                                }
                            }

                            @Override
                            public void onFailure(Call<ModifyBean> call, Throwable t) {

                            }
                        });
                    } else {
                        //取消点赞
                        Call<ModifyBean> praiseCancelCall = ReaderRetroift.getInstance(ChallengeDetailActivity.this).getApi().challengePraiseCancelCall(challengeUserList.get(finalI).getId());
                        praiseCancelCall.enqueue(new Callback<ModifyBean>() {
                            @Override
                            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                                ModifyBean bean = response.body();
                                if (bean != null) {
                                    praisenum[0] -= 1;
                                    cb.setText(praisenum[0] + "");
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

        //当前用户没参与的情况下
        if (joinStatus == 1) {
            if (challengeStatus == 1) {
                //即将开始，加入挑战
                detailMatchChallegeState.setText("加入挑战");
                detailMatchChallegeState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (respondataBean.getBookInfo().getStatus() == 0){
                            Toast.makeText(ChallengeDetailActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!detailMatchCheckbox.isChecked()) {
                            Toast.makeText(ChallengeDetailActivity.this, "同意挑战规则才可加入挑战", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Call<ModifyBean> joinChallengeCall = ReaderRetroift.getInstance(ChallengeDetailActivity.this).getApi().joinChallengeCall(challengeId);
                        joinChallengeCall.enqueue(new Callback<ModifyBean>() {
                            @Override
                            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                                ModifyBean bean = response.body();
                                if (bean != null && bean.getResponseCode() == 1) {
                                    //支付金额 TODO
                                    payChallenge();
                                } else {
                                    Toast.makeText(ChallengeDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ModifyBean> call, Throwable t) {

                            }
                        });
                    }
                });
            }
            if (challengeStatus == 2 || challengeStatus == 3) {
                //已经开始或者已经结束，按钮消失
                llDetailMatchChallegeState.setVisibility(View.GONE);
                detailMatchLlRule.setVisibility(View.GONE);
            }
        } else {
            //当前用户参与
            detailMatchLlRule.setVisibility(View.GONE);
            if (challengeStatus == 1) {
                //即将开始，倒计时
                timer.schedule(task, 1000, 1000);
            } else if (challengeStatus == 2) {
                //激战正酣，阅读书籍
                detailMatchChallegeState.setText("阅读书籍");
                detailMatchChallegeState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (respondataBean.getBookInfo().getStatus() == 0){
                            Toast.makeText(ChallengeDetailActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LoadDialog.show(ChallengeDetailActivity.this);
                        //请求获取书籍详情
                        Call<BookDetailBean> bookDetailCall = ReaderRetroift.getInstance(ChallengeDetailActivity.this).getApi().BookDetailCall(respondataBean.getBookInfo().getId());
                        bookDetailCall.enqueue(new Callback<BookDetailBean>() {
                            @Override
                            public void onResponse(Call<BookDetailBean> call, Response<BookDetailBean> response) {
                                bookDetailBean = response.body();
                                if (bookDetailBean != null && bookDetailBean.getResponseData() != null) {
                                    responseDataBean = bookDetailBean.getResponseData();
                                    readBook();
                                }
                            }

                            @Override
                            public void onFailure(Call<BookDetailBean> call, Throwable t) {

                            }
                        });
                    }
                });

            } else if (challengeStatus == 3) {
                //挑战结束，按钮消失
                llDetailMatchChallegeState.setVisibility(View.GONE);

            }
        }
    }

    private void payChallenge() {
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
        final double paymoney = respondataBean.getTotalMoney() / respondataBean.getTotal();
        DecimalFormat df = new DecimalFormat("######0.00");
        String formatMoney = df.format(paymoney);
        tv_money.setText("" + formatMoney);
        tv_bookname.setText(respondataBean.getBookInfo().getName());
        tv_time.setText(detailTvStartTime.getText().toString() + "~" + detailTvEndTime.getText().toString());
        DecimalFormat df1 = new DecimalFormat("######0.00");
        String account = df1.format(accountMoney);
        tv_balance.setText("账户余额：" + account);
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
                    final ProgressDialog progressDialog = new ProgressDialog(ChallengeDetailActivity.this);
                    progressDialog.setTitle(null);
                    progressDialog.setMessage("正在支付...");
                    progressDialog.show();
                    Call<ModifyBean> payChallenge = ReaderRetroift.getInstance(ChallengeDetailActivity.this).getApi().payChallenge(challengeId, (respondataBean.getTotalMoney() / respondataBean.getTotal()));
                    payChallenge.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                if (bean.getResponseCode() == 1) {
                                    Toast.makeText(ChallengeDetailActivity.this, "已加入挑战", Toast.LENGTH_LONG).show();
//                        detailMatchChallegeState.setVisibility(View.GONE);
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
        android.view.WindowManager.LayoutParams p = payChallengeDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        payChallengeDialog.getWindow().setAttributes(p); //设置生效
        payChallengeDialog.setContentView(contentView);
        payChallengeDialog.show();
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
                Dialog detailDialog = new Dialog(ChallengeDetailActivity.this,R.style.myDialogTheme);
                View contentView = LayoutInflater.from(ChallengeDetailActivity.this).inflate(R.layout.view_dialog_account_detail, null);
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
                        Call<ApplyBean> applyBeanCall = ReaderRetroift.getInstance(ChallengeDetailActivity.this).getApi().applyMoney(rechargeprice);
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
                    Toast.makeText(ChallengeDetailActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
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
                PayTask alipay = new PayTask(ChallengeDetailActivity.this);
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
                        Toast.makeText(ChallengeDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ChallengeDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                //刷新下参赛者
                Call<ChallengeDetailBean> callengeDetailCall = ReaderRetroift.getInstance(this).getApi().callengeDetailCall(challengeId);
                callengeDetailCall.enqueue(new Callback<ChallengeDetailBean>() {
                    @Override
                    public void onResponse(Call<ChallengeDetailBean> call, Response<ChallengeDetailBean> response) {
                        ChallengeDetailBean bean = response.body();
                        if (bean != null && bean.getResponseData() != null) {
                            respondataBean = bean.getResponseData();
                            List<ChallengeDetailBean.ResponseDataBean.ChallengeUserListBean> newList = respondataBean.getChallengeUserList();
                            for (int i = 0; i < newList.size(); i++) {
                                ChallengeDetailBean.ResponseDataBean.ChallengeUserListBean challengeUserListBean = newList.get(i);
                                final int[] praisenum = {challengeUserListBean.getTotalPraise()};
                                final CheckBox cb = (CheckBox) detailMatchPeopleContainer.getChildAt(i).findViewById(R.id.item_match_like);
                                cb.setText(praisenum[0] + "");
                                cb.setChecked(challengeUserListBean.isPraiseFlag());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChallengeDetailBean> call, Throwable t) {

                    }
                });
                break;
            default:
                break;
        }
    }

    private void readBook() {
        //判断下载路径是否为空
        if (respondataBean.getBookInfo().getPath() == null || "".equals(respondataBean.getBookInfo().getPath())){
            Toast.makeText(ChallengeDetailActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
            return;
        }
        //先判断本地是否有缓存
        File file = new File(Environment.getExternalStorageDirectory().getPath() + respondataBean.getBookInfo().getPath());
        if (!file.exists()) {
            //下载
            download();
        } else {
            Intent intent = new Intent();
            String paths = Environment.getExternalStorageDirectory().getPath() + respondataBean.getBookInfo().getPath();
            Uri uri = Uri.parse(paths);
            intent.setClass(this, FBReader.class);
            intent.setData(uri);
            Bundle bundle = new Bundle();
            //传递是否挑战书籍标识
            int challengeFlag = 1;
            intent.putExtra("challengeflag", challengeFlag);//0-没有挑战，1-挑战中
            //将书籍详情bean获取到
            bundle.putSerializable("bookdetail", responseDataBean);
            intent.putExtras(bundle);
            startActivity(intent);
            LoadDialog.dismiss(ChallengeDetailActivity.this);
        }
    }

    private void download() {
        downloaddialog = new DownloadProgressDialog(ChallengeDetailActivity.this);
        downloaddialog.setCancelable(false);
        downloaddialog.setCanceledOnTouchOutside(false);
        downloaddialog.show();
        FileDownloader.setup(this);
        FileDownloader.getImpl().create(ReaderRetroift.BASE_URL + "/download" + respondataBean.getBookInfo().getPath())
                .setPath(Environment.getExternalStorageDirectory().getPath() + respondataBean.getBookInfo().getPath())
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
                        Toast.makeText(ChallengeDetailActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        String paths = Environment.getExternalStorageDirectory().getPath() + respondataBean.getBookInfo().getPath();
                        Uri uri = Uri.parse(paths);
                        intent.setClass(ChallengeDetailActivity.this, FBReader.class);
                        intent.setData(uri);
                        Bundle bundle = new Bundle();
                        int challengeFlag = 1;
                        intent.putExtra("challengeflag", challengeFlag);//0-没有挑战，1-挑战中
                        bundle.putSerializable("bookdetail", responseDataBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Toast.makeText(ChallengeDetailActivity.this, "下载失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        downloaddialog.dismiss();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }




    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            String[] timearr = TimeUtil.getDifference(startTime);
            final String day = timearr[0];
            final String hour = timearr[1];
            final String minute = timearr[2];
            final String second = timearr[3];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    detailMatchChallegeState.setText("开始倒计时 " + day + "天" + hour + ":" + minute + ":" + second);
                }
            });
        }
    };

    @OnClick({R.id.detail_iv_return, R.id.detail_iv_share, R.id.detail_iv_book, R.id.detail_match_challege_state, R.id.detail_match_accept_challege, R.id.detail_tv_question, R.id.detail_match_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_iv_return:
                finish();
                break;
            case R.id.detail_iv_share:
                share();
                break;
            case R.id.detail_iv_book:
                break;
            case R.id.detail_match_challege_state:
                break;
            case R.id.detail_match_accept_challege:
                break;
            case R.id.detail_tv_question:
                challengeQuestion();
                break;
            case R.id.detail_match_rule:
                Intent intent = new Intent(this, agreementActivity.class);
                intent.putExtra("url", "http://test1.17dubei.com/rule.html");
                startActivity(intent);
                break;
        }
    }

    private void share() {
        //获取分享信息
        Call<ShareInfoBean> shareInfoCall = ReaderRetroift.getInstance(this).getApi().shareInfoCall(challengeId, "challenge");
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
        web.setThumb(new UMImage(ChallengeDetailActivity.this, ReaderRetroift.IMAGE_URL + shareinfo.getImagePath()));  //缩略图
        web.setDescription(shareinfo.getSummary());//描述

        final Dialog bottomDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_share, null);
        TextView tv_wx = (TextView) contentView.findViewById(R.id.dialog_share_wx);
        TextView tv_pengyouquan = (TextView) contentView.findViewById(R.id.dialog_share_pengyouquan);
        Button btn_cancel = (Button) contentView.findViewById(R.id.dialog_share_cancel);
        tv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享微信好友,分享成功之后调用分享接口
                new ShareAction(ChallengeDetailActivity.this)
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
                new ShareAction(ChallengeDetailActivity.this)
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
            Toast.makeText(ChallengeDetailActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ChallengeDetailActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ChallengeDetailActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };

    private void challengeQuestion() {
        final Dialog ruleDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_challenge_question, null);
        TextView done = (TextView) contentView.findViewById(R.id.dialog_question_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ruleDialog.dismiss();
            }
        });
        android.view.WindowManager.LayoutParams p = ruleDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        ruleDialog.getWindow().setAttributes(p); //设置生效
        ruleDialog.setContentView(contentView);
        ruleDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
