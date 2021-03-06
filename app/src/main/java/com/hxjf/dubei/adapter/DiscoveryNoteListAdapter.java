package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.NoteFactoryListBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Chen_Zhang on 2017/6/14.
 * 发现模块笔记工厂的适配器
 */

public class DiscoveryNoteListAdapter extends BaseAdapter {
    Context mcontext;
    List<NoteFactoryListBean.ResponseDataBean.ContentBean> mList;

    public DiscoveryNoteListAdapter(Context context, List<NoteFactoryListBean.ResponseDataBean.ContentBean> list) {
        this.mcontext = context;
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
            convertView = View.inflate(mcontext, R.layout.item_discovery_note,null);
            holder = new ViewHolder();
            holder.ivNote = (ImageView) convertView.findViewById(R.id.item_discovery_note);
            holder.tvNoteName = (TextView) convertView.findViewById(R.id.item_discovery_note_name);
            holder.tvNoteAuthor = (TextView) convertView.findViewById(R.id.item_discovery_author);
            holder.tvNoteContent = (TextView) convertView.findViewById(R.id.item_discovery_note_content);
            holder.ivPratroit = (CircleImageView) convertView.findViewById(R.id.item_discovery_note_pratroit);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_discovery_note_fromwho);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        NoteFactoryListBean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        holder.tvNoteName.setText("【"+contentBean.getTagValue()+"】"+contentBean.getTitle());
        holder.tvNoteAuthor.setText(contentBean.getLecturer());
        holder.tvNoteContent.setText(contentBean.getContentAbout());
        holder.tvName.setText(contentBean.getOwnerName());
        Glide.with(mcontext).load(ReaderRetroift.IMAGE_URL +contentBean.getCover()).into(holder.ivNote);
        Glide.with(mcontext).load(ReaderRetroift.IMAGE_URL +contentBean.getOwnerHeadPath()).into(holder.ivPratroit);

        return convertView;
    }

    public class ViewHolder{
        public ImageView ivNote;
        public TextView tvNoteName;
        public TextView tvNoteAuthor;
        public TextView tvNoteContent;
        public CircleImageView ivPratroit;
        public TextView tvName;
    }
}
