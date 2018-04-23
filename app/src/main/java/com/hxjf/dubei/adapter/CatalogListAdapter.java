package com.hxjf.dubei.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxjf.dubei.R;

import org.geometerplus.fbreader.bookmodel.TOCTree;

import java.util.List;

/**
 * Created by Chen_Zhang on 2017/7/6.
 */

public class CatalogListAdapter extends BaseAdapter {
    Context mContext;
    List<TOCTree> mList;
    TOCTree mCurrenttree;

    public CatalogListAdapter(Context context, List<TOCTree> list, TOCTree currenttree) {
        this.mContext = context;
        this.mList = list;
        mCurrenttree = currenttree;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.item_catalog,null);
            holder = new ViewHolder();
            holder.tvname = (TextView) convertView.findViewById(R.id.item_catalog_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        TOCTree tocTree = mList.get(position);
        if (mCurrenttree != null){
            if (tocTree.getText().equals(mCurrenttree.getText())) {
                holder.tvname.setTextColor(mContext.getResources().getColor(R.color.orange));
                holder.tvname.setText(tocTree.getText());
            } else {
                holder.tvname.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.tvname.setText(tocTree.getText());
            }
        }else{
            holder.tvname.setText(tocTree.getText());
        }


        return convertView;
    }

    public class ViewHolder{
        public TextView tvname;
    }
}
