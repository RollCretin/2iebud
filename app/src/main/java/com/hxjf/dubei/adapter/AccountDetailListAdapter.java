package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.bean.BalanceListBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/14.
 */

public class AccountDetailListAdapter extends BaseAdapter {

    List<BalanceListBean.ResponseDataBean.ListBean> mList;
    Context mContext;

    public AccountDetailListAdapter(Context context, List<BalanceListBean.ResponseDataBean.ListBean> list) {
        mList = list;
        mContext = context;
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
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_account_detail, null);
            holder.tvUsefulness = (TextView) convertView.findViewById(R.id.item_account_detail_usefulness);
            holder.tvtime = (TextView) convertView.findViewById(R.id.item_account_detail_time);
            holder.tvmoney = (TextView) convertView.findViewById(R.id.item_account_detail_money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BalanceListBean.ResponseDataBean.ListBean listBean = mList.get(position);
        holder.tvUsefulness.setText(listBean.getInfo());
        double affectMoney = listBean.getAffectMoney();
        DecimalFormat df = new DecimalFormat("######0.00");
        if (affectMoney > 0){
            holder.tvmoney.setText("+"+df.format(affectMoney) + "");
        }else{
            holder.tvmoney.setText(df.format(affectMoney) + "");
        }
        holder.tvtime.setText(listBean.getCreateTime());
        return convertView;
    }

    public class ViewHolder {
        public TextView tvUsefulness;
        public TextView tvtime;
        public TextView tvmoney;
    }
}
