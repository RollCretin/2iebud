package com.hxjf.dubei.ui.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.ApplyBean;
import com.hxjf.dubei.bean.BalanceBean;
import com.hxjf.dubei.bean.BannerBean;
import com.hxjf.dubei.bean.ChangDuParamBean;
import com.hxjf.dubei.bean.ChangduInfoBean;
import com.hxjf.dubei.bean.DiscoveryBean;
import com.hxjf.dubei.bean.HotBookBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.network.EnhancedCall;
import com.hxjf.dubei.network.EnhancedCallback;
import com.hxjf.dubei.network.PayResult;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.BookAnalysisActivity;
import com.hxjf.dubei.ui.activity.BookAnalysisDetailActivity;
import com.hxjf.dubei.ui.activity.BookDetailActivity;
import com.hxjf.dubei.ui.activity.BookListActivity;
import com.hxjf.dubei.ui.activity.BookListDetailActivity;
import com.hxjf.dubei.ui.activity.ChallegeBookActivity;
import com.hxjf.dubei.ui.activity.ChangduActivity;
import com.hxjf.dubei.ui.activity.HotBookActivity;
import com.hxjf.dubei.ui.activity.NoteDetailActivity;
import com.hxjf.dubei.ui.activity.NoteFactoryActivity;
import com.hxjf.dubei.ui.activity.SearchActivity;
import com.hxjf.dubei.ui.activity.agreementActivity;
import com.hxjf.dubei.util.GlideImageLoader;
import com.hxjf.dubei.util.SPUtils;
import com.hxjf.dubei.util.TimeUtil;
import com.hxjf.dubei.widget.MySwipeRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

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
 * Created by Chen_Zhang on 2017/5/19.
 */

