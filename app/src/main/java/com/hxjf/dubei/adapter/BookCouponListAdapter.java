package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/6/23.
 */

public class BookCouponListAdapter extends BaseAdapter {
    Context mContext;
    List mList;
    public BookCouponListAdapter(Context context, List list) {
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
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.item_book_coupon, null);
            holder = new ViewHolder();
            holder.ivCouponImage = (ImageView) convertView.findViewById(R.id.item_bookcoupon_image);
            holder.tvCouponTime = (TextView) convertView.findViewById(R.id.item_bookcoupon_recevice_time);
            holder.tvCouponUse = (TextView) convertView.findViewById(R.id.item_bookcoupon_use);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    public class ViewHolder{
        public ImageView ivCouponImage;
        public TextView tvCouponTime;
        public TextView tvCouponUse;
    }
}
