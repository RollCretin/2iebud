package com.hxjf.dubei.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/7/14.
 */

public class FeedbackAactivity extends BaseActivity {
    @BindView(R.id.feedback_back)
    ImageView feedbackBack;
    @BindView(R.id.feedback_et_suggestion)
    EditText feedbackEtSuggestion;
    @BindView(R.id.feedback_submit)
    TextView feedbackSubmit;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.feedback_back, R.id.feedback_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback_back:
                finish();
                break;
            case R.id.feedback_submit:
                String suggestion = feedbackEtSuggestion.getText().toString();
                //上传建议
                //TODO
                feedbackEtSuggestion.setText("");
                Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
