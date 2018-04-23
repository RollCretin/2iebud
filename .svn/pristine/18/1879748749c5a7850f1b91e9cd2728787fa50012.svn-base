package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.BSBookListBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/6/12.
 * 书籍、好书拆读的适配器
 */

public class BSBookAdapter extends BaseAdapter {
    List<BSBookListBean.ResponseDataBean> mList;
    Context mContext;
    GridView mGridView;


    public BSBookAdapter(GridView gridView, List<BSBookListBean.ResponseDataBean> list, Context context) {
        this.mList = list;
        this.mContext = context;
        this.mGridView = gridView;
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
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_bs_book, null);
            holder = new ViewHolder();

            //设置属性
            holder.ivBook = (ImageView) convertView.findViewById(R.id.item_bs_book);
            holder.tvBookState = (TextView) convertView.findViewById(R.id.item_bs_book_state);
            holder.tvBookName = (TextView) convertView.findViewById(R.id.item_bs_book_name);

            convertView.setTag(holder);
            holder.update();
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //设置属性
        BSBookListBean.ResponseDataBean bean = mList.get(position);
        if (bean == null || bean.getBookInfo() == null || bean.getBookInfo().getStatus() == 0) {
            Glide.with(mContext).load(R.mipmap.gray_bg).into(holder.ivBook);
            holder.tvBookState.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
            holder.tvBookState.setVisibility(View.VISIBLE);
            holder.tvBookState.setText("已下架");
            holder.tvBookName.setText("该书已下架");
            return convertView;
        }else{
            Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + bean.getBookInfo().getCoverPath()).into(holder.ivBook);
            holder.tvBookName.setText(bean.getBookInfo().getName());
            int progress = bean.getProgress();
            if (bean.getChallengeFlag() == 1) {
                holder.tvBookState.setVisibility(View.VISIBLE);
                holder.tvBookState.setText(bean.getFlagValue());
                holder.tvBookState.setBackgroundColor(mContext.getResources().getColor(R.color.red));

            } else if (bean.getReadStatus() == 3) {
                holder.tvBookState.setVisibility(View.VISIBLE);
                holder.tvBookState.setText(bean.getStatusValue());
                holder.tvBookState.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
            } else {
                holder.tvBookState.setVisibility(View.GONE);
            }
            holder.tvBookState.setTag(position);
            holder.tvBookName.setTag(convertView);
            return convertView;

        }
    }

    public class ViewHolder {
        public ImageView ivBook;
        public TextView tvBookState;
        public TextView tvBookName;

        public void update() {
            ivBook.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (tvBookState.getTag() != null) {
                        int position = (Integer) tvBookState.getTag();
                        if (position == 0 || position % 3 == 0) {
                            //比较三个item的高度，取最高一个
                            View view = (View) tvBookName.getTag();
                            int height = 0;
                            if (view != null) {
                                height = view.getHeight();
                            }

                            View view1 = mGridView.getChildAt(position + 1);
                            int height1 = 0;
                            if (view1 != null) {
                                height1 = view1.getHeight();
                            }

                            View view2 = mGridView.getChildAt(position + 2);
                            int height2 = 0;
                            if (view2 != null) {
                                height2 = view2.getHeight();
                            }

                            if (height >= height1 && height >= height2) {
                                if (view1 != null) {
                                    view1.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, height));
                                }
                                if (view2 != null) {
                                    view2.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, height));
                                }
                            } else if (height1 >= height && height1 >= height2) {
                                if (view != null) {
                                    view.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, height1));
                                }
                                if (view2 != null) {
                                    view2.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, height1));
                                }

                            } else if (height2 >= height && height2 >= height1) {
                                if (view != null) {
                                    view.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, height2));
                                }
                                if (view1 != null) {
                                    view1.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.FILL_PARENT, height2));
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
