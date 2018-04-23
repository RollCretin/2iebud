package com.hxjf.dubei.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.PKListBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.DensityUtil;
import com.hxjf.dubei.util.ScreenSizeUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chen_Zhang on 2017/6/6.
 */

public class pkListAdapter extends BaseAdapter {

    Context mContext;
    List<PKListBean.ResponseDataBean.ContentBean> mList;

    public pkListAdapter(Context context, List<PKListBean.ResponseDataBean.ContentBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_pk, null);
            holder = new ViewHolder();
            holder.ivBook = (ImageView) convertView.findViewById(R.id.item_iv_book);
            holder.tvState = (TextView) convertView.findViewById(R.id.item_tv_book_state);
            holder.tvVs = (TextView) convertView.findViewById(R.id.item_vs);
            holder.tvBookName = (TextView) convertView.findViewById(R.id.item_book_name);
            holder.tvCanChallenge = (TextView) convertView.findViewById(R.id.item_tv_can_challenge);
            holder.tvMatchTime = (TextView) convertView.findViewById(R.id.item_match_time);
            holder.tvMyName = (CircleImageView) convertView.findViewById(R.id.item_my_name);
            holder.llOthers = (LinearLayout) convertView.findViewById(R.id.item_ll_others);
            holder.tvOtherName = (TextView) convertView.findViewById(R.id.item_other_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PKListBean.ResponseDataBean.ContentBean pkInfoBean = mList.get(position);

        if (pkInfoBean != null && pkInfoBean.getBookInfo() != null) {
            Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + pkInfoBean.getBookInfo().getCoverPath()).into(holder.ivBook);
            //判断挑战状态
            if (pkInfoBean.getChallengeStatus() == 1){
                holder.tvState.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            }else if(pkInfoBean.getChallengeStatus() == 2){
                holder.tvState.setBackgroundColor(mContext.getResources().getColor(R.color.challeging));
            }else {
                holder.tvState.setBackgroundColor(mContext.getResources().getColor(R.color.challeged));
            }
            holder.tvState.setText(pkInfoBean.getChallengeStatusValue());
            holder.tvBookName.setText(pkInfoBean.getBookInfo().getName());
            String startTime = pkInfoBean.getStartTime();//开始时间
            StringBuilder sb = new StringBuilder();
            if (pkInfoBean.getChallengeStatus() != 1 ||  pkInfoBean.getJoinStatus() == 2) {
                holder.tvCanChallenge.setVisibility(View.GONE);
            }else{
                holder.tvCanChallenge.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < startTime.length(); i++) {
                char c = startTime.charAt(i);
                sb.append(c);
                if (i == 3 || i == 5) {
                    sb.append(".");
                }
            }
            startTime = sb.toString();
            String endTime = pkInfoBean.getEndTime();
            StringBuilder sbend = new StringBuilder();//结束时间
            for (int i = 0; i < endTime.length(); i++) {
                char c = endTime.charAt(i);
                sbend.append(c);
                if (i == 3 || i == 5) {
                    sbend.append(".");
                }
            }
            endTime = sbend.toString();
            holder.tvMatchTime.setText("挑战时间：" + startTime + "~" + endTime);
            holder.llOthers.removeAllViews();
            List<PKListBean.ResponseDataBean.ContentBean.ChallengeUserListBean> challengeUserList = pkInfoBean.getChallengeUserList();
            if (challengeUserList.size() != 0) {

                for (int i = 0; i < challengeUserList.size(); i++) {
                    PKListBean.ResponseDataBean.ContentBean.ChallengeUserListBean.ParticipantUserBean participantUser = challengeUserList.get(i).getParticipantUser();
                    if (i == 0) {
                        if (participantUser == null || participantUser.getNormalPath()==null) {
                            Glide.with(mContext).load(R.mipmap.user_default_portrait).into(holder.tvMyName);
                        } else {
                            Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + participantUser.getNormalPath()).into(holder.tvMyName);
                        }
                        holder.tvVs.setVisibility(View.GONE);
                    } else {
                        holder.tvVs.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView = new CircleImageView(mContext);
                        circleImageView.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 16), DensityUtil.dip2px(mContext, 16)));
                        if (participantUser == null || participantUser.getNormalPath()==null) {
                            Glide.with(mContext).load(R.mipmap.user_default_portrait).into(circleImageView);
                        } else {
                            Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + participantUser.getNormalPath()).into(circleImageView);
                        }
                        holder.llOthers.addView(circleImageView);
                    }
                }
                TextView tvother = new TextView(mContext);
                tvother.setTextSize(10);
                tvother.setPadding(10,0,0,0);
                tvother.setTextColor(mContext.getResources().getColor(R.color.gray));
                if (pkInfoBean.getTotal() > 6) {
                    tvother.setText("等" + (pkInfoBean.getTotal() - 1) + "人");
                }else {
                    tvother.setText("");
                }
                holder.llOthers.addView(tvother);
            }

        }
        return convertView;

    }

    static class ViewHolder {
        public ImageView ivBook;
        public TextView tvState;
        public TextView tvBookName;
        public TextView tvCanChallenge;
        public TextView tvMatchTime;
        public CircleImageView tvMyName;
        public LinearLayout llOthers;
        public TextView tvOtherName;
        public TextView tvVs;
    }
}
