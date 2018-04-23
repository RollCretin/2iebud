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

public class NoteListAdapter extends BaseAdapter {
    Context mContext;
    List mList;
    public NoteListAdapter(Context context, List list) {
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
            convertView = View.inflate(mContext, R.layout.item_my_notebook,null);
            holder = new ViewHolder();
            holder.ivNoteImage = (ImageView) convertView.findViewById(R.id.item_notebook_image);
            holder.tvNoteName = (TextView) convertView.findViewById(R.id.item_notebook_name);
            holder.tvNoteAuthor = (TextView) convertView.findViewById(R.id.item_notebook_author);
            holder.tvNoteCount = (TextView) convertView.findViewById(R.id.item_notebook_count);
            holder.tvNoteTime = (TextView) convertView.findViewById(R.id.item_notebook_time);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();

        }
        return convertView;
    }

    public class ViewHolder{
        public ImageView ivNoteImage;
        public TextView tvNoteName;
        public TextView  tvNoteAuthor;
        public TextView  tvNoteCount;
        public TextView tvNoteTime;
    }
}
