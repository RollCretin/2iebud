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
 * Created by Chen_Zhang on 2017/12/19.
 * 新发现的Retorfit
 */

public class NewDiscoveryRetrofit {
    private static NewDiscoveryRetrofit sReaderRetroift;

    //线下环境
//    public static final String BASE_URL = "http://test2.17dubei.com";//测试环境
//    public static final String IMAGE_URL = "http://test2.17dubei.com/image";//测试图片接口域名

    //线上环境
    public static final String BASE_URL = "http://web.17dubei.com/";//线上
    public static final String IMAGE_URL = "http://image.17dubei.com";//线上图片接口域名



    private ReaderApi mApi;

    private Gson mGson = new GsonBuilder().setLenient().create();//设置宽大处理畸形的json
    private static Context mContext;

    public static NewDiscoveryRetrofit getInstance(Context context) {
        mContext = context;
        if (sReaderRetroift == null) {
            synchronized (NewDiscoveryRetrofit.class) {
                if (sReaderRetroift == null) {
                    sReaderRetroift = new NewDiscoveryRetrofit();
                }
            }
        }
        return sReaderRetroift;
    }

    private NewDiscoveryRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                connectTimeout(10, TimeUnit.SECONDS).//连接超时时间
                readTimeout(10,TimeUnit.SECONDS).
                writeTimeout(10,TimeUnit.SECONDS).
                addInterceptor(new SaveCookiesInterceptor(mContext)).
                addInterceptor(new AddCookiesInterceptor(mContext))
                .addInterceptor(new Interceptor() {
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

    public ReaderApi getApi() {
        return mApi;
    }


}
