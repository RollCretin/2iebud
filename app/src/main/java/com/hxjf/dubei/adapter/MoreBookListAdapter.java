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
import com.hxjf.dubei.bean.HotBookBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chen_Zhang on 2017/7/31.
 */

public class MoreBookListAdapter extends BaseAdapter {
    Context mContext;
    List<HotBookBean.ResponseDataBean.ListBean> mList;
    public MoreBookListAdapter(Context context, List<HotBookBean.ResponseDataBean.ListBean> list) {
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
        HotBookBean.ResponseDataBean.ListBean listBean = mList.get(position);
        Glide .with(mContext).load(ReaderRetroift.IMAGE_URL +listBean.getCoverPath()).into(holder.ivImage);
        holder.tvName.setText(listBean.getName());
        holder.tvAuthor.setText(listBean.getAuthor());
        holder.pbScore.setRating((float) (listBean.getDoubanScore()/2));
        holder.tvScore.setText("豆瓣/"+listBean.getDoubanScore()+"分");
        holder.tvFromwho.setText(listBean.getOwnerName());
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +listBean.getOwnerHeadPath()).into(holder.ivPratroit);
        return convertView;
    }

    public class ViewHolder{
        public ImageView ivImage;
        public TextView tvName;
        public TextView tvAuthor;
        public TextView tvScore;
        public RatingBar pbScore;
        public CircleImageView ivPratroit;
        public TextView tvFromwho;
    }
}
