package com.hxjf.dubei.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.BookFriendBean;
import com.hxjf.dubei.network.ImLoginHelper;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class MoreFriendAdapter extends BaseAdapter {
    List<BookFriendBean.ResponseDataBean.ContentBean> mContentBeans;
    Context mContext;

    public MoreFriendAdapter(List<BookFriendBean.ResponseDataBean.ContentBean> mContentBeans, Context mContext) {
        this.mContentBeans = mContentBeans;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mContentBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mContentBeans.get(position);
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
            convertView = View.inflate(mContext, R.layout.item_zone_friend, null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.friend_pratroit);
            holder.name = (TextView) convertView.findViewById(R.id.friend_name);
            holder.bookcount = (TextView) convertView.findViewById(R.id.friend_book_count);
            holder.distance = (TextView) convertView.findViewById(R.id.friend_distance);
            holder.tag1 = (TextView) convertView.findViewById(R.id.friend_tag1);
            holder.tag2 = (TextView) convertView.findViewById(R.id.friend_tag2);
            holder.tag3 = (TextView) convertView.findViewById(R.id.friend_tag3);
            holder.btn = (TextView) convertView.findViewById(R.id.friend_btn_message);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final BookFriendBean.ResponseDataBean.ContentBean contentBean = mContentBeans.get(position);
        ArrayList<TextView> tvList = new ArrayList<>();
        tvList.add(holder.tag1);
        tvList.add(holder.tag2);
        tvList.add(holder.tag3);
        Glide.with(mContext).load(NewDiscoveryRetrofit.IMAGE_URL + contentBean.getHeadPhoto()).into(holder.ivImage);
        holder.name.setText(contentBean.getNickName());
        holder.bookcount.setText(contentBean.getBookCount() + "本藏书");
        String tag = contentBean.getTag();
        if (tag != null && !"".equals(tag)) {
            String[] split = tag.split("、");

            for (int j = 0; j < split.length; j++) {
                tvList.get(j).setVisibility(View.VISIBLE);
                tvList.get(j).setText(split[j]);
            }
        }
        holder.distance.setText(contentBean.getCityDistrict());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //私信
                YWIMKit imKit = ImLoginHelper.getInstance().getIMKit();
                Intent intent = imKit.getChattingActivityIntent(contentBean.getUserId(), ImLoginHelper.getInstance().getAPP_KEY());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder{
        public ImageView ivImage;
        public TextView name;
        public TextView bookcount;
        public TextView distance;
        public TextView tag1;
        public TextView tag2 ;
        public TextView tag3;
        public TextView btn;
    }
}
