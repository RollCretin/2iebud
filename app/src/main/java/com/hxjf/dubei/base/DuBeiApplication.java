package com.hxjf.dubei.base;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.wxlib.util.SysUtil;
import com.bugtags.library.Bugtags;
import com.hxjf.dubei.BuildConfig;
import com.hxjf.dubei.network.ConversationListUICustomSample;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.geometerplus.android.fbreader.FBReaderApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Chen_Zhang on 2017/7/19.
 */

public class DuBeiApplication extends FBReaderApplication {
    private  static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        //极光初始化sdk
        JPushInterface.setDebugMode(false);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);

//        Config.DEBUG = true;//友盟调试，发布时删除
        //初始化友盟sdk
        UMShareAPI.get(this);

        //阿里IM初始化
        final String APP_KEY = "24550795";
        SysUtil.setApplication(this);
        if(SysUtil.isTCMSServiceProcess(this)){
            return;
        }
        if(SysUtil.isMainProcess()){
            YWAPI.init(DuBeiApplication.this, APP_KEY);
        }
        //定制注册
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUICustomSample.class);

        //阿里的意见反馈
        FeedbackAPI.init(this, APP_KEY);

        //初始化tagbugs
        Bugtags.start(BuildConfig.DEBUG ? "a899d37870106393735ffb6b87a13d28" : "27d84dd5e909185def3ff642fee6e6ac", this, Bugtags.BTGInvocationEventNone);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    {
        PlatformConfig.setWeixin("wx80233bac71311239", "f05f241030c033f1723054fc5dc0ef51");
    }

    public static Context getContext() {
        return context;
    }
}
