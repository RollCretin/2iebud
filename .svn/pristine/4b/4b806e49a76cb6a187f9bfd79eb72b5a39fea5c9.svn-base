package com.hxjf.dubei.network;

import android.support.v4.app.Fragment;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;

/**
 * Created by Chen_Zhang on 2017/8/16.
 */

public class ConversationListUICustomSample extends IMConversationListUI{
    public ConversationListUICustomSample(Pointcut pointcut) {
        super(pointcut);
    }
    //不显示标题栏
    @Override
    public boolean needHideTitleView(Fragment fragment) {
        return true;
    }
}
