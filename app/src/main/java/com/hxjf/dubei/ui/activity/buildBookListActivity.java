package com.hxjf.dubei.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.BuildBookAdapter;
import com.hxjf.dubei.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/21.
 */

public class buildBookListActivity extends BaseActivity {
    @BindView(R.id.buildbooklist_back)
    ImageView buildbooklistBack;
    @BindView(R.id.buildbooklist_save)
    TextView buildbooklistSave;
    @BindView(R.id.buildbooklist_title)
    EditText buildbooklistTitle;
    @BindView(R.id.buildbooklist_info)
    EditText buildbooklistInfo;
    @BindView(R.id.buildbooklist_add)
    TextView buildbooklistAdd;
    @BindView(R.id.buildbooklist_listview)
    ListView buildbooklistListview;
    private List<String> booklist;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_buil_booklist;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //获取数据
        //TODO
        booklist = new ArrayList<>();
        //假数据
        for (int i = 0; i < 10; i++) {
            booklist.add(i+"");
        }
        BuildBookAdapter adapter = new BuildBookAdapter(this,booklist);
        buildbooklistListview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.buildbooklist_back, R.id.buildbooklist_save, R.id.buildbooklist_title, R.id.buildbooklist_info, R.id.buildbooklist_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buildbooklist_back:
                finish();
                break;
            case R.id.buildbooklist_save:
                //TODO
                break;
            case R.id.buildbooklist_title:
                buildbooklistTitle.setCursorVisible(true);
                break;
            case R.id.buildbooklist_info:
                buildbooklistInfo.setCursorVisible(true);
                break;
            case R.id.buildbooklist_add:
                Intent intent = new Intent(this,AddBookActivity.class);
                startActivity(intent);
                break;
        }
    }
}
