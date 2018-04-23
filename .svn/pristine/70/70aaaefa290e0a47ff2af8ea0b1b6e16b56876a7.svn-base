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
 * Created by Chen_Zhang on 2017/6/20.
 */

public class MyExperienceActivity extends BaseActivity {
    @BindView(R.id.experience_back)
    ImageView experienceBack;
    @BindView(R.id.experience_save)
    TextView experienceSave;
    @BindView(R.id.experienc_company_name)
    EditText experiencCompanyName;
    @BindView(R.id.experience_industry)
    TextView experienceIndustry;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.experience_touxian)
    EditText experienceTouxian;
    @BindView(R.id.experience_time)
    TextView experienceTime;
    @BindView(R.id.experience_delete)
    TextView experienceDelete;
    @BindView(R.id.experience_end_time)
    TextView experienceEndTime;

    ArrayList<String> industryData; //行业信息
    ArrayList<String> starttimeData;//开始时间
    ArrayList<String> endtimeData;//结束时间


    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_experience;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        industryData = new ArrayList<>();
        industryData.add("未毕业");
        industryData.add("互联网");
        industryData.add("IT");
        industryData.add("制造业");
        industryData.add("金融");
        industryData.add("教育");
        industryData.add("医疗医药");
        industryData.add("建筑房地产");
        industryData.add("传媒");
        industryData.add("政府机关");
        industryData.add("其他");

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


    @OnClick({R.id.experience_back, R.id.experience_save, R.id.experienc_company_name, R.id.experience_industry, R.id.textView, R.id.experience_touxian, R.id.experience_time, R.id.experience_delete,R.id.experience_end_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.experience_back:
                finish();
                break;
            case R.id.experience_save:
                break;
            case R.id.experienc_company_name:
                experiencCompanyName.setCursorVisible(true);
                break;
            case R.id.experience_industry:
                industryDialog();
                break;
            case R.id.experience_touxian:
                experienceTouxian.setCursorVisible(true);
                break;
            case R.id.experience_time:
            case R.id.experience_end_time:
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
                experienceTime.setText(starttimeData.get(position) + "~");
            }
        });
        endtime.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                experienceEndTime.setText(endtimeData.get(position));
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

    //行业选择
    private void industryDialog() {
        Dialog industryDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_industry, null);
        WheelView wheelview = (WheelView) contentView.findViewById(R.id.industry_wheelview);
        wheelview.setWheelAdapter(new ArrayWheelAdapter(this)); // 文本数据源
        wheelview.setWheelData(industryData);
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
                experienceIndustry.setText(industryData.get(position));

            }
        });

        industryDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        industryDialog.getWindow().setGravity(Gravity.BOTTOM);
        industryDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        industryDialog.show();
    }

}
