package com.hxjf.dubei.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/25 0025.
 */

public class SaveExplainActivity extends BaseActivity {
    @BindView(R.id.save_explain_back)
    ImageView saveExplainBack;
    @BindView(R.id.save_explain_save)
    TextView saveExplainSave;
    @BindView(R.id.save_explain_edittext)
    EditText saveExplainEdittext;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_save_explain;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);

    }

    @OnClick({R.id.save_explain_back, R.id.save_explain_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save_explain_back:
                finish();
                break;
            case R.id.save_explain_save:

                break;
        }
    }
}
