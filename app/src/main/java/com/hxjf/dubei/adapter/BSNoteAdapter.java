package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.BSNoteListBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/6/12.
 * 书架中笔记工厂适配器
 */

public class BSNoteAdapter extends BaseAdapter {
    List<BSNoteListBean.ResponseDataBean> mList;
    Context mContext;


    public BSNoteAdapter(List<BSNoteListBean.ResponseDataBean> list, Context context) {
        this.mList = list;
        this.mContext = context;
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
            convertView = View.inflate(mContext, R.layout.item_bs_note_factory,null);
            holder = new ViewHolder();
            holder.ivNote = (ImageView) convertView.findViewById(R.id.item_bs_note);
            holder.tvNoteState = (TextView) convertView.findViewById(R.id.item_bs_note_state);
            holder.tvNoteName = (TextView) convertView.findViewById(R.id.item_bs_note_name);
            convertView.setTag(holder);
        }else{

            holder = (ViewHolder) convertView.getTag();
        }
        BSNoteListBean.ResponseDataBean bean = mList.get(position);
        if (bean == null || bean.getNoteFactoryInfo() == null){
            return convertView;
        }
        if (bean.getReadStatus() != 3){
            holder.tvNoteState.setVisibility(View.GONE);
        }else{
            holder.tvNoteState.setVisibility(View.VISIBLE);
        }
        holder.tvNoteName.setText(bean.getNoteFactoryInfo().getTitle());
        String coverId = bean.getNoteFactoryInfo().getCoverId();
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL+bean.getNoteFactoryInfo().getCoverPath()).into(holder.ivNote);
        return convertView;
    }

    public class ViewHolder{
        public ImageView ivNote;
        public TextView tvNoteState;
        public TextView tvNoteName;
    }
}
