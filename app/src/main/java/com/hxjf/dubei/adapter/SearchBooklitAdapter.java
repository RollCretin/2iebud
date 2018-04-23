package com.hxjf.dubei.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.SearchBookListBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/28.
 */

public class SearchBooklitAdapter extends BaseAdapter {
    Context mContext;
    List<SearchBookListBean.ResponseDataBean.ContentBean> mList;

    public SearchBooklitAdapter(Context mContext, List<SearchBookListBean.ResponseDataBean.ContentBean> mList) {
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
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_preferred_booklist,null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.item_prefferred_booklist_iamge);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_prefferred_booklist_title);
            holder.tvDes = (TextView) convertView.findViewById(R.id.item_prefferred_booklist_des);
            holder.tvNum = (TextView) convertView.findViewById(R.id.item_prefferred_booklist_num);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        SearchBookListBean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +contentBean.getCoverPath()).into(holder.ivImage);
        holder.tvTitle.setText(contentBean.getTitle());
        holder.tvDes.setText(Html.fromHtml(contentBean.getSummary()));
        holder.tvNum.setText(contentBean.getBookListTotal()+"本书籍");
        return convertView;
    }

    public class  ViewHolder{
        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvDes;
        public TextView tvNum;
    }
}
