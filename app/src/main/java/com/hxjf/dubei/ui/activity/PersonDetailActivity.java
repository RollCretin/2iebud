package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.bumptech.glide.Glide;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.BSBookListBean;
import com.hxjf.dubei.bean.FavoriteBookListbean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.ImLoginHelper;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.ScreenSizeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * Created by Chen_Zhang on 2017/8/10.
 */

public class PersonDetailActivity extends BaseActivity {
    private static final String TAG = "PersonDetailActivity";
    @BindView(R.id.person_detail_scrollView)
    PullToZoomScrollViewEx personDetailScrollView;
    private ImageView back;
    private ImageView protrait;
    private TextView name;
    private TextView tabel2;
    private TextView tabel3;
    private TextView tabel1;
    private TextView attentionCount;
    private TextView autograph;
    private TextView achievement;
    private TextView privateletter;
    private TextView tvAttention;
    private LinearLayout llbookshelf;
    private LinearLayout llContainer;
    private UserDetailBean.ResponseDataBean detailBean;
    private String userId;
    private List<BSBookListBean.ResponseDataBean> booklist;
    private List<TextView> tvList;
    private LinearLayout llcollectbook;
    private LinearLayout llcollectbookContainer;
    private double longitude;
    private double latitude;
    private List<FavoriteBookListbean.ResponseDataBean.ContentBean> collectBookList;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_person_detail;
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
        userId = intent.getStringExtra("userId");
        String loc = SPUtils.getString(this, "location", "114.07/22.62");
        String[] split = loc.split("/");
        longitude = Double.valueOf(split[0]);
        latitude = Double.valueOf(split[1]);

