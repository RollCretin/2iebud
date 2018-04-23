package com.hxjf.dubei.network;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;

/**
 * Created by Chen_Zhang on 2017/8/16.
 */

public class ImLoginHelper {
    private static ImLoginHelper sInstance = new ImLoginHelper();

    public static ImLoginHelper getInstance() {
        return sInstance;
    }

    public static  String APP_KEY = "24550795";
    private YWIMKit mIMKit;

    //获取SDK对象
    public YWIMKit getIMKit() {
        return mIMKit;
    }
    public String getAPP_KEY(){
        return APP_KEY;
    }

    public void setIMKit(YWIMKit imkit) {
        mIMKit = imkit;
    }

    public void initIMKit(String userId) {

        mIMKit = YWAPI.getIMKitInstance(userId,APP_KEY);
    }

}
