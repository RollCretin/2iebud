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
import com.hxjf.dubei.bean.ChaiduListBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chen_Zhang on 2017/7/15.
 */

public class BooKAnalysisListAdapter extends BaseAdapter {
    Context mContext;
    List<ChaiduListBean.ResponseDataBean.ContentBean> mList;

    public BooKAnalysisListAdapter(Context context, List<ChaiduListBean.ResponseDataBean.ContentBean> list) {
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
            convertView = View.inflate(mContext, R.layout.item_more_book,null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.more_book_iamge);
            holder.tvState = (TextView) convertView.findViewById(R.id.more_book_state);
            holder.tvName = (TextView) convertView.findViewById(R.id.more_book_name);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.more_book_author);
            holder.tvScore = (TextView) convertView.findViewById(R.id.more_book_score);
            holder.pbScore = (RatingBar) convertView.findViewById(R.id.more_book_ratingbar);
            holder.ivPratroit = (CircleImageView) convertView.findViewById(R.id.more_book_pratroit);
            holder.tvFromwho = (TextView) convertView.findViewById(R.id.more_book_fromwho);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ChaiduListBean.ResponseDataBean.ContentBean responseDataBean = mList.get(position);
        holder.tvName.setText(responseDataBean.getName());
        holder.tvState.setVisibility(View.VISIBLE);
        holder.tvAuthor.setText(responseDataBean.getAuthor());
        holder.tvScore.setText("豆瓣/"+responseDataBean.getDoubanScore()+"分");
        holder.pbScore.setRating((float) (responseDataBean.getDoubanScore()/2.0));
        holder.tvFromwho.setText(responseDataBean.getOwnerName());
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +responseDataBean.getCover()).into(holder.ivImage);
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +responseDataBean.getOwnerHeadPath()).into(holder.ivPratroit);
        return convertView;
    }

    public class ViewHolder{
        public ImageView ivImage;
        public TextView tvState;
        public TextView tvName;
        public TextView tvAuthor;
        public TextView tvScore;
        public RatingBar pbScore;
        public CircleImageView ivPratroit;
        public TextView tvFromwho;
    }
}
