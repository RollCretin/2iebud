package com.hxjf.dubei.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 * Created by Chen_Zhang on 2017/8/4.
 */

public class TimeUtil {
    /**
     * 当前时间距离指定时间0点的   天数：小时：分钟：秒
     * 开始时间 - 当前时间
     * @param startTime
     * @return
     */
    public static  String[] getDifference(String  startTime){
        String[] time = new String[4];
        //获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        calendar.setTimeInMillis(currentTimeMillis);
        //解析开始挑战时间
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        try {
            Date parse = format.parse(startTime);
            long starttime = parse.getTime();
            int daycountNum = (int) ((starttime - currentTimeMillis) / (1000 * 60 * 60 * 24));//相差天数
            String daycountStr = String.valueOf(daycountNum);
            int hourNum = (int) ((starttime - currentTimeMillis) / (1000 * 60 * 60)) % 24;//小时数
            String hourStr = String.valueOf(hourNum);
            int minuteNum = (int) ((starttime - currentTimeMillis) / (1000 * 60 )) % 60;//分钟数
            String minuteStr = String.valueOf(minuteNum);
            int secondNum = (int) ((starttime - currentTimeMillis) / (1000)) % 60;//分钟数
            String secondStr = String.valueOf(secondNum);
            if (hourNum < 10){
                hourStr = "0"+hourNum;
            }
            if (minuteNum < 10){
                minuteStr = "0"+minuteNum;
            }
            if (secondNum < 10){
                secondStr = "0"+secondNum;
            }
            time = new String[]{daycountStr,hourStr,minuteStr,secondStr};
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static  String getCurrentTime(){
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        calendar.setTimeInMillis(currentTimeMillis);
        //解析开始挑战时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(calendar.getTime());
    }

    /**
     *
     * @param startTime
     * @return
     * 当前时间距离指定时间24点的   天数：小时：分钟：秒
     * 开始时间 - 当前时间
     */
    public static  String[] getDifference2(String  startTime){
        String[] time = new String[4];
        //获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        calendar.setTimeInMillis(currentTimeMillis);
        //解析开始挑战时间
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        try {
            Date parse = format.parse(startTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(parse);
            calendar2.add(calendar.DATE,1);
            parse = calendar2.getTime();
            long starttime = parse.getTime();
            int daycountNum = (int) ((starttime - currentTimeMillis) / (1000 * 60 * 60 * 24));//相差天数
            String daycountStr = String.valueOf(daycountNum);
            int hourNum = (int) ((starttime - currentTimeMillis) / (1000 * 60 * 60)) % 24;//小时数
            String hourStr = String.valueOf(hourNum);
            int minuteNum = (int) ((starttime - currentTimeMillis) / (1000 * 60 )) % 60;//分钟数
            String minuteStr = String.valueOf(minuteNum);
            int secondNum = (int) ((starttime - currentTimeMillis) / (1000)) % 60;//分钟数
            String secondStr = String.valueOf(secondNum);
            if (hourNum < 10){
                hourStr = "0"+hourNum;
            }
            if (minuteNum < 10){
                minuteStr = "0"+minuteNum;
            }
            if (secondNum < 10){
                secondStr = "0"+secondNum;
            }
            time = new String[]{daycountStr,hourStr,minuteStr,secondStr};
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

}
