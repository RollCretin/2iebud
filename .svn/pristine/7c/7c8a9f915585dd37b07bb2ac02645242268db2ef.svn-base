package com.hxjf.dubei.ui.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.bean.AlarmBean;
import com.hxjf.dubei.receiver.AlarmReceiver;
import com.hxjf.dubei.util.SPUtils;
import com.kyleduo.switchbutton.SwitchButton;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/6/23.
 */

public class ReadRemindActivity extends BaseActivity {
    @BindView(R.id.my_book_coupon_back)
    ImageView myBookCouponBack;
    @BindView(R.id.my_book_coupon_button)
    SwitchButton myBookCouponButton;
    @BindView(R.id.my_book_coupon_container)
    LinearLayout myBookCouponContainer;
    @BindView(R.id.my_book_coupon_time_remind)
    LinearLayout myBookCouponTimeRemind;
    private WheelView hourtime;
    private WheelView minutetime;
    ArrayList<String> hourtimeData;
    ArrayList<String> minutetimeData;
    private TextView minutecancel;
    private TextView minutedone;
    private TextView minuteweek;
    private int hour;
    private int minute;
    private ArrayList<Integer> weekint;
    private ArrayList<AlarmBean> alarmlist;
    private StringBuilder sb = new StringBuilder();
    ;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_read_remind;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);
        hourtimeData = new ArrayList<>();
        minutetimeData = new ArrayList<>();
        alarmlist = new ArrayList<>();
        weekint = new ArrayList();
        for (int i = 0; i < 24; i++) {
            hourtimeData.add(i + "");
        }
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutetimeData.add("0" + i);
            } else {
                minutetimeData.add("" + i);
            }
        }
        //初始化回显
        int alarmnumber = SPUtils.getInt(ReadRemindActivity.this, "alarmnumber", 0);
        for (int i = 0; i < alarmnumber; i++) {
            AlarmBean initbean = (AlarmBean) SPUtils.getBean(ReadRemindActivity.this, "alarmbean" + i);
            if (initbean != null) {
                View childview = View.inflate(ReadRemindActivity.this, R.layout.view_item_remind, null);
                TextView time = (TextView) childview.findViewById(R.id.read_remind_time);
                TextView state = (TextView) childview.findViewById(R.id.read_remind_time_state);
                TextView week = (TextView) childview.findViewById(R.id.read_remind_week);
                String timestr = new String(initbean.getHour() + ":" + minutetimeData.get(initbean.getMinute()));
                time.setText(timestr);
                state.setText(initbean.getState());
                String weekdatastr = initbean.getWeekdata();
                if (weekdatastr.length() > 20)
                    week.setText("每天");
                weekint = initbean.getWeeklist();
                myBookCouponContainer.addView(childview);
            }
            boolean read_remind = SPUtils.getBoolean(ReadRemindActivity.this, "read_remind", false);
            myBookCouponButton.setChecked(read_remind);
            if (read_remind) {
                myBookCouponTimeRemind.setVisibility(View.VISIBLE);
                myBookCouponContainer.setVisibility(View.VISIBLE);
            } else {
                myBookCouponTimeRemind.setVisibility(View.GONE);
                myBookCouponContainer.setVisibility(View.GONE);
            }


        }


        myBookCouponButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //开启阅读提醒
                    myBookCouponTimeRemind.setVisibility(View.VISIBLE);
                    myBookCouponContainer.setVisibility(View.VISIBLE);
                    SPUtils.putBoolean(ReadRemindActivity.this, "read_remind", true);
                    startRemind();
                } else {
                    //关闭阅读提醒
                    myBookCouponTimeRemind.setVisibility(View.GONE);
                    myBookCouponContainer.setVisibility(View.GONE);
                    SPUtils.putBoolean(ReadRemindActivity.this, "read_remind", false);
                    stopRemind();
                }
            }
        });
    }

    @OnClick({R.id.my_book_coupon_back, R.id.my_book_coupon_button, R.id.my_book_coupon_time_remind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_book_coupon_back:
                finish();
                break;
            case R.id.my_book_coupon_time_remind:
                //时间选择dialog
                timeDialog();
                break;
        }
    }

    //时间选择
    private void timeDialog() {
        final Dialog timeDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_minute, null);
        hourtime = (WheelView) contentView.findViewById(R.id.dialog_start_time);
        minutetime = (WheelView) contentView.findViewById(R.id.dialog_end_time);
        minutecancel = (TextView) contentView.findViewById(R.id.dialog_minute_cancel);
        minutedone = (TextView) contentView.findViewById(R.id.dialog_minute_done);
        minuteweek = (TextView) contentView.findViewById(R.id.dialog_minute_week);
        minutecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.dismiss();
            }
        });
        minutedone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = hourtime.getCurrentPosition();
                minute = minutetime.getCurrentPosition();
                String statestr = hour > 11 ? "下午" : "上午";
                String timestr = new String(hour + ":" + minutetimeData.get(minute));
                String weekstr = minuteweek.getText().toString();
                AlarmBean bean = new AlarmBean();
                bean.setHour(hour);
                bean.setMinute(minute);
                bean.setState(statestr);
                bean.setWeeklist(weekint);
                bean.setWeekdata(sb.toString());
                alarmlist.add(bean);
                int alarmnumber = SPUtils.getInt(ReadRemindActivity.this, "alarmnumber", 0) + 1;
                SPUtils.putBean(ReadRemindActivity.this, "alarmbean" + alarmlist.size(), bean);
                SPUtils.putInt(ReadRemindActivity.this, "alarmnumber", alarmnumber);

                //container中添加一条view
                View childview = View.inflate(ReadRemindActivity.this, R.layout.view_item_remind, null);
                TextView time = (TextView) childview.findViewById(R.id.read_remind_time);
                TextView state = (TextView) childview.findViewById(R.id.read_remind_time_state);
                TextView week = (TextView) childview.findViewById(R.id.read_remind_week);
                time.setText(timestr);
                state.setText(statestr);
                week.setText(weekstr);
                myBookCouponContainer.addView(childview);
                timeDialog.dismiss();
                startRemind();
            }
        });
        minuteweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekDialog();
            }
        });

        hourtime.setWheelAdapter(new ArrayWheelAdapter(this));
        minutetime.setWheelAdapter(new ArrayWheelAdapter(this));
        hourtime.setWheelData(hourtimeData);
        minutetime.setWheelData(minutetimeData);
        hourtime.setSkin(WheelView.Skin.Holo);
        minutetime.setSkin(WheelView.Skin.Holo);
        hourtime.setWheelSize(5);
        minutetime.setWheelSize(5);
        hourtime.setLoop(true);
        minutetime.setLoop(true);

        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#000000");
        style.textColor = Color.GRAY;
        style.selectedTextSize = 25;
        style.textAlpha = 0.6f;
        hourtime.setStyle(style);
        minutetime.setStyle(style);

        timeDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        timeDialog.getWindow().setGravity(Gravity.BOTTOM);
        timeDialog.show();
    }

    //星期选择
    private void weekDialog() {
        final Dialog weekDialog = new Dialog(this, R.style.myDialogTheme);
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_dialog_week, null);
        TextView back = (TextView) contentView.findViewById(R.id.dialog_week_return);
        final CheckBox monday = (CheckBox) contentView.findViewById(R.id.per_monday);
        final CheckBox tuesday = (CheckBox) contentView.findViewById(R.id.per_tuesday);
        final CheckBox wednesday = (CheckBox) contentView.findViewById(R.id.per_wednesday);
        final CheckBox thursday = (CheckBox) contentView.findViewById(R.id.per_thursday);
        final CheckBox friday = (CheckBox) contentView.findViewById(R.id.per_friday);
        final CheckBox saturday = (CheckBox) contentView.findViewById(R.id.per_saturday);
        final CheckBox sunday = (CheckBox) contentView.findViewById(R.id.per_sunday);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (monday.isChecked()) {
                    sb.append("周一 ");
                    weekint.add(1);
                }
                if (tuesday.isChecked()) {
                    sb.append("周二 ");
                    weekint.add(2);
                }
                if (wednesday.isChecked()) {
                    sb.append("周三 ");
                    weekint.add(3);
                }
                if (thursday.isChecked()) {
                    sb.append("周四 ");
                    weekint.add(4);
                }
                if (friday.isChecked()) {
                    sb.append("周五 ");
                    weekint.add(5);
                }
                if (saturday.isChecked()) {
                    sb.append("周六 ");
                    weekint.add(6);
                }
                if (sunday.isChecked()) {
                    sb.append("周日 ");
                    weekint.add(0);
                }
                minuteweek.setText(sb.toString());
                if (sb.toString().length() > 20) {
                    minuteweek.setText("每天");
                }
                weekDialog.dismiss();
            }
        });

        weekDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        weekDialog.getWindow().setGravity(Gravity.BOTTOM);
        weekDialog.show();
    }

    //开启提醒
    private void startRemind() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        long systemTime = System.currentTimeMillis();
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        //设置闹钟时间
        for (int i = 0; i < weekint.size(); i++) {
            mCalendar.set(Calendar.DAY_OF_WEEK, i);//设置周几
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);
            mCalendar.set(Calendar.SECOND, 0);
            mCalendar.set(Calendar.MILLISECOND, 0);
            long selectTime = mCalendar.getTimeInMillis();
            if (systemTime > selectTime) {
                mCalendar.add(Calendar.DAY_OF_WEEK, 1);
            }
            //AlarmReceiver.class为广播接受者
            Intent intent = new Intent(ReadRemindActivity.this, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(ReadRemindActivity.this, 0, intent, 0);
            //得到AlarmManager实例
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
            Date curDate = new Date(selectTime);//获取当前时间
            String str = formatter.format(curDate);
        }
    }

    //关闭提醒
    private void stopRemind() {

        Intent intent = new Intent(ReadRemindActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ReadRemindActivity.this, 0,
                intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
    }

}
