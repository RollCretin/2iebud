package com.hxjf.dubei.ui.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.mobileim.YWIMKit;
import com.hxjf.dubei.R;
import com.hxjf.dubei.base.BaseActivity;
import com.hxjf.dubei.network.ImLoginHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chen_Zhang on 2017/7/14.
 */

public class MyMessageActivity extends BaseActivity {
    @BindView(R.id.my_message_back)
    ImageView myMessageBack;
    @BindView(R.id.my_message_fragment)
    LinearLayout myMessageFragment;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_message;
    }

    @Override
    public void init() {
        super.init();
        ButterKnife.bind(this);

        YWIMKit imKit = ImLoginHelper.getInstance().getIMKit();
        Fragment privateFragment = imKit.getConversationFragment();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyMessageActivity.this);
        if(-1== sharedPreferences.getInt("IgnoreBatteryOpt", -1)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("IgnoreBatteryOpt", 1);
            editor.commit();

        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.my_message_fragment,privateFragment);
        fragmentTransaction.commit();
    }


    @OnClick(R.id.my_message_back)
    public void onClick() {
        finish();
    }

}
