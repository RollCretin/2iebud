package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.SearchNoteBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/28.
 */

public class SearchNoteAdapter extends BaseAdapter {
    Context mContext;
    List<SearchNoteBean.ResponseDataBean.ContentBean> mList;

    public SearchNoteAdapter(Context mContext, List<SearchNoteBean.ResponseDataBean.ContentBean> mList) {
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
            convertView = View.inflate(mContext, R.layout.item_discovery_note,null);
            holder = new ViewHolder();
            holder.ivNote = (ImageView) convertView.findViewById(R.id.item_discovery_note);
            holder.tvNoteName = (TextView) convertView.findViewById(R.id.item_discovery_note_name);
            holder.tvNoteAuthor = (TextView) convertView.findViewById(R.id.item_discovery_author);
            holder.tvNoteContent = (TextView) convertView.findViewById(R.id.item_discovery_note_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        SearchNoteBean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        holder.tvNoteName.setText("【"+ contentBean.getTagValue() +"】"+contentBean.getTitle());
        holder.tvNoteAuthor.setText(contentBean.getLecturer());
        holder.tvNoteContent.setText(contentBean.getContentAbout());
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +contentBean.getCoverPath()).into(holder.ivNote);

        return convertView;
    }

    public class ViewHolder{
        public ImageView ivNote;
        public TextView tvNoteName;
        public TextView tvNoteAuthor;
        public TextView tvNoteContent;
    }
}
