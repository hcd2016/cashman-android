package com.innext.pretend.ptd_util;


import android.text.TextUtils;

import com.innext.xjx.app.App;
import com.innext.xjx.config.Constant;
import com.innext.xjx.http.HttpApi;
import com.innext.xjx.util.SpUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p>
 * Description：Retrofit工具类
 * </p>
 */
public class RetrofitUtil {

    /**
     * 超时时间（单位s）
     */
    private static final int DEFAULT_TIMEOUT = 10;

    public static <T> T create(Class<T> service) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(App.getConfig().baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .callFactory(genericClient());
        Retrofit mRetrofit = builder.build();
        return mRetrofit.create(service);
    }

    public static HttpApi create() {
        return create(HttpApi.class);
    }

    /**
     * 配置OkHttpClient
     */
    public static OkHttpClient genericClient() {
        Cache cache = new Cache(new File(App.getContext().getCacheDir(), "jsxjxCache"),
                1024 * 1024 * 100);
        //用于打印http信息的拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //添加全局统一请求头
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                String sessionid = SpUtil.getString(Constant.SHARE_TAG_SESSIONID);
                if (App.getConfig().getLoginStatus()&&!TextUtils.isEmpty(sessionid)){
                    builder.addHeader("Cookie", "SESSIONID="+sessionid);
                }
                Response response = chain.proceed(builder.build());

                //拦截需要的相应头
                if(response.header("RCaptchaKey") != null) {
                    SpUtil.putString("RCaptchaKey",response.header("RCaptchaKey"));
                }
                return response;
            }
        };



        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)   //缓存
                .retryOnConnectionFailure(false)
                .addInterceptor(interceptor)
                .addInterceptor(headerInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }
}
