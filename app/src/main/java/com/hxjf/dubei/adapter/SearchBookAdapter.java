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
import com.hxjf.dubei.bean.SearchBookBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chen_Zhang on 2017/8/21.
 */

public class SearchBookAdapter extends BaseAdapter {
    Context mContext;
    List<SearchBookBean.ResponseDataBean.ListBean> mList;

    public SearchBookAdapter(Context context,List<SearchBookBean.ResponseDataBean.ListBean> list) {
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
        SearchBookAdapter.ViewHolder holder= null;
        if (convertView == null){
            holder = new SearchBookAdapter.ViewHolder();
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
        SearchBookBean.ResponseDataBean.ListBean bean = mList.get(position);
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +bean.getCoverPath()).into(holder.ivImage);
        holder.tvName.setText(bean.getName());
        holder.tvAuthor.setText(bean.getAuthor());
        holder.tvScore.setText("豆瓣/"+bean.getDoubanScore()+"分");
        holder.pbScore.setRating((float) (bean.getDoubanScore()/2.0));
        holder.tvFromwho.setText(bean.getOwnerName());
        Glide.with(mContext).load(ReaderRetroift.IMAGE_URL +bean.getOwnerHeadPath()).into(holder.ivPratroit);
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
