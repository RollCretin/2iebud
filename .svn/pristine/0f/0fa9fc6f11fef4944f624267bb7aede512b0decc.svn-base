package com.hxjf.dubei.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.hxjf.dubei.R;

/**
 * Created by Administrator on 2018/5/3.
 */

public class SignInDialog extends Dialog {
    private static final int FLAG_DISMISS = 1;
    private boolean flag = true;

    public SignInDialog(@NonNull Context context) {
        this(context,0);
    }

    public SignInDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sign_in_dialog, null);
        super.setContentView(view);
    }

    @Override
    public void show() {
        super.show();
        mThread.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        flag = false;
    }

    private Thread mThread = new Thread(){
        @Override
        public void run() {
            super.run();

            while(flag){
                try {
                    Thread.sleep(5000);
                    Message msg = mHandler.obtainMessage();
                    msg.what = FLAG_DISMISS;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == FLAG_DISMISS)
                dismiss();
        }

    };
}
