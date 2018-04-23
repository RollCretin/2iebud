package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hxjf.dubei.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/5 0005.
 */

public class BorrowInOutAdapter extends BaseAdapter {
    ArrayList<String> list;
    Context context;

    public BorrowInOutAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_adapter_borrowinout,null);
            holder.ivPratroit = (ImageView) convertView.findViewById(R.id.item_borrowinout_iv_pratroit);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_borrowinout_tv_name);
            holder.tvState = (TextView)convertView.findViewById(R.id.item_borrowinout_tv_state);
            holder.llCovers = (LinearLayout)convertView.findViewById(R.id.item_borrowinout_ll_images);
            holder.tvBookName = (TextView)convertView.findViewById(R.id.item_borrowinout_tv_bookname);
            holder.tvBookCount = (TextView)convertView.findViewById(R.id.item_borrowinout_tv_bookcount);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String s = list.get(position);
        holder.tvName.setText(s);

        return convertView;
    }
    public class ViewHolder{
        public ImageView ivPratroit;
        public TextView tvName;
        public TextView tvState;
        public LinearLayout llCovers;
        public TextView tvBookName;
        public TextView tvBookCount;
    }


}
