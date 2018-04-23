package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/6/22.
 */

public class BuildBookAdapter extends BaseAdapter {
    Context mContext;
    List mList;

    public BuildBookAdapter(Context context, List list) {
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
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_build_booklist,null);
            holder = new ViewHolder();
            holder.ivbook = (ImageView) convertView.findViewById(R.id.item_buildbooklist_image);
            holder.tvbookname  = (TextView) convertView.findViewById(R.id.item_buildbooklist_name);
            holder.tvbookauthor  = (TextView) convertView.findViewById(R.id.item_buildbooklist_author);
            holder.edbookremark = (EditText) convertView.findViewById(R.id.item_buildbooklist_remark);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //设置属性

        //对editText绑定
        holder.edbookremark.setTag(mList.get(position));

        return convertView;
    }

    public class ViewHolder{
        public ImageView ivbook;
        public TextView tvbookname;
        public TextView tvbookauthor;
        public EditText edbookremark;

    }
}
