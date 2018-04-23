package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.FavoriteBookListbean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;

import java.util.List;

/**
 * Created by Chen_Zhang on 2018/1/2.
 */

public class PersonCollectBookAdapter extends BaseAdapter {
    Context mContext;
    List<FavoriteBookListbean.ResponseDataBean.ContentBean> mList;

    public PersonCollectBookAdapter(Context mContext, List<FavoriteBookListbean.ResponseDataBean.ContentBean> mList) {
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
        PersonCollectBookAdapter.ViewHolder holder = null;
        if (convertView == null){
            holder = new PersonCollectBookAdapter.ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_person_collect_book,null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.item_collect_book_image);
            holder.llbook = (LinearLayout) convertView.findViewById(R.id.item_collect_book_ll);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_collect_book_name);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.item_collect_book_author);
            holder.tvScore = (TextView) convertView.findViewById(R.id.item_collect_book_score);
            holder.rbScore = (RatingBar) convertView.findViewById(R.id.item_collect_book_ratingbar);
            convertView.setTag(holder);
        }else{
            holder = (PersonCollectBookAdapter.ViewHolder) convertView.getTag();
        }
        FavoriteBookListbean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        Glide.with(mContext).load(NewDiscoveryRetrofit.IMAGE_URL+contentBean.getCover()).into(holder.ivImage);
        holder.tvName.setText(contentBean.getName());
        holder.tvAuthor.setText(contentBean.getAuthor());
        Float doubanScore = Float.valueOf(contentBean.getDoubanScore());
        holder.rbScore.setRating((float) (doubanScore/2.0));
        holder.tvScore.setText("豆瓣/"+contentBean.getDoubanScore()+"分");

        return convertView;
    }
    public class ViewHolder{
        public LinearLayout llbook;
        public ImageView ivImage;
        public TextView tvName;
        public TextView tvAuthor;
        public TextView tvScore;
        public RatingBar rbScore;
    }
}
