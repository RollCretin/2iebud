package com.hxjf.dubei.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.MyExpandableListViewAdapter;
import com.hxjf.dubei.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/27 0027.
 */

public class BookPocketActivity extends BaseActivity implements MyExpandableListViewAdapter.Callback {
    @BindView(R.id.book_pocket_back)
    ImageView bookPocketBack;
    @BindView(R.id.book_pocket_empty)
    TextView bookPocketEmpty;
    @BindView(R.id.book_pocket_list)
    ExpandableListView bookPocketList;
    @BindView(R.id.book_pocket_btn)
    Button bookPocketBtn;
    @BindView(R.id.book_pocket_ll)
    LinearLayout bookPocketLl;
    private Map<String, List<String>> dataset;
    private List<String> parentList;
    private MyExpandableListViewAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_book_pocket;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //假数据
        dataset = new HashMap<>();
        parentList = new ArrayList();
        parentList.add("书友一");
        parentList.add("书友二");
        parentList.add("书友三");

        List<String> childrenList1 = new ArrayList<>();
        List<String> childrenList2 = new ArrayList<>();
        List<String> childrenList3 = new ArrayList<>();
        childrenList1.add(parentList.get(0) + "的" + "小说");
        childrenList1.add(parentList.get(0)+ "的" + "散文");
        childrenList1.add(parentList.get(0) + "的" + "笔记");
        childrenList2.add(parentList.get(1) + "的" + "散文");
        childrenList2.add(parentList.get(1)+ "的" + "游记");
        childrenList2.add(parentList.get(1) + "的" + "小说");
        childrenList3.add(parentList.get(2) + "的" + "笔记");
        childrenList3.add(parentList.get(2)+ "的" + "唐诗");
        childrenList3.add(parentList.get(2) + "的" + "宋词");
        dataset.put(parentList.get(0), childrenList1);
        dataset.put(parentList.get(1), childrenList2);
        dataset.put(parentList.get(2), childrenList3);

        initData();
    }

    private void initData() {
        bookPocketEmpty.setVisibility(View.GONE);
        adapter = new MyExpandableListViewAdapter(dataset,parentList,BookPocketActivity.this,BookPocketActivity.this);
        bookPocketList.setAdapter(adapter);

        //去除收缩图标
        bookPocketList.setGroupIndicator(null);
        //全部展开
        for (int i = 0; i < parentList.size(); i++) {
            bookPocketList.expandGroup(i);
        }
        //禁止收缩
        bookPocketList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });

    }

    @OnClick({R.id.book_pocket_back, R.id.book_pocket_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.book_pocket_back:
                finish();
                break;
            case R.id.book_pocket_btn:
                borrow();
                break;
        }
    }

    private void borrow() {
        Map<Integer, Boolean> map = adapter.map;
        for (Integer key :
                map.keySet()) {
            if (map.get(key)){
                Toast.makeText(this, "选择"+parentList.get(key), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void delete(View view) {
        String s = (String) view.getTag();
        String[] split = s.split("/");
        int parent = Integer.valueOf(split[0]);
        int child = Integer.valueOf(split[1]);
        List<String> childs = dataset.get(parentList.get(parent));
        childs.remove(child);
        if (childs.size() == 0){
            parentList.remove(parent);
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "父项："+split[0]+"，子项："+split[1], Toast.LENGTH_SHORT).show();
    }
}
