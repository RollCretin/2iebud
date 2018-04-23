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
 * Created by Chen_Zhang on 2017/6/22.
 */

public class BooklistDetailAdapter extends BaseAdapter {
    Context mContext;
    List<String> mList;

    public BooklistDetailAdapter(Context context,List list) {
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
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_booklist_detail,null);
            holder.ivdetailimage = (ImageView) convertView.findViewById(R.id.item_booklist_image);
            holder.tvdetailname = (TextView) convertView.findViewById(R.id.item_booklist_name);
            holder.tvdetailauthor = (TextView) convertView.findViewById(R.id.item_booklist_author);
            holder.tvaddBS = (TextView) convertView.findViewById(R.id.item_booklist_add);
            convertView.setTag(holder);
        }else{
           holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }


    public class ViewHolder{
        public ImageView ivdetailimage;
        public TextView tvdetailname;
        public TextView tvdetailauthor;
        public TextView tvaddBS;
    }
}
