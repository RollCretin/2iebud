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
import com.hxjf.dubei.bean.BookListbean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chen_Zhang on 2017/7/15.
 */

public class BookListItemListAdapter extends BaseAdapter {
    Context mContext;
    List<BookListbean.ResponseDataBean.ContentBean> mList;
    public BookListItemListAdapter(Context context, List<BookListbean.ResponseDataBean.ContentBean> list) {
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
            convertView = View.inflate(mContext,R.layout.item_preferred_booklist,null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.item_prefferred_booklist_iamge);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_prefferred_booklist_title);
            holder.tvDes = (TextView) convertView.findViewById(R.id.item_prefferred_booklist_des);
            holder.tvNum = (TextView) convertView.findViewById(R.id.item_prefferred_booklist_num);
            holder.ivPratroit = (CircleImageView) convertView.findViewById(R.id.item_prefferred_booklist_pratroit);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_prefferred_booklist_name);
            convertView.setTag(holder);
        }else{
           holder = (ViewHolder) convertView.getTag();
        }
        BookListbean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +contentBean.getCover()).into(holder.ivImage);
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +contentBean.getOwnerHeadPath()).into(holder.ivPratroit);
        holder.tvName.setText(contentBean.getOwnerName());
        holder.tvTitle.setText(contentBean.getTitle());
        String des = Html.fromHtml(contentBean.getSummary()).toString();
        holder.tvDes.setText(des);
        holder.tvNum.setText(contentBean.getBookCount()+"本书籍");
        return convertView;
    }
    public class  ViewHolder{
        public ImageView ivImage;
        public TextView tvTitle;
        public TextView tvDes;
        public TextView tvNum;
        public CircleImageView ivPratroit;
        public TextView tvName;
    }
}
