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

public class ReaderNoteListAdapter extends BaseAdapter {
    Context mContext;
    List<Bookmark> mList;

    public ReaderNoteListAdapter(Context context, List<Bookmark> list) {
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
            convertView = View.inflate(mContext, R.layout.item_reader_note,null);
            holder.tvnote = (TextView) convertView.findViewById(R.id.item_tv_note);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String note = mList.get(position).getText().toString();
        //去除笔记的标记#
        String newNote = note.replaceFirst("^#*", "");
        holder.tvnote.setText(newNote);
        return convertView;
    }

    public class ViewHolder{
        public TextView tvnote;
    }
}
