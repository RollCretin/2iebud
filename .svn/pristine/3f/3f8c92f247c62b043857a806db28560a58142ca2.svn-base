package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.ActivityListBean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/9/20.
 */

public class MoreWonderfuladapter extends BaseAdapter {
    Context mContext;
    List<ActivityListBean.ResponseDataBean.ContentBean> contentList;

    public MoreWonderfuladapter(Context mContext, List<ActivityListBean.ResponseDataBean.ContentBean> mList) {
        this.mContext = mContext;
        this.contentList = mList;
    }


    @Override
    public int getCount() {
        return contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return contentList.get(position);
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
            convertView = View.inflate(mContext, R.layout.item_wonderful_activity,null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.item_wonderful_activity_image);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_wonderful_activity_name);
            holder.tvForm = (TextView) convertView.findViewById(R.id.item_wonderful_activity_form);
            holder.tvTime = (TextView) convertView.findViewById(R.id.item_wonderful_activity_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ActivityListBean.ResponseDataBean.ContentBean contentBean = contentList.get(position);
        Glide.with(mContext).load(NewDiscoveryRetrofit.IMAGE_URL+contentBean.getBanner()).into(holder.ivImage);
        holder.tvName.setText(contentBean.getTitle());
        holder.tvForm.setText(contentBean.getOwnerName());
        holder.tvTime.setText(contentBean.getTime());
        return convertView;
    }

    public class ViewHolder{
        public ImageView ivImage;
        public TextView tvName;
        public TextView tvForm;
        public TextView tvTime;
    }

}
