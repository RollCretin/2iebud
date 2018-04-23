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
import com.hxjf.dubei.bean.MyBookListBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/6/22.
 */

public class BooklistAdapter extends BaseAdapter {

    Context mContext;
    List<MyBookListBean.ResponseDataBean> mList;

    public BooklistAdapter(Context context, List<MyBookListBean.ResponseDataBean> list) {
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
        ViewHolder holder =null;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.item_my_book_list,null);
            holder = new ViewHolder();
            holder.ivbooklistimage = (ImageView) convertView.findViewById(R.id.my_booklist_image);
            holder.tvbooklisttitle = (TextView) convertView.findViewById(R.id.my_booklist_name);
            holder.tvbooklistcount = (TextView) convertView.findViewById(R.id.my_booklist_count);
            holder.tvbooklistdes = (TextView) convertView.findViewById(R.id.my_booklist_des);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        MyBookListBean.ResponseDataBean responseDataBean = mList.get(position);
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +responseDataBean.getCoverPath()).into(holder.ivbooklistimage);
        holder.tvbooklisttitle.setText(responseDataBean.getTitle());
        holder.tvbooklistcount.setText(responseDataBean.getBookListTotal()+"本书籍");
        holder.tvbooklistdes.setText(Html.fromHtml(responseDataBean.getSummary()));
        return convertView;
    }

    public class ViewHolder{
        public ImageView ivbooklistimage;
        public TextView tvbooklisttitle;
        public TextView tvbooklistcount;
        public TextView tvbooklistdes;
    }
}
