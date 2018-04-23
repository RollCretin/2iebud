package com.hxjf.dubei.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gigamole.library.ShadowLayout;
import com.google.gson.Gson;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.DuBeiBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.BookDetailActivity;
import com.hxjf.dubei.ui.activity.CanJoinChallengeActivity;
import com.hxjf.dubei.ui.activity.MobileVerificationActivity;
import com.hxjf.dubei.ui.activity.MyChallengeActivity;
import com.hxjf.dubei.ui.activity.SettingChallegeDetailActivity;
import com.hxjf.dubei.ui.activity.registerSuccessActivity;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.ScreenSizeUtil;
import com.hxjf.dubei.util.TimeUtil;
import com.hxjf.dubei.widget.CardView;
import com.hxjf.dubei.widget.ChallegeDubiCardView;
import com.hxjf.dubei.widget.ChallegeTimeCardView;
import com.hxjf.dubei.widget.DownloadProgressDialog;
import com.hxjf.dubei.widget.NineGridImageView;
import com.hxjf.dubei.widget.UnChallegeCardView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import org.geometerplus.android.fbreader.FBReader;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Chen_Zhang on 2017/6/5.
 */

public class CardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<DuBeiBean.ResponseDataBean.ChallengeInfoBean> mChallengeList;
    private List<DuBeiBean.ResponseDataBean.RecommendInfoBean> mRecommendList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener = null;
    private List<String> totalList;
    private DuBeiBean.ResponseDataBean.ChallengeInfoBean challengeInfoBean;
    private DuBeiBean.ResponseDataBean.RecommendInfoBean recommendInfoBean;
    int challengeNum = 0;
    int recLen = 100;
    private DownloadProgressDialog downloaddialog;
    private int isGiveReadCoin;//0-未领取，1-已领取
    private int currentFirst;//First位置（领读币）是否存在，1存在，0不存在

    public static enum ITEM_TYPE {
        ITEM_TYPE_CHALLEGE, ITEM_TYPE_NO_CHALLEGE, ITEM_TYPE_TIME, ITEM_TYPE_DUBI
    }

    /**
     * 传递数据
     */
    public void setData(int num) {
        //总的挑战次数
        challengeNum = num;
    }


    public CardViewAdapter(Context context, int giveReadCoin, List<DuBeiBean.ResponseDataBean.ChallengeInfoBean> challengeList, List<DuBeiBean.ResponseDataBean.RecommendInfoBean> recommendList) {
        this.mContext = context;
        isGiveReadCoin = giveReadCoin;
        if (isGiveReadCoin == 0) {
            currentFirst = 1;
        } else {
            currentFirst = 0;
        }
        mChallengeList = challengeList;
        mRecommendList = recommendList;
        totalList = new ArrayList<>();
        if (giveReadCoin == 0) {
            totalList.add("dubi" + 1);
        }
        if (mChallengeList != null) {
            for (int i = 0; i < mChallengeList.size(); i++) {
                totalList.add("challenge" + i);
            }
        }
        if (mRecommendList != null) {
            for (int i = 0; i < mRecommendList.size(); i++) {
                totalList.add("recommend" + i);
            }
        }
        totalList.add("all" + 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE.ITEM_TYPE_CHALLEGE.ordinal()) {
            View cardview = new CardView(mContext);
            cardview.setOnClickListener(this);
            return new ChallegeViewHolder(cardview);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_NO_CHALLEGE.ordinal()) {
            View unChallegeCardView = new UnChallegeCardView(mContext);
            return new NoChallegeViewHolder(unChallegeCardView, mContext);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_DUBI.ordinal()) {
            View challegeDubiCardView = new ChallegeDubiCardView(mContext);
            return new ChallegeDubiViewHolder(challegeDubiCardView);
        } else {
            View challegeTimeCardView = new ChallegeTimeCardView(mContext);
            return new ChallegeTimeViewHolder(challegeTimeCardView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (totalList.get(position).contains("challenge")) {
            type = ITEM_TYPE.ITEM_TYPE_CHALLEGE.ordinal();
        } else if (totalList.get(position).contains("recommend")) {
            type = ITEM_TYPE.ITEM_TYPE_NO_CHALLEGE.ordinal();
        } else if (totalList.get(position).contains("dubi")) {
            type = ITEM_TYPE.ITEM_TYPE_DUBI.ordinal();
        } else {
            type = ITEM_TYPE.ITEM_TYPE_TIME.ordinal();
        }
        return type;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //TODO

        String s = totalList.get(position);
        if (s.contains("challenge")) {
            challengeInfoBean = mChallengeList.get(position - currentFirst);
        } else if (s.contains("recommend")) {
            recommendInfoBean = mRecommendList.get(position - mChallengeList.size() - currentFirst);
        }

        if (holder instanceof ChallegeViewHolder) {
            if (challengeInfoBean != null) {

                List<String> images = new ArrayList<>();
                NineGridImageView.NineGridImageViewAdapter<String> mAdapter = new NineGridImageView.NineGridImageViewAdapter<String>() {
                    @Override
                    protected void onDisplayImage(Context context, ImageView imageView, String s) {
                        Glide.with(context).load(s).placeholder(R.mipmap.ic_launcher).into(imageView);
                    }

                    @Override
                    protected ImageView generateImageView(Context context) {
                        return super.generateImageView(context);
                    }
                };

                //书籍挑战
                //获取挑战者信息列表
                List<DuBeiBean.ResponseDataBean.ChallengeInfoBean.ChallengeUserListBean> challengeUserList = challengeInfoBean.getChallengeUserList();
                if (challengeUserList.size() == 1) {
                    //只有自己的情况
                    ((ChallegeViewHolder) holder).ivothernullProtrait.setVisibility(View.VISIBLE);
                    ((ChallegeViewHolder) holder).rlOthers.setVisibility(View.GONE);
                } else {
                    ((ChallegeViewHolder) holder).ivothernullProtrait.setVisibility(View.GONE);
                    ((ChallegeViewHolder) holder).rlOthers.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < challengeUserList.size(); i++) {
                    if (i == 0) {
                        //发起者头像
                        if (challengeUserList.get(i).getParticipantUser() != null) {
                            Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + challengeUserList.get(i).getParticipantUser().getNormalPath()).into(((ChallegeViewHolder) holder).ivMyProtrait);
                        }
                    } else {
                        if (challengeUserList.get(i).getParticipantUser() != null) {
                            //其他用户头像
                            images.add(ReaderRetroift.IMAGE_URL + challengeUserList.get(i).getParticipantUser().getNormalPath());
                        }
                    }
                }
                ((ChallegeViewHolder) holder).nineView.setAdapter(mAdapter);
                ((ChallegeViewHolder) holder).nineView.setImagesData(images);//其他人组合头像
                ((ChallegeViewHolder) holder).tvType.setText(challengeInfoBean.getChallengeStatusValue());//挑战状态
                String startTime = challengeInfoBean.getStartTime();
                String endTime = challengeInfoBean.getEndTime();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < startTime.length(); i++) {
                    sb.append(startTime.charAt(i));
                    if (i == 3 || i == 5) {
                        sb.append(".");
                    }
                }
                String newstartTime = sb.toString();
                ((ChallegeViewHolder) holder).tvTime.setText(newstartTime);//挑战开始时间
                ((ChallegeViewHolder) holder).tvMyName.setText(challengeInfoBean.getChallengeUserList().get(0).getParticipantUser().getNickName());//发起者昵称
                if (challengeInfoBean.getTotal() == 1) {
                    ((ChallegeViewHolder) holder).tvOtherName.setText("0人");
                } else {
                    ((ChallegeViewHolder) holder).tvOtherName.setText(challengeInfoBean.getTotal() - 1 + "人");//人数

                }
                String normalPath = challengeInfoBean.getBookInfo().getCoverPath();
                Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + normalPath).into(((ChallegeViewHolder) holder).ivBookCover);//书籍封面
                ((ChallegeViewHolder) holder).tvBookName.setText(challengeInfoBean.getBookInfo().getName());
                ((ChallegeViewHolder) holder).tvBookAuthor.setText(challengeInfoBean.getBookInfo().getAuthor());
                ((ChallegeViewHolder) holder).ivBookCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int layoutPosition = holder.getLayoutPosition();
                        String s2 = totalList.get(layoutPosition);
                        if (s2.contains("challenge")) {
                            DuBeiBean.ResponseDataBean.ChallengeInfoBean newbean = mChallengeList.get(position-currentFirst);
                            if (newbean.getBookInfo().getStatus() == 0) {
                                Toast.makeText(mContext, "该书已下架", Toast.LENGTH_SHORT).show();
                            } else if (newbean.getChallengeStatus() == 2) {
                                //激战正酣的时候，直接进阅读器
                                readBook(newbean);
                            }
                        }
                    }
                });
                //倒计时设置
                if (challengeInfoBean.getChallengeStatus() == 2) {
                    //激战正酣，结束时间-开始时间；
                    String[] difference = TimeUtil.getDifference2(endTime);
                    ((ChallegeViewHolder) holder).tvDay.setText(difference[0]);
                    ((ChallegeViewHolder) holder).tvHour.setText(difference[1]);
                    ((ChallegeViewHolder) holder).tvMinute.setText(difference[2]);
                    ((ChallegeViewHolder) holder).tvSecond.setText(difference[3]);
                    ((ChallegeViewHolder) holder).slShadow.setIsShadowed(true);
                    ((ChallegeViewHolder) holder).slShadow.setShadowAngle(45);
                    ((ChallegeViewHolder) holder).slShadow.setShadowRadius(15);
                    ((ChallegeViewHolder) holder).slShadow.setShadowDistance(15);
                    ((ChallegeViewHolder) holder).slShadow.setShadowColor(mContext.getResources().getColor(R.color.sec_gray));

                } else if (challengeInfoBean.getChallengeStatus() == 1) {
                    //即将开始
                    ((ChallegeViewHolder) holder).slShadow.setIsShadowed(false);
                    ((ChallegeViewHolder) holder).slShadow.setShadowAngle(0);
                    ((ChallegeViewHolder) holder).slShadow.setShadowRadius(15);
                    ((ChallegeViewHolder) holder).slShadow.setShadowDistance(15);
                    ((ChallegeViewHolder) holder).slShadow.setShadowColor(mContext.getResources().getColor(R.color.sec_gray));
                    //挑战开始的时候，结束时间-当前时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                    Date parse1 = null;
                    Date parse2 = null;
                    try {
                        parse1 = format.parse(startTime);
                        parse2 = format.parse(endTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long starttime = parse1.getTime();
                    long endtime = parse2.getTime();
                    int daycountNum = (int) ((endtime - starttime) / (1000 * 60 * 60 * 24));//相差天数
                    ((ChallegeViewHolder) holder).tvDay.setText(daycountNum + 1 + "");
                    ((ChallegeViewHolder) holder).tvHour.setText("00");
                    ((ChallegeViewHolder) holder).tvMinute.setText("00");
                    ((ChallegeViewHolder) holder).tvSecond.setText("00");
                }
            }


        } else if (holder instanceof NoChallegeViewHolder) {
            if (recommendInfoBean != null) {
                //推荐书籍
                if (totalList.get(0).contains("challenge")) {
                    ((NoChallegeViewHolder) holder).tvUnchallegetv.setVisibility(View.INVISIBLE);
                }
                Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + recommendInfoBean.getCoverPath()).into(((NoChallegeViewHolder) holder).ivBookCover);
                ((NoChallegeViewHolder) holder).tvBookName.setText(recommendInfoBean.getName());
                ((NoChallegeViewHolder) holder).tvBookAuthor.setText(recommendInfoBean.getAuthor());
                ((NoChallegeViewHolder) holder).ivBookCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //书籍详情页
                        int layoutPosition = holder.getLayoutPosition();
                        String s1 = totalList.get(layoutPosition);
                        if (s1.contains("recommend")) {
                            DuBeiBean.ResponseDataBean.RecommendInfoBean newrecommendInfoBean = mRecommendList.get(layoutPosition - mChallengeList.size()-currentFirst);
                            Intent bookdetailIntent = new Intent(mContext, BookDetailActivity.class);
                            bookdetailIntent.putExtra("bookid", newrecommendInfoBean.getId());
                            mContext.startActivity(bookdetailIntent);
                        }
                    }
                });

                ((NoChallegeViewHolder) holder).tvLaunchChallege.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //发起挑战
                        int layoutPosition = holder.getLayoutPosition();
                        String s1 = totalList.get(layoutPosition);
                        if (s1.contains("recommend")) {
                            DuBeiBean.ResponseDataBean.RecommendInfoBean newrecommendInfoBean = mRecommendList.get(layoutPosition - mChallengeList.size()-currentFirst);
                            Intent intent = new Intent(mContext, SettingChallegeDetailActivity.class);
                            intent.putExtra("bookId", newrecommendInfoBean.getId());
                            intent.putExtra("bookName", newrecommendInfoBean.getName());
                            mContext.startActivity(intent);
                        }
                    }
                });
                ((NoChallegeViewHolder) holder).tvJoinChallege.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //参与挑战
                        int layoutPosition = holder.getLayoutPosition();
                        String s1 = totalList.get(layoutPosition);
                        if (s1.contains("recommend")) {
                            DuBeiBean.ResponseDataBean.RecommendInfoBean newrecommendInfoBean = mRecommendList.get(layoutPosition - mChallengeList.size()-currentFirst);
                            Intent intent = new Intent(mContext, CanJoinChallengeActivity.class);
                            intent.putExtra("bookId", newrecommendInfoBean.getId());
                            mContext.startActivity(intent);
                        }
                    }
                });
            }


        } else if (holder instanceof ChallegeTimeViewHolder) {
            ((ChallegeTimeViewHolder) holder).tvNum.setText(challengeNum + "");
            ((ChallegeTimeViewHolder) holder).tvAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //所有挑战
                    Intent intent = new Intent(mContext, MyChallengeActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof ChallegeDubiViewHolder) {
            ((ChallegeDubiViewHolder) holder).tvDubiUse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog detailDialog = new Dialog(mContext, R.style.myDialogTheme);
                    View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_dubi_use, null);
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
                    p.width = (int) (ScreenSizeUtil.getScreenWidth(mContext) * 0.7); //宽度设置为屏幕的
                    detailDialog.getWindow().setAttributes(p); //设置生效

                }
            });

            ((ChallegeDubiViewHolder) holder).tvDubiGet.setOnClickListener(new View.OnClickListener() {

                private String telephone;

                @Override
                public void onClick(View v) {
                    //先判断是否注册手机号
                    String bindbeanStr = SPUtils.getString(mContext, "bindbean", "");
                    Gson gson = new Gson();
                    final UserDetailBean userbean = gson.fromJson(bindbeanStr, UserDetailBean.class);
                    if (userbean != null && userbean.getResponseData() != null) {
                        telephone = userbean.getResponseData().getTelephone();
                    }
                    if (telephone == null || "".equals(telephone)) {
                        //跳转到手机注册页面
                        Intent mobileIntent = new Intent(mContext, MobileVerificationActivity.class);
                        mContext.startActivity(mobileIntent);
                    } else {
                        Call<ModifyBean> getDubiCall = ReaderRetroift.getInstance(mContext).getApi().getDubi();
                        getDubiCall.enqueue(new Callback<ModifyBean>() {
                            @Override
                            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                                ModifyBean modifyBean = response.body();
                                if (modifyBean != null) {
                                    if (modifyBean.getResponseCode() == 1) {
                                        //跳转到成功领取界面
                                        Intent successIntent = new Intent(mContext,registerSuccessActivity.class);
                                        mContext.startActivity(successIntent);
                                    } else {
                                        Toast.makeText(mContext, modifyBean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                                    }
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
        holder.itemView.setTag(position);
    }

    private void readBook(DuBeiBean.ResponseDataBean.ChallengeInfoBean newbean) {
        //判断路径是否为空
        if (newbean.getBookInfo().getPath() == null || "".equals(newbean.getBookInfo().getPath())){
            Toast.makeText(mContext, "该书已下架", Toast.LENGTH_SHORT).show();
            return;
        }
        //先判断本地是否有缓存
        File file = new File(Environment.getExternalStorageDirectory().getPath() + newbean.getBookInfo().getPath());
        if (!file.exists()) {
            //下载
            download(newbean);
        } else {
            Intent intent = new Intent();
            String paths = Environment.getExternalStorageDirectory().getPath() + newbean.getBookInfo().getPath();
            Uri uri = Uri.parse(paths);
            intent.setClass(mContext, FBReader.class);
            intent.setData(uri);
            Bundle bundle = new Bundle();
            bundle.putSerializable("challenge_bookdetail", newbean.getBookInfo());
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }

    private void download(final DuBeiBean.ResponseDataBean.ChallengeInfoBean newbean) {
        downloaddialog = new DownloadProgressDialog(mContext);
        downloaddialog.setCancelable(false);
        downloaddialog.setCanceledOnTouchOutside(false);
        downloaddialog.show();
        FileDownloader.setup(mContext);
        FileDownloader.getImpl().create(ReaderRetroift.BASE_URL + "/download" + newbean.getBookInfo().getPath())
                .setPath(Environment.getExternalStorageDirectory().getPath() + newbean.getBookInfo().getPath())
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
                        String paths = Environment.getExternalStorageDirectory().getPath() + newbean.getBookInfo().getPath();
                        Uri uri = Uri.parse(paths);
                        intent.setClass(mContext, FBReader.class);
                        intent.setData(uri);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("challenge_bookdetail", newbean.getBookInfo());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Toast.makeText(mContext, "下载失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        downloaddialog.dismiss();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

    @Override
    public int getItemCount() {
        return totalList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    //设置接口回调
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //持有每个item的所有元素
    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener mListener;

        MyViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    //有挑战时的布局
    public static class ChallegeViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView ivMyProtrait;
        private final CircleImageView ivothernullProtrait;
        private final ImageView ivzhezhao;
        private final TextView tvType;
        private final TextView tvTime;
        private final TextView tvMyName;
        private final TextView tvOtherName;
        private final TextView tvDay;
        private final TextView tvHour;
        private final TextView tvMinute;
        private final TextView tvSecond;
        private final ShadowLayout slShadow;
        private final ImageView ivBookCover;
        private final TextView tvBookName;
        private final TextView tvBookAuthor;
        private final NineGridImageView nineView;
        private final RelativeLayout rlOthers;


        public ChallegeViewHolder(View itemView) {
            super(itemView);
            ivMyProtrait = (CircleImageView) itemView.findViewById(R.id.home_my_head_portrait);
            ivothernullProtrait = (CircleImageView) itemView.findViewById(R.id.home_others_head_portrait_null);
            ivzhezhao = (ImageView) itemView.findViewById(R.id.home_other_zhezhao);
            nineView = (NineGridImageView) itemView.findViewById(R.id.home_others_portrait);
            tvType = (TextView) itemView.findViewById(R.id.home_type);
            tvTime = (TextView) itemView.findViewById(R.id.home_time);
            tvMyName = (TextView) itemView.findViewById(R.id.home_my_name);
            tvOtherName = (TextView) itemView.findViewById(R.id.home_others_name);
            tvHour = (TextView) itemView.findViewById(R.id.home_countdown_hour);
            tvDay = (TextView) itemView.findViewById(R.id.count_of_day);
            tvMinute = (TextView) itemView.findViewById(R.id.home_countdown_minute);
            tvSecond = (TextView) itemView.findViewById(R.id.home_countdown_second);
            slShadow = (ShadowLayout) itemView.findViewById(R.id.home_shadow);
            ivBookCover = (ImageView) itemView.findViewById(R.id.home_book_cover);
            tvBookName = (TextView) itemView.findViewById(R.id.home_book_name);
            tvBookAuthor = (TextView) itemView.findViewById(R.id.home_book_auhtor);
            rlOthers = (RelativeLayout) itemView.findViewById(R.id.home_others_rl);
        }
    }

    //没有挑战时的布局
    public static class NoChallegeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivBookCover;
        private final TextView tvBookName;
        private final TextView tvBookAuthor;
        public final TextView tvLaunchChallege;
        private final TextView tvJoinChallege;
        private final TextView tvUnchallegetv;

        Context context;

        public NoChallegeViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context = context;
            ivBookCover = (ImageView) itemView.findViewById(R.id.un_challege_book_image);
            tvBookName = (TextView) itemView.findViewById(R.id.un_challege_book_name);
            tvBookAuthor = (TextView) itemView.findViewById(R.id.un_challege_book_author);
            tvLaunchChallege = (TextView) itemView.findViewById(R.id.un_challege_launch_challenge);
            tvJoinChallege = (TextView) itemView.findViewById(R.id.un_challege_join_challenge);
            tvUnchallegetv = (TextView) itemView.findViewById(R.id.un_challege_tv);

            ivBookCover.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    //记录挑战次数的布局
    public static class ChallegeTimeViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNum;
        private final TextView tvAll;

        public ChallegeTimeViewHolder(View itemView) {
            super(itemView);
            tvNum = (TextView) itemView.findViewById(R.id.challege_time_num);
            tvAll = (TextView) itemView.findViewById(R.id.challege_time_all);
        }
    }

    //领取读币的布局
    public static class ChallegeDubiViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvDubiUse;
        private final TextView tvDubiGet;

        public ChallegeDubiViewHolder(View itemView) {
            super(itemView);
            tvDubiUse = (TextView) itemView.findViewById(R.id.challege_give_dubi_purpose);
            tvDubiGet = (TextView) itemView.findViewById(R.id.challege_give_dubi_btn);
        }
    }

}
