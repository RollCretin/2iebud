package com.hxjf.dubei.ui.activity;

import android.widget.ImageView;
import android.widget.ListView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.adapter.NoteListAdapter;
import com.hxjf.dubei.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/23.
 */

public class MyNoteBookActivity extends BaseActivity {
    @BindView(R.id.mynotebook_back)
    ImageView mynotebookBack;
    @BindView(R.id.my_notebook_list)
    ListView myNotebookList;
    private List<String> list;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_notebook;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        //请求数据
        //TODO
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i+"");
        }

        NoteListAdapter adapter = new NoteListAdapter(this,list);
        myNotebookList.setAdapter(adapter);

    }


    @OnClick(R.id.mynotebook_back)
    public void onClick() {
        finish();
    }
}
