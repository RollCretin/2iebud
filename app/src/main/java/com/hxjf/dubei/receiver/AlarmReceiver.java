package com.hxjf.dubei.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Chen_Zhang on 2017/6/27.
 * 阅读提醒
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO
        Toast.makeText(context,"闹钟响了",Toast.LENGTH_LONG).show();
    }
}