        int[] screenWidthAndHeight = DensityUtil.getScreenWidthAndHeight(getContext());
        int mScreenWidth = screenWidthAndHeight[0];
        int mScreenHeight = screenWidthAndHeight[1];
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (6.0F * (mScreenHeight / 15.0F)));
        personDetailScrollView.setHeaderLayoutParams(localObject);

        View headView = LayoutInflater.from(getContext()).inflate(R.layout.person_detail_head_view, null, false);
        View zoomView = LayoutInflater.from(getContext()).inflate(R.layout.person_detail_zoom_view, null, false);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.person_detail_content_view, null, false);
        back = (ImageView) headView.findViewById(R.id.person_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        protrait = (ImageView) headView.findViewById(R.id.person_detail_protrait);
        name = (TextView) headView.findViewById(R.id.person_detail_name);
        attentionCount = (TextView) headView.findViewById(R.id.person_detail_attentionCount);
        tabel1 = (TextView) headView.findViewById(R.id.person_detail_tabel1);
        tabel2 = (TextView) headView.findViewById(R.id.person_detail_tabel2);
        tabel3 = (TextView) headView.findViewById(R.id.person_detail_tabel3);
        autograph = (TextView) headView.findViewById(R.id.person_detail_autograph);


        //成就
        achievement = (TextView) contentView.findViewById(R.id.person_detail_achievement);
        //私信
        privateletter = (TextView) contentView.findViewById(R.id.person_detail_private_letter);
        //Ta的书架
        llbookshelf = (LinearLayout) contentView.findViewById(R.id.person_detail_bookshelf);
        llContainer = (LinearLayout) contentView.findViewById(R.id.person_detail_ll_container);
        //Ta的藏书
        llcollectbook = (LinearLayout) contentView.findViewById(R.id.person_detail_collect_book);
        llcollectbookContainer = (LinearLayout) contentView.findViewById(R.id.person_detail_ll_collect_book);
        //关注
        tvAttention = (TextView) contentView.findViewById(R.id.person_detail_attention);

        tvList = new ArrayList<>();
        tvList.add(tabel1);
        tvList.add(tabel2);
        tvList.add(tabel3);


        personDetailScrollView.setHeaderView(headView);
        personDetailScrollView.setZoomView(zoomView);
        personDetailScrollView.setScrollContentView(contentView);

        Call<UserDetailBean> detailCall = ReaderRetroift.getInstance(this).getApi().usertDetailCall(userId);
        detailCall.enqueue(new Callback<UserDetailBean>() {
            @Override
            public void onResponse(Call<UserDetailBean> call, Response<UserDetailBean> response) {
                UserDetailBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    detailBean = bean.getResponseData();
                    initData();
                }
            }

            @Override
            public void onFailure(Call<UserDetailBean> call, Throwable t) {

            }
        });

        Call<FavoriteBookListbean> personFavoriteBookListCall = NewDiscoveryRetrofit.getInstance(this).getApi().PersonFavoriteBookListCall(userId, longitude, latitude, 1, 10);
        personFavoriteBookListCall.enqueue(new Callback<FavoriteBookListbean>() {
            @Override
            public void onResponse(Call<FavoriteBookListbean> call, Response<FavoriteBookListbean> response) {
                FavoriteBookListbean bean = response.body();
                if (bean != null){
                    if (bean.getResponseCode() == 1 && bean.getResponseData() != null){
                        collectBookList = bean.getResponseData().getContent();
                        initCollectBook();
                    }else{
                        Toast.makeText(PersonDetailActivity.this, bean.getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteBookListbean> call, Throwable t) {

            }
        });
    }

    private void initCollectBook() {
        //藏书列表
        for (int i = 0; i < collectBookList.size(); i++) {
            View child = View.inflate(PersonDetailActivity.this, R.layout.item_zone_book, null);
            final FavoriteBookListbean.ResponseDataBean.ContentBean bookFavoritesBean = collectBookList.get(i);
            ImageView bookBg = (ImageView) child.findViewById(R.id.zone_book_image);
            TextView bookName = (TextView) child.findViewById(R.id.zone_book_name);
            TextView bookScore = (TextView) child.findViewById(R.id.zone_book_score);
            GlideLoadUtils.getInstance().glideLoad(this,NewDiscoveryRetrofit.IMAGE_URL + bookFavoritesBean.getCover(),bookBg,0);
            bookName.setText(bookFavoritesBean.getName());
            bookScore.setText(bookFavoritesBean.getDoubanScore()+"分/豆瓣");
            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(DensityUtil.dip2px(PersonDetailActivity.this, 15), DensityUtil.dip2px(PersonDetailActivity.this, 20), 0, DensityUtil.dip2px(PersonDetailActivity.this, 15));
            param.gravity = Gravity.TOP;
            child.setLayoutParams(param);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonDetailActivity.this, BookBorrowDetailActivity.class);
                    intent.putExtra("bookFavoriteId", bookFavoritesBean.getId());
                    startActivity(intent);
                }
            });
            llcollectbookContainer.addView(child);
        }
        llcollectbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonDetailActivity.this, ZoneCollectBookActivity.class);
                intent.putExtra("ownerId",detailBean.getId());
                intent.putExtra("ownerName",detailBean.getNickName());
                startActivity(intent);
            }
        });
    }

    private void initData() {

        Glide.with(this).load(ReaderRetroift.IMAGE_URL + detailBean.getNormalPath()).into(protrait);
        name.setText(detailBean.getNickName());
        attentionCount.setText(detailBean.getAttentionCount()+" 人关注了Ta");
        String tag = detailBean.getTag();
        if (detailBean.isAttentionFlag()){
            tvAttention.setText("已关注");
            tvAttention.setTextColor(getResources().getColor(R.color.gray_text));
            tvAttention.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }else{
            tvAttention.setText("关注");
            tvAttention.setTextColor(getResources().getColor(R.color.black));
            Drawable plus = getResources().getDrawable(R.mipmap.plus_attention);
            tvAttention.setCompoundDrawablesWithIntrinsicBounds(plus,null,null,null);
            tvAttention.setCompoundDrawablePadding(5);
        }
        if (tag != null) {
            String[] split = tag.split("、");

            for (int i = 0; i < split.length; i++) {
                tvList.get(i).setVisibility(View.VISIBLE);
                tvList.get(i).setText(split[i]);
            }
        }
        autograph.setText(detailBean.getIntro());

        achievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonDetailActivity.this, PersonAchievementActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        privateletter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //私信
                YWIMKit imKit = ImLoginHelper.getInstance().getIMKit();
                Intent intent = imKit.getChattingActivityIntent(detailBean.getId(), ImLoginHelper.getInstance().getAPP_KEY());
                startActivity(intent);
            }
        });
        //关注
        tvAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailBean.isAttentionFlag()){
                    //点击后取消关注
                    cacelAttentionDialog();
                }else{
                    //点击后关注
                    tvAttention.setEnabled(false);
                    detailBean.setAttentionFlag(true);
                    Call<ModifyBean> addAttentionCall = ReaderRetroift.getInstance(PersonDetailActivity.this).getApi().addAttentionCall(detailBean.getId());
                    addAttentionCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            tvAttention.setEnabled(true);
                            tvAttention.setTextColor(getResources().getColor(R.color.gray_text));
                            tvAttention.setText("已关注");
                            tvAttention.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                            Toast.makeText(PersonDetailActivity.this, response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable throwable) {

                        }
                    });

                }
            }
        });

        Call<BSBookListBean> personBookListCall = ReaderRetroift.getInstance(this).getApi().personBookListCall(userId);
        personBookListCall.enqueue(new Callback<BSBookListBean>() {

            @Override
            public void onResponse(Call<BSBookListBean> call, Response<BSBookListBean> response) {
                BSBookListBean bean = response.body();
                if (bean != null && bean.getResponseData() != null) {
                    booklist = bean.getResponseData();
                    initBookList();
                }
            }

            @Override
            public void onFailure(Call<BSBookListBean> call, Throwable t) {

            }
        });
    }

    private void cacelAttentionDialog() {
        final Dialog detailDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_cancel_attention, null);
        TextView done = (TextView) contentView.findViewById(R.id.dialog_cancel_attention_done);
        TextView dlater = (TextView) contentView.findViewById(R.id.dialog_cancel_attention_later);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvAttention.setEnabled(false);
                detailBean.setAttentionFlag(false);
                Call<ModifyBean> cancelAttentionCall = ReaderRetroift.getInstance(PersonDetailActivity.this).getApi().cancelAttentionCall(detailBean.getId());
                cancelAttentionCall.enqueue(new Callback<ModifyBean>() {
                    @Override
                    public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                        tvAttention.setEnabled(true);
                        tvAttention.setText("关注");
                        tvAttention.setTextColor(getResources().getColor(R.color.black));
                        Drawable plus = getResources().getDrawable(R.mipmap.plus_attention);
                        tvAttention.setCompoundDrawablesWithIntrinsicBounds(plus,null,null,null);

                        detailDialog.dismiss();
                        Toast.makeText(PersonDetailActivity.this, response.body().getResponseMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ModifyBean> call, Throwable throwable) {

                    }
                });

            }
        });
        dlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDialog.dismiss();
            }
        });
        detailDialog.setContentView(contentView);
        detailDialog.show();
        android.view.WindowManager.LayoutParams p = detailDialog.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = (int) (ScreenSizeUtil.getScreenWidth(this) * 0.7); //宽度设置为屏幕的
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        detailDialog.getWindow().setAttributes(p); //设置生效
    }

    private void initBookList() {
        for (int i = 0; i < booklist.size(); i++) {
            final BSBookListBean.ResponseDataBean booksBean = booklist.get(i);
            View child = View.inflate(PersonDetailActivity.this, R.layout.item_discovery_book, null);
            ImageView image = (ImageView) child.findViewById(R.id.discovery_book_image);
            TextView name = (TextView) child.findViewById(R.id.discovery_book_name);
            String coverpath = booksBean.getBookInfo().getCoverPath();
            Glide.with(getContext()).load(ReaderRetroift.IMAGE_URL + coverpath).into(image);
            name.setText(booksBean.getBookInfo().getName());

            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(30, 20, 0, 20);
            param.gravity = Gravity.TOP;
            child.setLayoutParams(param);
            final int finalI = i;
            // item设置点击事件
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (booksBean.getBookInfo().getStatus() == 0) {
                        Toast.makeText(PersonDetailActivity.this, "该书已下架", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(PersonDetailActivity.this, BookDetailActivity.class);
                        intent.putExtra("bookid", booksBean.getBookId());
                        startActivity(intent);
                    }
                }
            });
            llContainer.addView(child);
        }
        llbookshelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ta的书架
                Intent intent = new Intent(PersonDetailActivity.this, personBookListActivity.class);
                intent.putExtra("detailBean", detailBean);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}
