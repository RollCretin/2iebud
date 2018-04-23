package com.hxjf.dubei.util;

import android.content.Context;
import android.content.res.Resources;

import com.hxjf.dubei.R;

/**
 * Created by Chen_Zhang on 2017/11/8.
 */

public class ADFilterTool {
    public static boolean hasAd(Context context, String url){
        Resources res= context.getResources();
        String[]adUrls =res.getStringArray(R.array.adBlockUrl);
        for(String adUrl :adUrls){
            if(url.contains(adUrl)){
                return true;
            }
        }
        return false;
    }
}
