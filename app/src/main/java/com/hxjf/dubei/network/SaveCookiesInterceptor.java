package com.hxjf.dubei.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Chen_Zhang on 2017/7/19.
 */

public class SaveCookiesInterceptor implements Interceptor {
    private static final String COOKIE_PREF = "cookies_prefs";
    private Context mContext;

    public SaveCookiesInterceptor(Context context) {
        mContext = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //set-cookie可能为多个
        if (!response.headers("Set-Cookie").isEmpty()) {
            List<String> cookies = response.headers("Set-Cookie");
            String cookie = encodeCookie(cookies);
            saveCookie(request.url().host(),cookie);
        }
        return response;
    }

    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set=new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if(set.contains(s))continue;
                set.add(s);

            }
        }

        Iterator<String> ite = set.iterator();
        while (ite.hasNext()) {
            String cookie = ite.next();
            sb.append(cookie).append(";");
        }

        int last = sb.lastIndexOf(";");
        if (sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }

        return sb.toString();
    }

    //保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
    private void saveCookie(String domain, String cookie) {
        SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        /*if (StringUtils.isEmpty(url)) {
            throw new NullPointerException("url is null.");
        }else{

            editor.putString(url, cookies);
        }*/


        if (!TextUtils.isEmpty(domain)) {
            if (sp.getString(domain,"").length() == 0){
                editor.putString(domain, cookie);
                Log.d("TAG", "intercept: 保存Cookie"+cookie);
            }
        }

        editor.apply();
    }


}
