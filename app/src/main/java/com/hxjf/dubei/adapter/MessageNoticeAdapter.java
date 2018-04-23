package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.MyMessageBean;
import com.hxjf.dubei.network.ReaderRetroift;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/8/17.
 */

public class MessageNoticeAdapter extends BaseAdapter {
    Context mContext;
    List<MyMessageBean.ResponseDataBean.ListBean> list;

    public MessageNoticeAdapter(Context mContext, List<MyMessageBean.ResponseDataBean.ListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_message_notice, null);
            holder = new ViewHolder();
            holder.ivProtrait = (ImageView) convertView.findViewById(R.id.item_message_protrait);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_message_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.item_message_time);
            holder.tvContent = (TextView) convertView.findViewById(R.id.item_message_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyMessageBean.ResponseDataBean.ListBean responseDataBean = list.get(position);
        if (responseDataBean.getMsgFrom().equals("DUBEI_ASSISTANT")) {
            //读呗小助手，头像不变，名字 读呗小助手
            holder.tvName.setText("读呗小助手");
            Glide.with(mContext).load(R.mipmap.dubei_dafult_pratroit).into(holder.ivProtrait);
        } else {
            if (responseDataBean.getUserInfo() != null) {
                Glide.with(mContext).load(ReaderRetroift.IMAGE_URL + responseDataBean.getUserInfo().getNormalPath()).into(holder.ivProtrait);
                holder.tvName.setText(responseDataBean.getUserInfo().getNickName());
            }else{
                Glide.with(mContext).load(R.mipmap.user_default_portrait).into(holder.ivProtrait);
                holder.tvName.setText("未知");
            }
        }
        holder.ivProtrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemDeleteListener.onDeleteClick(position);
            }
        });
        String createTime = responseDataBean.getCreateTime();
        String[] times = createTime.split("-");
        Integer month = Integer.valueOf(times[1]);
        Integer day = Integer.valueOf(times[2]);
        holder.tvTime.setText(month + "月" + day + "日");
        holder.tvContent.setText(responseDataBean.getContent());
        return convertView;
    }
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setOnItemDeleteClickListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    public class ViewHolder {
        public ImageView ivProtrait;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvContent;
    }
}
