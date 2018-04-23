package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.bean.NewDiscoveryBean;
import com.hxjf.dubei.network.NewDiscoveryRetrofit;
import com.hxjf.dubei.util.DensityUtil;

import java.util.List;

import jp.wasabeef.glide.transformations.CropTransformation;

/**
 * Created by Chen_Zhang on 2017/9/14.
 */

public class GalleryAdapter extends BaseAdapter {
    Context mContext;
    List<NewDiscoveryBean.ResponseDataBean.BookbarsBean> bookbars;
    private int selectItem;

    public GalleryAdapter(Context mContext, List<NewDiscoveryBean.ResponseDataBean.BookbarsBean> bookbars) {
        this.mContext = mContext;
        this.bookbars = bookbars;
    }

    @Override
    public int getCount() {
        return bookbars.size();       //这里的目的是可以让图片循环浏览
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectItem(int selectItem) {

        if (this.selectItem != selectItem) {
            this.selectItem = selectItem;
            notifyDataSetChanged();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ImageView imageView = new ImageView(mContext);
        String imgUrl = NewDiscoveryRetrofit.IMAGE_URL + bookbars.get(position % bookbars.size()).getBanner();
        Glide.with(mContext).load(imgUrl).bitmapTransform(new CropTransformation(mContext, DensityUtil.dip2px(mContext, 236), DensityUtil.dip2px(mContext, 120), CropTransformation.CropType.CENTER)).into(imageView);
//取余，让图片循环浏览

        if (selectItem == position) {
//            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.my_scale_action);    //实现动画效果
            imageView.setLayoutParams(new Gallery.LayoutParams(DensityUtil.dip2px(mContext, 315), DensityUtil.dip2px(mContext, 160)));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.startAnimation(animation);  //选中时，这是设置的比较大
        } else {
            imageView.setLayoutParams(new Gallery.LayoutParams(DensityUtil.dip2px(mContext, 236), DensityUtil.dip2px(mContext, 120)));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//未选中
        }
        return imageView;
    }
}
