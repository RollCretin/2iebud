package com.hxjf.dubei.ui.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/21.
 */

public class MybackgroundActivity extends BaseActivity {
    @BindView(R.id.background_back)
    ImageView backgroundBack;
    @BindView(R.id.background_save)
    TextView backgroundSave;
    @BindView(R.id.background_school_name)
    EditText backgoroundschoolname;
    @BindView(R.id.background_education)
    TextView backgroundEducation;
    @BindView(R.id.background_major)
    EditText backgroundMajor;
    @BindView(R.id.background_end_time)
    TextView backgroundEndTime;
    @BindView(R.id.background_starttime)
    TextView backgroundStarttime;
    @BindView(R.id.experience_delete)
    TextView experienceDelete;

    private ArrayList<String> educationData;
    ArrayList<String> starttimeData;//开始时间
    ArrayList<String> endtimeData;//结束时间

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_background;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
//        高中以下、专科、本科、研究生、博士及以上。
        educationData = new ArrayList<>();
        educationData.add("高中以下");
        educationData.add("专科");
        educationData.add("本科");
        educationData.add("研究生");
        educationData.add("博士及以上");

        starttimeData = new ArrayList<>();
        endtimeData = new ArrayList<>();
        for (int i = 1950; i <= 2017; i++) {
            if (i == 2017) {
                starttimeData.add(String.valueOf(2017));
                endtimeData.add("至今");
            } else {
                starttimeData.add(String.valueOf(i));
                endtimeData.add(String.valueOf(i));
            }
        }
    }



    @OnClick({R.id.background_back, R.id.background_save, R.id.background_school_name, R.id.background_education, R.id.background_major, R.id.background_end_time, R.id.background_starttime, R.id.experience_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.background_back:
                finish();
                break;
            case R.id.background_save:
                break;
            case R.id.background_school_name:
                backgoroundschoolname.setCursorVisible(true);
                break;
            case R.id.background_education:
                educationDialog();
                break;
            case R.id.background_major:
                backgroundMajor.setCursorVisible(true);
                break;
            case R.id.background_end_time:
            case R.id.background_starttime:
                timeDialog();
                break;
            case R.id.experience_delete:
                break;
        }
    }

    //时间选择
    private void timeDialog() {
        Dialog timeDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_time, null);
        WheelView starttime = (WheelView) contentView.findViewById(R.id.dialog_start_time);
        WheelView endtime = (WheelView) contentView.findViewById(R.id.dialog_end_time);
        starttime.setWheelAdapter(new ArrayWheelAdapter(this));
        endtime.setWheelAdapter(new ArrayWheelAdapter(this));
        starttime.setWheelData(starttimeData);
        endtime.setWheelData(endtimeData);
        starttime.setSkin(WheelView.Skin.Holo);
        endtime.setSkin(WheelView.Skin.Holo);
        starttime.setWheelSize(5);
        endtime.setWheelSize(5);

        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#000000");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;
        style.textAlpha = 0.6f;
        starttime.setStyle(style);
        endtime.setStyle(style);
        starttime.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                backgroundStarttime.setText(starttimeData.get(position) + "~");
            }
        });
        endtime.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                backgroundEndTime.setText(endtimeData.get(position));
            }
        });


        timeDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        timeDialog.getWindow().setGravity(Gravity.BOTTOM);
        timeDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        timeDialog.show();

    }

    //教育选择
    private void educationDialog() {
        Dialog educationDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_industry, null);
        WheelView wheelview = (WheelView) contentView.findViewById(R.id.industry_wheelview);
        wheelview.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wheelview.setWheelData(educationData);
        wheelview.setWheelSize(5);
        wheelview.setSkin(WheelView.Skin.Holo);
        wheelview.setLoop(true);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#000000");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 20;
        style.textAlpha = 0.6f;
        wheelview.setStyle(style);

        wheelview.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                backgroundEducation.setText(educationData.get(position));

            }
        });

        educationDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        educationDialog.getWindow().setGravity(Gravity.BOTTOM);
        educationDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        educationDialog.show();
    }
}
