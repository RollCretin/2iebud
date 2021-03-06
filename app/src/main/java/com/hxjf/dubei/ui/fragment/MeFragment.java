package com.hxjf.dubei.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.UserDetailBean;
import com.hxjf.dubei.network.EnhancedCall;
import com.hxjf.dubei.network.EnhancedCallback;
import com.hxjf.dubei.network.ImLoginHelper;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.ui.activity.LoginActivity;
import com.hxjf.dubei.ui.activity.MyAccountActivity;
import com.hxjf.dubei.ui.activity.MyAttentionActivity;
import com.hxjf.dubei.ui.activity.MyBookListActivity;
import com.hxjf.dubei.ui.activity.MyBorrowDetailActivity;
import com.hxjf.dubei.ui.activity.MyInformationActivity;
import com.hxjf.dubei.ui.activity.MyMessageActivity;
import com.hxjf.dubei.ui.activity.NoticeActivity;
import com.hxjf.dubei.ui.activity.PersonAchievementActivity;
import com.hxjf.dubei.ui.activity.SettingActivity;
import com.hxjf.dubei.ui.activity.myCollectBookActivity;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.GlideLoadUtils;
import com.hxjf.dubei.util.SPUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/5/19.
 */

public class MeFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "Mefragment";
    @BindView(R.id.me_scrollView)
    PullToZoomScrollViewEx meScrollView;
    @BindView(R.id.me_nonet)
    TextView meNonet;
    private ImageView meHeadnotice;
    private CircleImageView meHeadProtrait;
    private TextView meHeadName;
    private TextView meHeadAutograph;
    private ImageView ivZoom;
    private TextView meMyBookList;
    private TextView meMyAttention;
    private TextView meAccountBalance;
    private LinearLayout meAccount;
    private ImageView meMessageArrow;
    private LinearLayout meMessage;
    private UserDetailBean bean;
    private TextView meAchievement;
    private ImageView hasMessage;
    private LinearLayout setting;
    private ImageView meHeadNotice;
    private LinearLayout mecollectbook;
    private TextView meBorrowBook;
    private LinearLayout meCollect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_me_content, null);
        ButterKnife.bind(this, view);

        int[] screenWidthAndHeight = DensityUtil.getScreenWidthAndHeight(getContext());
        int mScreenWidth = screenWidthAndHeight[0];
        int mScreenHeight = screenWidthAndHeight[1];
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (8.0F * (mScreenHeight / 21.0F)));
        meScrollView.setHeaderLayoutParams(localObject);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        View headView = LayoutInflater.from(getContext()).inflate(R.layout.me_head_view, null, false);
        View zoomView = LayoutInflater.from(getContext()).inflate(R.layout.me_zoom_view, null, false);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.me_content_view, null, false);
        meHeadnotice = (ImageView) headView.findViewById(R.id.me_notice);
        meHeadProtrait = (CircleImageView) headView.findViewById(R.id.me_head_protrait);
        meHeadName = (TextView) headView.findViewById(R.id.me_head_name);
        meHeadAutograph = (TextView) headView.findViewById(R.id.me_head_autograph);
        meHeadNotice = (ImageView) headView.findViewById(R.id.me_head_has_notice);
        meHeadnotice.setOnClickListener(this);
        meHeadProtrait.setOnClickListener(this);
        meHeadName.setOnClickListener(this);
        meHeadAutograph.setOnClickListener(this);

        ivZoom = (ImageView) zoomView.findViewById(R.id.iv_zoom);

        meAchievement = (TextView) contentView.findViewById(R.id.me_my_achievement);
        meMyBookList = (TextView) contentView.findViewById(R.id.me_my_book_list);
        meMyAttention = (TextView) contentView.findViewById(R.id.me_my_attention);
        meAccountBalance = (TextView) contentView.findViewById(R.id.me_account_balance);
        meAccount = (LinearLayout) contentView.findViewById(R.id.me_account);
        meMessageArrow = (ImageView) contentView.findViewById(R.id.me_message_arrow);
        meMessage = (LinearLayout) contentView.findViewById(R.id.me_message);
        hasMessage = (ImageView) contentView.findViewById(R.id.me_has_message);
        setting = (LinearLayout) contentView.findViewById(R.id.me_setting);
        mecollectbook = (LinearLayout) contentView.findViewById(R.id.me_collect_book);
        meBorrowBook = (TextView) contentView.findViewById(R.id.me_my_book_borrow);
        meCollect = (LinearLayout) contentView.findViewById(R.id.me_collect);

        meMyBookList.setOnClickListener(this);
        meMyAttention.setOnClickListener(this);
        meAccount.setOnClickListener(this);
        meMessage.setOnClickListener(this);
        meAchievement.setOnClickListener(this);
        setting.setOnClickListener(this);
        mecollectbook.setOnClickListener(this);
        meBorrowBook.setOnClickListener(this);
        meCollect.setOnClickListener(this);


        meScrollView.setHeaderView(headView);
        meScrollView.setZoomView(zoomView);
        meScrollView.setScrollContentView(contentView);
        Call<UserDetailBean> myDetailCall = ReaderRetroift.getInstance(getContext()).getApi().myDetailCall();
        EnhancedCall<UserDetailBean> userDetailBeanEnhancedCall = new EnhancedCall<>(myDetailCall);
        userDetailBeanEnhancedCall.dataClassName(UserDetailBean.class).enqueue(new EnhancedCallback<UserDetailBean>() {
            @Override
            public void onResponse(Call<UserDetailBean> call, Response<UserDetailBean> response) {
                bean = response.body();
                if (bean != null && bean.getResponseCode() == 2) {
                    SharedPreferences cookies_prefs = getActivity().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
                    cookies_prefs.edit().clear().commit();
                    SPUtils.remove(getActivity(), "bindbean");
                    //将栈中所有activity清空，跳到login页面
                    Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (bean != null && bean.getResponseData() != null) {
                    String normalPath = bean.getResponseData().getNormalPath();
                    GlideLoadUtils.getInstance().glideLoad(getActivity(),ReaderRetroift.IMAGE_URL + normalPath,meHeadProtrait,0);
                    String nickName = bean.getResponseData().getNickName();
                    meHeadName.setText(nickName);
                    String intro = bean.getResponseData().getIntro();
                    if ("".equals(intro) || intro == null) {
                        meHeadAutograph.setText("这个人很懒，什么都没留下！");
                    } else {
                        meHeadAutograph.setText(intro);

                    }
                    DecimalFormat df = new DecimalFormat("######0.00");
                    meAccountBalance.setText("" + df.format(bean.getResponseData().getAccountMoney()));
                    if (bean.getResponseData().getMsgNum() != 0) {
                        meHeadNotice.setVisibility(View.VISIBLE);
                    }

                    YWIMKit imKit = ImLoginHelper.getInstance().getIMKit();
                    YWIMCore imCore = imKit.getIMCore();
                    IYWConversationService conversationService = imCore.getConversationService();
                    int totalUnreadCount = conversationService.getAllUnreadCount();
                    if (totalUnreadCount != 0) {
                        hasMessage.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailBean> call, Throwable t) {
                Toast.makeText(getActivity(), "请检查网络..", Toast.LENGTH_LONG).show();
                meScrollView.setVisibility(View.GONE);
                meNonet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGetCache(UserDetailBean bean) {
                Toast.makeText(getActivity(), "请检查网络..", Toast.LENGTH_LONG).show();
                if (bean != null && bean.getResponseCode() == 2) {
                    SharedPreferences cookies_prefs = getActivity().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE);
                    cookies_prefs.edit().clear().commit();
                    SPUtils.remove(getActivity(), "bindbean");
                    //将栈中所有activity清空，跳到login页面
                    Intent intent = new Intent(getActivity(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (bean != null && bean.getResponseData() != null) {
                    String normalPath = bean.getResponseData().getNormalPath();
                    GlideLoadUtils.getInstance().glideLoad(getActivity(),ReaderRetroift.IMAGE_URL + normalPath,meHeadProtrait,0);
                    String nickName = bean.getResponseData().getNickName();
                    meHeadName.setText(nickName);
                    String intro = bean.getResponseData().getIntro();
                    if ("".equals(intro) || intro == null) {
                        meHeadAutograph.setText("这个人很懒，什么都没留下！");
                    } else {
                        meHeadAutograph.setText(intro);

                    }
                    DecimalFormat df = new DecimalFormat("######0.00");
                    meAccountBalance.setText("" + df.format(bean.getResponseData().getAccountMoney()));
                    if (bean.getResponseData().getMsgNum() != 0) {
                        meHeadNotice.setVisibility(View.VISIBLE);
                    }

                    YWIMKit imKit = ImLoginHelper.getInstance().getIMKit();
                    YWIMCore imCore = imKit.getIMCore();
                    IYWConversationService conversationService = imCore.getConversationService();
                    int totalUnreadCount = conversationService.getAllUnreadCount();
                    if (totalUnreadCount != 0) {
                        hasMessage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        /*//获取bindbean
        String bindbeanStr = SPUtils.getString(getContext(), "bindbean", "");
        Gson gson = new Gson();
        UserBean bindBean = gson.fromJson(bindbeanStr, UserBean.class);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_head_protrait:
            case R.id.me_head_name:
            case R.id.me_head_autograph:
                //个人信息页
                Intent infoIntent = new Intent(getActivity(), MyInformationActivity.class);
                startActivity(infoIntent);
                break;
            case R.id.me_setting:
                //设置页
                Intent setIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(setIntent);
                break;
            case R.id.me_my_book_list:
                //我的书单
                Intent listIntent = new Intent(getActivity(), MyBookListActivity.class);
                startActivity(listIntent);
                break;
            case R.id.me_my_attention:
                //我的关注
                Intent attentionIntent = new Intent(getActivity(), MyAttentionActivity.class);
                startActivity(attentionIntent);
                break;
            case R.id.me_account:
//                我的账户页
                Intent accountIntent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(accountIntent);
                break;
            case R.id.me_message:
                //我的消息
                Intent messageIntent = new Intent(getActivity(), MyMessageActivity.class);
                startActivity(messageIntent);
                break;
            case R.id.me_my_achievement:
                //我的成就页
                Intent achievementIntent = new Intent(getActivity(), PersonAchievementActivity.class);
                startActivity(achievementIntent);
                break;
            case R.id.me_notice:
                //通知
                Intent noticeIntent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(noticeIntent);
                break;
            case R.id.me_collect_book:
                Intent collectBookIntent = new Intent(getActivity(), myCollectBookActivity.class);
                startActivity(collectBookIntent);
                break;
            case R.id.me_my_book_borrow:
                Toast.makeText(getActivity(), "我的借阅", Toast.LENGTH_SHORT).show();
                Intent myborrowIntent = new Intent(getActivity(),MyBorrowDetailActivity.class);
                startActivity(myborrowIntent);
                break;
            case R.id.me_collect:
                Toast.makeText(getActivity(), "我的收藏", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.me_nonet)
    public void onViewClicked() {
        meScrollView.setVisibility(View.VISIBLE);
        meNonet.setVisibility(View.GONE);
        init();
    }



}
