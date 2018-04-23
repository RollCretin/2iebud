package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.MyChallengeBean;
import com.hxjf.dubei.network.ReaderRetroift;
import com.hxjf.dubei.util.DensityUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chen_Zhang on 2017/8/8.
 */

public class MyChallengeListAdapter extends BaseAdapter {
    Context mContext;
    List<MyChallengeBean.ResponseDataBean.ContentBean> mList;
    private MyChallengeBean.ResponseDataBean.ContentBean contentBean;

    public MyChallengeListAdapter(Context mContext, List<MyChallengeBean.ResponseDataBean.ContentBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_my_challenge, null);
            holder.ivBook = (ImageView) convertView.findViewById(R.id.item_iv_book);
            holder.tvState = (TextView) convertView.findViewById(R.id.item_tv_book_state);
            holder.tvBookName = (TextView) convertView.findViewById(R.id.item_book_name);
            holder.ivsuccess = (ImageView) convertView.findViewById(R.id.item_book_success);
            holder.ivfailed = (ImageView) convertView.findViewById(R.id.item_book_failed);
            holder.tvMatchTime = (TextView) convertView.findViewById(R.id.item_match_time);
            holder.tvMyName = (CircleImageView) convertView.findViewById(R.id.item_my_name);
            holder.llOthers = (LinearLayout) convertView.findViewById(R.id.item_ll_others);
            holder.tvOtherName = (TextView) convertView.findViewById(R.id.item_other_name);
            holder.tvVS = (TextView) convertView.findViewById(R.id.item_tv_vs);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        contentBean = mList.get(position);
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + contentBean.getBookInfo().getCoverPath()).into(holder.ivBook);
        //判断挑战状态
        if (contentBean.getChallengeStatus() == 1){
            holder.tvState.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        }else if(contentBean.getChallengeStatus() == 2){
            holder.tvState.setBackgroundColor(mContext.getResources().getColor(R.color.challeging));
        }else {
            holder.tvState.setBackgroundColor(mContext.getResources().getColor(R.color.challeged));
        }
        holder.tvState.setText(contentBean.getChallengeStatusValue());

        holder.tvBookName.setText(contentBean.getBookInfo().getName());
        String startTime = contentBean.getStartTime();//开始时间
        StringBuilder sb = new StringBuilder();
        if (contentBean.getChallengeUserList().size() != 0) {
            if (contentBean.getMyChallengeResult() == 4) {
                //挑战成功
                holder.ivsuccess.setVisibility(View.VISIBLE);
                holder.ivfailed.setVisibility(View.GONE);
            } else if (contentBean.getMyChallengeResult() == 2) {
                //挑战失败
                holder.ivsuccess.setVisibility(View.GONE);
                holder.ivfailed.setVisibility(View.VISIBLE);
            }else{
                holder.ivsuccess.setVisibility(View.GONE);
                holder.ivfailed.setVisibility(View.GONE);
            }

        }
        if (contentBean.getChallengeUserList().size() == 1) {
            holder.tvVS.setVisibility(View.GONE);
        }else{
            holder.tvVS.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < startTime.length(); i++) {
            char c = startTime.charAt(i);
            sb.append(c);
            if (i == 3 || i == 5) {
                sb.append(".");
            }
        }
        startTime = sb.toString();
        String endTime = contentBean.getEndTime();
        StringBuilder sbend = new StringBuilder();//结束时间
        for (int i = 0; i < endTime.length(); i++) {
            char c = endTime.charAt(i);
            sbend.append(c);
            if (i == 3 || i == 5) {
                sbend.append(".");
            }
        }
        endTime = sbend.toString();
        holder.llOthers.removeAllViews();
        holder.tvMatchTime.setText("参赛时间：" + startTime + "~" + endTime);
        List<MyChallengeBean.ResponseDataBean.ContentBean.ChallengeUserListBean> challengeUserList = contentBean.getChallengeUserList();
        if (challengeUserList.size() != 0) {
            if (contentBean.getTotal() > 6) {
                holder.tvOtherName.setText("等" + (contentBean.getTotal() - 1) + "人");
            }
            for (int i = 0; i < challengeUserList.size(); i++) {
                String normalPath = challengeUserList.get(i).getParticipantUser().getNormalPath();
                if (i == 0) {
                    Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + normalPath).into(holder.tvMyName);
                } else {
                    CircleImageView circleImageView = new CircleImageView(mContext);
                    circleImageView.setLayoutParams(new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 16), DensityUtil.dip2px(mContext, 16)));
                    Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + normalPath).into(circleImageView);
                    holder.llOthers.addView(circleImageView);
                }
            }
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView ivBook;
        public TextView tvState;
        public TextView tvBookName;
        public ImageView ivsuccess;
        public ImageView ivfailed;
        public TextView tvMatchTime;
        public CircleImageView tvMyName;
        public LinearLayout llOthers;
        public TextView tvOtherName;
        public TextView tvVS;
    }
}
