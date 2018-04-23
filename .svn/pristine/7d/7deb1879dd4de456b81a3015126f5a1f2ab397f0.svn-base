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
 * Created by Chen_Zhang on 2017/9/20.
 * 接口回调
 */

public class CollectBookAdapter extends BaseAdapter implements View.OnClickListener{
    Context mContext;
    List<FavoriteBookListbean.ResponseDataBean.ContentBean> mList;
    private Callback mCallback;

    public CollectBookAdapter(Context mContext, List<FavoriteBookListbean.ResponseDataBean.ContentBean> mList,Callback callback) {
        this.mContext = mContext;
        this.mList = mList;
        mCallback = callback;
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
            convertView = View.inflate(mContext, R.layout.item_collect_book,null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.item_collect_book_image);
            holder.llbook = (LinearLayout) convertView.findViewById(R.id.item_collect_book_ll);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_collect_book_name);
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.item_collect_book_author);
            holder.tvScore = (TextView) convertView.findViewById(R.id.item_collect_book_score);
            holder.rbScore = (RatingBar) convertView.findViewById(R.id.item_collect_book_ratingbar);
            holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.tvState = (TextView) convertView.findViewById(R.id.item_collect_book_state);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvDelete.setOnClickListener(this);
        holder.llbook.setOnClickListener(this);
        holder.tvDelete.setTag(position);
        holder.llbook.setTag(position);
        FavoriteBookListbean.ResponseDataBean.ContentBean contentBean = mList.get(position);
        Glide.with(mContext).load(NewDiscoveryRetrofit.IMAGE_URL+contentBean.getCover()).into(holder.ivImage);
        holder.tvName.setText(contentBean.getName());
        holder.tvAuthor.setText(contentBean.getAuthor());
        Float doubanScore = Float.valueOf(contentBean.getDoubanScore());
        holder.rbScore.setRating((float) (doubanScore/2.0));
        holder.tvScore.setText("豆瓣/"+contentBean.getDoubanScore()+"分");

        return convertView;
    }

    @Override
    public void onClick(View v) {
       if (v.getId() == R.id.tv_delete){
        mCallback.delete(v);
       }else{
           mCallback.click(v);
       }
    }



   public interface Callback{
        void click(View v);
        void delete(View v);
   }

    public class ViewHolder{
        public LinearLayout llbook;
        public ImageView ivImage;
        public TextView tvName;
        public TextView tvAuthor;
        public TextView tvScore;
        public RatingBar rbScore;
        public TextView tvDelete;
        public TextView tvState;
    }
}
