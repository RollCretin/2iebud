package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.ChallengeJoinUserListBean;
import com.hxjf.dubei.bean.ModifyBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chen_Zhang on 2017/8/4.
 */

public class ChallengeUserListAdapter extends BaseAdapter {
    Context mContext;
    List<ChallengeJoinUserListBean.ResponseDataBean.ContentBean> mList;

    public ChallengeUserListAdapter(Context mContext, List<ChallengeJoinUserListBean.ResponseDataBean.ContentBean> mList) {
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_match_people,null);
            holder.ivPortrait = (ImageView) convertView.findViewById(R.id.item_match_portrait);
            holder.tvOriginator = (TextView) convertView.findViewById(R.id.item_match_originator);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_match_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.item_match_read_time);
            holder.cbLike = (CheckBox) convertView.findViewById(R.id.item_match_like);
            holder.ivSuccess = (ImageView) convertView.findViewById(R.id.item_match_challege_success);
            holder.ivFailed = (ImageView) convertView.findViewById(R.id.item_match_challege_failed);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final ChallengeJoinUserListBean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        String normalPath = contentBean.getParticipantUser().getNormalPath();
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL+normalPath).into(holder.ivPortrait);//头像
        if(position != 0){
            holder.tvOriginator.setVisibility(View.GONE);//发起者
        }
        holder.tvName.setText(contentBean.getParticipantUser().getNickName());//昵称
        int readTime = contentBean.getReadTime();
        int readhour = readTime / 3600;
        int readminute = readTime / 60;
        if (readhour == 0) {
            holder.tvTime.setText(readminute + "分钟");
        } else {
            readminute = readminute - readhour * 60;
            holder.tvTime.setText(readhour + "小时" + readminute + "分钟"); //时间
        }

        if (contentBean.getResult() == 4){
            //挑战成功
            holder.ivSuccess.setVisibility(View.VISIBLE);
        }else if(contentBean.getResult() == 2){
            //挑战失败
            holder.ivFailed.setVisibility(View.INVISIBLE);
        }
        final int[] praisenum = {contentBean.getTotalPraise()};

        holder.cbLike.setText(praisenum[0] + "");
        holder.cbLike.setChecked(contentBean.isPraiseFlag());
        final ViewHolder finalHolder = holder;
        final ViewHolder finalHolder1 = holder;
        holder.cbLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//点赞
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //点赞
                    Call<ModifyBean> challengePraiseCall = ReaderRetroift.getInstance(mContext).getApi().challengePraiseCall(contentBean.getId());
                    challengePraiseCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                praisenum[0] += 1;
                                finalHolder.cbLike.setText(praisenum[0] + "");
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {

                        }
                    });
                } else {
                    //取消点赞
                    Call<ModifyBean> praiseCancelCall = ReaderRetroift.getInstance(mContext).getApi().challengePraiseCancelCall(contentBean.getId());
                    praiseCancelCall.enqueue(new Callback<ModifyBean>() {
                        @Override
                        public void onResponse(Call<ModifyBean> call, Response<ModifyBean> response) {
                            ModifyBean bean = response.body();
                            if (bean != null) {
                                praisenum[0] -= 1;
                                finalHolder1.cbLike.setText(praisenum[0] + "");
                            }
                        }

                        @Override
                        public void onFailure(Call<ModifyBean> call, Throwable t) {

                        }
                    });
                }
            }
        });


        return convertView;
    }

    public class  ViewHolder{
        public ImageView ivPortrait;
        public TextView tvOriginator;
        public TextView tvName;
        public TextView tvTime;
        public CheckBox cbLike;
        public ImageView ivSuccess;
        public ImageView ivFailed;

    }
}
