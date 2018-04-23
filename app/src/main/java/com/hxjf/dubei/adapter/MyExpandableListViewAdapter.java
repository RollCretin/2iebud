package com.hxjf.dubei.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    public Map<Integer,Boolean> map=new HashMap<>();// 存放已被选中的CheckBox
    Map<String, List<String>> dataset;
    List<String> parentList;
    Context context;
    private ImageView pratroit;
    private TextView owner_name;
    private CheckBox checkBox;
    private ImageView childImage;
    private TextView childName;
    private TextView childAuthor;
    private TextView childDelete;
    private Callback mCallback;

    public MyExpandableListViewAdapter(Map<String, List<String>> dataset, List<String> parentList, Callback callback,Context context) {
        this.dataset = dataset;
        this.parentList = parentList;
        this.context = context;
        this.mCallback = callback;
    }

    /*获取父项个数*/
    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    /*获取某个父项的子项个数*/
    @Override
    public int getChildrenCount(int groupPosition) {
        Log.d("hahaha..", "getChildrenCount: "+groupPosition);
        Log.d("hahaha..", "getChildrenCount: "+parentList.get(groupPosition));
        Log.d("hahaha..", "getChildrenCount: "+dataset.get(parentList.get(groupPosition)));
        return dataset.get(parentList.get(groupPosition)).size();
    }

    /*获取某个父项*/
    @Override
    public Object getGroup(int groupPosition) {
        return dataset.get(parentList.get(groupPosition));
    }

    /*获取某个父项的子项*/
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataset.get(parentList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String parentStr = parentList.get(groupPosition);
        GroupHolder groupHolder = null;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = View.inflate(context, R.layout.view_book_pocket_group, null);
            groupHolder.pratroit = (ImageView) convertView.findViewById(R.id.item_book_pocket_group_pratroit);
            groupHolder.owner_name = (TextView) convertView.findViewById(R.id.item_book_pocket_group_name);
            groupHolder.checkBox = (CheckBox) convertView.findViewById(R.id.item_book_pocket_group_checkbox);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    map.put(groupPosition,true);
                }else {
                    map.remove(groupPosition);
                }
            }
        });

        if(map!=null&&map.containsKey(groupPosition)){
            groupHolder.checkBox.setChecked(true);
        }else {
            groupHolder.checkBox.setChecked(false);
        }
        //设置 TODO
        groupHolder.owner_name.setText(parentStr);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String chidStr = dataset.get(parentList.get(groupPosition)).get(childPosition);
        ChildHolder childHolder = null;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = View.inflate(context, R.layout.view_book_pocket_child, null);
            childHolder.childImage = (ImageView) convertView.findViewById(R.id.item_book_pocket_child_image);
            childHolder.childName = (TextView) convertView.findViewById(R.id.item_book_pocket_child_name);
            childHolder.childAuthor = (TextView) convertView.findViewById(R.id.item_book_pocket_child_author);
            childHolder.childDelete = (TextView) convertView.findViewById(R.id.item_book_pocket_child_delete);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.childDelete.setOnClickListener(this);
        childHolder.childDelete.setTag(groupPosition+"/"+childPosition);
        //TODO 设置
        childHolder.childName.setText(chidStr);


        return convertView;
    }

    /*
    子项是否可选中，如果需要设置子项的点击事件，需要返回true
    */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item_book_pocket_child_delete) {
            mCallback.delete(v);
        }
    }

    //定义接口
    public interface Callback {
        void delete(View view);
    }

    static class GroupHolder {
        ImageView pratroit;
        TextView owner_name;
        CheckBox checkBox;
    }

    static class ChildHolder {
        ImageView childImage;
        TextView childName;
        TextView childAuthor;
        TextView childDelete;
    }


}
