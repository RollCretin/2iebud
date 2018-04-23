package com.hxjf.dubei.network;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.hxjf.dubei.base.DuBeiApplication;
import com.hxjf.dubei.util.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Chen_Zhang on 2018/1/4.
 */

public class NoNetIntercepter implements Interceptor {
    private Context mContext;

    public NoNetIntercepter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtils.isNetworkReachable(DuBeiApplication.getContext())) {
            Looper.prepare();
            Toast.makeText(mContext, "网络异常，请检查网络...", Toast.LENGTH_LONG).show();
            Looper.loop();
        }
        Response response = chain.proceed(request);
        return response;
    }
}
