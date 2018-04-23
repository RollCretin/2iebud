package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.BookbarListBean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Chen_Zhang on 2017/9/19.
 */

public class MoreZoneListAdapter extends BaseAdapter {
    Context mContext;
    List<BookbarListBean.ResponseDataBean.ContentBean> mList;
    private double distanceKM = 0.0;

    public MoreZoneListAdapter(Context mContext, List<BookbarListBean.ResponseDataBean.ContentBean> mList) {
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
            convertView = View.inflate(mContext, R.layout.item_more_zone,null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.item_more_zone_image);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_more_zone_name);
            holder.tvTag = (TextView) convertView.findViewById(R.id.item_more_zone_tag);
            holder.tvLocation = (TextView) convertView.findViewById(R.id.item_more_zone_location);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        BookbarListBean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        Glide.with(mContext).load(NewDiscoveryRetrofit.IMAGE_URL+contentBean.getBanner()).into(holder.ivImage);
        holder.tvName.setText(contentBean.getName());
        holder.tvTag.setText(contentBean.getTag());
        Double distance = Double.valueOf(contentBean.getDistance());
        if (distance >= 1000){
            distanceKM = distance / 1000.0;
            DecimalFormat format = new DecimalFormat("######0.00");
            holder.tvLocation.setText(format.format(distanceKM) + " KM");
        }else {
            holder.tvLocation.setText((new Double(distance)).intValue() + " M");
        }
        return convertView;
    }
    public class ViewHolder{
        public ImageView ivImage;
        public TextView tvName;
        public TextView tvTag;
        public TextView tvLocation;
    }
}