public class DiscoveryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int SDK_PAY_FLAG = 1;
    @BindView(R.id.discovery_container_book_list)
    LinearLayout discoveryContainerBookList;
    @BindView(R.id.discovery_sv_book_list)
    HorizontalScrollView discoverySvBookList;
    @BindView(R.id.discovery_banner)
    Banner discoveryBanner;
    @BindView(R.id.discovery_container_book_analysis)
    LinearLayout discoveryContainerBookAnalysis;
    @BindView(R.id.discovery_iv_search)
    ImageView discoveryIvSearch;
    @BindView(R.id.discovery_boook_list_arrow)
    LinearLayout discoveryBoookListArrow;
    @BindView(R.id.discovery_analysis_arrow)
    LinearLayout discoveryAnalysisArrow;
    @BindView(R.id.discovery_note_arrow)
    LinearLayout discoveryNoteArrow;
    @BindView(R.id.discovery_sv_book_analysis)
    HorizontalScrollView discoverySvBookAnalysis;
    @BindView(R.id.discovery_container_note_factory)
    LinearLayout discoveryContainerNoteFactory;
    @BindView(R.id.discovery_sv_note_factory)
    HorizontalScrollView discoverySvNoteFactory;
    @BindView(R.id.discovery_hot_book_arrow)
    LinearLayout discoveryHotBookArrow;
    @BindView(R.id.discovery_container_hot_book)
    LinearLayout discoveryContainerHotBook;
    @BindView(R.id.discovery_sv_hot_book)
    HorizontalScrollView discoverySvHotBook;
    @BindView(R.id.discovery_refresh)
    MySwipeRefreshLayout discoveryRefresh;
    @BindView(R.id.discovery_tv_changdu_detail)
    TextView discoveryTvChangduDetail;
    @BindView(R.id.discovery_tv_understand)
    TextView discoveryTvUnderstand;
    @BindView(R.id.discovery_btn_open)
    Button discoveryBtnOpen;
    @BindView(R.id.discovery_nonet)
    TextView discoveryNonet;
    @BindView(R.id.discovery_scrollview)
    ScrollView discoveryScrollview;

    /*banner图片的集合*/
    private ArrayList<String> images;
    private ArrayList<String> notelist;
    private List<DiscoveryBean.ResponseDataBean.ChaidusBean> chaidus; //拆读
    private List<DiscoveryBean.ResponseDataBean.NoteFactoriesBean> noteFactories; //笔记工厂
    private List<DiscoveryBean.ResponseDataBean.BookListsBean> bookLists; //书单
    private List<HotBookBean.ResponseDataBean.ListBean> hotbooks; //热门书籍
    private ChangduInfoBean.ResponseDataBean changduInfo;
    private List<ChangDuParamBean.ResponseDataBean> changduparamList;
    private ChangDuParamBean.ResponseDataBean changduparamDataBean;
    private double accountMoney;
    private ApplyBean applyBean;
    private boolean searchSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_discovery_content, null);
        ButterKnife.bind(this, view);
        searchSwitch = SPUtils.getBoolean(getActivity(), "searchSwitch", false);
        if (searchSwitch){
            discoveryIvSearch.setVisibility(View.VISIBLE);
        }
        init();
        discoveryRefresh.setOnRefreshListener(this);
        discoveryRefresh.setColorSchemeColors(getResources().getColor(R.color.blue));
        return view;
    }

    private void init() {
        images = new ArrayList<>();
        hotbooks = new ArrayList<>();
        bookLists = new ArrayList<>();
        chaidus = new ArrayList<>();
        noteFactories = new ArrayList<>();
        discoveryRefresh.setRefreshing(true);
        Call<BannerBean> bannerCall = ReaderRetroift.getInstance(getActivity()).getApi().bannerCall();
        EnhancedCall<BannerBean> bannerBeanEnhancedCall = new EnhancedCall<>(bannerCall);
        bannerBeanEnhancedCall.dataClassName(BannerBean.class).enqueue(new EnhancedCallback<BannerBean>() {
            @Override
            public void onResponse(Call<BannerBean> call, Response<BannerBean> response) {
                BannerBean bean = response.body();
                if (bean != null) {
                    final List<BannerBean.ResponseDataBean> responseData = bean.getResponseData();
                    for (int i = 0; i < responseData.size(); i++) {
                        images.add(ReaderRetroift.IMAGE_URL + responseData.get(i).getNormalPath());
                    }
                    discoveryBanner.setImageLoader(new GlideImageLoader());//设置Bannner的图片加载器
                    discoveryBanner.setImages(images).setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER).setDelayTime(3000).setOnBannerClickListener(new OnBannerClickListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            if ("5ff8322ee43646929402a7bd7fbb1a3f".equals(responseData.get(position - 1).getId())) {
                                Intent challengeIntent = new Intent(getActivity(), ChallegeBookActivity.class);
                                startActivity(challengeIntent);
                            } else if ("adf0a05681c84271837f1655e52eb5e4".equals(responseData.get(position - 1).getId())) {
                                Intent chaiduIntent = new Intent(getActivity(), BookAnalysisActivity.class);
                                startActivity(chaiduIntent);

                            } else if (responseData.get(position - 1).getUrl() != null) {
                                //跳转到WebView上
                                Intent intent = new Intent(getActivity(), agreementActivity.class);
                                intent.putExtra("url", "http://test1.17dubei.com/getChangCard.html");
                                startActivity(intent);

                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<BannerBean> call, Throwable t) {
            }

            @Override
            public void onGetCache(BannerBean bean) {
                if (bean != null) {
                    final List<BannerBean.ResponseDataBean> responseData = bean.getResponseData();
                    for (int i = 0; i < responseData.size(); i++) {
                        images.add(ReaderRetroift.IMAGE_URL + responseData.get(i).getNormalPath());
                    }
                    discoveryBanner.setImageLoader(new GlideImageLoader());//设置Bannner的图片加载器
                    discoveryBanner.setImages(images).setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER).setDelayTime(3000).setOnBannerClickListener(new OnBannerClickListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            if ("5ff8322ee43646929402a7bd7fbb1a3f".equals(responseData.get(position - 1).getId())) {
                                Intent challengeIntent = new Intent(getActivity(), ChallegeBookActivity.class);
                                startActivity(challengeIntent);
                            } else if ("adf0a05681c84271837f1655e52eb5e4".equals(responseData.get(position - 1).getId())) {
                                Intent chaiduIntent = new Intent(getActivity(), BookAnalysisActivity.class);
                                startActivity(chaiduIntent);

                            } else if (responseData.get(position - 1).getUrl() != null) {
                                //跳转到WebView上
                                Intent intent = new Intent(getActivity(), agreementActivity.class);
                                intent.putExtra("url", "http://test1.17dubei.com/getChangCard.html");
                                startActivity(intent);

                            }
                        }
                    }).start();
                }
            }
        });


        //热门书籍
        Call<HotBookBean> hotBookCall = ReaderRetroift.getInstance(getActivity()).getApi().discoveryHotBookCall();
        EnhancedCall<HotBookBean> hotBookBeanEnhancedCall = new EnhancedCall<>(hotBookCall);
        hotBookBeanEnhancedCall.dataClassName(HotBookBean.class).enqueue(new EnhancedCallback<HotBookBean>() {
            @Override
            public void onResponse(Call<HotBookBean> call, Response<HotBookBean> response) {
                HotBookBean bean = response.body();
                if (bean != null) {
                    hotbooks = bean.getResponseData().getList();
                    initBooks();
                }
            }

            @Override
            public void onFailure(Call<HotBookBean> call, Throwable t) {
            }

            @Override
            public void onGetCache(HotBookBean hotBookBean) {
                if (hotBookBean != null) {
                    hotbooks = hotBookBean.getResponseData().getList();
                    initBooks();
                }
            }
        });

        //书单、拆读、笔记工厂
        Call<DiscoveryBean> discoveryCall = ReaderRetroift.getInstance(getActivity()).getApi().discoveryCall();
        EnhancedCall<DiscoveryBean> discoveryBeanEnhancedCall = new EnhancedCall<>(discoveryCall);
        discoveryBeanEnhancedCall.dataClassName(DiscoveryBean.class).enqueue(new EnhancedCallback<DiscoveryBean>() {
            @Override
            public void onResponse(Call<DiscoveryBean> call, Response<DiscoveryBean> response) {
                DiscoveryBean bean = response.body();
                if (bean != null && bean.getResponseCode() == 1) {
                    discoveryRefresh.setRefreshing(false);
                    bookLists = bean.getResponseData().getBookLists();
                    chaidus = bean.getResponseData().getChaidus();
                    noteFactories = bean.getResponseData().getNoteFactories();
                    initBookList();
                    initChaiDu();
                    initNoteFactory();
                }
            }

            @Override
            public void onFailure(Call<DiscoveryBean> call, Throwable t) {
                Toast.makeText(getActivity(), "请检查网络..", Toast.LENGTH_LONG).show();
                discoveryRefresh.setRefreshing(false);
                discoveryScrollview.setVisibility(View.GONE);
                discoveryNonet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetCache(DiscoveryBean bean) {
                Toast.makeText(getActivity(), "请检查网络..", Toast.LENGTH_LONG).show();
                if (bean != null && bean.getResponseCode() == 1) {
                    discoveryRefresh.setRefreshing(false);
                    bookLists = bean.getResponseData().getBookLists();
                    chaidus = bean.getResponseData().getChaidus();
                    noteFactories = bean.getResponseData().getNoteFactories();
                    initBookList();
                    initChaiDu();
                    initNoteFactory();
                }
            }
        });
        //畅读卡状态
        Call<ChangduInfoBean> changduInfoCall = ReaderRetroift.getInstance(getActivity()).getApi().ChangDuInfoCall();
        EnhancedCall<ChangduInfoBean> changduInfoBeanEnhancedCall = new EnhancedCall<>(changduInfoCall);
        changduInfoBeanEnhancedCall.dataClassName(ChangduInfoBean.class).enqueue(new EnhancedCallback<ChangduInfoBean>() {
            @Override
            public void onResponse(Call<ChangduInfoBean> call, Response<ChangduInfoBean> response) {
                ChangduInfoBean changduInfoBean = response.body();
                if (changduInfoBean != null && changduInfoBean.getResponseData() != null) {
                    changduInfo = changduInfoBean.getResponseData();
                    initChangdu();
                }
            }

            @Override
            public void onFailure(Call<ChangduInfoBean> call, Throwable t) {
            }

            @Override
            public void onGetCache(ChangduInfoBean changduInfoBean) {
                if (changduInfoBean != null && changduInfoBean.getResponseData() != null) {
                    changduInfo = changduInfoBean.getResponseData();
                    initChangdu();
                }
            }
        });

        discoveryBtnOpen.setClickable(false);
        //获取畅读卡类型以及单价
        Call<ChangDuParamBean> changDuParamCallCall = ReaderRetroift.getInstance(getActivity()).getApi().changDuParamCall("vipType");
        EnhancedCall<ChangDuParamBean> changDuParamBeanEnhancedCall = new EnhancedCall<>(changDuParamCallCall);
        changDuParamBeanEnhancedCall.dataClassName(ChangDuParamBean.class).enqueue(new EnhancedCallback<ChangDuParamBean>() {
            @Override
            public void onResponse(Call<ChangDuParamBean> call, Response<ChangDuParamBean> response) {
                ChangDuParamBean changDuParamBean = response.body();
                if (changDuParamBean != null && changDuParamBean.getResponseData() != null) {
                    changduparamList = changDuParamBean.getResponseData();
                    discoveryBtnOpen.setClickable(true);
                    changduparamDataBean = changduparamList.get(0);
                }
            }

            @Override
            public void onFailure(Call<ChangDuParamBean> call, Throwable t) {
            }

            @Override
            public void onGetCache(ChangDuParamBean changDuParamBean) {
                if (changDuParamBean != null && changDuParamBean.getResponseData() != null) {
                    changduparamList = changDuParamBean.getResponseData();
                    discoveryBtnOpen.setClickable(true);
                    changduparamDataBean = changduparamList.get(0);
                }
            }
        });

    }

    private void initChangdu() {
        //初始化畅读卡
        if (changduInfo.getVipStatus() == 1 || changduInfo.getVipStatus() == 3) {
            //未开通or已过期
            discoveryTvChangduDetail.setText("未开通畅读卡");
            discoveryBtnOpen.setText("开通");
            discoveryBtnOpen.setVisibility(View.VISIBLE);
        } else {
            //已开通
            String vipOverTime = changduInfo.getVipOverTime();
            String endtime = vipOverTime.replace("-", "");//yyyymmdd
            String[] difference2 = TimeUtil.getDifference2(endtime);
            discoveryTvChangduDetail.setText("剩余 " + difference2[0] + " 天到期");
            discoveryBtnOpen.setText("续费");
            if (Integer.valueOf(difference2[0]) > 10) {
                discoveryBtnOpen.setVisibility(View.GONE);
            } else {
                discoveryBtnOpen.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initBooks() {
        //加载热门书籍数据
        //清空所有子view
        discoveryContainerHotBook.removeAllViews();
        if (hotbooks.size() == 0 || searchSwitch == false){
            discoveryHotBookArrow.setVisibility(View.GONE);
            discoverySvHotBook.setVisibility(View.GONE);
        }else {
            discoveryHotBookArrow.setVisibility(View.VISIBLE);
            discoverySvHotBook.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < hotbooks.size(); i++) {
            final HotBookBean.ResponseDataBean.ListBean booksBean = hotbooks.get(i);


            View child = View.inflate(getActivity(), R.layout.item_discovery_book, null);
            ImageView image = (ImageView) child.findViewById(R.id.discovery_book_image);
            TextView name = (TextView) child.findViewById(R.id.discovery_book_name);
            String coverpath = booksBean.getCoverPath();

            Glide.with(getContext()).load(ReaderRetroift.IMAGE_URL + coverpath).into(image);
            name.setText(booksBean.getName());

            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(30, 50, 0, 50);
            param.gravity = Gravity.TOP;
            child.setLayoutParams(param);
            final int finalI = i;
            // item设置点击事件
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                    intent.putExtra("bookid", booksBean.getId());
                    startActivity(intent);
                }
            });
            discoveryContainerHotBook.addView(child);
        }
    }

    private void initBookList() {
        //加载优选书单数据
        discoveryContainerBookList.removeAllViews();
        if (bookLists.size() == 0){
            discoveryBoookListArrow.setVisibility(View.GONE);
            discoverySvBookList.setVisibility(View.GONE);
        }else{
            discoveryBoookListArrow.setVisibility(View.VISIBLE);
            discoverySvBookList.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < bookLists.size(); i++) {

            final DiscoveryBean.ResponseDataBean.BookListsBean bookListsBean = bookLists.get(i);
            View child = View.inflate(getActivity(), R.layout.item_discovery_booklist, null);
            ImageView image = (ImageView) child.findViewById(R.id.discovery_booklist_image);
            TextView name = (TextView) child.findViewById(R.id.discovery_booklist_name);
            String coverpath = bookListsBean.getCover();
            String title = bookListsBean.getTitle();
            name.setText(title);
            Glide.with(getContext()).load(ReaderRetroift.IMAGE_URL + coverpath).into(image);

            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(30, 50, 0, 50);

            child.setLayoutParams(param);
            final int finalI = i;
            // item设置点击事件
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BookListDetailActivity.class);
                    intent.putExtra("booklistid", bookListsBean.getId());
                    startActivity(intent);
                }
            });
            discoveryContainerBookList.addView(child);
        }
    }

    private void initChaiDu() {
        //加载好书拆读数据
        discoveryContainerBookAnalysis.removeAllViews();
        if (chaidus.size() == 0){
            discoveryAnalysisArrow.setVisibility(View.GONE);
            discoverySvBookAnalysis.setVisibility(View.GONE);
        }else{
            discoveryAnalysisArrow.setVisibility(View.VISIBLE);
            discoverySvBookAnalysis.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < chaidus.size(); i++) {
            final DiscoveryBean.ResponseDataBean.ChaidusBean chaidusBean = chaidus.get(i);
            View child = View.inflate(getActivity(), R.layout.item_discovery_book, null);
            ImageView ivimage = (ImageView) child.findViewById(R.id.discovery_book_image);
            TextView tvname = (TextView) child.findViewById(R.id.discovery_book_name);
            String cover = chaidusBean.getCover();
            String name = chaidusBean.getName();
            tvname.setText(name);
            Glide.with(getContext()).load(ReaderRetroift.IMAGE_URL + cover).into(ivimage);
            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(30, 50, 0, 50);
            param.gravity = Gravity.TOP;
            child.setLayoutParams(param);
            final int finalI = i;
            // item设置点击事件
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BookAnalysisDetailActivity.class);
                    intent.putExtra("ChaiduId", chaidusBean.getId());
                    startActivity(intent);
                }
            });
            discoveryContainerBookAnalysis.addView(child);
        }
    }

    //笔记工厂
    private void initNoteFactory() {
        discoveryContainerNoteFactory.removeAllViews();
        if (noteFactories.size() == 0 ){
            discoveryNoteArrow.setVisibility(View.GONE);
            discoverySvNoteFactory.setVisibility(View.GONE);
        }else{
            discoveryNoteArrow.setVisibility(View.VISIBLE);
            discoverySvNoteFactory.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < noteFactories.size(); i++) {
            final DiscoveryBean.ResponseDataBean.NoteFactoriesBean noteFactoriesBean = noteFactories.get(i);
            View child = View.inflate(getActivity(), R.layout.item_discovery_note_factory, null);
            ImageView ivImage = (ImageView) child.findViewById(R.id.discovery_note_factory_image);
            TextView tvTitle = (TextView) child.findViewById(R.id.discovery_note_factory_title);
            TextView tvAuthor = (TextView) child.findViewById(R.id.discovery_note_factory_author);
            String coverpath = noteFactoriesBean.getCover();
            String lecturer = noteFactoriesBean.getLecturer();
            String title = noteFactoriesBean.getTitle();
            String tagValue = noteFactoriesBean.getTagValue();
            tvTitle.setText("【" + tagValue + "】" + title);
            tvAuthor.setText(lecturer);
            Glide.with(getContext()).load(ReaderRetroift.IMAGE_URL + coverpath).into(ivImage);

            //item设置外边距
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(30, 50, 0, 50);

            child.setLayoutParams(param);
            final int finalI = i;
            // item设置点击事件
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noteFactoryId = noteFactoriesBean.getId();
                    Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                    intent.putExtra("noteFactoryId", noteFactoryId);
                    startActivity(intent);
                }
            });
            discoveryContainerNoteFactory.addView(child);
            discoveryContainerNoteFactory.setGravity(Gravity.TOP);
        }
    }


    @OnClick({R.id.discovery_iv_search, R.id.discovery_boook_list_arrow, R.id.discovery_analysis_arrow, R.id.discovery_note_arrow, R.id.discovery_hot_book_arrow, R.id.discovery_tv_understand, R.id.discovery_btn_open, R.id.discovery_nonet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.discovery_iv_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.discovery_boook_list_arrow:
                //优选书单
                Intent booklistIntent = new Intent(getActivity(), BookListActivity.class);
                startActivity(booklistIntent);
                break;
            case R.id.discovery_analysis_arrow:
                //好书拆读
                Intent analysisIntent = new Intent(getActivity(), BookAnalysisActivity.class);
                startActivity(analysisIntent);
                break;
            case R.id.discovery_note_arrow:
                //笔记工厂
                Intent noteIntent = new Intent(getActivity(), NoteFactoryActivity.class);
                startActivity(noteIntent);
                break;
            case R.id.discovery_hot_book_arrow:
                //热门书籍
                Intent bookIntent = new Intent(getActivity(), HotBookActivity.class);
                startActivity(bookIntent);
                break;
            case R.id.discovery_tv_understand:
                //畅读卡详情页面
                Intent changduIntent = new Intent(getActivity(), ChangduActivity.class);
                startActivity(changduIntent);
                break;
            case R.id.discovery_btn_open:
                //开通或者续费
                openOrRenew();
                break;
            case R.id.discovery_nonet:
                discoveryScrollview.setVisibility(View.VISIBLE);
                discoveryNonet.setVisibility(View.GONE);
                init();
                break;
        }
    }

    private void openOrRenew() {

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
        final Dialog openChangduDialog = new Dialog(getActivity(), R.style.myDialogTheme);
        final View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_open_changdu, null);
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

    @Override
    public void onRefresh() {
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        //畅读卡状态
        Call<ChangduInfoBean> changduInfoCall = ReaderRetroift.getInstance(getActivity()).getApi().ChangDuInfoCall();
        changduInfoCall.enqueue(new Callback<ChangduInfoBean>() {
            @Override
            public void onResponse(Call<ChangduInfoBean> call, Response<ChangduInfoBean> response) {
                ChangduInfoBean changduInfoBean = response.body();
                if (changduInfoBean != null && changduInfoBean.getResponseData() != null) {
                    changduInfo = changduInfoBean.getResponseData();
                    initChangdu();
                }
            }

            @Override
            public void onFailure(Call<ChangduInfoBean> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
