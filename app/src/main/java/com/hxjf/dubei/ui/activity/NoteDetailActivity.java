package com.hxjf.dubei.ui.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.NoteFactoryDetailBean;
import com.hxjf.dubei.bean.ShareInfoBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.StatusBarUtils;
import com.hxjf.dubei.widget.SmartScrollView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.text.DecimalFormat;
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
 * Created by Chen_Zhang on 2017/7/16.
 */

public class NoteDetailActivity extends BaseActivity {
    private static final String TAG = "NoteDetailActivity";
    private static final int TIME_TO_SCROLL_VIEW = 1;
    @BindView(R.id.note_detail_bg)
    ImageView noteDetailBg;
    @BindView(R.id.note_detail_back)
    ImageView noteDetailBack;
    @BindView(R.id.note_detail_collection)
    CheckBox noteDetailCollection;
    @BindView(R.id.note_detail_share)
    ImageView noteDetailShare;
    @BindView(R.id.note_detail_name)
    TextView noteDetailName;
    @BindView(R.id.note_detail_catagory)
    TextView noteDetailCatagory;
    @BindView(R.id.note_detail_author)
    TextView noteDetailAuthor;
    @BindView(R.id.note_detail_text_num)
    TextView noteDetailTextNum;
    @BindView(R.id.note_detail_people)
    TextView noteDetailPeople;
    @BindView(R.id.note_detail_note_introduce)
    TextView noteDetailNoteIntroduce;
    @BindView(R.id.note_detail_cb_read_done)
    CheckBox noteDetailCbReadDone;
    @BindView(R.id.note_detail_ll_read_done)
    RelativeLayout noteDetailLlReadDone;
    @BindView(R.id.note_detail_scrollview)
    SmartScrollView noteDetailScrollview;
    @BindView(R.id.note_detail_container)
    LinearLayout noteDetailContainer;
    @BindView(R.id.note_detail_cb_rl)
    RelativeLayout noteDetailCbRl;
    @BindView(R.id.note_detail_pratroit)
    CircleImageView noteDetailPratroit;
    @BindView(R.id.note_detail_fromwho)
    TextView noteDetailFromwho;
    Boolean flag = true;
    int sum = 0;
    int freeSum = 0;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    private NoteFactoryDetailBean.ResponseDataBean responseData;
    private Timer timer = new Timer();
    private String noteFactoryId;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    sum++;
                    freeSum++;
                    if (freeSum >= 299) {
                        //如果299秒不动，sum置空
                        freeSum = 0;
                        sum = 0;
                    }
//                    Log.d(TAG, "handleMessage: "+sum+"，空闲时间："+freeSum);
                    if (sum >= 300) {
                        //5分钟同步一下,sum 置为0
                        updateReadTime(sum);
                        sum = 0;
                    }
            }
        }
    };
    private int height = 0;
    private String theoryReadTime;
    private String totalTime;
    private ShareInfoBean.ResponseDataBean shareinfo;
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
            Toast.makeText(NoteDetailActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(NoteDetailActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(NoteDetailActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public int getLayoutResId() {
        return R.layout.activity_note_detail;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
//        //状态栏沉浸
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        Intent intent = getIntent();
        noteFactoryId = intent.getStringExtra("noteFactoryId");
        //设置状态栏的颜色
        StatusBarUtils.setWindowStatusBarColor(NoteDetailActivity.this, R.color.note_bg);


        //开始计时
//            startTimer();

//        getReadTime(true);//获取阅读时间、判断按钮

        noteDetailScrollview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        height++;
                        freeSum = 0;
                        int scrollY = v.getScrollY();
                        int height = v.getHeight();
                        int scrollViewMeasuredHeight = noteDetailScrollview.getChildAt(0).getMeasuredHeight();
                        if (scrollY == 0) {
                            noteDetailCbRl.setBackgroundColor(getResources().getColor(R.color.transparent));
                        } else {
                            noteDetailCbRl.setBackgroundColor(getResources().getColor(R.color.note_bg));
                        }
                        break;
                }

                return false;
            }
        });

        //监听noteDetailScrollview的滚动
        noteDetailScrollview.setOnScrollChangeListener(new SmartScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(SmartScrollView scrollView, int l, int t, int oldl, int oldt) {
            }
        });

        //请求获取笔记详情数据
        Call<NoteFactoryDetailBean> noteFactoryDetailCall = ReaderRetroift.getInstance(this).getApi().noteFactoryDetailCall(noteFactoryId);
        noteFactoryDetailCall.enqueue(new Callback<NoteFactoryDetailBean>() {
            @Override
            public void onResponse(Call<NoteFactoryDetailBean> call, Response<NoteFactoryDetailBean> response) {
                NoteFactoryDetailBean bean = response.body();
                if (bean != null) {
                    responseData = bean.getResponseData();
                    if (bean.getResponseData() != null) {
                        initData();
                    }
                }
            }

            @Override
            public void onFailure(Call<NoteFactoryDetailBean> call, Throwable t) {

            }
        });
    }

    private void addBookshelf() {
//        请求加入书架
        Call<ModifyBean> addBookshelfCall = ReaderRetroift.getInstance(this).getApi().noteAddBookshelfCall(noteFactoryId);
        addBookshelfCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
    }

    private void cancelBookshelf() {
        Call<ModifyBean> noteRemoveBS = ReaderRetroift.getInstance(this).getApi().noteRemoveBS(noteFactoryId);
        noteRemoveBS.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    Toast.makeText(NoteDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initData() {
        if (responseData.isFinished()) {
            noteDetailCbReadDone.setClickable(false);
            noteDetailCbReadDone.setChecked(true);
            noteDetailCbReadDone.setText("已阅");
        }
        if (this != null && !NoteDetailActivity.this.isDestroyed()) {
            Glide.with(this).load(ReaderRetroift.IMAGE_URL + responseData.getCover()).into(noteDetailBg);
            Glide.with(this).load(ReaderRetroift.IMAGE_URL + responseData.getOwnerHeadPath()).into(noteDetailPratroit);
        }
        noteDetailFromwho.setText(responseData.getOwnerName());
        noteDetailName.setText(responseData.getTitle());
        noteDetailCatagory.setText(responseData.getTagValue());
        noteDetailAuthor.setText(responseData.getLecturer());
        int wordNumber = responseData.getWordNumber();
        float newNumber = (float) wordNumber / 10000;
        DecimalFormat df = new DecimalFormat("######0.00");
        noteDetailTextNum.setText("约" + df.format(newNumber) + "万字");
        noteDetailPeople.setText(responseData.getReadCount() + " 人已阅");
        noteDetailNoteIntroduce.setText(Html.fromHtml(responseData.getContentAbout()));
//        final String sText = "测试图片信息：<br><img src=\"http://pic004.cnblogs.com/news/201211/20121108_091749_1.jpg\" />";
//        noteDetailNoteDetail.setText(Html.fromHtml(sText, imageGetter, null));
//        noteDetailNoteDetail.setText(Html.fromHtml(responseData.getContent()));
        //添加WebView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(DensityUtil.dip2px(NoteDetailActivity.this, 15), 0, DensityUtil.dip2px(NoteDetailActivity.this, 15), 0);
        WebView webView = new WebView(this);
        webView.setLayoutParams(params);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        webView.loadData(getHtmlData(responseData.getContent()), "text/html; charset=utf-8", "utf-8");
        noteDetailContainer.addView(webView);

        noteDetailCollection.setChecked(responseData.isInBookShelf());

        noteDetailCbReadDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ReadDone();
                }
            }
        });
        noteDetailScrollview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NoteDetailActivity.this, "操作了", Toast.LENGTH_SHORT).show();
            }
        });
       /* noteDetailCbReadDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadDone();
                noteDetailCbReadDone.setChecked(false);
            }
        });*/


        noteDetailCollection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //加入书架
                    Toast.makeText(NoteDetailActivity.this, "笔记加入书架", Toast.LENGTH_SHORT).show();
                    addBookshelf();
                } else {
                    //从书架删除
                    cancelBookshelf();
                }
            }
        });

    }

    //为html内容添加头
    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private void ReadDone() {
        //请求完成阅读
        Call<ModifyBean> modifyBeanCall = ReaderRetroift.getInstance(this).getApi().noteReadDoneCall(noteFactoryId, sum);
        modifyBeanCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                    if (bean.getResponseCode() == 1) {
                        //阅读完成
                        noteDetailCbReadDone.setChecked(true);
                        noteDetailCbReadDone.setClickable(false);
                        noteDetailCbReadDone.setText("已阅");
                        Toast.makeText(NoteDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        noteDetailCbReadDone.setChecked(false);

                    }
                    Toast.makeText(NoteDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //同步一下阅读时间
        updateReadTime(sum);
//        Log.d(TAG, "onDestroy: 取消计时"+sum);
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();

        }
        startTimer();
//        Log.d(TAG, "onResume: 开始计时");
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateReadTime(sum);
        if (timer != null) {
            timer.cancel();
        }
//        Log.d(TAG, "onPause: 取消计时");
    }

    @OnClick({R.id.note_detail_back, R.id.note_detail_share, R.id.note_detail_note_introduce,R.id.note_detail_pratroit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.note_detail_back:
                finish();
                break;
            case R.id.note_detail_share:
                share();
                break;
            case R.id.note_detail_note_introduce:
                setExtend();
                break;
            case R.id.note_detail_pratroit:
                Intent personIntent = new Intent(this,PersonDetailActivity.class);
                personIntent.putExtra("userId",responseData.getOwnerId());
                startActivity(personIntent);
                break;
        }
    }

    private void share() {
        Call<ShareInfoBean> shareInfoCall = ReaderRetroift.getInstance(this).getApi().shareInfoCall(noteFactoryId, "noteFactory");
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
        web.setThumb(new UMImage(NoteDetailActivity.this, ReaderRetroift.IMAGE_URL + shareinfo.getImagePath()));  //缩略图
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
                new ShareAction(NoteDetailActivity.this)
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
                new ShareAction(NoteDetailActivity.this)
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
            noteDetailNoteIntroduce.setEllipsize(null); // 展开
            noteDetailNoteIntroduce.setSingleLine(flag);
        } else {
            flag = true;
            noteDetailNoteIntroduce.setMaxLines(4);
            noteDetailNoteIntroduce.setEllipsize(TextUtils.TruncateAt.END); // 收缩
        }
    }

    private void updateReadTime(int sum) {
        Call<ModifyBean> updateCall = ReaderRetroift.getInstance(this).getApi().updatenNoteFactoryCall(noteFactoryId, sum);
        updateCall.enqueue(new Callback<ModifyBean>() {
            @Override
            public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                ModifyBean bean = response.body();
                if (bean != null) {
                }
            }

            @Override
            public void onFailure(Call<ModifyBean> call, Throwable t) {

            }
        });
    }

    private void startTimer() {

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 0, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
