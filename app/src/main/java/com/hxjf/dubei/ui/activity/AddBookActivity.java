package com.hxjf.dubei.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/21.
 */

public class AddBookActivity extends BaseActivity {
    @BindView(R.id.addbook_back)
    ImageView addbookBack;
    @BindView(R.id.addbook_serach)
    ImageView addbookSerach;
    @BindView(R.id.addbook_list)
    ListView addbookList;
    @BindView(R.id.addbook_done)
    Button addbookDone;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_book;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.addbook_back, R.id.addbook_serach, R.id.addbook_done})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addbook_back:
                finish();
                break;
            case R.id.addbook_serach:
                serach();
                break;
            case R.id.addbook_done:
                addDone();
                break;
        }
    }

    private void addDone() {

    }

    private void serach() {

    }
}
