package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjf.dubei.R;

import org.geometerplus.fbreader.book.Bookmark;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/10.
 */

public class BookmarkListAdapter extends BaseAdapter {
    Context mContext;
    List<Bookmark> mList;
    public BookmarkListAdapter(Context context, List<Bookmark> list) {
        mContext = context;
        mList = list;
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
            convertView = View.inflate(mContext, R.layout.item_reader_bookmark,null);
            holder.tvbookmark = (TextView) convertView.findViewById(R.id.item_tv_bookmark);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Bookmark bookmark = mList.get(position);
        holder.tvbookmark.setText(bookmark.getText());
        return convertView;
    }

    public class ViewHolder{
        public TextView tvbookmark;
    }

}
