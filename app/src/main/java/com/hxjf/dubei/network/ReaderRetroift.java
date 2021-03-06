package com.hxjf.dubei.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chen_Zhang on 2017/7/18.
 */

public class ReaderRetroift {
    private static ReaderRetroift sReaderRetroift;
//    线下环境
//    public static final String BASE_URL = "http://test2.17dubei.com/";//测试环境
//    public static final String IMAGE_URL = BASE_URL + "/image";//测试图片接口域名


//    线上环境
    public static final String BASE_URL = "https://web.17dubei.com";//线上环境
    public static final String IMAGE_URL = "http://image.17dubei.com";//线上图片接口域名

    //线上时间测试
//    public static final String BASE_URL = "http://backup.17dubei.com";
//    public static final String IMAGE_URL = "http://image.17dubei.com";
    private static Context mContext;
    private ReaderApi mApi;
    private Gson mGson = new GsonBuilder().setLenient().create();//设置宽大处理畸形的json

    private ReaderRetroift() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                connectTimeout(20, TimeUnit.SECONDS).//连接超时时间
                readTimeout(20,TimeUnit.SECONDS).
                writeTimeout(20,TimeUnit.SECONDS).
                addInterceptor(new SaveCookiesInterceptor(mContext)).
                addInterceptor(new AddCookiesInterceptor(mContext)).
                addInterceptor(new EnhancedCacheInterceptor()).
                addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("User-Agent", "Android")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                }).
                build();


        //使用Retrofit来实现Api接口 需要配置gson转换器
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .client(okHttpClient)
                .build();
        mApi = retrofit.create(ReaderApi.class);
    }


    public static ReaderRetroift getInstance(Context context) {
        mContext = context;
        if (sReaderRetroift == null) {
            synchronized (ReaderRetroift.class) {
                if (sReaderRetroift == null) {

                    sReaderRetroift = new ReaderRetroift();
                }
            }
        }
        return sReaderRetroift;
    }

    public ReaderApi getApi() {
        return mApi;
    }


}
