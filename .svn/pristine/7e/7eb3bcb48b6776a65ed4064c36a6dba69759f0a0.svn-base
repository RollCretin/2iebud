package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.FavoriteBookListbean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chen_Zhang on 2017/9/20.
 */

public class MoreBookBorrowAdapter extends BaseAdapter {
    Context mContext;
    List<FavoriteBookListbean.ResponseDataBean.ContentBean> mList;
    private double distanceKM;

    public MoreBookBorrowAdapter(Context mContext, List<FavoriteBookListbean.ResponseDataBean.ContentBean> mList) {
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
            convertView = View.inflate(mContext, R.layout.item_book_borrow,null);
            holder.ivImage  = (ImageView) convertView.findViewById(R.id.item_book_borrow_image);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_book_borrow_name);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.item_book_borrow_author);
            holder.tvScore = (TextView) convertView.findViewById(R.id.item_book_borrow_score);
            holder.rbScore = (RatingBar) convertView.findViewById(R.id.item_book_borrow_ratingbar);
            holder.tvLocation = (TextView) convertView.findViewById(R.id.item_book_borrow_location);
            holder.tvFromWho = (TextView) convertView.findViewById(R.id.item_book_borrow_formwho);
            holder.civPratroit = (CircleImageView) convertView.findViewById(R.id.item_book_borrow_pratroit);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        FavoriteBookListbean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        Glide.with(mContext).load(NewDiscoveryRetrofit.IMAGE_URL+contentBean.getCover()).into(holder.ivImage);
        Glide.with(mContext).load(NewDiscoveryRetrofit.IMAGE_URL+contentBean.getOwnerHeadPhoto()).into(holder.civPratroit);
        holder.tvName.setText(contentBean.getName());
        holder.tvAuthor.setText(contentBean.getAuthor());
        Float doubanScore = Float.valueOf(contentBean.getDoubanScore());
        holder.rbScore.setRating((float) (doubanScore/2.0));
        holder.tvScore.setText("豆瓣/"+contentBean.getDoubanScore()+"分");
        Double distance = Double.valueOf(contentBean.getDistance());
//        if (distance >= 1000){
//            distanceKM = distance / 1000.0;
//            DecimalFormat format = new DecimalFormat("######0.00");
//            holder.tvLocation.setText(format.format(distanceKM) + " KM");
//        }else {
//            holder.tvLocation.setText((new Double(distance)).intValue() + " M");
//        }
        holder.tvLocation.setText(contentBean.getCityDistrict());
        holder.tvFromWho.setText(contentBean.getOwnerName());
//        holder.civPratroit
        return convertView;
    }

    public class ViewHolder{
        public ImageView ivImage;
        public TextView tvName;
        public TextView tvAuthor;
        public TextView tvScore;
        public RatingBar rbScore;
        public TextView tvLocation;
        public CircleImageView civPratroit;
        public TextView tvFromWho;
    }

}
