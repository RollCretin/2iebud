package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.PromoteDubiDean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/26.
 */

public class PromoteDubiAdapter extends BaseAdapter {

    Context mContext;
    List<PromoteDubiDean> mList;

    public PromoteDubiAdapter(Context mContext, List<PromoteDubiDean> mList) {
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
        ViewHolder holder ;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_promote_yueli,null);
            holder.ivLogo = (ImageView) convertView.findViewById(R.id.item_promote_yueli_logo);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_promote_yueli_title);
            holder.tvDes = (TextView) convertView.findViewById(R.id.item_promote_yueli_des);
            holder.tvNum = (TextView) convertView.findViewById(R.id.item_promote_yueli_num);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        PromoteDubiDean promoteDubiDean = mList.get(position);
        Glide.with(mContext).load(promoteDubiDean.getResId()).into(holder.ivLogo);
        holder.tvTitle.setText(promoteDubiDean.getTitle());
        holder.tvDes.setText(promoteDubiDean.getDes());
        holder.tvNum.setText(promoteDubiDean.getNuml());
        return convertView;
    }

    class ViewHolder{
        ImageView ivLogo;
        TextView tvTitle;
        TextView tvDes;
        TextView tvNum;
    }
}
